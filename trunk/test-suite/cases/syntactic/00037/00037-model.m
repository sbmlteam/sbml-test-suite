category:      SBML
synopsis:      Restricted top-level element within math in a functionDefinition.
levels:        2.1, 2.2
status:        Fail

This model fails SBML validation rule *20301*:     

The top-level element within *%<%math%>%* in a *%<%functionDefinition%>%* must be
one and only one *%<%lambda%>%*.

References: L2V1 Section 4.3.2; L2V2 Section 4.3.2.


The *%<%lambda%>%* element is missing from the *%<%math%>%* element on line 7:
		
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
| *Levels:*| L2V1 |
| | L2V2 |]