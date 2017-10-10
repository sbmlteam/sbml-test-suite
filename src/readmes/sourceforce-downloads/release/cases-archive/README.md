**The SBML Test Suite – Test Case Archives**
======================================================================

The SBML Test Suite test cases are divided into 3 sets and (as of version 3.2.0), distributed separately.  Each set of cases tests a different aspect of [SBML](http://sbml.org/Documents/Specifications) understanding and compliance.

* **Semantic**: The semantic test suite contains valid SBML models with known, deterministic simulation results.  These models can be used to test the ability of a software system to understand the meaning of different SBML constructs and properly simulate the models in a deterministic fashion.  An example of a deterministic simulator is a system using a numerical differential-algebraic solver that supports discontinuous events.  Each test consists of a directory containing the model (or models, if that model can be translated to other SBML Levels + Versions combinations without semantic loss), together with instructions on how to simulate that model, and the expected results.  For more information about the semantic test cases, please look in the file named _+README.txt_ after you unzip the archive (i.e., the file _sbml-semantic-test-cases-YYYY-MM-DD.zip_, where _YYYY-MM-DD_ is the release date), or visit the [SBML Test Suite website](http://sbml.org/Software/SBML_Test_Suite).

* **Stochastic**: The stochastic test suite contains valid SBML models with known, stochastic simulation results.  These tests exercise fewer features of SBML, and are instead intended to test the accuracy of systems that employ discrete stochastic simulation algorithms.  Each test consists of a directory containing the model (or models, if that model can be translated to other SBML Levels + Versions combination without semantic loss), together with instructions on how to simulate that model, and the expected results.  Because the expected results are stochastic, each test case is designed to be performed multiple times, with summary statistics collected for each, which are then compared to the expected summary statistics using a formula derived from the number of times the test was repeated.  For more information about the semantic test cases, please look in the file named _+README.txt_ after you unzip the archive (i.e., the file _sbml-stochastic-test-cases-YYYY-MM-DD.zip_, where _YYYY-MM-DD_ is the release date), or visit the [SBML Test Suite website](http://sbml.org/Software/SBML_Test_Suite).

* **Syntactic**: The syntactic test suite consists of valid and invalid SBML models.  Each test is designed to check a particular SBML validation rule.  These rules are defined in the SBML specification documents.  Each case model in this part of the Test Suite is expected to be recognized by a software system as being either valid or invalid – nothing more.  The validity is indicated in the file name of a model.  (Example: _01002-fail-01-01-sev2-l2v1.txt_ means the test is expected to fail.)  Details about the error and the error message produced by libSBML are included, as are incidental warnings that libSBML may also produce for a given model/test case.  For more information about the semantic test cases, please look in the file named _+README.txt_ after you unzip the archive (i.e., the file _sbml-syntactic-test-cases-YYYY-MM-DD.zip_, where _YYYY-MM-DD_ is the release date), or visit the [SBML Test Suite website](http://sbml.org/Software/SBML_Test_Suite).

_Licensing_
======================================================================

For full license information, please refer to the file [_LICENSE.txt_](_LICENSE.txt_).  Briefly, the test case distributions of the SBML Test Suite are distributed under the terms of the LGPL; the overall SBML Test Suite (including the software components) are distributed under the [LGPL](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html) but include components from other sources licensed under other open-source terms.  (However, none of the terms are more restrictive than the [LGPL](https://www.gnu.org/licenses/old-licenses/lgpl-2.1.en.html).)

_Bugs and other issues_
======================================================================

Please report problems using the tracker at [https://github.com/sbmlteam/sbml-test-suite/issues](https://github.com/sbmlteam/sbml-test-suite/issues).

_More information_
======================================================================

More information about the SBML Test Suite is available online at
[http://sbml.org/Software/SBML_Test_Suite](http://sbml.org/Software/SBML_Test_Suite).

[![SBML Logo](http://sbml.org/images/8/82/Official-sbml-supported-70.jpg)](http://sbml.org)

