(*

category:      Test
synopsis:      Basic single forward reaction with two species in one
               compartment using initialAssignment to set the compartment size.
componentTags: Compartment, Species, Reaction, InitialAssignment
testTags:      InitialAmount, NonUnityCompartment, LocalParameters
testtype:      TimeCourse
levels:        2.2, 2.3

This model involves one compartment named compartment, two species named S1 
and S2, and one reaction $S1 -> S2$, with the biochemical rate of the reaction 
being $k * S1$.  The compartment has non-unity size which is determined
using an initialAssignment.  In this case the compartment size is explicitly
declared and consistent with the value returned by the initialAssignment.  
The species amounts are $1.5 * 10^{-15}$ for S1 and 0 for S2.  The parameter 
k used by the reaction is defined locally.  The units are the SBML defaults 
(mole for species, litre for volume) and the unit of the k is assumed to be 
second^-1^.

*)

newcase[ "00027" ];

addCompartment[ compartment, size -> 0.534];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 0.0 ];
addInitialAssignment[ compartment, math -> 534*0.001];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> compartment*k*S1, parameters -> {k -> 100} ];

makemodel[]
