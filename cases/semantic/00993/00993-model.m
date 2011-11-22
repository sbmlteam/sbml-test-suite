(* 

category:      Test
synopsis:      A simple reaction with stoichiometry set to a parameter, which in turn is set to be the same as its species via an algebraic rule.
componentTags: Parameter, Species, Compartment, AlgebraicRule
testTags:      Amount, AssignedVariableStoichiometry
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

 The stoichiometry of the reaction '-> nX' is set to be equal to p1, a parameter that is set by an algebraic rule to be equal to X.

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Initial amount of X        |$1$                  |
|Value of parameter k1       |$1$          |
|Value of parameter p1       |$1$          |
|Volume of compartment default_compartment     |$1$             |]

*)
