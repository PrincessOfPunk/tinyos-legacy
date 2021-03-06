/* "Copyright (c) 2000-2002 The Regents of the University  of California.  
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 * 
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF
 * CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS."
 */
/*
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
 */

/**
 * This is a wrapper to the SendMsg interface that internally queues
 * messages for transmission, so that SendMsg.send() always succeeds
 * (unless another command is concurrently attempting an enqueue).
 */
module QueuedSend {
  provides interface StdControl;
  provides interface SendMsg[uint8_t id];
  uses interface SendMsg as RealSendMsg[uint8_t id];
  uses interface Leds;
  uses event void sendFail(uint16_t destaddr);
  uses event void sendSucceed(uint16_t destaddr);
}
implementation {

  enum {
    MESSAGE_QUEUE_SIZE = 8,
    MAX_RETRANSMIT_COUNT = 10
  };

  struct _msgq_entry {
    uint16_t address;
    uint8_t length;
    uint8_t id;
    uint8_t xmit_count;
    struct TOS_Msg msg;
  } msgqueue[MESSAGE_QUEUE_SIZE];

  bool enqueue_busy;
  int enqueue_next, dequeue_next;
  bool retransmit;

  // For enqueue task
  uint16_t tmp_address;
  uint8_t  tmp_length;
  uint8_t  tmp_id;
  TOS_MsgPtr tmp_msg;

  command result_t StdControl.init() {
    int i;
    for (i = 0; i < MESSAGE_QUEUE_SIZE; i++) {
      msgqueue[i].length = 0;
    }
    retransmit = TRUE;  // Set to FALSE to disable retransmission
    //retransmit = FALSE;  // Set to TRUE to enable retransmission
    enqueue_busy = FALSE;
    enqueue_next = 0;
    dequeue_next = 0;
    msgqueue[dequeue_next].length = 0;
    return SUCCESS;
  }

  command result_t StdControl.start() {
    return SUCCESS;
  }
  command result_t StdControl.stop() {
    return SUCCESS;
  }

/* Queue data structure
   Circular Buffer
   enqueue_next indexes first empty entry
   buffer full if incrementing enqueue_next would wrap to dequeue
   empty if dequeue_next == enqueue_next
         or msgqueue[dequeue_next].length == 0
*/

  task void enqueueTask() {
    uint8_t id;
    dbg(DBG_USR1, "QueuedSend: queue msg enq 0x%x deq 0x%x\n", enqueue_next, dequeue_next);
    if (((enqueue_next + 1) % MESSAGE_QUEUE_SIZE) == dequeue_next) {
      // Fail if queue is full
      signal SendMsg.sendDone[tmp_id](tmp_msg, FAIL);
      enqueue_busy = FALSE;
      return;
    }
    msgqueue[enqueue_next].address = tmp_address;
    msgqueue[enqueue_next].length = tmp_length;
    msgqueue[enqueue_next].id = tmp_id;
    msgqueue[enqueue_next].xmit_count = 0;
    // Copy message
    memcpy(&msgqueue[enqueue_next].msg, tmp_msg, sizeof(struct TOS_Msg));
    msgqueue[enqueue_next].msg.ack = 0;
    enqueue_next++; enqueue_next %= MESSAGE_QUEUE_SIZE;

    dbg(DBG_USR1, "QueuedSend: Successfully queued msg to 0x%x\n", tmp_address);

    // Try to send next message (ignore xmit_count)
    if (msgqueue[dequeue_next].length != 0) {
      call Leds.greenToggle();
      dbg(DBG_USR1, "QueuedSend: sending msg (0x%x)\n", dequeue_next);
      id = msgqueue[dequeue_next].id;
      if (!(call RealSendMsg.send[id](msgqueue[dequeue_next].address, 
	  msgqueue[dequeue_next].length, &msgqueue[dequeue_next].msg))) {
         dbg(DBG_USR1, "QueuedSend: send request failed. stuck in queue\n");
      }
    }
    enqueue_busy = FALSE;
    signal SendMsg.sendDone[tmp_id](tmp_msg, SUCCESS);  
  }

  command result_t SendMsg.send[uint8_t id](uint16_t address, uint8_t length, TOS_MsgPtr msg) {
    if (!enqueue_busy) {
      dbg(DBG_USR1, "QueuedSend: %d Queueing msg: addr 0x%x len 0x%x 0x%x\n",
	id, address, length, msg);
      tmp_address = address;
      tmp_length = length;
      tmp_msg = msg;
      tmp_id = id;
      enqueue_busy = TRUE;
      post enqueueTask();
      return SUCCESS;
    } else {
      dbg(DBG_USR1, "QueuedSend: enqueue busy, not queueing\n");
      return FAIL;
    }
  }

  event result_t RealSendMsg.sendDone[uint8_t id](TOS_MsgPtr msg, result_t success) {
    if (msg != &msgqueue[dequeue_next].msg) {
      return FAIL;		// This would be internal error
    }
    // filter out non-queuesend msgs
    
    if ((msg->ack == 1) || (msgqueue[dequeue_next].address == BASE_ADDRESS)) {
      signal sendSucceed(msgqueue[dequeue_next].address);
      msgqueue[dequeue_next].length = 0;
      dequeue_next++; dequeue_next %= MESSAGE_QUEUE_SIZE;
    }
    else {
      call Leds.redToggle();
      if ((!retransmit) ||
	  (++(msgqueue[dequeue_next].xmit_count) > MAX_RETRANSMIT_COUNT)) {
	// Tried to send too many times, just drop
	signal sendFail(msgqueue[dequeue_next].address);
	msgqueue[dequeue_next].length = 0;
	dequeue_next++; dequeue_next %= MESSAGE_QUEUE_SIZE;
      } 
    }
    
    // Send next
    if (msgqueue[dequeue_next].length != 0) {
      dbg(DBG_USR1, "QueuedSend: Sending queued msg to 0x%x\n", msgqueue[dequeue_next].address);
      call Leds.greenToggle();
      id = msgqueue[dequeue_next].id;
      call RealSendMsg.send[id](msgqueue[dequeue_next].address, 
				msgqueue[dequeue_next].length, &msgqueue[dequeue_next].msg);
    }
    
    return SUCCESS;
  }
  
  
  default event result_t SendMsg.sendDone[uint8_t id](TOS_MsgPtr msg, result_t success) {
    return SUCCESS;
  }
}
