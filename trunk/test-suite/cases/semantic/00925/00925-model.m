(* 

category:      Test
synopsis:      Model with parameters and assignmentRule only.
componentTags: Parameter, AssignmentRule 
testTags:      
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

The model contains two constant
parameters called k1 and k2.


The model contains one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| assignmentRule | k1 | $k2 * 3$  |]


Note: AssignmentRules override any declared initial values.  In this
case, the initial value of k1 is consistent with the value 
calculated using the assignmentRule.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|      |*Value*          |*Units*  |
|Value of parameter k1 |$   0.6$ |any |
|Value of parameter k2 |$0.2$ |same as k1 |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.


*)

newcase[ "00925" ];

addParameter[ k1, constant -> False, value->0.6 ];
addParameter[ k2, value->0.2 ];
addRule[ type -> assignmentRule, variable -> k1, math -> k2 * 3];

makemodel[]
