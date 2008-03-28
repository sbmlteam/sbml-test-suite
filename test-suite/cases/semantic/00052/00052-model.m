(*

category:      Test
synopsis:      Basic reaction with two species in a compartment whose 
               volume is varying.
componentTags: Compartment, Species, Reaction, Parameter, RateRule 
testTags:      InitialAmount, NonConstantCompartment
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are three species named S1, S2 and S3 and three parameters named k1, k2 and p1.
  The model contains two reactions defined as:

[{width:30em,left-margin:5em}| | *Reaction* | *Rate* |
| S1+S2 -> S3 | $k1*S1*S2*compartment$  |
| S3 -> S1+S2 | $k2*S3*compartment$  |]

  The model contains one rule which defines the rate at which the volume of the 
compartment is changing:

[{width:30em,left-margin:5em}| *Type* | *Variable* | *Formula* |
 | Rate | compartment | $-p1*compartment$  |]


The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $          1.0$ | mole                      |
|              Initial amount of S2:| $          2.0$ | mole                      |
|              Initial amount of S3:| $          1.0$ | mole                      |
|             Value of parameter k1:| $         0.75$ | litre mole^-1^ second^-1^ |
|             Value of parameter k2:| $         0.25$ | second^-1^ |
|             Value of parameter p1:| $          0.1$ | second^-1^ |
| Volume of compartment compartment:| $            1$ | litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00052" ];

addCompartment[ compartment, constant->False, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0];
addSpecies[ S2, initialAmount -> 2.0];
addSpecies[ S3, initialAmount -> 1.0];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addParameter[ p1, value -> 0.1 ];
addRule[ type->RateRule, variable -> compartment, math -> -p1*compartment];
addReaction[ S1+S2 -> S3, reversible -> False,
	     kineticLaw -> k1*S1*S2*compartment ];
addReaction[ S3 -> S1+S2, reversible -> False,
	     kineticLaw -> k2*S3*compartment ];

makemodel[]
