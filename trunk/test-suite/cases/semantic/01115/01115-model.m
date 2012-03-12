(*

category:      Test
synopsis:      Test of nested 1-child MathML constructs plus, times, and, or, and xor.
componentTags: InitialAssignment, Parameter
testTags:      InitialValueReassigned
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

In the official MathML spec, plus, times, and, or, and xor may all have 1 arguments.  When they do, they are supposed return that argument.

The model contains:
* 5 parameters (a, b, c, d, e)
The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial value of parameter a | $plus(1, plus(1, plus(1)))$ | constant |
| Initial value of parameter b | $times(2, times(2, times(2))) $ | constant |
| Initial value of parameter c | $piecewise(and(and(and(true), true), true), 1, 0)$ | constant |
| Initial value of parameter d | $piecewise(or(or(or(true), true), true), 1, 0)$ | constant |
| Initial value of parameter e | $piecewise(xor(xor(xor(true), true), true), 1, 0)$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
