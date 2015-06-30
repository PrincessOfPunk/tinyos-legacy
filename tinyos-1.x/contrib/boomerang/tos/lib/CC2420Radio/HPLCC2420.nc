// $Id: HPLCC2420.nc,v 1.1.1.1 2007/11/05 19:11:24 jpolastre Exp $
/*
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

/**
 * Access to hardware presentation layer of the CC2420 transceiver.
 * This interface primary allows register access to the CC2420.
 *
 * @author Joe Polastre, Moteiv Corporation
 */
interface HPLCC2420 {

  /**
   * Send a command strobe
   * 
   * @param rh either RESOURCE_NONE for automatic resource scheduling or a
   * resource handle acquired by CC2420ResourceC
   * @param addr 8-bit address
   *
   * @return status byte from the chipcon
   */
  async command uint8_t cmd( uint8_t rh, uint8_t addr );

  /**
   * Transmit 16-bit data
   *
   * @param rh either RESOURCE_NONE for automatic resource scheduling or a
   * resource handle acquired by CC2420ResourceC
   * @param addr 8-bit address
   * @param data 16-bit data
   *
   * @return status byte from the chipcon.  0xff is return of command failed.
   */
  async command uint8_t write( uint8_t rh, uint8_t addr, uint16_t data );

  /**
   * Read 16-bit data
   *
   * @param rh either RESOURCE_NONE for automatic resource scheduling or a
   * resource handle acquired by CC2420ResourceC
   * @param addr 8-bit address
   *
   * @return 16-bit register value
   */
  async command uint16_t read( uint8_t rh, uint8_t addr );

  

}
