(* 

category:      Test
synopsis:      Basic two reactions with three species in one compartment 
               of non-unity size using a functionDefinition.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition 
testTags:      InitialConcentration, NonUnityCompartment
testType:      TimeCourse
levels:        2.1, 2.2, 2.3
generatedBy:   Numeric

The model contains one compartment called C.
There are three species called S1, S2 and S3 and two parameters called k1 and k2.
The model contains two reactions defined as:

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 | $multiply(k1, S1, S2) * C$  |
| S3 -> S1 + S2 | $k2 * S3 * C$  |]


The model contains one functionDefinition defined as:

[{width:30em,margin-left:5em}|  *Id*  |  *Arguments*  |  *Formula*  |
 | multiply | x, y, z | $x * y * z$ |]


The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*          |*Units*  |
|Initial concentration of S1        | $1.0 \x 10^-15$ | mole litre^-1^                      |
|Initial concentration of S2        | $2.0 \x 10^-15$ | mole litre^-1^                      |
|Initial concentration of S3        | $1.0 \x 10^-15$ | mole litre^-1^                      |
|Value of parameter k1       | $         0.75$ | litre mole^-1^ second^-1^ |
|Value of parameter k2       | $         0.25$ | second^-1^ |
|Volume of compartment C:| $          2.3$ | litre                     |]

*)

newcase[ "00739" ];

addFunction[ multiply, arguments -> {x, y, z}, math -> x * y * z];
addCompartment[ C, size -> 2.3 ];
addSpecies[ S1, initialConcentration -> 1.0 10^-15];
addSpecies[ S2, initialConcentration -> 2.0 10^-15];
addSpecies[ S3, initialConcentration -> 1.0 10^-15];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> multiply[k1, S1, S2] * C ];
addReaction[ S3 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * C ];

makemodel[]
