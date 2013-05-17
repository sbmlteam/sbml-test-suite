The **SBML Test Suite** is a conformance testing system for SBML. It
allows developers and users to test the degree and correctness of SBML
support provided in an SBML-compatible software package.  The SBML
Test Suite consists of (1) a collection of SBML models, each with
associated simulated results where appropriate; (2) a testing
framework for running software tools through the suite; and (3) basic
documentation on the test cases and the use of the suite.

You in this directory, you will find two subdirectories:

* **cases-archive**: this contains an archive of the SBML test cases
  _only_.  Download this if you do not need or want the test runner,
  and want to run the tests using your own testing framework.

* **test-runner**: this contains a standalone application that
  incorporates the test cases and lets you test SBML-compatible
  software on your computer.  You do not need to download the test
  cases archive separately if you download the test runner.

More information about the SBML Test Suite is available online at
[http://sbml.org/Software/SBML_Test_Suite](http://sbml.org/Software/SBML_Test_Suite).

_Authors: Sarah Keating, Michael Hucka, Frank Bergmann,
Lucian Smith, Kimberly Begley, and Andrew Finney,
with contributions from
Bruce Shapiro and many others in the SBML community._
