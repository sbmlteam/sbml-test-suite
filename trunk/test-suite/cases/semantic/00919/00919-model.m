(* 

category:      Test
synopsis:      Model varying compartment using rules only.
componentTags: Compartment, RateRule, InitialAssignment 
testTags:      NonConstantCompartment, NonUnityCompartment, InitialValueReassigned
testType:      TimeCourse
levels:        2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

The model contains one varying 1-dimensional compartment called c.

The model contains one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*         |
 | Rate                                 | c           | $1.25 * c$  |]

The model contains one initialAssignment:

[{width:30em,margin: 1em auto}| Variable | Formula |
 | c       | $3 / 2$  |]

Note: SBML's InitialAssignment construct override any declared initial
values.  In this model, the initial value for the compartment c is
inconsistent with the value returned by the InitialAssignment.


The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value*         |*Units*  |
|Size of compartment c         |$2$ |metre        |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.


*)

newcase[ "00919" ];

addCompartment[ c, constant -> False, size -> 2, spatialDimensions -> 1 ];
addInitialAssignment[ c, math -> 2.1/4];
addRule[ type->RateRule, variable -> c, math -> 1.25 * c];

makemodel[]
