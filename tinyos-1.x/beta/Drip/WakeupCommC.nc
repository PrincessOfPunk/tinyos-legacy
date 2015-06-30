//$Id: WakeupCommC.nc,v 1.3 2005/06/14 18:19:35 gtolle Exp $

/*
 * Copyright (c) 2000-2005 The Regents of the University  of California.  
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
 *
 */

/**
 * WakeupComm takes a message to be sent and sends it multiple times
 * in succession, so as to ensure that it will be received by a node
 * that is periodically waking up to check the radio channel.
 * <p>
 * Drip uses this as part of a sleep-wake power management system.
 *
 * @author Gilman Tolle <get@cs.berkeley.edu>
 */
includes WakeupComm;

configuration WakeupCommC {
  provides interface SendMsg[uint8_t id];
}
implementation {
  components WakeupCommM, GenericComm, TimerC;
  
  SendMsg = WakeupCommM.WakeupSendMsg;

  WakeupCommM.Timer -> TimerC.Timer[unique("Timer")];
  WakeupCommM.SendMsg -> GenericComm;
}