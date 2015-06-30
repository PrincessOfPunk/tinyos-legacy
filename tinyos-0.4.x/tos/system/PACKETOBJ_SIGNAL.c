/*									tab:4
 *
 *
 * "Copyright (c) 2000 and The Regents of the University 
 * of California.  All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice and the following
 * two paragraphs appear in all copies of this software.
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
 * Authors:		Jason Hill
 *
 *
 */

/*
   Conributers : Edward Cedeno 		(edddie@eden.rutgers.edu)
                 Christopher Carrer (ccarrer@eden.rutgers.edu)
                 Kevin Wine 		(kevinw@liman.Rutgers.edu)
   Organization: Rutgers University.
   Date        : 6 Sept 2001
   
   This code is different from the original version in that it will only take one 
   high order sample (1 bit instead of 0) and the sampling rate has been increased.
   This change was made so that the call stack can be reduced and the chances of the
   sample discharging in the cummulator is decreased.  There are no averages taken,
   but rather you will be getting the raw sample.  This still provides a more consistent
   result.   
*/

#include "tos.h"
#include "PACKETOBJ_SIGNAL.h"


#define TOS_FRAME_TYPE PACKET_obj_frame
TOS_FRAME_BEGIN(PACKET_obj_frame) 
{
    char state;
    char count;
	TOS_Msg buffer;
	char* rec_ptr;
	char* send_ptr;
	int strength;
}
TOS_FRAME_END(PACKET_obj_frame);



char TOS_COMMAND(PACKET_TX_PACKET)(TOS_MsgPtr msg)
{
    if(VAR(state) == 0)
    {
		VAR(send_ptr) = (char*)msg;
		VAR(state) = 1;
		VAR(count) = 1;
		if(TOS_CALL_COMMAND(PACKET_SUB_TX_BYTES)(VAR(send_ptr)[0]))
		{
	    	return 1;
		}
		else
		{
	    	VAR(state) = 0;
	    	VAR(count) = 0;
	    	return 0;
		}
    }
    else
    {
		return 0;
    }
}

void TOS_COMMAND(PACKET_POWER)(char mode)
{
    //do this later;
    ;
}


char TOS_COMMAND(PACKET_INIT)()
{
    TOS_CALL_COMMAND(PACKET_SUB_INIT)();
    VAR(rec_ptr) = (char*)&VAR(buffer);
    VAR(state) = 0;

    return 1;
} 


char TOS_EVENT(PACKET_RX_BYTE_READY)(char data, char error, int strength)
{

    if(error)
	{
		VAR(state) = 0;
		return 0;
    }
    if(VAR(state) == 0)
	{
		VAR(state) = 5;
		VAR(count) = 1;
		VAR(rec_ptr)[0] = data;
		VAR(strength) = strength;
    }
	else if(VAR(state) == 5)
	{
		VAR(rec_ptr)[(int)VAR(count)] = data;
		VAR(count)++;
	
		if(VAR(count) == sizeof(TOS_Msg) - sizeof(((TOS_MsgPtr)(VAR(rec_ptr)))->strength))
		{
	    		TOS_MsgPtr tmp;
	    		
	    		VAR(state) = 0;
	   	 
            	//Ed and Chris Here the value of strength is placed in the packet and then the pointer to the packet is signaled up
	    		((TOS_MsgPtr)(VAR(rec_ptr)))->strength = VAR(strength);
	    		tmp = TOS_SIGNAL_EVENT(PACKET_RX_PACKET_DONE)((TOS_Msg*)VAR(rec_ptr));
	    
            	if(tmp != 0) VAR(rec_ptr) = (char*)tmp;
	    		return 0;
		}
    }
    return 1;
}


char TOS_EVENT(PACKET_TX_BYTE_READY)(char success)
{
    if(success == 0)
    {
		TOS_SIGNAL_EVENT(PACKET_TX_PACKET_DONE)(0);
		VAR(state) = 0;
		VAR(count) = 0;
    }
    if(VAR(state) == 1)
    {
		if(VAR(count) < sizeof(TOS_Msg)  - sizeof( ((TOS_MsgPtr)(VAR(rec_ptr)))->strength))
		{
			TOS_CALL_COMMAND(PACKET_SUB_TX_BYTES)(VAR(send_ptr)[(int)VAR(count)]);
	    	VAR(count) ++;
		}
		else if(VAR(count) == sizeof(TOS_Msg)  - sizeof( ((TOS_MsgPtr)(VAR(rec_ptr)))->strength))
		{
	    	VAR(count)++;
	    	return 0;
		}
		else
		{
	    	VAR(state) = 0;
	    	VAR(count) = 0;
	    	TOS_SIGNAL_EVENT(PACKET_TX_PACKET_DONE)((TOS_MsgPtr)VAR(send_ptr));
	    	return 0;
		}
   }
   return 1; 
}

char TOS_EVENT(PACKET_BYTE_TX_DONE)()
{
    return 1;
}
