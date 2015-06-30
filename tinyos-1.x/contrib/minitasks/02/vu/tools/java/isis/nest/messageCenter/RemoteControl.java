/*
 * Copyright (c) 2003, Vanderbilt University
 * All rights reserved.
 *
 * Permission to use, copy, modify, and distribute this software and its
 * documentation for any purpose, without fee, and without written agreement is
 * hereby granted, provided that the above copyright notice, the following
 * two paragraphs and the author appear in all copies of this software.
 * 
 * IN NO EVENT SHALL THE VANDERBILT UNIVERSITY BE LIABLE TO ANY PARTY FOR
 * DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
 * OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE VANDERBILT
 * UNIVERSITY HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * THE VANDERBILT UNIVERSITY SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
 * AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
 * ON AN "AS IS" BASIS, AND THE VANDERBILT UNIVERSITY HAS NO OBLIGATION TO
 * PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 */

package isis.nest.messageCenter;

import java.util.prefs.*;
import java.util.*;

/**
 *
 * @author  nadand
 */
public class RemoteControl extends MessageCenterInternalFrame {
    
    // the AM type
    public static byte AM_TYPE = (byte)0x5E;
    
    // command constants
    public static byte STOP = (byte)0;
    public static byte START = (byte)1;
    public static byte RESTART = (byte)2;
    
    // data type constants
    public static byte INT = (byte)0;
    public static byte DATA = (byte)1;
    public static byte COMMAND = (byte)2;
    
    // target constants
    public static int BROADCAST = 0xFFFF;
    
	// other constants
	public static int COMMAND_RESEND = 3;
	
    private static byte sequenceNum = 1;
	private static byte commandState = START;
    Preferences prefs = null;
    
