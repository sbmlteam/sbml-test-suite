(*

category:      Test
synopsis:      Model using rules and parameters with a functionDefinition.
componentTags: Parameter, FunctionDefinition, RateRule 
testTags:      NonConstantParameter
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains three parameters named S1, S2 and k1.

  The model contains two rules:
[|| Type || Variable || Formula |
 || Rate || S1 || $-multiply(k1, S1)$  |
 || Rate || S2 || $multiply(k1, S1)$  |]


The model contains one functionDefinition defined as:
[|| Id      | Arguments || Formula |
 || multiply | x, y || $x*y$ |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|             Value of parameter S1:|| $1.5 \x 10^-15$ || any |
|             Value of parameter S2:|| $1.5 \x 10^-15$ || same as S1 |
|             Value of parameter k1:|| $            1$ || second^-1^ |]


*)

newcase[ "00171" ];

addFunction[ multiply, arguments -> {x, y}, math -> x*y];
addParameter[ S1, value -> 1.5 10^-15, constant -> False ];
addParameter[ S2, value -> 1.5 10^-15, constant -> False ];
addParameter[ k1, value -> 1 ];
addRule[ type->RateRule, variable -> S1, math -> -multiply[k1,S1]];
addRule[ type->RateRule, variable -> S2, math -> multiply[k1,S1]];

makemodel[]
