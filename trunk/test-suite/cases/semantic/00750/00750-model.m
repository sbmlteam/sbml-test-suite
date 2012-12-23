(* 

category:      Test
synopsis:      Basic single forward reaction with three species in one
compartment using an assignmentRule to vary one species.
componentTags: Compartment, Species, Reaction, Parameter, AssignmentRule, EventNoDelay  
testTags:      Concentration, NonUnityCompartment, InitialValueReassigned
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There are three
species called S1, S2 and S3 and two parameters called k1 and k2.  The model
contains one reaction defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $C * k2 * S1$  |]

The model contains one rule which assigns value to species S3:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | S3 | $k1 * S2$  |]

In this case the initial value declared for species S3 is inconsistent with
the value calculated by the assignmentRule.  Note that since this
assignmentRule must always remain true, it should be considered during
simulation.

The model contains one event that assigns a value to species S2:

[{width:30em,margin: 1em auto}| | *Trigger*    | *Delay* | *Assignments* |
 | Event1 | $S1 < 0.1$ | $-$   | $S2 = 1$    |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*         |*Units*  |
|Initial concentration of S1                |$            1$ |mole litre^-1^                      |
|Initial concentration of S2                |$            0$ |mole litre^-1^                      |
|Initial concentration of S3                |$        3.75$ |mole litre^-1^                      |
|Value of parameter k1               |$         0.75$ |dimensionless |
|Value of parameter k2               |$            5$ |second^-1^ |
|Volume of compartment C |$            2.5$ |litre                     |]

*)

newcase[ "00750" ];

addCompartment[ C, size -> 7.5];
addSpecies[ S1, initialConcentration->1 ];
addSpecies[ S2, initialConcentration -> 0.5 ];
addSpecies[ S3, initialConcentration -> 3.75];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 5 ];
addRule[ type->AssignmentRule, variable -> S3, math ->k1 * S2];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> C * k2 * S1];
addEvent[ trigger -> S1 < 0.1, eventAssignment -> S2->1 ];

makemodel[]
