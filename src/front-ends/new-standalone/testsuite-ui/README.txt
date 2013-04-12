
                          SBML Test Runner:
                  A Graphical User Interface for the
                           SBML Test Suite

                 Frank T. Bergmann and Michael Hucka
                 Computing and Mathematical Sciences
                  California Institute of Technology
                      Pasadena, California, USA

          This code is licensed under the LGPL version 2.1.
          Please see the file "../LICENSE.txt" for details.


INTRODUCTION
======================================================================

This directory contains the source code for a standalone application
called the SBML Test Runner.  It is written in Java and uses the SWT
(Standard Widget Toolkit) for creating the graphical user interface.


BUILDING AND INSTALLING THE APPLICATION
======================================================================

To build the SBML Test Runner, you will need Java version 1.6 or
later, and Ant, the Apache Java build tool.  Note that because SWT
uses native code graphical widgets, the SBML Test Runner JAR file is
architecture-specific -- you have to build a version for the operating
system and machine word size (32-bit or 64-bit) you will run it on.

You can use one of the following commands to build a self-contained,
runnable JAR file:

  # This will build a self-contained .jar file, defaulting to a 64-bit
  # architecture for the operating system you're using:

  ant runnable-jar
  
  # If you need to explicit request a 32-bit or 64-bit .jar file, use
  # one of the following commands

  ant runnable-jar-32
  ant runnable-jar-64

(Additional build targets are defined in the file build.xml; for
example, there are targets to let you cross-compile the application
for a different operating system than the one you're running on.)
Unless problems arise, the outcome of running one of the commands
above should be a file named "sbmltestrunner.jar".

To run the application, if you are using Mac OS X, execute the
following command in a terminal shell:

  java -XstartOnFirstThread -jar sbmltestrunner.jar

On all other systems, run

  java -jar sbmltestrunner.jar

To save you the trouble of doing all of the above and provide a
standalone application you can simple double-click to start, we also
make precompiled, standalone applications available.  Please visit the
SBML Test Suite download site to find them:

  https://sourceforge.net/projects/sbml/files/test-suite/


USING THE APPLICATION
======================================================================


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
# The following is for [X]Emacs users.  Please leave in place.

# Local Variables:
# fill-column: 70
# End:
======================================================================
