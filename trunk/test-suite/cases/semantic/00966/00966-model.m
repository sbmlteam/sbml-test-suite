(* 

category:      Test
synopsis:      Competing events with the same priority, jointly causing a parameter to monotonically increase, calculating a chi-squared statistic to ensure randomness.  NOTE:  STOCHASTIC TEST. This test is designed to fail with a frequency of 0.00002, but it still may happen.
componentTags: Parameter, EventNoDelay, EventPriority, AssignmentRule
testTags:      InitialValue, CSymbolTime, RandomEventExecution
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

This model (the same basic model as 952) contains two events with the same trigger, the same priority, both set 'persistent=false', and both of which disable the trigger of the other.  This means that every .01 seconds, one fires and the other does not, at random, and increases the parameters Q or R, respectively.  A third parameter, S, is assigned the value of Q+R, meaning that it doesn't matter which one fires; S will increase monotonically.

In this test, we also keep track of each run of 16 tests in a row, calculating the sum of Q during that run, and incrementing the appropriate frequency (chi0 -> chi16).  At the end of 6,420 tests, the chi-square value is calculated from the observed and the pre-calculated expected frequencies of the 17 possible values.  If this value is greater than 50, an error is triggered.  The liklihood of the error being triggered is 0.00002, so it is quite unlikely (though possible) that a simulator would trigger it with a working random number generator, and almost impossible for this to happen twice in a row.

This model has a number of events, so it's probably best to look at the antimony file for more details:  00966-antimony.txt

Note: The test data for this model was generated from an analytical
solution of the system of equations.
*)
