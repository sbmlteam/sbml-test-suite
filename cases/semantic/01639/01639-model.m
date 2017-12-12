(*

category:        Test
synopsis:        A species reference for a boundary species that has the same ID as a local parameter.
componentTags:   Compartment, Reaction, Species
testTags:        Amount, BoundaryCondition, LocalParameters
testType:        TimeCourse
levels:          3.1
generatedBy:     Analytic
packagesPresent: 

 In this model, a local parameter has the same ID as a species reference for a boundary species (but another .

The model contains:
* 2 species (S1, S2)
* 1 local parameter (J0.k0)
* 1 compartment (C)

There is one reaction:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| J0: S1 -> S2 | $1 / k0$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial amount of species S1 | $2$ | variable |
| Initial amount of species S2 | $0$ | variable |
| Initial value of local parameter 'J0.k0' | $3$ | constant |
| Initial volume of compartment 'C' | $1$ | constant |]

The species' initial quantities are given in terms of substance units to
make it easier to use the model in a discrete stochastic simulator, but
their symbols represent their values in concentration units where they
appear in expressions.

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
