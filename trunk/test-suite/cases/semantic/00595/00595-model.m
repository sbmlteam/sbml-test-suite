(* 

category:      Test
synopsis:      Basic two reactions with three species and parameters local to 
               reactions.
componentTags: Compartment, Species, Reaction 
testTags:      Concentration, LocalParameters
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called C.  There are three
species called S1, S2 and S3.  The model contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k * S1 * C,$  |
| S2 -> S3 | $k * S2 * C,$  |]

Reaction S1 -> S2 defines one local parameter k.  Reaction S2 -> S3 also
defines one (different) local parameter k.  Both of these parameters have a
scope local to the defining reaction.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value*         |*Units*  |
|Initial concentration of S1   |$3.0 \x 10^-3$ |mole litre^-1^                      |
|Initial concentration of S2   |$            0$ |mole litre^-1^                      |
|Initial concentration of S3   |$            0$ |mole litre^-1^                      |
|Value of local parameter k    |$            1$ |second^-1^ |
|Value of local parameter k    |$            2$ |second^-1^ |
|Volume of compartment C       |$            1$ |litre                     |]

*)

newcase[ "00595" ];

addCompartment[ C, size -> 1 ];
addSpecies[ S1, initialConcentration -> 3.0 10^-3];
addSpecies[ S2, initialConcentration -> 0];
addSpecies[ S3, initialConcentration -> 0];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k * S1 * C, parameters -> {k -> 1} ];
addReaction[ S2 -> S3, reversible -> False,
	     kineticLaw -> k * S2 * C, parameters -> {k -> 2} ];

makemodel[]
