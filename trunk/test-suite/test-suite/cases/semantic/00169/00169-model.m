(*

category:      Test
synopsis:      Model using parameters and rules only.
componentTags: Parameter, RateRule 
testTags:      NonConstantParameter
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains seven parameters named S1, S2, S3, S4, k1, k2 and k3.

  The model contains four rules:

[{width:30em,left-margin:5em}| *Type* | *Variable* | *Formula* |
 | Rate | S1       | $-k1*S1$  |
 | Rate | S2       | $k1*S1-k2*S2$  |
 | Rate | S3       | $k2*S2-k3*S3$  |
 | Rate | S4       | $k3*S3$  |]


The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|             Value of parameter S1:| $1.0 \x 10^-15$ | any |
|             Value of parameter S2:| $            0$ | same as S1 |
|             Value of parameter S3:| $            0$ | same as S1 |
|             Value of parameter S4:| $            0$ | same as S1 |
|             Value of parameter k1:| $          0.7$ | second^-1^ |
|             Value of parameter k2:| $          0.5$ | second^-1^ |
|             Value of parameter k3:| $          1.0$ | second^-1^ |]


*)

newcase[ "00169" ];

addParameter[ S1, value -> 1.0 10^-15, constant -> False];
addParameter[ S2, value -> 0, constant -> False];
addParameter[ S3, value -> 0, constant -> False];
addParameter[ S4, value -> 0, constant -> False];
addParameter[ k1, value -> 0.7];
addParameter[ k2, value -> 0.5];
addParameter[ k3, value -> 1.0];
addRule[ type->RateRule, variable -> S1, math -> -k1*S1];
addRule[ type->RateRule, variable -> S2, math -> k1*S1-k2*S2];
addRule[ type->RateRule, variable -> S3, math -> k2*S2-k3*S3];
addRule[ type->RateRule, variable -> S4, math -> k3*S3];

makemodel[]
