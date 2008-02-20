(*

category:      Test
synopsis:      Basic single forward reaction with two species in one
               compartment using a function to apply the kinetic law.
componentTags: Compartment, Species, Reaction, Parameter, FunctionDefinition
testTags:      InitialAmount
testtype:      TimeCourse
levels:        2.1, 2.2, 2.3

This case is essentially identical to 00005, but here the rate law uses
a functionDefinition.  This model involves one compartment named compartment, 
two species named S1 and S2, one reaction $S1 -> S2$, with the biochemical 
rate of the reaction being $multiply(k1, [S1])$ and one functionDefinition 
named multiply which applies the formula $f(x, y) = x * y$.  The species 
amounts are $1.5 x 10^{-15}$ for both S1 and S2.  The units are the SBML 
defaults (mole for species, litre for volume) and the unit of k1 is assumed 
to be second^-1^.

*)

newcase[ "00025" ];

addFunction[ multiply, arguments -> {x, y}, math -> x*y];
addCompartment[ compartment, size -> 1 ];
addSpecies[ S1, initialAmount -> 1.5 10^-15 ];
addSpecies[ S2, initialAmount -> 1.5 10^-15 ];
addParameter[ k1, value -> 1 ];
addReaction[ S1 -> S2, reversible -> False,
	     kineticLaw -> compartment*multiply[k1,S1] ];

makemodel[]
