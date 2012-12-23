(* 

category:      Test
synopsis:      One reactions and two rate rules with four species in a compartment. 
componentTags: Compartment, Species, Reaction, Parameter, RateRule 
testTags:      Concentration, NonUnityCompartment
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

The model contains one compartment called C.  There are four
species called S1, S2, S3 and S4 and two parameters called k1 and k2.  The
model contains one reaction defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k1 * S1 * C$  |]

The model contains two rules:

[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |
 | Rate | S3 | $k1  *  0.5$  |
 | Rate | S4 | $-k2  *  0.5$  |]


The initial conditions are as follows:

[{width:30em,margin: 1em auto}|       |*Value*          |*Units*  |
|Initial concentration of S1                |$          1.5$ |mole litre^-1^                      |
|Initial concentration of S2                |$          2.0$ |mole litre^-1^                      |
|Initial concentration of S3                |$          1.5$ |mole litre^-1^                      |
|Initial concentration of S4                |$            4$ |mole litre^-1^                      |
|Value of parameter k1               |$         0.75$ |second^-1^ |
|Value of parameter k2               |$         0.25$ |second^-1^ |
|Volume of compartment C |$          0.5$ |litre                     |]

Note: The test data for this model was generated from an analytical solution
of the system of equations.

*)

newcase[ "00709" ];

addCompartment[ C, size -> 0.5 ];
addSpecies[ S1, initialConcentration -> 1.5 ];
addSpecies[ S2, initialConcentration -> 2.0];
addSpecies[ S3, initialConcentration -> 1.5];
addSpecies[ S4, initialConcentration -> 4];
addParameter[ k1, value -> .75 ];
addParameter[ k2, value -> .25 ];
addRule[ type->RateRule, variable -> S3, math -> k1  *  0.5];
addRule[ type->RateRule, variable -> S4, math -> -k2  *  0.5];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1 * S1 * C ];

makemodel[]
