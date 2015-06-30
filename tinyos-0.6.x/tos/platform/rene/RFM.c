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
 * This component performs bit level control over the RF Monolitics radio.
 * Addtionally, it controls the amount of time per bit by using TCNT1.
 * The sample period can be set to 1/2x, 3/4x, and x. Where x is the 
 * bit transmisison period. 1/2 and 3/4 are provided to do sampling 
 * and then read at the point half way between samples.
 * 
 */



#include "tos.h"
#include "RFM.h"
#include "dbg.h"

#define TOS_FRAME_TYPE RFM_frame
TOS_FRAME_BEGIN(RFM_frame) {
        char state;
#ifdef DOT
	char precision;
#endif
}
TOS_FRAME_END(RFM_frame);

//states:
// 0 == receive mode;
// 1 == transmit mode;
// 2 == low power mode;

extern int bit_timer;
extern char increment;
extern char value1;

/* This is a SIGNAL handler that timer1 generates to trigger this component to sample on the radio */
TOS_SIGNAL_HANDLER(SIG_OUTPUT_COMPARE1A, ()){

    char in;
#ifdef DOT
    int avilable = 0x190;
    if (VAR(precision)) {
	value1 += increment;
	avilable = bit_timer;
	if (value1 & 0x10) {
	    avilable++;
	    value1 &= 0xf;
	}
	outp(avilable >> 8, OCR1AH); // set upper byte of comp reg.
	outp(avilable & 0xff, OCR1AL); // set the lower byte compare
    }
#endif

    //debug: set this pin high at the start of interrupt so 
    // sample times can me measured on a scope.
    if(VAR(state) == 1){
	//if we are writing, then fire the bit send event.
      TOS_SIGNAL_EVENT(RFM_TX_BIT_EVENT)(); 
    } else if(VAR(state) == 0){
	//if we are reading, read in the value.
        in = READ_RFM_RXD_PIN();
	//fire the bit arrived event and send up the value.
	TOS_SIGNAL_EVENT(RFM_RX_BIT_EVENT)(in);
    }
}


/* This command sets the RFM component (radio) to transmit bit "data" */
char TOS_COMMAND(RFM_TX_BIT)(char data){
  //if not in the transmit mote fail.
  if(VAR(state) != 1) return 0;

  //sent the output pin accordingly.
  if(data & 0x01){
    SET_RFM_TXD_PIN();
  }
  else{
    CLR_RFM_TXD_PIN();
  }

  dbg(DBG_RADIO, ("transmitting %x\n", data & 0x01));
  
  return 1;
}

/* This command sets the RFM component (radio) into different power mode */
char TOS_COMMAND(RFM_PWR)(char mode){
  if(mode == 0){
    //turn off the RFM chip.
    CLR_RFM_CTL0_PIN();
    CLR_RFM_CTL1_PIN();
    // disable timer1 interupt
    outp(0x00, TCCR1B); // scale the counter
    cbi(TIMSK, OCIE1A); 
    //record the current state.
    VAR(state) = 2;
  }else if(mode == 1){
    VAR(state) = 3;
    outp(0x09, TCCR1B); // scale the counter
    sbi(TIMSK, OCIE1A); 
  }else if (mode == 2){
    // turn off the RFM chip
    CLR_RFM_CTL0_PIN();
    CLR_RFM_CTL1_PIN();
  }
  return 1;
}

/* This command sets the RFM component (radio) into transmit mode */
char TOS_COMMAND(RFM_TX_MODE)(){
  if(VAR(state) == 2) return 0;

  //set the RFM chip to TX mode.
  SET_RFM_CTL0_PIN();
  CLR_RFM_CTL1_PIN();
  
  dbg(DBG_RADIO, ("RADIO: set TX mode....\n"));
  
  //record the current state.
  VAR(state) = 1;
  return 1;
}



/* This command sets the RFM component (radio) into receiving mode */
char TOS_COMMAND(RFM_RX_MODE)(){
  if(VAR(state) == 2) return 0;
  //set the RFM to RX mode.
  SET_RFM_CTL0_PIN();
  SET_RFM_CTL1_PIN();
  CLR_RFM_TXD_PIN();
  
  dbg(DBG_RADIO, ("RADIO: set RX mode....\n"));
  
  //record the current state.
  VAR(state) = 0;
  return 1;
}


/* This command sets timer1 to different sampling level */
char TOS_COMMAND(RFM_SET_BIT_RATE)(char level){

    if(level == 0){
#ifdef DOT
      VAR(precision) = 0;
#endif
      outp(0x00, OCR1AH); // set upper byte of comp reg.
      outp(0xc8, OCR1AL); // set the lower byte compare
      outp(0x00, TCNT1H); // clear current counter value
      outp(0x00, TCNT1L); // clear current couter high byte value
    }else if(level == 1){
#ifdef DOT
      VAR(precision) = 0;
#endif
      outp(0x01, OCR1AH); // set upper byte of comp reg.
      outp(0x2c, OCR1AL); // set the lower byte compare
    }else if(level == 2){
#ifdef DOT
      VAR(precision) = 1;
      outp(bit_timer >> 8, OCR1AH); // set upper byte of comp reg.
      outp(bit_timer & 0xff, OCR1AL); // set the lower byte compare
#else 
      outp(0x01, OCR1AH); // set upper byte of comp reg.
      outp(0x90, OCR1AL); // set the lower byte compare
#endif
    }

    return 1;
}


/* Initialization of the Component */
char TOS_COMMAND(RFM_INIT)(){

  //Reset to idle state.
  VAR(state) = 0;
  
  //set the RFM pins.
  SET_RFM_CTL0_PIN();
  SET_RFM_CTL1_PIN();
  CLR_RFM_TXD_PIN();
  
  cbi(TIMSK, OCIE1A); //clear interrupts
  cbi(TIMSK, TICIE1); //clear interrupts
  cbi(TIMSK, TOIE1);  //clear interrupts
  cbi(TIMSK, OCIE1B); //clear interrupts
  outp(0x09, TCCR1B); //scale the counter
  outp(0x00, TCCR1A);
  outp(0x00, OCR1AH); // set upper byte of comp reg.
  outp(0xc8, OCR1AL); // set the lower byte compare
  sbi(TIMSK, OCIE1A); // enable timer1 interupt
  outp(0x00, TCNT1H); // clear current counter value
  outp(0x00, TCNT1L); // clear current couter high byte value
  sei(); //enable system interrupts.
  
  dbg(DBG_BOOT, ("RFM initialized\n"));

  return 1;
}
