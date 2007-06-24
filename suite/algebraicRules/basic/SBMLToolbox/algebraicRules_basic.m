function xdot = algebraicRules_basic(time, x_values)
% function algebraicRules_basic takes
%
% either	1) no arguments
%       	    and returns a vector of the initial species concentrations
%
% or    	2) time - the elapsed time since the beginning of the reactions
%       	   x_values    - vector of the current concentrations of the species
%       	    and returns a vector of the rate of change of concentration of each of the species
%
% algebraicRules_basic can be used with matlabs odeN functions as 
%
%	[t,x] = ode23(@algebraicRules_basic, [0, t_end], algebraicRules_basic)
%
%			where  t_end is the end time of the simulation
%
%The species in this model are related to the output vectors with the following indices
%	Index	Species name
%	  1  	  x
%	  2  	  y
%	  3  	  z
%
%--------------------------------------------------------
% output vector

xdot = zeros(3, 1);

%--------------------------------------------------------
% parameter values


%--------------------------------------------------------
% initial species values - these may be overridden by assignment rules

if (nargin == 0)

	% initial concentrations
	x = 5.000000e-001;
	y = 5.000000e-001;
	z = 1;

else
	% floating species concentrations
	x = x_values(1);
	y = x_values(2);
	z = x_values(3);

end;

%--------------------------------------------------------
% assignment rules

%--------------------------------------------------------
% algebraic rules
y = -x+z;

%--------------------------------------------------------
% calculate concentration values

if (nargin == 0)

	% initial concentrations
	xdot(1) = 5.000000e-001;
	xdot(2) = 5.000000e-001;
	xdot(3) = 1;

else

	% species concentration rate equations
	xdot(1) = y*z;
	xdot(3) = 0;
	xdot(2) = -xdot(1)+xdot(3);

end;
