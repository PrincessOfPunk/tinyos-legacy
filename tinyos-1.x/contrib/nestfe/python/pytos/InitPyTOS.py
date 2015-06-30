#$Id: InitPyTOS.py,v 1.1 2005/05/03 07:38:38 cssharp Exp $

# "Copyright (c) 2000-2003 The Regents of the University of California.  
# All rights reserved.
#
# Permission to use, copy, modify, and distribute this software and its
# documentation for any purpose, without fee, and without written agreement
# is hereby granted, provided that the above copyright notice, the following
# two paragraphs and the author appear in all copies of this software.
# 
# IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR
# DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING OUT
# OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY
# OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
# 
# THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
# INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY
# AND FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS
# ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
# PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS."

# @author Cory Sharp <cssharp@eecs.berkeley.edu>

import os, PyTOS
from jpype import *

startJVM( "%s/jre/bin/server/jvm.dll" % os.popen("cygpath -m \"`which java`\"").read()[:-10], "-ea", "-Djava.class.path=%s" % os.environ["CLASSPATH"] )

pytos = PyTOS.PyTOS()

