/*									tab:4
 *
 *
 * "Copyright (c) 2002 and The Regents of the University 
 * of California.  All rights reserved.
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
 * Authors:		Su Ping, ported to nesC by Sam Madden
 * Date last modified:  6/25/02
 *
 */
/** 
 * This configuration component wires the the TestTimer to lower level
 * TinyOS components: Timer, ClockC and LedsC
 **/
configuration TestTimer {
}

implementation {
	components Main, NewTimerC, TestTimerM, LedsC;

	Main.StdControl -> TestTimerM;
	TestTimerM.TimerControl -> NewTimerC.StdControl;
	TestTimerM.Leds -> LedsC;
	TestTimerM.Timer3 -> NewTimerC.Timer[unique("Timer")];
	TestTimerM.Timer2 -> NewTimerC.Timer[unique("Timer")];
	TestTimerM.Timer1 -> NewTimerC.Timer[unique("Timer")];
	TestTimerM.Timer0 -> NewTimerC.Timer[unique("Timer")];
	
}
