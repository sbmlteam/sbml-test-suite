(* 

category:      Test
synopsis:      A very simple reaction whose stoichiometry is referenced in other math.
componentTags: Parameter, Species, RateRule, InitialAssignment, AssignmentRule
testTags:      InitialAmount, SpeciesReferenceMath
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

 This model contains a single rule:
[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
|  -> nX | $k1$  |]

 Where 'n' is a speciesReference (actually 'Xref'), and has a 'stoichiometry' value of '1'one in the speciesReference in the reaction itself.

 Three other parameters reference this stoichiometry in their own math:  Y has a rateRule of Xref, Z has an assignmentRule of Xref, and Q has an initialAssignment of Xref. Y, Z, and Q all have 'value="0"' in their definitions, to ensure that this is being appropriately overridden.

 The rate rule is:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*         |
 | Rate                                 | Y           | $Xref$  |]

 The assignment rule is:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*         |
 | Rate                                 | Z           | $Xref$  |]

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Initial amount of X        |$0$                  |
|Value of parameter k1       |$1$          |
|Value of parameter Q        |$Xref$          |
|Volume of compartment default_compartment     |$1$             |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.
*)
