(*

category:         Test
synopsis:         A hierarchical model with a submodel initial assignment and rate rule.
componentTags:    InitialAssignment, Parameter, RateRule, comp:ModelDefinition, comp:Port, comp:ReplacedElement, comp:Submodel
testTags:         InitialValueReassigned, NonConstantParameter, comp:SubmodelOutput
testType:         TimeCourse
levels:           3.1
generatedBy:      Analytic
requiredPackages: comp

{Write general description of why you have created the model here.}

The 'flattened' version of this hierarchical model contains:
* 2 parameters (param1, submod1__subparam2)

There is one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | param1 | $8 * submod1__subparam2 + param1$ |]

The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial value of parameter submod1__subparam2 | $6$ | constant |
| Initial value of parameter param1 | $8 * submod1__subparam2 + param1$ | variable |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
