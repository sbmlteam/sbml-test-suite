(*

category:      Test
synopsis:      Basic reaction and rate rule with three species in a compartment. 
componentTags: Compartment, Species, Reaction, Parameter, RateRule 
testTags:      InitialAmount, HasOnlySubstanceUnits
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are three species named S1, S2 and S3 and two parameters named k1 and k2.
  The model contains one reaction defined as:
[| | Reaction |||||| Rate |
 | | S1 -> S2 |||||| $k1*S1*compartment$  |]

  The model contains one rule:
[|| Type || Variable || Formula |
 || Rate || S3 || $k1*k2$  |]


The initial conditions are as follows:
[|                                  ||          Value  || Units                     |
|              Initial amount of S1:|| $1.5 \x 10^-12$ || mole                      |
|              Initial amount of S2:|| $1.0 \x 10^-12$ || mole                      |
|              Initial amount of S3:|| $1.0 \x 10^-12$ || mole                      |
|             Value of parameter k1:|| $         1.75$ || second^-1^ |
|             Value of parameter k2:|| $1.5 \x 10^-13$ || mole |
| Volume of compartment compartment:|| $            1$ || litre                     |]

The species have been declared as having substance units only. Thus they 
must be treated as amounts where they appear in expressions.

*)

newcase[ "00331" ];

addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-12, hasOnlySubstanceUnits->True ];
addSpecies[ S2, initialAmount -> 1.0 10^-12, hasOnlySubstanceUnits->True ];
addSpecies[ S3, initialAmount -> 1.0 10^-12, hasOnlySubstanceUnits->True ];
addParameter[ k1, value -> 1.75 ];
addParameter[ k2, value -> 1.5 10^-13 ];
addRule[ type->RateRule, variable -> S3, math -> k1*k2];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> k1*S1*compartment ];

makemodel[]
