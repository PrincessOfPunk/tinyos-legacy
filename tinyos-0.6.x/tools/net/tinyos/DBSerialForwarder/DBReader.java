/* -*-Mode: C; c-file-style: "BSD" -*-					       	tab:4
 *
 * "Copyright (c) 2001 and The Regents of the University
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
 * $Id: DBReader.java,v 1.2 2002/06/06 01:17:05 ammbot Exp $
 */

package net.tinyos.DBSerialForwarder;

import java.sql.*;
import java.util.*;

public class DBReader {

    Connection db = null;
    Statement  st = null;
    private static final String urlMYSQL = "jdbc:mysql://127.0.0.1:3306/packets";
    private static final String urlPSQL= "jdbc:postgresql://127.0.0.1:16882/motes";
    private String m_usr = "bwhull";
    private String m_pwd = "";
    private boolean POSTGRESQL = true;
    public static final boolean DEBUG_QUERIES = true;
    public static final boolean DEBUG         = true;
    public static final int FIELD1_POS = 1;
    public static final int FIELD2_POS = 2;
    public static final int FIELD3_POS = 3;

    private ResultSet m_rsPackets = null;

    public DBReader( String user, String pass, boolean ps ) {
	m_usr = user;
	m_pwd = pass;
	POSTGRESQL = ps;
    }

    public boolean Connect ( ) {
	try {
	    System.out.println ("Connecting to database");

	    if ( POSTGRESQL ) {
		Class.forName ( "org.postgresql.Driver" );
		db = DriverManager.getConnection(urlPSQL, m_usr, m_pwd);
	    } else {
		Class.forName("org.gjt.mm.mysql.Driver");
		db = DriverManager.getConnection(urlMYSQL, m_usr, m_pwd);
	    }
	    db.setAutoCommit (false);
	    //st = db.createStatement( ResultSet.TYPE_SCROLL_SENSITIVE,	    ResultSet.CONCUR_UPDATABLE );
	    st = db.createStatement ( );
	    return true;
	} catch (Exception ex) {
	    ex.printStackTrace();
	    return false;
	}
    }

    public void Close ( ) {
	try {
	    if ( st != null ) { st.close ( ); }
	    if ( db != null ) { db.close ( ); }
	    st = null;
	    db = null;
	} catch (Exception e ) { }
    }

    public byte[] NextPacket (  ) {
	System.out.println ( "DBReader: getting next packet");
	if ( st == null ) {
	    System.out.println ( "DBReader: Unable to write packet\n");
	    return null;
	}

	byte[] packet = null;

	try {
	    if ( m_rsPackets == null ) {
		System.out.println ("DBReader: executing read query");
		m_rsPackets = st.executeQuery ( "SELECT * FROM packets ORDER BY rcvdtime asc");
	    }
	    if ( m_rsPackets != null && m_rsPackets.next() ) {
		System.out.println ("DBReader: extracting payload");
		packet = m_rsPackets.getBytes ( "payload" );
	    }
	}
	catch ( SQLException e ) {
	    e.printStackTrace ( );
	    return null;
	}
	return packet;
    }

    public Timestamp GetTimestamp ( ) {

	if ( m_rsPackets == null ) {
	    System.out.println ( "DBReader: must first read a packet\n");
	    return null;
	}

	Timestamp ts = null;

	try {
	    if ( m_rsPackets != null ) {
		ts = m_rsPackets.getTimestamp ( "rcvdTime" );
	    }
	}
	catch ( SQLException e ) {
	    e.printStackTrace ();
	    return null;
	}

	return ts;
    }
		
}
