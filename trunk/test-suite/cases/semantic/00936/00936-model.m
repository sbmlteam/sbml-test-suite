(* 

category:      Test
synopsis:      Limited-time oscillation that triggers a delayed event three times before the first one executes.
componentTags: Compartment, Species, EventWithDelay
testTags:      Concentration, UseValuesFromTriggerTime, PersistentTrigger
testType:      TimeCourse
levels:        2.4, 3.1
generatedBy:   Analytic

The model contains one compartment called "default_compartment".  There are two
species called S1 and S2. 


 The model contains one species (S1) that oscillates (from the 'sin' function) for two seconds, then stops.  During that oscillation, the single event is triggered three times with a delay of two seconds each, increasing the value of the second species (S2) by one each time.  As 'useValuesFromTriggerTime' is false, this means that first S1 oscillates, then S2 steps up three times.

The initial conditions are as follows:

[{width:30em,margin-left:5em}|       |*Value*          |
|Initial concentration of S1                |$0$  |
|Initial concentration of S2                |$0$              |
|Volume of compartment "default_compartment" |$1$              |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

