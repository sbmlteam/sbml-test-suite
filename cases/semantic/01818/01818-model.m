(*

category:        Test
synopsis:        Test SBML IDs named 'pi'
componentTags:   Parameter
testTags:        
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'pi' are properly handled by the simulator.

The model contains:
* 3 parameters (pi, Pi, PI)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter pi | $7$ | constant |
| Initial value of parameter Pi | $8$ | constant |
| Initial value of parameter PI | $9$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
