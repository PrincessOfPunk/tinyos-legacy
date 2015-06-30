/*
* Copyright (c) 2006 Stanford University.
* All rights reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
* - Redistributions of source code must retain the above copyright
*   notice, this list of conditions and the following disclaimer.
* - Redistributions in binary form must reproduce the above copyright
*   notice, this list of conditions and the following disclaimer in the
*   documentation and/or other materials provided with the
*   distribution.
* - Neither the name of the Stanford University nor the names of
*   its contributors may be used to endorse or promote products derived
*   from this software without specific prior written permission
*
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
* ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
* LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS
* FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL STANFORD
* UNIVERSITY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
* INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
* (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
* HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
* STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
* ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
* OF THE POSSIBILITY OF SUCH DAMAGE.
*/
/**
 * @author Brano Kusy (branislav.kusy@gmail.com)
 */
#include "enalabCam.h"
#include "cameraJpegTest.h"
#include "sdram.h"
#include "jpeghdr.h"

module cameraJpegTestM
{
  uses {
    interface Boot;
    interface Leds;
    interface SendBigMsg;
    interface Jpeg;
    interface Timer<TMilli> as Timer0;
    interface EnalabCam;
    interface Init as CameraInit;
    interface SplitControl as SerialControl;
    interface Packet;
    //interface Receive as CmdReceive;
    interface Send as ImgStatSend;
    //interface AMSend as ImgStatSend;
    //dbg
    interface HplOV7649Advanced as OVAdvanced;
    interface StdControl as DisseminationControl;
    interface DisseminationValue<cmd_msg_t> as DisseminationCmdMsgReceive;
    interface DisseminationUpdate<cmd_msg_t> as DisseminationCmdMsgSend;

    interface StdControl as RoutingControl;
    interface RootControl;
    interface CollectionPacket;
    interface CtpInfo;
    interface CtpCongestion;
  }
}
implementation
{
  norace frame_t* frame;
  img_stat_t img_stat;
  message_t img_stat_msg;
  message_t pxa_dbg_msg;
  //----------------------------------------------------------------------------
  // StdControl Interface Implementation
  //----------------------------------------------------------------------------
  event void Boot.booted()
  {
    img_stat.width=320;
    img_stat.height=240;
    img_stat.type=0;
    img_stat.node_id=TOS_NODE_ID;
    call CameraInit.init();
    call SerialControl.start();
		call Jpeg.init();
    //call Timer0.startOneShot(1000);
    //call Timer0.startPeriodic(1000);
  }
  event void SerialControl.startDone(error_t result) {
    call DisseminationControl.start();
    call RoutingControl.start();
  }

  event void SerialControl.stopDone(error_t result) {}

  void *sendAddr;

  task void sendImg()
  {
    img_stat_t* tx_data = (img_stat_t*)call ImgStatSend.getPayload(&img_stat_msg, sizeof(img_stat_t));

    memcpy(tx_data,&img_stat,sizeof(img_stat_t));
    call ImgStatSend.send(&img_stat_msg, sizeof(img_stat_t));
    call Leds.led2Toggle();
  }

  event void ImgStatSend.sendDone(message_t* bufPtr, error_t error) {
    call Leds.led0Toggle();
    call SendBigMsg.send(sendAddr,img_stat.data_size);
  }

  event void SendBigMsg.sendDone(error_t success){};

  task void startCompression()
  {
    void *addrJpeg = (void *)JPEG_FRAME_ADDRESS;//jpegBuffer;//&(frame->buf[frame->size]);
    uint16_t jpegMaxSize = 65000;
    if (img_stat.type&IMG_COL)
      //by this time, buffer should be fixed and contain 3 bytes per pixel (r,g,b)
      jpegMaxSize = call Jpeg.encodeColJpeg(frame->buf,addrJpeg,jpegMaxSize,320,240,9,3);
    else
      //buffer reading tweak: only every 2nd byte is read
      jpegMaxSize = call Jpeg.encodeJpeg(&frame->buf[1],addrJpeg,jpegMaxSize,320,240,9,2,0);
    img_stat.timeProc += call Timer0.getNow();
    sendAddr=addrJpeg;
    img_stat.data_size=jpegMaxSize;
    post sendImg();
  }

  task void startProcessing()
  {
    img_stat.timeProc += call Timer0.getNow();
    sendAddr=frame->buf;
    img_stat.data_size=frame->header->size;
    post sendImg();
  }

  task void fixAcqBuffer()
  {
		int32_t i;

    if (!(img_stat.type&IMG_COL))
    {//BW JPG encoder can read every second byte, no fix required
      if (!(img_stat.type&IMG_JPG))
      {
    		for (i = 0; i < frame->header->size; i++)
    			frame->buf[i] = frame->buf[2*i+1];
    		frame->header->size /= 2;
    	}
    }
		else
    {
      if (img_stat.type&IMG_JPG)
      {//COL non-JPG images don't need fix, it's more efficient to xmit 2 bytes than 3
    		for (i = (frame->header->size/2-1); i >=0; i--)
    		{
    			uint8_t b1 = frame->buf[2*i];
    			uint8_t b2 = frame->buf[2*i+1];
    			frame->buf[3*i] = b2&0xF8;//red
    			frame->buf[3*i+1] = ((b2&0x07)<<5) | ((b1&0xE0)>>3);//blue
    			frame->buf[3*i+2] = (b1&0x1F)<<3;//green
    		}
    		frame->header->size = frame->header->size/2*3;
    	}
    }

		img_stat.timeAcq += call Timer0.getNow();
		img_stat.timeProc = -call Timer0.getNow();

    if (img_stat.type&IMG_JPG)
      post startCompression();
    else
      post startProcessing();
  }

  event void Timer0.fired()
  {
      //call Leds.led2Toggle();
      //post fixAcqBuffer();
  }

  async event void EnalabCam.acquireDone()
  {
    call Leds.led1Toggle();
    //call Timer0.startOneShot(10);
      post fixAcqBuffer();
  }

  task void acquireTask() {
    img_stat.timeAcq = -call Timer0.getNow();
    if (!(img_stat.type&IMG_COL))
      frame = call EnalabCam.acquire(COLOR_UYVY, (void*)BASE_FRAME_ADDRESS);
    else
      frame = call EnalabCam.acquire(COLOR_RGB565, (void*)BASE_FRAME_ADDRESS);
  }

  event void DisseminationCmdMsgReceive.changed(){
      const cmd_msg_t* cmdMsg = call DisseminationCmdMsgReceive.get();
      if(cmdMsg->node_id==TOS_NODE_ID){
      if (cmdMsg->cmd == 0) {
        img_stat.type&=~IMG_COL;
        img_stat.type&=~IMG_JPG;
        call Leds.led1Off();
        call Leds.led0Off();
        post acquireTask();
      }
      else if (cmdMsg->cmd == 1) {
        img_stat.type|=IMG_COL;
        img_stat.type&=~IMG_JPG;
        call Leds.led1On();
        call Leds.led0Off();
        post acquireTask();
      }
      else if (cmdMsg->cmd == 2) {
        img_stat.type&=~IMG_COL;
        img_stat.type|=IMG_JPG;
        call Leds.led1Off();
        call Leds.led0On();
        post acquireTask();
      }
  		else if (cmdMsg->cmd == 3) {
        img_stat.type|=IMG_COL;
        img_stat.type|=IMG_JPG;
        call Leds.led1On();
        call Leds.led0On();
  			post acquireTask();
  		}
      else if (cmdMsg->cmd == 5) {
        call Leds.led0Toggle();
        call SendBigMsg.resend(frame->buf,cmdMsg->val1, cmdMsg->val2);
      }
    }
  }
}
