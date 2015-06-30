module IBADCM
{
  provides {
    interface StdControl;
    interface ADC[uint8_t port];
    interface Excite;
  }
  uses interface I2CPacket;
  uses interface Leds;
  uses interface StdControl as I2CPacketControl;
  uses interface Timer as PowerStabalizingTimer;
  uses interface StdControl as SwitchControl;
  uses interface Switch;
}
implementation
{

  enum {IDLE, SINGLE_PICK_CHANNEL, SINGLE_GET_SAMPLE, SINGLE_GOT_SAMPLE,
	MULT_PICK_CHANNEL,CONTINUE_SAMPLE};

  char state;       /* current state of the i2c request */
  char addr;        /* destination address */
  uint8_t condition;   /* set the condition command byte */
  char flags;       /* flags set on the I2C packets */
  uint16_t value;   /* value of the incoming ADC reading */
  char chan;
  int8_t conversionNumber;   /* the variable that keep number of conversion so far */
  int8_t powerSaveMode;      /*the state of turning off devices after measurement or keep them on*/
  int8_t conversionMode;     /*the state of sampling right away or waiting 100ms for devices warm up after turn on excitation*/
  int8_t averagingMode;      /*can be 1,4,8,16 look at enum in IB.h header file*/

#define VOLTAGE_STABLE_TIME 100           //Time it takes for the supply voltage to be stable enough
  //#define NUMBER_OF_CONVERSION 1           //It can be either 1  4  8 or 16 depending on the needed percision


  //The excitation circuits
#define FIVE_VOLT_ON() TOSH_SET_PW5_PIN()
#define FIVE_VOLT_OFF() TOSH_CLR_PW5_PIN()

#define THREE_VOLT_ON()  TOSH_SET_PW3_PIN()
#define THREE_VOLT_OFF() TOSH_CLR_PW3_PIN()

#define TURN_VOLTAGE_BUFFER_ON() TOSH_SET_PW2_PIN()
#define TURN_VOLTAGE_BUFFER_OFF() TOSH_CLR_PW2_PIN()

#define VOLTAGE_BOOSTER_ON() TOSH_SET_PW1_PIN()
#define VOLTAGE_BOOSTER_OFF() TOSH_CLR_PW1_PIN()

  //The instrumentation amplifier
#define TURN_AMPLIFIERS_ON() TOSH_SET_PW6_PIN()
#define TURN_AMPLIFIERS_OFF() TOSH_CLR_PW6_PIN()

  static inline void setExcitation(uint8_t excitation){
      switch (excitation) {
      case NO_EXCITATION:
          FIVE_VOLT_OFF();
          THREE_VOLT_OFF();
          TURN_VOLTAGE_BUFFER_OFF();
           break;
      case ADCREF:
          FIVE_VOLT_OFF();
          THREE_VOLT_OFF();
          TURN_VOLTAGE_BUFFER_ON();
          break;
      case THREE_VOLT:
          FIVE_VOLT_OFF();
          THREE_VOLT_ON();
          TURN_VOLTAGE_BUFFER_OFF();
           break;
      case FIVE_VOLT:
          FIVE_VOLT_ON();
          THREE_VOLT_OFF();
          TURN_VOLTAGE_BUFFER_OFF();
           break;
      case ALL_EXCITATION:	
          FIVE_VOLT_ON();
          THREE_VOLT_ON();
          TURN_VOLTAGE_BUFFER_ON();
           break;
      default:
          FIVE_VOLT_OFF();
          THREE_VOLT_OFF();
          TURN_VOLTAGE_BUFFER_OFF();
           break;
      }
  }

  static inline result_t convert() {
      if (state == IDLE || state == CONTINUE_SAMPLE)
          {
              state = SINGLE_PICK_CHANNEL;
              /* figure out which channel is to be set */
              if (chan == 0)
                  condition = 8;
              else if (chan == 1)
                  condition = 12;
              else if (chan == 2)
                  condition = 9;
              else if (chan == 3)
                  condition = 13;
              else if (chan == 4)
                  condition = 10;
              else if (chan == 5)
                  condition = 14;
              else if (chan == 6)
                  condition = 11;
              //these channels all use ADC channel 7 and multiplex it.
              else if (chan == 7 || chan==8 || chan==9 || chan==10)  
                  condition = 15;
              else if (chan == 11)
                  condition = 0;
              else if (chan == 12)
                  condition = 1;
              else if (chan == 13)
                  condition = 2;
              else
                  {
                      /* invalid channel number specified */
                      state = IDLE;
                      TURN_AMPLIFIERS_OFF();
                      return FAIL;
                  }
              /* shift the channel and single-ended input bits over */
              condition = (condition << 4) & 0xf0;
              condition = condition | 0x0f;
              /* send the stop condition */
              flags = 0x01;
              /* tell the ADC to start converting */
              if ((call I2CPacket.writePacket(1, (char*)(&condition), flags)) == FAIL)
                  {
                      state = IDLE;
                      TURN_AMPLIFIERS_OFF();
                      return FAIL;
                  }

              return SUCCESS;
          }
      TURN_AMPLIFIERS_OFF();
      return FAIL;
  }



  command result_t StdControl.init() {
      state = IDLE;
      call I2CPacketControl.init();
      call SwitchControl.init();
      TOSH_MAKE_PW2_OUTPUT();
      TOSH_MAKE_PW4_OUTPUT();
      TOSH_MAKE_PW5_OUTPUT();
      TOSH_MAKE_PW6_OUTPUT();
      TURN_AMPLIFIERS_OFF();           
      setExcitation(NO_EXCITATION);
      powerSaveMode=POWER_SAVING_MODE;
      conversionMode=FAST_COVERSION_MODE;
      averagingMode=NO_AVERAGE;
      return SUCCESS;
  }

  command result_t StdControl.start() {
      call SwitchControl.start();
      return SUCCESS;
  }

  command result_t StdControl.stop() {
      call SwitchControl.stop();
      return SUCCESS;
  }

  command result_t Excite.setEx(uint8_t excitation){
      setExcitation(excitation);
      return SUCCESS;
  }

 command result_t Excite.setPowerMode(uint8_t mode){
     powerSaveMode=mode;
     return SUCCESS;
  }

 command result_t Excite.setCoversionSpeed(uint8_t mode){
     conversionMode=mode;
     return SUCCESS;
  }

 command result_t Excite.setAvergeMode(uint8_t mode){
     averagingMode=mode;
     return SUCCESS;
  }

  default event result_t ADC.dataReady[uint8_t id](uint16_t data) {
      return SUCCESS;
  }  

  /* get a single reading from id we */
  command result_t ADC.getData[uint8_t id]() {      
      chan=id;
      value=0;
      conversionNumber=averagingMode;
      //if among the instrumentation channels we set the MUX
      if(chan == 7 || chan==8 || chan==9 || chan==10) 
          {
              TURN_AMPLIFIERS_ON(); 
              switch (chan) {
              case 7:
                  call Switch.setAll(MUX_CHANNEL_SEVEN);
                  break;
              case 8:
                  call Switch.setAll(MUX_CHANNEL_EIGHT);
                  break;
              case 9:
                  call Switch.setAll(MUX_CHANNEL_NINE);
                  break;
              case 10:
                  call Switch.setAll(MUX_CHANNEL_TEN);
                  break;
              default:
              }
              return SUCCESS;
          }
      else {
          //If the conversions happens fast there is no need to
          //wait for settling of the power supply,note that power supply should be set ON by user using the excitation command
          if(conversionMode==SLOW_COVERSION_MODE) {
              call PowerStabalizingTimer.start(TIMER_ONE_SHOT, VOLTAGE_STABLE_TIME);
              return SUCCESS;
          }
          else {
              return convert();
          }
      }
  }

  //Setting the MUX has been done.
  event result_t Switch.setAllDone(bool r) 
      {
          //If the conversions happens fast there is no need to
          //wait for settling of the power supply,note that power supply should be set ON by user using the excitation command
          if(conversionMode==SLOW_COVERSION_MODE) {
              call PowerStabalizingTimer.start(TIMER_ONE_SHOT, VOLTAGE_STABLE_TIME);
              return SUCCESS;
          }
          else {
              return convert();
          }          
          return SUCCESS;
      }


  
  event result_t PowerStabalizingTimer.fired() {      
      return convert();
  }

/* not yet implemented */
 command result_t ADC.getContinuousData[uint8_t id]() {
     return FAIL;
 }

 event result_t I2CPacket.readPacketDone(char length, char* data) {
     if (state == SINGLE_GET_SAMPLE)
         {
             value += (data[1] & 0xff) + ((data[0] << 8) & 0x0f00);
             conversionNumber--;
             //value = (data[0] << 8) & 0x0f00;
             //value += (data[1] & 0xff);        
             if (conversionNumber==0) {
                 state = IDLE;
                 switch(averagingMode)
                     {
                     case NO_AVERAGE:
                         //do nothing
                         break;
                     case FOUR_AVERAGE:
                         value = ((value+2) >>2) & 0x0fff;   //the addition is for more percision 
                         break;
                     case EIGHT_AVERAGE:
                         value = ((value+4) >>3) & 0x0fff;  //the addition is for more percision 
                         break;
                     case SIXTEEN_AVERAGE:
                         value = ((value+8) >>4) & 0x0fff;  //the addition is for more percision 
                         break;
                     default:
                     }
                 if(powerSaveMode==POWER_SAVING_MODE) {
                     setExcitation(NO_EXCITATION);
                     TURN_AMPLIFIERS_OFF();
                 }
                 signal ADC.dataReady[chan](value);
             }
             else {
                 state = CONTINUE_SAMPLE;
                 convert();
             }
         }
     return SUCCESS;
 }

  event result_t I2CPacket.writePacketDone(bool result) {
      if (state == SINGLE_PICK_CHANNEL)
          {
              state = SINGLE_GET_SAMPLE;
              flags = 0x03;
              if ((call I2CPacket.readPacket(2, flags)) == 0)
                  {
                //reading from the bus failed
                      state = IDLE;
                      signal ADC.dataReady[chan](-1);
                  }
              return SUCCESS;
          }

      return SUCCESS;            
  }


  event result_t Switch.getDone(char val) 
      {
          return SUCCESS;
      }
  
  event result_t Switch.setDone(bool r) 
      {
          return SUCCESS;
      }

}
