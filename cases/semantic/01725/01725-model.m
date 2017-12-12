(*

category:        Test
synopsis:        Species conversion factor affecting a reaction with assigned stoichiometries.
componentTags:   Compartment, InitialAssignment, Parameter, Reaction, Species
testTags:        Amount, AssignedConstantStoichiometry, ConversionFactors, InitialValueReassigned, NonUnityStoichiometry
testType:        TimeCourse
levels:          3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

 In this model, a species conversion factor affects a reaction with assigned stoichiometries, changing the levels of S1 and S2.

The model contains:
* 2 species (S1, S2)
* 1 parameter (s1_cf)
* 1 compartment (C)
* 2 species references (S1_stoich, S2_stoich)

There is one reaction:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| J0: S1_stoich S1 -> S2_stoich S2 | $0.01$ |]
Note:  the following stoichiometries are set separately:  S1_stoich, S2_stoich

The conversion factor for the species 'S1' is 's1_cf'

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial amount of species S1 | $2$ | variable |
| Initial amount of species S2 | $3$ | variable |
| Initial value of parameter s1_cf | $5$ | constant |
| Initial volume of compartment 'C' | $1$ | constant |
| Initial value of species reference 'S1_stoich' | $1.5$ | constant |
| Initial value of species reference 'S2_stoich' | $1.5$ | constant |]

The species' initial quantities are given in terms of substance units to
make it easier to use the model in a discrete stochastic simulator, but
their symbols represent their values in concentration units where they
appear in expressions.

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
