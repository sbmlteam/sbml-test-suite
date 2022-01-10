(*

category:        Test
synopsis:        CauchyDistribution test
componentTags:   Compartment, EventNoDelay, Parameter, RateRule, Species
testTags:        Amount, HasOnlySubstanceUnits, NonConstantParameter
testType:        StatisticalDistribution
levels:          3.1, 3.2
generatedBy:     Analytic
packagesPresent: distrib

Test of the CauchyDistribution function from the 'distrib' package. The Cauchy distribution is known for not having a defined mean and variance.  As a result, the median (the location parameter) is used as the expected mean in the results file, and the standard deviation is not requested at all.  The mean and standard deviation could probably be estimated at some point, but are currently unknown.

The model contains:
* 1 species (X)
* 1 parameter (t)
* 1 compartment (C)

There is one event:

[{width:30em,margin: 1em auto}|  *Event*  |  *Trigger*  | *Event Assignments* |
| E0 | $t >= 0.5$ | $X = cauchy(1, 2)$ |
|  |  | $t = -0.5$ |]


There is one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | t | $1$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial amount of species X | $0$ | variable |
| Initial value of parameter t | $0$ | variable |
| Initial volume of compartment 'C' | $1$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
