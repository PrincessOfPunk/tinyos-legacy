/** * Copyright (c) 2003 - The Ohio State University. * All rights reserved. * * Permission to use, copy, modify, and distribute this software and its * documentation for any purpose, without fee, and without written agreement is * hereby granted, provided that the above copyright notice, the following * two paragraphs, and the author attribution appear in all copies of this * software. * * IN NO EVENT SHALL THE OHIO STATE UNIVERSITY BE LIABLE TO ANY PARTY FOR * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE OHIO STATE * UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. * * THE OHIO STATE UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES, * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS * ON AN "AS IS" BASIS, AND THE OHIO STATE UNIVERSITY HAS NO OBLIGATION TO * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS. */

/*
*
*   FILE NAME
*
*        Scale.java
*
*   DESCRIPTION
*
*   This class implements method used to handle Scaling and Shifting of
*   mote object.  Two offset factors XO, YO and single scale factor S.
*   This information will be utilized for Scaling/Shifting functionality 
*   of the Visualization.  Both mote location information and these new 
*   measures are read from the existing file "MoteLocation.dat" as before, 
*   except first line in the file will contain the new measures. 
*   The mote coordinates X and Y in the file will have to be translated 
*   using these factors to get the actual coordinates XA and YA as follows:
*
*      XA = S*X + XO
*      YA = S*Y + YO
*
*  Author : Mark E. Miyashita  -  Kent State University
*
*  Modification History:
*  1. 6/5/03 Mark E. Miyashita
*	- Craeted this file
*
*/
public class Scale {
   private int offsetX;
   private int offsetY;
   private int ScaleFactor;
  
   /* Constructor */
 
   public Scale(int x, int y, int factor) {
		this.setoffsetX( x );
		this.setoffsetY( y );
		this.setScaleFactor( factor );
   }

   public Scale() {
		this.setoffsetX( 0 );
		this.setoffsetY( 0 );
		this.setScaleFactor( 0 );
   }

   public void setScale(int x, int y, int factor) {
		this.setoffsetX( x );
		this.setoffsetY( y );
		this.setScaleFactor( factor );
   }

   public int getScaleFactor() { return this.ScaleFactor; }
   public void setScaleFactor( int factor ) { this.ScaleFactor = factor; }

   public int getoffsetX() { return this.offsetX; }
   public void setoffsetX( int x ) { this.offsetX = x; }

   public int getoffsetY() { return this.offsetY; }
   public void setoffsetY( int y ) { this.offsetY = y; }

   public String toString() {
	return( "Scale " + getScaleFactor() + " (" + getoffsetX() + ", " + getoffsetY() + ")" );
   }
}

