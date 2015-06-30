/*									tab:4
 *  IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.  By
 *  downloading, copying, installing or using the software you agree to
 *  this license.  If you do not agree to this license, do not download,
 *  install, copy or use the software.
 *
 *  Intel Open Source License 
 *
 *  Copyright (c) 2002 Intel Corporation 
 *  All rights reserved. 
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 *	Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *	Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *      Neither the name of the Intel Corporation nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *  PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE INTEL OR ITS
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 */
/*
 *
 * Authors:		Su Ping <sping@intel-research.net>
 * Date last modified:  
 *
 */


includes TimeSyncMsg;
includes TimeSync;
includes TosTime;
includes SendTime;

module InitialSyncM {
    provides {
        interface StdControl;
    }
    uses {
        interface Time;
	interface TimeSet;
        interface TimeUtil;
        interface Leds;
	interface StdControl as TimeControl;
	interface SendMsg as SendSyncMsg;
        interface SendMsg as SendTime;
        interface ReceiveMsg as TimeSyncReceive;
	interface StdControl as CommControl;
        interface RadioTiming ;
	interface AbsoluteTimer as AbsoluteTimer0;
        interface StdControl as AtimerControl;
    }
}
implementation
{
    bool auto_correct ;
    bool state; 
    TOS_Msg RxBuffer, TxBuffer;
    TOS_MsgPtr pRx, pSend;
    bool sendPending;
    task void sendSyncTask();
    tos_time_t t0, t1;
    uint16_t recvCounter;

    void task debugTime() {
        bool temp;
        struct TimeResp *pdata;

        if (!sendPending) {
            pdata = (struct TimeResp *)pSend->data;
            pdata->source_addr = TOS_LOCAL_ADDRESS;
            dbg(DBG_USR1, "t=\%x, \%x\n", t0.high32, t0.low32);
            temp = TOSH_interrupt_disable();
            pdata->timeH = t0.high32 ; // test should be time.high32
            pdata->timeL = t0.low32;
            if (temp) TOSH_interrupt_enable();
            // send the msg now
            sendPending = call SendTime.send(TOS_UART_ADDR, sizeof(struct TimeResp), pSend);
        }
    }


    inline uint32_t abd(uint32_t a, uint32_t b) {
    	if (a>b) return a-b;
    	else return b-a ;
    }
    /**
     * Receive a time sync message 
     * extract the time from the message
     * adjust the lst byte of time with message time stamp (msg->time)
     * set System time to the new value if in UNSNCED state
     * or adjust local time if syned before.
     **/

    void timeSyncTask( ) {
        uint16_t now, macRxTime;
        int32_t  delta;
        char temp;
        struct TimeSyncMsg  msg; 
        struct TimeSyncMsg * pmsg= (struct TimeSyncMsg *) pRx->data;

        temp = TOSH_interrupt_disable();

        msg.source_addr= pmsg->source_addr;
        msg.timeL= pmsg->timeL;
        msg.timeH= pmsg->timeH;

        macRxTime = pRx->time;
        if (temp)  TOSH_interrupt_enable(); 

        if (TOS_LOCAL_ADDRESS==0) {
            temp = TOSH_interrupt_disable();
            t0 = call Time.get();
            if (temp) TOSH_interrupt_enable();
            //call Leds.greenToggle();
            post debugTime();
        }
        else if (TOS_LOCAL_ADDRESS==1) {
            // receiver side delay calculation
            temp = TOSH_interrupt_disable();
            now = call  RadioTiming.currentTime(); // currenTime is from 4 MHz timer1
            if (now >= macRxTime)  delta = now - macRxTime;
            else delta = 0x10000 - pRx->time + now  ;
	    // convert delta to us
	    delta >>= 2;
            delta += TX_DELAY;

            if (msg.timeL +delta > msg.timeL) {
	        msg.timeL += delta ;
	    } else {
  	        msg.timeL += delta ;
	        msg.timeH++;
	    }
            call TimeSet.set(call TimeUtil.create(msg.timeH , msg.timeL));
            if (temp) TOSH_interrupt_enable();
            call Leds.greenToggle();
            post sendSyncTask() ;
        } 
        else if (TOS_LOCAL_ADDRESS==2) {
            
            if (msg.source_addr==1 ) {
                temp = TOSH_interrupt_disable();
                now = call  RadioTiming.currentTime(); // currenTime
                if (now >= macRxTime)  delta = now - macRxTime;
                else delta = 0x10000 - macRxTime + now  ;
                // convert delta to us
                delta >>= 2;
                delta += TX_DELAY;

                if (msg.timeL +delta > msg.timeL) {
                    msg.timeL += delta ;
                } else {
                    msg.timeL += delta ;
                    msg.timeH++;
                }
                t0.low32 = msg.timeL;
                t0.high32 = msg.timeH;
                if (temp) TOSH_interrupt_enable();
                call Leds.greenToggle();
                post debugTime();
            } else  {
                call Leds.yellowToggle(); // temp 
            }
        }
    }        

    event result_t SendSyncMsg.sendDone(TOS_MsgPtr msg, result_t success) {
        call Leds.yellowToggle();
        dbg(DBG_USR1, "time sync msg sent\n");
        sendPending = FALSE;
        /*
        if (TOS_LOCAL_ADDRESS==1) {
        // read time and send it out
            t0 = call Time.get();
            debugTime();
        }
        */
        return SUCCESS;
    } 

    event result_t SendTime.sendDone(TOS_MsgPtr msg, result_t success) {
        call Leds.redToggle();
        sendPending = FALSE;
        return SUCCESS;
    } 
    /**
     * send a time sync message
     **/
    task void sendSyncTask() {
        TOS_MsgPtr pmsg = &TxBuffer;
        struct TimeSyncMsg * pdata ;
        if (!sendPending) {
        pdata = (struct TimeSyncMsg *) pmsg->data;
	dbg(DBG_USR1, "InitalSync.sendSync\n");
        pdata->source_addr = TOS_LOCAL_ADDRESS;
        //if ( state == MASTER)
        // send the msg now
            sendPending = call SendSyncMsg.send(TOS_BCAST_ADDR, sizeof(struct TimeSyncMsg), pmsg);      
        }
       
    }  

    command result_t StdControl.init() {
        sendPending = FALSE;
	pSend = &TxBuffer; 
	t1.high32 =0; t1.low32=0;
        recvCounter =0;
        if (TOS_LOCAL_ADDRESS==0) state= MASTER;
        else state = SLAVE_UNSYNCED;
        call CommControl.init();
        call TimeControl.init();
	call AtimerControl.init();
        return SUCCESS;
    }

    command result_t StdControl.start() {
        call CommControl.start() ;
        call TimeControl.start(); 
	call AtimerControl.start();

	if (state == MASTER) {
            call TimeSet.set(t1);
            t1 = call TimeUtil.addUint32(t1, 0x500000);
	    call AbsoluteTimer0.set(t1);
            //sendSyncTask();
	}
        return SUCCESS;
    }

 
    /** 
     *  @return Always return <code>SUCCESS</code>
     **/
    command result_t StdControl.stop() {
        call TimeControl.stop();
        return call CommControl.stop() ;
    }

    void timerStart() {
	result_t retval; 
	t1 = call TimeUtil.addUint32(t1, 0x200000); 
        
	// start a timer so that we can periodically send time sync msg
	retval = call AbsoluteTimer0.set(t1);
        if (retval) dbg(DBG_USR1, "ATimer start successfully\n");
        else {
            dbg(DBG_USR1, "ATimer start failed\n");
            //call Leds.redToggle();
        }
    }

    /** 
     *  Timer expired Event Handler 
     *   
     *  @return Alway return <code>SUCCESS</code>
     **/
    event result_t AbsoluteTimer0.fired() {
        dbg(DBG_USR1, "TimeSyncM.Timer.expired\n");
	// send another time sync msg
        //if (TOS_LOCAL_ADDRESS==0) 
            post sendSyncTask();        
        // restart timer
        timerStart();
        //call Leds.redToggle(); // Master toggles its red led when timer fires.
	return SUCCESS;	
    }

    /**
     * Receive a time sync message 
     * check the type field. if type is TIMESYNC_REQUEST
     * call TimeSync.timeSync
     * else if type is TIME REQUEST, send our current time back  
     * 
     **/

    event TOS_MsgPtr TimeSyncReceive.receive(TOS_MsgPtr msg) {
        TOS_MsgPtr p = pRx;
	pRx = msg; 
        //recvCounter++; 
	timeSyncTask();
        return p; // keep msg and return the other buffer. 
    } 

}
