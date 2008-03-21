(*

category:      Test
synopsis:      Single forward reaction with two species in one compartment 
               and one event that assigns value to a species.
componentTags: Compartment, Species, Reaction, Parameter, Event 
testTags:      InitialAmount
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are two species named S1 and S2 and one parameter named k1.
  The model contains one reaction defined as:
[| | Reaction |||||| Rate                 |
 | | S1 -> S2 |||||| $compartment*S1*k1$  |]

The model contains one event that assigns value to species S1 defined as:
[||        | Trigger    | Delay || Assignments |
 || Event1 | $S1 < 0.1$ | $-$   || $S1 = 1$    |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $       1.0$ || mole                      |
|              Initial amount of S2:|| $       0$ || mole                      |
|             Value of parameter k1:|| $            1$ || second^-1^                |
| Volume of compartment compartment:|| $            1$ || litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00026" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0 ];
addSpecies[ S2, initialAmount -> 0.0 ];
addParameter[ k1, value -> 1 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> compartment*S1*k1 ];
addEvent[ trigger -> S1 < 0.1, eventAssignment -> S1->1];

makemodel[]
