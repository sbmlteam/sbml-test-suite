(*

category:        Test
synopsis:        Test SBML IDs named 'nan'
componentTags:   InitialAssignment, Parameter
testTags:        InitialValueReassigned
testType:        TimeCourse
levels:          2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'nan' are properly handled by the simulator when assigned as values to other parameters.

The model contains:
* 7 parameters (NAN, NaN, nan, p, q, r, s)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter NAN | $0.1$ | constant |
| Initial value of parameter NaN | $0.01$ | constant |
| Initial value of parameter nan | $0.001$ | constant |
| Initial value of parameter p | $NAN$ | constant |
| Initial value of parameter q | $NaN$ | constant |
| Initial value of parameter r | $nan$ | constant |
| Initial value of parameter s | $NaN$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
