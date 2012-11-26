(*

category:         Test
synopsis:         A hierarchical model with a conversion factor.
componentTags:    Parameter, RateRule, comp:ModelDefinition, comp:ReplacedElement, comp:Submodel
testTags:         NonConstantParameter, comp:ConversionFactor
testType:         TimeCourse
levels:           3.1
generatedBy:      Analytic
requiredPackages: comp

This model is a simple test of a replacement with a conversion factor that affects the rate rule of the replaced parameter.

The 'flattened' version of this hierarchical model contains:
* 2 parameters (p8, conv)

There is one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | p8 | $(4 * (p8 / conv) + 3) * conv$ |]

The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial value of parameter conv | $0.1$ | constant |
| Initial value of parameter p8 | $(4 * (p8 / conv) + 3) * conv$ | variable |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
