category:      SBML
synopsis:      Invalid return type in functionDefinition.
levels:        2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20305*:     

The value type returned by a *%<%functionDefinition%>%*'s *%<%lambda%>%* must be 
either boolean or numeric. 

References: L2V2 Section 3.5.8; L2V3 Section 3.4.9.


The %<%apply/%>% element on line 10 does not return either a boolean or a
numeric value:
		
{>}%<%functionDefinition id="pow"%>%\\
%<%math xmlns="http://www.w3.org/1998/Math/MathML"%>%\\
%<%lambda%>%\\
%<%bvar%>% %<%ci%>% x %<%/ci%>% %<%/bvar%>%\\
%<%bvar%>% %<%ci%>% y %<%/ci%>% %<%/bvar%>%\\
%<%apply/%>%\\
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML  |
| *Status:*| Fail  |
| *Levels:*| L2V1 |
| | L2V2 |
| | L2V3 |]