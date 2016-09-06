(*

category:        Test
synopsis:        A rateOf csymbol in an event trigger.
componentTags:   CSymbolRateOf, EventNoDelay, Parameter, RateRule
testTags:        NonConstantParameter
testType:        TimeCourse
levels:          3.2
generatedBy:     Numeric
packagesPresent: 

 A rateOf csymbol is used here in an event trigger that assigns to p2.

The model contains:
* 2 parameters (p1, p2)

There is one event:

[{width:30em,margin: 1em auto}|  *Event*  |  *Trigger*  | *Event Assignments* |
| _E0 | $rateOf(p1) > 1.05$ | $p2 = 5$ |]


There is one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | p1 | $0.01 * p1$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter p1 | $1$ | variable |
| Initial value of parameter p2 | $10$ | variable |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
