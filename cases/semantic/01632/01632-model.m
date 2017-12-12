(*

category:        Test
synopsis:        Assigned stoichiometries, both constant and variable.
componentTags:   Compartment, Parameter, RateRule, Reaction, Species, StoichiometryMath
testTags:        Amount, AssignedConstantStoichiometry, AssignedVariableStoichiometry, NonConstantParameter, NonUnityStoichiometry
testType:        TimeCourse
levels:          2.2, 2.3, 2.4, 2.5
generatedBy:     Analytic
packagesPresent: 

 The model has two reactions that affect one species; one with an assigned constant stoichiometry, and one with an assigned variable stoichiometry.  (L2 version of 01631, using stoichiometry math.)

The model contains:
* 1 species (S1)
* 1 parameter (parameterId_0)
* 1 compartment (C)
* 2 species references (S1_degrade, S1_create)

There are 2 reactions:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| J0: S1_degrade S1 -> | $1$ |
| J1: -> S1_create S1 | $1$ |]
Note:  the following stoichiometries are set separately:  S1_degrade, S1_create


There is one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | parameterId_0 | $1$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial amount of species S1 | $2$ | variable |
| Initial value of parameter parameterId_0 | $1$ | variable |
| Initial volume of compartment 'C' | $1$ | constant |
| Initial value of species reference 'S1_degrade' | $3$ | variable |
| Initial value of species reference 'S1_create' | $parameterId_0$ | variable |]

The species' initial quantities are given in terms of substance units to
make it easier to use the model in a discrete stochastic simulator, but
their symbols represent their values in concentration units where they
appear in expressions.

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
