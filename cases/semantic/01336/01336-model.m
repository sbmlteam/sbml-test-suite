(*

category:        Test
synopsis:        Two events that trigger at t0, one of which cancels the other.
componentTags:   EventNoDelay, EventPriority, Parameter
testTags:        EventIsNotPersistent, EventT0Firing, NonConstantParameter
testType:        TimeCourse
levels:          3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

 In this model, two events trigger at t0, but the first reverses the trigger of the second, which is set persistent=false, cancelling the event's execution.

The model contains:
* 2 parameters (x, y)

There are 2 events:

[{width:45em,margin: 1em auto}|  *Event*  |  *Trigger*  |  *Priority*  |  *Persistent*  |  *initialValue*  | *Event Assignments* |
| E1 | $x > 2$ | $10$ | true | false | $x = 0$ |
| E2 | $x > 2$ | $5$ | false | false | $y = 7$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter x | $3$ | variable |
| Initial value of parameter y | $6$ | variable |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
