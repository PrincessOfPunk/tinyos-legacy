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

module OPgetsetvar2M {
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
    interface MateVirus as Virus;
  }
}

implementation {

  typedef enum {
    MATE_LOCK_2_0 = unique("MateLock"),
    MATE_LOCK_2_1 = unique("MateLock"),
    MATE_LOCK_2_2 = unique("MateLock"),
    MATE_LOCK_2_3 = unique("MateLock"),
    MATE_LOCK_2_COUNT = 4,
  } LockNames;

  
  MateStackVariable heap[MATE_LOCK_2_COUNT];

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
      return MATE_LOCK_2_0;
    case 1:
      return MATE_LOCK_2_1;
    case 2:
      return MATE_LOCK_2_2;
    case 3:
      return MATE_LOCK_2_3;
    default:
      return 255;
    }
  }

  command int16_t LockGet.lockNum(uint8_t instr) {
    uint8_t arg = instr - OP_GETVAR2;
    dbg(DBG_USR2, "OPgetsetvar2M: LockGet.lockNum called with rval of %i\n", (int)arg);
    return varToLock(arg);
  }

  command int16_t LockSet.lockNum(uint8_t instr) {
    uint8_t arg = instr - OP_SETVAR2;
    dbg(DBG_USR2, "OPgetsetvar2M: LockSet.lockNum called with rval of %i\n", (int)arg);
    return varToLock(arg);
  }
  
  command result_t Get.execute(uint8_t instr,
			       MateContext* context) {
    uint8_t arg = instr - OP_GETVAR2;
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
    uint8_t arg = instr - OP_SETVAR2;
    uint8_t lock = varToLock(arg);
    if ((lock == 255) || !call Locks.isHeldBy(lock, context)) {
      call Error.error(context, MATE_ERROR_INVALID_ACCESS);
      return FAIL;
    }

    MateStackVariable* var = call Stacks.popOperand(context);
    if (!call Types.checkTypes(context, var, (MATE_TYPE_INTEGER | MATE_VAR_S))) {
      return FAIL;
    }
    dbg(DBG_USR1, "VM (%i): OPsetvarM (%i) executed.\n", (int)context->which, (int)arg);
    heap[arg] = *var;
    return SUCCESS;
  }
  command uint8_t Set.byteLength() {return 1;}
  
  event result_t Virus.capsuleInstalled(MateCapsule* capsule) {
    int i;
    for (i = 0; i < MATE_HEAPSIZE; i++) {
      heap[(int)i].type = MATE_TYPE_INTEGER;
      heap[(int)i].value.var = 0;
    }
    return SUCCESS;
  }

  event result_t Virus.enableExecution() {
    return SUCCESS;
  }

  event result_t Virus.disableExecution() {
    return SUCCESS;
  }

  event result_t Virus.capsuleHeard(uint8_t type) {
    return SUCCESS;
  }

  event void Virus.capsuleForce(uint8_t type) {
    return;
  }
}
