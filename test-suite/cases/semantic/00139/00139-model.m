(*

category:      Test
synopsis:      Basic single forward reaction with three species in one
               compartment using an assignmentRule to vary one species.
componentTags: Compartment, Species, Reaction, Parameter, AssignmentRule 
testTags:      InitialAmount
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are three species named S1, S2 and S3 and two parameters named k1 and k2.
  The model contains one reaction defined as:

[{width:30em,left-margin:5em}| | *Reaction* | *Rate* |
| S1 -> S2 | $compartment*k2*S1$  |]

  The model contains one rule which assigns value to compartment compartment:

[{width:30em,left-margin:5em}| *Type* | *Variable* | *Formula* |
 | Assignment | compartment | $k1/k1$  |]
In this case the initial value declared for compartment compartment is consistent with the value
calculated by the assignmentRule.

The initial conditions are as follows:

[{width:30em,left-margin:5em}| | *Value* | *Units* |
|              Initial amount of S1:| $            1$ | mole                      |
|              Initial amount of S2:| $          1.5$ | mole                      |
|              Initial amount of S3:| $        1.125$ | mole                      |
|             Value of parameter k1:| $         0.75$ | any |
|             Value of parameter k2:| $           50$ | second^-1^ |
| Volume of compartment compartment:| $            1$ | litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00139" ];

addCompartment[ compartment, size -> 1, constant -> False];
addSpecies[ S1, initialAmount->1 ];
addSpecies[ S2, initialAmount -> 1.5 ];
addSpecies[ S3, initialAmount -> 1.125];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 50 ];
addRule[ type->AssignmentRule, variable -> compartment, math ->k1/k1];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> compartment*k2*S1];

makemodel[]