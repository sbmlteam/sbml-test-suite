(* 

category:      Test
synopsis:      A fast and slow reaction, working against each other, with the fast reaction turned on and off by Events.
componentTags: Species, Reaction, Compartment, EventNoDelay, EventDelay
testTags:      FastReaction
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

 After B is converted to A by the slow reaction for a while, eventually the two events trigger, setting the reaction rate of the fast reaction to 1*A--consuming all of A and turning it back into B.  Then it turns back off, and B is once again slowly converted to A.  This would cycle again, but we only test one cycle.

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Value of species A       |$0$          |
|Value of species B       |$2$          |]


*)
