(* 

category:      Test
synopsis:      Rate rule used to determine parameter value which is used 
               in a reaction.
componentTags: Compartment, Species, Reaction, Parameter, RateRule 
testTags:      InitialAmount, NonConstantParameter
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3, 2.4
generatedBy:   Numeric

The model contains one compartment called "compartment".  There are two
species called S1 and S2 and one parameter called k1, whose value is varying.
The model contains one reaction defined as:

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k1 * S1 * compartment$  |]

The model contains one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
| Rate | k1       | $0.5$   |]

The initial conditions are as follows:

[{width:30em,margin-left:5em}|       |*Value*         |*Units*  |
|Initial amount of S1                |$1.5 \x 10^-15$ |mole                      |
|Initial amount of S2                |$            0$ |mole                      |
|Value of parameter k1               |$            1$ |second^-1^                |
|Volume of compartment "compartment" |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00033" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 0 ];
addParameter[ k1, value -> 1, constant->False ];
addRule[ type->RateRule, variable -> k1, math -> 0.5];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1 * S1 * compartment ];

makemodel[]
