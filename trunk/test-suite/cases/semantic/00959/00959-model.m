(* 

category:      Test
synopsis:      Several parameters with trigonometric assignment rules, testing various L2v1 built-in functions acting on parameters.
componentTags: Parameter, AssignmentRule
testTags:      InitialValueReassigned
testType:      TimeCourse
levels:        2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

 The model tests the various trigonometrical constructs added in L2v1, as assignment rules with time.  Some functions with weird boundary conditions have been set up with the 'piecewise' function so that the results are never 'inf', or 'nan'.

This file doesn't contain a list of the initial conditions, since there are a ton of them, and the whole point is that you are supposed to calculate them yourself.
