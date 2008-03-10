(*

category:      Test
synopsis:      Linear chain of reactions in one compartment.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      InitialAmount
testtype:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are four species named S1, S2, S3 and S4 and three parameters named k1, k2 and k3.
  The model contains three reactions defined as:
[| | Reaction |||||| Rate                 |
 | | S1 -> S2 |||||| $k1*S1*compartment$  |
 | | S2 -> S3 |||||| $k2*S2*compartment$  |
 | | S3 -> S4 |||||| $k3*S3*compartment$  |] 

The initial conditions are as follows:
[|                                  || Value            || Units          |
|              Initial amount of S1:|| $1.0 \x 10^-15$  || mole           |
|              Initial amount of S2:|| $0$              || mole           |
|              Initial amount of S3:|| $0$              || mole           |
|              Initial amount of S4:|| $0$              || mole           |
|             Value of parameter k1:|| $0.7$            || second^-1^     |
|             Value of parameter k2:|| $0.5$            || second^-1^     |
|             Value of parameter k3:|| $1$              || second^-1^     |
| Volume of compartment compartment:|| $1$              || litre          |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00020" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0 10^-15];
addSpecies[ S2, initialAmount -> 0];
addSpecies[ S3, initialAmount -> 0];
addSpecies[ S4, initialAmount -> 0];
addParameter[ k1, value -> 0.7 ];
addParameter[ k2, value -> 0.5 ];
addParameter[ k3, value -> 1 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1*S1*compartment ];
addReaction[ S2 -> S3, reversible -> False,
	     kineticLaw -> k2*S2*compartment ];
addReaction[ S3 -> S4, reversible -> False,
	     kineticLaw -> k3*S3*compartment ];

makemodel[]