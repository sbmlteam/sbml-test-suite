(* 

category:      Test
synopsis:      Model varying compartment using rules only.
componentTags: Compartment, RateRule, InitialAssignment 
testTags:      NonConstantCompartment, NonUnityCompartment, InitialValueReassigned
testType:      TimeCourse
levels:        2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

The model contains one varying 2-dimensional compartment called c.

The model contains one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*         |
 | Rate                                 | c           | $0.5 * c$  |]

The model contains one initialAssignment:

[{width:30em,margin-left:5em}| Variable | Formula |
 | c       | $3/2$  |]

Note: SBML's InitialAssignment construct override any declared initial
values.  In this model, the initial value for the compartment c has not been
explicitly declared and must be calculated using the InitialAssignment.


The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*         |*Units*  |
|Size of compartment c         |$undeclared$ |metre^2^        |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.


*)

newcase[ "00916" ];

addCompartment[ c, constant -> False, spatialDimensions -> 2 ];
addInitialAssignment[ c, math -> 2/3];
addRule[ type->RateRule, variable -> c, math -> 0.5 * c];

makemodel[]
