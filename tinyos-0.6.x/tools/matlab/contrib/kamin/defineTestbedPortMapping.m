function defineTestbedPortMapping()
%defineTestbedPortMapping()
%
%This function will set the portmapping of any node 
%that you hardcoded into this file
%

%     "Copyright (c) 2000 and The Regents of the University of California.  All rights reserved.
% 
%     Permission to use, copy, modify, and distribute this software and its documentation for any purpose, without fee, and without written agreement 
%     is hereby granted, provided that the above copyright notice and the following two paragraphs appear in all copies of this software.
%     
%     IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES ARISING 
%     OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
%
%     THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
%     FITNESS FOR A PARTICULAR PURPOSE.  THE SOFTWARE PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF CALIFORNIA HAS NO OBLIGATION TO
%     PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS."
%     
%     Authors:  Kamin Whitehouse <kamin@cs.berkeley.edu>
%     Date:     May 10, 2002 

global PORT_MAP
global COMM

if isfield(COMM,'packetPorts')
    packetPorts = COMM.packetPorts;

	for i= 1:length(packetPorts)
        pp = get(packetPorts(i),'dataPort');
        idStr = pp(1).name.toCharArray;  
        idStr=idStr';
        pos=findstr(':',idStr);
        host=idStr(1:pos-1);
        if strcmpi(host,'testbed')
            offset=0;
        else
            offset=100;
        end
%        port=idStr{i}(pos+1:end);

        moteID = idStr(end-1:end);
        moteID = str2num(moteID)+offset;
        PORT_MAP{moteID} = idStr;
	end
end