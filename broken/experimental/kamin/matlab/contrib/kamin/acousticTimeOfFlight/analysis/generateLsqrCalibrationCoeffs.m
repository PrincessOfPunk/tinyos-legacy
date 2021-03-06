function calibrationCoeffs=generateLsqrCalibrationCoeffs(rangingData, varargin)
%calibrationCoeffs=generateLsqrCalibrationCoeffs(rangingData, varargin)
%
%this function uses the data in rangingData to calculate calibration
%coefficients for each sounder/microphone.   It does this by choosing
%parameters for each device that minimize the difference between the 
%estimated distances and the true distances (in the least squares sense).
%If the true distances are not known (indicated with -1), it minimizes the difference between
%the estimated distances and the estimated backwards distances.
%
%rangingData is a matrix as follows:
%
%rangingData(transmitter, receiver, time, [truth estimate])
%
%This file assumes that there are no false positives in the data, which
%would really screw up the calibration coefficients.

%parse arguments
if length(varargin)>0
    polynomialDegree = varargin{1};
else
    polynomialDegree= 1; %default to linear regression
end

rangingData = rangingData./max(max(max(max(rangingData))));

row=0;
for transmitter = 1:size(rangingData,1)
    for receiver = 1:size(rangingData,2)
        for time = 1:size(rangingData,3) %assume TOF is a vector of TOF_DISTANCE structures
	        if rangingData(transmitter, receiver, time, 2)~=0
                
                if rangingData(transmitter, receiver, time, 1)>0 %if we know the distance, minimize error to known
                    row=row+1;
                    A(row,:) = polynomialCalibrationFunction(rangingData(transmitter, receiver, time, 2),transmitter,receiver,polynomialDegree, size(rangingData,1)*(polynomialDegree+1)+size(rangingData,2)*(polynomialDegree+1));
                    B(row) = rangingData(transmitter, receiver, time, 1); 
                end
                if rangingData(receiver, transmitter, time, 2)~=0 & (transmitter < receiver) %otherwise maximize consistency to reverse estimate
                    row=row+1;
                    A(row,:) = polynomialCalibrationFunction(rangingData(transmitter, receiver, time, 2),transmitter,receiver,polynomialDegree, size(rangingData,1)*(polynomialDegree+1)+size(rangingData,2)*(polynomialDegree+1));
                    A(row,:) = A(row,:)-polynomialCalibrationFunction(rangingData(receiver, transmitter, time, 2),receiver,transmitter,polynomialDegree, size(rangingData,1)*(polynomialDegree+1)+size(rangingData,2)*(polynomialDegree+1));
                    B(row) = 0; 
                end
            end
        end
    end  
end   

% for transmitter = 1:size(rangingData,1)
%     for receiver = 1:size(rangingData,2)
%         if any(rangingData(transmitter, receiver, :, 2)~=0 & (rangingData(transmitter, receiver, :, 1)>=0 | (rangingData(receiver, transmitter, :, 2)~=0)))
%             row=row+1;
%             A(row,(transmitter-1)*4+1) = 1/32;
%             A(row,(receiver-1)*4+3) = 1/32;
%             B(row) = 1/32; 
%         end
%     end  
% end   


for i=1:size(rangingData,1)*(polynomialDegree+1)
    row=row+1;
    A(row,i*2-1) = 1;
    B(row) = .5;
end
    
%find the calibration coefficients
calibrationCoeffs=lsqr(A,B',1e-9,200,[],[],repmat([.5; 0],2*size(rangingData,1),1));
%calibrationCoeffs=lsqr(A,B',1e-9,200,[],[],[]);            