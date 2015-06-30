// $Id: Base.nc,v 1.1.1.1 2005/05/10 23:37:05 rsto99 Exp $

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

includes BASE;

configuration Base {
}
implementation {

  components Main,TimeSyncC,GenericComm,BaseM, TimerC, LedsC,FramerM;

  Main.StdControl -> TimerC;
  Main.StdControl -> BaseM;
  Main.StdControl -> GenericComm;
  Main.StdControl -> TimeSyncC;
    
  BaseM.RcvSyncMsg -> GenericComm.ReceiveMsg[AM_SYNCMSG];  
  BaseM.TxSyncMsg -> GenericComm.SendMsg[AM_SYNCMSG];  
  
  BaseM.RcvReportMsg -> GenericComm.ReceiveMsg[AM_REPORTMSG];  
  BaseM.TxReportMsg -> GenericComm.SendMsg[AM_REPORTMSG]; 
  
  BaseM.RcvConfigMsg -> GenericComm.ReceiveMsg[AM_CONFIGMSG]; 
  BaseM.TxConfigMsg -> GenericComm.SendMsg[AM_CONFIGMSG];     

  BaseM.RcvReportAckMsg -> GenericComm.ReceiveMsg[AM_REPORTACKMSG]; 
  BaseM.TxReportAckMsg -> GenericComm.SendMsg[AM_REPORTACKMSG];     
         
  BaseM.Timer -> TimerC.Timer[unique("Timer")];
  BaseM.Leds -> LedsC;
  BaseM.GlobalTime -> TimeSyncC.GlobalTime;
  BaseM.UARTTimeSync ->FramerM;
  FramerM.GlobalTime -> TimeSyncC.GlobalTime;
  FramerM.Leds -> LedsC;  
}
