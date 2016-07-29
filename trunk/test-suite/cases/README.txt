
              The SBML Test Suite -- Test Cases Archive

            For more information about the SBML Test Suite
        please visit http://sbml.org/Software/SBML_Test_Suite
          or contact the SBML Team at sbml-team@caltech.edu

             Please report problems  using the tracker at
          https://github.com/sbmlteam/sbml-test-suite/issues

    Please join the sbml-interoperability mailing list by visiting
                      http://www.sbml.org/Forums

Authors
======================================================================

The SBML Test Suite has been developed over many years, with the
contributions of many people.  The following are the primary authors
of the three separate test case collections:

* Semantic test suite:

  Sarah M. Keating (a,b), Lucian P. Smith (b,c), Bruce Shapiro (b),
  Michael Hucka (b), Frank T. Bergmann (d), Brett Olivier (g),
  Andrew Finney (b)

* Stochastic test suite:

  Thomas W. Evans (e), Colin S. Gillespie (f), Darren J. Wilkinson (f),
  Lucian P. Smith (b,c)
   
* Syntactic test suite:

  Sarah M. Keating (a,b), Lucian P. Smith (b,c)

Additional contributions are gratefully acknowledged from Ralph Gauges
(h), Chris Myers (i), Akira Funahashi (j), Andreas Dräger (k), Roland
Keller (k), Fedor Kolpakov (l), Stanley Gu (c), and others in the SBML
community.

Institutions:

   (a) EMBL-EBI, Hinxton, Cambridgeshire, UK
   (b) California Institute of Technology, Pasadena, CA, US
   (c) University of Washington, Seattle, WA, US
   (d) University of Heidelberg, Heidelberg, DE
   (e) University of Liverpool, Liverpool, UK
   (f) Newcastle University, Newcastle, UK
   (g) Vrije Universiteit Amsterdam
   (h) Heidelberg Institute for Theoretical Studies, Heidelberg, DE
   (i) University of Utah, Salt Lake City, UT, US
   (j) Systems Biology Institute, Tokyo, JP
   (k) University of Tuebingen, Tübingen, DE
   (l) Institute of Systems Biology, Novosibirsk, RU   

Introduction
======================================================================

The SBML Test Suite test cases are divided into 3 sets and (as of
version 3.2.0), distributed separately.  Each tests a different aspect
of SBML understanding and compliance.

* Semantic: The semantic test suite contains valid SBML models with
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
  look in the file named "+README.txt" in the semantic/ subdirectory.

* Stochastic: The stochastic test suite contains valid SBML models
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
  stochastic test cases, please look in the file named "+README.txt"
  in the stochastic/ subdirectory.

* Syntactic: The syntactic test suite consists of valid and invalid
  SBML models.  Each test is designed to check a particular SBML
  validation rule.  These rules are defined in the SBML specification
  documents.  Each case model in this part of the Test Suite is
  expected to be recognized by a software system as being either valid
  or invalid -- nothing more.  The validity is indicated in the file
  name of a model.  (Example: "01002-fail-01-01-sev2-l2v1.txt".)
  Details about the error and the error message produced by libSBML
  are included, as are incidental warnings that libSBML may also
  produce for a given model/test case.  For more information about the
  stochastic test cases, please look in the file named "+README.txt"
  in the syntactic/ subdirectory.


Usage
======================================================================

For more information about using these test cases, please refer to the
instructions either at the top level of the SBML Test Suite project
repository, or in the individual subdirectories under this directory.


Licensing
======================================================================

For full license information, please refer to the file "LICENSE.txt".
Briefly, the test case distributions of the SBML Test Suite are
distributed under the terms of the LGPL; the overall SBML Test Suite
(including the software components) are distributed under the LGPL but
include components from other sources licensed under other open-source
terms.  (However, none of the terms are more restrictive than the
LGPL.)




     .-://///:`  .:/+++++/-`      .--.             `---`  `--
  -/++//:---:.`://+syyyssoo+`    ohhy`            /hhh.  -hy`
`/++/-`       ::/ohhyyssssoss-   ohhh+           .yhhh.  .hy`          
:++/.        `:::sysoo+++++oss.  ohoyh-         `ohoyh.  .hy`          
++//`        `--:/oo+///://+os:  oh//hs`        :hs.yh.  .hy`          
/+//.       `..--:////:--:/oos.  oh/`sh/       `yh-`yh.  .hy`          
`////:-.......---::://///++oo-   oh/ -hy.      +h+ `yh.  .hy`          
  .:///:::::--::::://///++oo:    oh/  +hs     -hy` `yh.  .hy`          
`::-``..--::::::://osyyysoooo.   oh/  `yh:   `sh:  `yh.  .hy`          
:o+/`      .:////oyhyyyyyyssss`  oh/   :hy`  /ho`  `yh.  .hy`          
/oo/        .///oyysoo+++oosyy-  oh/    oho .hh.   `yh.  .hy`          
.sso:       `+++oso+//////syyy`  oh/    .hh-oh/    `yh.  .hy`          
 :sss+-`   ./oooooo//:::+syyy.   oh/     /hhhs`    `yh.  -hy`          
  `/syssooossssssssssssyyyy/`    oh/      shh-     `yh.  -hhooooooooooo
    `-/+oso+/-.-:/osyyso/-`      -:.      .:-      `--`  `:::::::::::::



----------------------------------------------------------------------
The following is for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
----------------------------------------------------------------------
