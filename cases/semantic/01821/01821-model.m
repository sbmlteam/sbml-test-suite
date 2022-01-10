(*

category:        Test
synopsis:        Test SBML IDs named 'time'
componentTags:   CSymbolTime, InitialAssignment, Parameter
testTags:        InitialValueReassigned, NonConstantParameter
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

This tests whether parameters named 'time' are properly handled by the simulator when assigned as values to other parameters.

The model contains:
* 7 parameters (time, Time, TIME, a, b, c, d)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter time | $10$ | constant |
| Initial value of parameter Time | $11$ | constant |
| Initial value of parameter TIME | $12$ | constant |
| Initial value of parameter a | $time$ | constant |
| Initial value of parameter b | $Time$ | constant |
| Initial value of parameter c | $TIME$ | constant |
| Initial value of parameter d | $time$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
