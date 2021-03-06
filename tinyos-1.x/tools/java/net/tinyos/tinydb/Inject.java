// $Id: Inject.java,v 1.3 2003/10/07 21:46:07 idgay Exp $

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
package net.tinyos.tinydb;
import net.tinyos.tinydb.parser.*;
import net.tinyos.message.*;
import java.io.*;

/** 
    Simple standalone demo application to run a 
    query and print out the results.  
*/

public class Inject implements ResultListener {
    public Inject(String argv[]) {
	TinyDBMain.debug = true;
	int qid =0;
	String qs = "";

	try {
	    TinyDBMain.initMain();

	    
	    if (argv.length != 2) {
		System.out.println("Invalid number of arguments.");
		System.exit(0);

	    } else if (argv.length == 2) {
		qs = argv[0];
		qid = new Integer(argv[1]).intValue();
	    } 
	    
	    //parse the query
	    TinyDBQuery q = SensorQueryer.translateQuery(qs, (byte)qid);
	    TinyDBMain.injectQuery(q,this);
	    while (TinyDBMain.network.sendingQuery()) {
		Thread.currentThread().sleep(100);
	    }
	    Thread.currentThread().sleep(100);
	    System.exit(0);
	} catch (IOException e) {
	    System.out.println("Couldn't access network: " + e);
	} catch (InterruptedException e) {
	    System.exit(0);
	} catch (NumberFormatException e) {
	    System.out.println("Invalid query id: " + argv[1]);
	} catch (ParseException e) {
	    System.out.println("Invalid query: " + qs);
	}
    }

    public static void main(String argv[]) {
	new Inject(argv);
    }

    /* ResultListenr method called whenever a result arrives */
    public void addResult(QueryResult qr) {


    }


}
