/*
 * "Copyright (c) 2002 and The Regents of the University 
 * of California.  All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and
 * its documentation for any purpose, without fee, and without written
 * agreement is hereby granted, provided that the above copyright
 * notice, the following two paragraphs and the author appear in all
 * copies of this software.
 * 
 * IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY
 * PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL
 * DAMAGES ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS
 * DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE
 * PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 * CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT,
 * UPDATES, ENHANCEMENTS, OR MODIFICATIONS."
 *
 * Authors:		Philip Levis
 *              Neil Patel
 * Date last modified:  9/10/02
 *
 */

/**
 * @author Philip Levis
 * @author Neil Patel
 */

includes Bombilla;
includes AbstractConstants;

/**
   AbstractMate is a test application for Bombilla, the TinyOS
   communication-centric bytecode interpreter. It includes the VM and
   all of its subcomponents. Installing this application on a mote
   will install Bombilla, which by default has a program similar to
   CntToLedsAndRFM. new programs can be installed on motes over the
   network. A Bombilla tutorial and reference are provided in the
   TinyOS release, outlining its capabilities and uses.

   The inclusion of an application-specific of GenericComm and
   GenericCommPromiscuous is necessary due to the combine
   rules on message buffers. Specifically, some AM types in the
   VM are promiscuous (ad-hoc routing), while others are not.
   Including both GenericComms as is will cause both to be signaled,
   confusing the networking stack on which buffer is passed back.
   The fix is to be promiscuous if wired as such, but to default to
   a normal filtered AM.
*/

