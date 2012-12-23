(* 

category:      Test
synopsis:      Basic single forward reaction with two species in a 1-dimensional
size-10 compartment, where the species have only substance units.
componentTags: Compartment, Species, Reaction, Parameter 
testTags:      Amount, HasOnlySubstanceUnits, NonUnityCompartment
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

Note:  This test is a direct copy of test 00223, but with a size-10 compartment, to test the 'hasOnlySubstanceUnits' attribute.  The model should have exactly the same results, as concentration should have never been used to calculate anything.
 
The model contains one compartment called "compartment".  There are two
species named S1 and S2 and one parameter named k1.  Compartment
"compartment" is one-dimensional.  Species S1 and S2 are declared to have
only substance units.  The model contains one reaction defined as:

[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k1 * S1$  |]

The initial conditions are as follows:

[{width:30em,margin: 1em auto}|      |*Value*          |*Units*  |
|Initial amount of S1              |$ 1.5 \x 10^-6$ |mole                      |
|Initial amount of S2              |$   1 \x 10^-6$ |mole                      |
|Value of parameter k1             |$          1.5$ |second^-1^ |
|Area of compartment "compartment" |$            10$ |metre^2^                  |]

The species have been declared as having substance units only. Thus, they
must be treated as amounts where they appear in expressions.

Note: The test data for this model was generated from an analytical solution
of the system of equations.

*)

newcase[ "00223" ];

addCompartment[ compartment, spatialDimensions-> 1, size -> 10 ];
addSpecies[ S1, initialAmount -> 1.5 10^-6, hasOnlySubstanceUnits->True ];
addSpecies[ S2, initialAmount -> 1 10^-6, hasOnlySubstanceUnits->True ];
addParameter[ k1, value -> 1.5 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1 * S1 ];

makemodel[]
