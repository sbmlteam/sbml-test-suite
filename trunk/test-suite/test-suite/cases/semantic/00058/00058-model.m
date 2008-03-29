(*

category:      Test
synopsis:      Basic two reactions with three species and parameters both global
               and local to reactions.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      InitialAmount, LocalParameters
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are three species named S1, S2 and S3 and one parameter named k.
  The model contains two reactions defined as:

[{width:30em,left-margin:5em}| *Reaction* | *Rate* |
| S1 -> S2 | $k*S1*compartmen$  |
| S2 -> S3 | $k*S2*compartment,$  |]

Reaction S2 -> S3 defines one local parameter k.

Note that the id of the local parameter k shadows the global parameter k.  Within
the defining reaction the value of the local parameter should be used.

The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $3.0 \x 10^-15$ | mole                      |
|              Initial amount of S2:| $            0$ | mole                      |
|              Initial amount of S3:| $            0$ | mole                      |
|              Value of parameter k:| $            1$ | second^-1^ |
|        Value of local parameter k:| $            2$ | second^-1^ |
| Volume of compartment compartment:| $            1$ | litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00058" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 3.0 10^-15];
addSpecies[ S2, initialAmount -> 0];
addSpecies[ S3, initialAmount -> 0];
addParameter[ k, value -> 1];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k*S1*compartment];
addReaction[ S2 -> S3, reversible -> False,
	     kineticLaw -> k*S2*compartment, parameters -> {k -> 2} ];

makemodel[]
