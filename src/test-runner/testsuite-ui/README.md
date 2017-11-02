SBML Test Runner <img width="100px" align="right" src="https://raw.githubusercontent.com/sbmlteam/sbml-test-suite/master/src/front-ends/standalone/testsuite-ui/src/data/application-icons/linux/icon_256x256.png">
================

The SBML Test Runner is a standalone desktop application with a graphical user interface (GUI) for controlling SBML-compatible applications and making them run test cases from the [SBML Test Suite](http://sbml.org/Software/SBML_Test_Suite).  It is written in 
Java with the Eclipse [SWT](https://www.eclipse.org/swt) widget toolkit and can be used on macOS, Linux and Windows.

----
*Main Authors*: [Michael Hucka](http://www.cds.caltech.edu/~mhucka) and [Frank T. Bergmann](http://www.cos.uni-heidelberg.de/index.php/f.bergmann?l=_e)

*Repository*:   [https://github.com/sbmlteam/sbml-test-suite](https://github.com/sbmlteam/sbml-test-suite)

*Developers' discussion group*: [https://groups.google.com/forum/#!forum/sbml-interoperability](https://groups.google.com/forum/#!forum/sbml-interoperability)

*Bug reports*: Please use the [GitHub issue tracker](https://github.com/sbmlteam/sbml-test-suite/issues) to report problems with the SBML Test Runner (or the SBML Test Suite in general).  You can also contact the developers directly by sending email to [sbml-team@googlegroups.com](sbml-team@googlegroups.com).

*Pivotal tracker*: [https://www.pivotaltracker.com/n/projects/68714](https://www.pivotaltracker.com/n/projects/68714)

*License*: For full license information, please refer to the file [LICENSE.txt](LICENSE.txt) for details.  Briefly, the SBML Test Runner and the test cases are distributed under the terms of the [LGPL v2.1](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html) but include components from other sources licensed under other open-source terms.  (However, none of the terms are more restrictive than the LGPL.)


☀ Introduction
--------------

The SBML Test Suite is a conformance testing system for [SBML](http://sbml.org). It allows developers and users to test the degree and correctness of SBML support provided in an SBML-compatible software package.  The SBML Test Suite consists of (1) a collection of SBML models, each with associated simulated results where appropriate; (2) a testing framework for running software tools through the suite; and (3) basic documentation on the test cases and the use of the suite.

Note: the test cases in the overall Test Suite are divided into 3 sets: _semantic_, _stochastic_, and _syntactic_. Each set is meant to exercise a different aspect of SBML understanding and compliance.  The SBML Test Runner currently **only implements facilities for running the semantic tests*.  The semantic portion of the Test Suite contains valid SBML models with known, deterministic simulation results.  These models can be used to test the ability of a software system to understand the meaning of different SBML constructs and properly simulate the models in a deterministic fashion.

The SBML Test Runner is written in Java and uses the [SWT (Standard Widget Toolkit)](https://www.eclipse.org/swt) widgets in its graphical user interface.  If you obtained a binary release of the SBML Test Runner, then you can start it simply by double-clicking the application shortcut `SBML Test Runner` (or invoking it from a command line, if you prefer).  If you obtained the source code distribution, you will need to build the application first, using the instructions below.

🚧 Building the SBML Test Runner (if you must)
------------------------------

_Note: We provide ready-to-run binary installations of the SBML Test Suite for Windows, macOS and Linux operating systems.  You may wish to [download the installer](https://github.com/sbmlteam/sbml-test-suite/releases) and skip directly to the [section below](#user-content-running) on running the SBML Test Runner._

If you want to build the Test Runner from the source code distribution, you will need Java version 1.8 or later.  Note that because [SWT](https://www.eclipse.org/swt) uses native code graphical widgets, the JAR file that is built is platform-system specific &ndash; you must build a version for the operating system and machine word size (32-bit or 64-bit) you will run it on, unlike simpler platform-independent Java applications.

The Ant configuration understands the following commands.  All outputs are placed in the subdirectory `dist`.

* `ant jar`: Creates a self-contained jar file in the subdirectory `dist`.  Whether it is 32-bit or 64-bit is determined automatically based on the platform you are running on.  You can run the JAR using either `java -XstartOnFirstThread -jar dist/sbmltestrunner.jar` (if you use macOS) or `java -jar dist/sbmltestrunner.jar` (if you use any other operating system).

* `ant app`: Creates an executable desktop application that wraps the JAR file and puts it in the subdirectory `dist`.  On Windows and Linux, this produces one executable for 32-bit and another for 64-bit versions of the current platform; on OS&nbsp;X, it only produces a 64-bit version.  The result can be executed as a normal application (e.g., by doing-clicking it). On Windows and Linux, the result still requires Java to be installed on the computer; on OS&nbsp;X, the Java run-time is included in the application bundle.

* `ant dist`: Creates an installer for the current platform.  On macOS, it creates a `.pkg` package installer, on Windows it creates a runnable `.exe` installer, and on Linux, it creates 3 items: a runnable binary installer, an RPM package, and a Debian `.deb` package.


⚙️ <a name="wrappers"/>Defining test wrappers
---------------------------------------------

To test an SBML-compatible application using the SBML Test Runner, you will probably need to create a _test wrapper_: a small program that acts as an interface between the SBML Test Runner and the application to be tested.  The SBML Test Runner will invoke the wrapper with certain arguments for each test case, and the wrapper's job is to invoke the to-be-tested application in turn.  The wrapper is responsible for making the application read an SBML file for a given test case, simulate the SBML model with certain simulation settings, and write an output file containing the numerical results of the simulation in a comma-separated value (CSV) format.  A wrapper might be as simple as a shell script, or it might be a small program all on its own, depending on the application being tested.  (Some applications may not need a wrapper at all, if they can understand the necessary command line arguments directly and produce output in the format needed by the SBML Test Runner.) The SBML Test Runner does not supply the wrappers &ndash; **you will have to write a wrapper program** yourself or obtain one from the developer(s) of the software you want to test.

When you first start the Runner, you will not have any wrapper configurations defined except for one pseudo-wrapper definition named `-- no wrapper --`.  This is a view-only wrapper that allows you to browse the test cases provided in the SBML Test Suite, and nothing more.  To let you test an actual SBML-compatible application, the SBML Test Runner will open a Preferences panel to let you define one or more wrapper configurations.  The following image shows the wrapper configuration part of the preferences panel:

<p align="center">
<img src=".graphics/wrapper-config.png"/>
</p>

Each configuration has:

1. A name.
2. The path to the wrapper program on your computer.
3. The path to a directory where the Test Runner will find the application's output after running a test case.
4. A list of test components or test tags that the application is known to be unable to understand.
5. Command-line arguments that should be passed to the wrapper.

The SBML Test Runner provides you with the ability to specify a complete command line invoking the wrapper.  In the command-line arguments given to the wrapper, the following substitution codes can be used.  Their values will be substituted each time the wrapper is invoked:

* `%d` = the path to the directory containing all test cases
* `%n` = the current test case number (as a 5-digit number)
* `%o` = the directory where the CSV output file should be written
* `%l` = the SBML Level of the test case
* `%v` = the Version of SBML within the SBML Level of the test case

The specific values will be set by the SBML Test Runner itself; they are not under user control.  However, the _order_ in which the arguments are handed to the wrapper is under your control, simply by writing the arguments in the desired order in the _Arguments to wrapper_ field.

Each test case consists of an SBML file and a settings file. The files are located in the directory named `%d/%n`.  The directory indicated by `%d` will contain a large number of subdirectories named after the test case number (i.e., `00001`, `00002`, `00003`, etc. &ndash; these are the values that `%n` will taken on).  Inside each of these numbered directories, there will be multiple SBML files (for different SBML Level/Version combinations), a settings file (named `%n-settings.txt`), and other files.

You will need to create a wrapper such that it performs the following steps:

1. Extracts the relevant simulation run settings from the file `%d/%n/%n-settings.txt`.  These settings include the starting time of the simulation, the duration of the simulation, the variables whose values should appear in the output, the number of output steps to record in the output, and the tolerances to use.

2. Tells the to-be-tested application to (i) read an SBML file named `%d/%n/%n-sbml-lXvY.xml`, where _X_ is the SBML Level and _Y_ is the Version within the Level, (ii) execute a simulation with the settings determined in step (1), and (iii) write the output as a file named `%o/%n.csv`.  The command line arguments to be handed to the application depend on the application itself.

You can define multiple wrappers, but only one will be executed during any given test run.  (You will be able to choose the wrapper from a pull-down menu in the main window of the SBML Test Runner.)

The definition of a wrapper also includes 3 options, which can be seen in the image above:

* _Pseudo-wrapper to view test cases only_: When set, this option defines a wrapper as non-executable and disables some of the fields in the preferences panel.  Use this option to define a wrapper for viewing existing results (e.g., results created in some other fashion than through the SBML Test Runner).  The wrapper definition allows you to define the directory where the results will be found, as well as unsupported tags, but not program path or arguments.

* _Wrapper can handle any SBML Level/Version_: Some applications may not be able to accept any SBML Level and Version.

* _Wrapper can be run in parallel_: The Runner can execute tasks in parallel, but it must be informed whether multiple instances of a wrapper or application can be invoked simultaneously.  Use this flag to indicate that multiple copies of the wrapper can be started simultaneously.  Do not enable this option if the wrapper (or application) writes to the same file (e.g., a single log file), locks a single resource, or does something else that would result in non-deterministic behavior if multiple copies of the wrapper or application are started simultaneously.  If you use a shell script, also make sure that the script does not return before the application you're testing returns a result.  (In other words, do not have the shell script start the application as a background process; make sure the script terminates only when the application itself terminates.)

Once the wrapper is defined, back in the main screen of the SBML Test Runner, you will be able to click on the run button in the middle of the application toolbar.  The SBML Test Runner will proceed to go through every test in the test case directory and invoke the wrapper, once for each test case, substituting the values for the place holders `%d`, `%n`, `%o`, `%l`, and `%v`.  Each time the wrapper exits after running a particular test case, the Test Runner will look for a file named `%o/%n.csv` to read the application's output for that test case.  The Test Runner will then compare this output to the expected results for the test case, and indicate whether the output matches the expected results within specific tolerances.


► <a name="running"/>Running the SBML Test Runner
 ------------------------------------------------

There are a couple of easy ways to start the Test Runner. If you installed the official release installation or built from sources using `ant app`, you can run the `SBML Test Runner` application (typically by double-clicking it in a window environment).  Alternatively, you can run the JAR file directly, if you built the `.jar` file using `ant jar`:

* On macOS, execute the following command in a terminal shell:
  ```
  java -XstartOnFirstThread -jar dist/sbmltestrunner.jar
  ```
* On Windows and Linux: execute the following command instead:
  ```
  java -jar dist/sbmltestrunner.jar
  ```

Once at least one wrapper is defined, you will be able to run tests on an application (or more precisely, run a wrapper to invoke an SBML-aware application) and view the results.  The main panel of the SBML Test Runner is oriented towards this purpose.

<p align="center">
<img src=".graphics/example-run.png"/>
</p>

The main window consists of a toolbar and 5 regions below it:

* A list of test case numbers vertically along the left-hand side.

* An upper panel showing two graphs, one depicting the expected results and the other the actual results for a selected test case.

* A lower panel showing a graph of the differences between the values of the expected and actual results.
  
* A description area below the graphs, describing the purpose and features of the currently-selected test case.

* A status area at the very bottom, with a progress bar and other information about the current state of the SBML Test Runner.

After you start the runner, initially there will not be any test cases selected or graphs shown.  If you click on the triangle-shaped icon in the middle of the toolbar (or select the **Run** option in the menubar pull-down menu named **Test**), the SBML Test Runner will proceed to invoke the wrapper for every test case in the suite, starting at case 00001.  It will the wrapper once for each test case, substituting the values for the place holders `%d`, `%n`, `%o`, `%l`, and `%v` [discussed above](#user-content-wrappers).  The status area in the bottom of the main window provides a real-time sense for the progress; you can also open a map view of the results (via the grid-shaped icon in the far left of the tool bar) and the map will be updated as the run continues.  You can pause execution at any time by clicking on the double-bar-shaped "Pause" button that becomes visible while a run is in progress.

At any time, even while a run is in progress, you can select a test case from the list in the left-hand side of the main window.  The upper and lower panels of the main window will then display the graphs of the results, and the description area will provide some details about the selected test case.

While a case is select, or while multiple cases are selected, clicking on the triangle-shaped run button or selecting the **Run** option from the menubar will run only the selected test cases, and not all test cases.  To run all the tests again from the beginning, you must first _clear the selections_ from the list in the left-hand side.  There is a toolbar button for quickly deselecting everything; you can also select the **Deselect All** option from the **Edit** pull-down menu in the menubar.


⧉ Filtering
------------

It is often convenient to be able to limit the currently-displayed results to a chosen subset.  The SBML Test Runner's filter facility (invocable from the menubar or the toolbar icon) lets you select a subset based on either case numbers and/or tags.  For either one, you can chose to select by including cases (by numbers and/or tags) and/or excluding cases (again by numbers and/or tags).  When both inclusions are exclusions are applied, inclusions are processed first, followed by exclusions.  When both case numbers and tags are specified, numbers are processed first, followed by tags.

When a filter is in effect, the main window will display a notice bar near the top to alert you that the results shown are not the entire set.  The case numbers listed in the left-hand panel of the main window will only include the selected cases; likewise, the cases shown in the result map will also be limited to just those cases selected by the filter.

To disable the filter, clear all the filter criteria in the filter dialog.


☺ Acknowledgments
-----------------

Funding for the continued development of the core of the SBML Test Suite currently comes from the [National Institute of General Medical Sciences](https://www.nigms.nih.gov) via grant NIH R01070923 (Principal Investigator: Michael Hucka).


🏛 Third-party software licenses
------------------------------

This software uses libraries and other materials from third-party sources.  All are licensed for free use and redistribution under open-source terms. For more information, please see the individual license statements contained in the subdirectory [../3rd-party-licenses](../3rd-party-licenses).


⁇ More information
------------------

More information about the SBML Test Suite is available online at [http://sbml.org/Software/SBML_Test_Suite](http://sbml.org/Software/SBML_Test_Suite).

[![SBML Logo](../../../dev/graphics/Official-sbml-supported-70.jpg)](http://sbml.org)
