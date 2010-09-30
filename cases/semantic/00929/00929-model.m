(* 

category:      Test
synopsis:      Single forward reaction with two species in one compartment and an event
componentTags: Compartment, Species, Reaction, Parameter, Event
testTags:      Amount, InitialValue
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

The model contains one compartment called "compartment".  There are two
species called S1 and S2 and one parameter called k1.  The model contains
one reaction:

[{width:30em,margin-left:5em}|  *Reaction*  |  *Rate*  |
| S1 -> S2 | $k1 * S1 * compartment$ |]

The model contains one event that is triggered at t=0.

The initial conditions are as follows:

[{width:30em,margin-left:5em}|       |*Value*          |*Units*        |
|Initial amount of S1                |$1.5 \x 10^-4$  |mole           |
|Initial amount of S2                |$0$              |mole           |
|Value of parameter k1               |$1$              |second^-1^     |
|Volume of compartment "compartment" |$1$              |litre          |]

The species' initial quantities are given in terms of substance units to
make it easier to use the model in a discrete stochastic simulator, but (as
per usual SBML principles) their symbols represent their values in
concentration units where they appear in expressions.

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)

