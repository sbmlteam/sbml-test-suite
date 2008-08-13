category:      SBML
synopsis:      Forward reference to functionDefinitions not allowed.
levels:        2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20302*:     

Inside the %<%lambda%>% of a *%<%functionDefinition%>%*, if a %<%ci%>% element is the 
first element within a MathML %<%apply%>%, then the %<%ci%>%'s value can only 
be chosen from the set of identifiers of other SBML 
%<%functionDefinition%>%s defined prior to that point in the SBML model. In 
other words, forward references to user-defined functions are not 
permitted.

References: L2V2 Section 4.3.2; L2V3 Section 4.3.2.


The %<%ci%>% element *add* refers to a functionDefinition that has not yet been defined:
		
{>}%<%apply%>%\\
%<%ci%>%add %<%/ci%>% %<%ci%>% x %<%/ci%>% %<%ci%>% y %<%/ci%>%
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