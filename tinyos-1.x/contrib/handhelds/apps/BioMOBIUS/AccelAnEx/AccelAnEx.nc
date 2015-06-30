/*
 * Copyright (c) 2007, Intel Corporation
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer. 
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution. 
 *
 * Neither the name of the Intel Corporation nor the names of its contributors
 * may be used to endorse or promote products derived from this software 
 * without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * Author: Adrian Burns
 *         November, 2007
 * AccelECG hacked into AccelAnEx by Steve Ayer, Feb. 2010
 */

 /* This app uses Bluetooth to stream 3 Accelerometer channels and 2 ECG channels 
    of data to a BioMOBIUS PC application. 
    Tested on SHIMMER Base Board Rev 1.3, SHIMMER ECG board Rev 1.1. */

configuration AccelAnEx {
}
implementation {
  components 
    Main, 
    DMA_M, 
    AccelC,
    AccelAnExM,
    RovingNetworksC,
    TimerC, 
    LedsC;

  Main.StdControl  -> AccelAnExM;
  Main.StdControl  -> TimerC;

  AccelAnExM.Leds          -> LedsC;
  AccelAnExM.SampleTimer   -> TimerC.Timer[unique("Timer")];
  AccelAnExM.SetupTimer    -> TimerC.Timer[unique("Timer")];
  AccelAnExM.ActivityTimer -> TimerC.Timer[unique("Timer")];
  AccelAnExM.LocalTime     -> TimerC;
  
  AccelAnExM.BTStdControl -> RovingNetworksC;
  AccelAnExM.Bluetooth    -> RovingNetworksC;

  AccelAnExM.DMA0         -> DMA_M.DMA[0];

  AccelAnExM.AccelStdControl   -> AccelC;
  AccelAnExM.Accel             -> AccelC;

}

