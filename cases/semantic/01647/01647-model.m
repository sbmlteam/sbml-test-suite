(*

category:        Test
synopsis:        A model conversion factor for substance-only species.
componentTags:   Compartment, Parameter, Reaction, Species
testTags:        Amount, ConversionFactors, HasOnlySubstanceUnits, NonUnityCompartment
testType:        TimeCourse
levels:          3.1, 3.2
generatedBy:     Analytic
packagesPresent: 

 This model has a model conversion factor for species set to be substance-only (hasOnlySubstanceUnits=true).

The model contains:
* 2 species (S1, S2)
* 1 parameter (m_cf)
* 1 compartment (C)

There is one reaction:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| J0: S1 -> S2 | $0.01$ |]

The conversion factor for the model is 'm_cf'

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial amount of species S1 | $2$ | variable |
| Initial amount of species S2 | $3$ | variable |
| Initial value of parameter m_cf | $3$ | constant |
| Initial volume of compartment 'C' | $2$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
