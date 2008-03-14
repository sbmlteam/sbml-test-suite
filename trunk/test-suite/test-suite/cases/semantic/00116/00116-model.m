(*

category:      Test
synopsis:      Basic two reactions using functionDefinitions in one compartment,
               with one species acting as a boundary condition.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition 
testTags:      InitialAmount, BoundaryCondition
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are two species named S1 and S2 and two parameters named k1 and k2.
  Species S1 is labeled as an SBML boundary species.
  The model contains two reactions defined as:
[| | Reaction |||||| Rate |
 | | S1 -> S2 |||||| $multiply(k1,S1)*compartment$  |
 | | S2 -> S1 |||||| $k2*S2*compartment$  |]


The model contains one functionDefinition defined as:
[|| Id      | Arguments || Formula |
 || multiply | x, y || $x*y$ |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $1.5 \x 10^-15$ || mole                      |
|              Initial amount of S2:|| $1.5 \x 10^-15$ || mole                      |
|             Value of parameter k1:|| $          0.5$ || second^-1^ |
|             Value of parameter k2:|| $         0.25$ || second^-1^ |
| Volume of compartment compartment:|| $            1$ || litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00116" ];

addFunction[ multiply, arguments -> {x, y}, math -> x*y];
addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15, boundaryCondition -> True];
addSpecies[ S2, initialAmount -> 1.5 10^-15 ];
addParameter[ k1, value -> 0.5 ];
addParameter[ k2, value -> 0.25 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> multiply[k1,S1]*compartment ];
addReaction[ S2 -> S1, reversible -> False,
	     kineticLaw -> k2*S2*compartment ];

makemodel[]
