// $Id: MonibusHPLUARTC.nc,v 1.3 2005/06/20 08:09:51 neturner Exp $

/*
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
 */

/**
 * @author Neil E. Turner
 */

configuration MonibusHPLUARTC {
  provides interface HPLUART as UART;
}

implementation {
  components MonibusHPLUARTM, HPLUSART0M as HPLUSART, LedsC, BusArbitrationC;

  UART=MonibusHPLUARTM;

  MonibusHPLUARTM.UARTControl -> HPLUSART;
  MonibusHPLUARTM.UARTData -> HPLUSART;
  MonibusHPLUARTM.BusArbitration ->
    BusArbitrationC.BusArbitration[unique("BusArbitration")];
  MonibusHPLUARTM.Leds -> LedsC;

}
