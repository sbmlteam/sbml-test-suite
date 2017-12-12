
			   SBML Test Suite
             "plotresults":  plot simulation case results

			    Michael Hucka
			 http://www.sbml.org/
                  mailto:sbml-team@googlegroups.com

This is a simple program for plotting the results of a semantic case
simulation.  It produces an HTML file that uses JavaScript libraries
to plot the results.

Synopsis:
          plotresults.py -d XXXXX-results.csv -o XXXXX-plot.html

The input file given by the -d argument is assumed be a
comma-separated value file.  The input file should normally have a
name of the form XXXXX-results.csv.

To find out about other options, run

          plotresults.py --help

----------------------------------------------------------------------
Copyright 2008-2012 California Institute of Technology.

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
