category:      SBML
synopsis:      Invalid unit definition id.
levels:        1.2, 2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20401*:     

The value of the _id_ attribute in a *%<%unitDefinition%>%* must be of 
type 'UnitSId' and not be identical to any unit predefined in SBML. 

References: L2V1 erratum 14; L2V2 Section 4.4.2; L2V3 Section 4.4.2.


The _id_ attribute on line 5 is identical to the predefined unit 'litre':
		
{>}%<%listOfUnitDefinitions%>%\\
%<%unitDefinition id="litre"/%>%
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML  |
| *Status:*| Fail  |
| *Levels:*| L1V2 |
| | L2V1 |
| | L2V2 |
| | L2V3 |]