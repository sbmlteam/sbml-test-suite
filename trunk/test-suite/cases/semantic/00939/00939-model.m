(* 

category:      Test
synopsis:      A species that with an initial concentration set, and subsequently produced by a reaction, being echoed by a parameter viewing it through a delay.
componentTags: Compartment, Parameter, Species, Reaction
testTags:      CSymbolDelay, CSymbolTime, InitialConcentration
testType:      TimeCourse, NonConstantParameter
levels:        1.2, 2.1, 2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

 The model contains one compartment called "default_compartment".  There is a single species x with an initial concentration, which at the start of the simulation increases due to a reaction.  A parameter (y) echoes this species with a 0.2 second delay, meaning that it starts by 'seeing' the initial constant value before echoing the linear increase.

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

