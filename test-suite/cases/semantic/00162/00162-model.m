(*

category:      Test
synopsis:      Model using parameters and rules only.
componentTags: Parameter, RateRule 
testTags:      NonConstantParameter
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains four parameters named S1, S2, k1 and k2.

  The model contains two rules:
[|| Type || Variable || Formula |
 || Rate || S1       || $S2*k2-S1*k1$  |
 || Rate || S2       || $S1*k1-S2*k2$  |]


The initial conditions are as follows:
[|                                  ||          Value  || Units      |
|             Value of parameter S1:|| $1.5 \x 10^-15$ || any        |
|             Value of parameter S2:|| $            0$ || same as S1 |
|             Value of parameter k1:|| $            1$ || second^-1^ |
|             Value of parameter k2:|| $            0$ || second^-1^ |]


*)

newcase[ "00162" ];

addParameter[ S1, value -> 1.5 10^-15, constant -> False ];
addParameter[ S2, value -> 0, constant -> False ];
addParameter[ k1, value -> 1 ];
addParameter[ k2, value -> 0 ];
addRule[ type->RateRule, variable -> S1, math -> S2*k2-S1*k1];
addRule[ type->RateRule, variable -> S2, math -> S1*k1-S2*k2];

makemodel[]