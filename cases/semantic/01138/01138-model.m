(*

category:         Test
synopsis:         [[Write description here.]]
componentTags:    AssignmentRule, Parameter, comp:ModelDefinition, comp:ReplacedElement, comp:Submodel
testTags:         InitialValueReassigned, NonConstantParameter, comp:ConversionFactor
testType:         TimeCourse
levels:           3.1
requiredPackages: comp
generatedBy:      Analytic||Numeric

{Write general description of why you have created the model here.}

The 'flattened' version of this hierarchical model contains:
* 3 parameters (p8, conv, sub1__prel)

There is one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
| Assignment | sub1__prel | $p8 / conv / 8$ |]

The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial value of parameter conv | $0.1$ | constant |
| Initial value of parameter p8 | $8$ | variable |
| Initial value of parameter sub1__prel | $p8 / conv / 8$ | variable |]

{Keep this next line if 'generatedBy' is 'Analytic':}
Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
