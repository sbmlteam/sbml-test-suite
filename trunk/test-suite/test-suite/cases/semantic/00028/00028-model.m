(*

category:      Test
synopsis:      Basic single forward reaction with two species in one
               compartment using a rate that causes a discontinuity in 
               the output.
componentTags: Compartment, Species, Reaction, Parameter
testTags:      InitialAmount, MathML, Discontinuity
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

This model involves one compartment named compartment, two species named S1 
and S2, and one reaction $S1 -> S2$. The biochemical rate of the reaction 
is a mathematical formula, $ceil(S1*p1)!/p2$, that causes a discontinuity 
within the simulation output.  The species amounts are 1.0 for S1 and 0 for 
S2.  The parameters p1 and p2 have values 4 and 25 respectively.  The units 
are the SBML defaults (mole for species, litre for volume), the units of p1 
are assumed to be litre per mole and those of p2 to be second per mole.

*)

newcase[ "00028" ];

addCompartment[ compartment, size -> 1.0];
addSpecies[ S1, initialAmount -> 1.0 ];
addSpecies[ S2, initialAmount -> 0.0 ];
addParameter[ p1, value -> 4];
addParameter[ p2, value -> 25];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> Ceiling[S1*p1]!/p2 ];

makemodel[]
