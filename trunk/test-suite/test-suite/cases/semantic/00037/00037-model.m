(*

category:      Test
synopsis:      Basic single forward reaction with two species in one
               compartment using initialAssignment to set the initial value of one species.
componentTags: Compartment, Species, Reaction, Parameter, InitialAssignment 
testTags:      InitialAmount
testType:      TimeCourse
levels:        2.2, 2.3

The model contains one compartment named compartment.
  There are two species named S1 and S2 and two parameters named k1 and k2.
  The model contains one reaction defined as:
[| | Reaction |||||| Rate |
 | | S1 -> S2 |||||| $compartment*k2*S$  |]

  The model contains one initialAssignment:
[|| Variable || Formula |
 || S1 || $k1*S2$  |]

  Note: InitialAssignments override any declared initial values.  In this case the initial
value declared for species S1 is inconsistent with the value returned by the initialAssignment.


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $            1$ || mole                      |
|              Initial amount of S2:|| $1.5 \x 10^-15$ || mole                      |
|             Value of parameter k1:|| $         0.75$ || dimensionless             |
|             Value of parameter k2:|| $           50$ || second^-1^                |
| Volume of compartment compartment:|| $             $ || litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00037" ];

addCompartment[ compartment, size -> 1];
addSpecies[ S1, initialAmount->1 ];
addSpecies[ S2, initialAmount -> 1.5 10^-15 ];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 50 ];
addInitialAssignment[ S1, math -> k1*S2];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> compartment*k2*S1];

makemodel[]
