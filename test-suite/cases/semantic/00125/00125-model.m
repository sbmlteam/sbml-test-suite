(*

category:      Test
synopsis:      Basic single reaction using functionDefinitions involving species
               with a non-unity stoichiometry.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition 
testTags:      InitialAmount, NonUnityStoichiometry
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are two species named S1 and S2 and one parameter named k1.
  The model contains one reaction defined as:
[| | Reaction |||||| Rate |
 | | S1 -> 2 S2 |||||| $multiply(k1,S1)*compartment$  |]


The model contains one functionDefinition defined as:
[|| Id      | Arguments || Formula |
 || multiply | x, y || $x*y$ |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $1.5 \x 10^-15$ || mole                      |
|              Initial amount of S2:|| $            0$ || mole                      |
|             Value of parameter k1:|| $            1$ || second^-1^ |
| Volume of compartment compartment:|| $            1$ || litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00125" ];

addFunction[ multiply, arguments -> {x, y}, math -> x*y];
addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 1 ];
addReaction[ S1 -> 2 S2, reversible -> False,
	     kineticLaw -> multiply[k1,S1]*compartment ];

makemodel[]
