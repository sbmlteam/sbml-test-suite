(*

category:        Test
synopsis:        Test SBML IDs named 'true'
componentTags:   Parameter
testTags:        
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'true' are properly handled by the simulator when assigned as values to other parameters.

The model contains:
* 3 parameters (true, True, TRUE)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter true | $10$ | constant |
| Initial value of parameter True | $20$ | constant |
| Initial value of parameter TRUE | $30$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
