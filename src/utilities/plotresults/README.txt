
			   SBML Test Suite
      "plotresults":  plot simulation case results using gnuplot

			    Michael Hucka
			 http://www.sbml.org/
		     mailto:sbml-team@caltech.edu

This is a simple program for plotting the results of a semantic case
simulation.  This script assumes gnuplot version 4.2, and that gnuplot
has been compiled with the --enable-datastrings option.

Synopsis:
          plotresults.sh XXXXX-results.csv

The program takes the input file (assumed be a comma-separated value
file) and runs it through gnuplot to produce a plot in a JPEG file.
The output file is named after the basename of the input file.  The
input file should normally have a name of the form XXXXX-results.csv.

----------------------------------------------------------------------
Copyright 2008-2010 California Institute of Technology.

The SBML Test Suite is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation.  A copy of the license
agreement is provided in the file named "LICENSE.txt" included with
this software distribution and also available at
http://sbml.org/Software/SBML_Test_Suite/license.html

----------------------------------------------------------------------
$Id$
$HeadURL$

The following lines are for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
----------------------------------------------------------------------
