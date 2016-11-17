
	    Imported software used by the SBML Test Suite

			    Michael Hucka
			 http://www.sbml.org/
                  mailto:sbml-team@googlegroups.com

------------
Introduction
------------

This directory contains open-source software produced by other groups
and used by the SBML Test Suite.  Please see the subdirectories under
here for explanations of what the software is and their licensing
terms.

Please see the file README.txt in the root of the test-suite source
direcory (../../README.txt) for more information about the SBML Test
Suite itself, or visit http://sbml.org.

------------
Installation
------------

There is currently no installation script or makefile.

The following files must be copied manually to the directory
/var/lib/tomcat5/webapps/test_suite/WEB-INF/lib/ on server for the
Online SBML Test Suite (i.e., on http://sbml.org):

  commons-io/commons-io-1.4.jar
  commons-fileupload/commons-fileupload-1.2.jar
  lib/jstl.jar from jakarta-standard-taglib/jakarta-taglibs-standard-1.1.2.tar.gz
  lib/standard.jar from jakarta-standard-taglib/jakarta-taglibs-standard-1.1.2.tar.gz

For "wiky", "rhino" and "batik", nothing needs to be done.  The
relevant scripts use them directly from these directories.


---------------------------------------------------------------------
$Id$
$HeadURL$

The following lines are for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
----------------------------------------------------------------------
