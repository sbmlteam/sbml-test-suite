(*

category:      Test
synopsis:      Basic two reactions with two species in one compartment,
               with one reaction having a rate of zero.
componentTags: Compartment, Species, Reaction, Parameter
testTags:      InitialAmount, ZeroRate
testtype:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3

This should produce identical results to 0001.  This model involves one 
compartment named compartment, two species named S1 and S2, and two reactions.  
One reaction is $S1 ->S2$, with the biochemical rate of the reaction being 
$k1 * [S1]$.  The other reaction is $S2 -> S1$ with the biochemical rate of
the reaction being $k2 * [S2]$.  The value of k2 is zero, which means the 
reverse reaction should occur at zero rate (i.e. not at all).  The species 
amounts are $1.5 * 10^{-15}$ for S1 and 0 for S2.  The units are the
SBML defaults (mole for species, litre for volume) and the unit of k1 is 
assumed to be second^-1^.

*)

newcase[ "00002" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 1 ];
addParameter[ k2, value -> 0 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1*S1*compartment ];
addReaction[ S2 -> S1, reversible -> False,
	     kineticLaw -> k2*S2*compartment ];

makemodel[]
