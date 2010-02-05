(* 

category:      Test
synopsis:      Two reactions with five species in one
compartment using an assignmentRules with functionDefinitions to vary two species.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition, AssignmentRule 
testTags:      InitialConcentration, NonUnityCompartment
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There are five
species called S1, S2, S3, S4 and S5 and three parameters called k1, k2 and
p1.  The model contains two reactions defined as:

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| S1 -> S3 | $k1 * S1 * C$  |
| S3 -> S2 | $k2 * S5 * C$  |]

The model contains two rules:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | S4 | $func1(S3, p1)$  |
 | Assignment | S5 | $multiply(S4, p1)$  |]

The model contains two functionDefinitions defined as:

[{width:30em,margin-left:5em}|  *Id*  |  *Arguments*  |  *Formula*  |
 | multiply | x, y | $x * y$ |
 | func1 | x, y | $x/(1 + y)$ |]

The initial conditions are as follows:

[{width:30em,margin-left:5em}|       |*Value*          |*Units*  |
|Initial concentration of S1                |$          1.0$ |mole litre^-1^                      |
|Initial concentration of S2                |$            0$ |mole litre^-1^                      |
|Initial concentration of S3                |$            0$ |mole litre^-1^                      |
|Initial concentration of S4                |$            0$ |mole litre^-1^                      |
|Initial concentration of S5                |$            0$ |mole litre^-1^                      |
|Value of parameter k1               |$          0.1$ |second^-1^ |
|Value of parameter k2               |$         0.15$ |second^-1^ |
|Value of parameter p1               |$          2.5$ |dimensionless |
|Volume of compartment C |$            2.5$ |litre                     |]

*)

newcase[ "00739" ];

addFunction[ multiply, arguments -> {x, y}, math -> x * y];
addFunction[ func1, arguments -> {x, y}, math -> x/(1 + y)];
addCompartment[ C, size -> 2.5];
addSpecies[ S1, initialConcentration -> 1.0];
addSpecies[ S2, initialConcentration -> 0];
addSpecies[ S3, initialConcentration -> 0];
addSpecies[ S4, initialConcentration -> 0];
addSpecies[ S5, initialConcentration -> 0];
addParameter[ k1, value -> .1];
addParameter[ k2, value -> .15];
addParameter[ p1, value -> 2.5];
addRule[ type->AssignmentRule, variable -> S4, math ->func1[S3, p1]];
addRule[ type->AssignmentRule, variable -> S5, math ->multiply[S4, p1]];
addReaction[ S1 -> S3, reversible -> False,
	     kineticLaw -> k1 * S1 * C ];
addReaction[ S3 -> S2, modifiers-> S5, reversible -> False,
	     kineticLaw -> k2 * S5 * C ];

makemodel[]
