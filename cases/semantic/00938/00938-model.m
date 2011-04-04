(* 

category:      Test
synopsis:      A species that changes behavior between its IntialAssignment andsubsequently produced by a reaction, being echoed by a parameter viewing it through a delay.
componentTags: Compartment, Parameter, Species, InitialAssignment, Reaction, AssignmentRule, CSymbolDelay, CSymbolTime
testTags:      Amount, NonConstantParameter
testType:      TimeCourse
levels:        2.2, 2.3, 2.4, 3.1
generatedBy:   Analytic

 The model contains one compartment called "default_compartment".  There is a single species S1 whose InitialAssignment claims that it has been oscillating (the sine function) up until the start of the simulation, at which point a reaction begins to create it.  A parameter (y) echoes this species with a 0.2 second delay, meaning that it starts by 'seeing' the oscillations before echoing the linear increase.

The initial conditions are as follows:

[{width:30em,margin-left:5em}|       |*Value*          |
|Initial amount of x                |$0$  |
|Initial amount of y                |$1.090703$              |
|Volume of compartment "default_compartment" |$1$              |]

The species values are given as amounts of substance to make it easier to
use the model in a discrete stochastic simulator, but (as per usual SBML
principles) their symbols represent their values in concentration units
where they appear in expressions.

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

