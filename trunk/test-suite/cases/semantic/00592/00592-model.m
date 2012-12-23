(* 

category:      Test
synopsis:      Basic single forward reaction with two species in a 1-dimensional
compartment.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      Concentration
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

The model contains one compartment called C.  There are two
species called S1 and S2 and one parameter called k1.  Compartment
C is 1-dimensional.  The model contains one reaction defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k1 * S1 * C$  |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value*         |*Units*  |
|Initial concentration of S1   |$1.5 \x 10^-1$ |mole metre^-1^                      |
|Initial concentration of S2   |$         0$    |mole metre^-1^                      |
|Value of parameter k1         |$            1$ |second^-1^                |
|Length of compartment C       |$            1$ |metre                     |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

newcase[ "00592" ];

addCompartment[ C, spatialDimensions-> 1, size -> 1 ];
addSpecies[ S1, initialConcentration -> 1.5 10^-1 ];
addSpecies[ S2, initialConcentration -> 0 ];
addParameter[ k1, value -> 1 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1 * S1 * C ];

makemodel[]
