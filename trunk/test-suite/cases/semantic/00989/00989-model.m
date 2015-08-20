(*

category:      Test
synopsis:      A simple reaction with stoichiometry the same as its species.  For convenience, another reaction is present with the same kinetics, as the species is used in the kinetic law instead.
componentTags: StoichiometryMath, Compartment, Parameter, Reaction, Species
testTags:      Amount, AssignedVariableStoichiometry, HasOnlySubstanceUnits, NonUnityStoichiometry
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 2.5
generatedBy:   Numeric

Note:  earlier versions of the test suite contained a 3.1 version of this test.
That model was moved to its own test, because it did not have the 'StoichiometryMath'
component.

This model contains two reactions that do the same thing, one by having a kinetic law that varies with the produced species, and the other by having a variable stoichiometry set to equal the produced species, for a sort of auto-catalytic reaction.
 

The model contains:
* 2 species (X, Y)
* 1 parameter (k1)
* 1 compartment (default_compartment)

There are 2 reactions:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| -> Xref X | $k1$ |
| -> Y | $k1 * Y$ |]
Note:  the following stoichiometries are set separately:  Xref


There is one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Assignment | Xref | $X$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial amount of species X | $1$ | variable |
| Initial amount of species Y | $1$ | variable |
| Initial value of parameter k1 | $1$ | constant |
| Initial volume of compartment 'default_compartment' | $1$ | constant |]

*)
