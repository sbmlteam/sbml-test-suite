(* 

category:      Test
synopsis:      Two reactions with four species in one
compartment using an assignmentRule to vary one species.
componentTags: StoichiometryMath, Compartment, Species, Reaction, Parameter, AssignmentRule 
testTags:      Amount, AssignedConstantStoichiometry, NonUnityStoichiometry, InitialValueReassigned
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 2.5
generatedBy:   Numeric

Note:  earlier versions of the test suite contained a 3.1 version of this test.
That model was moved to its own test, because it did not have the 'StoichiometryMath'
component.

The model contains one compartment called C.  There are four
species called S1, S2, S3 and S and four parameters called k1, k2, k3 and p1.
The model contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + (4 * p1)S2 -> S3 | $k1 * S1 * S2 * C$  |
| S3 -> S1 + S2 | $k2 * S3 * C$  |]

The model contains one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | S4 | $3 * S2$  |]

Note that in this case the initial value of species S4 has not been declared and must be 
calculated using the assignmentRule.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                |$    1 \x 10^-5 |mole                      |
|Initial amount of S2                |$ 1.5 \x 10^-5$ |mole                      |
|Initial amount of S3                |$   1 \x 10^-5$ |mole                      |
|Initial amount of S4                |$   undeclared$ |mole                      |
|Value of parameter k1               |$  1.5 \x 10^4$ |litre mole^-1^ second^-1^ |
|Value of parameter k2               |$            5$ |second^-1^ |
|Value of parameter k3               |$          1.5$ |dimensionless |
|Value of parameter p1               |$          0.5$ |dimensionless |
|Volume of compartment C             |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00610" ];

addCompartment[ C, size -> 1];
addSpecies[ S1, initialAmount->1 10^-5 ];
addSpecies[ S2, initialAmount -> 1.5 10^-5];
addSpecies[ S3, initialAmount -> 1 10^-5];
addSpecies[ S4];
addParameter[ k1, value -> 1.5 10^4];
addParameter[ k2, value -> 5 ];
addParameter[ k3, value -> 1.5 ];
addParameter[ p1, value -> 0.5 ];
addRule[ type->AssignmentRule, variable -> S4, math ->k3 * S2];
addReaction[reactants->{S1, S2}, products->{S3}, reactantStoichiometry->{1, 4 * p1}, 
       reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * C ];
addReaction[ S3 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * C ];

makemodel[]
