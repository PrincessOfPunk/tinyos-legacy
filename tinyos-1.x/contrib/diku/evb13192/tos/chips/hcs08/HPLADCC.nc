// $Id: HPLADCC.nc,v 1.1 2006/01/16 18:43:17 janflora Exp $

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
 * Revision:		$Rev$
 * Adapted for 13192EVB by Mads Bondo Dydensborg
 * NOTE: Assumptions about the BUSCLK is made here: 8MHz!
 *
 */

/**
 * @author Jason Hill
 * @author David Gay
 * @author Philip Levis
 */
module HPLADCC {
  provides interface HPLADC as ADC;
}
implementation
{
  /* The port mapping table */
  bool init_portmap_done;
  /*
    enum {
    TOSH_ADC_PORTMAPSIZE = 10
    };
  */

  uint8_t TOSH_adc_portmap[TOSH_ADC_PORTMAPSIZE];

  void init_portmap() {
    /* The default ADC port mapping */
    atomic {
      if( init_portmap_done == FALSE ) {
	int i;
	for (i = 0; i < TOSH_ADC_PORTMAPSIZE; i++)
	  TOSH_adc_portmap[i] = i;
	init_portmap_done = TRUE;
      }
    }
  }


  async command result_t ADC.init() {
    init_portmap();

    /*
    outp(0x04, ADCSR); // 250KHz ADC clock (4MHz/16)
	
    cbi(ADCSR, ADSC);
    sbi(ADCSR, ADIF);
    sbi(ADCSR, ADIE);
    cbi(ADCSR, ADEN);

    */

    /* The ADC for the 13192 EVB is described in chapter 14 of the Data Sheet. */
    /* Set up ATDC (14.6.1) - it is 10 bits pr. default. */
    // ATDC_ATDPU = 1; // Power it on << -- wait to later
    ATDC_DJM = 1; // Right justified mode
    ATDC_PRS = 8; // Pre scale factor, Dunno, medium value

    

    return SUCCESS;
  }
  
  /**
   * Unimplemented!
   * 
   * @return SUCCESS always.
   */
  async command result_t ADC.setSamplingRate(uint8_t rate) {
    /*
    uint8_t current_val = inp(ADCSR);
    current_val = (current_val & 0xF8) | (rate & 0x07);
    outp(current_val, ADCSR); */
    return SUCCESS;
  }
  
  async command result_t ADC.bindPort(uint8_t port, uint8_t adcPort) {
    if (port < TOSH_ADC_PORTMAPSIZE)
      {
	atomic {
	  init_portmap();
	  TOSH_adc_portmap[port] = adcPort;
	}
	return SUCCESS;
      }
    else
      return FAIL;
  }

  async command result_t ADC.samplePort(uint8_t port) {
    atomic {
      /* Power up the converter. Note, this does not start it. */
      ATDC_ATDPU = 1;
      /* Writing to this register start a conversion */
      /* Choose the pin that the port points at */
      ATDSC_ATDCH = TOSH_adc_portmap[port]; 
      /* Ask for an interrupt at end of conversion */
      ATDSC_ATDIE = 1;
    }
    return SUCCESS;
  }

  async command result_t ADC.sampleAgain() {
    /* Ask for an interrupt at end of conversion */
    ATDSC_ATDIE = 1;
    return SUCCESS;
  }

  async command result_t ADC.sampleStop() {
    /* We do not want any interrupts */
    ATDSC_ATDIE = 0;
    /* Stop the converter! */
    ATDC_ATDPU = 0;
    return SUCCESS;
  }

  default async event result_t ADC.dataReady(uint16_t done) { return SUCCESS; }
  TOSH_SIGNAL(ATD) {
    uint16_t data = (ATDRH << 8) | ATDRL;
    /* Whoa... */
    __nesc_enable_interrupt();
    signal ADC.dataReady(data);
  }
}