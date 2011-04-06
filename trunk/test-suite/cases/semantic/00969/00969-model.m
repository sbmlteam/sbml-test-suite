(* 

category:      Test
synopsis:      A very simple reaction with stoichiometry set by an initialAssignment.
componentTags: Parameter, Species, InitialAssignment
testTags:      Amount, SpeciesReferenceMath
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

 This model contains a single rule:
[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
|  -> nX | $k1$  |]

 Where 'n' is a speciesReference (actually 'Xref') with an initialAssignment of 3.  A 'stoichiometry' value is provided in the speciesReference in the reaction itself of '1'.  This should be replaced by the value in the initialAssignment.
 
The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Initial amount of X        |$0$                  |
|Value of parameter k1       |$1$          |
|Volume of compartment default_compartment     |$1$             |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.
*)
