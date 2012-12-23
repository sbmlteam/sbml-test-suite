(* 

category:      Test
synopsis:      Two reactions with four species in one
compartment using an assignmentRule to vary one species.
componentTags: Compartment, Species, Reaction, Parameter, AssignmentRule 
testTags:      Amount, ConstantSpecies
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called "compartment".  There are four
species named S1, S2, S3 and S4 and three parameters named k1, k2 and k3.
Species S5 is labeled as constant and therefore cannot be changed by rules
or reactions.  The model contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 | $k1 * S1 * S5 * compartment$  |
| S3 -> S1 + S2 | $k2 * S3 * compartment$  |]

The model contains one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | S4 | $k3 * S2$  |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                |$   1 \x 10^-5$ |mole                      |
|Initial amount of S2                |$ 1.5 \x 10^-5$ |mole                      |
|Initial amount of S3                |$   1 \x 10^-5$ |mole                      |
|Initial amount of S4                |$2.25 \x 10^-5$ |mole                      |
|Initial amount of S5                |$   1 \x 10^-5$ |mole                      |
|Value of parameter k1               |$ 0.5 \x 10^-2$ |litre mole^-1^ second^-1^ |
|Value of parameter k2               |$            5$ |second^-1^ |
|Value of parameter k3               |$          1.5$ |dimensionless |
|Volume of compartment "compartment" |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00308" ];

addCompartment[ compartment, size -> 1];
addSpecies[ S1, initialAmount->1 10^-5 ];
addSpecies[ S2, initialAmount -> 1.5 10^-5];
addSpecies[ S3, initialAmount -> 1 10^-5 ];
addSpecies[ S4, initialAmount -> 2.25 10^-5];
addSpecies[ S5, initialAmount -> 1 10^-5,constant -> True ];
addParameter[ k1, value -> 0.5 10^-2 ];
addParameter[ k2, value -> 5 ];
addParameter[ k3, value -> 1.5 ];
addRule[ type->AssignmentRule, variable -> S4, math ->k3 * S2];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> k1 * S1 * S5 * compartment ];
addReaction[ S3 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * compartment ];

makemodel[]
