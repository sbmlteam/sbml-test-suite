(* 

category:      Test
synopsis:      Reactions occurring between two compartments with functionDefinitions.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition 
testTags:      Amount, MultiCompartment, ReversibleReaction
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 2.5, 3.1
generatedBy:   Numeric

The model contains two compartments named compartment and compartment1.
There are two species named S1 and S3 and two parameters named k1 and k2.
Compartment "compartment" contains species S1.  Compartment "compartment"1
contains species S3.  The model contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S3  | $multiply(k1,S1) * compartment$  |
| S3 -> S1 | $k2 * (S3-S1) * compartment1$  |]

The reactions occur between the species within different compartments:

The model contains one functionDefinition defined as:

[{width:30em,margin: 1em auto}|  *Id*  |  *Arguments*  |  *Formula*  |
 | multiply | x, y | $x * y$ |]


The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                 |$          1.0$ |mole                      |
|Initial amount of S3                 |$            0$ |mole                      |
|Value of parameter k1                |$         0.75$ |second^-1^ |
|Value of parameter k2                |$         0.25$ |second^-1^ |
|Volume of compartment "compartment"  |$            1$ |litre                     |
|Volume of compartment "compartment"1 |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00112" ];

addFunction[ multiply, arguments -> {x, y}, math -> x * y];
addCompartment[ compartment, size -> 1 ];
addCompartment[ compartment1, size -> 1 ];
addSpecies[ S1, compartment->compartment, initialAmount -> 1.0];
addSpecies[ S3, compartment->compartment1, initialAmount -> 0];
addParameter[ k1, value -> 0.75 ];
addParameter[ k2, value -> 0.25 ];
addReaction[ S1 -> S3 , reversible -> False,
	     kineticLaw -> multiply[k1,S1] * compartment ];
addReaction[ S3 -> S1, reversible -> False,
	     kineticLaw -> k2 * (S3-S1) * compartment1 ];

makemodel[]
