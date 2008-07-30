category:      SBML
synopsis:      Inconsistent SBML version.
levels:        1.2, 2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20103*:     

The *%<%sbml%>%* container element must declare the SBML Version using the
attribute _version_, and this declaration must be consistent with the XML
Namespace declared for the *%<%sbml%>%* element. 
    
    
References: L2V2 Section 4.1; L2V3 Section 4.1. 

The _version_ attribute is inconsistent with the namespace declared on the sbml element on line 2:
		
{>}%<%sbml  xmlns="http://www.sbml.org/sbml/level2/version3" level="2" version="2"%>% 
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