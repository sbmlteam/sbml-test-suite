(*

category:        Test
synopsis:        Use of the 'implies' MathML in assignment rules.
componentTags:   AssignmentRule, CSymbolTime, Parameter, UncommonMathML
testTags:        InitialValueReassigned, NonConstantParameter
testType:        TimeCourse
levels:          3.2
generatedBy:     Numeric
packagesPresent: 

 In this model, the 'implies' MathML symbol is used in assignment rules.  'Implies' means 'if A is true, then B is also true', or '(not A) or B'.  With the automatic translation (in l3v2) of boolean values to numeric values, this means that only p2 becomes 0 (false) during the middle of the run.

The model contains:
* 2 parameters (p1, p2)

There are 2 rules:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Assignment | p1 | $implies((time > 5.5), (time > 2.5))$ |
| Assignment | p2 | $implies((time > 2.5), (time > 5.5))$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter p1 | $implies((time > 5.5), (time > 2.5))$ | variable |
| Initial value of parameter p2 | $implies((time > 2.5), (time > 5.5))$ | variable |]

*)
