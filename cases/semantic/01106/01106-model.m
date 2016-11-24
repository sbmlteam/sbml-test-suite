(*

category:      Test
synopsis:      A simple reaction with stoichiometry set to a parameter that varies from an Event.
componentTags: AssignmentRule, Compartment, EventNoDelay, Parameter, Reaction, Species
testTags:      Amount, AssignedVariableStoichiometry, NonUnityStoichiometry, NonConstantParameter, InitialValueReassigned
testType:      TimeCourse
levels:        3.1
generatedBy:   Numeric

Note:  This test is the L3 version of model 991.

The stoichiometry of the reaction '-> nX' is set to be equal to p1, a parameter that starts at 1 and switches to 2 due to an event.

The model contains:
* 1 species (X)
* 2 parameters (p1, k1)
* 1 compartment (default_compartment)

There is one reaction:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| -> Xref X | $k1$ |]
Note:  the following stoichiometries are set separately:  Xref


There is one event:

[{width:30em,margin: 1em auto}|  *Event*  |  *Trigger*  | *Event Assignments* |
| _E0 | $X &geq; 2$ | $p1 = 2$ |]


There is one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Assignment | Xref | $p1$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial concentration of species X | $1$ | variable |
| Initial value of parameter k1 | $1$ | constant |
| Initial value of parameter p1 | $1$ | variable |
| Initial volume of compartment 'default_compartment' | $1$ | constant |]

*)
