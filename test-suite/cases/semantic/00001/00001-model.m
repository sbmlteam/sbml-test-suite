(*

category: Basic reactions/Irreversible
synopsis: Basic single forward reaction with two species in one compartment
tags:     InitialAmount
testtype: TimeCourse

This model involves one compartment named compartment, two species named
S1 and S2, and one reaction $S1 -> S2$, with the biochemical rate of
the reaction being $k1 * [S1]$.  The species initial amount values are
given as amounts of substance to make it easier to use the model in a
discrete stochastic simulator, but (as per usual SBML principles) the
species' symbols must be treated as representing a concentration where they
appear in expressions because their hasOnlySubstanceUnits attribute is not
set to true.  The species amounts are $1.5 * 10^{-15}$ for S1 and 0 for S2.
The units are the SBML defaults (mole for species, litre for volume) and
the unit of k1 is assumed to be second.

*)

newcase[ "00001" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 1 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1*S1*compartment ];

makemodel[]
