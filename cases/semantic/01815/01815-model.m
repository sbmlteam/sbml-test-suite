(*

category:        Test
synopsis:        Test SBML IDs named 'true'
componentTags:   InitialAssignment, Parameter
testTags:        InitialValueReassigned
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'true' are properly handled by the simulator when assigned as values to other parameters.

The model contains:
* 6 parameters (true, True, TRUE, a, b, c)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter true | $10$ | constant |
| Initial value of parameter True | $20$ | constant |
| Initial value of parameter TRUE | $30$ | constant |
| Initial value of parameter a | $true$ | constant |
| Initial value of parameter b | $True$ | constant |
| Initial value of parameter c | $TRUE$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
