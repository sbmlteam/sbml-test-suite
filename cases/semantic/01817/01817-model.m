(*

category:        Test
synopsis:        Test SBML IDs named 'false'
componentTags:   InitialAssignment, Parameter
testTags:        InitialValueReassigned
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'false' are properly handled by the simulator and when assigned to parameters.

The model contains:
* 6 parameters (false, False, FALSE, a, b, c)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter false | $4$ | constant |
| Initial value of parameter False | $5$ | constant |
| Initial value of parameter FALSE | $6$ | constant |
| Initial value of parameter a | $false$ | constant |
| Initial value of parameter b | $False$ | constant |
| Initial value of parameter c | $FALSE$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
