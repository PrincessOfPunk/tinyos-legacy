// $Id: TASKClientInfo.java,v 1.2 2003/10/07 21:46:05 idgay Exp $

/*									tab:4
 * "Copyright (c) 2000-2003 The Regents of the University  of California.  
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
 *
 * Copyright (c) 2002-2003 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704.  Attention:  Intel License Inquiry.
 */
package net.tinyos.task.taskapi;

import java.io.*;

/**
 * TASKClientInfo is the class for named opaque client information
 * managed by TASKServer.  Typically clientinfo contains layout information
 * for sensor network deployment.
 * Each TASK client can have multiple clientinfos identified by unique names.
 */
public class TASKClientInfo implements Serializable
{
	/**
	 * Constructor for TASKClientInfo.
	 * 
	 * @param	name	name of the clientinfo.
	 * @param	type	type of the clientinfo, e.g., Java class name
	 * @param	clientinfo	bytes for opaque clientinfo data
	 */
	public TASKClientInfo(String name, String type, byte[] data) 
	{
		this.name = name;
		this.type = type;
		this.data = data;
	};

	public String 	name;	// unique name for the clientinfo
	public String	type;	// type information for clientinfo
	public byte[] 	data;	// data for clientinfo
};

