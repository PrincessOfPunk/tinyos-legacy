/*                                                                      tab:4
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
 * Authors:             Philip Levis
 * Description:         Implementation of TOSSIM hardware emulation.
 * Date:                September 24, 2001
 *
 */

#include "tos.h"
#include "hardware.tossim.h"
#include "dbg.h"

#define TOS_FRAME_TYPE HARDWARE_frame
TOS_FRAME_BEGIN(HARDWARE_frame) {
  char status_register;  // SREG
  char register_A;
  char register_B;
  char register_C;
  char register_D;
  char register_E;
}
TOS_FRAME_END(HARDWARE_frame);


void init_hardware() {
  int i;
  for (i = 0; i < tos_state.num_nodes; i++) {
    tos_state.current_node = i;
    VAR(status_register) = 0xff;
  }
}

short set_io_bit(char port, char bit) {
  char* register_ptr;
  switch(port) {
  case 'A':
    register_ptr = &VAR(register_A);
    break;

  case 'B':
    register_ptr = &VAR(register_B);
    break;

  case 'C':
    register_ptr = &VAR(register_C);
    break;
    
  case 'D':
    register_ptr = &VAR(register_D);
    break;
    
  case 'E':
    register_ptr = &VAR(register_E);
    break;
    
  case 'S':
    register_ptr = &VAR(status_register);
    break;

  default:
    register_ptr = NULL;
    break;
  }
  
  dbg(DBG_HARD, ("Set bit %i of port %c\n", (int)bit, port));

  *register_ptr = (*register_ptr |= (0x1 << bit));

  return *register_ptr;
}

short clear_io_bit(char port, char bit) {
  dbg(DBG_HARD, ("Clear bit %i of port %c\n", (int)bit, port));
  return 0xff;
}

char inp_emulate(char port) {
  switch(port) {
  case SREG:
    //dbg(DBG_HARD, ("inp(SREG)\n"));
    return VAR(status_register);

  default:
    dbg(DBG_HARD, ("inp(%c)\n", port));
    return 0xff;
  }
}

char outp_emulate(char val, char port) {
  dbg(DBG_HARD, ("outp(0x%x, %c)", val, port));
  return 0xff;
}

short disable_interrupts() {
  //dbg(DBG_HARD, ("Disabled interrupts.\n"));
  VAR(status_register) &= 0x7f;
  return 0xff;
}

short enable_interrupts() {
  //dbg(DBG_HARD, ("Enabled interrupts.\n"));
  VAR(status_register) |= 0x80;
  return 0xff;
}
