category:      SBML
synopsis:      Missing model.
levels:        1.2, 2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20201*:     

An SBML document must contain a *%<%model%>%* definition.

References: L2V1, L2V2 and L2V3 Section 4.1. 


The *%<%model%>%* element is missing from the sbml element:

{>}%<%sbml xmlns="http://www.sbml.org/sbml/level2/version3" level="2" version="3"%>%\\
%<%/sbml%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML  |
| *Status:*| Fail  |
| *Levels:*| L1V2 |
| | L2V1 |
| | L2V2 |
| | L2V3 |]