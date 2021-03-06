// $Id: MotePlugin.java,v 1.9 2003/12/05 07:43:37 mikedemmer Exp $

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
 * Desc:        Default Mote Plugin
 *              GUI interactions with motes.
 *
 */

/**
 * @author Nelson Lee
 */


package net.tinyos.sim;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;

import net.tinyos.sim.event.*;

public class MotePlugin extends GuiPlugin implements SimConst {
  public final Color BASIC_COLOR = Color.black;
  public final Color SELECTED_COLOR = new Color(150, 150, 255);
  public final Color BASIC_COLOR_OFF = Color.lightGray;
  public final Color SELECTED_COLOR_OFF = SELECTED_COLOR;

  private MotePanel motePanel;
  private SimComm simComm;
  private SimState state;
  private MoteLayoutPlugin moteLayoutPlugin;
  
  public void initialize(TinyViz tv, JPanel pluginPanel) {
    super.initialize(tv, pluginPanel);
    this.motePanel = tv.getMotePanel();
    this.state = driver.getSimState();
    this.simComm = driver.getSimComm();
    this.moteLayoutPlugin = driver.getMoteLayout();
  }

  public void register() {
    JButton bPower = tv.getMenuBar().addIconButton("On/off", "ui/power.gif");
    bPower.addActionListener(new bPowerListener());

    // Add layout menu
    ButtonGroup group = new ButtonGroup();
    menuListener ml = new menuListener();
    JMenu layoutMenu = new JMenu("Layout");

    JRadioButtonMenuItem layoutRandom;
    layoutRandom = new JRadioButtonMenuItem("Random");
    group.add(layoutRandom);
    layoutRandom.setFont(tv.defaultFont);
    layoutRandom.addActionListener(ml);
    layoutRandom.setSelected(true);

    JRadioButtonMenuItem layoutGrid;
    layoutGrid = new JRadioButtonMenuItem("Grid");
    group.add(layoutGrid);
    layoutGrid.setFont(tv.defaultFont);
    layoutGrid.addActionListener(ml);

    JRadioButtonMenuItem layoutGridRandom;
    layoutGridRandom = new JRadioButtonMenuItem("Grid + Random");
    group.add(layoutGridRandom);
    layoutGridRandom.setFont(tv.defaultFont);
    layoutGridRandom.addActionListener(ml);

    JMenuItem layoutFileLoad = new JMenuItem("File Load");
    layoutFileLoad.addActionListener(ml);
    layoutFileLoad.setFont(tv.defaultFont);

    JMenuItem layoutFileSave = new JMenuItem("File Save");
    layoutFileSave.addActionListener(ml);
    layoutFileSave.setFont(tv.defaultFont);

    layoutMenu.add(layoutRandom);
    layoutMenu.add(layoutGrid);
    layoutMenu.add(layoutGridRandom);
    layoutMenu.add(layoutFileLoad);
    layoutMenu.add(layoutFileSave);

    tv.getMenuBar().addMenu(layoutMenu);
  }

  public void handleEvent(SimEvent event) {
    if (event instanceof TossimEvent) {
      TossimEvent tosEvent = (TossimEvent)event;
      
      if (tosEvent instanceof LedEvent) {
	LedEvent ledEvent = (LedEvent)tosEvent;

        MoteSimObject mote = state.getMoteSimObject(ledEvent.getMoteID());
        MoteLedsAttribute ledAttrib = (MoteLedsAttribute)
           (mote.getAttribute("net.tinyos.sim.MoteLedsAttribute"));

        boolean attributeModified = false;
        if (ledEvent.redLedOn() != ledAttrib.redLedOn()) {
          if (ledEvent.redLedOn())
            ledAttrib.setRedOn();
          else
            ledAttrib.setRedOff();
          attributeModified = true;
        }
        
        if (ledEvent.greenLedOn() != ledAttrib.greenLedOn()) {
          if (ledEvent.greenLedOn())
            ledAttrib.setGreenOn();
          else
            ledAttrib.setGreenOff();
          attributeModified = true;
        }
        
        if (ledEvent.yellowLedOn() != ledAttrib.yellowLedOn()) {
          if (ledEvent.yellowLedOn())
            ledAttrib.setYellowOn();
          else
            ledAttrib.setYellowOff();
          attributeModified = true;
        }
        
        if (attributeModified) {
          eventBus.addEvent(new AttributeEvent(ATTRIBUTE_CHANGED,
                                               mote, ledAttrib));
          motePanel.refresh();
        } else {
          System.out.println("Warning: irrelevant LED event");
        }
      }
    }
  }

