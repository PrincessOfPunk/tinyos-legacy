// $Id: 

/*									tab:4
 * "Copyright (c) 2000-2004 The Regents of the University  of California.  
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
 * Copyright (c) 2002-2004 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704.  Attention:  Intel License Inquiry.
 */

includes MaxClique;
includes MultiHop;

configuration MaxClique {}
implementation {
  components Main, MaxCliqueM, LedsC, TimerC, RandomLFSR;
  components WMEWMAMultiHopRouter as Router;
  components GenericCommPromiscuous as GenericComm;
  components QueuedSend;
  
  Main.StdControl -> MaxCliqueM.StdControl;

  MaxCliqueM.SubControl -> TimerC;
  MaxCliqueM.SubControl -> QueuedSend;
  MaxCliqueM.SubControl -> GenericComm;
  MaxCliqueM.SubControl -> Router;
  
  MaxCliqueM.StartTimer -> TimerC.Timer[unique("Timer")];
  MaxCliqueM.CliqueTimer -> TimerC.Timer[unique("Timer")];
  MaxCliqueM.CliqueSendTimer -> TimerC.Timer[unique("Timer")];
  MaxCliqueM.ResponseTimer -> TimerC.Timer[unique("Timer")];


  MaxCliqueM.Leds -> LedsC;
  MaxCliqueM.Random -> RandomLFSR;
  MaxCliqueM.RouteQuery -> Router;
  
  MaxCliqueM.Send -> Router.Send[AM_MULTIHOPMSG];
  //MaxCliqueM.Intercept -> Router;
  
  //MaxCliqueM.SendConnect -> QueuedSend.SendMsg[AM_CONNECTMSG];
  //MaxCliqueM.ReceiveConnect -> GenericComm.ReceiveMsg[AM_CONNECTMSG];

  MaxCliqueM.SendClique -> QueuedSend.SendMsg[AM_CLIQUEMSG];
  MaxCliqueM.ReceiveClique -> GenericComm.ReceiveMsg[AM_CLIQUEMSG];

  MaxCliqueM.SendCliqueResponse -> QueuedSend.SendMsg[AM_CLIQUERESPONSEMSG];
  MaxCliqueM.ReceiveCliqueResponse -> GenericComm.ReceiveMsg[AM_CLIQUERESPONSEMSG];
  
}

