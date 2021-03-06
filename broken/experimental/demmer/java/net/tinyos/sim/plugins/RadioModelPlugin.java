// $Id: RadioModelPlugin.java,v 1.11 2004/01/09 19:50:43 mikedemmer Exp $

/*									tab:2
 *
 *
 * "Copyright (c) 2000 and The Regents of the University 
 * of California.  All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and
 * its documentation for any purpose, without fee, and without written
 * agreement is hereby granted, provided that the above copyright
 * notice and the following two paragraphs appear in all copies of
 * this software.
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
 * Authors:	Nelson Lee
 * Date:        December 11 2002
 * Desc:        Default Radio Plugin
 *              Manipulate the radio model.
 *
 */

/**
 * @author Nelson Lee
 * @author Michael Demmer
 */


package net.tinyos.sim.plugins;

import java.lang.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.text.DecimalFormat;

import net.tinyos.message.*;
import net.tinyos.sim.*;
import net.tinyos.sim.event.*;

public class RadioModelPlugin extends Plugin implements SimConst {
  private static SimDebug debug = SimDebug.get("radio");

  private Hashtable models = new Hashtable();
  private PropagationModel curModel;
  private Hashtable connectivityGraph;
  private double scalingFactor = 1;

  private boolean autoPublish = false;

  public PropagationModel getCurModel() {
    return curModel;
  }

  public void setCurModel(PropagationModel model) {
    this.curModel = model;
  }

  public void setCurModel(String modelname) {
    PropagationModel m = (PropagationModel)models.get(modelname);
    if (m != null) {
      this.curModel = m;
    }
  }

  public Enumeration getModels() {
    return models.elements();
  }

  public PropagationModel getModel(String name) {
    return (PropagationModel)models.get(name);
  }

  public void setScalingFactor(double scalingFactor) {
    this.scalingFactor = scalingFactor;
  }

  public void setAutoPublish(boolean autoPublish) {
    this.autoPublish = autoPublish;
  }

  // XXX/demmer i took out a call to motePanel.refresh() from each
  // event handler. make sure this is ok

  public void handleEvent(SimEvent event) {
    if (event instanceof SimObjectEvent) {
      SimObjectEvent simObjectEvent = (SimObjectEvent)event;
      SimObject simObject = simObjectEvent.getSimObject();
      switch (simObjectEvent.getType()) {
        case SimObjectEvent.OBJECT_ADDED:
        case SimObjectEvent.OBJECT_REMOVED:
          // always fully update the model
          debug.err.println("RADIOMODEL: sim object add/remove, updating model");
	  updateModel();
	  break;
      }
    }
    
    else if (event instanceof AttributeEvent) {
      AttributeEvent attributeEvent = (AttributeEvent)event;
      switch (attributeEvent.getType()) {
      case ATTRIBUTE_CHANGED:
	if (attributeEvent.getAttribute() instanceof MoteCoordinateAttribute) {
          MoteSimObject mote = (MoteSimObject)attributeEvent.getOwner();
          debug.err.println("RADIOMODEL: "+mote+" moved, updating links");
          updateLossRates(mote);
        }
        break;
      }
    }

    // XXX/demmer this isn't necessary since we already respond to the
    // updated location attributes
//     else if (event instanceof SimObjectDraggedEvent) {
//       updateModel(true);
//     }
    
    else if (event instanceof OptionSetEvent) {
      OptionSetEvent ose = (OptionSetEvent)event;

      if (ose.name.equals("radiomodel")) {
	PropagationModel pm = (PropagationModel)models.get(ose.value);
	if (pm != null) {
	  debug.err.println("RADIOMODEL: Setting model to "+pm);
	  curModel = pm;
	  updateModel();
	}
      }

      // also not necessary since it generates new simobjectevents
//     } else if (event instanceof TossimInitEvent) {
//       driver.pause();
//       updateModel();
//       publishModel();
//       driver.resume();
    }
  }

  private String graphKey(int senderID, int receiverID) {
    return "" + senderID + "," + receiverID;
  }
    
  public double getLossRate(int senderID, int receiverID) {
    Double d = (Double)connectivityGraph.get(graphKey(senderID, receiverID));
    if (d == null) {
      throw new ArrayIndexOutOfBoundsException(
        "no connectivity entry for "+senderID+" -> "+receiverID);
    }
    return d.doubleValue();
  }

  private String graphKey(MoteSimObject sender, MoteSimObject receiver) {
    return graphKey(sender.getID(), receiver.getID());
  }

  public double getLossRate(MoteSimObject sender, MoteSimObject receiver) {
    return getLossRate(sender.getID(), receiver.getID());
  }

  public void setLossRate(MoteSimObject sender, MoteSimObject receiver,
                          double prob) {
    connectivityGraph.put(graphKey(sender, receiver), new Double(prob));
  }
    
