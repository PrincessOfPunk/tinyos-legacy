/*									tab:4
 * "Copyright (c) 2000-2003 The Regents of the University  of California.  
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
 * Copyright (c) 2002-2003 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704.  Attention:  Intel License Inquiry.
 */
/*
 *
 * Authors:		Joe Polastre
 *
 * $Id: sensorboard.h,v 1.17 2003/12/19 17:28:05 idgay Exp $
 */

#define PRESSURE_POWER_ON() { TOSH_MAKE_ADC6_OUTPUT(); TOSH_SET_ADC6_PIN(); }
#define PRESSURE_POWER_OFF()           TOSH_CLR_ADC6_PIN()

#define PRESSURE_INTERRUPT             SIG_INTERRUPT0

#define PRESSURE_SET_CLOCK()           TOSH_SET_PWM1B_PIN()
#define PRESSURE_CLEAR_CLOCK()         TOSH_CLR_PWM1B_PIN()
#define PRESSURE_MAKE_CLOCK_OUTPUT()   TOSH_MAKE_PWM1B_OUTPUT()
#define PRESSURE_MAKE_IN_INPUT()       TOSH_MAKE_INT0_INPUT()
#define PRESSURE_MAKE_OUT_OUTPUT()     TOSH_MAKE_GPS_ENA_OUTPUT()
#define PRESSURE_READ_IN_PIN()         TOSH_READ_INT0_PIN()
#define PRESSURE_SET_IN_PIN()          TOSH_SET_INT0_PIN()
#define PRESSURE_CLEAR_IN_PIN()        TOSH_CLR_INT0_PIN()
#define PRESSURE_SET_OUT_PIN()         TOSH_SET_GPS_ENA_PIN()
#define PRESSURE_CLEAR_OUT_PIN()       TOSH_CLR_GPS_ENA_PIN()

#define HUMIDITY_POWER_ON() { TOSH_MAKE_ADC7_OUTPUT(); TOSH_SET_ADC7_PIN(); }
#define HUMIDITY_POWER_OFF()         TOSH_CLR_ADC7_PIN()

#define HUMIDITY_INT_ENABLE()        sbi(EIMSK , 1)
#define HUMIDITY_INT_DISABLE()       cbi(EIMSK , 1)

#define HUMIDITY_INTERRUPT           SIG_INTERRUPT1

#define HUMIDITY_MAKE_CLOCK_OUTPUT() TOSH_MAKE_PW0_OUTPUT() //sbi(DDRC, 3)
#define HUMIDITY_MAKE_CLOCK_INPUT()  TOSH_MAKE_PW0_INPUT() //sbi(DDRC, 3)
#define HUMIDITY_SET_CLOCK()         TOSH_SET_PW0_PIN()   // sbi(PORTC, 3)
#define HUMIDITY_CLEAR_CLOCK()       TOSH_CLR_PW0_PIN() // cbi(PORTC, 3)
#define HUMIDITY_SET_DATA()          TOSH_SET_INT1_PIN()   // sbi(PORTD, 3)
#define HUMIDITY_CLEAR_DATA()        TOSH_CLR_INT1_PIN() // cbi(PORTD, 3)
#define HUMIDITY_MAKE_DATA_OUTPUT()  TOSH_MAKE_INT1_OUTPUT() // sbi(DDRD, 3)
#define HUMIDITY_MAKE_DATA_INPUT()   TOSH_MAKE_INT1_INPUT() //cbi(DDRD, 3)
#define HUMIDITY_GET_DATA()          TOSH_READ_INT1_PIN()  // (inp(PIND) >> 3) & 0x1
#define HUMIDITY_TIMEOUT_MS          30
#define HUMIDITY_TIMEOUT_TRIES       20

#define PRESSURE_TIMEOUT_TRIES       40

#define TAOS_TIMEOUT_MS              50
#define TAOS_TIMEOUT_TRIES           20

#define TAOS_POWER_ON() { TOSH_MAKE_ADC4_OUTPUT(); TOSH_SET_ADC4_PIN(); }
#define TAOS_POWER_OFF()             TOSH_CLR_ADC4_PIN()

#define TAOS_MAKE_CLOCK_INPUT()      TOSH_MAKE_PW0_INPUT()
#define TAOS_MAKE_CLOCK_OUTPUT()     TOSH_MAKE_PW0_OUTPUT()
#define TAOS_SET_CLOCK()             TOSH_SET_PW0_PIN()
#define TAOS_CLEAR_CLOCK()           TOSH_CLR_PW0_PIN()

#define TAOS1_SET_DATA()             TOSH_SET_ADC5_PIN()
#define TAOS1_CLEAR_DATA()           TOSH_CLR_ADC5_PIN()
#define TAOS1_MAKE_DATA_OUTPUT()     TOSH_MAKE_ADC5_OUTPUT()
#define TAOS1_MAKE_DATA_INPUT()      TOSH_MAKE_ADC5_INPUT()
#define TAOS1_GET_DATA()             TOSH_READ_ADC5_PIN()

#define TAOS2_SET_DATA()             TOSH_SET_PW1_PIN()
#define TAOS2_CLEAR_DATA()           TOSH_CLR_PW1_PIN()
#define TAOS2_MAKE_DATA_OUTPUT()     TOSH_MAKE_PW1_OUTPUT()
#define TAOS2_MAKE_DATA_INPUT()      TOSH_MAKE_PW1_INPUT()
#define TAOS2_GET_DATA()             TOSH_READ_PW1_PIN()