    /** Creates new form RemoteController */
    public RemoteControl() {
        super("RemoteController");
        
        initComponents();
        
        buttonGroup1.add(startRadioButton);
        buttonGroup1.add(stopRadioButton);
        buttonGroup1.add(restartRadioButton);
        buttonGroup1.setSelected(startRadioButton.getModel(),true);
        
        prefs = Preferences.userNodeForPackage(this.getClass());
        prefs = prefs.node(prefs.absolutePath()+"/RemoteController");
        loadComboBox();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
        private void initComponents() {//GEN-BEGIN:initComponents
                java.awt.GridBagConstraints gridBagConstraints;

                buttonGroup1 = new javax.swing.ButtonGroup();
                savePanel = new javax.swing.JPanel();
                configNameText = new javax.swing.JComboBox();
                saveConfigButton = new javax.swing.JButton();
                delConfigButton = new javax.swing.JButton();
                controlPanel = new javax.swing.JPanel();
                seqnLabel = new javax.swing.JLabel();
                seqnTextField = new javax.swing.JTextField();
                targetLabel = new javax.swing.JLabel();
                targetTextField = new javax.swing.JTextField();
                appIdLabel = new javax.swing.JLabel();
                appIdTextField = new javax.swing.JTextField();
                sendButton = new javax.swing.JButton();
                jTabbedPane1 = new javax.swing.JTabbedPane();
                intPanel = new javax.swing.JPanel();
                intCmdLabel = new javax.swing.JLabel();
                intCmdTextField = new javax.swing.JTextField();
                runPanel = new javax.swing.JPanel();
                startRadioButton = new javax.swing.JRadioButton();
                stopRadioButton = new javax.swing.JRadioButton();
                restartRadioButton = new javax.swing.JRadioButton();
                commandPanel = new javax.swing.JPanel();
                dataParamLabel = new javax.swing.JLabel();
                dataParamTextField = new javax.swing.JTextField();
                logPanel = new javax.swing.JPanel();
                jScrollPane1 = new javax.swing.JScrollPane();
                logTextArea = new javax.swing.JTextArea();
                addToLogTextField = new javax.swing.JTextField();
                addtoLogButton = new javax.swing.JButton();
                clearLogButton = new javax.swing.JButton();
                usageTextField = new javax.swing.JTextArea();

                getContentPane().setLayout(new java.awt.GridBagLayout());

                savePanel.setLayout(new java.awt.GridBagLayout());

                savePanel.setBorder(new javax.swing.border.TitledBorder("Configuration"));
                configNameText.setEditable(true);
                configNameText.setMaximumRowCount(100);
                configNameText.setToolTipText("the name of the configuration");
                configNameText.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                configNameTextActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
                savePanel.add(configNameText, gridBagConstraints);

                saveConfigButton.setText("Save");
                saveConfigButton.setToolTipText("save the current configuration in the preferences");
                saveConfigButton.setMaximumSize(new java.awt.Dimension(80, 26));
                saveConfigButton.setMinimumSize(new java.awt.Dimension(80, 26));
                saveConfigButton.setPreferredSize(new java.awt.Dimension(80, 26));
                saveConfigButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                saveConfigButtonActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
                savePanel.add(saveConfigButton, gridBagConstraints);

                delConfigButton.setText("Delete");
                delConfigButton.setToolTipText("delete the current configuration from the preferences");
                delConfigButton.setMaximumSize(new java.awt.Dimension(80, 26));
                delConfigButton.setMinimumSize(new java.awt.Dimension(80, 26));
                delConfigButton.setPreferredSize(new java.awt.Dimension(80, 26));
                delConfigButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                delConfigButtonActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
                savePanel.add(delConfigButton, gridBagConstraints);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                getContentPane().add(savePanel, gridBagConstraints);

                controlPanel.setLayout(new java.awt.GridBagLayout());

                controlPanel.setBorder(new javax.swing.border.TitledBorder("Command"));
                seqnLabel.setText("sequence");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.insets = new java.awt.Insets(0, 7, 0, 3);
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                gridBagConstraints.weightx = 1.0;
                controlPanel.add(seqnLabel, gridBagConstraints);

                seqnTextField.setBackground(new java.awt.Color(204, 204, 204));
                seqnTextField.setToolTipText("the sequence number of the command");
                seqnTextField.setMinimumSize(new java.awt.Dimension(40, 20));
                seqnTextField.setPreferredSize(new java.awt.Dimension(40, 20));
                seqnTextField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                seqnTextFieldActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                controlPanel.add(seqnTextField, gridBagConstraints);

                targetLabel.setText("target");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 3);
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                gridBagConstraints.weightx = 1.0;
                controlPanel.add(targetLabel, gridBagConstraints);

                targetTextField.setToolTipText("node id, or 0xFFFF for all motes");
                targetTextField.setMinimumSize(new java.awt.Dimension(60, 20));
                targetTextField.setPreferredSize(new java.awt.Dimension(60, 20));
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                controlPanel.add(targetTextField, gridBagConstraints);

                appIdLabel.setText("appId");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.insets = new java.awt.Insets(0, 6, 0, 3);
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                gridBagConstraints.weightx = 1.0;
                controlPanel.add(appIdLabel, gridBagConstraints);

                appIdTextField.setToolTipText("your remote control application id");
                appIdTextField.setMinimumSize(new java.awt.Dimension(40, 20));
                appIdTextField.setPreferredSize(new java.awt.Dimension(40, 20));
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 3);
                controlPanel.add(appIdTextField, gridBagConstraints);

                sendButton.setText("Send");
                sendButton.setToolTipText("this sends the command");
                sendButton.setMaximumSize(new java.awt.Dimension(80, 26));
                sendButton.setMinimumSize(new java.awt.Dimension(80, 26));
                sendButton.setPreferredSize(new java.awt.Dimension(80, 26));
                sendButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                sendButtonActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridwidth = java.awt.GridBagConstraints.RELATIVE;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                gridBagConstraints.weightx = 1.0;
                controlPanel.add(sendButton, gridBagConstraints);

