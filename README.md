SBML Test Suite
===============

The SBML Test Suite is a conformance testing system for [SBML](http://sbml.org).  It allows developers and users to test the degree and correctness of SBML support provided in an SBML-compatible software package.

[![License](http://img.shields.io/:license-LGPL-blue.svg)](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html)  [![Latest stable release](https://img.shields.io/badge/Latest_stable_release-3.2-brightgreen.svg)](http://shields.io)

----
*Main Authors*: [Sarah Keating](http://www.ebi.ac.uk/about/people/sarah-keating), [Michael Hucka](http://www.cds.caltech.edu/~mhucka), [Lucian P. Smith](http://www.washington.edu/home/peopledir/?method=name&term=smith), [Frank T. Bergmann](http://www.cos.uni-heidelberg.de/index.php/f.bergmann?l=_e), [Bruce Shapiro](http://www.bruce-shapiro.com), Thomas W. Evans, [Colin S. Gillespie](http://www.ncl.ac.uk/maths/about/staff/profile/colingillespie.html#background), [Darren J. Wilkinson](https://www.staff.ncl.ac.uk/d.j.wilkinson/), [Brett Olivier](http://www.bgoli.net), [Andrew Finney](https://www.linkedin.com/in/andrewmartinfinney).

*Repository*:   [https://github.com/sbmlteam/sbml-test-suite](https://github.com/sbmlteam/sbml-test-suite)

*Developers' discussion group*: [https://groups.google.com/forum/#!forum/sbml-interoperability](https://groups.google.com/forum/#!forum/sbml-interoperability)

*Pivotal tracker*: [https://www.pivotaltracker.com/n/projects/68714](https://www.pivotaltracker.com/n/projects/68714)

*License*: For full license information, please refer to the file [LICENSE.txt](LICENSE.txt) for details.  Briefly, the test case distributions of the SBML Test Suite are distributed under the terms of the [LGPL v2.1](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html); the overall SBML Test Suite (including the software components) are distributed under the LGPL but include components from other sources licensed under other open-source terms.  (However, none of the terms are more restrictive than the LGPL.)

🏁 Recent news and activities
------------------------------

Please see the file [NEWS.md](NEWS.md) for a summary of the changes in the most recent release.

☀ Background
----------

Computational modeling has become a crucial aspect of biological research, and [SBML](http://sbml.org) (the Systems Biology Markup Language) has become the de facto standard open format for exchanging models between software tools in systems biology.  At last count, over 280 software tools have provided support for SBML.  However, full support for SBML (especially for numerical simulations of models described by SBML) is a complex undertaking.

The SBML Test Suite can be used by developers to test their understanding of SBML and their software's implementation of SBML support; likewise, the Test Suite can be used by modelers and other users to test the correctness and completeness of SBML support in a software system.  The SBML Test Suite consists of (1) a collection of SBML models, each with associated simulated results where appropriate; (2) a testing framework for running software tools through the suite; and (3) basic documentation on the test cases and the use of the suite.

✺ The Test Cases
----------------

The SBML Test Suite test cases are divided into 3 sets and (as of version 3.2.0), distributed separately.  Each tests a different aspect of SBML understanding and compliance.

* *Semantic*: The semantic test suite contains valid SBML models with known, deterministic simulation results.  These models can be used to test the ability of a software system to understand the meaning of different SBML constructs and properly simulate the models in a deterministic fashion.  An example of a deterministic simulator is a system using a numerical differential-algebraic solver that supports discontinuous events.  Each test consists of a directory containing the model (or models, if that model can be translated to other SBML Levels + Version combinations without semantic loss), together with instructions on how to simulate that model, and the expected results.  For more information about the semantic test cases, please look in the file named [README.md](cases/semantic/README.md) in the [cases/semantic/](cases/semantic) subdirectory.

* *Stochastic*: The stochastic test suite contains valid SBML models with known, stochastic simulation results.  These tests exercise fewer features of SBML, and are instead intended to test the accuracy of systems that employ discrete stochastic simulation algorithms.  Each test consists of a directory containing the model (or models, if that model can be translated to other SBML Levels + Version combination without semantic loss), together with instructions on how to simulate that model, and the expected results.  Because the expected results are stochastic, each test case is designed to be performed multiple times, with summary statistics collected for each, which are then compared to the expected summary statistics using a formula derived from the number of times the test was repeated.  For more information about the stochastic test cases, please look in the file named [README.md](cases/stochastic/README.md) in the [cases/stochastic/](cases/stochastic) subdirectory.

* *Syntactic*: The syntactic test suite consists of valid and invalid SBML models.  Each test is designed to check a particular SBML validation rule.  These rules are defined in the SBML specification documents.  Each case model in this part of the Test Suite is expected to be recognized by a software system as being either valid or invalid &ndash; nothing more.  The validity is indicated in the file name of a model.  (Example: `01002-fail-01-01-sev2-l2v1.txt`.)  Details about the error and the error message produced by libSBML are included, as are incidental warnings that libSBML may also produce for a given model/test case.  For more information about the stochastic test cases, please look in the file named [README.md](cases/syntactic/README.md) in the [cases/syntactic/](cases/syntactic) subdirectory.


► The Test Runner
-----------------

If you have downloaded a release of the standalone version of the SBML Test Suite, your distribution will consist of a few files and a program named `SBML Test Runner`.

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


⁇ Getting Help
------------

The SBML Test Suite is under active development by a distributed team.  If you have any questions, please feel free to post or email on the  ([https://groups.google.com/forum/#!forum/sbml-interoperability](https://groups.google.com/forum/#!forum/sbml-interoperability)) forum, or contact the [SBML Team](mailto:sbml-team@googlegroups.com) directly.


♬ Contributing
------------

Contributions to the SBML Test Suite are very much welcome in all areas.  Please feel free to contact the developers.  A quick way to find out what is currently on people's plates and our near-term plans is to look at the [Pivotal Tracker](https://www.pivotaltracker.com/n/projects/68714) for this project.


☺ Acknowledgments
-----------------------

Funding for the continued development of the core of the SBML Test Suite currently comes from the [National Institute of General Medical Sciences](https://www.nigms.nih.gov) via grant NIH R01070923 (Principal Investigator: Michael Hucka).

The SBML Test Suite has been in development for many years, and many people have contributed in various ways large and small.  The following are the primary authors of different parts:

* *Semantic test cases*:

  Sarah M. Keating<sup>a,b</sup>, Lucian P. Smith<sup>b,c</sup>, Bruce Shapiro <sup>b</sup>, Michael Hucka<sup>b</sup>, Frank T. Bergmann<sup>d</sup>, Brett Olivier<sup>g</sup>, Andrew Finney<sup>b</sup>

* *Stochastic test cases*:

  Thomas W. Evans<sup>e</sup>, Colin S. Gillespie<sup>f</sup>, Darren J. Wilkinson<sup>f</sup>, Lucian P. Smith<sup>b,c</sup>
   
* *Syntactic test cases*:

  Sarah M. Keating<sup>a,b</sup>, Lucian P. Smith<sup>b,c</sup>

* *SBML Test Runners*:

  Frank T. Bergmann<sup>d</sup>, Michael Hucka<sup>b</sup>, Kimberley Begley

* *Online SBML Test Suite Database*:

  Frank T. Bergmann<sup>d</sup>

Additional contributions are gratefully acknowledged from Ralph Gauges<sup>h</sup>, Chris Myers<sup>i</sup>, Akira Funahashi<sup>j</sup>, Andreas Dräger<sup>k</sup>, Roland Keller<sup>k</sup>, Fedor Kolpakov<sup>l</sup>, Stanley Gu<sup>c</sup>, and others in the SBML community.

Institutions:

&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>a</sup> EMBL-EBI, Hinxton, Cambridgeshire, UK<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>b</sup> California Institute of Technology, Pasadena, CA, US<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>c</sup> University of Washington, Seattle, WA, US<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>d</sup> University of Heidelberg, Heidelberg, DE<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>e</sup> University of Liverpool, Liverpool, UK<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>f</sup> Newcastle University, Newcastle, UK<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>g</sup> Vrije Universiteit Amsterdam<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>h</sup> Heidelberg Institute for Theoretical Studies, Heidelberg, DE<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>i</sup> University of Utah, Salt Lake City, UT, US<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>j</sup> Systems Biology Institute, Tokyo, JP<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>k</sup> University of Tuebingen, Tübingen, DE<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>l</sup> Institute of Systems Biology, Novosibirsk, RU   <br>


☮ Copyright and license
---------------------

Please see the file [../LICENSE.txt](https://raw.githubusercontent.com/sbmlteam/sbml-test-suite/develop/src/misc/graphics-originals/Official-sbml-supported-70.jpg) for copyright and license details.


More information
----------------

More information about the SBML Test Suite is available online at
[http://sbml.org/Software/SBML_Test_Suite](http://sbml.org/Software/SBML_Test_Suite).

[![SBML Logo](https://raw.githubusercontent.com/sbmlteam/sbml-test-suite/develop/src/misc/graphics-originals/Official-sbml-supported-70.jpg)](http://sbml.org)

