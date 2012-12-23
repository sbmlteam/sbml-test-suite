(* 

category:      Test
synopsis:      Basic reactions with four species in one
compartment using a rate that causes a discontinuity in the output.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition 
testTags:      Amount
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

The model contains one compartment called "compartment".  There are four
species named S1, S2, S3 and S4 and three parameters named p1, p2 and k1.
The model contains two reactions defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $compartment * k1 * S1$  |
| S3 -> S4 | $calculate(S2, p1, p2)$  |]

The model contains one functionDefinition, which is used within the
reaction, defined as:

[{width:30em,margin: 1em auto}|  *Id*  |  *Arguments*  |  *Formula*  |
 | calculate | x, y, z | $Piecewise({{y, Or(x < 2, x > 4, x < 1)}}, z)$ |]


The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial amount of S1                |$            5$ |mole                      |
|Initial amount of S2                |$            0$ |mole                      |
|Initial amount of S3                |$            4$ |mole                      |
|Initial amount of S4                |$            0$ |mole                      |
|Value of parameter p1               |$          1.5$ |mole second^-1^ |
|Value of parameter p2               |$         0.05$ |mole second^-1^ |
|Value of parameter k1               |$          1.0$ |second^-1^ |
|Volume of compartment "compartment" |$            1$ |litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

*)

newcase[ "00278" ];

addFunction[ calculate, arguments -> {x, y, z}, 
             math -> Piecewise[{{y, Or[x < 2, x > 4, x < 1]}}, z] ];
addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 5];
addSpecies[ S2, initialAmount -> 0];
addSpecies[ S3, initialAmount -> 4];
addSpecies[ S4, initialAmount -> 0];
addParameter[ p1, value -> 1.5 ];
addParameter[ p2, value -> 0.05 ];
addParameter[ k1, value -> 1.0 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> compartment * k1 * S1 ];
addReaction[ S3 -> S4, reversible -> False,
	     kineticLaw -> calculate[S2,p1,p2] ];

makemodel[]
