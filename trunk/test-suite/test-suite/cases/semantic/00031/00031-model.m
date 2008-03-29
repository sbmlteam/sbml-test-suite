(* 

category:      Test
synopsis:      Basic single rate rule used to determine species value.
componentTags: Compartment, Species, RateRule 
testTags:      InitialAmount
testType:      TimeCourse
levels:        1.2, 2.1, 2.2, 2.3

The model contains one compartment named compartment.
  There is one species named S1.
  The model does not contain any reactions.
  The model contains one rule:

[{width:30em,margin-left:5em}|  *Type*  |  *Variable*  |  *Formula*  |
 | Rate | S1       | $7$     |]


The initial conditions are as follows:

[{width:30em,margin-left:5em}| |  *Value*  |  *Units*  |
|              Initial amount of S1:| $            0$ | mole                      |
| Volume of compartment compartment:| $            1$ | litre                     |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) they must be treated as concentrations where they appear in
expressions.

*)

newcase[ "00031" ];

addCompartment[ compartment, size -> 1.0];
addSpecies[ S1, initialAmount -> 0 ];
addRule[ type->RateRule, variable -> S1, math -> 7];

makemodel[]
