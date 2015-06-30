// $Id: HPLADCC.nc,v 1.1 2004/04/22 01:16:51 shnayder Exp $

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
 * Authors:		Jason Hill, David Gay, Philip Levis
 * Date last modified:  6/25/02
 *
 */

// The hardware presentation layer. See hpl.h for the C side.
// Note: there's a separate C side (hpl.h) to get access to the avr macros

// The model is that HPL is stateless. If the desired interface is as stateless
// it can be implemented here (Clock, FlashBitSPI). Otherwise you should
// create a separate component


/**
 * @author Jason Hill
 * @author David Gay
 * @author Philip Levis
 */
module HPLADCC {
  provides interface HPLADC as ADC;
  uses interface PowerState;
}
implementation
{
  async command result_t ADC.init() {
    call PowerState.ADCon();
    TOSH_adc_init();
    return SUCCESS;
  }

  async command result_t ADC.setSamplingRate(uint8_t rate) {
    TOSH_adc_set_sampling_rate(rate);
    return SUCCESS;
  }

  async command result_t ADC.bindPort(uint8_t port, uint8_t adcPort) {
    return SUCCESS;
  }

  async command result_t ADC.samplePort(uint8_t port) {
    call PowerState.ADCsample(port);
    TOSH_adc_sample_port(port);
    return SUCCESS;
  }

  async command result_t ADC.sampleAgain() {
    TOSH_adc_sample_again();
    return SUCCESS;
  }

  async command result_t ADC.sampleStop() {
    TOSH_adc_sample_stop();
    return SUCCESS;
  }

  default async event result_t ADC.dataReady(uint16_t done) { return SUCCESS; }
  void TOSH_adc_data_ready(uint16_t data) __attribute__ ((C)) {
    call PowerState.ADCdataReady();
    signal ADC.dataReady(data);
  }
}