  public void setLossRate(int senderID, int receiverID, double prob) {
    connectivityGraph.put(graphKey(senderID, receiverID), new Double(prob));
  }

  // Recalculate the loss rate for the pair of motes based on their
  // distance and the current model
  public void updateLossRate(MoteSimObject moteSender, MoteSimObject moteReceiver) {
    double distance = moteSender.getDistance(moteReceiver);
    double prob = curModel.getPacketLossRate(distance, this.scalingFactor);

    debug.out.println("RADIOMODEL: " + moteSender + "->" + moteReceiver +
                      " dist " + distance +
                      " scale " + scalingFactor + 
                      " prob " + prob);
	
    long scaledBitLossRate = (long)(curModel.getBitLossRate(prob)*10000);
    
    debug.out.println("RADIOMODEL: sampleLossRate " +
                      "[moteSender " + moteSender + "] 1" +
                      "[moteReceiver " + moteReceiver + "] " +
                      "[packetLossRate " + prob + "] " +
                      "[scaledBitLossRate " + scaledBitLossRate + "]");
	
    connectivityGraph.put(graphKey(moteSender, moteReceiver), new Double(prob));

    if (autoPublish) {
      publishLossRate(moteSender, moteReceiver, prob);
    }
  }

  // Do a O(n) pass through the motes updating the given moteSender's
  // connectivity to and from each other mote
  public void updateLossRates(MoteSimObject moteSender) {
    Iterator it = state.getMoteSimObjects().iterator();
    while (it.hasNext()) {
      MoteSimObject moteReceiver = (MoteSimObject)it.next();
      if (moteReceiver.getID() == moteSender.getID()) continue;

      updateLossRate(moteSender, moteReceiver);
      updateLossRate(moteReceiver, moteSender);
    }
  }

  // Do an O(n^2) pass through updating all connectivity
  public void updateModel() {
    Iterator it1 = state.getMoteSimObjects().iterator();
    while (it1.hasNext()) {
      MoteSimObject moteSender = (MoteSimObject)it1.next();
      Iterator it2 = state.getMoteSimObjects().iterator();
      while (it2.hasNext()) {
        MoteSimObject moteReceiver = (MoteSimObject)it2.next();
        if (moteReceiver.getID() == moteSender.getID()) continue;

        updateLossRate(moteSender, moteReceiver);
      }
    }
  }

  // Send the loss rate for the pair of motes to the simulator
  public void publishLossRate(MoteSimObject moteSender, MoteSimObject moteReceiver,
                              double prob) {
    try {
      int senderID = moteSender.getID();
      int receiverID = moteReceiver.getID();
      
      debug.err.println("RADIOMODEL: publishing rate ["+
                        senderID+"->"+receiverID+"]: "+prob);
      
      long scaledBitLossRate = (long)(curModel.getBitLossRate(prob)*10000);
      SetLinkProbCommand cmd =
        new SetLinkProbCommand((short)senderID, 0L, (short)receiverID, scaledBitLossRate);
      simComm.sendCommand(cmd);
    } catch (java.io.IOException ioe) {
      System.err.println("RADIOMODEL: Cannot send command: "+ioe);
    }
  }

  // Send the loss rate for all pairs of motes to the simulator
  public void publishModel() {
    debug.err.println("RADIOMODEL: Publishing model, current is "+curModel);
    Iterator it1 = state.getMoteSimObjects().iterator();
    while (it1.hasNext()) {
      MoteSimObject moteSender = (MoteSimObject)it1.next();
      MoteCoordinateAttribute moteSenderCoord = moteSender.getCoordinate();
      Iterator it2 = state.getMoteSimObjects().iterator();
      while (it2.hasNext()) {
        MoteSimObject moteReceiver = (MoteSimObject)it2.next();
        if (moteReceiver.getID() == moteSender.getID()) continue;

        String key = graphKey(moteSender, moteReceiver);
        double prob = ((Double)connectivityGraph.get(key)).doubleValue();
        publishLossRate(moteSender, moteReceiver, prob);
      }
    }
  }

  public void register() {
    debug.out.println("RADIOMODEL: registering radio model plugin");
    connectivityGraph = new Hashtable();

    EmpiricalModel empiricalModel = new EmpiricalModel();
    models.put("empirical", empiricalModel);
    curModel = empiricalModel;

    // User can use scaling factor to adjust
    DiscModel dm;
    dm = new DiscModel(10.0);
    models.put("disc10", dm);
    dm = new DiscModel(100.0);
    models.put("disc100", dm);
    dm = new DiscModel(1000.0);
    models.put("disc1000", dm);

    updateModel();
  }

  public void deregister() {}
  
  public String toString() {
    return "Radio Model (non-gui)";
  }
  
}
