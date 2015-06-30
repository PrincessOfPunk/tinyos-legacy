// $Id: CC2420RadioC.nc,v 1.1.1.1 2007/11/05 19:11:23 jpolastre Exp $
/*									tab:4
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
 * Copyright (c) 2006 Moteiv Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached MOTEIV-LICENSE     
 * file. If you do not find these files, copies can be found at
 * http://www.moteiv.com/MOTEIV-LICENSE.txt and by emailing info@moteiv.com.
 */

/**
 * Driver for the CC2420 Radio Transceiver.  This configuration
 * provides all the necessary external interfaces for using the CC2420
 * radio.  
 * <p>
 * The logic contained within the implementation of this configuration
 * is highly complex and is only recommended for expert users.
 * Novice users, or those build protocols and applications at the network
 * layer and above, should use SPC to send and receive packets.
 *
 * @author Joe Polastre, Moteiv Corporation <info@moteiv.com>
 */

#include "CC2420Const.h"

configuration CC2420RadioC
{
  provides {
    interface StdControl;
    interface SplitControl;
    interface CC2420BareSendMsg as Send;
    interface ReceiveMsg as Receive @exactlyonce();
    interface CC2420Control;
    interface MacControl;
    interface MacBackoff;
    interface RadioCoordinator as RadioReceiveCoordinator;
    interface RadioCoordinator as RadioSendCoordinator;

    interface Counter<T32khz,uint32_t> as RadioActiveTime;
  }
}
implementation
{
  components MainCC2420RadioC;

  components CC2420RadioM, CC2420ControlM, HPLCC2420C, RandomLFSR;

  components new Alarm32khz16C() as BackoffAlarm32khzC;
  components Counter32khzC;

  components new CC2420ResourceC() as CmdCCAFiredC;
  components new CC2420ResourceC() as CmdSplitControlInitC;
  components new CC2420ResourceC() as CmdSplitControlStartC;
  components new CC2420ResourceC() as CmdSplitControlStopC;
  components new CC2420ResourceC() as CmdCmds;

  components new CC2420ResourceC() as CmdFlushRXFIFOC;
  components new CC2420ResourceC() as CmdReceiveC;
  components new CC2420ResourceC() as CmdTransmitC;
  components new CC2420ResourceC() as CmdTryToSendC;

  StdControl = CC2420RadioM;
  SplitControl = CC2420RadioM;
  Send = CC2420RadioM;
  Receive = CC2420RadioM;
  MacControl = CC2420RadioM;
  MacBackoff = CC2420RadioM;
  RadioActiveTime = CC2420RadioM;
  CC2420Control = CC2420ControlM;
  RadioReceiveCoordinator = CC2420RadioM.RadioReceiveCoordinator;
  RadioSendCoordinator = CC2420RadioM.RadioSendCoordinator;

  CC2420RadioM.CC2420SplitControl -> CC2420ControlM;
  CC2420RadioM.CC2420Control -> CC2420ControlM;
  CC2420RadioM.Random -> RandomLFSR;
  CC2420RadioM.TimerControl -> BackoffAlarm32khzC;
  CC2420RadioM.BackoffAlarm32khz -> BackoffAlarm32khzC;
  CC2420RadioM.Counter32khz -> Counter32khzC;
  CC2420RadioM.Counter32khz16 -> Counter32khzC;
  CC2420RadioM.HPLChipcon -> HPLCC2420C.HPLCC2420;
  CC2420RadioM.HPLChipconFIFO -> HPLCC2420C.HPLCC2420FIFO;
  CC2420RadioM.FIFOP -> HPLCC2420C.InterruptFIFOP;
  CC2420RadioM.SFD -> HPLCC2420C.CaptureSFD;

  CC2420ControlM.HPLChipconControl -> HPLCC2420C.StdControl;
  CC2420ControlM.HPLChipcon -> HPLCC2420C.HPLCC2420;
  CC2420ControlM.HPLChipconRAM -> HPLCC2420C.HPLCC2420RAM;
  CC2420ControlM.CCA -> HPLCC2420C.InterruptCCA;


  // CC2420 Control commands

  CC2420ControlM.CmdCCAFired -> CmdCCAFiredC;
  CC2420ControlM.CmdSplitControlInit -> CmdSplitControlInitC;
  CC2420ControlM.CmdSplitControlStart -> CmdSplitControlStartC;
  CC2420ControlM.CmdSplitControlStop -> CmdSplitControlStopC;
  CC2420ControlM.CmdCmds -> CmdCmds;

  // CC2420 Radio commands

  CC2420RadioM.CmdFlushRXFIFO -> CmdFlushRXFIFOC;
  CC2420RadioM.CmdReceive -> CmdReceiveC;
  CC2420RadioM.CmdTransmit -> CmdTransmitC;
  CC2420RadioM.CmdTryToSend -> CmdTryToSendC;
}

