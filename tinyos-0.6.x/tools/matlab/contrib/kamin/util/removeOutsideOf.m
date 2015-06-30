function data = removeOutsideOf(data, lowerBound, upperBound)
%data = removeAbove(data, lowerBound, upperBound)
%
%this function removes all values outside of [lowerBound, upperBound] from the dataset

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

data2=[];
for i = 1:length(data)
    if data(i) <= upperBound & data(i) >= lowerBound  
        data2(end+1)=data(i);
    end
end

if ~isempty(data2)
    data = data2;
end