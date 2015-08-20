(* 

category:      Test
synopsis:      Algebraic rule used to determine rate of species change.
componentTags: StoichiometryMath, Compartment, Species, Reaction, Parameter, AlgebraicRule 
testTags:      Amount, AssignedConstantStoichiometry, NonUnityStoichiometry, InitialValueReassigned
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 2.5
generatedBy:   Numeric

Note:  earlier versions of the test suite contained a 3.1 version of this test.
That model was moved to its own test, because it did not have the 'StoichiometryMath'
component.

The model contains one compartment called C.  There are four
species called X0, X1, T and S1 and four parameters called k1, k2, k3 and p1.
Species X0 is labeled as an SBML boundary species.  The model
contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| (4 * p1)X0 -> T     | $C * k1 * X0$  |
| T -> X1     | $C * k2 * S1$  |]

The model contains one rule:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Algebraic |   n/a    | $(1 + k3) * S1 - T$  |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}| |*Value* |*Units*  |
|Initial amount of X0          |$0.5$     |mole                      |
|Initial amount of X1          |$0$     |mole                      |
|Initial amount of T           |$0$     |mole                      |
|Initial amount of S1          |$0$     |mole                      |
|Value of parameter k1         |$0.1$   |second^-1^            |
|Value of parameter k2         |$0.375$ |second^-1^                |
|Value of parameter k3         |$2.5$   |dimensionless                |
|Value of parameter p1         |$0.5$   |dimensionless   |
|Volume of compartment C       |$1$     |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00562" ];

addCompartment[ C, size -> 1 ];
addSpecies[ X0, initialAmount -> 0.5];
addSpecies[ X1, initialAmount -> 0];
addSpecies[ T, initialAmount -> 0];
addSpecies[ S1, initialAmount -> 0];
addParameter[ k1, value -> 0.1 ];
addParameter[ k2, value -> 0.375 ];
addParameter[ k3, value -> 2.5 ];
addParameter[ p1, value -> 0.5 ];
addRule[ type->AlgebraicRule, math -> (1+k3) * S1 - T];
addReaction[reactants->{X0}, products->{T}, reactantStoichiometry->{4 * p1}, 
       reversible -> False,
	     kineticLaw -> C * k1 * X0 ];
addReaction[ T -> X1, reversible -> False,
	     kineticLaw -> C * k2 *S1 ];

makemodel[]
