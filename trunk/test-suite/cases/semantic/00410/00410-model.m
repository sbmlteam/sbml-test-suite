(* 

category:      Test
synopsis:      Basic two reactions with four species in one compartment
               and an event subject to delay.
componentTags: Compartment, Species, Reaction, Parameter, EventWithDelay 
testTags:      Amount
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There are four species called
S1, S2, S3 and S4 and two parameters called k1 and k2.  The model contains
two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 + S4 | $k1 * S1 * S2 * C$  |
| S3 + S4 -> S1 + S2 | $k2 * S3 * S4 * C$  |]

The model contains one event that assign values to both species S1 and S4:

[{width:30em,margin: 1em auto}| | *Trigger*    | *Delay* | *Assignments*         |
 | Event1                      | $S4 > S2$    | $1.2$   | $S1 -> 2 \x 10^-2$   |]
 |                             |              |         | $S4 -> 0.5 \x 10^-2$ |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value*          |*Units*  |
|Initial amount of S1          |$1.0 \x 10^-2$  |mole                       |
|Initial amount of S2          |$1.0 \x 10^-2$  |mole                       |
|Initial amount of S3          |$2.0 \x 10^-2$  |mole                       |
|Initial amount of S4          |$1.0 \x 10^-2$  |mole                       |
|Value of parameter k1         |$0.75 \x 10^2$  |litre mole^-1^ second^-1^  |
|Value of parameter k2         |$0.25 \x 10^2$  |litre mole^-1^ second^-1^  |
|Volume of compartment C       |$1$              |litre                      |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00410" ];

addCompartment[ C, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0 10^-2];
addSpecies[ S2, initialAmount -> 1.2 10^-2];
addSpecies[ S3, initialAmount -> 2.0 10^-2];
addSpecies[ S4, initialAmount -> 1.0 10^-2];
addParameter[ k1, value -> 0.75 10^2 ];
addParameter[ k2, value -> 0.25 10^2 ];
addReaction[ S1 + S2 -> S3 + S4, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * C ];
addReaction[ S3 + S4 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * S4 * C ];
addEvent[ trigger -> S4 > S2, delay->1.2, eventAssignment -> {S1->2/10^2, S4->5/10^3}  ];

makemodel[]
