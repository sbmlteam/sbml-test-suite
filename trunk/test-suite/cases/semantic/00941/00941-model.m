(* 

category:      Test
synopsis:      A parameter that varies in its IntialAssignment, but is constant thereafter, being echoed by a second parameter viewing it through a delay.
componentTags: Compartment, Parameter, InitialAssignment, AssignmentRule
testTags:      Amount, CSymbolDelay, CSymbolTime, NonConstantParameter
testType:      TimeCourse
levels:        2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

 The model contains a parameter x whose InitialAssignment claims that it has been oscillating (the sine function) up until the start of the simulation, at which point it stops changing.  A second parameter (y) echoes this species with a 0.5 second delay, meaning that it starts by 'seeing' the oscillations before flattening out.

The initial conditions are as follows:

[{width:30em,margin-left:5em}|       |*Value*          |
|Initial amount of x                |$0$  |
|Initial amount of y                |$2.958924275$              |
|Volume of compartment "default_compartment" |$1$              |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

