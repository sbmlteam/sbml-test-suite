category:      SBML_NOTES
synopsis:      Notes must be in XHTML Namespace.
levels:        2.2, 2.3
status:        Fail

This model fails SBML validation rule *10801*:     

The contents of the *%<%notes%>%* element must be explicitly placed in the
XHTML XML namespace.

References: L2V2 Section 3.3.2; L2V3 Section 3.2.3.


The *%<%notes%>%* element on line 4 has no XHTML namespace declared:

{>}%<%sbml xmlns="http://www.sbml.org/sbml/level2/version3" level="2" version="3"%>%\\
%<%model%>%\\
%<%notes%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML_NOTES  |
| *Status:*| Fail  |
| *Levels:*| L2V2 |
| | L2V3 |]