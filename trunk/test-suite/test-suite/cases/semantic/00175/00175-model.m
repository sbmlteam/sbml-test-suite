(* 

category:      Test
synopsis:      Model using rules and parameters only.
componentTags: Parameter, RateRule 
testTags:      NonConstantParameter
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one parameter named S1.

The model contains one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
 | Rate | S1 | $7$  |]


The initial conditions are as follows:

[{width:30em,margin-left:5em}|      |*Value*          |*Units*  |
|Value of parameter S1 |$          0.0$ |any |]


*)

newcase[ "00175" ];

addParameter[ S1, value -> 0.0, constant -> False ];
addRule[ type->RateRule, variable -> S1, math -> 7];

makemodel[]
