(* 

category:      Test
synopsis:      Basic single forward reaction with two species in a 1-dimensional
compartment.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      Amount
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

The model contains one compartment called "compartment".  There are two
species named S1 and S2 and one parameter named k1.  Compartment
compartment is 1-dimensional.  The model contains one reaction defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k1 * S1 * compartment$  |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*         |*Units*  |
|Initial amount of S1                |$1.5 \x 10^-1$ |mole                      |
|Initial amount of S2                |$         0$    |mole                      |
|Value of parameter k1               |$            1$ |second^-1^                |
|Length of compartment "compartment" |$            1$ |metre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

newcase[ "00045" ];

addCompartment[ compartment, spatialDimensions-> 1, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-1 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 1 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1 * S1 * compartment ];

makemodel[]
