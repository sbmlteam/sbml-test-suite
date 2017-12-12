(*

category:        Test
synopsis:        Test of 0-child MathML constructs in function definitions.
componentTags:   FunctionDefinition, InitialAssignment, Parameter
testTags:        InitialValueReassigned
testType:        TimeCourse
levels:          2.2, 2.3, 2.4, 2.5, 3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

In the official MathML spec, plus, times, and, or, and xor may all have 0 arguments.  When they do, they are supposed return identity for that construct, namely:
 *plus:  0
 *times: 1
 *and:   true
 *or:    false
 *xor:   false

This tests tests these constructs in function definitions.

The model contains:
* 5 parameters (a, b, c, d, e)
The model contains the following function definitions:

[{width:30em,margin: 1em auto}|  *Id*  |  *Arguments*  |  *Formula*  |
 | my_plus |  | $plus()$ |
 | my_times |  | $times()$ |
 | my_and |  | $and()$ |
 | my_or |  | $or()$ |
 | my_xor |  | $xor()$ |
]
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter a | $my_plus()$ | constant |
| Initial value of parameter b | $my_times()$ | constant |
| Initial value of parameter c | $piecewise(1, my_and(), 0)$ | constant |
| Initial value of parameter d | $piecewise(1, my_or(), 0)$ | constant |
| Initial value of parameter e | $piecewise(1, my_xor(), 0)$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
