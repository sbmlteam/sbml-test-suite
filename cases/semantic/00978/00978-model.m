(* 

category:      Test
synopsis:      Several events conspire within the same time step to trigger two events multiple times, one persistent and the other non-persistent.
componentTags: EventNoDelay, EventPriority, Parameter
testTags:      InitialValue, CSymbolTime, 
testType:      TimeCourse
levels:        3.1
generatedBy:   Analytic

This model contains two events that trigger when the parameter multitrig increases to 
above 1.  The first is set 'persistent=true', and sets x=x+1, meaning that every time
it triggers, it ends up increasing x by one.  The second is set 'persistent=false', 
and sets y=y+1, meaning that every time it triggers, it has the potential to increase y
by one, but if multitrig drops below 1 again, it will not actually do so.

The model also contains nine events with the same trigger and with decreasing priorities 
that alternate setting the value of the parameter 'multitrig' from zero to 2, then to 
zero, then back again, etc.  The priorities of all these events are higher than the first 
two, meaning that at a single time point, the first two events are triggered five times,
and 'de-triggered' four times.  Since the first event was persistent, x should increase
from zero to five, and since the second event was not persistent, y should increase from
zero to one.

For a different view of this model, see 00978-antimony.txt

The initial conditions are as follows:

[{width:30em,margin-left:5em}| |*Value*       |
|Value of parameter x       |$0$          |
|Value of parameter y       |$0$          |
|Value of parameter multitrig    |$0$          |]

*)
