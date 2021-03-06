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

import net.tinyos.util.*;
import java.util.*;
import java.util.prefs.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;

/**
 * @author  Miklos Maroti (miklos.maroti@vanderbilt.edu)
 */
public class MessageTable extends MessageCenterInternalFrame implements PacketListenerIF
{
	static final int TYPE_INT8 = 1;
	static final int TYPE_UINT8 = 2;
	static final int TYPE_HEX8 = 3;
	static final int TYPE_INT16 = 4;
	static final int TYPE_UINT16 = 5;
	static final int TYPE_HEX16 = 6;
	static final int TYPE_INT32 = 7;
	static final int TYPE_UINT32 = 8;
	static final int TYPE_HEX32 = 9;
	static final int TYPE_FLOAT = 10;
	static final int TYPE_CHAR = 11;

	static final int TYPE_NONE = 0;			// invalid type
	static final int TYPE_TIME = 101;		// current time
	static final int TYPE_COUNTER = 102;		// increasing counter

	static final int MODIFIER_OMIT = 0x01;		// do not display this idtem
	static final int MODIFIER_UNIQUE = 0x02;	// must be unique
	static final int MODIFIER_CONST = 0x04;		// value must match title
	
	static final int PACKET_TYPE = 2;
	static final int PACKET_LENGTH = 4;
	static final int PACKET_DATA = 5;
	static final int PACKET_TOTAL = 36;
		
	private class MyTableModel extends DefaultTableModel
	{
		java.text.SimpleDateFormat timestamp = new java.text.SimpleDateFormat("HH:mm:ss.SSSS");

		class Entry
		{
			String title;
			int type;
			int modifier;
			int column;
			String value;
			String defValue;
		};

		ArrayList entries = new ArrayList();
		int[] uniqueEntries = new int[0];		// the index of unique entries
		int packetLength;
		
		byte[] packet;
		int packetIndex;

		byte getByte()
		{
			return (byte)(packet[packetIndex++] & 0xFF);
		}

		short getShort()
		{
			return (short)((packet[packetIndex++] & 0xFF) 
				+ ((packet[packetIndex++] & 0xFF) << 8));
		}

		int getInt()
		{
			return (packet[packetIndex++] & 0xFF) 
				+ ((packet[packetIndex++] & 0xFF) << 8)
				+ ((packet[packetIndex++] & 0xFF) << 16)
				+ ((packet[packetIndex++] & 0xFF) << 24);
		}

		String toHex(int value, int len)
		{
			String a = Integer.toHexString(value);
			String b = "0x";

			for(int i = a.length(); i < len; ++i)
				b += '0';

			b += a;
			return b;
		}

		String decodeField(int type)
		{
			switch(type)
			{
			case TYPE_UINT8:
				return Integer.toString(getByte() & 0xFF);

			case TYPE_INT8:
				return Byte.toString(getByte());

			case TYPE_HEX8:
				return toHex(getByte() & 0xFF, 2);

			case TYPE_UINT16:
				return Integer.toString(getShort() & 0xFFFF);

			case TYPE_INT16:
				return Short.toString(getShort());

			case TYPE_HEX16:
				return toHex(getShort() & 0xFFFF, 4);

			case TYPE_UINT32:
				return Long.toString(getInt() & 0xFFFFFFFFL);

			case TYPE_INT32:
				return Integer.toString(getInt());

			case TYPE_HEX32:
				return toHex(getInt(), 8);

			case TYPE_FLOAT:
				return Float.toString(Float.intBitsToFloat(getInt()));

			case TYPE_CHAR:
				return "'" + (char)getByte() + "'";

			case TYPE_TIME:
				return timestamp.format(new java.util.Date());
				
			case TYPE_COUNTER:
				return "1";
			}

			return null;
		}
	
		// returns true if the packet is valid
		boolean decodePacket(byte[] packet)
		{
			if( (packet[PACKET_LENGTH] & 0xFF) != packetLength )
				return false;
			
			this.packet = packet;
			packetIndex = PACKET_DATA;

			Iterator iter = entries.iterator();
			while( iter.hasNext() )
			{
				Entry entry = (Entry)iter.next();
				entry.value = decodeField(entry.type);

				// const value does not match
				if( (entry.modifier & MODIFIER_CONST) != 0 && ! entry.value.equals(entry.defValue) )
					return false;
			}
	
			return true;
		}

