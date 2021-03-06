/*									tab:4
 * SENSE.c - display sensor value on the LEDs
 *
 * "Copyright (c) 2001 and The Regents of the University 
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
 * Authors:   David Culler
 * History:   created 10/9/2001
 *
 */

#include "tos.h"
#include "SENSE.h"

//Frame Declaration
#define TOS_FRAME_TYPE SENSE_frame
TOS_FRAME_BEGIN(SENSE_frame) {
  char sound;
}
TOS_FRAME_END(SENSE_frame);

/* SENSE_INIT: 
   Clear all the LEDs and initialize state
*/

char TOS_COMMAND(SENSE_INIT)(){
  //  TOS_CALL_COMMAND(SENSE_LEDr_on)();
  //  TOS_CALL_COMMAND(SENSE_LEDy_on)();
  //  TOS_CALL_COMMAND(SENSE_LEDg_on)();
  TOS_CALL_COMMAND(SENSE_SUB_INIT)(); 
  return 1;
}

/* SENSE_START: 
   initialize clock component to generate periodic events.
*/
char TOS_COMMAND(SENSE_START)(){
  //  TOS_CALL_COMMAND(SENSE_LEDr_off)();
  //  TOS_CALL_COMMAND(SENSE_LEDy_off)();
  //  TOS_CALL_COMMAND(SENSE_LEDg_off)();
  TOS_CALL_COMMAND(SENSE_CLOCK_INIT)(tick4ps); 
  return 1;
}

void display(char val)
{
  if (val & 1) TOS_CALL_COMMAND(SENSE_LEDy_on)();  
  else TOS_CALL_COMMAND(SENSE_LEDy_off)();
  if (val & 2) TOS_CALL_COMMAND(SENSE_LEDg_on)();  
  else TOS_CALL_COMMAND(SENSE_LEDg_off)();
  if (val & 4) TOS_CALL_COMMAND(SENSE_LEDr_on)();  
  else TOS_CALL_COMMAND(SENSE_LEDr_off)();
}

/* Clock Event Handler:
   Increment counter and display
 */
void TOS_EVENT(SENSE_CLOCK_EVENT)(){
  //  TOS_CALL_COMMAND(SENSE_LEDr_toggle)();
  TOS_CALL_COMMAND(SENSE_GET_DATA)();

  if (VAR(sound) == 1){
     TOS_CALL_COMMAND(SENSE_SOUNDER_ON)();
     VAR(sound)++;
  }else if (VAR(sound) == 2){
     TOS_CALL_COMMAND(SENSE_SOUNDER_OFF)();
     VAR(sound) = 0;
  } 
}

/* Data ready event Handler:
 *   full dark should display 7
 *   full light zero
*/
char TOS_EVENT(SENSE_DATA_READY)(short data){
  // TOS_CALL_COMMAND(SENSE_LEDg_toggle)();
  display(7 - ((data >> 7) & 0x7));
  if (((data >> 7) & 0x7) == 0)
     VAR(sound) = 1;
  return 1;
}



