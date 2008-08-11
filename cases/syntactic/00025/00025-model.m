category:      SBML_NOTES
synopsis:      Notes must be in XHTML Namespace.
levels:        2.2, 2.3
status:        Pass

This model passes SBML validation rule *10801*:     

The contents of the *%<%notes%>%* element must be explicitly placed in the
XHTML XML namespace.

References: L2V2 Section 3.3.2; L2V3 Section 3.2.3.


In this case the *%<%sbml%>%* element on line 2 declares the XHTML namespace:

{>}%<%sbml xmlns="http://www.sbml.org/sbml/level2/version3" level="2" version="3"
 		xmlns:xhtml="http://www.w3.org/1999/xhtml"%>%\\
%<%model%>%\\
%<%notes%>%\\
%<%xhtml:body%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML_NOTES  |
| *Status:*| Pass  |
| *Levels:*| L2V2 |
| | L2V3 |]
