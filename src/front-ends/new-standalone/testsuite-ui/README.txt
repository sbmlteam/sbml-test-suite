
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
platform-system specific -- you must build a version for the operating
system and machine word size (32-bit or 64-bit) you will run it on,
unlike typical platform-independent Java applications.

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

To build the executable applications and distributions ("ant jar" and
"ant dist", but *not* the plain "ant jar"), you will need some
additional tools, depending on the operating system:

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

1. Defining wrappers
--------------------

The execution of tests involves the use of "wrapper" programs that act
as interfaces between the SBML Test Runner and the application being
tested.  The SBML Test Runner invokes this "wrapper" for each test
case in the suite, passing command-line arguments to the wrapper to
indicate such things as which case to run and where to find the files.
It is the responsibility of the wrapper to invoke the application to
run the case in whatever fashion is appropriate for that application.
A wrapper can be simple (e.g., a shell script) or a small program all
on its own, depending on the application being tested.  (Some
applications may not need a wrapper at all, if they can understand the
necessary command line arguments directly and produce output in the
format needed by the SBML Test Runner.)

When you first start the Runner, you will not have any wrapper
configurations defined.  The Runner will open a Preferences panel to
let you define one or more wrapper configurations.  Each configuration
has:

  1. a name

  2. the path to the wrapper program on your computer

  3. the path to a directory where the Runner will find the
     application's output from running each test case

  4. a list of component or test tags that the application is known
     to be unable to understand

  5. command-line arguments that should be passed to the wrapper

In the command-line arguments given to the wrapper, the following
substitution codes can be used.  Their values will be substituted at
run-time whenever the wrapper is invoked:

  %d  the path to the directory containing all the test cases
  %n  the current test case number (in the form of 5 digits)
  %o  the directory where the output will be written (same as #3 above)
  %l  the SBML Level of the test case
  %v  the SBML Version within the Level of the test case

Each test case consists of an SBML file and a settings file. The files
are located in the directory named %d/%n. The SBML file for the test
model is named "%n-sbml-l%lv%v.xml". (Example: "00123-sbml-l2v3.xml".)
The settings file is named "%n-settings.txt" in the same directory.
The wrapper must be instructed to write out the results into a file
named "%o/%n.csv" so that the SBML Test Runner can find it.

The definition of a wrapper also includes 3 options:

 * "Pseudo-wrapper to view test cases only": When set, this option
   defines a wrapper as non-executable and disables some of the fields
   in the preferences panel.  Use this option to define a wrapper for
   viewing existing results (e.g., results created in some other
   fashion than through the SBML Test Runner).  The wrapper definition
   allows you to define the directory where the results will be found,
   as well as unsupported tags, but not program path or arguments.

 * "Wrapper can handle any SBML Level/Version": Some applications may
   not be able to accept any SBML Level and Version.

 * "Wrapper can be run in parallel": The Runner can execute tasks in
   parallel, but it must be informed whether multiple instances of a
   wrapper or application can be invoked simultaneously.  Use this
   flag to indicate that multiple copies of the wrapper can be started
   simultaneously.  Do not enable this option if the wrapper (or
   application) writes to the same file (e.g., a single log file),
   locks a single resource, or does something else that would result
   in non-deterministic behavior if multiple copies of the wrapper or
   application are started simultaneously.

You can define multiple wrappers, but only one will be executed during
any given test run.  (You will be able to choose it from a pull-down
menu in the main window of the SBML Test Runner.)


2. Running tests
----------------

The basic operation of the SBML Test Runner consists of running tests
(or more precisely, running an SBML-aware application via a wrapper),
and viewing the results.  The main panel of the SBML Test Runner is
oriented towards this purpose.  The main window consists of a toolbar
and 5 regions below it:

  * a list of test case numbers vertically along the left-hand side

  * an upper panel showing two graphs, one depicting the expected
    results and the other the actual results for a selected test case

  * a lower panel showing a graph of the differences between the
    values of the expected and actual results
  
  * a description area below the graphs, describing the purpose and
    features of the currently-selected test case

  * a status area at the very bottom, with a progress bar and other
    information about the current state of the SBML Test Runner

After you start the runner, initially there will not be any test cases
selected or graphs shown.  If you click on the triangle-shaped "Run"
icon in the middle of the toolbar (or select the "Run" option in the
menubar pull-down menu named "Test"), the SBML Test Runner will
proceed to invoke the wrapper for every test case in the suite,
starting at case 00001.  The status area in the bottom of the main
window provides a real-time sense for the progress; you can also open
a map view of the results (via the grid-shaped icon in the far
left of the tool bar) and the map will be updated as the run
continues.  You can pause execution at any time by clicking on the
double-bar-shaped "Pause" button that becomes visible while a run is
in progress.

At any time, even while a run is in progress, you can select a test
case from the list in the left-hand side of the main window.  The
upper and lower panels of the main window will then display the graphs
of the results, and the description area will provide some details
about the selected test case.

While a case is select, or while multiple cases are selected, clicking
on the "Run" button or selecting the "Run" option from the menubar
will run only the selected test cases, and not all test cases.  To run
all the tests again from the beginning, you must first clear the
selections from the list in the left-hand side.  There is a toolbar
button for quickly deselecting everything; you can also select the
"Deselect All" option from the "Edit" pull-down menu in the menubar.

3. Filtering
------------

It is often convenient to be able to limit the currently-displayed
results to a chosen subset.  The SBML Test Runner's filter facility
(invocable from the menubar or the toolbar icon) lets you select a
subset based on either case numbers and/or tags.  For either one, you
can chose to select by including cases (by numbers and/or tags) and/or
excluding cases (again by numbers and/or tags).  When both inclusions
are exclusions are applied, inclusions are processed first, followed
by exclusions.  When both case numbers and tags are specified, numbers
are processed first, followed by tags.

When a filter is in effect, the main window will display a notice bar
near the top to alert you that the results shown are not the entire
set.  The case numbers listed in the left-hand panel of the main
window will only include the selected cases; likewise, the cases shown
in the result map will also be limited to just those cases selected by
the filter.

To disable the filter, clear all the filter criteria in the filter
dialog.


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


THIRD-PARTY SOFTWARE LICENSES
======================================================================

This software uses libraries and other materials from third-party
sources.  All are licensed for free use and redistribution under
open-source terms.  Here is a summary of the items used and their
licenses:

* Icons8 icons for iOS -- http://icons8.com
  Creative Commons Attribution-NoDerivs 3.0 Unported

. Simple Widget Toolkit (SWT) -- http://www.eclipse.org/swt/
  Eclipse Public License v1.0

. Simple XML Framework -- http://simple.sourceforge.net
  Apache License 2.0

. Opal Widgets -- http://code.google.com/a/eclipselabs.org/p/opal/
  Eclipse Public License v1.0   
  
. SWTChart -- http://www.swtchart.org
  Eclipse Public License v1.0      





======================================================================
# The following is for [X]Emacs users.
# Local Variables:
# fill-column: 70
# End:
======================================================================
