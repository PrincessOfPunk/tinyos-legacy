function h=watch(varargin)
%watch(varargin)
%
%USAGE:     watch(moteIDs, <PLOT parameters>)
%
%This function takes an array of moteIDs and matlab PLOT parameters.
%
%THIS FUNCTION IS EXACTLY LIKE PLOT.  It even takes the same arguments (in fact it just passes them on to PLOT). 
%WATCH pops up a watch figure, which is just a modified PLOT figure. WATCH returns a handle to that figure, just as PLOT does.
%
%There are only ways that WATCH differs from PLOT:
%   1.  The first argument must always be an array of moteIDs
%   2.  data from these motes is constantly added to the WATCH figure
%   3.  The xaxis of the WATCH figure is automatically adjusted so that the new data fits on the figure
%
%You can interact with the WATCH figure the same way you would after using PLOT.  In particular, you should know how to use
%the SET, AXIS, LEGEND, XLABEL, YLABEL, TITLE, etc, commands.  To start watching new data in a previously existing figure
%just use HOLD like you would for PLOT.  
%
%There are also a few new functions associated with a WATCH figure
%   1. removeWatch(h)
%   1. setWatchedField(h) -- this is a string holding the name of the field of the packet that should be watched.
%   1. setAddMethod(h) -- this is a string holding the name of the function used to add data.
%   2. setWatchDuration(h) -- sets the number of data points saved in the watch figure
%   3. setTrailerLength(h)  -- sets the amount of space behind the beginning of the last watched data to show
%   4. setHeaderLength(h)  -- sets the amount of space in front of the beinning of the first watched data to show
%
%To get the data out of the watch, use the handle that was returned: x = get(h, 'XData'); y = get(h, 'YData');
%In this way, you can watch some data and then extract the data later for further analysis.
%
%It is possible to set a flag such that when a data stream realizes that it is going off the plot it could simply wrap
%around to the beginning of the plot again, but this hasn't been implemented.
%
%If you want to watch on a log scale or use some other plot function, change the WATCH_PLOT_FUNTION global variable.

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

global DEFAULT_WATCH_PARAMS
global WATCH_PLOT_FUNCTION %note that the default is 'plot'.  Change this global to 'loglog' or whatever kind of 2D plot you want

watchParams = DEFAULT_WATCH_PARAMS;   %note that we could allow the user to send a new set of watch params, but that is too much trouble.
moteIDs = varargin{1};
varargin = {varargin{2:end}};
h = [];

holdWasOn = 0;  %remember if HOLD was on before
if ishold
    holdWasOn = 1;
    hold off;
end

packetPorts = getPortMapping(moteIDs);
for i = 1:length(packetPorts)
    
    %Note: I have to go through all these next few lines of code because matlab doesn't have a command
    %that will just give me a blank plot
    eval(['hTemp = ' WATCH_PLOT_FUNCTION '(1, 1, varargin{:});']);   %create a new figure (with bogus data x=1, y=1)
    h(i) = hTemp(1);        %Note that if the user assigns initial data it will be stored in a second plot, not this one.
    set(h(i), 'XData',[], 'YData', []);   %get rid of the bogus data.  Now we have an new empty plot
    watchParams.packetPort = packetPorts(i);
    set(h,'EraseMode','background');                   
    if watchParams.circularIndex>0
        set(h(i),'XData',1:watchParams.duration,'YData',zeros(1,watchParams.duration))
        hold on
        watchParams.trace = plot(1,0,'r.','MarkerSize',20);
    end
    set(h(i),'UserData', watchParams);           % and now it is a WATCH figure
    addPacketListeners(packetPorts(i), {'watchReceiveNewData', h(i)});
    hold on; %make sure all these serial ports are ploted on the same figure
end

if holdWasOn==0  %return the previous hold parameter
    hold off;
end