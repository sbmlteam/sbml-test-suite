(* 

category:      Test
synopsis:      Two events with different 'initialValue's, causing one to fire and one to not fire.
componentTags: Parameter, EventNoDelay
testTags:      EventT0Firing
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

 The model contains two events, one that assigns '1' to parameter p2, and one that assigns '1' to parameter p3.  Both have the same trigger, p1>0, which is true the entire time, but the first trigger is set 'initialValue="false"' and the second 'initialValue="true"'.  This causes the first to trigger at t0 and the second to not trigger.

[{width:30em,margin-left:5em}|       |*Value*          |
|Value of parameter p1               |$1$              |
|Value of parameter p2               |$0$              |
|Value of parameter p3               |$0$              |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

