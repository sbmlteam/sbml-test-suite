(*

category:        Test
synopsis:        Test SBML IDs named 'false'
componentTags:   Parameter
testTags:        
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'false' are properly handled by the simulator.

The model contains:
* 3 parameters (false, False, FALSE)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter false | $4$ | constant |
| Initial value of parameter False | $5$ | constant |
| Initial value of parameter FALSE | $6$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
