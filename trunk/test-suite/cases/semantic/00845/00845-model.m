(* 

category:      Test
synopsis:      One reversible reactions with two species in one compartment
               and one event that assigns value to a species.
componentTags: Compartment, Species, Reaction, Parameter, EventNoDelay 
testTags:      Amount, ReversibleReaction
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There are three species
called S1, S2 and S3 and two parameters called k1 and k2.  The model
contains one reactions defined as:

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| S1 <-> S2 | $(kf * S1 - kr * S2) * C$  |]

The model contains one event that assigns values to species S1:

[{width:30em,margin-left:5em}| | *Trigger*   | *Delay* | *Assignments* |
 | Event1                      | $S1 < 0.75$ | $-$     | $S2 = 1.5$    |]
 
The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value* |*Units*  |
|Initial amount of S1          |$1.0$   |mole                      |
|Initial amount of S2         |$2.0$   |mole                      |
|Value of parameter kf         |$0.9$  |second^-1^ |
|Value of parameter kr         |$0.075$  |second^-1^                |
|Volume of compartment C       |$1$     |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00845" ];

addCompartment[ C, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0];
addSpecies[ S2, initialAmount -> 2.0];
addParameter[ kf, value -> 0.9 ];
addParameter[ kr, value -> 0.075 ];
addReaction[ S1 -> S2,
	     kineticLaw -> (kf * S1 - kr * S2) * C ];
addEvent[ trigger -> S1 < 0.75, eventAssignment -> {S2->1.5} ];

makemodel[]
