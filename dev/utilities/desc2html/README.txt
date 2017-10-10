			   SBML Test Suite
		     "desc2html" utility program

			    Michael Hucka
			 http://www.sbml.org/
                  mailto:sbml-team@googlegroups.com

"desc2html.sh" is a simple program for pulling out descriptions from
SBML Test Suite test case definition files (which are Mathematica .m
files) and generating HTML versions of the descriptions.  It uses a
wiki markup language called "Wiky" (the source code for which is
provided in <test-suite>/src/imported/wiky) and the Mozilla Rhino
version 1.6 JavaScript interpreter (the source code for which is
provided in <test-suite>/src/imported/rhino).

Synopsis:
          desc2html.sh XXXXX-model.m XXXXX-model.html

The detailed usage instructions for the program are provided at the
beginning of the file desc2html.sh.

---------------------------------------------------------------------
Copyright 2008-2010 California Institute of Technology.

The SBML Test Suite is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation.  A copy of the license
agreement is provided in the file named "LICENSE.txt" included with
this software distribution and also available at
http://sbml.org/Software/SBML_Test_Suite/license.html

$Id$
$HeadURL$

The following lines are for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
----------------------------------------------------------------------
