(*

category: Basic reactions/Irreversible
synopsis: Basic single reaction involving two species and a non-unity
 stoichiometry.
tags:     NonUnityStoichiometry, InitialAmount
testtype: TimeCourse

This is a basic model involving one compartment named compartment, 
two species named S1 and S2, and one reaction.  The reaction is 
$S1 -> 2 * S2$, with the biochemical rate of the reaction
being $k1 * [S1]$.  This differs from 0001 in that the stoichiometry of 
S2 is 2 in the reaction, but otherwise is identical to that case.

*)

newcase[ "00003" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 1 ];
addReaction[ S1 -> 2 S2, reversible -> False,
	     kineticLaw -> k1*S1*compartment ];

makemodel[]
