(*

category:        Test
synopsis:        A species and a compartment both under control of a rate rule.
componentTags:   AssignmentRule, Compartment, Parameter, RateRule, Species
testTags:        Amount, BoundaryCondition, InitialValueReassigned, NonConstantParameter, NonUnityCompartment
testType:        TimeCourse
levels:          1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:     Numeric
packagesPresent: 

 A species and its compartment are both under control of rate rules, and the species is set 'hasOnlySubstanceUnits=false', meaning that the species concentration is affected by both rates of change.  That value then gets assigned to the parameter 'x', since we typically only require Species output in amounts.

The model contains:
* 1 species (S1)
* 1 parameter (x)
* 1 compartment (C1)

There are 2 rules:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | S1 | $0.4$ |
| Assignment | x | $S1$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial concentration of species S1 | $0$ | variable |
| Initial value of parameter x | $S1$ | variable |
| Initial volume of compartment 'C1' | $0.5$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
