(*

category:         Test
synopsis:         [[Write description here.]]
componentTags:    Compartment, Species, comp:ModelDefinition, comp:ReplacedElement, comp:SBaseRef, comp:Submodel
testTags:         Amount||Concentration, NonUnityCompartment
testType:         TimeCourse
levels:           3.1
requiredPackages: comp
generatedBy:      Analytic||Numeric

{Write general description of why you have created the model here.}

The 'flattened' version of this hierarchical model contains:
* 4 species (S1, sub1__S1, sub2__S1, sub2__sub1__S1)
* 3 compartments (C, sub1__C, sub2__C)
The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial amount of species S1 | $5$ | variable |
| Initial amount of species sub1__S1 | $7$ | variable |
| Initial amount of species sub2__S1 | $9$ | variable |
| Initial amount of species sub2__sub1__S1 | $7$ | variable |
| Initial volume of compartment 'C' | $10$ | variable |
| Initial volume of compartment 'sub1__C' | $1$ | variable |
| Initial volume of compartment 'sub2__C' | $1$ | variable |]

The species' initial quantities are given in terms of substance units to
make it easier to use the model in a discrete stochastic simulator, but
their symbols represent their values in concentration units where they
appear in expressions.

{Keep this next line if 'generatedBy' is 'Analytic':}
Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
