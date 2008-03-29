(*

category:      Test
synopsis:      Rate rule using a functionDefinition used to determine value of parameter
               which is used in a reaction.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition, RateRule 
testTags:      InitialAmount, NonConstantParameter
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are two species named S1 and S2 and three parameters named k1, k2 and k3.
  The model contains one reaction defined as:

[{width:30em,left-margin:5em}| | *Reaction* | *Rate* |
| S1 -> S2 | $k1*S1*compartment$  |]

  The model contains one rule that determines the value of parameter k1:

[{width:30em,left-margin:5em}| *Type* | *Variable* | *Formula* |
 | Rate | k1 | $add(k2, k3)$  |]


The model contains one functionDefinition defined as:

[{width:30em,left-margin:5em}| *Id* | *Arguments* | *Formula* |
 | add | x, y | $x+y$ |]


The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $1.5 \x 10^-15$ | mole                      |
|              Initial amount of S2:| $            0$ | mole                      |
|             Value of parameter k1:| $            1$ | second^-1^ |
|             Value of parameter k2:| $          0.2$ | second^-1^ |
|             Value of parameter k3:| $          0.3$ | lsecond^-1^ |
| Volume of compartment compartment:| $            1$ | litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00122" ];

addFunction[ add, arguments -> {x, y}, math -> x+y];
addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 1, constant->False ];
addParameter[ k2, value -> 0.2];
addParameter[ k3, value -> 0.3];
addRule[ type->RateRule, variable -> k1, math -> add[k2,k3]];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1*S1*compartment ];

makemodel[]