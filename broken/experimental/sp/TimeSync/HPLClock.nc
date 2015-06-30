/*									tab:4
 *
 *
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
 */
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
 * Authors:		Jason Hill, David Gay, Philip Levis
 * Date last modified:  6/25/02
 *
 */

// The Mica-specific parts of the hardware presentation layer.

module HPLClock {
  provides interface Clock;
}
implementation
{
  char clockRate;
  unsigned char mscale, minterval ;
  // The clock.

  command result_t Clock.init() {
    clockRate = TOS_CLOCK_STOP;
	mscale =0; minterval =0;
    return SUCCESS;
  }

  command result_t Clock.set(char clock_index) {
     clockRate = clock_index;
	 
    switch (clock_index) {
      case TOS_1s_CLOCK:	minterval = 128; mscale =6; break;
      case TOS_500ms_CLOCK:   minterval = 128; mscale = 5; break;
      case TOS_250ms_CLOCK:   minterval = 128; mscale =4; break;
      case TOS_125ms_CLOCK:   minterval = 128; mscale =3; break;
      case TOS_62ms_CLOCK:   minterval = 64; mscale = 3; break;
      case TOS_31ms_CLOCK:   minterval = 32; mscale = 3; break;
      case TOS_16ms_CLOCK:   minterval = 16; mscale =3; break;
      case TOS_8ms_CLOCK:   minterval = 8; mscale = 3; break;
      case TOS_4ms_CLOCK:   minterval = 4; mscale = 3; break;
      case TOS_2ms_CLOCK:   minterval = 2; mscale =3; break;
      case TOS_1ms_CLOCK:  minterval =1; mscale =3; break;
      case TOS_488us_CLOCK:  minterval =2; mscale = 2; break;
      case TOS_244us_CLOCK:  minterval =1; mscale =2; break;
	  case TOS_CLOCK_STOP:   minterval =0; mscale =0; break;
	  default: return FAIL; 
    }
    call Clock.setRate(minterval, mscale);
    return SUCCESS;
  }

  command uint8_t Clock.getRate() {
    return clockRate;
  }

  command result_t Clock.setRate(char interval, char scale) {
    scale &= 0x7;
    scale |= 0x8;
    cbi(TIMSK, TOIE0);
    cbi(TIMSK, OCIE0);     //Disable TC0 interrupt
    sbi(ASSR, AS0);        //set Timer/Counter0 to be asynchronous
    //from the CPU clock with a second external
    //clock(32,768kHz)driving it.
    outp(scale, TCCR0);    //prescale the timer to be clock/128 to make it
    outp(0, TCNT0);
    outp(interval, OCR0);
    sbi(TIMSK, OCIE0); 
    return SUCCESS;
  }

  default event result_t Clock.fire() { return SUCCESS; }
  TOSH_INTERRUPT(SIG_OUTPUT_COMPARE0) {
    signal Clock.fire();
  }

}
