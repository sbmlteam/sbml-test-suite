(* 

category:      Test
synopsis:      Three constant parameters, with initialassignments to inf, -inf, and nan.
componentTags: Parameter, InitialAssignment
testTags:      InitialValueReassigned
testType:      TimeCourse
levels:        2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

 The model contains three parameters P, Q, and R. They are set so their initial values are 0, but then are reassigned to be infinity, negative infinity, and 'not a number' through initial assignments.  They then do not change.

The initial condition is as follows:

[{width:30em,margin-left:5em}| |*Value*|
|Value of parameter P          |$INF$  |
|Value of parameter P          |$-INF$  |
|Value of parameter P          |$NaN$  |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.
*)

