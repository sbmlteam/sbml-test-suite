(*

category:      Test
synopsis:      Basic reactions with three species in a 1-dimensional
               compartment, with two species acting as a boundary condition.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      InitialAmount, 1D-Compartment, BoundaryCondition
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are three species named S1, S2 and S3 and two parameters named k1 and k2.
  Compartment compartment is  1-dimensional.
  Both species S1 and S3 are labeled as an SBML boundary species.
  The model contains two reactions defined as:

[{width:30em,left-margin:5em}| | *Reaction* | *Rate* |
| S1+S2 -> S3 | $k1*S1*S2*compartment$  |
| S3 -> S1+S2 | $k2*S3*compartment$  |]

The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $          1.0$ | mole                      |
|              Initial amount of S2:| $          2.0$ | mole                      |
|              Initial amount of S3:| $          1.5$ | mole                      |
|             Value of parameter k1:| $          7.5$ | metre mole^-1^ second^-1^ |
|             Value of parameter k2:| $          0.3$ | second^-1^ |
| Length of compartment compartment:| $            1$ | metre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00232" ];

addCompartment[ compartment, spatialDimensions-> 1, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0, boundaryCondition->True ];
addSpecies[ S2, initialAmount -> 2.0];
addSpecies[ S3, initialAmount -> 1.5, boundaryCondition->True ];
addParameter[ k1, value -> 7.5 ];
addParameter[ k2, value -> 0.3 ];
addReaction[ S1+S2 -> S3, reversible -> False,
	     kineticLaw -> k1*S1*S2*compartment ];
addReaction[ S3 -> S1+S2, reversible -> False,
	     kineticLaw -> k2*S3*compartment ];

makemodel[]