		int findReplaceIndex()
		{
			if( uniqueEntries.length == 0 )
				return -1;

			outer: for(int i = 0; i < getRowCount(); ++i)
			{
				for(int j = 0; j < uniqueEntries.length; ++j)
				{
					Entry entry = (Entry)entries.get(uniqueEntries[j]);
					if( ! entry.value.equals(getValueAt(i,entry.column)) )
						continue outer;
				}
				
				return i;
			}
			
			return -1;
		}

		void writeRow(int index)
		{
			for(int i = 0; i < entries.size(); ++i)
			{
				Entry entry = (Entry)entries.get(i);
				if( entry.column >= 0 )
				{
					if( entry.type == TYPE_COUNTER )
						entry.value = Integer.toString(parseInt((String)getValueAt(index, entry.column)) + 1);

					setValueAt(entry.value, index, entry.column);
				}
			}
		}
		
		void addPacket(byte[] packet)
		{
			if( ! decodePacket(packet) )
				return;
			
			int index = findReplaceIndex();
			if( index < 0 )
			{
				int c = getRowCount();
				setRowCount(c+1);
				writeRow(c);
				fireTableRowsInserted(c, c);
			}
			else
			{
				writeRow(index);
				fireTableRowsUpdated(index, index);
			}
		}

		void addEntry(Entry entry)
		{
			if( entry.title == null )
				entry.title = "unnamed";
			
			if( entry.type == TYPE_NONE )
			{
				errorText.setText("missing type");
				return;
			}
			
			if( (entry.modifier & MODIFIER_OMIT) == 0 )
			{
				entry.column = getColumnCount();
				addColumn(entry.title);
			}
			else
				entry.column = -1;

			entries.add(entry);
		}
		
		void addEntry(String line)
		{
			int i = line.indexOf(';');
			if( i >= 0 )
				line = line.substring(0, i);
			
			Entry entry = new Entry();
			entry.type = TYPE_NONE;

			StringTokenizer tokens = new StringTokenizer(line, " \t,");

			while( tokens.hasMoreTokens() )
			{
				String token = tokens.nextToken();
				String ltoken = token.toLowerCase();
				
				if( ltoken.endsWith("_t") )
					ltoken = ltoken.substring(0, ltoken.length()-2);

				if( entry.type == TYPE_NONE )
				{
					if( ltoken.equals("uint8") )
					{
						entry.type = TYPE_UINT8;
						packetLength += 1;
					}
					else if( ltoken.equals("int8") )
					{
						entry.type = TYPE_INT8;
						packetLength += 1;
					}
					else if( ltoken.equals("hex8") )
					{
						entry.type = TYPE_HEX8;
						packetLength += 1;
					}
					else if( ltoken.equals("uint16") )
					{
						entry.type = TYPE_UINT16;
						packetLength += 2;
					}
					else if( ltoken.equals("int16") )
					{
						entry.type = TYPE_INT16;
						packetLength += 2;
					}
					else if( ltoken.equals("hex16") )
					{
						entry.type = TYPE_HEX16;
						packetLength += 2;
					}
					else if( ltoken.equals("uint32") )
					{
						entry.type = TYPE_UINT32;
						packetLength += 4;
					}
					else if( ltoken.equals("int32") )
					{
						entry.type = TYPE_INT32;
						packetLength += 4;
					}
					else if( ltoken.equals("hex32") )
					{
						entry.type = TYPE_HEX32;
						packetLength += 4;
					}
					else if( ltoken.equals("float") )
					{
						entry.type = TYPE_FLOAT;
						packetLength += 4;
					}
					else if( ltoken.equals("char") )
					{
						entry.type = TYPE_CHAR;
						packetLength += 1;
					}
					else if( ltoken.equals("time") )
						entry.type = TYPE_TIME;
					else if( ltoken.equals("counter") )
						entry.type = TYPE_COUNTER;
					else if( ltoken.equals("omit") )
						entry.modifier |= MODIFIER_OMIT;
					else if( ltoken.equals("unique") )
						entry.modifier |= MODIFIER_UNIQUE;
					else if( ltoken.equals("const") )
						entry.modifier |= MODIFIER_OMIT | MODIFIER_CONST;
					else
					{
						errorText.setText("unknown type or modifier: " + token);
						break;
					}
				}
				else if( ltoken.equals("=") && tokens.hasMoreTokens() )
					entry.defValue = tokens.nextToken();
				else if( entry.title == null )
					entry.title = token;
				else
				{
					errorText.setText("extra token found: " + token);
					break;
				}
			}

			addEntry(entry);
		}

