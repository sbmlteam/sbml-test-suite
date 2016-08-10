(* 

category:      Test
synopsis:      Reactions occurring between two compartments. 
componentTags: Compartment, Species, Reaction, Parameter, AssignmentRule 
testTags:      Amount, MultiCompartment, InitialValueReassigned, NonUnityStoichiometry
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1
generatedBy:   Numeric

The model contains two compartments named compartment and compartment1.
There are five species named S1, S2, S3, S4 and S5 and four parameters
named k1, k2, k3 and k4.  Compartment "compartment" contains species S1 and
S2.  Compartment "compartment"1 contains species S3, S4 and S5.  The model
contains three reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S2  +  S2 | $k1 * S1 * S2 * compartment$  |
| S2 -> S3         | $k2 * (S2-S3) * compartment1$  |
| S3 + S4 -> S4  +  S4 | $k3 * S3 * S4 * compartment$  |]

The first reaction occurs entirely within compartment, the second reaction
occurs between a species in compartment and a species in compartment1 and the
third reaction occurs entirely within compartment1.

The model contains one rule which assigns value to species S5:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | S5 | $k4 * S2$  |]

In this case there is no initial value declared for species S3 and thus it
must be calculated by the assignmentRule.  Note that since this
assignmentRule must always remain true, it should be considered during
simulation.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                 |$          1.0$ |mole                      |
|Initial amount of S2                 |$          1.0$ |mole                      |
|Initial amount of S3                 |$            0$ |mole                      |
|Initial amount of S4                 |$          0.1$ |mole                      |
|Initial amount of S5                 |$    ndeclared$ |mole                      |
|Value of parameter k1                |$         0.75$ |litre mole^-1^ second^-1^ |
|Value of parameter k2                |$          7.5$ |second^-1^ |
|Value of parameter k3                |$         0.75$ |litre mole^-1^ second^-1^ |
|Value of parameter k4                |$          0.5$ |dimensionless |
|Volume of compartment "compartment"  |$            1$ |litre                     |
|Volume of compartment "compartment"1 |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00292" ];

addCompartment[ compartment, size -> 1 ];
addCompartment[ compartment1, size -> 1 ];
addSpecies[ S1, compartment->compartment, initialAmount -> 1.0];
addSpecies[ S2, compartment->compartment, initialAmount -> 1.0];
addSpecies[ S3, compartment->compartment1, initialAmount -> 0];
addSpecies[ S4, compartment->compartment1, initialAmount -> 0.1];
addSpecies[ S5, compartment->compartment1];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 7.5 ];
addParameter[ k3, value -> 0.75 ];
addParameter[ k4, value -> 0.5 ];
addRule[ type->AssignmentRule, variable -> S5, math ->k4 * S2];
addReaction[ S1 + S2 -> S2  +  S2, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * compartment ];
addReaction[ S2 -> S3, reversible -> False,
	     kineticLaw -> k2 * (S2-S3) * compartment1 ];
addReaction[ S3 + S4 -> S4  +  S4, reversible -> False,
	     kineticLaw -> k3 * S3 * S4 * compartment ];

makemodel[]
