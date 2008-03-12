(*

category:      Test
synopsis:      Basic reaction with four species in a compartment 
               where the one species is constant.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      InitialAmount, ConstantSpecies
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are four species named S1, S2, S3 and S4 and two parameters named k1 and k2.
  The model contains two reactions defined as:
[| | Reaction    |||||| Rate |
 | | S1+S2 -> S3 |||||| $k1*S1*S2*compartment$  |
 | | S3 -> S1+S2 |||||| $k2*S3*S4*compartment$  |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $          1.0$ || mole                      |
|              Initial amount of S2:|| $          2.0$ || mole                      |
|              Initial amount of S3:|| $          1.0$ || mole                      |
|              Initial amount of S4:|| $          1.5$ || mole                      |
|             Value of parameter k1:|| $          1.7$ || litre mole^-1^ second^-1^ |
|             Value of parameter k2:|| $          0.3$ || litre mole^-1^ second^-1^ |
| Volume of compartment compartment:|| $            1$ || litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00064" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0 ];
addSpecies[ S2, initialAmount -> 2.0 ];
addSpecies[ S3, initialAmount -> 1.0 ];
addSpecies[ S4, initialAmount -> 1.5 , constant->True];
addParameter[ k1, value -> 1.7  ];
addParameter[ k2, value -> 0.3  ];
addReaction[ S1+S2 -> S3, reversible -> False,
	     kineticLaw -> k1*S1*S2*compartment ];
addReaction[ S3 -> S1+S2, reversible -> False,
	     kineticLaw -> k2*S3*S4*compartment ];

makemodel[]