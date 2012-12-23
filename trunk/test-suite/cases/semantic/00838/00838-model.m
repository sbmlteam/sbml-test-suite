(* 

category:      Test
synopsis:      Single reversible reaction.
componentTags: Compartment, Species, Reaction, Parameter, AssignmentRule 
testTags:      Amount, InitialValueReassigned
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There are three species called 
S1, S2 and S3 and two parameters called kf and kr.  The model contains  one reaction
defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 <-> S2 + S3 | $(kf * S1 - kr * S2 * S3) * C$  |]

The model contains one rule which assigns value to parameter kf:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | kf | $2.5$  |]

In this case there is no initial value declared for parameter k1.  Thus the
value must be calculated using the assignmentRule.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value*       |*Units*  |
|Initial amount of S1        |$1.0$ |mole                      |
|Initial amount of S2        |$0.5$ |mole                      |
|Initial amount of S3        |$0$ |mole                      |
|Value of parameter kf       |$undeclared$          |second^-1^ |
|Value of parameter kr       |$0.2$          |litre mole^-1^ second^-1^ |
|Volume of compartment C     |$1$             |litre                  |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00838" ];

addCompartment[ C, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0];
addSpecies[ S2, initialAmount -> 0.5];
addSpecies[ S3, initialAmount -> 0.0];
addParameter[ kf, constant -> False ];
addParameter[ kr, value -> 0.2 ];
addRule[ type->AssignmentRule, variable -> kf, math -> 2.5];
addReaction[ S1 -> S2 + S3,
	     kineticLaw -> (kf * S1 - kr * S2 * S3) * C ];

makemodel[]