		void updateUniqueEntries()
		{
			int size = 0;
			for(int i = 0; i < entries.size(); ++i)
			{
				Entry entry = (Entry)entries.get(i);
				if( (entry.modifier & MODIFIER_UNIQUE) != 0 )
					++size;
			}
			
			uniqueEntries = new int[size];
			size = 0;
			
			for(int i = 0; i < entries.size(); ++i)
			{
				Entry entry = (Entry)entries.get(i);
				if( (entry.modifier & MODIFIER_UNIQUE) != 0 )
					uniqueEntries[size++] = i;
			}
		}
		
		void resetEntries(String text)
		{
			setColumnCount(0);
			setRowCount(0);
			entries.clear();
			packetLength = 0;
			
			StringTokenizer lines = new StringTokenizer(text, "\n\r\f");
			while( lines.hasMoreTokens() )
				addEntry(lines.nextToken());
			
			updateUniqueEntries();
			
			lenText.setText(Integer.toString(packetLength));
			
			fireTableStructureChanged();
		}
		
		void removeRows(int[] rows)
		{
			int i = rows.length;
			while( --i >= 0 )
				removeRow(rows[i]);
			
			fireTableDataChanged();
		}

		void sendByte(int a)
		{
			packet[packetIndex++] = (byte)a;
		}

		void sendShort(int a)
		{
			packet[packetIndex++] = (byte)a;
			packet[packetIndex++] = (byte)(a >> 8);
		}

		void sendInt(int a)
		{
			packet[packetIndex++] = (byte)a;
			packet[packetIndex++] = (byte)(a >> 8);
			packet[packetIndex++] = (byte)(a >> 16);
			packet[packetIndex++] = (byte)(a >> 24);
		}

		void sendEntry(Entry entry)
		{
			if( (entry.modifier & MODIFIER_OMIT) != 0 )
				entry.value = entry.defValue;
			
			switch(entry.type)
			{
			case TYPE_UINT8:
			case TYPE_INT8:
			case TYPE_HEX8:
				sendByte(parseInt(entry.value));
				break;

			case TYPE_UINT16:
			case TYPE_INT16:
			case TYPE_HEX16:
				sendShort(parseInt(entry.value));
				break;

			case TYPE_UINT32:
			case TYPE_INT32:
			case TYPE_HEX32:
				sendInt(parseInt(entry.value));
				break;

			case TYPE_FLOAT:
				sendInt(Float.floatToIntBits(parseFloat(entry.value)));
				break;
			}
		}
		
		void sendRow(int index)
		{
			packet = new byte[packetIndex];
			packetIndex = 0;
			
			String text = "sending message " + index;
			errorText.setText(text);
			
			for(int i = 0; i < entries.size(); ++i)
			{
				Entry entry = (Entry)entries.get(i);
				if( entry.column >= 0 )
					entry.value = (String)getValueAt(index, entry.column);

				sendEntry(entry);
			}
			
			if( text.equals(errorText.getText()) )	// if no error
			{
				// broadcast it now
				SerialConnector.instance().sendMessage(0xFFFF, (short)amType, (short)0, packet);
			}
		}
		
		void sendRows(int[] rows)
		{
			for(int i = 0; i < rows.length; ++i)
				sendRow(rows[i]);
		}
		
		class RowComparator implements Comparator
		{
			int column = 0;
			
			public RowComparator(int column)
			{
				this.column = column;
			}
			
			public int compare(Object o1, Object o2)
			{
				if( o1 == null )
					return -1;
				if( o2 == null )
					return 1;
				
				Comparable c1 = (Comparable)((Vector)o1).get(column);
				Comparable c2 = (Comparable)((Vector)o2).get(column);
				
				return c1.compareTo(c2);
			}
		};
		
		void sort(int column)
		{
			Collections.sort(dataVector, new RowComparator(column));
		}
	};
	
	int amType;
	private MyTableModel tableModel = new MyTableModel();

