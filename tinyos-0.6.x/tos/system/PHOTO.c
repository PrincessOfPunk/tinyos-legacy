/*									tab:4
 * PHOTO.c - TOS abstraction of asynchronous digital photo sensor
 *
 * "Copyright (c) 1000 and The Regents of the University 
 * of California.  All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice and the following
 * two paragraphs appear in all copies of this software.
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
 * Authors:		Jason Hill
 *  modified: DEC 10/4/2000 function commented
 *
 */

/*  OS component abstraction of the analog photo sensor and */
/*  associated A/D support.  It provides an asynchronous interface */
/*  to the photo sensor. */

/*  PHOTO_INIT command initializes the device */
/*  PHOTO_GET_DATA command initiates acquiring a sensor reading. */
/*  It returns immediately.   */
/*  PHOTO_DATA_READY is signaled, providing data, when it becomes */
/*  available. */
/*  Access to the sensor is performed in the background by a separate */
/* TOS task. */

#include "tos.h"
#include "PHOTO.h"
#include "sensorboard.h"
#include "dbg.h"

char TOS_COMMAND(PHOTO_GET_DATA)(){
    return TOS_CALL_COMMAND(SUB_ADC_GET_DATA)(TOS_ADC_PORT_1);
}

char TOS_COMMAND(PHOTO_INIT)(){
  dbg(DBG_BOOT, ("PHOTO initialized.\n"));
  ADC_PORTMAP_BIND(TOS_ADC_PORT_1, PHOTO_PORT);
  MAKE_PHOTO_CTL_OUTPUT();
  SET_PHOTO_CTL_PIN();
  return TOS_CALL_COMMAND(SUB_ADC_INIT)();
}

char TOS_COMMAND(PHOTO_PWR)(char val){
  CLR_PHOTO_CTL_PIN();
  MAKE_PHOTO_CTL_INPUT();
  return 1;
}
 
