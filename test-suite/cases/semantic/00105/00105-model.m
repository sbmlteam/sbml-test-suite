(*

category:      Test
synopsis:      Basic two reactions with three species in one varying compartment 
               and one functionDefinition
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition, RateRule 
testTags:      InitialAmount, NonConstantCompartment
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are three species named S1, S2 and S3 and three parameters named k1, k2 and p1.
  The model contains two reactions defined as:
[| | Reaction |||||| Rate |
 | | S1+S2 -> S3 |||||| $multiply(k1, S1, S2)*compartment$  |
 | | S3 -> S1+S2 |||||| $k2*S3*compartment$  |]

  The model contains one rule:
[|| Type || Variable || Formula |
 || Rate || compartment || $-p1*compartment$  |]


The model contains one functionDefinition defined as:
[|| Id      | Arguments || Formula |
 || multiply | x, y, z || $x*y*z$ |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $1.0 \x 10^-15$ || mole                      |
|              Initial amount of S2:|| $2.0 \x 10^-15$ || mole                      |
|              Initial amount of S3:|| $1.0 \x 10^-15$ || mole                      |
|             Value of parameter k1:|| $         0.75$ || litre mole^-1^ second^-1^ |
|             Value of parameter k2:|| $         0.25$ || second^-1^ |
|             Value of parameter p1:|| $0.1 \x 10^-15$ || second^-1^ |
| Volume of compartment compartment:|| $            1$ || litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00105" ];

addFunction[ multiply, arguments -> {x, y, z}, math -> x*y*z];
addCompartment[ compartment, constant -> False, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0 10^-15];
addSpecies[ S2, initialAmount -> 2.0 10^-15];
addSpecies[ S3, initialAmount -> 1.0 10^-15];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addParameter[ p1, value -> 0.1 10^-15];
addRule[ type->RateRule, variable -> compartment, math -> -p1*compartment];
addReaction[ S1+S2 -> S3, reversible -> False,
	     kineticLaw -> multiply[k1, S1, S2]*compartment ];
addReaction[ S3 -> S1+S2, reversible -> False,
	     kineticLaw -> k2*S3*compartment ];

makemodel[]
