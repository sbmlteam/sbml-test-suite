(* 

category:      Test
synopsis:      Reactions occurring between two compartments. 
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      Amount, MultiCompartment, NonUnityStoichiometry
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1
generatedBy:   Numeric

The model contains two compartments called "compartment" and
"compartment1".  There are three species named S1, S2 and S3 and two
parameters named k1 and k2.  Compartment "compartment" contains species S1
and S2.  Compartment "compartment1" contains species S3.  The model
contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S2  +  S2 | $k1 * S1 * S2 * compartment$  |
| S2 -> S3             | $k2 * (S2-S3) * compartment1$  |]

The first reaction occurs entirely in compartment "compartment", whereas
the second reaction occurs between a species in "compartment" and a species
in "compartment1".

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|        |*Value*         |*Units*  |
|Initial amount of S1                 |$          1.0$ |mole                      |
|Initial amount of S2                 |$          1.0$ |mole                      |
|Initial amount of S3                 |$            0$ |mole                      |
|Value of parameter k1                |$         0.75$ |litre mole^-1^ second^-1^ |
|Value of parameter k2                |$         0.25$ |second^-1^                |
|Volume of compartment "compartment"  |$            1$ |litre                     |
|Volume of compartment "compartment1" |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00054" ];

addCompartment[ compartment, size -> 1 ];
addCompartment[ compartment1, size -> 1 ];
addSpecies[ S1, compartment->compartment, initialAmount -> 1.0];
addSpecies[ S2, compartment->compartment, initialAmount -> 1.0];
addSpecies[ S3, compartment->compartment1, initialAmount -> 0];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addReaction[ S1 + S2 -> S2  +  S2, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * compartment ];
addReaction[ S2 -> S3, reversible -> False,
	     kineticLaw -> k2 * (S2-S3) * compartment1 ];

makemodel[]
