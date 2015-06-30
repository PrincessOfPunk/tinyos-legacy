// $Id: UARTFramedPacket.nc,v 1.1.1.1 2007/08/22 00:43:54 konradlorincz Exp $

/*									tab:4
 * "Copyright (c) 2000-2003 The Regents of the University  of California.  
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
 * Copyright (c) 2002-2003 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704.  Attention:  Intel License Inquiry.
 */
/*
 *
 * Authors:		Phil Buonadonna
 * Revision:		$Id: UARTFramedPacket.nc,v 1.1.1.1 2007/08/22 00:43:54 konradlorincz Exp $
 *
 */

/**
 * @author Phil Buonadonna
 */


module UARTFramedPacket
{
  provides {
    interface StdControl as Control;
    interface BareSendMsg as Send;
    interface ReceiveMsg as Receive;
  }
}
implementation
{
//   components FramerM, FramerAckM, UART;

//   Control = FramerM;
//   Send = FramerM;
//   Receive = FramerAckM;

//   FramerAckM.TokenReceiveMsg -> FramerM;
//   FramerAckM.ReceiveMsg -> FramerM;

//   FramerM.ByteControl -> UART;
//   FramerM.ByteComm -> UART;


    command result_t Control.init()  {return SUCCESS;}
    command result_t Control.start() {return SUCCESS;}
    command result_t Control.stop()  {return SUCCESS;}


    command result_t Send.send(TOS_MsgPtr msg) {return FAIL;}

}