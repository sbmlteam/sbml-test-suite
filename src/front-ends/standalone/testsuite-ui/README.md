SBML Test Runner
================

The SBML Test Runner is a Java program that uses the Eclipse [SWT](https://www.eclipse.org/swt) widget toolkit and can be used on Mac OS X, Linux and Windows.  It is a standalone application with a graphical user interface (GUI) for controlling SBML-compatible applications and making them run test cases from the [SBML Test Suite](http://sbml.org/Software/SBML_Test_Suite).

----
*Main Authors*: [Michael Hucka](http://www.cds.caltech.edu/~mhucka) and [Frank T. Bergmann](http://www.cos.uni-heidelberg.de/index.php/f.bergmann?l=_e)

*Developers' discussion group*: [https://groups.google.com/forum/#!forum/sbml-interoperability](https://groups.google.com/forum/#!forum/sbml-interoperability)

*Bug reports*: Please use the [GitHub issue tracker](https://github.com/sbmlteam/sbml-test-suite/issues) to report problems with the SBML Test Runner (or the SBML Test Suite in general).  You can also contact the developers directly by sending email to [sbml-team@googlegroups.com](sbml-team@googlegroups.com).

*Pivotal tracker*: [https://www.pivotaltracker.com/n/projects/68714](https://www.pivotaltracker.com/n/projects/68714)

*License*: For full license information, please refer to the file [LICENSE.txt](LICENSE.txt) for details.  Briefly, the SBML Test Runner and the test cases are distributed under the terms of the [LGPL v2.1](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html) but include components from other sources licensed under other open-source terms.  (However, none of the terms are more restrictive than the LGPL.)


☀ Introduction
--------------

The SBML Test Suite is a conformance testing system for [SBML](http://sbml.org). It allows developers and users to test the degree and correctness of SBML support provided in an SBML-compatible software package.  The SBML Test Suite consists of (1) a collection of SBML models, each with associated simulated results where appropriate; (2) a testing framework for running software tools through the suite; and (3) basic documentation on the test cases and the use of the suite.

Note: the test cases in the overall Test Suite are divided into 3 sets: _semantic_, _stochastic_, and _syntactic_. Each set is meant to exercise a different aspect of SBML understanding and compliance.  The SBML Test Runner currently **only implements facilities for running the semantic tests*.  The semantic portion of the Test Suite contains valid SBML models with known, deterministic simulation results.  These models can be used to test the ability of a software system to understand the meaning of different SBML constructs and properly simulate the models in a deterministic fashion.


🚧 Building the SBML Test Runner
------------------------------

_We provide ready-to-run binary installations of the SBML Test Suite for Windows, Mac OS X and Linux operating systems.  You may wish to [download the installer](https://github.com/sbmlteam/sbml-test-suite/releases) and skip directly to the [section below](#user-content-running) on running the SBML Test Runner._

If you want to build the Test Runner from the source code distribution, you will need Java version 1.8.  Note that because [SWT](https://www.eclipse.org/swt) uses native code graphical widgets, the JAR file that is built is platform-system specific &ndash; you must build a version for the operating system and machine word size (32-bit or 64-bit) you will run it on, unlike simpler platform-independent Java applications.

The Ant configuration understands the following commands.  All outputs are placed in the subdirectory `dist`.

* `ant jar`: Creates a self-contained jar file in the subdirectory `dist`.  Whether it is 32-bit or 64-bit is determined automatically based on the platform you are running on.  You can run the JAR using either `java -XstartOnFirstThread -jar dist/sbmltestrunner.jar` (if you use Mac OS X) or `java -jar dist/sbmltestrunner.jar` (if you use any other operating system).

* `ant app`: Creates an executable desktop application that wraps the JAR file and puts it in the subdirectory `dist`.  On Windows and Linux, this produces one executable for 32-bit and another for 64-bit versions of the current platform; on OS&nbsp;X, it only produces a 64-bit version.  The result can be executed as a normal application (e.g., by doing-clicking it). On Windows and Linux, the result still requires Java to be installed on the computer; on OS&nbsp;X, the Java run-time is included in the application bundle.

* `ant dist`: Creates an installer for the current platform.  On Mac OS&nbsp;X, it creates a `.pkg` package installer, on Windows it creates a runnable `.exe` installer, and on Linux, it creates 3 items: a runnable binary installer, an RPM package, and a Debian `.deb` package.

To build the executable applications and distributions (that is, using `ant app` and `ant dist`, but *not* the plain `ant jar`), you will need some additional tools, depending on the operating system:

* _Mac_: You will need the program [Packages](http://s.sudre.free.fr/Software/Packages/about.html) by Stéphane Sudre.

* _Windows_: You will need [Launch4j](http://launch4j.sourceforge.net) to create the application and [NSIS](http://nsis.sourceforge.net) to create the distribution installer.

* _Linux_: You will need BitRock's [InstallBuilder](https://installbuilder.bitrock.com) but _only_ to create the distribution installer.  (You can create the standalone application without InstallBuilder.)


► <a name="running"/>Running the SBML Test Runner
 ------------------------------------------------

Once you start the SBML Test Runner, if you have never run it before, it will first open a preferences/configuration panel.  The most complicated part is providing the _test wrapper_: a small program that gets invoked by the SBML Test Runner with specific arguments on the command line and in turn invokes the application is being tested.  It is the wrapper's job to make the application read an SBML file, run it with certain simulation settings, and write an output file containing the results of a simulation in comma-separated value (CSV) format.  A wrapper might be as simple as a shell script that invokes the application with appropriate arguments, but in some cases, may need to be more elaborate (perhaps to convert the output data from the application).  The SBML Test Runner does not supply the wrappers &ndash; **you will have to write a wrapper program** yourself or obtain one from the developer(s) of the software you want to test.

The following image shows the wrapper configuration part of the preferences panel:

<p align="center">
<img src=".graphics/wrapper-config.png"/>
</p>

The SBML Test Runner provides you with the ability to specify a complete command line invoking the wrapper.  This command line can contain the following substitution symbols:

* `%d` = path to the directory containing all test cases
* `%n` = current test case number (of the form `NNNNN`)
* `%o` = directory where the CSV output file should be written
* `%l` = the SBML Level of the test case
* `%v` = the Version of SBML within the SBML Level

The specific values will be set by the SBML Test Runner itself; they are not under user control.  However, the _order_ in which the arguments are handed to the wrapper is under your control, simply by writing the arguments in the desired order in the _Arguments to wrapper_ field.

The directory indicated by `%d` will contain a large number of subdirectories named after the test case number (i.e., `00001`, `00002`, `00003`, etc.).  Inside each of these directories, there will be multiple SBML files, a settings file, and some miscellaneous other files:

* `xxxxx-sbml-l1v2.xml`   &ndash; the model in SBML Level 1 Version 2 format
* `xxxxx-sbml-l2v1.xml`   &ndash; the model in SBML Level 2 Version 1 format
* `xxxxx-sbml-l2v2.xml`   &ndash; the model in SBML Level 2 Version 2 format
* `xxxxx-sbml-l2v3.xml`   &ndash; the model in SBML Level 2 Version 3 format
* `xxxxx-settings.txt`    &ndash; the settings file

You will need to write the wrapper such that it performs the following
steps:

1. Extracts the relevant simulation run settings from the file `%d/%n/%n-sbml-lXvY.xml`.  These settings include the starting time of the simulation, the duration of the simulation, the variables whose values should appear in the output, the number of output steps to record in the output, and the tolerances to use.

2. Tells the to-be-tested application to (i) read an SBML file named `%d/%n/%n-sbml-lXvY.xml`, where _X_ is the SBML Level and _Y_ is the Version within the Level, (ii) execute a simulation with the settings determined in step (a), and (iii) write the output as a file named `%o/%n.csv`.  The command line arguments to be handed to the application depend on the application itself.

Once the wrapper is defined, back in the main screen of the SBML Test Runner, you will be able to click on the run button in the middle of the application toolbar.  The SBML Test Runner will proceed to go through every test in the test case directory and invoke the wrapper, once for each test case, substituting the values for the place holders `%d`, `%n`, `%o`, `%l`, and `%v`.  When the wrapper exits, the Test Runner will look for a file named `%o/%n.csv` to read the application's output.  Finally, it will compare the output to the expected results for the test case, and indicate whether the output matches the expected results within specific tolerances.


☺ Acknowledgments
-----------------

Funding for the continued development of the core of the SBML Test Suite currently comes from the [National Institute of General Medical Sciences](https://www.nigms.nih.gov) via grant NIH R01070923 (Principal Investigator: Michael Hucka).


🏛 Third-party software licenses
------------------------------

This software uses libraries and other materials from third-party sources.  All are licensed for free use and redistribution under open-source terms. For more information, please see the individual license statements contained in the subdirectory [../3rd-party-licenses](../3rd-party-licenses).


⁇ More information
------------------

More information about the SBML Test Suite is available online at [http://sbml.org/Software/SBML_Test_Suite](http://sbml.org/Software/SBML_Test_Suite).

[![SBML Logo](https://raw.githubusercontent.com/sbmlteam/sbml-test-suite/develop/src/misc/graphics-originals/Official-sbml-supported-70.jpg)](http://sbml.org)

