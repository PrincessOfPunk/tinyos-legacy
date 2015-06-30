/* "Copyright (c) 2001 and The Regents of the University  
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
* Authors:   Kamin Whitehouse <kamin@cs.berkeley.edu>
* History:   created 7/22/2001 
*/

package Surge.PacketAnalyzer.Location.StageOne;

import Surge.event.*;
import Surge.*;

public class StageOneAnalyzer implements NodeEventListener, EdgeEventListener
{

              //----------------------------------------------------------------------
              //CONSTRUCTOR
	public StageOneAnalyzer()
	{
				//register myself to recieve Node/Edge created/deleted Events
		MainClass.objectMaintainer.AddNodeEventListener(this);
		MainClass.objectMaintainer.AddEdgeEventListener(this);
	}
              //CONSTRUCTOR
              //----------------------------------------------------------------------
	

              //----------------------------------------------------------------------
	             //ESTIMATE DISTANCE
	public double EstimateDistance(Integer pSourceNumber, Integer pDestinationNumber, double pSignalStrength)
	{
		return -1;
	}
	             //ESTIMATE DISTANCE
              //----------------------------------------------------------------------
	

              //----------------------------------------------------------------------
              //Node/Edge CREATED/DELETED
	public synchronized void NodeCreated(NodeEvent e)
    {
    }

    public synchronized void NodeDeleted(NodeEvent e)
    {
    }
	
	public synchronized void EdgeCreated(EdgeEvent e)
    {
    }
 
    public synchronized void EdgeDeleted(EdgeEvent e)
    {
    }
              //Node/Edge CREATED/DELETED
              //----------------------------------------------------------------------




              //**********************************************************************
              //**********************************************************************
              //NODE INFO CLASS
	protected class NodeInfo
	{
		
	}
              //NODE INFO CLASS
              //**********************************************************************
              //**********************************************************************


              //**********************************************************************
              //**********************************************************************
              //EDGE INFO CLASS
	protected class EdgeInfo
	{
		
	}
              //EDGE INFO CLASS
              //**********************************************************************
              //**********************************************************************
}