enum {
  HAMAMATSU1_ADC_PORT = 2,
  HAMAMATSU2_ADC_PORT = 3
};

#define MELEXIS_MAKE_SELECT_OUTPUT() TOSH_MAKE_ADC5_OUTPUT()
#define MELEXIS_MAKE_SELECT_INPUT()  TOSH_MAKE_ADC5_INPUT()
#define MELEXIS_SET_SELECT_PIN()     TOSH_SET_ADC5_PIN()
#define MELEXIS_CLEAR_SELECT_PIN()   TOSH_CLR_ADC5_PIN()
#define MELEXIS_MAKE_SHDN_OUTPUT()   TOSH_MAKE_ADC6_OUTPUT()
#define MELEXIS_MAKE_SHDN_INPUT()    TOSH_MAKE_ADC6_INPUT()
#define MELEXIS_SET_SHDN_PIN()       TOSH_SET_ADC6_PIN()
#define MELEXIS_CLEAR_SHDN_PIN()     TOSH_CLR_ADC6_PIN()

#define THERMOPILE_SET_CLOCK()           TOSH_SET_PWM1B_PIN()
#define THERMOPILE_CLEAR_CLOCK()         TOSH_CLR_PWM1B_PIN()
#define THERMOPILE_MAKE_CLOCK_OUTPUT()   TOSH_MAKE_PWM1B_OUTPUT()
#define THERMOPILE_MAKE_CLOCK_INPUT()    TOSH_MAKE_PWM1B_INPUT()
#define THERMOPILE_MAKE_IN_INPUT()       TOSH_MAKE_INT0_INPUT()
#define THERMOPILE_MAKE_OUT_OUTPUT()     TOSH_MAKE_GPS_ENA_OUTPUT()
#define THERMOPILE_MAKE_OUT_INPUT()      TOSH_MAKE_GPS_ENA_INPUT()
#define THERMOPILE_READ_IN_PIN()         TOSH_READ_INT0_PIN()
#define THERMOPILE_SET_OUT_PIN()         TOSH_SET_GPS_ENA_PIN()
#define THERMOPILE_CLEAR_OUT_PIN()       TOSH_CLR_GPS_ENA_PIN()

  const char crctable[256] = {
    0, 49, 98, 83, 196, 245, 166, 151, 185, 136, 219, 234, 125, 76, 31, 46,
    67, 114, 33, 16,
    135, 182, 229, 212, 250, 203, 152, 169, 62, 15, 92, 109, 134, 183, 228,
    213, 66, 115, 32, 17,
    63, 14, 93, 108,251, 202,153, 168,197, 244,167, 150,1, 48, 99, 82, 124, 
    77, 30, 47,
    184, 137,218, 235,61, 12, 95, 110,249, 200,155, 170,132, 181,230, 215,
    64, 113,34, 19,
    126, 79, 28, 45, 186, 139,216, 233,199, 246,165, 148,3, 50, 97, 80, 187, 
    138,217, 232,
    127, 78, 29, 44, 2, 51, 96, 81, 198, 247,164, 149,248, 201,154, 171,60, 
    13, 94, 111,
    65, 112,35, 18, 133, 180,231, 214,122, 75, 24, 41, 190, 143,220, 237,
    195, 242,161, 144,
    7, 54, 101, 84, 57, 8, 91, 106,253, 204,159, 174,128, 177,226, 211,68, 
    117,38, 23,
    252, 205,158, 175,56, 9, 90, 107,69, 116,39, 22, 129, 176,227, 210,191,
    142,221, 236,
    123, 74, 25, 40, 6, 55, 100, 85, 194, 243,160, 145,71, 118,37, 20, 131, 
    178,225, 208,
    254, 207,156, 173,58, 11, 88, 105,4, 53, 102, 87, 192, 241,162, 147,189, 
    140,223, 238,
    121, 72, 27, 42, 193, 240,163, 146,5, 52, 103, 86, 120, 73, 26, 43, 188, 
    141,222, 239,
    130, 179,224, 209,70, 119,36, 21, 59, 10, 89, 104,255, 206,157, 172};



enum {
  // I2C Taos Photo Sensor Switch Address
  MICAWB_TAOS_ADDR = 57,

  // Thermopile address and constants
  MICAWB_THERM_POWER  = 8,
  MICAWB_THERM_RD_REG = 0x48,
  MICAWB_THERM_WR_REG = 0x50,
  MICAWB_THERM_RD_EE  = 0x08,
  MICAWB_THERM_WR_EE  = 0x10,
  MICAWB_THERM_ER_EE  = 0x20,
  MICAWB_THERM_BW_EE  = 0x90,
  MICAWB_THERM_BE_EE  = 0xA0,
  MICAWB_THERM_ADDR_CONFIG0 = 0x03,
  MICAWB_THERM_ADDR_CONFIG1 = 0x04,
  MICAWB_THERM_IROUT = 0x09,
  MICAWB_THERM_TOUT  = 0x0A,
  MICAWB_THERM_TEST_REG = 0x10,
  MICAWB_THERM_WP_REG = 0x18,

  MELEXIS_WP_EN = 0x0065,
  MELEXIS_TEST_EN = 0xB200,

  // Sensirion Humidity addresses and commands
  TOSH_HUMIDITY_ADDR = 5,
  TOSH_HUMIDTEMP_ADDR = 3,
  TOSH_HUMIDITY_RESET = 0x1E
};

