//$Id: Drip.h,v 1.7 2005/10/27 02:08:22 kaminw Exp $

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
 * Authors:		Gilman Tolle
 * Date last modified:  3/12/03
 *
 */

/**
 * @author Gilman Tolle
 */


#ifndef __DRIP_H__
#define __DRIP_H__

enum {
  AM_DRIPMSG = 3,
};

typedef struct DripMetadata {
  uint8_t id;
  uint8_t dummyAlignmentByte; //this byte can go away if you maintain alignment
  uint16_t seqno;
} DripMetadata;

typedef struct DripMsg {
  DripMetadata metadata;
  uint8_t      data[0];
} DripMsg;

enum {
  DRIP_INVALID_KEY = 0,
  DRIP_CACHE_ENTRIES = uniqueCount("DripState"),
  DRIP_SEQNO_OLDEST = 0xfffe, // not ffff, because both oldest and newest should have the least significant bit unset.

  DRIP_SEQNO_NEWEST = 0,
  DRIP_SEQNO_UNKNOWN = 2,
  DRIP_SEQNO_FIRST = DRIP_SEQNO_UNKNOWN, //DRIP_SEQNO_OLDEST, //2,
  DRIP_TIMER_PERIOD = 100,
  DRIP_MIN_SEND_INTERVAL = 0,
  DRIP_MAX_SEND_INTERVAL = 15,
  DRIP_PRE_SEND = 0,
  DRIP_POST_SEND = 1,
  DRIP_WAKEUP_BIT = 0x1,
};

typedef struct DripCacheEntry {
  DripMetadata     metadata;
  uint16_t         trickleAnnounce;
  uint16_t         trickleCountdown;
  uint8_t          trickleStage;
  uint8_t          trickleSuppress:1;
  uint8_t          trickleState:6;
} DripCacheEntry;

#endif


