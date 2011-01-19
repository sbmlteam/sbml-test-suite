(* 

category:      Test
synopsis:      Three constant parameters.
componentTags: Parameter
testTags:      InitialValueReassigned
testType:      TimeCourse
levels:        2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

 The model contains three parameters P, Q, and R. They are set so their initial values are 0, but then are reassigned to be infinity, negative infinity, and 'not a number' through initial assignments.  They then do not change.

The initial condition is as follows:

[{width:30em,margin-left:5em}| |*Value*|
|Value of parameter P          |$INF$  |
|Value of parameter P          |$-INF$  |
|Value of parameter P          |$NaN$  |]

*)

