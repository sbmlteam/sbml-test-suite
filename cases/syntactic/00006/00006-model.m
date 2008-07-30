category:      SBML
synopsis:      Inconsistent SBML level.
levels:        1.2, 2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20102*:     

The *%<%sbml%>%* container element must declare the SBML Level using the
attribute _level_, and this declaration must be consistent with the XML
Namespace declared for the *%<%sbml%>%* element. 
    
    
References: L2V2 Section 4.1; L2V3 Section 4.1. 

The _level_ attribute is inconsistent with the namespace declared on the sbml element on line 2:
		
{>}%<%sbml  xmlns="http://www.sbml.org/sbml/level2/version3" level="1" version="3"%>% 
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