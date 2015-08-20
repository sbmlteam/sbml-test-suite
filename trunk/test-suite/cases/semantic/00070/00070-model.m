(* 

category:      Test
synopsis:      Basic reactions with three species in a compartment 
               involving a stoichiometryMath element.
componentTags: StoichiometryMath, Compartment, Species, Reaction, Parameter
testTags:      Amount, AssignedConstantStoichiometry, NonUnityStoichiometry
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 2.5
generatedBy:   Numeric

Note:  earlier versions of the test suite contained a 3.1 version of this test.
That model was moved to its own test, because it did not have the 'StoichiometryMath'
component.

The model contains one compartment called "compartment".  There are three
species named S1, S2 and S3 and three parameters named k1, k2 and p1.  The
model contains two reactions defined as:

[{width:30em,margin: 1em auto}| *Reaction*  |  *Rate*  |
| S1 + S2 -> S3                   | $k1 * S1 * S2 * compartment$  |
| S3 -> (2 * p1)S1  +  (p1 / 0.5)S2 | $k2 * S3 * compartment$  |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*         |*Units*  |
|Initial amount of S1                |$1.0 \x 10^-2$ |mole                      |
|Initial amount of S2                |$2.0 \x 10^-2$ |mole                      |
|Initial amount of S3                |$1.0 \x 10^-2$ |mole                      |
|Value of parameter k1               |$ 1.7$ |litre mole^-1^ second^-1^ |
|Value of parameter k2               |$          0.3$ |second^-1^ |
|Value of parameter p1               |$            1$ |dimensionless |
|Volume of compartment "compartment" |$            1$ |rlitre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00070" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.0 10^-2];
addSpecies[ S2, initialAmount -> 2.0 10^-2];
addSpecies[ S3, initialAmount -> 1.0 10^-2];
addParameter[ k1, value -> 1.7 ];
addParameter[ k2, value -> 0.3 ];
addParameter[ p1, value -> 1 ];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * compartment ];
addReaction[ reactants->{S3}, products->{S1, S2}, productStoichiometry->{2 * p1, p1/0.5},
       reversible -> False,
	     kineticLaw -> k2 * S3 * compartment ];

makemodel[]
