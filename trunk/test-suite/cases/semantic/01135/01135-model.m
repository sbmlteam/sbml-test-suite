(*

category:         Test
synopsis:         Hierarchical model with chained replacedBys.
componentTags:    Parameter, comp:ModelDefinition, comp:ReplacedBy, comp:Submodel
testTags:         
testType:         TimeCourse
levels:           3.1
generatedBy:      Analytic
requiredPackages: comp

 The parameter in the parent model is replaced by a parameter in the submodel, which in turn is replaced by a parameter in *its* submodel.

The 'flattened' version of this hierarchical model contains:
* 1 parameter (p2)
The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial value of parameter p2 | $8$ | variable |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
