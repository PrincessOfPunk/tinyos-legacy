/*
 * Copyright (c) 2004, Technische Universitat Berlin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - Neither the name of the Technische Universitat Berlin nor the names
 *   of its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * - Description ----------------------------------------------------------
 * Implementation of UART0 lowlevel functionality - stateless.
 * - Revision -------------------------------------------------------------
 * $Revision: 1.2 $
 * $Date: 2005/03/23 12:47:50 $
 * @author Jan Hauer
 * @author Vlado Handziski
 * @author Joe Polastre
 * ========================================================================
 */

includes msp430baudrates;

module HPLUARTM {
  provides interface HPLUART as UART;
  uses interface HPLUSARTControl as USARTControl;
  uses interface HPLUSARTFeedback as USARTData;
}
implementation
{

  async command result_t UART.init() {
    // set up the USART to be a UART
    call USARTControl.setModeUART();
    // use SMCLK
    call USARTControl.setClockSource(SSEL_SMCLK);
    // set the bitrate to 57600
    call USARTControl.setClockRate(UBR_SMCLK_57600, UMCTL_SMCLK_57600);
    // enable interrupts

    call USARTControl.enableRxIntr();
    call USARTControl.enableTxIntr();
    return SUCCESS;
  }

  async command result_t UART.stop() {
    call USARTControl.disableRxIntr();
    call USARTControl.disableTxIntr();
    // disable the UART modules so that we can go in deeper LowPower mode
    call USARTControl.disableUART();
    return SUCCESS;
  }

  async event result_t USARTData.rxDone(uint8_t b) {
    return signal UART.get(b);
  }

  async event result_t USARTData.txDone() {
    return signal UART.putDone();
  }

  async command result_t UART.put(uint8_t data){
    return call USARTControl.tx(data);
  }

  default async event result_t UART.get(uint8_t data) { return SUCCESS; }

  default async event result_t UART.putDone() { return SUCCESS; }
}
