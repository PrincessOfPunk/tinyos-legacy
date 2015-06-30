function addPacketHeaders(name, headers)
%addPacketHeaders(name, headers)
%
%this function defines the default headers that are used by the comm stack
%when receiving and parsing a new packet.

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

global COMM
if ~isfield(COMM, 'packetHeaders')
    COMM.packetHeaders = [];
end

%there is a cleaner way to do this but I don't feel like it
alreadyExists=0;
if isfield(COMM,'packetHeaderNames') & ~isempty(COMM.packetHeaderNames)
	for i=1:length(COMM.packetHeaderNames)
        if strcmpi(COMM.packetHeaderNames{i}, name)
            COMM.packetHeaders{i} = headers;
            alreadyExists=1;
        end
	end
end

if alreadyExists==0
    COMM.packetHeaders{end+1} = headers;
    COMM.packetHeaderNames{length(COMM.packetHeaders)} = name;
end