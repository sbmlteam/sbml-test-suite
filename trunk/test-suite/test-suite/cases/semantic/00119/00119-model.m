(*

category:      Test
synopsis:      Basic single forward reaction using functionDefinitions
               with three species in a compartment where the one species is constant.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition
testTags:      InitialAmount, ConstantSpecies
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are three species named S1, S2 and S3 and one parameter named k1.
  Species S3 is labeled as constant and therefore does not vary.  
  The model contains one reaction defined as:

[{width:30em,left-margin:5em}| *Reaction* | *Rate* |
| S1 -> S2 | $k1*multiply(S1,S3)*compartment$  |]


The model contains one functionDefinition defined as:

[{width:30em,left-margin:5em}| *Id* | *Arguments* | *Formula* |
 | multiply | x, y | $x*y$ |]


The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $          1.5$ | mole                      |
|              Initial amount of S2:| $            0$ | mole                      |
|              Initial amount of S3:| $            2$ | mole                      |
|             Value of parameter k1:| $          1.5$ | litre mole^-1^ second^-1^ |
| Volume of compartment compartment:| $            1$ | litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00119" ];

addFunction[ multiply, arguments -> {x, y}, math -> x*y];
addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 ];
addSpecies[ S2, initialAmount -> 0 ];
addSpecies[ S3, initialAmount -> 2, constant->True ];
addParameter[ k1, value -> 1.5 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1*multiply[S1,S3]*compartment ];

makemodel[]
