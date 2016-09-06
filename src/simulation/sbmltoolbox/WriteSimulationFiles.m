function WriteSimulationFiles(varargin)
% WriteSimulationFiles writes the files needed by the ODE solvers
% 
% takes    1) a string representing the top level test-suite directory
%             (NOTE: assumes that there are directories cases/semantic/XXXXX)
%          2) the number of the case for which to write files 
%             or when a range has been specified the first case 
%          3) optionally a second number which is the end of the range 
%          
%     e.g. WriteSimulationFiles(2, 6) will write files for cases 2, 3, 4, 5 & 6
% The output file will be found in the folder simulationFiles. 
%     
    
% check args
if (nargin < 2)
  error('WriteSimulationFiles(dir, num, (num)) requires at least 2 arguments');
elseif (nargin > 3)
  error('Too many input arguments');
end;

if (~ischar(varargin{1}))
  error('First argument must be a string representing the toplevel test suite directory');
end;

if (~isnumeric(varargin{2}))
  error('Second argument must be a number representing a test case');
end;

if (nargin == 3 && ~isnumeric(varargin{3}))
  error('Third argument must be a number representing a test case');
end;
  
toplevel = varargin{1};
num1 = varargin{2};

if (nargin == 3)
  num2 = varargin{3};
else
  num2 = num1;
end;

if (num2 < num1)
  error('Second number in the range must be greater than the first');
end;



files = dir(strcat(toplevel, '/cases/semantic/00*'));


if (~isdir('simulationFiles'))
  mkdir('simulationFiles');
end;
cd ('simulationFiles');
%to write
for i=num1:num2
  num = files(i).name(1:5);
  sbml = strcat(toplevel,'/cases/semantic/', num, '/', num, '-sbml-l2v3.xml');
  m = TranslateSBML(sbml);
  try
    WriteODEFunction(m);
    disp(sprintf('%s ODE file written', num));
  catch ME
    disp(sprintf('%s failed to write', num));
  end;
end;
cd ..;
