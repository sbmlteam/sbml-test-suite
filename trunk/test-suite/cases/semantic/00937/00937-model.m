(* 

category:      Test
synopsis:      Two oscillators; one that echoes the first by a delay.
componentTags: Compartment, Parameter, AssignmentRule
testTags:      CSymbolDelay, CSymbolTime, NonConstantParameter
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

The model contains one compartment called "default_compartment".  

 The model contains one parameter (x) that oscillates (from the 'sin' function), and a second parameter (y) that echoes the first after a 0.2 second delay.

The initial conditions are as follows:

[{width:30em,margin-left:5em}|               |*Value*          |
|Initial value of x                          |$0$              |
|Initial value of y                          |$1.090703$       |
|Volume of compartment "default_compartment" |$1$              |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

