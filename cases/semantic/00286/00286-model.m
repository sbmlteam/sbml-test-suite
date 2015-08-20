(* 

category:      Test
synopsis:      Reactions occurring between two compartments. 
componentTags: Compartment, Species, Reaction, Parameter, AssignmentRule 
testTags:      Amount, MultiCompartment, NonUnityStoichiometry
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1
generatedBy:   Numeric

The model contains two compartments named compartment and compartment1.
There are four species named S1, S2, S3 and S4 and three parameters named
k1, k2 and k3.  Compartment "compartment" contains species S1 and S2.
Compartment "compartment"1 contains species S3 and S4.  The model contains
two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S2  +  S2 | $k1 * S1 * S2 * compartment$  |
| S2 -> S3         | $k2 * (S2-S3) * compartment1$  |]

The first reaction occurs entirely in compartment "compartment", whereas
the second reaction occurs between a species in "compartment" and a species
in "compartment1".

The model contains one rule which assigns value to species S4:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | S4 | $k3 * S3$  |]

In this case the initial value declared for species S4 is consistent with
that calculated by the assignmentRule.  Note that since this assignmentRule
must always remain true, it should be considered during simulation.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                 |$          1.0$ |mole                      |
|Initial amount of S2                 |$          1.0$ |mole                      |
|Initial amount of S3                 |$            0$ |mole                      |
|Initial amount of S4                 |$            0$ |mole                      |
|Value of parameter k1                |$         0.75$ |litre mole^-1^ second^-1^ |
|Value of parameter k2                |$         0.25$ |second^-1^ |
|Value of parameter k2                |$         0.25$ |dimensionless |
|Volume of compartment "compartment"  |$            1$ |litre                     |
|Volume of compartment "compartment"1 |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00286" ];

addCompartment[ compartment, size -> 1 ];
addCompartment[ compartment1, size -> 1 ];
addSpecies[ S1, compartment->compartment, initialAmount -> 1.0];
addSpecies[ S2, compartment->compartment, initialAmount -> 1.0];
addSpecies[ S3, compartment->compartment1, initialAmount -> 0];
addSpecies[ S4, compartment->compartment1, initialAmount -> 0];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addParameter[ k3, value -> 0.25 ];
addRule[ type->AssignmentRule, variable -> S4, math ->k3 * S3];
addReaction[ S1 + S2 -> S2  +  S2, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * compartment ];
addReaction[ S2 -> S3, reversible -> False,
	     kineticLaw -> k2 * (S2-S3) * compartment1 ];

makemodel[]
