(*

category:        Test
synopsis:        Use of the 'min' MathML in initial assignments.
componentTags:   InitialAssignment, Parameter
testTags:        InitialValueReassigned
testType:        TimeCourse
levels:          1.2, 2.1, 3.1, 3.2
generatedBy:     Numeric
packagesPresent: 

Four tests of the 'min' MathML construct. NOTE:  MUST UPDATE THIS TEST AFTER THE COMMUNITY VOTES ON WHAT SHOULD BE DONE WITH 'MIN()'.

The model contains:
* 4 parameters (p1, p2, p3, p4)
The initial conditions are as follows:

[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |
| Initial value of parameter p1 | $min(-1, -10)$ | constant |
| Initial value of parameter p2 | $min(30)$ | constant |
| Initial value of parameter p3 | $min(20, 200, 2, 2000)$ | constant |
| Initial value of parameter p4 | $min()$ | constant |]

*)
