
	       Explanation of this directory's purpose

			    Michael Hucka
			 http://www.sbml.org/
                  mailto:sbml-team@googlegroups.com

This directory contains the unmodified Rhino version 1.6 R7
distribution.  Rhino is an implementation of a JavaScript interpreter
written in Java.  The original source for Rhino 1.6 R7 was obtained
from http://www.mozilla.org/rhino/download.html in February 2008.

The SBML Test Suite uses Rhino in the program "desc2html" whose source
is in the <test-suite>/src/desc2html directory (where <test-suite> is
the root of the SBML Test Suite source tree).

We include the entire Rhino 1.6 R7 distribution so that the SBML Test
Suite is insulated against changes to Rhino over time.  The SBML Test
Suite currently does not use much of the functionality of Rhino
anyway, so we do not anticipate a need for tracking improvements in
Rhino in the future.

The Rhino licensing terms are described in the file LICENSE.html
included in this directory; the file is a copy of the file available 
at http://www.mozilla.org/MPL/MPL-1.1.html


----------------------------------------------------------------------
$Id$
$HeadURL$

The following lines are for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
----------------------------------------------------------------------
