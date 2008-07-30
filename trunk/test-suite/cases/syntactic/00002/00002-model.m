category:      SBML_MATHML
synopsis:      Permitted attribute encoding in MathML.
levels:        2.1, 2.2, 2.3
status:        Fail

This model fails SBML validation rule *10203*:     

In the SBML subset of MathML 2.0, the MathML attribute _encoding_ is only 
permitted on *csymbol*, *annotation* and *annotation-xml*.  No other MathML 
elements may have an _encoding_ attribute. 

References: L2V2 Section 3.5.1; L2V3 Section 3.4.1.


The model uses the _encoding_ attribute on the ci element on line 17:
		
{>}%<%ci encoding="text"%>% p %<%/ci%>% 
\\
\\
----

[{width:10em}| | |
|2> *TEST SUMMARY* |
| *Category:*| SBML_MATHML  |
| *Status:*| Fail  |
| *Levels:*| L2V1 |
| | L2V2 |
| | L2V3 |]