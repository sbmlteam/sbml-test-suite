(* 

category:      Test
synopsis:      Four different ways of writing the avogadro number in an assignment rule.
componentTags: Parameter, AssignmentRule, CSymbolAvogadro
testTags:      InitialValueReassigned
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

The model assigns avogadro's number as a csymbol to four parameters in an assignment rule.  It tries to fool you by calling two of them 'time' and 'delay', and by not giving one a name at all.

To test that the value of avogadro is the same as is in the specification without requiring that you write out all 9 digits of precision, a fifth parameter is added with the value (avogadro - 6.0221e+23).

The initial conditions are as follows:

[{width:30em,margin-left:5em}|       |*Value*          |
|Value of parameter P1               |$6.02214179e+23)$   |
|Value of parameter P2               |$6.02214179e+23)$   |
|Value of parameter P3               |$6.02214179e+23)$   |
|Value of parameter P4               |$6.02214179e+23)$   |
|Value of parameter P5               |$4.179e+18)$   |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.
