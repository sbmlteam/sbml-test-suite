(*

category:      Test
synopsis:      Basic rule that assigns value to a species.
componentTags: Compartment, Species, AssignmentRule 
testTags:      InitialAmount
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There is one species named S1.
  The model does not contain any reactions.
  
  The model contains one rule:
[|| Type || Variable || Formula |
|| assignmentRule || S1 || $3+4$  |]

The rule assigns a value to species S1 which is consistent with the value
attributed to the species by the model definition.

The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $            7$ || mole                      |
| Volume of compartment compartment:|| $            1$ || litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00029" ];

addCompartment[ compartment, size -> 1.0];
addSpecies[ S1, initialAmount -> 7 ];
addRule[ type -> assignmentRule, variable -> S1, math -> 3+4];

makemodel[]
