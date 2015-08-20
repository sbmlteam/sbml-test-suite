(* 

category:      Test
synopsis:      Single reversible reaction.
componentTags: StoichiometryMath, Compartment, Species, Reaction, Parameter 
testTags:      Amount, ReversibleReaction, AssignedConstantStoichiometry, NonUnityStoichiometry
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 2.5
generatedBy:   Numeric

Note:  This test is a copy of model 828, with the products and reactants reversed, 
and a negative kinetic law.  This gives the same results, but during the simulation, 
the kinetic law actually goes negative (a requirement for reversibe reactions).  
The reaction listed below is therfore equivalent to the one in the file, but reversed from it.

The version of this model using Level 3 constructs is present
as test 1111.

The model contains one compartment called C.  There are three species called 
S1, S2 and S3 and three parameters called kf, kr and p1.  The model contains  one reaction
defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 + (4 * p1)S2 <-> S3 | $(kf * S1 * S2 - kr * S3) * C$  |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value*       |*Units*  |
|Initial amount of S1        |$1.0$ |mole                      |
|Initial amount of S2        |$0.5$ |mole                      |
|Initial amount of S3        |$0$ |mole                      |
|Value of parameter kf       |$1.1$          |litre mole^-1^ second^-1^ |
|Value of parameter kr       |$0.09$          |second^-1^ |
|Value of parameter p1       |$0.5$          |dimensionless |
|Volume of compartment C     |$1$             |litre                  |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00828" ];

addCompartment[ C, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0];
addSpecies[ S2, initialAmount -> 0.5];
addSpecies[ S3, initialAmount -> 0.0];
addParameter[ kf, value -> 1.1 ];
addParameter[ kr, value -> 0.09 ];
addParameter[ p1, value -> 0.5 ];
addReaction[ reactants->{S1, S2}, products->{S3}, reactantStoichiometry->{1, 4 * p1},
	     kineticLaw -> (kf * S1 * S2 - kr * S3) * C ];

makemodel[]
