(* 

category:      Test
synopsis:      Two reactions with three species in one
compartment using an assignmentRule to assign value to a parameter.
componentTags: Compartment, Species, Reaction, Parameter, AssignmentRule 
testTags:      Amount, InitialValueReassigned
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called "compartment".  There are three
species named S1, S2 and S3 and two parameters named k1 and k2.  The model
contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 | $k1 * S1 * S2 * compartment$  |
| S3 -> S1 + S2 | $k2 * S3 * compartment$  |]

The model contains one rule which assigns value to parameter k2:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | k2 | $5$  |]

In this case the initial value declared for parameter k2 is inconsistent
with the value calculated by the assignmentRule.  The calculated value
should be used.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                |$   1 \x 10^-2$ |mole                      |
|Initial amount of S2                |$ 1.5 \x 10^-2$ |mole                      |
|Initial amount of S3                |$1.25 \x 10^-2$ |mole                      |
|Value of parameter k1               |$  1.5 \x 10^2$ |litre mole^-1^ second^-1^ |
|Value of parameter k2               |$        0.005$ |second^-1^ |
|Volume of compartment "compartment" |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00146" ];

addCompartment[ compartment, size -> 1];
addSpecies[ S1, initialAmount-> 1 10^-2 ];
addSpecies[ S2, initialAmount -> 1.5 10^-2];
addSpecies[ S3, initialAmount -> 1.25 10^-2];
addParameter[ k1, value -> 1.5 10^2];
addParameter[ k2, value -> 0.005, constant -> False ];
addRule[ type->AssignmentRule, variable -> k2, math -> 5];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * compartment ];
addReaction[ S3 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * compartment ];

makemodel[]
