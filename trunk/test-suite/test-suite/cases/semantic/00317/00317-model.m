(*

category:      Test
synopsis:      Basic two reactions with four species in a compartment whose 
               volume is varying.
componentTags: Compartment, Species, Reaction, Parameter, AssignmentRule 
testTags:      InitialAmount, NonConstantCompartment
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are four species named S1, S2, S3 and S4 and three parameters named k1, k2 and p1.
  The model contains two reactions defined as:

[{width:30em,left-margin:5em}| | *Reaction* | *Rate* |
| S1+S2 -> S3+S4 | $k1*S1*S2*compartment$  |
| S3+S4 -> S1+S2 | $k2*S3*S4*compartment$  |]

  The model contains one rule which assigns value to compartment:

[{width:30em,left-margin:5em}| *Type* | *Variable* | *Formula* |
 | Assignment | compartment | $p1*S1$  |]
In this case the initial value declared for compartment is consistent with that calculated
by the assignmentRule.  Note that since this assignmentRule 
must always remain true, it should be considered during
simulation.


The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $          1.0$ | mole                      |
|              Initial amount of S2:| $          1.5$ | mole                      |
|              Initial amount of S3:| $          1.1$ | mole                      |
|              Initial amount of S4:| $          1.0$ | mole                      |
|             Value of parameter k1:| $          7.5$ | litre mole^-1^ second^-1^ |
|             Value of parameter k2:| $          2.5$ | litre mole^-1^ second^-1^ |
|             Value of parameter p1:| $          0.1$ | litre^2^ mole^-1^ |
| Volume of compartment compartment:| $           0.1$ | litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00317" ];

addCompartment[ compartment, constant->False, size -> 0.1 ];
addSpecies[ S1, initialAmount -> 1.0];
addSpecies[ S2, initialAmount -> 1.5];
addSpecies[ S3, initialAmount -> 1.1];
addSpecies[ S4, initialAmount -> 1.0];
addParameter[ k1, value -> 7.5 ];
addParameter[ k2, value -> 2.5 ];
addParameter[ p1, value -> 0.1 ];
addRule[ type->AssignmentRule, variable -> compartment, math -> p1*S1];
addReaction[ S1+S2 -> S3+S4, reversible -> False,
	     kineticLaw -> k1*S1*S2*compartment ];
addReaction[ S3+S4 -> S1+S2, reversible -> False,
	     kineticLaw -> k2*S3*S4*compartment ];

makemodel[]
