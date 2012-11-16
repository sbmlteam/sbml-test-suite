(*

category:         Test
synopsis:         [[Write description here.]]
componentTags:    Compartment, Species, comp:ModelDefinition, comp:ReplacedBy, comp:ReplacedElement, comp:SBaseRef, comp:Submodel
testTags:         Amount||Concentration, NonUnityCompartment, comp:SubmodelOutput
testType:         TimeCourse
levels:           3.1
requiredPackages: comp
generatedBy:      Analytic||Numeric

{Write general description of why you have created the model here.}

The 'flattened' version of this hierarchical model contains:
* 6 species (sub1__S1, sub2__S1, S1, sub3__S1, sub3__sub1__S1, sub3__sub2__S1)
* 7 compartments (C, sub1__C, sub2__C, sub3__C, sub3__sub1__C, sub3__sub2__C, sub3__sub2__sub1__C)
The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial concentration of species sub1__S1 | $2.5$ | variable |
| Initial amount of species sub2__S1 | $9$ | variable |
| Initial concentration of species S1 | $2.5$ | variable |
| Initial amount of species sub3__S1 | $11$ | variable |
| Initial concentration of species sub3__sub1__S1 | $2.5$ | variable |
| Initial amount of species sub3__sub2__S1 | $9$ | variable |
| Initial volume of compartment 'C' | $10$ | variable |
| Initial volume of compartment 'sub1__C' | $1$ | variable |
| Initial volume of compartment 'sub2__C' | $1$ | variable |
| Initial volume of compartment 'sub3__C' | $1$ | variable |
| Initial volume of compartment 'sub3__sub1__C' | $1$ | variable |
| Initial volume of compartment 'sub3__sub2__C' | $1$ | variable |
| Initial volume of compartment 'sub3__sub2__sub1__C' | $1$ | variable |]

{Keep this next line if 'generatedBy' is 'Analytic':}
Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
