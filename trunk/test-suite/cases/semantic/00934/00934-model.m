(* 

category:      Test
synopsis:      Single forward reaction with two species in one compartment and an event
componentTags: Compartment, Species, EventNoDelay, EventPriority
testTags:      Amount
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

The model contains one compartment called "C".  There are two
species called S1 and S2. 


The model contains three events that assign value to species S1 and S2 defined as:

[{width:30em,margin-left:5em}| | *Trigger*    | *Priority* | *Delay* | *Assignments* |
 | Event1 | $t >= 1$ | $10$  | $-$   | $S1 = 1$    |
 |        |          |      |       | $S2 = 0$    |
 | Event2 | $t >= 1$ | $2*S2$  | $-$   | $S1 = 2$    |
 |        |          |      |       | $S2 = 1$    |
 | Event3 | $t >= 1$ | $2*S1$  | $-$   | $S1 = 3$    |
 |        |          |      |       | $S2 = 2$    |]
 
The Events will execute simultaneously and must be executed in order determined by
the Priority. Since the priority of all remaining events must be re-calculated
after each event is executed, the order in which the events execute changes after
Event1.

The initial conditions are as follows:

[{width:30em,margin-left:5em}|       |*Value*          |*Units*        |
|Initial amount of S1                |$0$  |mole           |
|Initial amount of S2                |$1$              |mole           |
|Volume of compartment "C" |$1$              |litre          |]

The species' initial quantities are given in terms of substance units to
make it easier to use the model in a discrete stochastic simulator, but (as
per usual SBML principles) their symbols represent their values in
concentration units where they appear in expressions.

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

