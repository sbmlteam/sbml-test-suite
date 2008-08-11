category:      SBML_ANNOTATION
synopsis:      SBML Namespace in an annotation.
levels:        2.2, 2.3
status:        Fail

This model fails SBML validation rule *10403*:     

Top-level elements within an *%<%annotation%>%* element cannot use any SBML
namespace, whether explicitly (by declaring the namespace to be one of
the URIs "http://www.sbml.org/sbml/level1",
"http://www.sbml.org/sbml/level2",
"http://www.sbml.org/sbml/level2/version2", or
"http://www.sbml.org/sbml/level2/version3", or implicitly (by failing 
to declare any namespace).

References: L2V2 Section 3.3.3; L2V3 Section 3.2.4. 


The *%<%annotation%>%* element declares the sbml namespace on line 7:

{>}%<%annotation%>%\\
%<%rdf:RDF xmlns:sbml="http://www.sbml.org/sbml/level2" ...
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML_ANNOTATION  |
| *Status:*| Fail  |
| *Levels:*| L2V2 |
| | L2V3 |]