category:      SBML_NOTES
synopsis:      Invalid notes contents.
levels:        2.2, 2.3
status:        Fail

This model fails SBML validation rule *10804*:     

The XHTML content inside a *%<%notes%>%* element can only take one of the
following general forms: (1) a complete XHTML document beginning with
the element *%<%html%>%* and ending with *%<%/html%>%*; (2) the "body" portion of
a document beginning with the element *%<%body%>%* and ending with *%<%/body%>%*; or
(3) XHTML content that is permitted within a *%<%body%>%* ... *%<%/body%>%* element.

References: L2V2 Section 3.3.2; L2V3 Section 3.2.3.


Note: In case (3) above, if there are multiple elements, each element must 
explicitly declare the XHTML namespace.


Both *%<%p%>%* elements on lines 5 and 6 declare the XHTML namespace implicitly:

{>}%<%notes%>%\\
%<%xhtml:p%>% %<%/xhtml:p%>%\\
%<%xhtml:p%>% %<%/xhtml:p%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML_NOTES  |
| *Status:*| Fail  |
| *Levels:*| L2V2 |
| | L2V3 |]