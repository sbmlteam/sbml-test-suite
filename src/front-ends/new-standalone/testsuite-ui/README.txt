
                          SBML Test Runner:
                  A Graphical User Interface for the
                           SBML Test Suite

                 Michael Hucka and Frank T. Bergmann
                 Computing and Mathematical Sciences
                  California Institute of Technology
                      Pasadena, California, USA

          This code is licensed under the LGPL version 2.1.
          Please see the file "../LICENSE.txt" for details.


INTRODUCTION
======================================================================

The SBML Test Runner is a standalone program that can be used to run
an application through the SBML Test Suite.  The SBML Test Suite is a
conformance testing system for SBML, used to test the degree and
correctness of SBML support provided in an SBML-compatible software
package.  The SBML Test Suite consists of (1) a collection of SBML
models, each with associated simulated results where appropriate, and
(2) the SBML Test Runner, for controlling applications to make them
run through each test case in the suite.

The SBML Test Runner is written in Java and uses the SWT (Standard
Widget Toolkit) in its graphical user interface.  If you obtained a
binary distribution of the SBML Test Runner, then you can start it
simply by double-clicking the application shortcut "SBML Test Runner"
(or invoking it from a command line, if you prefer).  If you obtained
the source code distribution, you will need to build the application
first, using the instructions below.


BUILDING THE SBML TEST RUNNER
======================================================================

If you obtained the source code distribution of the SBML Test Runner,
then to build the JAR file, you will need Java version 1.6 or later,
as well as Ant, the Apache Java build tool. Note that because SWT uses
native code graphical widgets, the SBML Test Runner JAR file is
architecture-specific -- you must build a version for the operating
system and machine word size (32-bit or 64-bit) you will run it on.

The Ant configuration understands the following commands.  All outputs
are placed in the directory "dist".

  ant jar                                                           
    Creates a self-contained jar file.  Whether it is 32-bit or 64-bit
    is determined automatically based on the current platform.  The
    result can be executed using "jar -jar sbmltestrunner.jar"
                                                                  
  ant app                                                           
    Creates *two* executable applications that wrap the jar file, 
    one executable for 32-bit and another for 64-bit versions of  
    the current platform. The result can be executed as a normal  
    application (e.g., by doing-clicking it). The result still    
    requires Java to be installed on the computer, however.       
                                                                  
  ant dist                                                          
    Creates a distribution with both 32-bit & 64-bit versions of  
    the application. The form of the distribution depends on the  
    platform currently running: on Macs, it creates a .dmg disk   
    archive, on Windows it creates a runnable .exe installer, and 
    on Linux, it creates 3 items: a runnable binary installer, an 
    RPM package, and a Debian .deb package.                       

To build the executable applications, you will need some additional
tools, depending on the operating system:

  Mac: You will need JarBundler (http://informagen.com/JarBundler).

  Windows: You will need Launch4j (http://launch4j.sourceforge.net)
  to create the application and NSIS (http://nsis.sourceforge.net)
  to create the distribution installer.

  Linux: You will need InstallBuilder but *only* to create the
  distribution installer.  (You can create the standalone application
  without InstallBuilder.)  


STARTING THE SBML TEST RUNNER
======================================================================

Depending on whether you chose to create only a jar file or a
self-contained application, you can use one of the following
approaches to starting the SBML Test Runner:

* If you created only the jar file:

  On Mac OS X, execute the following command in a terminal shell:

    java -XstartOnFirstThread -jar dist/sbmltestrunner.jar

  On Windows and Linux: execute the following command instead:

    java -jar dist/sbmltestrunner.jar

* If you created the standalone application:

  On all systems, you can simple double-click the application to
  start, or start the application from a terminal shell command line.


USING THE SBML TEST RUNNER
======================================================================

Forthcoming...


REPORTING BUGS AND OTHER PROBLEMS
======================================================================

Please report bugs and other issues to the developers at the following
email address:

  sbml-team@caltech.edu


ACKNOWLEDGMENTS
======================================================================

This and other projects of the SBML Team have been supported by the
following organizations: the National Institutes of Health (USA) under
grants R01 GM070923 and R01 GM077671; the International Joint Research
Program of NEDO (Japan); the JST ERATO-SORST Program (Japan); the
Japanese Ministry of Agriculture; the Japanese Ministry of Education,
Culture, Sports, Science and Technology; the BBSRC e-Science
Initiative (UK); the DARPA IPTO Bio-Computation Program (USA); the
Army Research Office's Institute for Collaborative Biotechnologies
(USA); the Air Force Office of Scientific Research (USA); the
California Institute of Technology (USA); the University of
Hertfordshire (UK); the Molecular Sciences Institute (USA); the
Systems Biology Institute (Japan); and Keio University (Japan).

This version of the SBML Test Runner is fashioned after the WIN32
application previously written by Frank T. Bergmann in .NET:

           http://sourceforge.net/projects/compare-results/




======================================================================
# The following is for [X]Emacs users.
# Local Variables:
# fill-column: 70
# End:
======================================================================
