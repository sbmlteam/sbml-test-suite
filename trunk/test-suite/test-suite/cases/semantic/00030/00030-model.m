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

[{width:30em,left-margin:5em}| *Type* | *Variable* | *Formula* |
| assignmentRule | S1 | $3+4$  |]

In the case there is no value attributed to the species by the model definition
and thus the value must be calculated by the rule.

The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $   undeclared$ | mole                      |
| Volume of compartment compartment:| $            1$ | litre                     |]


*)

newcase[ "00030" ];

addCompartment[ compartment, size -> 1.0];
addSpecies[ S1 ];
addRule[ type -> assignmentRule, variable -> S1, math -> 3+4];

makemodel[]
