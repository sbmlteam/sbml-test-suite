(*

category: Basic reactions/Irreversible
synopsis: Basic two reactions involving two species in one
 compartment, with non-unity stoichiometries and non-unity rate
 constants.
tags:     NonUnityStoichiometry, InitialAmount
testtype: TimeCourse

This case is similar to 0002.  This model has one compartment named compartment, 
two species named S1 and S2, and two reactions.  One reaction is $S1 -> S2$, 
with the biochemical rate of the reaction being $k1 * [S1]$.  The other 
reaction is $2 * S2 -> S1$ with the biochemical rate of the reaction being 
$k2 * [S2]^2$.  The values of the constants are $k1 = 0.35$ and $k2 = 180$.  
The species values are given as amounts of substance to make it easier to use 
the model in a discrete stochastic simulator, but (as per usual SBML principles)
they must be treated as concentrations where they appear in expressions.  The 
units are the SBML defaults (mole for species, litre for volume) and the 
units of k1 and k2 are assumed to be second^-1^.

*)

newcase[ "00004" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 0.35 ];
addParameter[ k2, value -> 180 ];
addReaction[ S1 -> 2 S2, reversible -> False,
	     kineticLaw -> k1*S1*compartment ];
addReaction[ 2 S2 -> S1, reversible -> False,
	     kineticLaw -> k2*S2*S2*compartment ];

makemodel[]
