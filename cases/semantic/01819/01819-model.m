(*

category:        Test
synopsis:        Test SBML IDs named 'pi'
componentTags:   InitialAssignment, Parameter
testTags:        InitialValueReassigned
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'pi' are properly handled by the simulator when assigned as values to other parameters.

The model contains:
* 7 parameters (pi, Pi, PI, a, b, c, d)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter pi | $7$ | constant |
| Initial value of parameter Pi | $8$ | constant |
| Initial value of parameter PI | $9$ | constant |
| Initial value of parameter a | $pi$ | constant |
| Initial value of parameter b | $Pi$ | constant |
| Initial value of parameter c | $PI$ | constant |
| Initial value of parameter d | $pi$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
