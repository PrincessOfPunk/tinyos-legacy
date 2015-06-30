/*									tab:4
 *
 *
 * "Copyright (c) 2000 and The Regents of the University 
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
 * Authors:		Joe Polastre
 *
 *
 */


//this components explores routing topology and then broadcasts back
// light readings.

#include "tos.h"
#include "ROUTER_TEST.h"

#define TOS_FRAME_TYPE ROUTE_obj_frame
TOS_FRAME_BEGIN(ROUTER_TEST_obj_frame) {
  char dummydata[20];
  char clockticks;
}
TOS_FRAME_END(ROUTER_TEST_obj_frame);

char TOS_COMMAND(ROUTER_TEST_INIT)(){
    //initialize sub components
  //TOS_CALL_COMMAND(ROUTER_TEST_SUB_CLOCK_INIT)(255,0x05);
  // TOS_CALL_COMMAND(ROUTER_TEST_SUB_LED_INIT)();
  char i;
  VAR(clockticks) = 3;
  
  for (i = 0; i < 20; i++) {
    VAR(dummydata)[(int)i] = i;
  }
  
  return 1;
}

char TOS_COMMAND(ROUTER_TEST_START)(){
  return 1;
}

void TOS_EVENT(ROUTER_TEST_SUB_CLOCK)(){

    VAR(clockticks)--;

    if (VAR(clockticks == 0))
    {
      if (TOS_CALL_COMMAND(ROUTER_TEST_SEND_PACKET)(VAR(dummydata)))
	TOS_CALL_COMMAND(ROUTER_TEST_LED1_TOGGLE)();
      VAR(clockticks) = 3;
    }
}


