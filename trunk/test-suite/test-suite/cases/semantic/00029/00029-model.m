(*

category: Basic rules
synopsis: Basic rule that assigns value to a species.
tags:     AssignmentRule, InitialAmount
testtype: TimeCourse

This model involves one compartment named compartment, one species named S1, 
and one assignmentRule that assigns a value to S1.  The species amount is 7 
which is consistent with the value assigned by the rule.  The units 
are the SBML defaults (mole for species, litre for volume).

*)

newcase[ "00029" ];

addCompartment[ compartment, size -> 1.0];
addSpecies[ S1, initialAmount -> 7 ];
addRule[ type -> assignmentRule, variable -> S1, math -> 3+4];

makemodel[]
