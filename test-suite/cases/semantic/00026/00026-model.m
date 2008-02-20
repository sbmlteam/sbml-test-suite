(*

category:      Test
synopsis:      Single forward reaction with two species in one compartment 
               and one event that assigns value to a species.
componentTags: Compartment, Species, Reaction, Parameter, EventNoDelay
testTags:      InitialAmount
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

This model involves one compartment named compartment, two species named S1 
and S2, one reaction $S1 -> S2$, with the biochemical rate of the reaction 
being $k1 * S1$.  The model also contains one event that triggers when 
$S1 < 0.1$; at which point the value of S1 is reset to 1.  The species 
amounts are 1.0 for S1 and 0 for S2.  The units are the SBML defaults (mole 
for species, litre for volume) and the unit of the parameter k1 is assumed 
to be second^-1^.

*)

newcase[ "00026" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0 ];
addSpecies[ S2, initialAmount -> 0.0 ];
addParameter[ k1, value -> 1 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> compartment*S1*k1 ];
addEvent[ trigger -> S1 < 0.1, eventAssignment -> S1->1];

makemodel[]
