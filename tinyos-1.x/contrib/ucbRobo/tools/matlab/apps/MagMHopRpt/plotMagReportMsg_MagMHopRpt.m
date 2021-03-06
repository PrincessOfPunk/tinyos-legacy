function plotMagReportMsg_MagMHopRpt(address, message, connectionName)
%hack, fix later!!!!
% Because MagReportMsgs report very quickly, you may get a steady stream of
% reports if your threshold is set too low.  MAGMHOPRPT.ReportMsgFlag is a
% flag to shut of message reports while you tune the report threshold

global APPS;
if (isfield(APPS,'MAGMHOPRPT') && APPS.MAGMHOPRPT.rptMsgFlag)
    moteID = message.get_sourceMoteID;
    x = floor(mod(moteID,100)/10);
    y = mod(mod(moteID,100),10);
    fprintf(1,'moteID=%d x=%d y=%d\n',moteID,x,y);
    figure(1); clf
    axis([-1 9 -1 5]);
    hold on
    for n1=0:8
        for n2=0:4
            plot(n1,n2,'k.');
        end
    end
    plot(x,y,'ro','MarkerSize',15);
    drawnow
end