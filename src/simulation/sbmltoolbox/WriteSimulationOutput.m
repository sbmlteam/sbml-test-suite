function WriteSimulationOutput(varargin)
% WriteSimulationOutput uses ODE solvers to perform simulation and output
% results
% 
% takes    1) a string representing the top level test-suite directory
%             (NOTE: assumes that there are directories cases/semantic/XXXXX)
%          2) the number of the case for which to write files 
%             or when a range has been specified the first case 
%          3) optionally a second number which is the end of the range 
%          
%     e.g. WriteSimulationOutput(2, 6) will write data for cases 2, 3, 4, 5 & 6
% The output file will be found in the folder simulationOutput. 
% 
% NOTE: the function reads the settings file to determine the time
    
% check args
if (nargin < 2)
  error('WriteSimulationOutput(dir, num, (num)) requires at least 2 arguments');
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
mydir = pwd;
if (~isdir('simulationOutput'))
  mkdir('simulationOutput');
end;
cd ('simulationFiles');
%to write
for i=num1:num2
  num = files(i).name(1:5);
  time = GetTime(num, toplevel);
  sbml = strcat(toplevel, '/cases/semantic/', num, '/', num, '-sbml-l2v3.xml');
  output = strcat(mydir, '/simulationFiles/case', num, '.csv');
  output1 = strcat(mydir, '/simulationOutput/case', num, '.csv');
  try
    m = TranslateSBML(sbml);
    OutputODEFunction(m, 0, time, 50, 1);
    copyfile(output, output1);
    disp(sprintf('%s output written', num));
  catch ME
    disp(sprintf('%s output failed', num));
  end;
end;
cd ..;


function t = GetTime(num, toplevel)

settings = strcat(toplevel, '/cases/semantic/', num, '/', num, '-settings.txt');

fileId = fopen(settings, 'r');
line = fgetl(fileId);
line = fgetl(fileId);
time = line(11:end);
t = str2num(time);
fclose(fileId);