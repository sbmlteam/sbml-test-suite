category:      XML
synopsis:      Badly formed XML.
levels:        1.2, 2.1, 2.2, 2.3
status:        Fail

This document is missing the colon ' : ' between the 'xmlns' prefix and the name 'anything'
on line 3:
	
{>}xmlns anything="http://www.anything.org"%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| XML  |
| *Status:*| Fail  |
| *Levels:*| L1V2 |
| | L2V1 |
| | L2V2 |
| | L2V3 |]