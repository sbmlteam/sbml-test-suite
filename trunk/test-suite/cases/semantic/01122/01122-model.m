(*

category:      Test
synopsis:      Test of proper interpretation of a boundary species set hasOnlySubstanceUnits=false in a reaction rate.
componentTags: Compartment, RateRule, Reaction, Species
testTags:      Amount, BoundaryCondition, NonConstantCompartment, NonUnityCompartment
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

 This model is a further simplification of test 1000/1121 that only test the use of a boundary species, used in the model in terms of concentrations, from a compartment that varies in time, used in a reaction rate, with hasOnlySubstanceUnits = false.

The model contains:
* 2 species (S1, S3)
* 1 compartment (comp)

There is one reaction:

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| S1 -> | $S3 / 10$ |]


There is one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | comp | $1$ |]

The initial conditions are as follows:

[{width:35em,margin-left:5em}|       | *Value* | *Constant* |
| Initial amount of species S1 | $1$ | variable |
| Initial amount of species S3 | $4$ | variable |
| Initial volume of compartment 'comp' | $1$ | variable |]

The species' initial quantities are given in terms of substance units to
make it easier to use the model in a discrete stochastic simulator, but
their symbols represent their values in concentration units where they
appear in expressions.

*)
