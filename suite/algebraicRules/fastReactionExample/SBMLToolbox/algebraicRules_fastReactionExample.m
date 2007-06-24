function xdot = algebraicRules_fastReactionExample(time, x_values)
% function algebraicRules_fastReactionExample takes
%
% either	1) no arguments
%       	    and returns a vector of the initial species concentrations
%
% or    	2) time - the elapsed time since the beginning of the reactions
%       	   x_values    - vector of the current concentrations of the species
%       	    and returns a vector of the rate of change of concentration of each of the species
%
% algebraicRules_fastReactionExample can be used with matlabs odeN functions as 
%
%	[t,x] = ode23(@algebraicRules_fastReactionExample, [0, t_end], algebraicRules_fastReactionExample)
%
%			where  t_end is the end time of the simulation
%
%The species in this model are related to the output vectors with the following indices
%	Index	Species name
%	  1  	  X0
%	  2  	  X1
%	  3  	  T
%	  4  	  S1
%	  5  	  S2
%
%--------------------------------------------------------
% output vector

xdot = zeros(5, 1);

%--------------------------------------------------------
% parameter values

Keq = 2.500000e+000;
k1_in = 1.000000e-001;
k2_out = 1.500000e-001;

%--------------------------------------------------------
% initial species values - these may be overridden by assignment rules

if (nargin == 0)

	% initial concentrations
	X0 = 1;
	X1 = 0;
	T = 0;
	S1 = 0;
	S2 = 0;

else
	% floating species concentrations
	X0 = x_values(1);
	X1 = x_values(2);
	T = x_values(3);
	S1 = x_values(4);
	S2 = x_values(5);

end;

%--------------------------------------------------------
% assignment rules
S2 = Keq*S1;

%--------------------------------------------------------
% algebraic rules
S1 = (+T)/(Keq+1);

%--------------------------------------------------------
% calculate concentration values

if (nargin == 0)

	% initial concentrations
	xdot(1) = 1;
	xdot(2) = 0;
	xdot(3) = 0;
	xdot(4) = 0;
	xdot(5) = S2;

else

	% species concentration rate equations
	xdot(1) =  - (k1_in*X0);
	xdot(2) =  + (k2_out*S2);
	xdot(3) =  + (k1_in*X0) - (k2_out*S2);
	xdot(4) = (+xdot(3))/(Keq+1);
	xdot(5) = Keq*xdot(4);

end;
