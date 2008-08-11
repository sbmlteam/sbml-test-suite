category:      SBML
synopsis:      Empty list in model.
levels:        1.2, 2.1, 2.2, 2.3
status:        Pass

This model passes SBML validation rule *20203*:     

The %<%listOf___%>% containers in a *%<%model%>%* are optional, but if present, 
the lists cannot be empty. Specifically, if any of the following are 
present in a *%<%model%>%*, they must not be empty: 
%<%listOfFunctionDefinitions%>%, %<%listOfUnitDefinitions%>%, 
%<%listOfCompartmentTypes%>%, %<%listOfSpeciesTypes%>%, %<%listOfCompartments%>%,  
%<%listOfSpecies%>%, %<%listOfParameters%>%, %<%listOfInitialAssignments%>%, 
%<%listOfRules%>%, %<%listOfConstraints%>%, %<%listOfReactions%>% and 
%<%listOfEvents%>%.

References: This is a requirement stemming from the XML Schema used for SBML; 
L2V3 Section 4.2. 
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
