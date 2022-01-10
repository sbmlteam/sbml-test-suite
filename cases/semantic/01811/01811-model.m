(*

category:        Test
synopsis:        Test SBML IDs named 'inf'
componentTags:   InitialAssignment, Parameter
testTags:        InitialValueReassigned
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'inf' are properly handled by the simulator when assigned as values to other parameters.

The model contains:
* 7 parameters (INF, Inf, inf, a, b, c, d)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter INF | $10$ | constant |
| Initial value of parameter Inf | $1$ | constant |
| Initial value of parameter inf | $0.1$ | constant |
| Initial value of parameter a | $INF$ | constant |
| Initial value of parameter b | $Inf$ | constant |
| Initial value of parameter c | $inf$ | constant |
| Initial value of parameter d | $INF$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
