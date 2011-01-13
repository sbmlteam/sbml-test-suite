(* 

category:      Test
synopsis:      Basic one reaction with one species in one compartment
               and one event, assigning to a parameter.
componentTags: Compartment, Species, Reaction, Parameter, EventNoDelay, 
testTags:      Amount, NonConstantCompartment, NonUnityCompartment, InitialAmount, NonConstantParameter, InitialValue
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There is one species
called S1 and one parameter called k1.  The model
contains one reaction defined as:

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| -> S1 | $k1 * C / S1$  |]

The model contains one event that assigns a value to the compartment C:

[{width:30em,margin-left:5em}| | *Trigger*   | *Delay* | *Assignments* |
 | Event1                      | $S1 > 2$ | $-$     | $C = 10$      |]

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*|
|Initial amount of S1          |$1.0$  |
|Value of parameter k1         |$1.0$  |
|Volume of compartment C       |$1$    |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00945" ];

addCompartment[ C, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0];
addParameter[ k1, value -> 1 ];
addReaction[ -> S1, reversible -> False,
	     kineticLaw -> k1 * C / S1 ];
addEvent[ trigger -> S1 < 0.75, eventAssignment -> C->10 ];

makemodel[]
