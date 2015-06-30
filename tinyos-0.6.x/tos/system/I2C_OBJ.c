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

#include "tos.h"
#include "I2C_OBJ.h"
#include "dbg.h"

//states
#define READ_DATA 1
#define WRITE_DATA 2
#define SEND_START 3
#define SEND_END 4

#if 0
#define wait() asm volatile  ("nop" ::)
#define SET_CLOCK() sbi(PORTD, 3)
#define CLEAR_CLOCK() cbi(PORTD, 3)
#define MAKE_CLOCK_OUTPUT() sbi(DDRD, 3)
#define MAKE_CLOCK_INPUT() cbi(DDRD, 3)
#define SET_DATA() sbi(PORTD, 4)
#define CLEAR_DATA() cbi(PORTD, 4)
#define MAKE_DATA_OUTPUT() sbi(DDRD, 4)
#define MAKE_DATA_INPUT() cbi(DDRD, 4)
#define GET_DATA() (inp(PIND) >> 4) & 0x1
#endif

void wait() {
    asm volatile  ("nop" ::);
}

#define SET_CLOCK SET_I2C_BUS1_SCL_PIN
#define CLEAR_CLOCK CLR_I2C_BUS1_SCL_PIN
#define MAKE_CLOCK_OUTPUT MAKE_I2C_BUS1_SCL_OUTPUT
#define MAKE_CLOCK_INPUT MAKE_I2C_BUS1_SCL_INPUT

#define SET_DATA SET_I2C_BUS1_SDA_PIN
#define CLEAR_DATA CLR_I2C_BUS1_SDA_PIN
#define MAKE_DATA_OUTPUT MAKE_I2C_BUS1_SDA_OUTPUT
#define MAKE_DATA_INPUT MAKE_I2C_BUS1_SDA_INPUT
#define GET_DATA READ_I2C_BUS1_SDA_PIN

#define TOS_FRAME_TYPE I2C_frame
TOS_FRAME_BEGIN(I2C_frame) {
        char state;
	char data;
}
TOS_FRAME_END(I2C_frame);


static inline void pulse_clock(){
wait();
	SET_CLOCK();
wait();
	CLEAR_CLOCK();
}

char read_bit(){
	char i;
	MAKE_DATA_INPUT();
wait();
	SET_CLOCK();
wait();
	i = GET_DATA();
	CLEAR_CLOCK();
	return i;
}
char i2c_read(){
	char data = 0;
	char i = 0;
	for(i = 0; i < 8; i ++){
		data = (data << 1) & 0xfe;
		if(read_bit() == 1){
			data |= 0x1;
		}
	}
	dbg(DBG_I2C, ("r"));
	return data;
}


char i2c_write(char c){ 
	int i;
	MAKE_DATA_OUTPUT();
	for(i = 0; i < 8; i ++){
		if(c & 0x80){
			dbg(DBG_I2C, ("1"));
			SET_DATA();
		}else{
			dbg(DBG_I2C, ("0"));
			CLEAR_DATA();
		}
		pulse_clock();
		c = c << 1;
	}
 	i = read_bit();	
	dbg(DBG_I2C, ("%x ", c & 0xff));
	return i == 0;
} 
void i2c_start(){
	SET_DATA();
	SET_CLOCK();
	MAKE_DATA_OUTPUT();
wait();
	CLEAR_DATA();
wait();
	CLEAR_CLOCK();
	dbg(DBG_I2C, (" i2c_start\n"));
}
void i2c_ack(){
	MAKE_DATA_OUTPUT();
	CLEAR_DATA();
	pulse_clock();
	dbg(DBG_I2C, (" i2c_ack\n"));
}
void i2c_nack(){
	MAKE_DATA_OUTPUT();
	SET_DATA();
	pulse_clock();
	dbg(DBG_I2C, (" i2c_nack\n"));
}
void i2c_end(){
	MAKE_DATA_OUTPUT();
	CLEAR_DATA();
wait();
	SET_CLOCK();
wait();
	SET_DATA();
	dbg(DBG_I2C, (" i2c_end\n"));
}


TOS_TASK(I2C_task){
    char state = VAR(state); 
    VAR(state) = 0;
    dbg(DBG_I2C,("i2c task posted\n"));
    if((state & 0xf) == READ_DATA){
	TOS_SIGNAL_EVENT(I2C_read_done)(i2c_read(), 1);
	if (state & 0xf0) 
	    i2c_ack();
	else
	    i2c_nack();
    }else if(state == WRITE_DATA){
	    TOS_SIGNAL_EVENT(I2C_write_done)(i2c_write(VAR(data)));
    }else if(state == SEND_START){
	    i2c_start();
	    TOS_SIGNAL_EVENT(I2C_send_start_done)();
    }else if(state == SEND_END){
	    i2c_end();
	    TOS_SIGNAL_EVENT(I2C_send_end_done)();
    }
}



char TOS_COMMAND(I2C_init)(){
	dbg(DBG_I2C, ("i2c_init\n"));
	SET_CLOCK();
	SET_DATA();
	MAKE_CLOCK_OUTPUT();
	MAKE_DATA_OUTPUT();
	VAR(state) = 0;
	VAR(data) = 0;
	return 1;
}

char TOS_COMMAND(I2C_read)(char ack){
    if(VAR(state) != 0) return 0;
    VAR(state) = READ_DATA;
    if (ack) 
	VAR(state) |= 0x10;
    TOS_POST_TASK(I2C_task);
    return 1;
}

char TOS_COMMAND(I2C_write)(char data){
    if(VAR(state) != 0) return 0;
    VAR(state) = WRITE_DATA;
    VAR(data) = data;
    TOS_POST_TASK(I2C_task);
    return 1;
}
char TOS_COMMAND(I2C_send_start)(){
    if(VAR(state) != 0) return 0;
    VAR(state) = SEND_START;
    TOS_POST_TASK(I2C_task);
    return 1;
}   
char TOS_COMMAND(I2C_send_end)(){
    if(VAR(state) != 0) return 0;
    VAR(state) = SEND_END;
    TOS_POST_TASK(I2C_task);
    return 1;
}
