(*

category:        Test
synopsis:        A simple reaction whose identifier is used in an initial assignment.
componentTags:   Compartment, InitialAssignment, Parameter, Reaction, Species
testTags:        Amount, InitialValueReassigned
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 2.5, 3.1
generatedBy:     Analytic
packagesPresent: 

This test was written to check using the identifier of a reaction (J0) in an initial assignment.

The model contains:
* 1 species (S1)
* 1 parameter (p1)
* 1 compartment (c)

There is one reaction:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| J0: -> S1 | $1$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial concentration of species S1 | $0$ | variable |
| Initial value of parameter p1 | $J0$ | variable |
| Initial volume of compartment 'c' | $1$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
