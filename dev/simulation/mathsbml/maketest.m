(* Version of 2008-02-11 *)

newcase[num_] := (
  newModel["case"<>num, level -> 2, version -> 3];
  modelnumber = num;
);

makemodel[] := (
  m = loadSimulator[];
);

makesymbol[x_] := Symbol["case"<>modelnumber<>"`"<>ToString[x]];

runtest[duration_, species_] := Module[ {},
  totalduration = duration;
  stepsize = N[duration/50, 6];
  specieslist = makesymbol/@species;

  r = SBMLNDSolve[ m, duration, AccuracyGoal -> 30, PrecisionGoal -> 12];
  p = SBMLPlot[ r, species, AxesLabel -> {"Time", ""} ];
];

zeroThreshold[x_?NumberQ, threshold_] := If[Abs[x] < threshold, 0, x]; 
zeroThreshold[{x___}, threshold_] := zeroThreshold[#, threshold] & /@ {x};
zeroThreshold[x_, threshold_] := x;

createoutput[] := Module[{pwd, sbmlfile}, 
  pwd      = Directory[];
  sbmlfile = modelnumber<>"-sbml-l2v3.xml";
  outfile  = modelnumber<>"-results.csv";
  jpgfile  = modelnumber<>"-plot.jpg";
  epsfile  = modelnumber<>"-plot.eps";

  If[ FileType[sbmlfile] == File, DeleteFile[sbmlfile] ];
  createModel[ sbmlfile, "annotations"->False, "notes"-> False, 
                    comments-> "False" ];
  
  data = dataTable[ specieslist, {t, 0, totalduration, stepsize}, r];
  data = zeroThreshold[ data, 10^-30 ];
  
  Export[ outfile, data, "CSV" ];

  Export[ jpgfile, p, "JPG" ];
  Export[ epsfile, p, "EPS" ];

  SetDirectory[pwd];

  Print["Output written to ", outfile, ", ", jpgfile, ", and ", epsfile ];
];

createsbml[] := Module[{pwd, sbmlfile}, 
  pwd      = Directory[];
  sbmlfile = modelnumber<>"-sbml-l2v3.xml";

  If[ FileType[sbmlfile] == File, DeleteFile[sbmlfile] ];
  createModel[ sbmlfile, "annotations"->False, "notes"-> False, 
                    comments-> "False" ];
  
  SetDirectory[pwd];

  Print["Output written"];
];

createresults[] := Module[{pwd, sbmlfile}, 
  pwd      = Directory[];
  outfile  = modelnumber<>"-results.csv";

  
  data = dataTable[ specieslist, {t, 0, totalduration, stepsize}, r];
  data = zeroThreshold[ data, 10^-30 ];
  
  Export[ outfile, data, "CSV" ];
  SetDirectory[pwd];

  Print["Output written to ", outfile ];
];

runacc[duration_, species_] := Module[ {},
  totalduration = duration;
  stepsize = N[duration/50, 6];
  specieslist = makesymbol/@species;

  r = SBMLNDSolve[ m, duration];
  p = SBMLPlot[ r, variables -> species, AxesLabel -> {"Time", ""} ];
];
