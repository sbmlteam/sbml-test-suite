category:      SBML
synopsis:      Annotation/notes not allowed on sbml element.
levels:        1.2
status:        Fail

This model fails SBML validation rule *20104*:     

The *%<%sbml%>%* container element cannot contain *%<%notes%>%* or *%<%annotations%>%* in
an SBML Level 1 document.

References: L1V2 Appendix B 

There is an annotation element within the sbml element on line 3:
		
{>}%<%sbml  xmlns="http://www.sbml.org/sbml/level1" level="1" version="2"%>%\\
%<%annotation/%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML  |
| *Status:*| Fail  |
| *Levels:*| L1V2 |]