	int parseInt(String value)
	{
		try
		{
			if(value == null)
				return 0;
			else if(value.trim().toUpperCase().startsWith("0X"))
				return Integer.parseInt(value.trim().substring(2),16);
			else
				return Integer.parseInt(value.trim());
		}
		catch(RuntimeException e)
		{
			errorText.setText("invalid integer format: " + value);
			return 0;
		}
	}
	
	float parseFloat(String value)
	{
		try
		{
			if( value == null )
				return 0;
			else
				return Float.parseFloat(value);
		}
		catch(RuntimeException e)
		{
			errorText.setText("invalid float format: " + value);
			return 0;
		}
	}

	public class MyColumnListener extends MouseAdapter
	{
		public void mouseClicked(MouseEvent event)
		{
			if( event.getClickCount() != 2 )
				return;
			
			int index = table.getColumnModel().getColumnIndexAtX(event.getX());
			index = table.convertColumnIndexToModel(index);
			if( index < 0 )
				return;
		
			tableModel.sort(index);
		}
	};
	
	Preferences prefs = null;
	
	/** Creates new form MessageTable */
	public MessageTable() 
	{
		initComponents();
		
		table.getTableHeader().addMouseListener(new MyColumnListener());
		
		prefs = Preferences.userNodeForPackage(this.getClass());
		prefs = prefs.node(prefs.absolutePath()+"/MessageTable");
		loadComboBox();
		
		SerialConnector.instance().registerPacketListener(this,
			SerialConnector.GET_ALL_MESSAGES);
	}
	
	public void packetReceived(byte[] packet) 
	{
		if( (packet[PACKET_TYPE] & 0xFF) == amType )
			tableModel.addPacket(packet);
	}	
	
	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
        private void initComponents() {//GEN-BEGIN:initComponents
                java.awt.GridBagConstraints gridBagConstraints;

                jPanel2 = new javax.swing.JPanel();
                configNameText = new javax.swing.JComboBox();
                jButton2 = new javax.swing.JButton();
                jButton3 = new javax.swing.JButton();
                errorText = new javax.swing.JTextField();
                jPanel3 = new javax.swing.JPanel();
                jLabel1 = new javax.swing.JLabel();
                amTypeText = new javax.swing.JTextField();
                timeStampBox = new javax.swing.JCheckBox();
                counterBox = new javax.swing.JCheckBox();
                jScrollPane2 = new javax.swing.JScrollPane();
                formatText = new javax.swing.JTextArea();
                jLabel2 = new javax.swing.JLabel();
                lenText = new javax.swing.JTextField();
                jPanel4 = new javax.swing.JPanel();
                jPanel1 = new javax.swing.JPanel();
                jButton41 = new javax.swing.JButton();
                jButton4 = new javax.swing.JButton();
                jButton5 = new javax.swing.JButton();
                jButton1 = new javax.swing.JButton();
                jScrollPane1 = new javax.swing.JScrollPane();
                table = new javax.swing.JTable();

                getContentPane().setLayout(new java.awt.GridBagLayout());

                setTitle("Message Table");
                setMinimumSize(new java.awt.Dimension(300, 100));
                jPanel2.setLayout(new java.awt.GridBagLayout());

                jPanel2.setBorder(new javax.swing.border.TitledBorder("Configuration"));
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
                jPanel2.add(configNameText, gridBagConstraints);

                jButton2.setText("Save");
                jButton2.setToolTipText("save the current configuration in the preferences");
                jButton2.setMaximumSize(new java.awt.Dimension(80, 26));
                jButton2.setMinimumSize(new java.awt.Dimension(80, 26));
                jButton2.setPreferredSize(new java.awt.Dimension(80, 26));
                jButton2.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton2ActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
                jPanel2.add(jButton2, gridBagConstraints);

                jButton3.setText("Delete");
                jButton3.setToolTipText("delete the current configuration from the preferences");
                jButton3.setMaximumSize(new java.awt.Dimension(80, 26));
                jButton3.setMinimumSize(new java.awt.Dimension(80, 26));
                jButton3.setPreferredSize(new java.awt.Dimension(80, 26));
                jButton3.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton3ActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTH;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 3, 3);
                jPanel2.add(jButton3, gridBagConstraints);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                getContentPane().add(jPanel2, gridBagConstraints);

