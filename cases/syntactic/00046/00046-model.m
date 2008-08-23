category:      SBML
synopsis:      Invalid ci element in functionDefinition.
levels:        2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20304*:     

Inside the *%<%lambda%>%* of a *%<%functionDefinition%>%*, if a *%<%ci%>%* element is not 
the first element within a MathML *%<%apply%>%*, then the *%<%ci%>%*'s value can 
only be the value of a *%<%bvar%>%* element declared in that *%<%lambda%>%*. In 
other words, all model entities referenced inside a function definition 
must be passed arguments to that function. 

References: L2V2 Section 4.3.2. 


The %<%ci%>% element *y* on line 15 refers to variable that is not a %<%bvar%>% element
of the functionDefinition:
		
{>}%<%functionDefinition id="pow"%>%\\
%<%math xmlns="http://www.w3.org/1998/Math/MathML"%>%\\
%<%lambda%>%\\
%<%bvar%>% %<%ci%>% x %<%/ci%>%  %<%ci%>% b %<%/ci%>%%<%/bvar%>%\\
%<%apply%>%\\
%<%power/%>%\\ 
%<%ci%>% x %<%/ci%>%\\ 
%<%ci%>% y %<%/ci%>% 
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