            DSMTS -- Discrete Stochastic Model Test Suite

 Thomas W. Evans (a), Colin S. Gillespie (b), Darren J. Wilkinson (c)
                   (a) University of Liverpool, UK
                     (b) Newcastle University, UK

          If you use DSMTS, please cite the following paper!

  Evans, T. W., Gillespie, C. S., Wilkinson, D. J. (2008) The SBML
  discrete stochastic models test suite, Bioinformatics, 24:285-286.

                Originally developed circa 2007-2010.
        The SBML 3.1 version of DSMTS (dated October 21, 2010)
        was added to the SBML Test Suite distribution in 2015.

            The master version of the DSMTS is located at:
                     http://dsmts.googlecode.com/

   ,--------------------------------------------------------------.
  | Table of contents                                             |
  | 1. Explanation                                                |  
  | 2. Getting started                                            |
  | 3. License and distribution terms                             |
   `--------------------------------------------------------------'

--------------
1. EXPLANATION
--------------
    
The SBML Discrete Stochastic Model Test Suite (DSMTS) was developed
and contributed by Thomas Evans, Colin Gillespie and Darren Wilkinson.
Each test case consists of an SBML model intended for simulation in a
discrete stochastic regime; the models have been solved either
analytically or using numerical methods, and the expected time course
data, together with expected means and standard deviations of model
species quantities, are provided for each test case.  This may be used
to test the behavior of stochastic simulators.

The directories and files have been renamed according to the
conventions used elsewhere in the SBML Test Suite, but many of the
files in each subdirectory are still named as they were originally so
that the correspondences should be easy to identify.

Here are the files in each directory (where "N", "Y" and "Z" are digits):

  NNNNN/dsmts-YYY-ZZ.mod       -- original model definition
        dsmts-YYY-ZZ-mean.csv  -- means of the simulation results
        dsmts-YYY-ZZ-sd.csv    -- standard deviations of the results
        NNNNN-results.csv      -- SBML Test Suite-style results file
        NNNNN-settings.txt     -- SBML Test Suite settings file
        NNNNN-sbml-l3v1.xml    -- SBML Level 3 Version 1 model file

The files "NNNNN-sbml-l3v1.xml" are renamed versions of the original
model files, which had names of the form "dsmts-YYY-ZZ.xml".

The files "NNNNN-results.csv" and "NNNNN-settings.txt" are new files
added for the purposes of the SBML Test Suite.  "NNNNN-results.csv" is
simply a combination of the mean and standard deviation results files
that are separated in the DSMTS; the other parts of the SBML Test
Suite use a single results file, so we simply combined the DSMTS
results into one file.


------------------
2. GETTING STARTED
------------------

As of this writing, the DSMTS are not full integrated into the SBML
Test Suite Test Runner or the Online SBML Test Suite.  The DSMTS is
distributed with the SBML Test Suite test cases in order to encourage
developers to begin investigating how they might be able to use it.

Please see the user guide in the file "dsmts-userguide.pdf" in this
directory for more information about the DSMTS.

If you use DSMTS, please make sure to cite the paper by the original
authors:

  Evans, T. W., Gillespie, C. S., Wilkinson, D. J. (2008) The SBML
  discrete stochastic models test suite, Bioinformatics, 24:285-286.


---------------------------------
3. LICENSE AND DISTRIBUTION TERMS
---------------------------------

The DSMTS is licensed under the GNU LGPL by the original authors
(Evans, Gillespie, Wilkinson).

