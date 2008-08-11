category:      SBML_NOTES
synopsis:      Notes must be in XHTML Namespace.
levels:        2.2, 2.3
status:        Pass

This model passes SBML validation rule *10801*:     

The contents of the *%<%notes%>%* element must be explicitly placed in the
XHTML XML namespace.

References: L2V2 Section 3.3.2; L2V3 Section 3.2.3.


In this case the *%<%body%>%* element on line 5 declares the XHTML namespace:

{>}%<%model%>%\\
%<%notes%>%\\
%<%body xmlns="http://www.w3.org/1999/xhtml"%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML_NOTES  |
| *Status:*| Pass  |
| *Levels:*| L2V2 |
| | L2V3 |]
