category:      SBML
synopsis:      Restricted top-level element within math in a functionDefinition.
levels:        2.3
status:        Fail

This model fails SBML validation rule *20301*:     

The top-level element within *%<%math%>%* in a *%<%functionDefinition%>%* must be
one and only one *%<%lambda%>%* or a *%<%semantics%>%* element containing
one and only one *%<%lambda%>%*.

References: L2V3 Section 4.3.2.


Either a *%<%lambda%>%* element or a *%<%semantics%>%* element is missing 
from the *%<%math%>%* element on line 7:
		
{>}%<%functionDefinition id="pow3"%>%\\
%<%math xmlns="http://www.w3.org/1998/Math/MathML"%>%\\
%<%apply%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML  |
| *Status:*| Fail  |
| *Levels:*| L2V3 |]