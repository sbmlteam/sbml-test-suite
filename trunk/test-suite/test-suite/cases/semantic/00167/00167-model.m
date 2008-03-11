(*

category:      Test
synopsis:      Model using parameters and rules only.
componentTags: Parameter, RateRule 
testTags:      InitialAmount, NonConstantParameter
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains eight parameters named S1, S2, S3, S4, k1, k2, k3 and k4.

  The model contains four rules:
[|| Type || Variable || Formula |
 || Rate || S1 || $k2*S2-k1*S1$  |
 || Rate || S2 || $k1*S1-k2*S2-k3*S2+k4*S3*S4$  |
 || Rate || S3 || $k3*S2-k4*S3*S4$  |
 || Rate || S4 || $k3*S2-k4*S3*S4$  |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|             Value of parameter S1:|| $1.0 \x 10^-15$ || any |
|             Value of parameter S2:|| $2.0 \x 10^-15$ || same as S1 |
|             Value of parameter S3:|| $            0$ || same as S1 |
|             Value of parameter S4:|| $            0$ || same as S1 |
|             Value of parameter k1:|| $         0.75$ || second^-1^ |
|             Value of parameter k2:|| $         0.25$ || second^-1^ |
|             Value of parameter k3:|| $         0.15$ || second^-1^ |
|             Value of parameter k4:|| $          0.1$ || (units of S1)^-1^ second^-1^ |]


*)

newcase[ "00167" ];

addParameter[ S1, value -> 1.0 10^-15, constant -> False];
addParameter[ S2, value -> 2.0 10^-15, constant -> False];
addParameter[ S3, value -> 0, constant -> False];
addParameter[ S4, value -> 0, constant -> False];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addParameter[ k3, value -> 0.15 ];
addParameter[ k4, value -> 0.1 ];
addRule[ type->RateRule, variable -> S1, math -> k2*S2-k1*S1];
addRule[ type->RateRule, variable -> S2, math -> k1*S1-k2*S2-k3*S2+k4*S3*S4];
addRule[ type->RateRule, variable -> S3, math -> k3*S2-k4*S3*S4];
addRule[ type->RateRule, variable -> S4, math -> k3*S2-k4*S3*S4];

makemodel[]
