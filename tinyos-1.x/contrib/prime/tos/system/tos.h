/*									tab:4
 *
 *
 * "Copyright (c) 2000-2002 The Regents of the University  of California.  
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
/*									tab:4
 *  IMPORTANT: READ BEFORE DOWNLOADING, COPYING, INSTALLING OR USING.  By
 *  downloading, copying, installing or using the software you agree to
 *  this license.  If you do not agree to this license, do not download,
 *  install, copy or use the software.
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
 *
 * Authors:		Jason Hill, David Gay, Philip Levis, Nelson Lee
 * Date last modified:  6/25/02
 *
 */

#ifndef __CYGWIN__
#include <inttypes.h>
#else //__CYGWIN
// Must be PLATFORM_PC...
#include <unistd.h>
#include <stdio.h>
#include <sys/types.h>
#endif //__CYGWIN
#include <string.h>
#include <stdlib.h>
#include <math.h>
#include <stddef.h>
#include <ctype.h>

#ifndef GENERICCOMM
# define GENERICCOMM GenericComm //the name of the default generic comm component
#endif

#ifndef GENERICCOMMPROMISCUOUS
# define GENERICCOMMPROMISCUOUS GenericCommPromiscuous //the name of the default promiscuous generic comm component
#endif

typedef unsigned char bool;
enum {
  FALSE = 0,
  TRUE = 1
};

uint16_t TOS_LOCAL_ADDRESS = 1;

enum { /* standard codes for result_t */
  FAIL = 0,
  SUCCESS = 1
};

#if NESC >= 110
uint8_t rcombine(uint8_t r1, uint8_t r2); // keep 1.1alpha1 happy
typedef uint8_t result_t __attribute__((combine(rcombine)));
#else
typedef uint8_t result_t;
#define atomic
#define async
#define norace
#endif

result_t rcombine(result_t r1, result_t r2)
/* Returns: FAIL if r1 or r2 == FAIL , r2 otherwise. This is the standard
     combining rule for results
*/
{
  return r1 == FAIL ? FAIL : r2;
}

result_t rcombine3(result_t r1, result_t r2, result_t r3)
{
  return rcombine(r1, rcombine(r2, r3));
}

result_t rcombine4(result_t r1, result_t r2, result_t r3,
				 result_t r4)
{
  return rcombine(r1, rcombine(r2, rcombine(r3, r4)));
}

#undef NULL
enum {
	NULL = 0x0
};

#include <hardware.h>
#include <dbg.h>
#include <sched.c>

// buggy in avr-gcc 3.1
void *nmemcpy(void *to, const void *from, size_t n)
{
  char *cto = to;
  const char *cfrom = from;

  while (n--) *cto++ = *cfrom++;
  
  return to;
}

void *nmemset(void *to, int val, size_t n)
{
  char *cto = to;

  while (n--) *cto++ = val;

  return to;
}

#if 0 /* to be turned by David Gay later */
int strcasecmp(const char *s1, const char *s2)
{
   while (*s1 && *s2)
     {
       int c1 = tolower(*s1++), c2 = tolower(*s2++);

       if (c1 != c2)
	return c1 - c2;
     }
   if (*s1)
     return 1;
   else if (*s2)
     return -1;
   else
     return 0;
}
#endif

#define memcpy nmemcpy
#define memset nmemset


/* avr-gcc lib 3.3 for 128s defines an output() macro. 
 * This is clearly a bad idea.
 * But we have to live with it. The problem is that
 * the IntOutput interface has a command, output; if we don't
 * undef gcc's output. the compiler tosses an error at you.
 * -pal, 6/2/03
 */ 
#ifdef output
#undef output
#endif
