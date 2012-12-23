(* 

category:      Test
synopsis:      Basic reaction with two species in a compartment whose 
               volume is varying.
componentTags: Compartment, Species, RateRule, Reaction, Parameter, AssignmentRule 
testTags:      Amount, NonConstantCompartment, NonUnityCompartment, NonConstantParameter, InitialValueReassigned
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one varying compartment called C.  There are three
species called S1, S2 and S3 and four parameters called k1, k2, p1 and p2.  The
model contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 | $k1 * S1 * S2 * C$  |
| S3 -> S1 + S2 | $k2 * S3 * C$  |]

The model contains two rules. The assignmentRule assigns value to C
and the rateRule determines the rate at which p2 is varying:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment |  C  | $p1 * p2$  |
 | Rate       | p2  | $0.1       |]

In this case, the initial value declared for compartment C is
inconsistent with that calculated by the assignmentRule; the calculated
value should be used.  Note that since this assignmentRule must always
remain true, it should be considered during simulation.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                |$          1.0$ |mole                      |
|Initial amount of S2                |$          2.0$ |mole                      |
|Initial amount of S3                |$          1.0$ |mole                      |
|Value of parameter k1               |$         0.75$ |litre mole^-1^ second^-1^ |
|Value of parameter k2               |$          2.5$ |second^-1^ |
|Value of parameter p1               |$          0.1$ |litre mole^-1^ |
|Value of parameter p2               |$          1.0$ |mole second^-1^ |
|Volume of compartment C |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00313" ];

addCompartment[ C, constant->False, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0];
addSpecies[ S2, initialAmount -> 2.0];
addSpecies[ S3, initialAmount -> 1.0];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 2.5 ];
addParameter[ p1, value -> 0.1 ];
addParameter[ p2, value -> 1.0, constant -> False ];
addRule[ type->AssignmentRule, variable -> C, math -> p1 * p2];
addRule[ type->RateRule, variable -> p2, math -> 0.1];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * C ];
addReaction[ S3 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * C ];

makemodel[]