                errorText.setEditable(false);
                errorText.setText("no error");
                errorText.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 0);
                getContentPane().add(errorText, gridBagConstraints);

                jPanel3.setLayout(new java.awt.GridBagLayout());

                jPanel3.setBorder(new javax.swing.border.TitledBorder("Message Format"));
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
                jLabel1.setText("msg type:");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 3);
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                jPanel3.add(jLabel1, gridBagConstraints);

                amTypeText.setText("0x00");
                amTypeText.setToolTipText("the active message type");
                amTypeText.setMinimumSize(new java.awt.Dimension(35, 20));
                amTypeText.setPreferredSize(new java.awt.Dimension(35, 20));
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                jPanel3.add(amTypeText, gridBagConstraints);

                timeStampBox.setText("time stamp");
                timeStampBox.setToolTipText("put a timestamp on each incoming message");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 2;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                jPanel3.add(timeStampBox, gridBagConstraints);

                counterBox.setText("counter");
                counterBox.setToolTipText("show the count of received messages with the same unique fields");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 3;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                gridBagConstraints.weightx = 1.0;
                jPanel3.add(counterBox, gridBagConstraints);

                jScrollPane2.setHorizontalScrollBarPolicy(javax.swing.JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
                jScrollPane2.setMinimumSize(new java.awt.Dimension(150, 100));
                formatText.setColumns(12);
                formatText.setToolTipText("format of each line: [unique, omit, const] <type> <name> [ = <value>]");
                formatText.setMinimumSize(new java.awt.Dimension(100, 100));
                formatText.setPreferredSize(new java.awt.Dimension(132, 100));
                jScrollPane2.setViewportView(formatText);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 4;
                gridBagConstraints.gridwidth = 2;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weighty = 1.0;
                jPanel3.add(jScrollPane2, gridBagConstraints);

                jLabel2.setText("length:");
                jLabel2.setToolTipText("null");
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.insets = new java.awt.Insets(0, 4, 0, 3);
                gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
                jPanel3.add(jLabel2, gridBagConstraints);

                lenText.setEditable(false);
                lenText.setToolTipText("the expected length of the packets");
                lenText.setPreferredSize(new java.awt.Dimension(35, 20));
                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
                jPanel3.add(lenText, gridBagConstraints);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 1;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                getContentPane().add(jPanel3, gridBagConstraints);

                jPanel4.setLayout(new java.awt.GridBagLayout());

                jPanel4.setBorder(new javax.swing.border.TitledBorder("Table"));
                jPanel1.setLayout(new java.awt.GridBagLayout());

                jButton41.setText("Add Row");
                jButton41.setToolTipText("adds an empty row in the table");
                jButton41.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton41ActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridy = 0;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
                jPanel1.add(jButton41, gridBagConstraints);

                jButton4.setText("Delete Row(s)");
                jButton4.setToolTipText("removes the selected rows from the table");
                jButton4.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton4ActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridy = 0;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
                jPanel1.add(jButton4, gridBagConstraints);

                jButton5.setText("Send Msg(s)");
                jButton5.setToolTipText("broadcasts  the selected rows");
                jButton5.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton5ActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridy = 0;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
                jPanel1.add(jButton5, gridBagConstraints);

                jButton1.setText("Reset");
                jButton1.setToolTipText("reparse the message format and clear the table");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridy = 0;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.insets = new java.awt.Insets(0, 3, 0, 3);
                jPanel1.add(jButton1, gridBagConstraints);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 0;
                gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
                gridBagConstraints.insets = new java.awt.Insets(0, 0, 5, 0);
                jPanel4.add(jPanel1, gridBagConstraints);

                jScrollPane1.setBorder(new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1)));
                jScrollPane1.setMinimumSize(new java.awt.Dimension(200, 100));
                jScrollPane1.setPreferredSize(new java.awt.Dimension(400, 403));
                table.setModel(tableModel);
                table.setToolTipText("double click on a column to sort the rows");
                jScrollPane1.setViewportView(table);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 2.0;
                gridBagConstraints.weighty = 1.0;
                jPanel4.add(jScrollPane1, gridBagConstraints);

                gridBagConstraints = new java.awt.GridBagConstraints();
                gridBagConstraints.gridx = 0;
                gridBagConstraints.gridy = 1;
                gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
                gridBagConstraints.weightx = 1.0;
                gridBagConstraints.weighty = 1.0;
                getContentPane().add(jPanel4, gridBagConstraints);

                pack();
        }//GEN-END:initComponents

	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
		int[] rows = table.getSelectedRows();
		tableModel.sendRows(rows);
	}//GEN-LAST:event_jButton5ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
		int[] rows = table.getSelectedRows();
		tableModel.removeRows(rows);
	}//GEN-LAST:event_jButton4ActionPerformed

	private void jButton41ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton41ActionPerformed
		byte[] packet = new byte[PACKET_DATA + tableModel.packetLength];
		packet[PACKET_LENGTH] = (byte)tableModel.packetLength;
		tableModel.addPacket(packet);
	}//GEN-LAST:event_jButton41ActionPerformed

	private void configNameTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_configNameTextActionPerformed
		if( configNameText.getSelectedIndex() < 0 )
			return;
		String name = (String)configNameText.getSelectedItem();
		Preferences p = prefs.node(prefs.absolutePath() + "/config/" + name);
		amTypeText.setText(new String(p.get("amType", "0x00")));
		counterBox.setSelected(p.getBoolean("counter", false));
		timeStampBox.setSelected(p.getBoolean("timeStamp", false));
		formatText.setText(new String(p.get("format", "")));
		jButton1ActionPerformed(null);
	}//GEN-LAST:event_configNameTextActionPerformed

	private void saveComboBox()
	{
		String items = "";
		for(int i = 0; i < configNameText.getItemCount(); ++i)
			items = items + (String)configNameText.getItemAt(i) + "\n";
		
		prefs.put("configs", items);
	}

	private void loadComboBox()
	{
		configNameText.removeAllItems();
		StringTokenizer tokenizer = new StringTokenizer(prefs.get("configs", ""), "\n");
		while( tokenizer.hasMoreTokens() )
			configNameText.addItem(tokenizer.nextToken());
	}
	
	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
		String name = (String)configNameText.getSelectedItem();
		errorText.setText("configuration " + name + " removed");
		configNameText.removeItem(name);
		try {
			saveComboBox();
			Preferences p = prefs.node(prefs.absolutePath() + "/config/" + name);
			p.removeNode();
			prefs.flush();
		} catch(BackingStoreException e) { 
			errorText.setText("could not write preferences");
		};
	}//GEN-LAST:event_jButton3ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		String name = (String)configNameText.getSelectedItem();
		errorText.setText("configuration " + name + " saved");
		if( configNameText.getSelectedIndex() < 0 )
			configNameText.addItem(name);
		try {
			saveComboBox();
			Preferences p = prefs.node(prefs.absolutePath() + "/config/" + name);
			p.put("amType", amTypeText.getText());
			p.putBoolean("counter", counterBox.isSelected());
			p.putBoolean("timeStamp", timeStampBox.isSelected());
			p.put("format", formatText.getText());
			prefs.flush();
		} catch(BackingStoreException e) { 
			errorText.setText("could not write preferences");
		};
	}//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
	errorText.setText("format parsed for AM type " + amTypeText.getText());
	amType = parseInt(amTypeText.getText()) & 0xFF;
	String format = formatText.getText();
	if( counterBox.isSelected() )
		format = "counter counter\n" + format;
	if( timeStampBox.isSelected() )
		format = "time timestamp\n" + format;
	tableModel.resetEntries(format);
    }//GEN-LAST:event_jButton1ActionPerformed
	
        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JTable table;
        private javax.swing.JButton jButton2;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JPanel jPanel4;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JComboBox configNameText;
        private javax.swing.JPanel jPanel3;
        private javax.swing.JTextField lenText;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JButton jButton1;
        private javax.swing.JCheckBox counterBox;
        private javax.swing.JPanel jPanel2;
        private javax.swing.JButton jButton3;
        private javax.swing.JScrollPane jScrollPane2;
        private javax.swing.JCheckBox timeStampBox;
        private javax.swing.JTextArea formatText;
        private javax.swing.JButton jButton5;
        private javax.swing.JButton jButton41;
        private javax.swing.JTextField errorText;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JTextField amTypeText;
        private javax.swing.JButton jButton4;
        // End of variables declaration//GEN-END:variables
	
}
