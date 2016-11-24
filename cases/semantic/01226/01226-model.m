(*

category:        Test
synopsis:        A simple reaction whose identifier is used in a rate rule.
componentTags:   Compartment, Parameter, RateRule, Reaction, Species
testTags:        Amount, NonConstantParameter
testType:        TimeCourse
levels:          2.1, 2.2, 2.3, 2.4, 2.5, 3.1
generatedBy:     Analytic
packagesPresent: 

This test was written to check using the identifier of a reaction (J0) in a rate rule.

The model contains:
* 1 species (S1)
* 1 parameter (p1)
* 1 compartment (c)

There is one reaction:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| J0: -> S1 | $1$ |]


There is one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | p1 | $J0$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial concentration of species S1 | $0$ | variable |
| Initial value of parameter p1 | $0$ | variable |
| Initial volume of compartment 'c' | $1$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
