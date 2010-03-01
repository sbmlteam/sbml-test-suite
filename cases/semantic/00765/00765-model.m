(* 

category:      Test
synopsis:      Basic two reactions with three species in one compartment
               and one event that assigns value to two species, subject to a delay.
componentTags: Compartment, Species, Reaction, Parameter, EventWithDelay 
testTags:      Concentration, NonUnityCompartment
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There are three species
called S1, S2 and S3 and two parameters called k1 and k2.  The model
contains two reactions defined as:

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 | $k1 * S1 * S2 * C$  |
| S3 -> S1 + S2 | $k2 * S3 * C$     |]

The model contains one event that assigns values to both species S1 and S2:

[{width:30em,margin-left:5em}| | *Trigger*   | *Delay* | *Assignments* |
 | Event1                      | $S1 < 0.65$ | $1$     | $S2 = 1.5$    |
 |                             |             |         | $S1 = 1$      |]

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value* |*Units*  |
|Initial concentration of S1          |$1.0$   |mole litre^-1^                      |
|Initial concentration of S2          |$2.0$   |mole litre^-1^                      |
|Initial concentration of S3          |$1.0$   |mole litre^-1^                      |
|Value of parameter k1         |$0.75$  |litre mole^-1^ second^-1^ |
|Value of parameter k2         |$0.25$  |second^-1^                |
|Volume of compartment C       |$0.9$   |litre                     |]

*)

newcase[ "00765" ];

addCompartment[ C, size -> 0.9 ];
addSpecies[ S1, initialConcentration -> 1.0];
addSpecies[ S2, initialConcentration -> 2.0];
addSpecies[ S3, initialConcentration -> 1.0];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * C ];
addReaction[ S3 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * C ];
addEvent[ trigger -> S1 < 0.65, delay->1, eventAssignment -> {S2->1.5, S1->1} ];

makemodel[]
