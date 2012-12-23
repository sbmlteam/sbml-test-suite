(* 

category:      Test
synopsis:      Basic single forward reaction with two species in one
compartment using initialAssignment to set the initial value of the compartment.
componentTags: Compartment, Species, Reaction, Parameter, InitialAssignment 
testTags:      Amount, InitialValueReassigned
testType:      TimeCourse
levels:        2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

The model contains one compartment called C.  There are two species called 
S1 and S2 and two parameters called k1 and k2.  The model contains one 
reaction defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $C * k1 * S1$  |]

  The model contains one initialAssignment:

[{width:30em,margin: 1em auto}| Variable | Formula |
 | C | $k2 / 50$  |]

Note: SBML's InitialAssignment construct override any declared initial
values.  In this model, the initial value for the compartment C has not been
explicitly declared and must be calculated using the InitialAssignment.


The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value*    |*Units*  |
|Initial amount of S1        |$1$          |mole                      |
|Initial amount of S2        |$1.5$        |mole                      |
|Value of parameter k1       |$0.5$        |second^-1^ |
|Value of parameter k2       |$50$         |litre |
|Volume of compartment C     |$undeclared$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

Note: The test data for this model was generated from an analytical solution
of the system of equations.

*)

newcase[ "00473" ];

addCompartment[ C, constant -> False];
addSpecies[ S1, initialAmount -> 1 ];
addSpecies[ S2, initialAmount -> 1.5 ];
addParameter[ k1, value -> 0.5];
addParameter[ k2, value -> 50 ];
addInitialAssignment[ C, math -> k2/50];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> C * k1 * S1];

makemodel[]
