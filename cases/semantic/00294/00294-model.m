(* 

category:      Test
synopsis:      Reactions occurring between two compartments. 
componentTags: Compartment, Species, Reaction, Parameter, AssignmentRule 
testTags:      Amount, MultiCompartment, InitialValueReassigned, ReversibleReaction
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1
generatedBy:   Numeric

The model contains two compartments named compartment and compartment1.
There are three species named S1, S3 and S5 and three parameters named k1,
k2 and k4.  Compartment "compartment" contains species S1.  Compartment
"compartment"1 contains species S3 and S5.  The model contains two
reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S3 | $k1 * S1 * compartment$  |
| S3 -> S1 | $k2 * (S3-S1) * compartment1$  |]

Both reactions occur between species within different compartments.

The model contains one rule which assigns value to species S5:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | S5 | $k4 * S3$  |]

In this case there is no initial value declared for species S3 and thus it
must be calculated by the assignmentRule.  Note that since this
assignmentRule must always remain true, it should be considered during
simulation.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                 |$          1.0$ |mole                      |
|Initial amount of S3                 |$            0$ |mole                      |
|Initial amount of S5                 |$   undeclared$ |mole                      |
|Value of parameter k1                |$         0.75$ |second^-1^ |
|Value of parameter k2                |$         0.25$ |second^-1^ |
|Value of parameter k4                |$          0.5$ |dimensionless |
|Volume of compartment "compartment"  |$            1$ |litre                     |
|Volume of compartment "compartment"1 |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00294" ];

addCompartment[ compartment, size -> 1 ];
addCompartment[ compartment1, size -> 1 ];
addSpecies[ S1, compartment->compartment, initialAmount -> 1.0];
addSpecies[ S3, compartment->compartment1, initialAmount -> 0];
addSpecies[ S5, compartment->compartment1];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addParameter[ k4, value -> 0.5 ];
addRule[ type->AssignmentRule, variable -> S5, math ->k4 * S3];
addReaction[ S1 -> S3 , reversible -> False,
	     kineticLaw -> k1 * S1 * compartment ];
addReaction[ S3 -> S1, reversible -> False,
	     kineticLaw -> k2 * (S3-S1) * compartment1 ];

makemodel[]