  public void draw(Graphics graphics) {
    //System.err.println("MotePlugin draw called.");
    //System.out.println("drawing on graphics " + graphics);
    Iterator it = state.getMoteSimObjects().iterator();
    graphics.setFont(tv.smallFont);

    while (it.hasNext()) {
      MoteSimObject moteSimObject = (MoteSimObject)it.next();

      if (!moteSimObject.isVisible()) {
	continue;
      }

      if (moteSimObject.getPower()) {
	if (moteSimObject.isSelected()) {
  	  graphics.setColor(SELECTED_COLOR);
   	}
    	else {
  	  graphics.setColor(BASIC_COLOR);
   	}
      } else {
	if (moteSimObject.isSelected()) {
  	  graphics.setColor(SELECTED_COLOR_OFF);
   	}
    	else {
  	  graphics.setColor(BASIC_COLOR_OFF);
   	}
      }

      MoteCoordinateAttribute coordinate = moteSimObject.getCoordinate();
      int x = (int)cT.simXToGUIX(coordinate.getX());
      int y = (int)cT.simYToGUIY(coordinate.getY());

      // Drawing Mote itself based on coordinate
      int size = (int)cT.simXToGUIX(moteSimObject.getObjectSize());
      int xl = x-(size/2);
      int yl = y-(size/2);
      graphics.fillOval(xl, yl, size, size);

      // Writing out MoteID
      int id = moteSimObject.getID(); 
      graphics.setColor(Color.black);
      graphics.drawString(Integer.toString(id), xl, yl-1);

      // Drawing Leds
      MoteLedsAttribute leds = (MoteLedsAttribute)
            moteSimObject.getAttribute("net.tinyos.sim.MoteLedsAttribute");
      if (leds.redLedOn()) {
	graphics.setColor(Color.red);
	graphics.fillRect(xl, y-(size/6), size/3, size/3);
      }
      if (leds.greenLedOn()) {
	graphics.setColor(Color.green);
	graphics.fillRect(xl+(size/3), y-(size/6), size/3, size/3);
      }
      if (leds.yellowLedOn()) {
	graphics.setColor(Color.yellow);
	graphics.fillRect(xl + (2*size/3), y-(size/6), size/3, size/3);
      }

      // Draw label if it exists
      MoteLabelAttribute label = (MoteLabelAttribute)
            moteSimObject.getAttribute("net.tinyos.sim.MoteLabelAttribute");
      if (label != null) {
        graphics.setColor(Color.black);
        graphics.drawString(label.getLabel(), x + label.getX(), y + label.getY());
      }
    }
  }

  public String toString() {
    return "MotePlugin";	
  }
  
  class bPowerListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      Iterator it = state.getSelectedMoteSimObjects().iterator();
      try {
	while (it.hasNext()) {
	  MoteSimObject mote = (MoteSimObject)it.next();
	  // Toggle power status for each mote
	  if (mote.getPower()) {
	    simComm.sendCommand(new TurnOffMoteCommand((short)mote.getID(), 0L));
	    mote.setPower(false);
  	  } else {
  	    simComm.sendCommand(new TurnOnMoteCommand((short)mote.getID(), 0L));
  	    mote.setPower(true);
  	  }
  	  motePanel.refresh();
   	}
      } catch (IOException ioe) {
	System.err.println("Cannot send command: "+ioe);
      }
    }
  }
  
  class menuListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
      if (e.getActionCommand().equals("Grid")) {
        moteLayoutPlugin.setLayout(moteLayoutPlugin.LAYOUT_GRID);
      } else if (e.getActionCommand().equals("Random")) {
        moteLayoutPlugin.setLayout(moteLayoutPlugin.LAYOUT_RANDOM);
      } else if (e.getActionCommand().equals("Grid + Random")) {
        moteLayoutPlugin.setLayout(moteLayoutPlugin.LAYOUT_GRID_RANDOM);
      } else if (e.getActionCommand().equals("File Load")) {
	JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
	fc.addChoosableFileFilter(new MotePositionsFileFilter());
	int returnVal = fc.showOpenDialog(motePanel);
	if (returnVal == JFileChooser.APPROVE_OPTION) {
	    File file = fc.getSelectedFile();
	    try {	    
              moteLayoutPlugin.loadLocationFile(file);
	    } catch (IOException exception) {
      	      tv.setStatus("Error reading location file "+file.getName());
	    }
	}
      } else if (e.getActionCommand().equals("File Save")) {
        JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
        fc.addChoosableFileFilter(new MotePositionsFileFilter());
        int returnVal = fc.showSaveDialog(motePanel);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
          File file = fc.getSelectedFile();
          try {
            moteLayoutPlugin.saveLocationFile(file);
          } catch (IOException ioException) {
            tv.setStatus("Error writing spatial file "+file.getName());
            System.out.println(ioException);
            ioException.printStackTrace();
          }
        }
      }
      motePanel.refresh();
    }
    
    protected class MotePositionsFileFilter extends javax.swing.filechooser.FileFilter {
      public MotePositionsFileFilter() {}

      public boolean accept(File f) {
	if (f.isDirectory()) {
	  return true;
	}
	String name = f.getName();
	int index = name.lastIndexOf('.');

	if (index < 0) {return false;}

	String extension = name.substring(index);
	if (extension != null) {
	  if (extension.equals(".mps")) {
	    return true;
	  } else {
	    return false;
	  }
	}
	return false;
      }
      public String getDescription() {
	return "*.mps";
      }
    }
  }   
}

