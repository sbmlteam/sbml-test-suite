(*

category:        Test
synopsis:        Test SBML IDs named 'time'
componentTags:   Parameter
testTags:        NonConstantParameter
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'time' are properly handled by the simulator.

The model contains:
* 3 parameters (time, Time, TIME)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter time | $10$ | constant |
| Initial value of parameter Time | $11$ | constant |
| Initial value of parameter TIME | $12$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
