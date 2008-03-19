(*

category:      Test
synopsis:      Basic two reactions with four species in a 1-dimensional
               compartment, with two species acting as a boundary condition.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      InitialAmount, 1D-Compartment, BoundaryCondition
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are four species named S1, S2, S3 and S4 and two parameters named k1 and k2.
  Compartment compartment is  1-dimensional.
  Both species S1 and S2 are labeled as an SBML boundary species.
  The model contains two reactions defined as:
[| | Reaction       |||||| Rate |
 | | S1+S2 -> S3+S4 |||||| $k1*S1*S2*compartment$  |
 | | S3+S4 -> S1+S2 |||||| $k2*S3*S4*compartment$  |]

The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $1.0 \x 10^-12$ || mole                      |
|              Initial amount of S2:|| $1.5 \x 10^-12$ || mole                      |
|              Initial amount of S3:|| $2.0 \x 10^-12$ || mole                      |
|              Initial amount of S4:|| $1.0 \x 10^-13$ || mole                      |
|             Value of parameter k1:|| $ 6.2 \x 10^12$ || mole^-1^ second^-1^ |
|             Value of parameter k2:|| $   5 \x 10^12$ || mole^-1^ second^-1^ |
| Length of compartment compartment:|| $            1$ || metre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00235" ];

addCompartment[ compartment, spatialDimensions-> 1, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0 10^-12, boundaryCondition->True];
addSpecies[ S2, initialAmount -> 1.5 10^-12, boundaryCondition->True];
addSpecies[ S3, initialAmount -> 2.0 10^-12];
addSpecies[ S4, initialAmount -> 1.0 10^-13];
addParameter[ k1, value -> 6.2 10^12 ];
addParameter[ k2, value -> 5 10^12 ];
addReaction[ S1+S2 -> S3+S4, reversible -> False,
	     kineticLaw -> k1*S1*S2*compartment ];
addReaction[ S3+S4 -> S1+S2, reversible -> False,
	     kineticLaw -> k2*S3*S4*compartment ];

makemodel[]
