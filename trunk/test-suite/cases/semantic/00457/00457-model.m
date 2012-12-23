(* 

category:      Test
synopsis:      Basic three reactions with three species in one compartment
               and one event that assigns value to two species, subject to a delay.
componentTags: Compartment, Species, Reaction, Parameter, EventWithDelay 
testTags:      Amount, EventUsesTriggerTimeValues
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There are three species
called S1, S2 and S3 and three parameters called k1, k2 and k3.  The model
contains three reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k1 * S1 * C$  |
| S2 -> S3 | $k2 * S2 * C$  |
| S3 -> S1 | $k3 * S3 * C$     |]

The model contains one event that assigns values to both species S1 and S2:

[{width:30em,margin: 1em auto}| | *Trigger*   | *Delay* | *Assignments* |
 | Event1                      | $S1 < 0.75$ | $1.5$   | $S2 = 1.5$    |
 |                             |             |         | $S1 = S2$     |]
 
Note that the event assignments should happen simultaneously, not
sequentially; i.e., the value of S2 assigned to S1 is the value at the
point at which the event was triggered.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value* |*Units*  |
|Initial amount of S1          |$1.0$   |mole                      |
|Initial amount of S2          |$2.0$   |mole                      |
|Initial amount of S3          |$1.0$   |mole                      |
|Value of parameter k1         |$0.75$  |second^-1^                |
|Value of parameter k2         |$0.55$  |second^-1^                |
|Value of parameter k3         |$0.25$  |second^-1^                |
|Volume of compartment C       |$1$     |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00457" ];

addCompartment[ C, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0];
addSpecies[ S2, initialAmount -> 2.0];
addSpecies[ S3, initialAmount -> 1.0];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.55 ];
addParameter[ k3, value -> 0.25 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1 * S1 * C ];
addReaction[ S2 -> S3, reversible -> False,
	     kineticLaw -> k2 * S2 * C ];
addReaction[ S3 -> S1 , reversible -> False,
	     kineticLaw -> k3 * S3 * C ];
addEvent[ trigger -> S1 < 0.75, delay->1.5, eventAssignment -> {S2->1.5, S1->S2} ];

makemodel[]
