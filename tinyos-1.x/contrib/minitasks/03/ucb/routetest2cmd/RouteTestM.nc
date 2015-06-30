/*									
 * "Copyright (c) 2000-2002 The Regents of the University  of California.  
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
 * Author: Naveen Sastry, nks
 */

module RouteTestM {
  provides interface StdControl;
  uses interface Leds;  
  uses interface ReceiveMsg as RBRecv;
  uses interface SendMsg as RBSend;  
} implementation {
  TOS_Msg msg;
  command result_t StdControl.init ()
    {
      return SUCCESS;
    }

  command result_t StdControl.start ()
    {
      return SUCCESS;
    }

  command result_t StdControl.stop ()
    {
      return SUCCESS;
    }

  event TOS_MsgPtr RBRecv.receive (TOS_MsgPtr pMsg) {
    RebroadcastCmd * cmd = (RebroadcastCmd*)pMsg->data;
    dbg(DBG_USR1, "** RT: Rebroadcasting a command to %d\n",
        ((RebroadcastCmd*)pMsg->data)->dest);        
    call Leds.greenToggle();
    memcpy (msg.data, &((RebroadcastCmd*)pMsg->data)->cmd,
            sizeof (RT2Command));
    call Leds.yellowToggle();
    call RBSend.send(((RebroadcastCmd*)pMsg->data)->dest,
                     sizeof(RT2Command),&msg);
    
    return pMsg;
  }

  event result_t RBSend.sendDone(TOS_MsgPtr pmsg, result_t success) {
    return SUCCESS;
  }


}
