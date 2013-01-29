(*

category:        Test
synopsis:        A hierarchical model with a time conversion factor and reaction.
componentTags:   CSymbolTime, Compartment, Parameter, Reaction, Species, comp:ModelDefinition, comp:ReplacedBy, comp:ReplacedElement, comp:Submodel
testTags:        Amount, HasOnlySubstanceUnits, comp:TimeConversionFactor
testType:        TimeCourse
levels:          3.1
generatedBy:     Analytic
packagesPresent: comp

This model contains a submodel with a reaction, contained in a model with a time conversion factor.

The 'flattened' version of this hierarchical model contains:
* 1 species (s1)
* 2 parameters (timeconv, t5)
* 1 compartment (C)

There is one reaction:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| -> s1 | $1 / timeconv * (t5 * (time / timeconv) / (s1 * t5))$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial amount of species s1 | $0.001$ | variable |
| Initial value of parameter timeconv | $60$ | constant |
| Initial value of parameter t5 | $3.1$ | variable |
| Initial volume of compartment 'C' | $1$ | constant |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
