
                           SBML Test Suite

                     Brief explanation about the
                "test_suite" web application directory
                 /var/lib/tomcat5/webapps/test_suite

                  Michael Hucka and Kimberly Begley

              For more information about SBML, contact:

                            The SBML Team
                         http://www.sbml.org/
                  mailto:sbml-team@googlegroups.com

This directory is the source directory for the Online SBML Test Suite.
On the server for sbml.org, it is /var/lib/tomcat5/webapps/test_suite/.

The NORMAL way of installing a web application for Tomcat is to
produce and deploy a .war file.  The directory contents here are set
up so that you can do that, but it is ALSO set up so that this whole
directory can be checked out from SVN as the web application directory
itself on the server (i.e., /var/lib/tomcat5/webapps/test_suite/), so
that file updates & Java class recompilation and other things can be
done directly on the server.  Beware that if you do this, DO NOT
DELETE THE .war file (/var/lib/tomcat5/webapps/test_suite.war).  If
you remove the .war file, Tomcat will delete the application directory
too (/var/lib/tomcat5/webapps/test_suite), taking with it any of your
changes :-(.


---------------------------------
Explanation of directory contents
---------------------------------

The "index.html" page is just a redirection page, to guard against
people trying to access http://sbml.org:8080/test_suite directly.  It
serves no other functional purpose.

The "web" subdirectory contains the JSP and HTML pages that are used
to produce the front-end web pages.  This is the interface that users
interact with through their browser.  The different JSP pages are
accessed via wrapper web pages that reside on sbml.org in 
http://sbml.org/Facilities/Online_SBML_Test_Suite

The "WEB-INF" contains the sources for the back-end programs that
implement the server-side functionality for running tests.  These Java
files are located in WEB-INF/src.  The Makefile at the top level
(i.e., the same level where this README.txt file is located) is used
to compile the Java files and put the results into WEB-INF/classes.

In the deployed installation of this system, a copy of the test suite
test cases must be put into a directory named "test-suite".  That is,
the /var/lib/tomcat5/webapps/test_suite/ directory should have
    
    META-INF/
    README.txt
    WEB-INF/
    index.html
    web/
    test-cases/           <<< symbolic link that must be added

We use a symbolic link for test-cases that points to the actual
collection of test cases.  Note that it is purposefully NOT simply a
link to the cases/semantic directory in the SVN repository for the
test suite.  We have to control the specific release of the test cases
provided online and in the standalone application, so a separate copy
of the test cases directory must be made when we make a public release
or update of the Test Suite.  (Programmer's note: if the symlink name
"test-cases" is moved or renamed, make sure to change the references
to the directory in WEB-INF/src/sbml/test/UploadUnzipTest.java and
web/process.jsp.)
    
------------
Installation
------------

Once this directory is checked out on the server in place as
/var/lib/tomcat5/webapps/test_suite/, run "make classes" to compile
the Java classes, create the symlink "test-cases" as described above,
and run the command "touch WEB-INF/web.xml".

---------------------
COPYRIGHT AND LICENSE
---------------------

This file is part of the SBML Test Suite.  Please visit http://sbml.org for
more information about SBML, and the latest version of the SBML Test Suite.

Copyright (C) 2010-2015 jointly by the following organizations: 
    1. California Institute of Technology, Pasadena, CA, USA
    2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
    3. University of Heidelberg, Heidelberg, Germany

Copyright (C) 2008-2009 California Institute of Technology (USA).

Copyright (C) 2004-2007 jointly by the following organizations:
    1. California Institute of Technology (USA) and
    2. University of Hertfordshire (UK).

The SBML Test Suite is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public License as
published by the Free Software Foundation.  A copy of the license
agreement is provided in the file named "LICENSE.txt" included with
this software distribution and also available at
http://sbml.org/Software/SBML_Test_Suite/license.html







----------------------------------------------------------------------
The following is for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
----------------------------------------------------------------------
