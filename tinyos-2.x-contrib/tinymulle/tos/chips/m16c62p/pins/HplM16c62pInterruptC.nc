/*
 * Copyright (c) 2004-2005 Crossbow Technology, Inc.  All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 * 
 * IN NO EVENT SHALL CROSSBOW TECHNOLOGY OR ANY OF ITS LICENSORS BE LIABLE TO 
 * ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL 
 * DAMAGES ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN
 * IF CROSSBOW OR ITS LICENSOR HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH 
 * DAMAGE. 
 *
 * CROSSBOW TECHNOLOGY AND ITS LICENSORS SPECIFICALLY DISCLAIM ALL WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY 
 * AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE PROVIDED HEREUNDER IS 
 * ON AN "AS IS" BASIS, AND NEITHER CROSSBOW NOR ANY LICENSOR HAS ANY 
 * OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR 
 * MODIFICATIONS.
 */
/**
 * @author Martin Turon <mturon@xbow.com>
 */
 
/**
 * Component providing access to all external interrupt pins on M16c/62P.
 * @author Henrik Makitaavola
 */

configuration HplM16c62pInterruptC
{
  provides
  {
    interface HplM16c62pInterrupt as Int0;
    interface HplM16c62pInterrupt as Int1;
    interface HplM16c62pInterrupt as Int2;
    interface HplM16c62pInterrupt as Int3;
    interface HplM16c62pInterrupt as Int4;
    interface HplM16c62pInterrupt as Int5;
  }
}
implementation
{
  components 
    HplM16c62pInterruptSigP as IrqVector,
    new HplM16c62pInterruptPinP((uint16_t)&INT0IC, 0) as IntPin0,
    new HplM16c62pInterruptPinP((uint16_t)&INT1IC, 1) as IntPin1,
    new HplM16c62pInterruptPinP((uint16_t)&INT2IC, 2) as IntPin2,
    new HplM16c62pInterruptPinP((uint16_t)&INT3IC, 3) as IntPin3,
    new HplM16c62pInterruptPinP((uint16_t)&INT4IC, 4) as IntPin4,
    new HplM16c62pInterruptPinP((uint16_t)&INT5IC, 5) as IntPin5;
  
  Int0 = IntPin0;
  Int1 = IntPin1;
  Int2 = IntPin2;
  Int3 = IntPin3;
  Int4 = IntPin4;
  Int5 = IntPin5;

  IntPin0.IrqSignal -> IrqVector.IntSig0;
  IntPin1.IrqSignal -> IrqVector.IntSig1;
  IntPin2.IrqSignal -> IrqVector.IntSig2;
  IntPin3.IrqSignal -> IrqVector.IntSig3;
  IntPin4.IrqSignal -> IrqVector.IntSig4;
  IntPin5.IrqSignal -> IrqVector.IntSig5;
}

