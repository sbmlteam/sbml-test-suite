SBML Test Suite
===============

The SBML Test Suite is a conformance testing system for [SBML](http://sbml.org).  It allows developers and users to test the degree and correctness of SBML support provided in an SBML-compatible software package.

[![License](http://img.shields.io/:license-LGPL-blue.svg)](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html)  [![Latest version](https://img.shields.io/badge/Latest_version-3.2-brightgreen.svg)](http://shields.io)

----
*Main Authors*: [Sarah Keating](http://www.ebi.ac.uk/about/people/sarah-keating), [Michael Hucka](http://www.cds.caltech.edu/~mhucka), [Lucian P. Smith](http://www.washington.edu/home/peopledir/?method=name&term=smith), [Frank T. Bergmann](http://www.cos.uni-heidelberg.de/index.php/f.bergmann?l=_e), [Bruce Shapiro](http://www.bruce-shapiro.com), Thomas W. Evans, [Colin S. Gillespie](http://www.ncl.ac.uk/maths/about/staff/profile/colingillespie.html#background), [Darren J. Wilkinson](https://www.staff.ncl.ac.uk/d.j.wilkinson/), [Brett Olivier](http://www.bgoli.net), [Andrew Finney](https://www.linkedin.com/in/andrewmartinfinney).

*Repository*:   [https://github.com/sbmlteam/sbml-test-suite](https://github.com/sbmlteam/sbml-test-suite)

*Developers' discussion group*: [https://groups.google.com/forum/#!forum/moccasin-dev](https://groups.google.com/forum/#!forum/sbml-interoperability)

*Pivotal tracker*: [https://www.pivotaltracker.com/n/projects/68714](https://www.pivotaltracker.com/n/projects/68714)

*License*: For full license information, please refer to the file [../LICENSE.txt](https://raw.githubusercontent.com/sbmlteam/moccasin/master/LICENSE.txt) for details.  Briefly, the test case distributions of the SBML Test Suite are distributed under the terms of the [LGPL v2.1](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html); the overall SBML Test Suite (including the software components) are distributed under the LGPL but include components from other sources licensed under other open-source terms.  (However, none of the terms are more restrictive than the LGPL.)

☀ Background
----------

Computational modeling has become a crucial aspect of biological research, and [SBML](http://sbml.org) (the Systems Biology Markup Language) has become the de facto standard open format for exchanging models between software tools in systems biology.  At last count, over 280 software tools have provided support for SBML.  However, full support for SBML (especially for numerical simulations of models described by SBML) is a complex undertaking.

The SBML Test Suite can be used by developers to test their understanding of SBML and their software's implementation of SBML support; likewise, the Test Suite can be used by modelers and other users to test the correctness and completeness of SBML support in a software system.  The SBML Test Suite consists of (1) a collection of SBML models, each with associated simulated results where appropriate; (2) a testing framework for running software tools through the suite; and (3) basic documentation on the test cases and the use of the suite.

✺ The Test Cases
----------------

The SBML Test Suite test cases are divided into 3 sets and (as of
version 3.2.0), distributed separately.  Each tests a different aspect
of SBML understanding and compliance.

* *Semantic*: The semantic test suite contains valid SBML models with
  known, deterministic simulation results.  These models can be used
  to test the ability of a software system to understand the meaning
  of different SBML constructs and properly simulate the models in a
  deterministic fashion.  An example of a deterministic simulator is a
  system using a numerical differential-algebraic solver that supports
  discontinuous events.  Each test consists of a directory containing
  the model (or models, if that model can be translated to other SBML
  Levels + Version combinations without semantic loss), together with
  instructions on how to simulate that model, and the expected
  results.  For more information about the semantic test cases, please
  look in the file named *+README.txt* in the [cases/semantic/](cases/semantic) subdirectory.

* *Stochastic*: The stochastic test suite contains valid SBML models
  with known, stochastic simulation results.  These tests exercise
  fewer features of SBML, and are instead intended to test the
  accuracy of systems that employ discrete stochastic simulation
  algorithms.  Each test consists of a directory containing the model
  (or models, if that model can be translated to other SBML Levels +
  Version combination without semantic loss), together with
  instructions on how to simulate that model, and the expected
  results.  Because the expected results are stochastic, each test
  case is designed to be performed multiple times, with summary
  statistics collected for each, which are then compared to the
  expected summary statistics using a formula derived from the number
  of times the test was repeated.  For more information about the
  stochastic test cases, please look in the file named *+README.txt*
  in the [cases/stochastic/](cases/stochastic) subdirectory.

* *Syntactic*: The syntactic test suite consists of valid and invalid
  SBML models.  Each test is designed to check a particular SBML
  validation rule.  These rules are defined in the SBML specification
  documents.  Each case model in this part of the Test Suite is
  expected to be recognized by a software system as being either valid
  or invalid &ndash; nothing more.  The validity is indicated in the file
  name of a model.  (Example: `01002-fail-01-01-sev2-l2v1.txt`.)
  Details about the error and the error message produced by libSBML
  are included, as are incidental warnings that libSBML may also
  produce for a given model/test case.  For more information about the
  stochastic test cases, please look in the file named *+README.txt*
  in the [cases/syntactic/](cases/syntactic) subdirectory.


⁇ Getting Help
------------

The SBML Test Suite is under active development by a distributed team.  If you have any questions, please feel free to post or email on the  ([https://groups.google.com/forum/#!forum/sbml-interoperability](https://groups.google.com/forum/#!forum/sbml-interoperability)) forum, or contact the [SBML Team](mailto:sbml-team@caltech.edu) directly.


♬ Contributing
------------

Contributions to the SBML Test Suite are very much welcome in all areas.  Please feel free to contact the developers.  A quick way to find out what is currently on people's plates and our near-term plans is to look at the [Pivotal Tracker](https://www.pivotaltracker.com/n/projects/68714) for this project.


☺ Acknowledgments
-----------------------

Funding for the continued development of the core of the SBML Test Suite currently comes from the [National Institute of General Medical Sciences](https://www.nigms.nih.gov) via grant NIH R01070923 (Principal Investigator: Michael Hucka).

The SBML Test Suite has been in development for many years, and many people have contributed in various ways large and small.  The following are the primary authors of different parts:

* Semantic test cases:

  Sarah M. Keating<sup>a,b</sup>, Lucian P. Smith<sup>b,c</sup>, Bruce Shapiro <sup>b</sup>, Michael Hucka<sup>b</sup>, Frank T. Bergmann<sup>d</sup>, Brett Olivier<sup>g</sup>, Andrew Finney<sup>b</sup>

* Stochastic test cases:

  Thomas W. Evans<sup>e</sup>, Colin S. Gillespie<sup>f</sup>, Darren J. Wilkinson<sup>f</sup>, Lucian P. Smith<sup>b,c</sup>
   
* Syntactic test cases:

  Sarah M. Keating<sup>a,b</sup>, Lucian P. Smith<sup>b,c</sup>

* SBML Test Runners:

  Frank T. Bergmann<sup>d</sup>, Michael Hucka<sup>b</sup>, Kimberley Begley<sup>m</sup>

* Online SBML Test Suite Database:

  Frank T. Bergmann<sup>d</sup>

Additional contributions are gratefully acknowledged from Ralph Gauges h</sup>, Chris Myers<sup>i</sup>, Akira Funahashi<sup>j</sup>, Andreas Dräger<sup>k</sup>, Roland Keller<sup>k</sup>, Fedor Kolpakov<sup>l</sup>, Stanley Gu<sup>c</sup>, and others in the SBML community.

Institutions:

  <sup>a</sup> EMBL-EBI, Hinxton, Cambridgeshire, UK<br>
  <sup>b</sup> California Institute of Technology, Pasadena, CA, US<br>
  <sup>c</sup> University of Washington, Seattle, WA, US<br>
  <sup>d</sup> University of Heidelberg, Heidelberg, DE<br>
  <sup>e</sup> University of Liverpool, Liverpool, UK<br>
  <sup>f</sup> Newcastle University, Newcastle, UK<br>
  <sup>g</sup> Vrije Universiteit Amsterdam<br>
  <sup>h</sup> Heidelberg Institute for Theoretical Studies, Heidelberg, DE<br>
  <sup>i</sup> University of Utah, Salt Lake City, UT, US<br>
  <sup>j</sup> Systems Biology Institute, Tokyo, JP<br>
  <sup>k</sup> University of Tuebingen, Tübingen, DE<br>
  <sup>l</sup> Institute of Systems Biology, Novosibirsk, RU   <br>
  <sup>m</sup> Population Health Research Institute, Hamilton, ON<br>


☮ Copyright and license
---------------------

Please see the file [../LICENSE.txt](https://raw.githubusercontent.com/sbmlteam/moccasin/master/LICENSE.txt) for copyright and license details.
