(* 

category:      Test
synopsis:      Basic single forward reaction with two species in a 2-dimensional
compartment, where the species have only substance units.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      Amount, HasOnlySubstanceUnits
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called "compartment".  There are two
species named S1 and S2 and one parameter named k1.  Compartment
"compartment" is two-dimensional.  Species S1 and S2 are declared to have
only substance units.  The model contains one reaction defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k1 * S1$  |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|     |*Value*         |*Units*  |
|Initial amount of S1              |$1.5 \x 10^-4$ |mole                      |
|Initial amount of S2              |$  1 \x 10^-4$ |mole                      |
|Value of parameter k1             |$            1$ |second^-1^ |
|Area of compartment "compartment" |$            1$ |metre^2^                  |]

The species have been declared as having substance units only.  Thus, they 
must be treated as amounts where they appear in expressions.

*)

newcase[ "00208" ];

addCompartment[ compartment, spatialDimensions-> 2, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-4, hasOnlySubstanceUnits->True ];
addSpecies[ S2, initialAmount -> 1 10^-4, hasOnlySubstanceUnits->True ];
addParameter[ k1, value -> 1 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1 * S1 ];

makemodel[]
