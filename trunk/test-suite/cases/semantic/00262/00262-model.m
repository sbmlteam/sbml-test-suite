(* 

category:      Test
synopsis:      Basic two reactions involving two species in one
               0 dimensional compartment, with non-unity stoichiometries.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      Amount, NonUnityStoichiometry, 0D-Compartment
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called "compartment".  There are two
species named S1 and S2 and two parameters named k1 and k2.  Compartment
"compartment" is 0-dimensional.  The model contains two reactions defined
as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> 2 S2 | $k1 * S1$    |
| 2 S2 -> S1 | $k2 * S2 * S2$ |]

Note the stoichiometry of S2 is 2 in both reactions.

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|      |*Value*          |*Units*  |
|Initial amount of S1  |$1.5 \x 10^-4$ |mole                      |
|Initial amount of S2  |$            0$ |mole                      |
|Value of parameter k1 |$         0.35$ |second^-1^ |
|Value of parameter k2 |$          180$ |mole^-1^ second^-1^ |]

In this example the compartment has its spatialDimensions attribute set to
zero; i.e., it represents a point and therefore cannot have size or units.
The species values must be treated as amounts and not concentrations.

*)

newcase[ "00262" ];

addCompartment[ compartment, spatialDimensions -> 0];
addSpecies[ S1, initialAmount -> 1.5 10^-4 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 0.35 ];
addParameter[ k2, value -> 180 ];
addReaction[ S1 -> 2 S2, reversible -> False,
	     kineticLaw -> k1 * S1 ];
addReaction[ 2 S2 -> S1, reversible -> False,
	     kineticLaw -> k2 * S2 * S2 ];

makemodel[]
