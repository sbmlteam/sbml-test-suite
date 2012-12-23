(* 

category:      Test
synopsis:      Basic single forward reaction with three species in a compartment 
               where one species is constant.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      Amount, ConstantSpecies, NonUnityCompartment
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called "compartment".  There are three
species named S1, S2 and S3 and one parameter named k1.  Species S3 is
labeled as constant and therefore cannot be changed by rules or reactions.
The model contains one reaction defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k1 * S1 * S3 * compartment$  |]


The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                |$          1.5$ |mole                      |
|Initial amount of S2                |$          0.5$ |mole                      |
|Initial amount of S3                |$          1.2$ |mole                      |
|Value of parameter k1               |$         1.78$ |litre mole^-1^ second^-1^ |
|Volume of compartment "compartment" |$         1.34$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00248" ];

addCompartment[ compartment, size -> 1.34 ];
addSpecies[ S1, initialAmount -> 1.5 ];
addSpecies[ S2, initialAmount -> 0.5 ];
addSpecies[ S3, initialAmount -> 1.2, constant->True ];
addParameter[ k1, value -> 1.78 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1 * S1 * S3 * compartment ];

makemodel[]
