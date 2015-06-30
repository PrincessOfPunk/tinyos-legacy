/*
 * Copyright (c) 2002, Vanderbilt University
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 * 
 * IN NO EVENT SHALL THE VANDERBILT UNIVERSITY BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE VANDERBILT
 * UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE VANDERBILT UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE VANDERBILT UNIVERSITY HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 * Author: Miklos Maroti
 * Date last modified: 2/14/03
 */

enum
{
	DIAGMSG_ACTIVE_MESSAGE = 0xB1,
	DIAGMSG_BASE_STATION = 0,
	DIAGMSG_RETRY_COUNT = 3,
	DIAGMSG_RECORDED_MSGS = 3,
};

/**
 * Tokens allow commonly used strings to be sent using a single byte
 * in the message.
 */
enum
{
	DIAGMSG_FALSE = 0,
	DIAGMSG_TRUE = 1,
	DIAGMSG_NODE = 2,
	DIAGMSG_SOURCE = 3,
	DIAGMSG_DESTINATION = 4,
	DIAGMSG_TARGET = 5,
	DIAGMSG_MINIMUM = 6,
	DIAGMSG_MAXIMUM = 7,
	DIAGMSG_MEAN = 8,
	DIAGMSG_AVERAGE = 9,
	DIAGMSG_TIME = 10,
	DIAGMSG_COUNTER = 11,
	DIAGMSG_STAMP = 12,
	DIAGMSG_SEQUENCE = 13,
	DIAGMSG_NUMBER = 14,
	DIAGMSG_CONFIG = 15,
	DIAGMSG_DIAMETER = 16,
	DIAGMSG_FAIL = 17,
	DIAGMSG_SUCCESS = 18,
	DIAGMSG_ACK = 19,
	DIAGMSG_POWER = 20,
	DIAGMSG_LOCAL = 21,
	DIAGMSG_REMOTE = 22,
	DIAGMSG_LEADER = 23,
	DIAGMSG_ROOT = 24,
	DIAGMSG_FIRST = 25,
	DIAGMSG_SECOND = 26,
	DIAGMSG_THIRD = 27,
	DIAGMSG_NODEID = 28,
	DIAGMSG_DELTA = 29,
	DIAGMSG_DELAY = 30,
	DIAGMSG_RATE = 31,
	DIAGMSG_FREQUENCY = 32,
	DIAGMSG_COORD = 33,
	DIAGMSG_COORDS = 34,
	DIAGMSG_ANGLE = 35,
	DIAGMSG_TEMPERATURE = 36,
	DIAGMSG_LIGHT = 37,
	DIAGMSG_NEXT = 38,
	DIAGMSG_PREVIOUS = 39,
	DIAGMSG_START = 40,
	DIAGMSG_END = 41,
	DIAGMSG_SPEED = 42,
	DIAGMSG_CPU = 43,
	DIAGMSG_BEACON = 44,
	DIAGMSG_ARRIVAL = 45,
	DIAGMSG_SENDDONE = 46,
	DIAGMSG_GLOBAL = 47,
	DIAGMSG_CLOCK = 48,
};
