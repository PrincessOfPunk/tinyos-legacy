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
 * Authors:  Wei Hong
 *           Intel Research Berkeley Lab
 * Date:     6/27/2002
 *
 */

includes SchemaType;
includes Attr;

/** Interface for registering attributes.	
    <p>
    See lib/Attributes/... for examples of components that register attributes
    <p>
    See interfaces/Attr.h for the data structures used in this interface.  See
    docs/tinyschema.pdf for more detailed documentation.
    <p>
    Implemented by lib/Attr.td
    <p>

    @author Wei Hong (wei.hong@intel-research.net)
*/
interface AttrRegister
{
  /** Register an attribute with the specified name, type, and length.
	@param name The name of the attribute to register.
	@param attrType The type of the attribute to register (see SchemaType.h for a list of types.)
	@param attrLen The length (in bytes) of the attribute.
  */
  command result_t registerAttr(char *name, TOSType attrType, uint8_t attrLen);

  /** The provider (client) receives this event when a request for an attribute
      value is made.  See AttrUse.td.  The provider must fetch the value
      of the attribute (and/or set the errorNo.)  If this is a split phase
      get, the client must call getAttrDone() once the fetch is complete.
      @param name The name of the attribute to get
      @param resultBuf The buffer to write the result into
      @param errorNo (on return) The error code, if any (see SchemaType.h)
  */
  event result_t getAttr(char *name, char *resultBuf, SchemaErrorNo *errorNo);

  /* The provider (client) receives this event when a request to set an attribute
     value is made. 
     @param name The name of the attribute to set
     @param attrValue The value to set the attribute to
  */
  event result_t setAttr(char *name, char *attrVal);
  
  /** Should be called when a split-phase attribute get has completed.
      @param name The name of the attribute that was fetched
      @param resultBuf The buffer where the value of the result was written
      @param errorNo An error code
  */
  command result_t getAttrDone(char *name, char *resultBuf, SchemaErrorNo errorNo);
}
