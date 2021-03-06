/**
 * Copyright (c) 2005 Hewlett-Packard Company
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:

 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of the Hewlett-Packard Company nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * communicate with a micro-sd slot wired to a telosb header
 */
/**                                   
 * @author Steve Ayer
 * @author Konrad Lorincz
 * @date March 25, 2008 - ported to TOS 2.x
 */
#include "printf.h"
configuration SDC 
{
    provides interface SD;
}
implementation 
{
    components SDP, LedsC, HplAtm128GeneralIOC as IO, new Atm128GpioInterruptC() as Interrupt, HplAtm128InterruptC as HplInterrupt;
    SD = SDP;
    SDP.Leds -> LedsC;
    components SDIOC;
    
    //components Atm128SpiC, HplAtm128SpiC;
    //SDP.Packet -> Atm128SpiC.SpiPacket;
    SDP.Byte -> SDIOC;
    
    //SDP.SpiBus -> HplAtm128SpiC;
    
    SDP.SS -> IO.PortF0;
    SDP.SCK -> IO.PortF1;
    SDP.CD -> IO.PortD1;
    SDP.CDInt -> Interrupt.Interrupt;
    Interrupt -> HplInterrupt.Int1;
    SDP.Resource -> SDIOC.Resource;
    SDP.SDByte -> SDIOC;
    
    components new TimerMilliC() as Timer;
    SDP.Timer0 -> Timer;
}
