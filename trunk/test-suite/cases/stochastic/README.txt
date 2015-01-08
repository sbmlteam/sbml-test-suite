            DSMTS -- Discrete Stochastic Model Test Suite

 Thomas W. Evans (a), Colin S. Gillespie (b), Darren J. Wilkinson (c)
                   (a) University of Liverpool, UK
                     (b) Newcastle University, UK

          If you use DSMTS, please cite the following paper!

  Evans, T. W., Gillespie, C. S., Wilkinson, D. J. (2008) The SBML
  discrete stochastic models test suite, Bioinformatics, 24:285-286.

                Originally developed circa 2007-2010.
        The SBML 3.1 version of DSMTS (dated October 21, 2010)
     was added to the SBML Test Suite distribution in late 2014.

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
conventions used elsewhere in the SBML Test Suite, but the name of the
model files in each subdirectory do still include the base name of the
original model files so that they can be identified more easily.

Here are the files in each directory (where "N", "Y" and "Z" are digits):

  NNNNN/NNNNN-dsmts-YYY-ZZ.mod   -- original model definition
        NNNNN-mean.csv           -- means of the simulation results
        NNNNN-results.csv        -- combined means & std deviation
        NNNNN-sd.csv             -- standard deviations of the results
        NNNNN-settings.txt       -- SBML test suite settings file
        NNNNN-sbml-l3v1.xml      -- SBML Level 3 Version 1 model file


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

