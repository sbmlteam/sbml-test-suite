
                           SBML Test Suite

                     Brief explanation about the
                "test_suite" web application directory
                 /var/lib/tomcat5/webapps/test_suite

                  Kimberly Begley and Michael Hucka

              For more information about SBML, contact:

                            The SBML Team
                         http://www.sbml.org/
                     mailto:sbml-team@caltech.edu

This directory is the source directory for the Online SBML Test Suite.
It can be used to produce a .war file to be loaded into a Tomcat 5 web
application server.

---------------------------------
Explanation of directory contents
---------------------------------

The "index.html" page is just a redirection to guard against people
trying to access http://sbml.org:8080/test_suite directly.  It serves
no other functional purpose.

The "web" subdirectory contains the JSP and HTML pages that are used
to produce the front-end web pages.  This is the interface that users
interact with through their browser.  The different JSP pages are
accessed via wrapper pages that reside on sbml.org in 
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
    test-cases/           <<< must be added

The test-cases is NOT a link to the test cases directory in SVN.  We
have to control the specific release snapshot of the test cases
provided online and in the standalone application, so a separate copy
of the test cases directory must be made when we make a public release
or update of the Test Suite.  (Programmer's note: if the directory
"test-cases" is moved or renamed, make sure to change the references
to the directory in WEB-INF/src/sbml/test/UploadUnzipTest.java and
web/process.jsp.)
    


----------------------------------------------------------------------
$Id$
$HeadURL$

The following is for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
----------------------------------------------------------------------
