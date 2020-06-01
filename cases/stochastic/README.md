The SBML Test Suite –– Stochastic Test Cases
============================================

The tests in this directory are of two types: tests that check that a stochastic simulation of the reactions in the SBML models are correct (with the testType 'StochasticTimeCourse'), and tests that check that assignments drawn from distributions (from the 'Distributions' package) are correct (with the testType 'StatisticalDistribution').

The first 39 tests are all of type 'StochasticTimeCourse' and were drawn from the SBML Discrete Stochastic Model Test Suite (DSMTS), developed and contributed by Thomas Evans, Colin Gillespie and Darren Wilkinson.  Each test case consists of an [SBML](http://sbml.org) model intended for simulation in a discrete stochastic regime; the models have been solved either analytically or using numerical methods, and the expected time course data, together with expected means and standard deviations of model species quantities, are provided for each test case.  The combination of models and known results may be used to test the behavior of SBML-compatible stochastic simulation systems.

The 'StatisticalDistribution' tests were developed by Lucian Smith to test implementations of the 'Distributions' package.  They are included here because the same basic structure (repeated tests where the means and standard deviations are compared to expectations) is required.

----
*Main Authors*: Thomas W. Evans<sup>a</sup>, [Colin S. Gillespie](https://github.com/csgillespie)<sup>b</sup>, [Darren J. Wilkinson](https://github.com/darrenjw)<sup>b</sup>, [Lucian P. Smith](https://github.com/luciansmith)<sup>c,d</sup>

*Institutions*:<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>a</sup> University of Liverpool, Liverpool, UK<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>b</sup> Newcastle University, Newcastle, UK<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>c</sup> California Institute of Technology, Pasadena, CA, US<br>
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<sup>d</sup> University of Washington, Seattle, WA, US<br>

*Repository*:   [https://github.com/sbmlteam/sbml-test-suite](https://github.com/sbmlteam/sbml-test-suite)

*Bug tracker*:   [https://github.com/sbmlteam/sbml-test-suite/issues](https://github.com/sbmlteam/sbml-test-suite/issues)

*Developers' discussion group*: [https://groups.google.com/forum/#!forum/moccasin-dev](https://groups.google.com/forum/#!forum/sbml-interoperability)

*Pivotal tracker*: [https://www.pivotaltracker.com/n/projects/68714](https://www.pivotaltracker.com/n/projects/68714)


Please cite the DSMTS paper!
----------------------------

If you use this test suite, please cite the following paper:

<dl>
<dd>
Evans, T. W., Gillespie, C. S., Wilkinson, D. J. <a href="http://bioinformatics.oxfordjournals.org/content/24/2/285">The SBML
discrete stochastic models test suite</a>, <i>Bioinformatics</i>, 24:285-286, 2008.
</dd>
</dl>

Please also indicate the specific version of the SBML Test Suite you
use, to improve other people's ability to reproduce your results.



Organization of folders and files
---------------------------------

The directories and files have been renamed from their original versions according to the conventions used elsewhere in the [SBML Test Suite](https://github.com/sbmlteam/sbml-test-suite), but many of the files in each subdirectory are still named as they were originally so that the correspondences between this and the DSMTS should be easy to identify.

Here are the files in each directory (where `N`, `Y` and `Z` are digits):

*  `NNNNN-model.m`          – model description
*  `NNNNN-plot.html`        – interactive plot of simulation results
*  `NNNNN-plot.jpg`         – image of plot of simulation results
*  `NNNNN-results.csv`      – SBML Test Suite-style results file
*  `NNNNN-sbml-l2v1.xml`    – SBML Level 2 Version 1 model file
*  `NNNNN-sbml-l2v2.xml`    – SBML Level 2 Version 2 model file
*  `NNNNN-sbml-l2v3.xml`    – SBML Level 2 Version 3 model file
*  `NNNNN-sbml-l2v4.xml`    – SBML Level 2 Version 4 model file
*  `NNNNN-sbml-l2v5.xml`    – SBML Level 2 Version 5 model file
*  `NNNNN-sbml-l3v1.xml`    – SBML Level 3 Version 1 model file
*  `NNNNN-sbml-l3v2.xml`    – SBML Level 3 Version 2 model file
*  `NNNNN-settings.txt`     – SBML Test Suite settings file
*  `dsmts-YYY-ZZ.mod`       – original model definition
*  `dsmts-YYY-ZZ-mean.csv`  – means of the simulation results
*  `dsmts-YYY-ZZ-sd.csv`    – standard deviations of the results

The files `NNNNN-sbml-l3v1.xml` are renamed versions of the original
model files, which had names of the form `dsmts-YYY-ZZ.xml`. One
slight change was made for each: all compartments were given a
`spatialDimensions` attribute so that they could be translated to [SBML
Level 2](http://sbml.org/Documents/Specifications#SBML_Level_2).

The files `NNNNN-sbml-l2vN.xml` are translated versions of the original [SBML Level 3](http://sbml.org/Documents/Specifications#SBML_Level_3) version of the model.

The file `NNNNN-results.csv` is a new file added for the purposes of the SBML Test Suite.  `NNNNN-results.csv` is simply a combination of the mean and standard deviation results files that are separated in the DSMTS; the other parts of the SBML Test Suite use a single results file, so we simply combined the DSMTS results into one file.

The file `NNNNN-model.m` is a new file added for the purposes of the SBML Test Suite.  It mirrors the model file in the semantic test suite, with the same overall format, same suite of component tags and test tags, and same overall description.  The only difference is that the `testType` listed for all stochastic test suite models is `StochasticTimeCourse`.

The file `NNNNN-settings.txt` is a new file added for the purposes of the SBML Test Suite.  It mirrors the settings files in the semantic test suite, with a few differences:

* The `absolute` and `relative` settings are unused, as the method used to calculate a stochastic test success or failure is very different from that of a normal semantic test suite simulation.

* A new `output` setting is used, which lists all the `X-mean` and `X-sd` entries present in the `NNNNN-results.csv` file.  The relevant output model variables are still listed in the `variables` and `amount` settings – `output` appends both `-mean` and `-sd` to those variables.

* New `meanRange` and `outputRange` settings are included, to be used in assessing a stochastic run's success or failure (see below).

The files `NNNNN-plot.*` are plots of the simulation results.  The HTML version of the file is interactive; mousing over the plot lines brings up a dynamic display of the value at that point in the plot.  The JPG image is a static version of the plot.

Finally, the files whose names begin with `dsmts-` are the original files from the DSMTS created by Evans, Gillespie and Wilkinson.


Getting started
---------------

For all tests, the stochastic simulations of these models should be run <i>n</i> times, where <i>n</i> is at a minimum 1,000, but more reasonably set to 10,000 for repeated tests, and which will need to be 100,000 or 1,000,000 to detect more subtle implementation errors.  Once the tests have been run, the `NNNNN-settings.txt` file will contain one or more of the following entries, where `X` is the name of the variable in question:
* <i>X-mean</i>: The average value of <i>X</i> for each time point <i>t</i> (<i>X<sub>t</sub></i>)
* <i>X-sd</i>: The standard deviation of the set of <i>X</i> values for each time point <i>t</i> (<i>S<sub>t</sub></i>)
* <i>ln(X)-mean</i>: The average value of the natural log of <i>X</i> for each time point <i>t</i> (<i>ln(X)<sub>t</sub></i>)
* <i>X-sd</i>: The standard deviation of the set of the natural log of <i>X</i> values for each time point <i>t</i> (<i>S<sub>ln, t</sub></i>)

Any requested calculated values are then compared with expected values found in the `NNNNN-results.csv` files, with the same column titles as above.  If we designate the expected means as <i>mu<sub>t</sub></i>, and the expected standard deviations as <i>sigma<sub>t</sub></i>, we then calculate Z (if `X-mean` or `ln(X)-mean` are in the expected output):

<p align="center">
<i>Z<sub>t</sub> = sqrt(n) * (X<sub>t</sub> - mu<sub>t</sub>)/sigma<sub>t</sub></i>
</p>

and Y (if `X-sd` or `ln(X)-sd` are in the expected output):

<p align="center">
<i>Y<sub>t</sub> = sqrt(n/2) * (S<sub>t</sub><sup>2</sup>/sigma<sub>t</sub><sup>2</sup> - 1)</i>
</p>

<i>Z<sub>t</sub></i> should fall in the range `meanRange` from the settings file (often the range (-3,3)).

<i>Y<sub>t</sub></i> should fall in the range `sdRange` from the settings file (often the range (-5,5)).

Due to the nature of stochastic simulation, a correct simulator will still occasionally fail a test or two here or there, especially when multiple tests are being performed. Z or Y failing 0-1 times per test is great, and failing 2-3 times should probably be checked to see if this is consistent, and/or at the same time points.

Note that for some tests, `X-sd` is provided in the `XXXX-results.txt` but not requested in the `XXXXX-settings.txt` file.  This is because the expected standard deviations (sigmas) are requried to calculate Z, but the distribution in question has no expected range for the standard deviations themselves.

More details about the first 39 tests are included in the document [DSMTS-userguide-31v2.pdf](DSMTS-userguide-31v2.pdf) included in this directory.

(Lack of) Integration with the rest of the SBML Test Suite
----------------------------------------------------------

As of this writing, the DSMTS is not integrated into the SBML Test Suite Test Runner or the database of test results at [http://sbml.org/Facilities/Database](http://sbml.org/Facilities/Database).  The DSMTS is distributed with the SBML Test Suite in order to encourage developers to begin investigating how they might be able to use it, but users and developers need to develop their own approaches to running the tests in software.

Please see the user guide in the file [DSMTS-userguide-31v2.pdf](DSMTS-userguide-31v2.pdf) in
this directory for more information about the DSMTS.


History
-------

Originally developed circa 2007-2010.  The SBML Level 3 Version 1 version of DSMTS (dated October 21, 2010) was added to the SBML Test Suite distribution in 2015.


License and distribution terms
------------------------------

The DSMTS is licensed under the GNU LGPL by the original authors
(Evans, Gillespie, Wilkinson).


More information
----------------

More information about the SBML Test Suite is available online at
[http://sbml.org/Software/SBML_Test_Suite](http://sbml.org/Software/SBML_Test_Suite).

[![SBML Logo](https://raw.githubusercontent.com/sbmlteam/sbml-test-suite/develop/src/misc/graphics-originals/Official-sbml-supported-70.jpg)](http://sbml.org)
