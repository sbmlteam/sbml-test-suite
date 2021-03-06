(*

category:        Test
synopsis:        A replaced parameter whose ID is used in an event delay.
componentTags:   CSymbolTime, EventWithDelay, Parameter, comp:ModelDefinition, comp:ReplacedElement, comp:Submodel
testTags:        NonConstantParameter, comp:SubmodelOutput
testType:        TimeCourse
levels:          3.1, 3.2
generatedBy:     Analytic
packagesPresent: comp

 In this model, a replaced parameter ID is used in an event delay.

The 'flattened' version of this hierarchical model contains:
* 2 parameters (J1, A__x)

There is one event:

[{width:35em,margin: 1em auto}|  *Event*  |  *Trigger*  |  *Delay*  | *Event Assignments* |
| A__E0 | $time > 0.5$ | $J1$ | $A__x = 2$ |]

The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter J1 | $5$ | constant |
| Initial value of parameter A__x | $1$ | variable |]

Note: The test data for this model was generated from an analytical
solution of the system of equations.

*)
