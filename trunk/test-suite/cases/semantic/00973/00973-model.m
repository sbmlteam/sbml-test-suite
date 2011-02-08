(* 

category:      Test
synopsis:      A very simple reaction with stoichiometry changed by an event.
componentTags: Parameter, Species, RateRule
testTags:      InitialAmount, SpeciesReferenceMath
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

 This model contains a single rule:
[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
|  -> nX | $k1$  |]

 Where 'n' is a speciesReference (actually 'Xref'), and has a 'stoichiometry' value of '1'one in the speciesReference in the reaction itself.

 Over the course of the simulation, Xref changes at a rate of 0.01/time, so the effective rate of synthesis increases as well, identically to the same model with k1 changing instead of Xref.

 The rate rule is:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*         |
 | Rate                                 | Xref           | $0.01$  |]

 
The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Initial amount of X        |$0$                  |
|Value of parameter k1       |$1$          |
|Volume of compartment default_compartment     |$1$             |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.
*)
