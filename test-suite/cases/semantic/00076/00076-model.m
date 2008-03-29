(*

category:      Test
synopsis:      Basic reaction with two species in a compartment 
               of non-unity volume. 
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      InitialAmount, NonUnityCompartment
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are three species named S1, S2 and S3 and two parameters named k1 and k2.
  The model contains two reactions defined as:

[{width:30em,left-margin:5em}| *Reaction* | *Rate* |
| S1+S2 -> S3 | $k1*S1*S2*compartment$  |
| S3 -> S1+S2 | $k2*S3*compartment$  |]


The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $1.0 \x 10^-12$ | mole                      |
|              Initial amount of S2:| $2.0 \x 10^-12$ | mole                      |
|              Initial amount of S3:| $1.5 \x 10^-12$ | mole                      |
|             Value of parameter k1:| $0.75 \x 10^12$ | litre mole^-1^ second^-1^ |
|             Value of parameter k2:| $0.25 \x 10^-6$ | second^-1^ |
| Volume of compartment compartment:| $          0.7$ | litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00076" ];

addCompartment[ compartment, size -> 0.7 ];
addSpecies[ S1, initialAmount -> 1.0 10^-12];
addSpecies[ S2, initialAmount -> 2.0 10^-12];
addSpecies[ S3, initialAmount -> 1.5 10^-12];
addParameter[ k1, value -> .75 10^12];
addParameter[ k2, value -> .25 10^-6];
addReaction[ S1+S2 -> S3, reversible -> False,
	     kineticLaw -> k1*S1*S2*compartment ];
addReaction[ S3 -> S1+S2, reversible -> False,
	     kineticLaw -> k2*S3*compartment ];

makemodel[]
