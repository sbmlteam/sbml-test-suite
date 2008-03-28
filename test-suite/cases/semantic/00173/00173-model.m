(*

category:      Test
synopsis:      Model using rules and parameters with a rate that causes a discontinuity
               in the output.
componentTags: Parameter, RateRule 
testTags:      NonConstantParameter, Discontinuity
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains four parameters named S1, S2, p1 and p2.

  The model contains two rules:
[|| Type || Variable || Formula |
 || Rate || S1 || $-Ceiling(S1*p1)!/p2$  |
 || Rate || S2 || $Ceiling(S1*p1)!/p2$  |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|             Value of parameter S1:|| $          1.0$ || any |
|             Value of parameter S2:|| $          0.0$ || same as S1 |
|             Value of parameter p1:|| $             $ || (units of S1)^-1^ |
|             Value of parameter p2:|| $            2$ || second (units of S1)^-1^ |]


*)

newcase[ "00173" ];

addParameter[ S1, value -> 1.0, constant -> False ];
addParameter[ S2, value -> 0.0, constant -> False ];
addParameter[ p1, value -> 4];
addParameter[ p2, value -> 25];
addRule[ type->RateRule, variable -> S1, math -> -Ceiling[S1*p1]!/p2];
addRule[ type->RateRule, variable -> S2, math -> Ceiling[S1*p1]!/p2];

makemodel[]
