(* 

category:      Test
synopsis:      Basic two reactions with three species in a compartment using 
initialAssignment to set the initial value of the compartment.
componentTags: Compartment, Species, Reaction, Parameter, InitialAssignment 
testTags:      Concentration, NonUnityCompartment, InitialValueReassigned
testType:      TimeCourse
levels:        2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There are three species called 
S1, S2 and S3 and three parameters called k1, k2 and p1.  The model contains 
two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 | $k1 * S1 * S2 * C$  |
| S3 -> S1 + S2 | $k2 * S3 * C$  |]

The model contains one initialAssignment:

[{width:30em,margin: 1em auto}| Variable | Formula |
 | C | $p1 * 2$  |]

Note: SBML's InitialAssignment construct override any declared initial
values.  In this case the initial value declared for compartment C is
consistent with the value returned by the InitialAssignment.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value*       |*Units*  |
|Initial concentration of S1        |$1.0 \x 10^-2$ |mole litre^-1^                      |
|Initial concentration of S2        |$2.0 \x 10^-2$ |mole litre^-1^                      |
|Initial concentration of S3        |$1.0 \x 10^-2$ |mole litre^-1^                      |
|Value of parameter k1       |$0.75$          |litre mole^-1^ second^-1^ |
|Value of parameter k2       |$0.25$          |second^-1^                |
|Value of parameter p1       |$0.25$          |litre                |
|Volume of compartment C     |$0.5$           |litre                  |]

*)

newcase[ "00784" ];

addCompartment[ C, size -> 0.5, constant -> False ];
addSpecies[ S1, initialConcentration -> 1.0 10^-2];
addSpecies[ S2, initialConcentration -> 2.0 10^-2];
addSpecies[ S3, initialConcentration -> 1.0 10^-2];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addParameter[ p1, value -> 0.25 ];
addInitialAssignment[ C, math -> p1*2];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * C ];
addReaction[ S3 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * C ];

makemodel[]
