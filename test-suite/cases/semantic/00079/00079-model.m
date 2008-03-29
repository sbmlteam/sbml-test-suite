(* 

category:      Test
synopsis:      Two reactions with four species in one
               compartment using an assignmentRule with a 
               functionDefinition to vary one species.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition, AssignmentRule 
testTags:      InitialAmount
testType:      TimeCourse
levels:        2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There are four species named S1, S2, S3 and S4 and three parameters named k1, k2 and p1.
  The model contains two reactions defined as:

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| S1 + S2 -> S3 | $k1 * S1 * S2 * compartment$  |
| S3 -> S1 + S2 | $k2 * S3 * compartment$  |]

  The model contains one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
 | Assignment | S4       | $multiply(p1, S2)$  |]


The model contains one functionDefinition defined as:

[{width:30em,margin-left:5em}|  * Id *  |  * Arguments *  |  *Formula*  |
 | multiply | x, y | $x * y$ |]

Note that in this case the initial value of the species S4 is not explicitly
declared and must be calculated by the assignmentRule. 


The initial conditions are as follows:

[{width:30em,margin-left:5em}| |  *Value*  |  *Units*  |
|              Initial amount of S1:| $1.0 \x 10^-12$ | mole                      |
|              Initial amount of S2:| $2.0 \x 10^-12$ | mole                      |
|              Initial amount of S3:| $1.5 \x 10^-12$ | mole                      |
|              Initial amount of S4:| $   undeclared$ | mole                      |
|             Value of parameter k1:| $0.75 \x 10^12$ | litre mole^-1^ second^-1^ |
|             Value of parameter k2:| $0.25 \x 10^-6$ | second^-1^ |
|             Value of parameter p1:| $         0.75$ | dimensionless |
| Volume of compartment compartment:| $            1$ | litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00079" ];

addFunction[ multiply, arguments -> {x, y}, math -> x * y];
addCompartment[ compartment, size -> 1];
addSpecies[ S1, initialAmount -> 1.0 10^-12];
addSpecies[ S2, initialAmount -> 2.0 10^-12];
addSpecies[ S3, initialAmount -> 1.5 10^-12];
addSpecies[ S4 ];
addParameter[ k1, value -> .75 10^12];
addParameter[ k2, value -> .25 10^-6];
addParameter[ p1, value -> 0.75];
addRule[ type->AssignmentRule, variable -> S4, math ->multiply[p1,S2]];
addReaction[ S1 + S2 -> S3, reversible -> False,
	     kineticLaw -> k1 * S1 * S2 * compartment ];
addReaction[ S3 -> S1 + S2, reversible -> False,
	     kineticLaw -> k2 * S3 * compartment ];

makemodel[]