configuration AbstractMate {}
implementation
{
  components BombillaEngine as VM;
  components Main; 
    
  components OPhalt, OPputled, OPcopy, OPadd, OPland, OPlor, OPlnot;
  components OPand, OPor, OPnot, OPerr, OPcpull, OPcpush, OPgetradio;
  components OPctrue, OPcfalse, OPinv, OPsense, OPid, OPrand, OPret;
  components OPbpush1, OPcall2;
  components OPpushc6;

  components OPuart, OPsendr, OPsend;
  components OPbhead, OPbtail, OPbclear, OPbsize, OPpop, OPbsorta, OPbsortd;
  components OPbfull, OPcast, OPunlock, OPunlockb, OPpunlock, OPpunlockb;
  components OPbget, OPbyank, OPswap, OPshiftr, OPshiftl, OPmod, OPeq;
  components OPlt, OPgt, OPlte, OPgte, OPeqtype;
  components OPgetms, OPgetmb, OPsetms, OPsetmb;
  components OPjumpc5, OPjumps5;
  components OPgetsetvar4, OPsetradio;
  components OPgetsetlocal3;

  components OnceContext, ClockContext, RecvContext;
  
  Main.StdControl -> VM;

  VM.Bytecode[OPhalt] -> OPhalt;
  VM.Bytecode[OPputled] -> OPputled;
  VM.Bytecode[OPadd] -> OPadd;
  VM.Bytecode[OPcopy] -> OPcopy;
  VM.Bytecode[OPland] -> OPland;
  VM.Bytecode[OPand] -> OPand;
  VM.Bytecode[OPor] -> OPor;
  VM.Bytecode[OPlor] -> OPlor;
  VM.Bytecode[OPlnot] -> OPlnot;
  VM.Bytecode[OPnot] -> OPnot;
  VM.Bytecode[OPinv] -> OPinv;
  VM.Bytecode[OPcpull] -> OPcpull;
  VM.Bytecode[OPcpush] -> OPcpush;
  VM.Bytecode[OPerr] -> OPerr;
  VM.Bytecode[OPgetradio] -> OPgetradio;
  VM.Bytecode[OPid] -> OPid;
  VM.Bytecode[OPrand] -> OPrand;
  VM.Bytecode[OPret] -> OPret;
  VM.Bytecode[OPsense] -> OPsense;

  VM.Bytecode[OPsendr] -> OPsendr;
  VM.Bytecode[OPsend] -> OPsend;
  VM.Bytecode[OPuart] -> OPuart;

  VM.Bytecode[OPcall0] -> OPcall2;
  VM.Bytecode[OPcall0+1] -> OPcall2;
  VM.Bytecode[OPcall0+2] -> OPcall2;
  VM.Bytecode[OPcall0+3] -> OPcall2;
  
  VM.Bytecode[OPbpush0] -> OPbpush1;
  VM.Bytecode[OPbpush0+1] -> OPbpush1;
  VM.Bytecode[OPbhead] -> OPbhead;
  VM.Bytecode[OPbtail] -> OPbtail;
  VM.Bytecode[OPbclear] -> OPbclear;
  VM.Bytecode[OPbsize] -> OPbsize;
  VM.Bytecode[OPsetradio] -> OPsetradio;
  VM.Bytecode[OPpop] -> OPpop;
  VM.Bytecode[OPbsorta] -> OPbsorta;
  VM.Bytecode[OPbsortd] -> OPbsortd;
  VM.Bytecode[OPbfull] -> OPbfull;
  VM.Bytecode[OPcast] -> OPcast;
  VM.Bytecode[OPunlock] -> OPunlock;
  VM.Bytecode[OPunlockb] -> OPunlockb;
  VM.Bytecode[OPbget] -> OPbget;
  VM.Bytecode[OPbyank] -> OPbyank;
  VM.Bytecode[OPswap] -> OPswap;
  VM.Bytecode[OPshiftr] -> OPshiftr;
  VM.Bytecode[OPshiftl] -> OPshiftl;
  VM.Bytecode[OPmod] -> OPmod;
  VM.Bytecode[OPeq] -> OPeq;
  VM.Bytecode[OPneq] -> OPeq;
  VM.Bytecode[OPlt] -> OPlt;
  VM.Bytecode[OPgt] -> OPgt;
  VM.Bytecode[OPlte] -> OPlte;
  VM.Bytecode[OPgte] -> OPgte;
  VM.Bytecode[OPeqtype] -> OPeqtype;
  VM.Bytecode[OPgetms] -> OPgetms;
  VM.Bytecode[OPgetmb] -> OPgetmb;
  VM.Bytecode[OPsetms] -> OPsetms;
  VM.Bytecode[OPsetmb] -> OPsetmb;

  VM.Bytecode[OPgetvar] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+1] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+2] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+3] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+4] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+5] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+6] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+7] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+8] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+9] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+10] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+11] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+12] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+13] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+14] -> OPgetsetvar4;
  VM.Bytecode[OPgetvar+15] -> OPgetsetvar4;
  
  VM.Bytecode[OPsetvar] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+1] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+2] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+3] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+4] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+5] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+6] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+7] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+8] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+9] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+10] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+11] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+12] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+13] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+14] -> OPgetsetvar4;
  VM.Bytecode[OPsetvar+15] -> OPgetsetvar4;

  VM.Bytecode[OPpushc] -> OPpushc6;
  VM.Bytecode[OPpushc+1] -> OPpushc6;
  VM.Bytecode[OPpushc+2] -> OPpushc6;
  VM.Bytecode[OPpushc+3] -> OPpushc6;
  VM.Bytecode[OPpushc+4] -> OPpushc6;
  VM.Bytecode[OPpushc+5] -> OPpushc6;
  VM.Bytecode[OPpushc+6] -> OPpushc6;
  VM.Bytecode[OPpushc+7] -> OPpushc6;
  VM.Bytecode[OPpushc+8] -> OPpushc6;
  VM.Bytecode[OPpushc+9] -> OPpushc6;
  VM.Bytecode[OPpushc+10] -> OPpushc6;
  VM.Bytecode[OPpushc+11] -> OPpushc6;
  VM.Bytecode[OPpushc+12] -> OPpushc6;
  VM.Bytecode[OPpushc+13] -> OPpushc6;
  VM.Bytecode[OPpushc+14] -> OPpushc6;
  VM.Bytecode[OPpushc+15] -> OPpushc6;
  VM.Bytecode[OPpushc+16] -> OPpushc6;
  VM.Bytecode[OPpushc+17] -> OPpushc6;
  VM.Bytecode[OPpushc+18] -> OPpushc6;
  VM.Bytecode[OPpushc+19] -> OPpushc6;
  VM.Bytecode[OPpushc+20] -> OPpushc6;
  VM.Bytecode[OPpushc+21] -> OPpushc6;
  VM.Bytecode[OPpushc+22] -> OPpushc6;
  VM.Bytecode[OPpushc+23] -> OPpushc6;
  VM.Bytecode[OPpushc+24] -> OPpushc6;
  VM.Bytecode[OPpushc+25] -> OPpushc6;
  VM.Bytecode[OPpushc+26] -> OPpushc6;
  VM.Bytecode[OPpushc+27] -> OPpushc6;
  VM.Bytecode[OPpushc+28] -> OPpushc6;
  VM.Bytecode[OPpushc+29] -> OPpushc6;
  VM.Bytecode[OPpushc+30] -> OPpushc6;
  VM.Bytecode[OPpushc+31] -> OPpushc6;
  VM.Bytecode[OPpushc+32] -> OPpushc6;
  VM.Bytecode[OPpushc+33] -> OPpushc6;
  VM.Bytecode[OPpushc+34] -> OPpushc6;
  VM.Bytecode[OPpushc+35] -> OPpushc6;
  VM.Bytecode[OPpushc+36] -> OPpushc6;
  VM.Bytecode[OPpushc+37] -> OPpushc6;
  VM.Bytecode[OPpushc+38] -> OPpushc6;
  VM.Bytecode[OPpushc+39] -> OPpushc6;
  VM.Bytecode[OPpushc+40] -> OPpushc6;
  VM.Bytecode[OPpushc+41] -> OPpushc6;
  VM.Bytecode[OPpushc+42] -> OPpushc6;
  VM.Bytecode[OPpushc+43] -> OPpushc6;
  VM.Bytecode[OPpushc+44] -> OPpushc6;
  VM.Bytecode[OPpushc+45] -> OPpushc6;
  VM.Bytecode[OPpushc+46] -> OPpushc6;
  VM.Bytecode[OPpushc+47] -> OPpushc6;
  VM.Bytecode[OPpushc+48] -> OPpushc6;
  VM.Bytecode[OPpushc+49] -> OPpushc6;
  VM.Bytecode[OPpushc+50] -> OPpushc6;
  VM.Bytecode[OPpushc+51] -> OPpushc6;
  VM.Bytecode[OPpushc+52] -> OPpushc6;
  VM.Bytecode[OPpushc+53] -> OPpushc6;
  VM.Bytecode[OPpushc+54] -> OPpushc6;
  VM.Bytecode[OPpushc+55] -> OPpushc6;
  VM.Bytecode[OPpushc+56] -> OPpushc6;
  VM.Bytecode[OPpushc+57] -> OPpushc6;
  VM.Bytecode[OPpushc+58] -> OPpushc6;
  VM.Bytecode[OPpushc+59] -> OPpushc6;
  VM.Bytecode[OPpushc+60] -> OPpushc6;
  VM.Bytecode[OPpushc+61] -> OPpushc6;
  VM.Bytecode[OPpushc+62] -> OPpushc6;
  VM.Bytecode[OPpushc+63] -> OPpushc6;

  VM.Bytecode[OPjumpc] -> OPjumpc5;
  VM.Bytecode[OPjumpc+1] -> OPjumpc5;
  VM.Bytecode[OPjumpc+2] -> OPjumpc5;
  VM.Bytecode[OPjumpc+3] -> OPjumpc5;
  VM.Bytecode[OPjumpc+4] -> OPjumpc5;
  VM.Bytecode[OPjumpc+5] -> OPjumpc5;
  VM.Bytecode[OPjumpc+6] -> OPjumpc5;
  VM.Bytecode[OPjumpc+7] -> OPjumpc5;
  VM.Bytecode[OPjumpc+8] -> OPjumpc5;
  VM.Bytecode[OPjumpc+9] -> OPjumpc5;
  VM.Bytecode[OPjumpc+10] -> OPjumpc5;
  VM.Bytecode[OPjumpc+11] -> OPjumpc5;
  VM.Bytecode[OPjumpc+12] -> OPjumpc5;
  VM.Bytecode[OPjumpc+13] -> OPjumpc5;
  VM.Bytecode[OPjumpc+14] -> OPjumpc5;
  VM.Bytecode[OPjumpc+15] -> OPjumpc5;
  VM.Bytecode[OPjumpc+16] -> OPjumpc5;
  VM.Bytecode[OPjumpc+17] -> OPjumpc5;
  VM.Bytecode[OPjumpc+18] -> OPjumpc5;
  VM.Bytecode[OPjumpc+19] -> OPjumpc5;
  VM.Bytecode[OPjumpc+20] -> OPjumpc5;
  VM.Bytecode[OPjumpc+21] -> OPjumpc5;
  VM.Bytecode[OPjumpc+22] -> OPjumpc5;
  VM.Bytecode[OPjumpc+23] -> OPjumpc5;

  VM.Bytecode[OPjumps] -> OPjumps5;
  VM.Bytecode[OPjumps+1] -> OPjumps5;
  VM.Bytecode[OPjumps+2] -> OPjumps5;
  VM.Bytecode[OPjumps+3] -> OPjumps5;
  VM.Bytecode[OPjumps+4] -> OPjumps5;
  VM.Bytecode[OPjumps+5] -> OPjumps5;
  VM.Bytecode[OPjumps+6] -> OPjumps5;
  VM.Bytecode[OPjumps+7] -> OPjumps5;
  VM.Bytecode[OPjumps+8] -> OPjumps5;
  VM.Bytecode[OPjumps+9] -> OPjumps5;
  VM.Bytecode[OPjumps+10] -> OPjumps5;
  VM.Bytecode[OPjumps+11] -> OPjumps5;
  VM.Bytecode[OPjumps+12] -> OPjumps5;
  VM.Bytecode[OPjumps+13] -> OPjumps5;
  VM.Bytecode[OPjumps+14] -> OPjumps5;
  VM.Bytecode[OPjumps+15] -> OPjumps5;
  VM.Bytecode[OPjumps+16] -> OPjumps5;
  VM.Bytecode[OPjumps+17] -> OPjumps5;
  VM.Bytecode[OPjumps+18] -> OPjumps5;
  VM.Bytecode[OPjumps+19] -> OPjumps5;
  VM.Bytecode[OPjumps+20] -> OPjumps5;
  VM.Bytecode[OPjumps+21] -> OPjumps5;
  VM.Bytecode[OPjumps+22] -> OPjumps5;
  VM.Bytecode[OPjumps+23] -> OPjumps5;
}
