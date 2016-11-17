
	       Explanation of this directory's purpose

			    Michael Hucka
			 http://www.sbml.org/
                  mailto:sbml-team@googlegroups.com

This directory contains a version of the Wiky program available from
http://goessner.net/articles/wiky/.  Wiky was written by Stefan
Goessner.

The SBML Test Suite uses Wiky in the program "desc2html" whose source
is in the <test-suite>/src/desc2html directory (where <test-suite> is
the root of the SBML Test Suite source tree).

For the purposes of the SBML Test Suite, we made small modifications
to the original Wiky code.  These modifications are:

1. Combined the math markup support into the main Wiky JavaScript file
   (the original program used two .js files)

2. Added code to the main JavaScript file allowing it to read input
   from a file named on the command line.  This modification uses
   Rhino-specific features.

3. Reformatted the code inside the file.

The Wiky distribution terms are described in the file LICENSE.html
included in this directory; the file is a copy of the file available
at http://creativecommons.org/licenses/LGPL/2.1/


----------------------------------------------------------------------
$Id$
$HeadURL$

The following lines are for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
----------------------------------------------------------------------
