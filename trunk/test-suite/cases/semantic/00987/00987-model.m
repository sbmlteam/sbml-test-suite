(* 

category:      Test
 synopsis:      A fast and slow reaction, the first of which feeds into the second, triggered by an event.
componentTags: Species, Reaction, Compartment, EventNoDelay
testTags:      FastReaction
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

After the event fires, the 'fast' reaction should go to completion instantly, converting all of A to B.  Then B converts to C normally.

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Value of species A       |$1$          |
|Value of species B       |$0$          |
|Value of species C       |$0$          |]


*)
