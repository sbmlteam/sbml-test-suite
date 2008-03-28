(*

category:      Test
synopsis:      Basic single forward reaction using functionDefinitions with two 
               species in a compartment where the species have only substance units.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition 
testTags:      InitialAmount, HasOnlySubstanceUnits
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are two species named S1 and S2 and one parameter named k1.
  The model contains one reaction defined as:
[| | Reaction |||||| Rate |
 | | S1 -> S2 |||||| $multiply(k1,S1)$  |]


The model contains one functionDefinition defined as:
[|| Id      | Arguments || Formula |
 || multiply | x, y || $x*y$ |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $1.5 \x 10^-12$ || mole                      |
|              Initial amount of S2:|| $            0$ || mole                      |
|             Value of parameter k1:|| $          1.5$ || second^-1^ |
| Volume of compartment compartment:|| $            1$ || litre                     |]

The species have been declared as having substance units only. Thus they 
must be treated as amounts where they appear in expressions.

*)

newcase[ "00113" ];

addFunction[ multiply, arguments -> {x, y}, math -> x*y];
addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-12, hasOnlySubstanceUnits->True ];
addSpecies[ S2, initialAmount -> 0, hasOnlySubstanceUnits->True ];
addParameter[ k1, value -> 1.5 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> multiply[k1,S1] ];

makemodel[]
