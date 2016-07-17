
              The SBML Test Suite: Stochastic Test Cases
                            based upon the
            DSMTS -- Discrete Stochastic Model Test Suite

 Thomas W. Evans (a), Colin S. Gillespie (b), Darren J. Wilkinson (b)
                   (a) University of Liverpool, UK
                     (b) Newcastle University, UK

          If you use DSMTS, please cite the following paper!

  Evans, T. W., Gillespie, C. S., Wilkinson, D. J. (2008) The SBML
  discrete stochastic models test suite, Bioinformatics, 24:285-286.

                Originally developed circa 2007-2010.
        The SBML 3.1 version of DSMTS (dated October 21, 2010)
        was added to the SBML Test Suite distribution in 2015.

            For more information about the SBML Test Suite
        please visit http://sbml.org/Software/SBML_Test_Suite
          or contact the SBML Team at sbml-team@caltech.edu

             Please report problems  using the tracker at
          https://github.com/sbmlteam/sbml-test-suite/issues

    Please join the sbml-interoperability mailing list by visiting
                      http://www.sbml.org/Forums

   ,--------------------------------------------------------------.
  | Table of contents                                             |
  | 1. Introduction                                               |  
  | 2. Getting started                                            |
  | 3. Test Suite integration                                     |
  | 4. License and distribution terms                             |
   `--------------------------------------------------------------'

1. INTRODUCTION
======================================================================
    
The SBML Discrete Stochastic Model Test Suite (DSMTS) was developed
and contributed by Thomas Evans, Colin Gillespie and Darren Wilkinson.
Each test case consists of an SBML model intended for simulation in a
discrete stochastic regime; the models have been solved either
analytically or using numerical methods, and the expected time course
data, together with expected means and standard deviations of model
species quantities, are provided for each test case.  The combination
of models and known results may be used to test the behavior of
SBML-compatible stochastic simulation systems.

The directories and files have been renamed from their original
versions according to the conventions used elsewhere in the SBML Test
Suite, but many of the files in each subdirectory are still named as
they were originally so that the correspondences between this and the
DSMTS should be easy to identify.

Here are the files in each directory (where "N", "Y" and "Z" are digits):

  NNNNN/NNNNN-model.m          -- model description
        NNNNN-plot.html        -- interactive plot of simulation results
        NNNNN-plot.jpg         -- image of plot of simulation results
        NNNNN-results.csv      -- SBML Test Suite-style results file
        NNNNN-sbml-l2v1.xml    -- SBML Level 2 Version 1 model file
        NNNNN-sbml-l2v2.xml    -- SBML Level 2 Version 2 model file
        NNNNN-sbml-l2v3.xml    -- SBML Level 2 Version 3 model file
        NNNNN-sbml-l2v4.xml    -- SBML Level 2 Version 4 model file
        NNNNN-sbml-l3v1.xml    -- SBML Level 3 Version 1 model file
        NNNNN-settings.txt     -- SBML Test Suite settings file
        dsmts-YYY-ZZ.mod       -- original model definition
        dsmts-YYY-ZZ-mean.csv  -- means of the simulation results
        dsmts-YYY-ZZ-sd.csv    -- standard deviations of the results

The files "NNNNN-sbml-l3v1.xml" are renamed versions of the original
model files, which had names of the form "dsmts-YYY-ZZ.xml". One slight
change was made for each: all compartments were given a 'spatialDimensions'
attribute so that they could be translated to SBML Level 2.

The files "NNNNN-sbml-l2vN.xml" are translated versions of the original
Level 3 model.

The file "NNNNN-results.csv" is a new file added for the purposes of the
SBML Test Suite.  "NNNNN-results.csv" is simply a combination of the mean
and standard deviation results files that are separated in the DSMTS; the
other parts of the SBML Test Suite use a single results file, so we simply
combined the DSMTS results into one file.

The file "NNNNN-model.m" is a new file added for the purposes of the SBML
Test Suite.  It mirrors the model file in the semantic test suite, with the
same overall format, same suite of component tags and test tags, and same
overall description.  The only difference is that the 'testType' listed
for all stochastic test suite models is 'StochasticTimeCourse'.

The file "NNNNN-settings.txt" is a new file added for the purposes of the
SBML Test Suite.  It mirrors the settings files in the semantic test suite,
with a few differences:
   * The 'absolute' and 'relative' settings are unused, as the method used
     to calculate a stochastic test success or failure is very different
     from that of a normal semantic test suite simulation.
   * A new 'output' setting is used, which lists all the 'X-mean' and 'X-sd'
     entries present in the NNNNN-results.csv file.  The relevant output 
     model variables are still listed in the 'variables' and 'amount' 
     settings--'output' appends both '-mean' and '-sd' to those variables.
   * New 'meanRange' and 'outputRange' settings are included, to be used
     in assessing a stochastic run's success or failure (see below).

The files "NNNNN-plot.*" are plots of the simulation results.  The HTML
version of the file is interactive; mousing over the plot lines brings
up a dynamic display of the value at that point in the plot.  The JPG
image is a static version of the plot.

Finally, the files whose names begin with "dsmts-" are the original
files from the DSMTS created by Evans, Gillespie and Wilkinson.


2. GETTING STARTED
======================================================================

To begin, please read the document "DSMTS-userguide-31v2.pdf" included
in this directory.  As described in more detail in that file, the
stochastic simulations of these models should be run n times, where n
is at a minimum 1,000, but more reasonably set to 10,000 for repeated
tests, and which will need to be 100,000 or 1,000,000 to detect more
subtle implementation errors.  Once the tests have been run, the
average value of X for each time point t (X_t) should be recorded,
along with the standard deviation of that value (S_t).  These
calculated values are then compared with the expected values (mu_t for
the expected mean at time point t, and sigma_t for the expected
standard deviation at time point t; values found in NNNNN-results.csv)
to calculate the following values:

  Z_t = sqrt(n) * (X_t - mu_t)/sigma_t

and

  Y_t = sqrt(n/2) * (S_t^2/sigma_t^2 - 1)


Z_t should always fall in the range 'meanRange' from the settings file
(which currently will always be the range (-3,3)).

Y_t should always fall in the range 'sdRange' from the settings file
(which currently will always be the range (-5,5)).

Note that due to the nature of stochastic simulation, a correct
simulator will still occasionally fail a test or two here or there,
especially when multiple tests are being performed.  As
DSMTS-userguide estimates, in a complete run of the entire stochastic
test suite with n=10,000, two or three Z_t tests may fail, and five or
six Y_t tests may fail.


3. TEST SUITE INTEGRATION
======================================================================

As of this writing, the DSMTS is not integrated into the SBML Test
Suite Test Runner or the Online SBML Test Suite.  The DSMTS is
distributed with the SBML Test Suite test cases in order to encourage
developers to begin investigating how they might be able to use it.

Please see the user guide in the file "dsmts-userguide.pdf" in this
directory for more information about the DSMTS.


4. LICENSE AND DISTRIBUTION TERMS
======================================================================

The DSMTS is licensed under the GNU LGPL by the original authors
(Evans, Gillespie, Wilkinson).

If you use DSMTS, please make sure to cite the paper by the original
authors:

  Evans, T. W., Gillespie, C. S., Wilkinson, D. J. (2008) The SBML
  discrete stochastic models test suite, Bioinformatics, 24:285-286.




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
