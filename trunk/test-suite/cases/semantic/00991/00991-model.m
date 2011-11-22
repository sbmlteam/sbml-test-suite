(* 

category:      Test
synopsis:      A simple reaction with stoichiometry set to a parameter that varies from an Event.
componentTags: Parameter, Species, Compartment, EventNoDelay
testTags:      Amount, AssignedVariableStoichiometry
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

 The stoichiometry of the reaction '-> nX' is set to be equal to p1, a parameter that starts at 1 and switches to 2 due to an event.

 The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Initial amount of X        |$1$                  |
|Value of parameter p1       |$1$          |
|Volume of compartment default_compartment     |$1$             |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.
*)
