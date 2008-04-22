(* 

category:      Test
synopsis:      Model using parameters and both rate and algebraic rules.   
componentTags: Parameter, RateRule, AlgebraicRule 
testTags:      NonConstantParameter
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains four parameters named S1, S2, k1 and k2.
The model contains two rules:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
 | Algebraic |          | $S1 + S2-k1$  |
 | Rate      | S1       | $k2 * S2 $  |]


The initial conditions are as follows:

[{width:30em,margin-left:5em}|      |*Value*          |*Units*  |
|Value of parameter S1 |$          0.5$ |any |
|Value of parameter S2 |$          0.5$ |same as S1 |
|Value of parameter k1 |$          1.0$ |same as S1 |
|Value of parameter k2 |$          0.8$ |second^-1^ |]


*)

newcase[ "00182" ];

addParameter[ S1, value -> 0.5, constant -> False ];
addParameter[ S2, value -> 0.5, constant -> False  ];
addParameter[ k1, value -> 1.0 ];
addParameter[ k2, value -> 0.8 ];
addRule[ type->AlgebraicRule, math -> S1 + S2-k1];
addRule[ type->RateRule, variable -> S1, math -> k2 * S2 ];

makemodel[]