                jTabbedPane1.setToolTipText("");
                intPanel.setLayout(new java.awt.GridBagLayout());

                intCmdLabel.setText("Integer Parameter");
                intCmdLabel.setToolTipText("(uint16)");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.ipadx = 10;
                gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 0);
                intPanel.add(intCmdLabel, gridBagConstraints);

                intCmdTextField.setToolTipText("application dependent integer parameter");
                intCmdTextField.setMinimumSize(new java.awt.Dimension(40, 20));
                intCmdTextField.setPreferredSize(new java.awt.Dimension(40, 20));
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.ipadx = 10;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(0, 0, 0, 5);
                intPanel.add(intCmdTextField, gridBagConstraints);

                jTabbedPane1.addTab("Int", intPanel);

                runPanel.setLayout(new java.awt.GridBagLayout());

                startRadioButton.setSelected(true);
                startRadioButton.setText("Start");
                startRadioButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                startRadioButtonActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.weightx = 1.0;
                runPanel.add(startRadioButton, gridBagConstraints);

                stopRadioButton.setText("Stop");
                stopRadioButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                stopRadioButtonActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.weightx = 1.0;
                runPanel.add(stopRadioButton, gridBagConstraints);

                restartRadioButton.setText("Restart");
                restartRadioButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                restartRadioButtonActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gridBagConstraints.weightx = 1.0;
                runPanel.add(restartRadioButton, gridBagConstraints);

                jTabbedPane1.addTab("Start/Stop", runPanel);

                commandPanel.setLayout(new java.awt.GridBagLayout());

                dataParamLabel.setText("Parameter Data");
                dataParamLabel.setToolTipText("separators:' '  ");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                commandPanel.add(dataParamLabel, gridBagConstraints);

                dataParamTextField.setToolTipText("Put either bytes or words here separated by whitespace. For words put a \"w\" at the end.");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(0, 5, 0, 5);
                commandPanel.add(dataParamTextField, gridBagConstraints);

                jTabbedPane1.addTab("Parameter", commandPanel);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.insets = new java.awt.Insets(10, 0, 0, 0);
                gridBagConstraints.weightx = 1.0;
                controlPanel.add(jTabbedPane1, gridBagConstraints);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                getContentPane().add(controlPanel, gridBagConstraints);

                logPanel.setLayout(new java.awt.GridBagLayout());

                logPanel.setBorder(new javax.swing.border.TitledBorder("Log"));
                jScrollPane1.setMaximumSize(new java.awt.Dimension(0, 103));
                jScrollPane1.setMinimumSize(new java.awt.Dimension(0, 103));
                jScrollPane1.setPreferredSize(new java.awt.Dimension(0, 103));
                jScrollPane1.setAutoscrolls(true);
                logTextArea.setBackground(new java.awt.Color(204, 204, 204));
                logTextArea.setMaximumSize(new java.awt.Dimension(0, 16));
                jScrollPane1.setViewportView(logTextArea);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridwidth = java.awt.GridBagConstraints.REMAINDER;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                logPanel.add(jScrollPane1, gridBagConstraints);

                addToLogTextField.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                addToLogTextFieldActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.weightx = 1.0;
                logPanel.add(addToLogTextField, gridBagConstraints);

                addtoLogButton.setText("add to log");
                addtoLogButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                addtoLogButtonActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.insets = new java.awt.Insets(4, 5, 2, 5);
                logPanel.add(addtoLogButton, gridBagConstraints);

                clearLogButton.setText("clear");
                clearLogButton.setToolTipText("clears the log");
                clearLogButton.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                clearLogButtonActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.insets = new java.awt.Insets(4, 0, 2, 0);
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                logPanel.add(clearLogButton, gridBagConstraints);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 2.0;
                getContentPane().add(logPanel, gridBagConstraints);

                usageTextField.setBackground(new java.awt.Color(204, 204, 204));
                usageTextField.setRows(1);
                usageTextField.setToolTipText("the documentation of your command");
                usageTextField.setBorder(new javax.swing.border.TitledBorder("Usage"));
                usageTextField.setMargin(new java.awt.Insets(0, 0, 5, 0));
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                getContentPane().add(usageTextField, gridBagConstraints);

                pack();
        }//GEN-END:initComponents

	private void seqnTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seqnTextFieldActionPerformed
		sequenceNum = (byte)Integer.parseInt(seqnTextField.getText());
	}//GEN-LAST:event_seqnTextFieldActionPerformed

	private void restartRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartRadioButtonActionPerformed
		if (commandState != RESTART){
			commandState = RESTART;
			startRadioButton.setSelected(false);
			stopRadioButton.setSelected(false);
		}
	}//GEN-LAST:event_restartRadioButtonActionPerformed

	private void stopRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopRadioButtonActionPerformed
		if (commandState != STOP){
			commandState = STOP;
			startRadioButton.setSelected(false);
			restartRadioButton.setSelected(false);
		}
	}//GEN-LAST:event_stopRadioButtonActionPerformed

	private void startRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startRadioButtonActionPerformed
		if (commandState != START){
			commandState = START;
			stopRadioButton.setSelected(false);
			restartRadioButton.setSelected(false);
		}
	}//GEN-LAST:event_startRadioButtonActionPerformed
    
    private void clearLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearLogButtonActionPerformed
        logTextArea.setText("");
    }//GEN-LAST:event_clearLogButtonActionPerformed
    
    private void sendButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sendButtonActionPerformed
		int selected = jTabbedPane1.getSelectedIndex();
		String commandString = "";
		
		switch (commandState){
			case 0: commandString = "STOP";
				break;
			case 1: commandString = "START";
				break;
			case 2: commandString = "RESTART";
				break;
		}
		
		byte appId;
		try{
			appId = (byte)parseInt(appIdTextField.getText());
			appId &= 0xFF;
		} catch (Exception e){
			appId = 0;
		}
		
		int target;
		try{
			target = parseInt(targetTextField.getText());
		} catch (Exception e){
			target = BROADCAST;
		}

		seqnTextField.setText(""+(sequenceNum & 0xFF));
		switch(selected){
			case 0: 
				int integer;
				try{
					integer = parseInt(intCmdTextField.getText());
				} catch (Exception e){
					integer = 0;
				}
				logTextArea.append((sequenceNum & 0xFF)+". sending integer (target: "+target+", appId: "+(appId & 0xFF)+", integer: "+integer+")\n");
				sendInteger(target, appId, integer);
				break;
			case 1: 
				logTextArea.append((sequenceNum & 0xFF)+". sending command (target: "+target+", appId: "+(appId & 0xFF)+", command: "+commandString+") "+COMMAND_RESEND+" times\n");
				sendCommand(target, appId, commandState);
				break;
			case 2: 
				logTextArea.append((sequenceNum & 0xFF)+". sending data (target: "+target+", appId: "+(appId & 0xFF)+", data: "+dataParamTextField.getText()+")\n");
				sendData(target, appId, parseDataField());
				break;
		}
    }//GEN-LAST:event_sendButtonActionPerformed
    
    private void configNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configNameTextActionPerformed
        if( configNameText.getSelectedIndex() < 0 )
            return;
        String name = (String)configNameText.getSelectedItem();
        Preferences p = prefs.node(prefs.absolutePath() + "/config/" + name);
        
        appIdTextField.setText(new String(p.get("appId", "0x00")));
        targetTextField.setText(new String(p.get("target", "0x0000")));
        intCmdTextField.setText(new String(p.get("intParam", "0x00")));
        dataParamTextField.setText(new String(p.get("dataParam", "")));
        usageTextField.setText(new String(p.get("usage", "")));
        
        jTabbedPane1.setSelectedIndex(Integer.parseInt(p.get("type","0")));
        int selected = Integer.parseInt(p.get("command","0"));
        
        if(selected == START)
            buttonGroup1.setSelected(startRadioButton.getModel(),true);
        else if(selected == STOP)
            buttonGroup1.setSelected(stopRadioButton.getModel(),true);
        else if(selected == RESTART)
            buttonGroup1.setSelected(restartRadioButton.getModel(),true);
                      
        setTitle("Remote Controller - "+name);
        super.setTitle(getTitle());
    }//GEN-LAST:event_configNameTextActionPerformed
    
    private void delConfigButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_delConfigButtonActionPerformed
        String name = (String)configNameText.getSelectedItem();
        logTextArea.append("configuration " + name + " removed\n");
        configNameText.removeItem(name);
        try {
            saveComboBox();
            Preferences p = prefs.node(prefs.absolutePath() + "/config/" + name);
            p.removeNode();
            prefs.flush();
        } catch(BackingStoreException e) {
            logTextArea.append("could not write preferences\n");
        };
    }//GEN-LAST:event_delConfigButtonActionPerformed
    
    private void saveConfigButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveConfigButtonActionPerformed
        String name = (String)configNameText.getSelectedItem();
        logTextArea.append("configuration " + name + " saved\n");
        
        if( configNameText.getSelectedIndex() < 0 )
            configNameText.addItem(name);
        try {
            setTitle("Remote Controller - "+name);
            super.setTitle(getTitle());
            saveComboBox();
            Preferences p = prefs.node(prefs.absolutePath() + "/config/" + name);
            p.put("appId", appIdTextField.getText());
            p.put("target", targetTextField.getText());
            p.put("intParam", intCmdTextField.getText());
            p.put("dataParam", dataParamTextField.getText());
            p.put("usage", usageTextField.getText());
            
            
            p.put("type", Integer.toString(jTabbedPane1.getSelectedIndex()));
            
            int commandSelect = 0;
            javax.swing.ButtonModel selectedButton = buttonGroup1.getSelection();
            if(selectedButton == startRadioButton.getModel())
                commandSelect = START;
            else if(selectedButton == stopRadioButton.getModel())
                commandSelect = STOP;
            else if(selectedButton == restartRadioButton.getModel())
                commandSelect = RESTART;
            p.put("command", Integer.toString(commandSelect ));
            
            prefs.flush();
        } catch(BackingStoreException e) {
            logTextArea.append("could not write preferences\n");
        };
    }//GEN-LAST:event_saveConfigButtonActionPerformed
    
    private void addToLogTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addToLogTextFieldActionPerformed
        logTextArea.append(addToLogTextField.getText()+"\n");
    }//GEN-LAST:event_addToLogTextFieldActionPerformed
    
    private void addtoLogButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addtoLogButtonActionPerformed
        logTextArea.append(addToLogTextField.getText()+"\n");
    }//GEN-LAST:event_addtoLogButtonActionPerformed
    
    
        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton sendButton;
        private javax.swing.JRadioButton startRadioButton;
        private javax.swing.JLabel intCmdLabel;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JTextField targetTextField;
        private javax.swing.JTextField intCmdTextField;
        private javax.swing.JPanel controlPanel;
        private javax.swing.JComboBox configNameText;
        private javax.swing.JRadioButton restartRadioButton;
        private javax.swing.JLabel seqnLabel;
        private javax.swing.JTextField dataParamTextField;
        private javax.swing.JPanel runPanel;
        private javax.swing.JLabel appIdLabel;
        private javax.swing.JButton delConfigButton;
        private javax.swing.JPanel commandPanel;
        private javax.swing.JTextArea usageTextField;
        private javax.swing.JTextField appIdTextField;
        private javax.swing.JButton clearLogButton;
        private javax.swing.JPanel intPanel;
        private javax.swing.JTextField seqnTextField;
        private javax.swing.JLabel dataParamLabel;
        private javax.swing.ButtonGroup buttonGroup1;
        private javax.swing.JPanel logPanel;
        private javax.swing.JTextArea logTextArea;
        private javax.swing.JLabel targetLabel;
        private javax.swing.JPanel savePanel;
        private javax.swing.JRadioButton stopRadioButton;
        private javax.swing.JTextField addToLogTextField;
        private javax.swing.JButton saveConfigButton;
        private javax.swing.JTabbedPane jTabbedPane1;
        private javax.swing.JButton addtoLogButton;
        // End of variables declaration//GEN-END:variables

	private byte[] parseDataField(){
		StringTokenizer tokenizedData = new StringTokenizer(dataParamTextField.getText());
		byte[] data = new byte[tokenizedData.countTokens()*2];
		int count = 0;
		while (tokenizedData.hasMoreTokens()) {
			String dataToken = tokenizedData.nextToken();
			if (dataToken.substring(dataToken.length()-1).toUpperCase().equals("W")){
				String wordString = dataToken.substring(0,dataToken.length()-1);
				data[count++] = (byte)parseInt(wordString);
				data[count++] = (byte)(parseInt(wordString)/256);
			}else{
				data[count++] = (byte)parseInt(dataToken);
			}
		}
		byte[] returnData = new byte[count];
		System.arraycopy(data, 0, returnData, 0, count);
		
		return returnData;
	}
	
	private int parseInt(String value){
		try{
			if(value == null)
				return 0;
			else if(value.trim().toUpperCase().startsWith("0X"))
				return Integer.parseInt(value.trim().substring(2),16);
			else
				return Integer.parseInt(value.trim());
		}
		catch(RuntimeException e){
			return 0;
		}
	}
	
    private void saveComboBox() {
        String items = "";
        for(int i = 0; i < configNameText.getItemCount(); ++i)
            items = items + (String)configNameText.getItemAt(i) + "\n";
        
        prefs.put("configs", items);
    }
    
    private void loadComboBox() {
        configNameText.removeAllItems();
        StringTokenizer tokenizer = new StringTokenizer(prefs.get("configs", ""), "\n");
        while( tokenizer.hasMoreTokens() )
            configNameText.addItem(tokenizer.nextToken());
    }

    public static void sendInteger(int target, byte appId, int integer){
        byte data[] = new byte[7];
        // seqNum
        data[0] = sequenceNum++;
        // target
        data[1] = (byte)target;
        data[2] = (byte)(target/256);
        // dataType
        data[3] = INT;
        // application ID
        data[4] = appId;
        // integer (uint16_t)
        data[5] = (byte)integer;
        data[6] = (byte)(integer/256);

		for (int i = 0; i < COMMAND_RESEND; i++)
			SerialConnector.instance().sendMessage(BROADCAST,AM_TYPE,data);
    }
    
    public static void sendData(int target, byte appId, byte pData[]){
        byte data[];
        if (pData.length > 24){
            // write to log that the data part is too long, it will be sent truncated
            data = new byte[29];
        } else{
            data = new byte[5+pData.length];
        }
        // seqNum
        data[0] = sequenceNum++;
        // target
        data[1] = (byte)target;
        data[2] = (byte)(target/256);
        // dataType
        data[3] = DATA;
        // application ID
        data[4] = appId;
        // data copy
        System.arraycopy(pData, 0, data, 5, data.length-5);

		for (int i = 0; i < COMMAND_RESEND; i++)
			SerialConnector.instance().sendMessage(BROADCAST,AM_TYPE,data);
    }
    
    public static void sendCommand(int target, byte appId, byte command){
		byte data[] = new byte[6];
        // seqNum
		data[0] = sequenceNum++;
        // target
        data[1] = (byte)target;
        data[2] = (byte)(target/256);
        // dataType
        data[3] = COMMAND;
        // application ID
        data[4] = appId;
        // command
        data[5] = command;
		
		for (int i = 0; i < COMMAND_RESEND; i++)
			SerialConnector.instance().sendMessage(BROADCAST,AM_TYPE,data);
	}
}
