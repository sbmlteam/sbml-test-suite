(* 

category:      Test
synopsis:      Model using parameters and rules only with
               one event that assigns value to a parameter, subject to a delay.
componentTags: Parameter, RateRule, EventWithDelay 
testTags:      NonConstantParameter
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains five parameters called S1, S2, S3, k1 and k2.
The model contains three rules defined as:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
 | Rate | S1  | $k2 * S3 - k1 * S1 * S2$  |
 | Rate | S2  | $k2 * S3 - k1 * S1 * S2$  |
 | Rate | S3  | $k1 * S1 * S2 - k2 * S3$  |]

The model contains one event that assigns value to parameter S2 defined as:

[{width:30em,margin-left:5em}| | *Trigger*   | *Delay* | *Assignments* |
 | Event1                      | $S1 < 0.75$ | $1.1$   | $S2 = 1$      |]

The initial conditions are as follows:

[{width:30em,margin-left:5em}|      |*Value*          |*Units*  |
|Initial amount of S1  |$1.0$  |any                      |
|Initial amount of S2  |$2.0$  |same as S1                      |
|Initial amount of S3  |$1.0$  |same as S1                      |
|Value of parameter k1 |$0.75$           |(unit of S1)^-1^ second^-1^ |
|Value of parameter k2 |$0.25$           |second^-1^                |]


*)

newcase[ "00453" ];

addParameter[ S1, value -> 1.0, constant -> False ];
addParameter[ S2, value -> 2.0, constant -> False ];
addParameter[ S3, value -> 1.0, constant -> False ];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addRule[ type->RateRule, variable -> S1, math -> k2 * S3 - k1 * S1 * S2];
addRule[ type->RateRule, variable -> S2, math -> k2 * S3 - k1 * S1 * S2];
addRule[ type->RateRule, variable -> S3, math -> k1 * S1 * S2 - k2 * S3];
addEvent[ trigger -> S1 < 0.75, delay->1.1, eventAssignment -> S2->1 ];

makemodel[]
