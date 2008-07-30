category:      SBML
synopsis:      Missing SBML namespace.
levels:        1.2, 2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20101*:     

The *%<%sbml%>%* container element must declare the XML Namespace for SBML,
and this declaration must be consistent with the values of the _level_ 
and _version_ attributes on the *%<%sbml%>%* element. 

References: L2V2 Section 4.1; L2V3 Section 4.1. 


The _namespace_ attribute is missing from the sbml element on line 2:
		
{>}%<%sbml level="2" version="3"%>% 
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