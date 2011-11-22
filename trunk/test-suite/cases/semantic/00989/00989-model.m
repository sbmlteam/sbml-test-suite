(* 

category:      Test
 synopsis:      A simple reaction with stoichiometry the same as its species.  For convenience, another reaction is present with the same kinetics, as the species is used in the kinetic law instead.
componentTags: Parameter, Species, Compartment
testTags:      Amount, AssignedVariableStoichiometry
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

 This model contains two rules that do the same thing:
[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
|  -> nX | $k1$  |
|  -> Y | $Y*k1$  |]

  Where 'n' is a speciesReference (actually 'Xref') that is set equal to X itself (or StoichiometryMath that does the same thing for level 2 models), for a sort of auto-catalytic reaction.  A parallel reaction is provided that will mechanically do exactly the same thing though the kinetic law, for error-checking purposes.
 
The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Initial amount of X        |$1$                  |
|Initial amount of Y        |$1$                  |
|Value of parameter k1       |$1$          |
|Volume of compartment default_compartment     |$1$             |]

*)
