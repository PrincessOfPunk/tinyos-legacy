/*									tab:4
 *
 *
 * "Copyright (c) 2002-2004 The Regents of the University  of California.  
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
 */
/*
 * Author:	Barbara Hohlt
 * Project: 	Ivy 
 *
 * The PowerModeSend interface provides an asynchronous mechanism for
 * providing knowledge about what state the PowerScheduler is in. 
 * 
 * 
 */

interface PowerModeSend {


  /**
   * Signaled when the component changes state.
   */
  event void modeNotify(powermode powerMode, uint8_t slotState, int s);

  /**
   * Signaled when the node changes parent.
   */
  event void parentNotify(uint16_t newparent);

  /**
   * Signaled when it is time for the radio 
   * to be turned on.
   */
  event void radioOnNotify();

}
