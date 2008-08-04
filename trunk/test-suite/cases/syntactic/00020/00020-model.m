category:      XML
synopsis:      Badly formed XML.
levels:        1.2, 2.1, 2.2, 2.3
status:        Fail

This document has a document type declaration in an incorrect position. Document type declarations must
occur before the first element.

{>}%<%model%>%\\	
%<%!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| XML  |
| *Status:*| Fail  |
| *Levels:*| L1V2 |
| | L2V1 |
| | L2V2 |
| | L2V3 |]