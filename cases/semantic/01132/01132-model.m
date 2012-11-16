(*

category:         Test
synopsis:         A hierarchical model with a three-level-deep replacement.
componentTags:    Compartment, Species, comp:ModelDefinition, comp:ReplacedElement, comp:SBaseRef, comp:Submodel
testTags:         Concentration, ConstantSpecies, NonUnityCompartment, comp:SubmodelOutput
testType:         TimeCourse
levels:           3.1
requiredPackages: comp
generatedBy:      Analytic

 This model contains submodels three levels deep, with the top model replacing the species at the deepest level, and the compartment at a level two down.

The 'flattened' version of this hierarchical model contains:
* 7 species (S1, sub1__S1, sub2__S1, sub2__sub1__S1, sub3__S1, sub3__sub1__S1, sub3__sub2__S1)
* 7 compartments (C, sub1__C, sub2__C, sub3__C, sub3__sub1__C, sub3__sub2__C, sub3__sub2__sub1__C)
The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial amount of species S1 | $5$ | constant |
| Initial amount of species sub1__S1 | $7$ | constant |
| Initial amount of species sub2__S1 | $9$ | constant |
| Initial amount of species sub2__sub1__S1 | $7$ | constant |
| Initial amount of species sub3__S1 | $11$ | constant |
| Initial amount of species sub3__sub1__S1 | $7$ | constant |
| Initial amount of species sub3__sub2__S1 | $9$ | constant |
| Initial volume of compartment 'C' | $10$ | constant |
| Initial volume of compartment 'sub1__C' | $1$ | constant |
| Initial volume of compartment 'sub2__C' | $1$ | constant |
| Initial volume of compartment 'sub3__C' | $1$ | constant |
| Initial volume of compartment 'sub3__sub1__C' | $1$ | constant |
| Initial volume of compartment 'sub3__sub2__C' | $1$ | constant |
| Initial volume of compartment 'sub3__sub2__sub1__C' | $1$ | constant |]

The species' initial quantities are given in terms of substance units to
make it easier to use the model in a discrete stochastic simulator, but
their symbols represent their values in concentration units where they
appear in expressions.

{Keep this next line if 'generatedBy' is 'Analytic':}
Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
