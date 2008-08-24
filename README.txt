                         The SBML Test Suite

          Sarah Keating, Kimberly Begley, and Michael Hucka

                       with contributions from
                     Andrew Finney, Bruce Shapiro
                and many others in the SBML community.

              For more information about SBML, contact:

                            The SBML Team
                         http://www.sbml.org/
                     mailto:sbml-team@caltech.edu


 Please report problems with the SBML Test Suite using the tracker at
          http://sbml.org/software/test-suite/issue-tracker

    Please join the sbml-interoperability mailing list by visiting
                      http://www.sbml.org/Forums

   ,--------------------------------------------------------------.
  | Table of contents                                             |
  | 1. Quick start                                                |  
  | 2. Detailed running instructions                              |
  | 3. Building from sources                                      |
  | 4. History                                                    |
   `--------------------------------------------------------------'
    Last update to this file: $Date::                           $



--------------
1. QUICK START
--------------

If you have downloaded a release of the standalone version of the SBML
Test Suite, your distribution will consist of a few files and a file
named SBMLTestSuite.jar.  To start the SBML Test Runner contained in
the .jar file, you can simply double-click on the .jar file in most
windowing environments (e.g., the MacOSX Finder).  If that does not
work, try executing the following command in a command shell or
terminal window:

   java -jar SBMLTestSuite.jar

Once the application is running, chose the option "New Test Run" from
the File menu.  The first time in a session, it will lead you through
the configuration steps.  Once the SBML Test Runner is configured, the
green arrow button on the front panel will be enabled.  Click on the
button to run the tests.

After the tests are completed, you can view the results using either
the list or map views.


------------------------
2. DETAILED INSTRUCTIONS
------------------------

The SBML Test Suite is a conformance testing system for SBML. It
allows developers and users to test the degree and correctness of SBML
support provided in an SBML-compatible software package.  The SBML
Test Suite consists of (1) a collection of SBML models, each with
associated simulated results where appropriate; (2) a testing
framework for running software tools through the suite; and
(3) basic documentation on the test cases and the use of the suite.

Two systems are available for running the Test Suite.  The first is
the Standalone Standalone, which is a package consisting of all the
test cases and a platform-independent application (the SBML Test
Runner) runnable on almost any computer.  The second is an online
system (http://sbml.org/Facilities/Online_SBML_Test_Suite) allowing
you to upload results from a simulator and have them analyzed by a web
server.  Both frameworks use the same corpus of test cases.


---------------------------------------
2.2  RUNNING THE STANDALONE APPLICATION
---------------------------------------

You can simply double-click on the SBMLTestSuite.jar file in most
windowing environments (e.g., the MacOSX Finder).  If that does not
work, try executing the following command in a command shell or
terminal window:

   java -jar SBMLTestSuite.jar

Once the application is running, chose the option "New Test Run" from
the File menu.  The first time in a session, it will lead you through
the configuration steps.  The configuration steps consist of selecting
the kinds of SBML components to be included in the test models, the
kinds of tests to perform, and information about how to drive the
application to be tested.

The most complicated part is providing the "test wrapper": a script or
application that takes certain commands from SBML Test Runner on the
command line and invokes the application being tested in such a way
that the application reads an SBML file, runs it with certain
simulation settings, and writes an output file containing the results
of the simulation.  The SBML Test Runner provides you with the ability
to specify a complete command line invoking the wrapper.  This command
line can contain the following substitution symbols:

  %d = path to the directory containing all test cases
  %n = current test case number (of the form NNNNN)
  %o = directory where the CSV output file should be written

The specific values will be set by the SBML Test Runner itself; they
are not under user control.  However, the order in which the arguments
are handed to the wrapper is under user control.  For example, if the
path to your test wrapper is /home/myself/wrapper, the command line
you provide might look like this:

  /home/myself/wrapper %d %n %o

but you could equally chose to write it as, say, 

  /home/myself/wrapper %n %d %o

if your wrapper was written to take the arguments in that order.

The reason all three values are needed will become apparent shortly.
The directory indicated by %d will contain a large number of
subdirectories named after the test case number (i.e., 00001, 00002,
00003, etc.).  Inside each of these directories, there will be
multiple SBML files, a settings file, and some miscellaneous other
files:

  0xxxx-sbml-l1v2.xml   -- the model in SBML Level 1 Version 2 format
  0xxxx-sbml-l2v1.xml   -- the model in SBML Level 2 Version 1 format
  0xxxx-sbml-l2v2.xml   -- the model in SBML Level 2 Version 2 format
  0xxxx-sbml-l2v3.xml   -- the model in SBML Level 2 Version 3 format
  0xxxx-settings.txt    -- the settings file

You will need to write the wrapper such that it performs the following
steps:

a) Extracts the relevant simulation run settings from the file
   "%d/%n/%n-sbml-lXvY.xml".  These settings include the starting time
   of the simulation, the duration of the simulation, the variables
   whose values should appear in the output, the number of output
   steps to record in the output, and the tolerances to use.

b) Tells the to-be-tested application to (i) read an SBML file named
   "%d/%n/%n-sbml-lXvY.xml", where X is the SBML Level and Y is the
   Version within the Level, (ii) execute a simulation with the
   settings determined in step (a), and (iii) write the output as a
   file named "%o/%n.csv".  The command line arguments to be handed to
   the application depend on the application itself.

The SBML Test Runner will go through every test in the test case
directory and invoke the wrapper, once for each test case.  It will do
this by executing command lines that look a bit like this:

  /path/to/your/wrapper  %d  00001  %o
  /path/to/your/wrapper  %d  00002  %o
  /path/to/your/wrapper  %d  00003  %o
  /path/to/your/wrapper  %d  00004  %o
  ...

where %d is the path to the directory containing all the test case
subdirectories and %o is the directory where the SBML Test Runner will
expect to find the output written by the application.


--------------------------------
2.3  RUNNING THE WEB APPLICATION
--------------------------------

The Online SBML Test Suite is similar to the SBML Test Suite
Standalone Application.  The main difference is that the web
application has no way to run your application.  Instead, it provides
a way to select and download a collection of test cases based on
selection criteria bout what should be tested, and it provides a way
to upload a set of results to be evaluated by the online server.  It
is up to you to determine how to run the test cases through your
application and gather the results to be uploaded.


----------------------------------------------------
3.  BUILDING THE STANDALONE APPLICATION FROM SOURCES
----------------------------------------------------

The sources to the SBML Test Suite are available via the public SVN
repository for the SBML Project.  You can check out a copy from SVN
using a command such as the following:

  svn co https://sbml.svn.sourceforge.net/svnroot/sbml/trunk/test-suite

The sources for the Standalone Application are located in the
subdirectory 

  src/front-ends/standalone

To build the Standalone Application, which is written in Java, you
will need NetBuilder version 6.1 or later.  From within NetBuilder,
you can use the command File->"Open Project" and navigate to this
directory to open the project in NetBuilder.  (There is a NetBuilder
configuration file, "build.xml", in the directory named above.)  Once
you have opened the project in NetBuilder, you can use NetBuilder's
"Build Main Project" command to build a new .jar file.  The
SBMLTestSuite.jar file will be stored in

  src/front-ends/standalone/dist



----------
4. HISTORY
----------

The current SBML Test Suite is based on earlier work by Andrew Finney
on the "SBML Semantic Test Suite".





----------------------------------------------------------------------
$Id$
$HeadURL$

The following is for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
----------------------------------------------------------------------
