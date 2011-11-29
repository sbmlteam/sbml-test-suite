(* 

category:      Test
synopsis:      Species with and initialConcentration but hasOnlySubstanceUnits="true".
componentTags: Species, Compartment, Reaction
testTags:      NonUnityCompartment, HasOnlySubstanceUnits
testType:      TimeCourse
levels:        2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Numeric

 The single species in this model has an initial concentration of '1', but lives in a compartment of size 5, giving it an initial amount of 5. Since 'hasOnlySubstanceUnits' is 'true', this amount is what is used for the rate rule of the reaction (10/S1).

 For comparison, if the size of the compartment is set to '1' and the initialConcentration of the species is changed to an initialAmount of '5', the same numerical results should be obtained.

*)
