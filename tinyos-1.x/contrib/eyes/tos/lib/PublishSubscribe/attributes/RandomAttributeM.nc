/* 
 * Copyright (c) 2004, Technische Universitaet Berlin
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * - Redistributions of source code must retain the above copyright notice,
 *   this list of conditions and the following disclaimer.
 * - Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * - Neither the name of the Technische Universitaet Berlin nor the names
 *   of its contributors may be used to endorse or promote products derived
 *   from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
 * TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
 * OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * - Revision -------------------------------------------------------------
 * $Revision: 1.1 $
 * $Date: 2005/11/18 15:50:45 $
 * @author: Jan Hauer <hauer@tkn.tu-berlin.de>
 * ========================================================================
 */
module RandomAttributeM {
  provides interface StdControl; 
  provides interface PSValue;
  provides interface PSOperation;
  uses interface Random;
}
implementation {
  uint16_t count;

  enum {
    // as defined in attributes.xml
    PS_OPR_EQUALS = 0,
    PS_OPR_SMALLER = 1,
    PS_OPR_SMALLER_EQUAL = 2,
    PS_OPR_GREATER = 3,
    PS_OPR_GREATER_EQUAL = 4,
    PS_OPR_ANY = 5,
  };
  
  command result_t StdControl.init() {
    call Random.init();
    return SUCCESS;  
  }
  
  command result_t StdControl.stop() { 
    return SUCCESS; 
  }

  command result_t StdControl.start() {
    return SUCCESS;
  }

  command ps_result_t PSValue.getValueSize(uint8_t *valueSize){
    *valueSize = 2;
    return PS_SUCCESS;
  }
  
  command ps_result_t PSValue.getValue(void *buffer){
    *((nx_uint16_t *) buffer) = call Random.rand();
    signal PSValue.valueReady(PS_SUCCESS, buffer);
    return PS_SUCCESS;
  }

  command bool PSOperation.isMatching(const void *value1, 
      const ps_opr_ID_t *operation, const void *value2)
  {
    switch(*operation)
    {
      case PS_OPR_EQUALS: return *((nx_uint16_t *) value1) == *((nx_uint16_t *) value2);
      case PS_OPR_SMALLER: return *((nx_uint16_t *) value1) < *((nx_uint16_t *) value2);
      case PS_OPR_SMALLER_EQUAL: return *((nx_uint16_t *) value1) <= *((nx_uint16_t *) value2);
      case PS_OPR_GREATER: return *((nx_uint16_t *) value1) > *((nx_uint16_t *) value2);
      case PS_OPR_GREATER_EQUAL: return *((nx_uint16_t *) value1) >= *((nx_uint16_t *) value2);
      case PS_OPR_ANY: return TRUE;
    }
    return FALSE;
  }
}

