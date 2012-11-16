(*

category:         Test
synopsis:         A simple hierarchical model with a submodel parameter
componentTags:    Parameter, comp:ModelDefinition, comp:Port, comp:ReplacedElement, comp:Submodel
testTags:         comp:SubmodelOutput
testType:         TimeCourse
levels:           3.1
requiredPackages: comp
generatedBy:      Analytic||Numeric

 This model contains a submodel with two parameters, one of which is replaced by the parameter in the containing model.

The 'flattened' version of this hierarchical model contains:
* 2 parameters (param1, submod1__subparam2)
The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial value of parameter param1 | $10.42$ | constant |
| Initial value of parameter submod1__subparam2 | $6$ | constant |]

{Keep this next line if 'generatedBy' is 'Analytic':}
Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
