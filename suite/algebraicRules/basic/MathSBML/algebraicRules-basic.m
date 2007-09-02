m=SBMLRead["algebraicRules-basic-l2.xml", context->"SBML"]
r=SBMLNDSolve[m,8, AccuracyGoal->11, PrecisionGoal->6]
p=SBMLPlot[r]
Export["algebraicRules-basic.gif",p,ImageSize->750]
plog=SBMLPlot[r, type->"Log"]
Export["algebraicRules-basic-log.gif",plog,ImageSize->750]
SBMLWrite[inputfile->"algebraicRules-basic-l2.xml", outputfile->"algebraicRules-basic.html", format->"html"]
results = Table[Flatten[{t, N /@ ({SBML`x[t], SBML`y[t], SBML`z[t] } /. r)}], {t, 0, 8, 0.2}]
results = Prepend[results, {"time","x", "y", "z"}]
Export["algebraicRules-basic.csv", results]