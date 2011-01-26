(* 

category:      Test
synopsis:      Competing events with different priorities, meaning one always fires and the other does not.
 jointly causing a parameter to monotonically increase, checking to make sure the two events are not exactly evently distributed.  NOTE:  STOCHASTIC TEST. Your software may fail periodically; it is only supposed to succeed in the majority of cases.
componentTags: Parameter, EventNoDelay, EventPriority, AssignmentRule
testTags:      InitialValue, CSymbolTime, RandomEventExecution
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

 This model contains two pairs of events with the same two triggers and different priorities, both set 'persistent=true', and both of which disable the trigger of the other.  This means that every .01 seconds, one fires and the other does not, and the higher-priority event is the one that always wins.  Just to make sure that simulators don't accidentally do what was designed to happen here, the two pairs of events come in the same order alphabetically and within the file, but the priorities are reversed, so if you always pick the first event instead of looking at the priority, you will fail one of the tests.
 
The events are:

[{width:30em,margin-left:5em}| | *Trigger*   | *Delay* | *Assignments* |
 | Qinc | $time - reset >= 0.01$ | $-$   | $Q = Q + 0.01, reset = time$ |
 | Rinc | $time - reset >= 0.01$ | $-$   | $R = R + 0.01, reset = time$ |
 | Qinc2| $time - reset2>= 0.01$ | $-$   | $Q2= Q2 + 0.01, reset2 = time$ |
 | Rinc2| $time - reset2>= 0.01$ | $-$   | $R2= R2 + 0.01, reset2 = time$ |]


 The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*|
|Value of parameter Q          |$0$  |
|Value of parameter R          |$0$  |
|Value of parameter reset      |$0$  |
|Value of parameter Q2         |$0$  |
|Value of parameter R2         |$0$  |
|Value of parameter reset2     |$0$  |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.
*)
