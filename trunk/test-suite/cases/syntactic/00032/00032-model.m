category:      SBML
synopsis:      Incorrect order in model.
levels:        1.2, 2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *20202*:     

The order of subelements within a *%<%model%>%* must be the following (where 
any one may be optional, but the ordering must be maintained): 
%<%listOfFunctionDefinitions%>%, %<%listOfUnitDefinitions%>%, 
%<%listOfCompartmentTypes%>%, %<%listOfSpeciesTypes%>%, %<%listOfCompartments%>%, 
%<%listOfSpecies%>%, %<%listOfParameters%>%, %<%listOfInitialAssignments%>%, 
%<%listOfRules%>%, %<%listOfConstraints%>%, %<%listOfReactions%>% 
and %<%listOfEvents%>%. 

References: L2V2 Section 4.2; L2V3 Section 4.2.


The *%<%listOfCompartments%>%* element on line 7 is after the *%<%listOfSpecies%>%* element:

{>}%<%/listOfSpecies%>%\\
%<%listOfCompartments%>%
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