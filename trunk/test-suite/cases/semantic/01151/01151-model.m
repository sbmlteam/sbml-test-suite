(*

category:        Test
synopsis:        A hierarchical model with a deleted rate rule.
componentTags:   Parameter, RateRule, comp:Deletion, comp:ModelDefinition, comp:ReplacedElement, comp:Submodel
testTags:        NonConstantParameter
testType:        TimeCourse
levels:          3.1
generatedBy:     Analytic
packagesPresent: comp

 Flattened, this model is functionally identical to model 01150, but instead of having the parent rate rule replace the submodel rate rule, the submodel rate rule is simply deleted.

The 'flattened' version of this hierarchical model contains:
* 1 parameter (p8)

There is one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | p8 | $3$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter p8 | $8$ | variable |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
