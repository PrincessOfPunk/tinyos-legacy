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
configuration TestSundial {
}

implementation {
	components Main, Sundial, TestSundialM, LedsC;

	Main.StdControl -> TestSundialM;
	TestSundialM.SundialControl -> Sundial;
	TestSundialM.Leds -> LedsC;
	TestSundialM.Timer3 -> Sundial.PhasedTimer[unique("Timer")];
	TestSundialM.Timer2 -> Sundial.PhasedTimer[unique("Timer")];
	TestSundialM.Timer1 -> Sundial.PhasedTimer[unique("Timer")];
	TestSundialM.Timer0 -> Sundial.PhasedTimer[unique("Timer")];
	
}
