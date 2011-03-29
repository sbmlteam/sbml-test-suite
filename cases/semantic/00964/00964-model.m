(* 

category:      Test
synopsis:      Three competing events with the same priority, jointly causing a parameter to monotonically increase, checking to make sure one event doesn't severely out-compete the other two.   NOTE:  STOCHASTIC TEST. This test is designed to fail only one time in a million, but it still may happen.
componentTags: Parameter, EventNoDelay, EventPriority, AssignmentRule
testTags:      PersistentTrigger, CSymbolTime, RandomEventExecution
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

This model contains three events with the same trigger, the same priority, and all set 'persistent=false', and all of which disable the trigger of the others.  This means that every .01 seconds, one fires and the others do not, at random, and increases the parameters Q, R, and T, respectively.  A fourth parameter, S, is assigned the value of Q+R+T, meaning that it doesn't matter which one fires; S will increase monotonically.  A final parameter, 'error' checks to make sure none of the three parameters are chosen more frequently than the other two--if the difference gets higher than 5, it triggers.  From our simulations, this should happen less often than one time in a million, so if you see it happen in your simulator more often than once, there's a good chance something is wrong.
 
The events are:

[{width:30em,margin-left:5em}| | *Trigger*   | *Delay* | *Assignments* |
 | Qinc | $time - reset >= 0.01$ | $-$   | $Q = Q + 0.01, reset = time$      |
 | Rinc | $time - reset >= 0.01$ | $-$   | $R = R + 0.01, reset = time$      |
 | Tinc | $time - reset >= 0.01$ | $-$   | $T = T + 0.01, reset = time$      |
 | error_check | $or((abs(Q - R) >= tolerance), (abs(Q - T) >= tolerance), (abs(T - R) >= tolerance))$ | $-$ |  $error = 1$      |]


 The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*|
|Value of parameter Q          |$0$  |
|Value of parameter R          |$0$  |
|Value of parameter S          |$0$  |
|Value of parameter T          |$0$  |
|Value of parameter tolerance  |$5$  |
|Value of parameter reset      |$0$  |
|Value of parameter error      |$0$  |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.
*)
