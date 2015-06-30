/*									tab:4
 *
 *
 * "Copyright (c) 2000-2002 The Regents of the University  of California.  
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and
 * its documentation for any purpose, without fee, and without written
 * agreement is hereby granted, provided that the above copyright
 * notice, the following two paragraphs and the author appear in all
 * copies of this software.
 * 
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
 * PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL
 * DAMAGES ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
 * DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE
 * PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 * CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT,
 * UPDATES, ENHANCEMENTS, OR MODIFICATIONS."
 *
 */
/*									tab:4
 *									
 *  IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.
 *  By downloading, copying, installing or using the software you
 *  agree to this license.  If you do not agree to this license, do
 *  not download, install, copy or use the software.
 *
 *  Intel Open Source License 
 *
 *  Copyright (c) 2002 Intel Corporation 
 *  All rights reserved. 
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions are
 *  met:
 * 
 *	Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *	Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in the
 *  documentation and/or other materials provided with the distribution.
 *      Neither the name of the Intel Corporation nor the names of its
 *  contributors may be used to endorse or promote products derived from
 *  this software without specific prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 *  ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 *  LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 *  PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE INTEL OR ITS
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 *  EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 *  PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 *  PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 *  LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 *  SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * 
 */

/*
 * Authors:   Philip Levis <pal@cs.berkeley.edu>
 * History:   Apr 13, 2003         Inception.
 *
 */

/**
 * @author Philip Levis <pal@cs.berkeley.edu>
 */


includes Mate;

module OPgetsetvar4M {
  provides {
    interface MateBytecode as Get;
    interface MateBytecode as Set;
    interface MateBytecodeLock as LockGet;
    interface MateBytecodeLock as LockSet;
    interface StdControl;
  }
  uses {
    interface MateError as Error;
    interface MateLocks as Locks;
    interface MateTypes as Types;
    interface MateStacks as Stacks;
    interface MateEngineStatus as EngineStatus;
  }
}

implementation {

  typedef enum {
    MATE_LOCK_4_0 = unique("MateLock"),
    MATE_LOCK_4_1 = unique("MateLock"),
    MATE_LOCK_4_2 = unique("MateLock"),
    MATE_LOCK_4_3 = unique("MateLock"),

    MATE_LOCK_4_4 = unique("MateLock"),
    MATE_LOCK_4_5 = unique("MateLock"),
    MATE_LOCK_4_6 = unique("MateLock"),
    MATE_LOCK_4_7 = unique("MateLock"),

    MATE_LOCK_4_8 = unique("MateLock"),
    MATE_LOCK_4_9 = unique("MateLock"),
    MATE_LOCK_4_10 = unique("MateLock"),
    MATE_LOCK_4_11 = unique("MateLock"),

    MATE_LOCK_4_12 = unique("MateLock"),
    MATE_LOCK_4_13 = unique("MateLock"),
    MATE_LOCK_4_14 = unique("MateLock"),
    MATE_LOCK_4_15 = unique("MateLock"),
    MATE_LOCK_4_COUNT = 16,
  } LockNames;

  
  MateStackVariable heap[MATE_LOCK_4_COUNT];

  command result_t StdControl.init() {
    int i;
    for (i = 0; i < MATE_LOCK_4_COUNT; i++) {
      heap[(int)i].type = MATE_TYPE_INTEGER;
      heap[(int)i].value.var = 0;
    }
    return SUCCESS;
  }
  
  command result_t StdControl.start() {
    return SUCCESS;
  }
  
  command result_t StdControl.stop() {
    return SUCCESS;
  }

  uint8_t varToLock(uint8_t num) {
    switch (num) {
    case 0:
      return MATE_LOCK_4_0;
    case 1:
      return MATE_LOCK_4_1;
    case 2:
      return MATE_LOCK_4_2;
    case 3:
      return MATE_LOCK_4_3;
    case 4:
      return MATE_LOCK_4_4;
    case 5:
      return MATE_LOCK_4_5;
    case 6:
      return MATE_LOCK_4_6;
    case 7:
      return MATE_LOCK_4_7;
    case 8:
      return MATE_LOCK_4_8;
    case 9:
      return MATE_LOCK_4_9;
    case 10:
      return MATE_LOCK_4_10;
    case 11:
      return MATE_LOCK_4_11;
    case 12:
      return MATE_LOCK_4_12;
    case 13:
      return MATE_LOCK_4_13;
    case 14:
      return MATE_LOCK_4_14;
    case 15:
      return MATE_LOCK_4_15;
    default:
      return 255;
    }
  }

  command int16_t LockGet.lockNum(uint8_t instr, uint8_t id, uint8_t pc) {
    uint8_t arg = instr - OP_GETVAR4;
    dbg(DBG_USR2, "OPgetsetvar4M: LockGet.lockNum called with rval of %i\n", (int)arg);
    return varToLock(arg);
  }

  command int16_t LockSet.lockNum(uint8_t instr, uint8_t id, uint8_t pc) {
    uint8_t arg = instr - OP_SETVAR4;
    dbg(DBG_USR2, "OPgetsetvar4M: LockSet.lockNum called with rval of %i\n", (int)arg);
    return varToLock(arg);
  }
  
  command result_t Get.execute(uint8_t instr,
			       MateContext* context) {
    uint8_t arg = instr - OP_GETVAR4;
    uint8_t lock = varToLock(arg);
    if ((lock == 255) || !call Locks.isHeldBy(lock, context)) {
      call Error.error(context, MATE_ERROR_INVALID_ACCESS);
      return FAIL;
    }
    
    dbg(DBG_USR1, "VM (%i): Executing getvar (%i).\n", (int)context->which, (int)arg);
    call Stacks.pushOperand(context, &heap[arg]);
    return SUCCESS;
  }

  command uint8_t Get.byteLength() {return 1;}

  command result_t Set.execute(uint8_t instr, 
			       MateContext* context) {
    uint8_t arg = instr - OP_SETVAR4;
    uint8_t lock = varToLock(arg);
    MateStackVariable* var;
    if ((lock == 255) || !call Locks.isHeldBy(lock, context)) {
      call Error.error(context, MATE_ERROR_INVALID_ACCESS);
      return FAIL;
    }

    var = call Stacks.popOperand(context);
    if (!call Types.checkValue(context, var)) {
      return FAIL;
    }
    dbg(DBG_USR1, "VM (%i): OPsetvarM (%i) executed.\n", (int)context->which, (int)arg);
    heap[arg] = *var;
    return SUCCESS;
  }

  command uint8_t Set.byteLength() {return 1;}
  
  event void EngineStatus.rebooted() {
    int i;
    for (i = 0; i < MATE_LOCK_4_COUNT; i++) {
      heap[(int)i].type = MATE_TYPE_NONE;
      heap[(int)i].value.var = 0;
    }
  }
}
