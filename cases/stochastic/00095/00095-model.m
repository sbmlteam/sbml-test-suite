(*

category:        Test
synopsis:        Gamma distribution test
componentTags:   Compartment, EventNoDelay, Parameter, RateRule, Species
testTags:        Amount, HasOnlySubstanceUnits, NonConstantParameter
testType:        StatisticalDistribution
levels:          3.1, 3.2
generatedBy:     Analytic
packagesPresent: distrib

Test of the gamma distribution function from the 'distrib' package, with variable shape and constant scale.

The model contains:
* 1 species (X)
* 2 parameters (t, shape)
* 1 compartment (C)

There is one event:

[{width:30em,margin: 1em auto}|  *Event*  |  *Trigger*  | *Event Assignments* |
| E0 | $t >= 0.5$ | $X = gamma(shape, 2)$ |
|  |  | $t = -0.5$ |]


There is one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | t | $1$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial amount of species X | $0$ | variable |
| Initial value of parameter shape | $1$ | constant |
| Initial value of parameter t | $0$ | variable |
| Initial volume of compartment 'C' | $1$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
