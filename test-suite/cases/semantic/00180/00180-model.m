(*

category:      Test
synopsis:      Model with parameters and rules with an initialAssignment.
componentTags: Parameter, InitialAssignment, RateRule 
testTags:      NonConstantParameter
testtype:      TimeCourse
levels:        2.2, 2.3

The model contains four parameters named S1, S2, k1 and k2.
  The model contains two rules:
[|| Type || Variable || Formula |
 || Rate || S1       || $-k2*S1 $  |
 || Rate || S2       || $k2*S1 $  |]

  The model contains one initialAssignment:
[|| Variable || Formula |
 || S1 || $k1*S2$  |]

  Note: InitialAssignments override any declared initial values.  In this case the initial
value of S1 has not been explicitly declared and must be calculated using the initialAssignment.


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|             Value of parameter S1:|| $   undeclared$ || any |
|             Value of parameter S2:|| $2.0 \x 10^-15$ || same as S1 |
|             Value of parameter k1:|| $         0.75$ || dimensionless |
|             Value of parameter k2:|| $         0.25$ || second^-1^ |]


*)

newcase[ "00180" ];

addParameter[ S1, constant -> False ];
addParameter[ S2, value -> 2.0 10^-15, constant -> False ];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addInitialAssignment[ S1, math -> k1*S2];
addRule[ type->RateRule, variable -> S1, math -> -k2*S1 ];
addRule[ type->RateRule, variable -> S2, math -> k2*S1 ];

makemodel[]