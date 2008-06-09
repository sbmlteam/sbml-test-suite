(* 

category:      Test
synopsis:      Second order mass action kinetics with two reactants.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      InitialAmount
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3
generatedBy:   Analytic

The model contains one compartment called C.  There are three species called 
S1, S2 and S3 and one parameter called k.  The model contains one reaction that has
a kinetic law that defines second order mass action kinetics with two reactants
(as referenced by SBO:0000054):

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 | $k * S1 * S2 * C$  |]

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |*Units*  |
|Initial concentration of S1        |$1.0$ |mole                      |
|Initial concentration of S2        |$0.5$ |mole                      |
|Initial concentration of S3        |$0$ |mole                      |
|Value of parameter k       |$1.1$          |litre mole^-1^ second^-1^ |
|Volume of compartment C     |$1$             |litre                  |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

Note: The test data for this model was generated from an analytical solution
of the system of equations.

*)

newcase[ "00805" ];

addCompartment[ C, size -> 1 ];
addSpecies[ S1, initialConcentration -> 1.0];
addSpecies[ S2, initialConcentration -> 0.5];
addSpecies[ S3, initialConcentration -> 0];
addParameter[ k, value -> 1.1 ];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> k * S1 * S2 * C ];

makemodel[]
