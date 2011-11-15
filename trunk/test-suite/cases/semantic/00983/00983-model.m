(* 

category:      Test
synopsis:      An assignment rule with a continually-changing delay from an algebraic rule. 
componentTags: CSymbolTime, CSymbolDelay, Parameter, AlgebraicRule
testTags:      NonConstantParameter
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

 This model contains a single parameter x changing at a constant rate, and a second parameter y with a continually-changing delay for an assignment rule:  it takes the value of x at 'temp' time ago, which in turn is assigned the value of time/2 via an algebraic rule.  Effectively, this means that y changes at half the rate of x.

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Value of parameter x       |$0$          |
|Value of parameter y       |$0$          |]


*)
