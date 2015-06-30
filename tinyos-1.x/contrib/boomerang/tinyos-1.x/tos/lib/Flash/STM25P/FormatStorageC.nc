// $Id: FormatStorageC.nc,v 1.1.1.1 2007/11/05 19:09:12 jpolastre Exp $

/*									tab:2
 * "Copyright (c) 2000-2005 The Regents of the University  of California.  
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

/*
 * @author: Jonathan Hui <jwhui@cs.berkeley.edu>
 */

includes crc;

configuration FormatStorageC {
  provides {
    interface FormatStorage;
  }
}

implementation {

  components CrcC, FormatStorageM, HALSTM25PC, Main;

  FormatStorage = FormatStorageM;

  Main.StdControl -> FormatStorageM;
  Main.StdControl -> HALSTM25PC;

  FormatStorageM.Crc -> CrcC;
  FormatStorageM.HALSTM25P -> HALSTM25PC.HALSTM25P[unique("HALSTM25P")];

}
