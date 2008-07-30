category:      XML
synopsis:      Element tag mismatch or missing tag.
levels:        1.2, 2.1, 2.2, 2.3
status:        Fail

This model is missing the closing tag '/' of the parameter element on line 12:
	
{>}%<%parameter  id="p"  value="1"  units="second" constant="false" %>%
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