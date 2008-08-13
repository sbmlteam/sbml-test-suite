category:      SBML
synopsis:      Recursive functionDefinitions not allowed.
levels:        2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20303*:     

Inside the *%<%lambda%>%* of a *%<%functionDefinition%>%*, the identifier of that 
*%<%functionDefinition%>%* cannot appear as the value of a *%<%ci%>%* element. SBML 
functions are not permitted to be recursive. 

References: L2V2 Sections 3.5.3 and 4.3.2; L2V3 Sections 3.4.3 and 4.3.2.


The %<%ci%>% element *add* on line 13 refers to the containing functionDefinition:
		
{>}%<%functionDefinition id="add"%>%\\
%<%math xmlns="http://www.w3.org/1998/Math/MathML"%>%\\
%<%lambda%>%\\
%<%bvar%>% %<%ci%>% x %<%/ci%>% %<%/bvar%>%\\
%<%apply%>%\\
%<%plus/%>%\\ 
%<%ci%>% x %<%/ci%>%\\ 
%<%apply%>%\\
%<%ci%>% add %<%/ci%>%
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