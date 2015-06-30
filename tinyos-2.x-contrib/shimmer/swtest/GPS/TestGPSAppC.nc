/*
 * Copyright (c) 2010, Shimmer Research, Ltd.
 * All rights reserved
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:

 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of Shimmer Research, Ltd. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.

 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * @author Steve Ayer
 * @date   November 2010
 *
 * move pressure and gps data over bt
 */

configuration TestGPSAppC {
}

implementation {
  components MainC, TestGPSC;
  TestGPSC -> MainC.Boot; 

  components LedsC;
  TestGPSC.Leds -> LedsC;

  components new TimerMilliC();
  TestGPSC.Timer -> TimerMilliC;
  
  components RovingNetworksC;
  TestGPSC.BluetoothInit -> RovingNetworksC.Init;
  TestGPSC.BTStdControl -> RovingNetworksC.StdControl;
  TestGPSC.Bluetooth    -> RovingNetworksC;

  components GpsC;
  TestGPSC.GpsInit        -> GpsC.Init;
  TestGPSC.Gps            -> GpsC.Gps;

  components PressureSensorC;
  TestGPSC.PSInit         -> PressureSensorC.Init;
  TestGPSC.PSStdControl   -> PressureSensorC.StdControl;
  TestGPSC.PressureSensor -> PressureSensorC.PressureSensor;

}

