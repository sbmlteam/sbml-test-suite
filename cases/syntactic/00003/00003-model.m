category:      SBML
synopsis:      A species requires a compartment.
levels:        1.2, 2.1, 2.2, 2.3
status:        Pass

This model passes SBML validation rule *20204*:     

If a model defines any *species*, then the model must also define at 
least one *compartment*. This is an implication of the fact that the
_compartment_ attribute on the *%<%species%>%* element is not optional.


References: L2V1 Section 4.5; L2V2 Section 4.8.3; L2V3 Section
4.8.3.
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML  |
| *Status:*| Pass  |
| *Levels:*| L1V2 |
| | L2V1 |
| | L2V2 |
| | L2V3 |]