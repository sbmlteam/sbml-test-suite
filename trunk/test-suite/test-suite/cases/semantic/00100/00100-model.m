(*

category:      Test
synopsis:      Basic two reactions with three species in one 0D compartment 
               and one functionDefinition.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition 
testTags:      InitialAmount, 0D-Compartment
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are three species named S1, S2 and S3 and two parameters named k1 and k2.
  Compartment compartment is 0-dimensional.
  The model contains two reactions defined as:
[| | Reaction |||||| Rate |
 | | S1+S2 -> S3 |||||| $multiply(k1, S1, S2)*compartment$  |
 | | S3 -> S1+S2 |||||| $k2*S3*compartment$  |]


The model contains one functionDefinition defined as:
[|| Id      | Arguments || Formula |
 || multiply | x, y, z || $x*y*z$ |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $1.0 \x 10^-15$ || mole                      |
|              Initial amount of S2:|| $2.0 \x 10^-15$ || mole                      |
|              Initial amount of S3:|| $1.0 \x 10^-15$ || mole                      |
|             Value of parameter k1:|| $         0.75$ || mole^-1^ second^-1^ |
|             Value of parameter k2:|| $         0.25$ || second^-1^ |]

In this example the compartment has spatialDimensions set to zero,
i.e., it represents a point and therefore cannot have size or units.  The 
species values must be treated as amounts and not concentrations.

*)

newcase[ "00100" ];

addFunction[ multiply, arguments -> {x, y, z}, math -> x*y*z];
addCompartment[ compartment, spatialDimensions-> 0 ];
addSpecies[ S1, initialAmount -> 1.0 10^-15];
addSpecies[ S2, initialAmount -> 2.0 10^-15];
addSpecies[ S3, initialAmount -> 1.0 10^-15];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addReaction[ S1+S2 -> S3, reversible -> False,
	     kineticLaw -> multiply[k1, S1, S2]*compartment ];
addReaction[ S3 -> S1+S2, reversible -> False,
	     kineticLaw -> k2*S3*compartment ];

makemodel[]
