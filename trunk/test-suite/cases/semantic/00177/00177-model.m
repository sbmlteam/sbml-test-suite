(* 

category:      Test
synopsis:      Model with parameters and rules only.
componentTags: Parameter, RateRule 
testTags:      NonConstantParameter
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains two varying parameters called S1 and S2 and one constant
parameter called k1.  (Note that indeed S1 and S2 are parameters and not 
species in this model.)
 
The model contains three rules:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Rate | k1       | $0.5$  |
 | Rate | S1       | $-(k1 * S1)$  |
 | Rate | S2       | $k1 * S1$  |]


The initial conditions are as follows:

[{width:30em,margin: 1em auto}|      |*Value*          |*Units*  |
|Value of parameter S1 |$1.5 \x 10^-1$ |any |
|Value of parameter S2 |$            0$ |same as S1 |
|Value of parameter k1 |$  1 \x 10^-1$ |second^-1^ |]


*)

newcase[ "00177" ];

addParameter[ S1, value -> 1.5 10^-1, constant -> False ];
addParameter[ S2, value -> 0, constant -> False ];
addParameter[ k1, value -> 1 10^-1, constant -> False ];
addRule[ type->RateRule, variable -> k1, math -> 0.5];
addRule[ type->RateRule, variable -> S1, math -> -(k1 * S1)];
addRule[ type->RateRule, variable -> S2, math -> k1 * S1];

makemodel[]
