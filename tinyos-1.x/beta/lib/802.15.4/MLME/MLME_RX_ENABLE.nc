// $Id: MLME_RX_ENABLE.nc,v 1.3 2004/03/09 01:10:34 jpolastre Exp $

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
 * Authors:		Joe Polastre
 */

includes IEEE802154;

/**
 * The MLME-SAP receiver state primitives define how a device can enable or 
 * disable the receiver at a given time.
 *
 * @author Joe Polastre
 */

interface MLME_RX_ENABLE {

  /**
   * Allows the next higher layer to request that the receiver is
   * enabled for a finite period of time
   * 
   * @param DeferPermit TRUE if the receiver enable can be deferred until
   *                    during the next superframe if the requested time has
   *                    already passed
   * @param RxOnTime The number of symbols from the start of the superframe 
   *                 before the receiver is to be enabled.  The precision 
   *                 of this value is a minimum of 20 bits.  This parameter 
   *                 is ignored for nonbeacon-enabled PANs
   * @param RxOnDuration The number of symbols for which the receiver 
   *                     is to be enabled
   */
  command void request  (
                          bool DeferPermit,
                          uint32_t RxOnTime,
                          uint32_t RxOnDuration
                        );

  /**
   * Reports the results of the attempt to enable the receiver
   *
   * @param status The status of the receiver enable request
   */
  event void confirm    (
                          IEEE_status status
                        );

}
