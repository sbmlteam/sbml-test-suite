(*

category:        Test
synopsis:        A hierarchical model with a replaced rate rule.
componentTags:   Parameter, RateRule, comp:ModelDefinition, comp:ReplacedElement, comp:Submodel
testTags:        NonConstantParameter, comp:SubmodelOutput, comp:NotRequired
testType:        TimeCourse
levels:          3.1
generatedBy:     Analytic
packagesPresent: comp

 This model contains a rate rule in the parent that replaces a rate rule in the submodel.

The 'flattened' version of this hierarchical model contains:
* 1 parameter (p8)

There is one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | p8 | $3$ |]

The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial value of parameter p8 | $3$ | variable |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
