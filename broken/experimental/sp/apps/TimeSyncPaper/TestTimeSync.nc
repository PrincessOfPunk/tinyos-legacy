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

configuration TestTimeSync {
}

implementation {
	components Main, TestTimeSyncM, LogicalTime, TimeSyncC, LedsC, GenericComm as Comm;

	Main.StdControl -> TestTimeSyncM;
	TestTimeSyncM.Leds -> LedsC;
	TestTimeSyncM.CommControl -> Comm;
	TestTimeSyncM.SendTime -> Comm.SendMsg[19];
	TestTimeSyncM.Receive -> Comm.ReceiveMsg[18];
        TestTimeSyncM.TimeSyncControl-> TimeSyncC.StdControl;
        TestTimeSyncM.Time -> LogicalTime;
	//TestTimeSyncM.TimeControl -> LogicalTime;

}
