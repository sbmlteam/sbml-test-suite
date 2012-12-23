(* 

category:      Test
synopsis:      Basic two reactions using functionDefinitions involving species in one
compartment, with non-unity stoichiometries.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition 
testTags:      Amount, NonUnityStoichiometry
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called "compartment".  There are two
species named S1 and S2 and two parameters named k1 and k2.  The model
contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> 2 S2 | $multiply(k1,S1) * compartment$  |
| 2 S2 -> S1 | $k2 * S2 * S2 * compartment$  |]


The model contains one functionDefinition defined as:

[{width:30em,margin: 1em auto}|  *Id*  |  *Arguments*  |  *Formula*  |
 | multiply | x, y | $x * y$ |]


The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*         |*Units*  |
|Initial amount of S1                |$1.5 \x 10^-3$ |mole                      |
|Initial amount of S2                |$            0$ |mole                      |
|Value of parameter k1               |$         0.35$ |second^-1^ |
|Value of parameter k2               |$          180$ |litre mole^-1^ second^-1^ |
|Volume of compartment "compartment" |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00126" ];

addFunction[ multiply, arguments -> {x, y}, math -> x * y];
addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-3 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 0.35 ];
addParameter[ k2, value -> 180 ];
addReaction[ S1 -> 2 S2, reversible -> False,
	     kineticLaw -> multiply[k1,S1] * compartment ];
addReaction[ 2 S2 -> S1, reversible -> False,
	     kineticLaw -> k2 * S2 * S2 * compartment ];

makemodel[]
