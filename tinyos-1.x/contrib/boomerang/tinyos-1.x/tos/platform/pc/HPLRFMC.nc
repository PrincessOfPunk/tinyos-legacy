// $Id: HPLRFMC.nc,v 1.1.1.1 2007/11/05 19:10:18 jpolastre Exp $

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
module HPLRFMC {
  provides interface HPLRFM as RFM;
}
implementation
{
  default event result_t RFM.bitEvent() { return SUCCESS; }
  void TOSH_rfm_bit_event() __attribute__ ((C)) {
    signal RFM.bitEvent();
  }

  command uint8_t RFM.rxBit() {
    return TOSH_rfm_rx_bit();
  }

  command result_t RFM.txBit(uint8_t data) {
    TOSH_rfm_tx_bit(data);
    return SUCCESS;
  }

  command result_t RFM.powerOff() {
    TOSH_rfm_power_off();
    return SUCCESS;
  }

  command result_t RFM.disableTimer() {
    TOSH_rfm_disable_timer();
    return SUCCESS;
  }

  command result_t RFM.enableTimer() {
    TOSH_rfm_enable_timer();
    return SUCCESS;
  }

  command result_t RFM.txMode() {
    TOSH_rfm_tx_mode();
    return SUCCESS;
  }

  command result_t RFM.rxMode() {
    TOSH_rfm_rx_mode();
    return SUCCESS;
  }

  command result_t RFM.setBitRate(uint8_t level) {
    TOSH_rfm_set_bit_rate(level);
    return SUCCESS;
  }


  command result_t RFM.init() {
    TOSH_rfm_init();
    return SUCCESS;
  }
}
