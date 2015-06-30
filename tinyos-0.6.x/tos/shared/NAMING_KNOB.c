/*									tab:4
 * NAMING_KNOB.c
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
 * Authors:   Solomon Bien
 * History:   created 8/14/2001
 *
 * This component has an AM handler to change the naming scheme through the
 * NAMING component.
 *
 */

#include "tos.h"
#include "NAMING_KNOB.h"

typedef struct{
  char namingScheme;  // value of new naming scheme
} naming_knob_msg;


#define TOS_FRAME_TYPE NAMING_KNOB_frame
TOS_FRAME_BEGIN(NAMING_KNOB_frame) {
  char pending;
  TOS_Msg data; 
}
TOS_FRAME_END(NAMING_KNOB_frame);


char TOS_COMMAND(NAMING_KNOB_INIT)(){
  TOS_CALL_COMMAND(NAMING_KNOB_SUB_INIT)();
  return 1;
}

char TOS_COMMAND(NAMING_KNOB_START)(){
  TOS_CALL_COMMAND(NAMING_KNOB_SUB_START)();
  return 1;
}


char TOS_EVENT(NAMING_KNOB_SUB_MSG_SEND_DONE)(TOS_MsgPtr msg) {
  if (VAR(pending) && msg == &VAR(data)) {
    VAR(pending) = 0;
    return 1;
  }
  return 0;
}


TOS_MsgPtr TOS_MSG_EVENT(SET_NAMING_SCHEME_MSG)(TOS_MsgPtr msg){
  naming_knob_msg * t;
  
  t = (naming_knob_msg *) msg->data;
  
  TOS_CALL_COMMAND(NAMING_KNOB_SET_NAMING_SCHEME)(t->namingScheme);
  
  return msg;
}


