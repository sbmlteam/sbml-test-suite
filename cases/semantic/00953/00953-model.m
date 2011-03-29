(* 

category:      Test
synopsis:      Competing events without priorities, jointly causing a parameter to monotonically increase.
componentTags: Parameter, EventNoDelay, EventPriority, AssignmentRule
testTags:      InitialValue, CSymbolTime, PersistentTrigger
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

This model contains two events with the same trigger, no priority, both set 'persistent=true', and both of which disable the trigger of the other.  This means that every .01 seconds, one fires and the other does not, at random, and increases the parameters Q or R, respectively.  A third parameter, S, is assigned the value of Q+R, meaning that it doesn't matter which one fires; S will increase monotonically.  This model is identical to 952, except that there are no priorities, so Qinc and Rinc do not have to be chosen between at random, but can be chosen in any ratio.
 
The events are:

[{width:30em,margin-left:5em}| | *Trigger*   | *Delay* | *Assignments* |
 | Qinc | $time - reset >= 0.01$ | $-$   | $Q = Q + 0.01, reset = time$ |
 | Rinc | $time - reset >= 0.01$ | $-$   | $R = R + 0.01, reset = time$ |]


 The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*|
|Value of parameter Q          |$0$  |
|Value of parameter R          |$0$  |
|Value of parameter S          |$0$  |
|Value of parameter reset      |$0$  |
|Value of parameter error      |$0$  |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.
*)
