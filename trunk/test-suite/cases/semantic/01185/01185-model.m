(*

category:        Test
synopsis:        A kinetic law set by a constant with an initial assignment.
componentTags:   Compartment, InitialAssignment, Parameter, Reaction, Species
testTags:        Amount, InitialValueReassigned
testType:        TimeCourse
levels:          2.2, 2.3, 2.4, 3.1
generatedBy:     Numeric
packagesPresent: 

 In some software packages, constant kinetic laws are pre-processed to a single value.  This tests to ensure that if a kinetic law is constant but depends on an initial assignment to a variable, this processing is still accurate, even when that value is given an (overridden) 'value' attribute.

The model contains:
* 1 species (S1)
* 2 parameters (k1, y)
* 1 compartment (default_compartment)

There is one reaction:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| -> S1 | $k1$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial amount of species S1 | $1$ | variable |
| Initial value of parameter k1 | $y$ | constant |
| Initial value of parameter y | $3$ | constant |
| Initial volume of compartment 'default_compartment' | $1$ | constant |]

*)
