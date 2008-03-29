(*

category:      Test
synopsis:      Basic single forward reaction with two species in a 1D
               compartment and nonzero initial amounts using a function to apply 
               the kinetic law.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition 
testTags:      InitialAmount, 1D_Compartment, NonUnityCompartment
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are two species named S1 and S2 and one parameter named k1.
  Compartment compartment is 1-dimensional.
  The model contains one reaction defined as:

[{width:30em,left-margin:5em}| *Reaction* | *Rate* |
| S1 -> S2 | $compartment*multiply(k1,S1)$  |]


The model contains one functionDefinition defined as:

[{width:30em,left-margin:5em}| *Id* | *Arguments* | *Formula* |
 | multiply | x, y | $x*y$ |]


The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $1.5 \x 10^-15$ | mole                      |
|              Initial amount of S2:| $1.5 \x 10^-15$ | mole                      |
|             Value of parameter k1:| $         2.63$ | second^-1^ |
| Length of compartment compartment:| $         0.07$ | metre                  |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00281" ];

addFunction[ multiply, arguments -> {x, y}, math -> x*y];
addCompartment[ compartment, spatialDimensions -> 1, size -> 0.07 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 1.5 10^-15 ];
addParameter[ k1, value -> 2.63 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> compartment*multiply[k1,S1] ];

makemodel[]
