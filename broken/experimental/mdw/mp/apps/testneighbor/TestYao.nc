/* Copyright (c) 2002 Intel Corporation
 * All rights reserved.
 *
 * This file is distributed under the terms in the attached INTEL-LICENSE     
 * file. If you do not find these files, copies can be found by writing to
 * Intel Research Berkeley, 2150 Shattuck Avenue, Suite 1300, Berkeley, CA, 
 * 94704. Attention: Intel License Inquiry.  
 * 
 * Author: Matt Welsh <mdw@eecs.harvard.edu>
 */

/**
 * Test Yao graph neighborhood construction.
 */
configuration TestYao {
}
implementation {
  components Main, TestNeighborhoodM, YaoNeighborhood;

  Main.StdControl -> TestNeighborhoodM.StdControl;
  TestNeighborhoodM.Neighborhood -> YaoNeighborhood;

}

