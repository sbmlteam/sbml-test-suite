(* 

category:      Test
synopsis:      Two event where the two event assignments should not interfere with each other. 
componentTags: EventWithDelay, Parameter, CSymbolTime
testTags:      NonConstantParameter
testType:      TimeCourse
levels:        2.4, 3.1
generatedBy:   Analytic

 This model contains two events that trigger when time > 0.99.  In each, there is on event assignment that changes the value of a parameter, and a second event assignment that uses that parameter to change the value of a second parameter.  However, the second event assignment should not use the changed value from the first parameter, but rather the value it had at the beginning of event assignment.  (This is true regardless of the value of 'useValuesFromTriggerTime', but the value of that flag here happens to be false.  In order to test this in l2v4, there had to be a delay.)

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Value of parameter x       |$0$          |
|Value of parameter y       |$0$          |
|Value of parameter p       |$0$          |
|Value of parameter q       |$0$          |]

The events are:

[{width:30em,margin-left:5em}| | *Trigger*   | *Delay* | *Assignments* |
 | _E0 | $time >= 0.99$ | $0.1$  | $x = 2, y = y + x$      |
 | _E1 | $time >= 0.99$ | $0.1$  | $q = p + 1, p = 3$      |]


*)
