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
 * Authors:		Su Ping
 * Date last modified:  
 *
 */
includes TimeSyncMsg;
configuration Test {
}

implementation {
	components Main, TestM, LogicTimeC, TimeSyncC, LedsC, GenericComm as Comm;

	Main.StdControl -> TestM;
	TestM.Leds -> LedsC;
	TestM.CommControl -> Comm;
	TestM.Send -> Comm.SendMsg[AM_TIMESYNCMSG];
        TestM.SendTime -> Comm.SendMsg[17];
	TestM.Receive -> Comm.ReceiveMsg[AM_TIMESYNCMSG];
	TestM.TimeSync -> TimeSyncC;
	TestM.LogicTime -> LogicTimeC;
        TestM.AbsoluteTimer -> LogicTimeC;
}