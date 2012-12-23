(* 

category:      Test
synopsis:      Basic reactions with three species in a 2-dimensional compartment.
compartment, with two species acting as a boundary condition.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      Amount, BoundaryCondition
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called "compartment".  There are three
species named S1, S2 and S3 and two parameters named k1 and k2.
Compartment "compartment" is 2-dimensional.  Both species S1 and S3 are
labeled as an SBML boundary species.  The model contains two reactions
defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 | $k1 * S1 * S2 * compartment$  |
| S3 -> S1 + S2 | $k2 * S3 * compartment$  |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1              |$1.0 \x 10^-2$ |mole                      |
|Initial amount of S2              |$2.0 \x 10^-2$ |mole                      |
|Initial amount of S3              |$1.0 \x 10^-2$ |mole                      |
|Value of parameter k1             |$         0.75$ |metre^2^ mole^-1^ second^-1^ |
|Value of parameter k2             |$         0.25$ |second^-1^                |
|Area of compartment "compartment" |$            1$ |metre^2^                  |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00217" ];

addCompartment[ compartment, spatialDimensions-> 2, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0 10^-2, boundaryCondition->True];
addSpecies[ S2, initialAmount -> 2.0 10^-2];
addSpecies[ S3, initialAmount -> 1.0 10^-2, boundaryCondition->True];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * compartment ];
addReaction[ S3 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * compartment ];

makemodel[]
