(* 

category:      Test
synopsis:      Several parameters with assignment rules, testing various L2v1 built-in functions acting on constants.
componentTags: Parameter, AssignmentRule
testTags:      InitialValueReassigned
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

The model tests the various mathematical constructs added in the L2v1 specification.  The libsbml parser still allows them in L1v2 via the infix-to-mathml converter, so the l1v2 version of the model is included, even though a specification-conforming application officially need not understand the included constructs.

This file doesn't contain a list of the initial conditions, since there are a ton of them, and the whole point is that you are supposed to calculate them yourself.

Note: The test data for this model was generated from an analytical
solution of the system of equations.
