NEWS &ndash; history of changes to the SBML Test Suite
================================================

Version 3.3.0 (2017-12-12)
--------------------------

_Important_: **do not run** semantic test cases 1199-1204 on systems that use [libSBML](http://sbml.org/Software/libSBML) 5.11.2 or earlier.  Test cases 1199-1204 test the use of nested MathML `<piecewise>` constructs, which unfortunately revealed a bug in [libSBML](http://sbml.org/Software/libSBML) versions prior to 5.11.4.  Make sure to use [libSBML](http://sbml.org/Software/libSBML) 5.11.4 or later.  LibSBML versions prior to 5.15.0 will also incorrectly report a handful of test files as being invalid SBML, but will actualy parse the files correctly; software developers should be aware of this if using libSBML versions prior to 5.15.0.

New in this release:

* The SBML Test Suite home repository has been moved to GitHub.  New development will take place in the branch called `develop`: [https://github.com/sbmlteam/sbml-test-suite/tree/develop](https://github.com/sbmlteam/sbml-test-suite/tree/develop) Each time we make a new release, we will merge the changes into branch `master` and start the next new developments in `develop`.

* There is a new SBML Test Runner installer for Mac OS&nbsp;X. The installation is now distributed as an OS&nbsp;X `.pkg` installer file instead of a `.dmg` image.  It will install the Test Runner into the user's `/Applications` folder by default.

* New semantic test cases.  Note that many tests starting at 01234 specifically test models that follow the release candidate specification of [SBML Level&nbsp;3 Version 2](http://sbml.org/Documents/Specifications/SBML_Level_3/Version_2/Core/Release_1), which encode elements and situations that were not present or illegal in SBML Level&nbsp;3 Version&nbsp;1.  If your simulator uses libSBML, it will need libSBML version [5.15.0](http://sbml.org/Software/libSBML) or later of the stable release of libSBML, or [5.14.0](http://sbml.org/Software/libSBML) or later of the experimental release.

    - All existing tests that could be translated to SBML Level 3 Version&nbsp;2 (L3V2) now have L3V2 versions.

    - Cases 01219-01223 test the use of compartment sizes other than `1` in combination with of species having attribute `hasOnlySubstanceUnits`=`true` and various values for attributes `initialAmount` and `initialConcentration` in combination with rate rules.

    - Cases 01224-01233 and 01300-01306 test the use of a reaction identifier in the MathML of another construct.

    - Cases 01234-01247, 01271, and 01300-01306 test simulator robustness to the use of elements without child elements allowed in L3V2 but *not* in L3V1.  This includes `<math>` children especially, but also things like the `<trigger>` of events.  This is because some things are allowed in L3V2 that were not allowed in lower Level+Version combinations of SBML.

    - Cases 01248-01270 and 01293-1299 test the new `rateOf` SBML Level&nbsp;3 Version&nbsp;2 construct.

    - Cases 01272-01281 test the new MathML elements: `max`, `min`, `rem`, `quotient`, and `implies`, allowed in SBML Level&nbsp;3 Version&nbsp;2.

    - Cases 01282-01292 test the use of Boolean values in numeric contexts, and visa versa.

    - Cases 01307-01309 are tests where a species in the model is used as an amount, but its output is requested by the test suite in terms of its concentration.

    - Case 01310 has a compartment with a `spatialDimensions` value of 2.7.  (Yes, this is valid SBML.)

    - Cases 01311-01315 test nested function definitions.

    - Cases 01316-01322 test csymbols with names that shadow other parameters or functions.

    - Cases 01323 tests that the proper value of Avogadro's constant is being used, when compared to all historical values of Avogadro's constant.

    - Cases 01324-01338 test various event semantics, including simultaneous and delayed events.

    - Cases 01339-01340 test reactions whose rate changes between being negative and being positive.

    - Cases 01341-01342 test the existence of a compartment, species, and nothing else.

    - Case 01343 tests the `tanh` MathML function.

    - Cases 01344-01394 test hierarchical models where the IDs of replaced elements in submodels are used in math in those submodels, including dependency chains across parent/child boundaries.

    - Case 01395 is a huge model where it can make a difference if the rules are not re-ordered.

    - Cases 01396-99 test combinations of `fast` reactions with assigned stoichiometries and local parameters.

    - Cases 01400-01419 test the `rateOf` and `delay` csymbols in combination with each other and with various other tests.

    - Cases 01420-01453 test single reactions that have multiple speciesReferences that all point to the same species.

    - Case 01454 tests the combination of the `delay` csymbol and the VolumeConcentrationRates.

    - Cases 01455-01460 test the combination of the `rateOf` csymbol with  compartments having volumes other than `1`, the `hasOnlySubstanceUnits` attribute, and local parameters.

    - Case 01461 tests a `rateOf` csymbol that points to a parameter with a rate rule that has no MathML content.

    - Cases 01462-01463 test the `rateOf` csymbol with VolumeConcentrationRates.

    - Cases 01464-01465 test assigned stoichiometries with no MathML.

    - Case 01466 tests random event execution at time = 0.

    - Cases 01467-01470 test conversion factors separated by submodel indirection.

    - Cases 01471-01477 and 1778 test combinations of `comp` elements, including external model definitions, conversion factors, and the `replacedBy` construct.

    - Case 01478 tests volume concentration rates with a function definition.

    - Case 01479 tests csymbol `avogadro` in an algebraic rule.

    - Cases 01480-01481 test stoichiometry math with the `delay` csymbol.

    - Cases 01482-01484 test the `rateOf` csymbol in an algebraic rule.

    - Cases 01485-01497 test various combinations of uncommon MathML used in function definitions.

    - Case 01498 tests the combination of species references in MathML with volume concentration rates.

    - Cases 01499-01503 test algebraic rules with conversion factors and uncommon MathML.

    - Cases 01504-01512 test volume concentration rates in concert with events of various types.

    - Cases 01513-01514 test volume concentration rates in concert with initial assignments.

    - Cases 01515-01517 test uncommon MathML in rate rules and with assigned stoichiometries.

    - Cases 01518-01524 test the csymbol `delay` with various types of events.

    - Cases 01525-01529 test the `rateOf` csymbol with various types of events.

    - Cases 01530-01533 test uncommon MathML in event triggers, assignments, priorities, and delays.

    - Cases 01534-01539 test the csymbol `delay` when assigning to stoichiometries, with boundary species, and with fast reactions.

    - Cases 01540-01543 test the `rateOf` csymbol with boundary species and with assigned stoichiometries.

    - Cases 01544-01546 test conversion factors in reactions that have a `fast` attribute value of `true`.

    - Cases 01547-01551 test reactions that have `fast="true"` with function definitions having various kinetic laws.

    - Cases 01552-01554 test assigning to stoichiometries, but with missing MathML.

    - Cases 01555-01557 test assigning to boundary species, but with missing MathML.

    - Cases 01558-01560 test referencing a species reference from a fast reaction.

    - Cases 01561-01563 test uncommon MathML used to assign to stoichiometries.

    - Cases 01564-01565 test uncommon MathML used to assign to kinetic laws of normal and fast reactions.

    - Case 01566 tests uncommon MathML that use a species reference.

    - Cases 01567-01570 test fast reactions with algebraic rules, assignment rules, rate rules, and initial assignments.

    - Cases 01571-01572 test fast reactions with assigned stoichiometries.

    - Cases 01573-01574 test assigned stoichiometries for boundary species.

    - Cases 01575-01579 test algebraic rules with various types of events and event elements.

    - Cases 01580-01587 test assigned stoichiometries in the context of various types of events.

    - Cases 01588-01593 test random events with initial assignments, algebraic rules, rate rules, delayed events, and the csymbol delay.

    - Cases 01594-01599 test uncommon MathML in various types of events.

    - Cases 01600-01603 test missing MathML in delayed events.

    - Case 01604 tests the `time` csymbol in a delayed event assignment.

    - Case 01605 tests random events with additional event assignments with no MathML.

    - Cases 01606-01616 are FBC v2 versions of the previously-added FBC v1 tests.

    - Cases 01617-01623 test FBC v2 non-strict tests with assigned constant bounds and stoichiometries.

    - Cases 01624-01625 test FBC minimization where one reaction has a fixed value (both v1 and v2).

    - Cases 01626-01627 test random events with stoichiometries and volume-concentration rates.

    - Cases 01628-01630 test FBC initial assignments and assignment rules with no MathML.

    - Cases 01631-01634 test when multiple reactions with assigned stoichiometries affect the same species.

    - Cases 01635-01636 test assigning variable stoichiometries to boundary species.

    - Case 01637 tests a variable stoichiometry with the same ID as a local parameter.

    - Cases 01638-01640 test boundary species and local parameter ID shadowing.

    - Case 01641 tests `avogadro` and `time` with the name of the other csymbol.

    - Cases 01642-01653 test conversion factors with function definitions, substance-only species, local parameters, and species references.

    - Case 01654 tests calling a function definition with a species reference.

    - Case 01655 tests a species reference to a substance-only species.

    - Cases 01656 and 1764-1774 test local parameters that shadow species reference IDs, in various MathML contexts.

    - Case 01657 tests a species reference with an empty assignment rule.

    - Cases 01658-01665 test the use of csymbol `avogadro` in various event elements and a rate rule.

    - Cases 01666-01668 test when conversion factors are set for species only affected by rate rules.

    - Cases 01669-01692 test conversion factors for species also affected by or used in events.

    - Cases 01693-01702 test function definitions and various events, including ones that fire at t0, and those affected by assignment-time or trigger-time settings.

    - Cases 01703-01709 test substance-only species together with various types of events.

    - Cases 01710-01716 test local parameters shadowing global parameters that are used in various event constructs, and for different types of events.

    - Cases 01717-01721 test referencing stoichiometries in various types of events.

    - Cases 01722-01723 test using `avogadro` with assigned stoichiometries.

    - Cases 01724-01738 test conversion factors, assigned stoichiometries, and boundary conditions in different combinations.

    - Cases 01739-01741 test conversion factors and local parameters.

    - Cases 01742-01745 test stoichiometries assigned with function definitions.

    - Cases 01746-01749 test substance-only species in reactions with set stoichiometries.

    - Cases 01750-01753 test local parameters shadowing variable stoichiometries and boundary species.

    - Cases 01754-01757 test the use of initial assignments with various types of events, especially those that fire at time = 0.

    - Cases 01758-01759 test delayed events that trigger themselves.

    - Cases 01760-01763 test the potential conflict between the `avogadro` csymbol with parameters and local parameters named `avogadro`.

    - Cases 01775-01777 test using avogadro to set conversion factor values.

    - Case 01778 tests a comp model with a conversion factor affecting a reaction rate.

    - Cases 01779-01780 test simultaneous event assignment of a species concentration and its compartment.

* In addition to the above, a few general things have been cleaned up in existing semantic test cases. Examples include model identifiers of a few test models that were misleading (now fixed), and a few tests that were designed to test species in multiple compartments were at some point accidentally changed so that all the species were put into a single compartment (they have been redistributed appropriately now).

Changes in this release:

* The SBML Test Runner's algorithm for comparing test results and displaying the differences between expected and actual result values now properly ignores column ordering.  Thanks to Leandro Watanabe (U. Utah) for reporting the problem.

* The Test Runner now allows the combination of filters that include, simultaneously, tags, problematic test cases, and only supported test cases.  Thanks to Chris Myers for raising the issue and testing the implementation.

* The Test Runner now optionally monitors for file changes in the currently-displayed test case.  If the application's output `.csv` file is changed outside the runner (e.g., by manually editing the file), it should now notice the changes and update the display.  In addition, the Test Runner also monitors the test case settings file, to detect changes that developers may make while experimenting with test case defnitions.  Note that for architectural reasons, only the case currently being displayed is so monitored.  A setting in the preferences dialog allows this feature to be turned on and off.

* This release of the SBML Test Runner works around a crashing bug caused by an SWT-GTK issue that manifests itself on Ubuntu 16.

* This release of the SBML Test Runner works around a font scaling issue that manifests itself on Windows when using display scaling of 150%.

* The default value of the "_Always check for new versions of test cases when application starts_" preference option is now "yes" instead of "no".  Users who have used the previous version of the Test Runner will not be affected; this only affects new users of the Test Runner.

* The File &#8594; Exit menu item should now work on Windows.  Thanks to Thomas Hamm for reporting that it did nothing before.  Missing keyboard shortcuts on Windows are now available in the lastest version.

* The Test Runner features various other small fixes and improvements.

* With the addition of FBC v2 tests, the `packagesPresent` line in the .m file can now include `fbc_v1` or `fbc_v2`, depending on which version of the test it is.  The original `fbc` tag is still present.  In addition, a new FBC test tag 'fbc:NonStrict' is now provided for FBC v2 models where the 'strict' flag has been set to 'false'.

* The issue tracker for the Test Suite is now the GitHub tracker: [https://github.com/sbmlteam/sbml-test-suite/issues](https://github.com/sbmlteam/sbml-test-suite/issues). Lucian Smith migrated the previous issues from the SourceForce tracker, so they are now in the GitHub tracker, for history.  We thank the guides by [Thomas Zajac](https://github.com/mephenor/JSBML-Migration-Guide/wiki/) and [Chris Mungall](https://github.com/cmungall/gosf2github) for helpful info about how to achieve this migration.


Version 3.2.0 (29 July 2016)
----------------------------

_Important_: **do not run** semantic test cases 1199-1204 on systems that use [libSBML](http://sbml.org/Software/libSBML) 5.11.2 or earlier.  Test cases 1199-1204 test the use of nested MathML `<piecewise>` constructs, which unfortunately revealed a bug in [libSBML](http://sbml.org/Software/libSBML) versions prior to 5.11.4.  Make sure to use [libSBML](http://sbml.org/Software/libSBML) 5.11.4 or later.

New in this release:

* Beginning with this version, the distribution of the test cases will be split, with separate archives for semantic, stochastic and syntactic test cases.  This will not effectively change the semantic cases distribution archives, but will allow for the introduction of new stochastic and syntactic test cases without disrupting the existing infrastructure for the test suite.

* New in 3.2.0, with the consent of the original authors, the [Discrete Stochastic Model Test Suite](https://www.ncbi.nlm.nih.gov/pubmed/18025005) (DSMTS) is available in SVN and will be available as a separate archive release.  The DSMTS was developed by Thomas W. Evans, Colin S. Gillespie, and Darren J. Wilkinson (from the University of Liverpool and Newcastle University).  The files are located in the directory cases/stochastic in the SVN repository and distributed as `sbml-stochastic-test-cases-YYYY-MM-DD.zip`.  Currently, these files are not fully integrated into the SBML Test Suite, and in particular, the SBML Test Runner and the Online facilities do not interact with these files, although it is our goal to figure out how to integrate them better in the future.  For now, as a first step, we distribute the DSMTS as an add-on to the SBML Test Suite so that people can start experimenting with it.  Please see the file [cases/stochastic/README.txt](cases/stochastic/README.txt) for more information, including a reference to the paper by Evans et al. and pointers on how to use the DSMTS.

* New in 3.2.0, the syntactic test cases directory ([cases/syntactic](cases/syntactic)) has been completely replaced by a large set of test cases auto-generated from libSBML's syntactic test cases.  Lucian Smith developed a program that generates the syntactic test cases from libSBML's code base.  This program is `createSyntacticTests` and can be found in the subdirectory [src/utilities/createSyntacticTests](src/utilities/createSyntacticTests) of the SBML Test Suite source code repository.

* The semantic test cases in the cases/semantic/ directory now include [OMEX](http://bmcbioinformatics.biomedcentral.com/articles/10.1186/s12859-014-0369-z) format files.  (See [http://co.mbine.org/documents/archive](http://co.mbine.org/documents/archive) for a description of this format.)  They were generated by a modified version of Frank Bergmann's program to generate SED-ML files.  Stanley Gu has also contributed an OMEX file generator written in Python, and this is available in the SBML Test Suite SVN repository.

* New test cases 1199-1218.  Cases 1199-1204 test the use of nested piecewise operators in mathematical formulas; cases 1205-1208 test variable-size compartments; cases 1209-1216 test the use of multiple arguments to MathML relational operators such as `le`, `ge` and `eq`; cases 1217-1218 check interdependent initial assignments and assignment rules.

Updates:

* SBML Level 2 Version 5 models added to all tests that included a Level 2 Version 4 model for that test.  All are syntactically identical to the L2v4 versions, apart from the declared SBML namespace.

* The `plotresults.py` script used to generate the plots of results now offers the ability to plot on dual axes.  This is used to generate the plots for the stochastic test suite results.  Also, `plotresults.py` has had its command line options changed in order to work with the input/output combinations that are now possible.  Also, `plotresults.py` now works in Python 3.

* The stochastic test cases were updated from the originals taken from the DSMTS to be fully valid SBML, and to include other level/version combinations.

* Results from stochastic test 33 (originally test 003-04 in the DSMTS) were re-calculated by Darren Wilkinson, after the previous results were found to be slightly incorrect.

* Fixes to validation warnings and errors for models 937, 950, 983, and 1018.

Additional notes:

* The SBML Test Runner has not yet been updated for release 3.2.0 of the test cases.  We will announce when a new version of the Test Runner is available.

* This is the last release planned from SourceForge.net.  In the future, we plan to move to GitHub and make future releases from GitHub.


Test case archive update (2014-10-23)
-------------------------------------

This release updates only test cases.

* Loosened tolerances of settings for case 1148. 

* Corrected the description of case 978.


Version 3.1.1 release (2014-03-06)
----------------------------------

Summary of changes in the SBML Test Runner:

* Fixed handling of network updates to the test cases.  The previous version would incorrectly always report that new test cases are available for downloading, even after the user did an update.

* Bundles the 2014-02-27 release of the test case archive.

* Tested under Mac OS X 10.9 (Mavericks).


Test case archive update for release 3.1.0 (2014-02-27)
-------------------------------------------------------

This release concentrated on updates to the SED-ML files included with the test case archive; no other changes have been made.

* Support for packages (_fbc_: uses SED-ML L1v2, _comp_: assumes flattening by libSBML)

* Fixed an issue where time course simulations would yield one additional output point

* Fixed a number of issues with adjustments for amounts and concentrations.

(In the source code, the changes above are implemented in the SED-ML file generator in src/utilities/sedml.  This is what is used to produce the -sedml.xml files included in the archive release.)


Version 3.1.0 release (2013-12-10)
----------------------------------

Summary of changes to the test cases:

* Test cases 1186-1196 for the [SBML Flux Balance Constraints](http://sbml.org/Documents/Specifications/SBML_Level_3/Packages/fbc) package (_fbc_) have been updated to reflect the syntax of the final SBML _fbc_ specification, for example with respect to `<listOfFluxes>` vs.  `<listOfFluxObjectives>`.

* Test cases 1186-1196 for _fbc_ have been udpated to standardize the way `<notes>` element bodies' XML namespaces are declared.

* Removed the Test Suite tag `comp:NotRequired` on [SBML Hierarchical Model Composition](http://sbml.org/Documents/Specifications/SBML_Level_3/Packages/comp) (_comp_) models because the `required` attribute for _comp_ is now considered to be always required to be `"true"`.

* Corrected a bug in the description of some _comp_ models that claimed their initial value was set by a rate rule.

* Corrected a small error in the description of a numerical value in model 1000.n

* Updated some _comp_ models to conform to the validation rules defined in Release 3 of the _comp_ specification.  This was mostly in regards to elements pointing at other elements.

* Added some missing `comp:SBaseRef` tags to _comp_ models that required them.

* Updated the names and id's of a number of models so that they matched the pattern `case#####` used by other models.

* The cases directories (cases/semantic and cases/syntactic) now contain a copy of the date-of-release marker file, named `.cases-release-date`.

* SED-ML files are now available for the FBC test cases.

Summary of changes and new features in the SBML Test Runner:

* The Runner can now check the test case download site at SourceForge for updates; if it finds a new case archive, it offers to download it and install it.  Along with this functionality, there is a new item in the preferences dialog to determine whether an update check is performed when the application first starts up.  (The default is no.)

* The dialog presented while test cases are being unpacked has been changed completely and is more responsive.

* There is a new menu item in the Help menu, for reinstalling the test cases shipped with the Test Runner.

* The dialogs showing tags (which appear in two places, one for the wrapper configuration, and another in the filter dialog) now show summaries of the tags in the pop-up tooltips.  Users can hover their mouse over a tag name to see a summary of the tag's meaning.

* The Runner now checks for missing columns in the data and reports it as an error.

* The user manual has been updated to account for some of the changes described above.

* Various bugs have been fixed.


Version 3.0.0 release (2013-06-06)
----------------------------------

Changes to the test cases:

* The previous 3.0.0 beta release had a testing artifact accidentally left in the results file for case 000950.  Fixed.

Changes to the SBML Test Runner:

_This is the release of version 3.0.0.  The following are additional
changes not logged in the notes for beta versions up to this point._

New features:

* The position and size of the main window and the results map are now remembered across invocations of the Test Runner.

* The results map now displays an explanation of the result codes, in the information box at the bottom, when the mouse is moved over a result in the map.

* The runner now provides a way to view output that might have been produced by the wrapper on the standard output and standard error streams.  There are menu options in the menu bar and the pop-up menus.

* There is a now a help system.  Contents are sparse at the moment, but at least there's something...

Changes: 

* The SBML Test Runner will now delete output results files before invoking the wrapper on each test case.  If the deletion fails, it reports an error (as a "black" color in the results map).  This is to help prevent situations in which an output file is locked by another process (e.g., open in an Excel window on Windows) and the wrapper does not return an error value when it tries to write to the file; in that situation, the Test Runner would have no way of knowing that a problem occurred.  (If the wrapper could be counted on to return an error, then there wouldn't be a potential problem, but not all wrappers behave as expected.)  This way, by attempting to delete the file first, the Test Runner can tell the user something is wrong.

* The system previously didn't report the case of when a results file existed but was unreadable or had a parse error.  The system now catches this and communicates the errors up to the GUI.

* The order of OK/Save and Cancel buttons in the dialogs has been made consistent across the different dialogs.

* There are now new menu items to sync/refresh selected results from their files, instead of (previously) having only the option to sync all results.

* The system no longer selects any cases when you first start up.  Instead, things are left blank, and a message is printed in the description area about "no case selected" to give the user a clue about why they're seeing blanks.

* Implemented graphing of SBML FBC results.  The current implementation is kind of simplistic and uses bar graphs, but hopefully will be good enough for now.

* Commented out the SED-ML menu options because they're currently unimplemented and may not be for a while yet.

* Fixed: changing the SBML Level & Version using the pull-down menu in the main window didn't refresh the case list or stop a running simulation.

Released 6 June 2013.


Version 3.0.0beta2 release (2013-05-21)
---------------------------------------

Changes to the SBML Test Runner:

* Upon a first-time start-up, the preferences/wrapper definition panel
  left the wrapper fields editable even if the user did not click on
  "Add".  This made was a confusing situation.  It now properly
  disables the fields.

* Released 21 May 2013.


Version 3.0.0beta1 release (2013-05-17)
---------------------------------------

* Release beta1 of the new, standalone, SBML Test Runner.  (Note: the
  test cases archive does not include the Test Runner; it is made
  available as a separate download.)

* Changes to SBML test cases:

  - Removed invalid model 00173-sbml-l1v2.xml (it used functions not
    allowed in l1 infix).

  - Added compartment to 951 and 954 l1 models, for validity.

  - Reordered components of 1027, 1028, and 1029 l2v1 models, for
    validity.

  - Fixed validity of model 1141 (incorrect `const` flag).

  - Model 1141's settings file claimed it wanted a compartment's
    `concentration`.  This was removed.

  - Various tags added and removed where appropriate, because they
    were incorrect.  A few InitialValue tags; a few BoundaryCondition
    tags, a few missing MultiCompartment tags for comp models; a few
    incorrect comp:SubmodelOutput tags; a few tags for deleted
    components of comp models, on the theory that correct
    interpetation of the model doesn't depend on anything that was
    deleted.

  - Model descriptions written for a few models where "{Write general
    description of why you have created the model here.}" was still
    there. Fixed.

  - Added pre-l3v1 versions of n-ary MathML tests to the .m files (the
    actual SBML had been there before).

    
Version 2.3.2 release (2013-02-03)
---------------------------------------

* Changes:

  - The settings.txt files for the FBC tests (01186-01196) now
    have their `start`, `duration`, and `steps` lines set to be
    blank instead of having dummy values that were not used.

  - FBC package test case results are now plotted in a more sensible
    way, using a different plot type.

  - The absolute and relative errors for FBC tests were relaxed
    to 0.001.

  - The *-model.m files for the FBC tests were modified to include
    more detailed descriptions of the models.

  - The test tags `BoundaryCondition` and `NonUnityStoichiometry`
    were added to all FBC tests (as they all contained those elements)
    and the tags fbc:BoundGreaterEqual, fbc:BoundLessEqual, and
    fbc:BoundEqual were added to several tests where they had been
    missing.

  - The tolerances for tests 00983 and 00993 were relaxed by request.


Version 2.3.1 release (2013-01-27)
---------------------------------------

The only difference is a change to the settings of case 993.  The
tolerances have been loosened slightly.


Version 2.3 release (2013-01-16)
---------------------------------------

The only real difference between this release and 2.2 is the addition
of test cases for the SBML Level 3 packages of (a) Hierarchical Model
Composition and (b) Flux Balance Constraints.  They are test cases
#1124-1196 minus #1184-1185.


Version 2.2 release (2012-12-28)
---------------------------------------

IMPORTANT NOTE: the .zip archive distribution of cases ONLY contains
cases #0-1123,1184,and 1185; cases #1124-1196 are available in the SVN
repository only, and are not distributed in the archive for 2.2.  They
will distributed in the archive for the next major release (2.3).

* Changes:

  - Completely changed how plots are generated.  The new version uses
    HTML-based plots, with interactive features thanks to the use of
    the Highcharts JS JavaScript-based plotting library.  Past code
    involving the use of gnuplot, batik, etc., is all gone.  

  - As part of the change noted above, SVG plot files are no longer
    provided; instead, the files are HTML files with embedded SVG (and
    JavaScript code) within them.  The Makefile commands involving the
    generation of specific plot formats are now different; the
    commands are

        make html-plots
        make jpg-plots
        make png-plots

    The command "make plots" still works as before, except for the
    change mentioned above, that no SVG files are produced.

  - Cases 1124-1196 are a new batch of test cases, the first for
    testing SBML Level 3 packages.  Along with this comes a change to
    the format of the *-model.m file, which now includes a new line,
 
        packagesPresent: X, Y, Z, ...

    where "X, Y, Z, ..." is a list of Level 3 packages used in the
    model; also, the componentTags and testTags have package-specific
    tags (e.g., "comp:ModelDefinition") about the components and
    tests related to the packages represented in the test.

* Bug fixes:

  - Case #967 was tagged as having a random event execution, but did
    not actually have such.  The tag has been removed.

  - Cleaned up some cruft accidentally left in test case description
    *-model.m files for cases #1117, 1118, 1126, 1146, 1147, 1161-1164
    and 1168.

  - Some cases in the range 1117-1168 were erroneous labeled as being
    analytic, when in fact they were generated numerically.


Version 2.1.2 release (2012-10-09)
---------------------------------------

* Bug fixes:

  - Models 1117 and 1121 did not have proper `synopsis` entries in their
    .m files.  These have been added.

  - The model ids in test 931 were changed from `case_00930` to `case_00931`

  - The VERSION.txt file was not updated for 2.1.1 from 2.1.0.  Now it
    correctly reads the current release number (2.1.2).

  - The documentation for some cases had misformatted division
    operators: the `/` character was missing in some cases where a
    division was being described.  Those parts of the documentation
    should now be fixed.

* Changes since the last update:

  - Models whose L3 versions were split to new test directories now mention
    this fact in the description of the test in the .m file.

  - We updated the copyright year.


Version 2.1.1 release (2012-08-21)
---------------------------------------

General note: it is not clearly stated anywhere, but test case
directories do not always contain all Level/Version combinations of
SBML.  This happens for cases that contain a feature handled
differently in one SBML Level compared to another.  In other words, in
the individual cases/semantic/* directories, you may find models that
are only provided in SBML Level 2 or Level 3 form, and this is normal;
it does not indicate models are missing.

* Bug fixes:

  - In the version 2.1.0 cases archive distribution file, some case
    directories contained SED-ML files even though there were no
    corresponding SBML model.  This happened for Level 3 versions of
    some models and changed between releases 2.0 and 2.1; the Level 3
    versions of some cases were split out as separate cases in release
    2.1, leaving only the Level 1-2 versions in the original test case
    directory.  Unfortunately, when we generated the distribution
    archive, we didn't properly delete all SED-ML files before
    regenerating them, and so some old SED-ML files were left from the
    time before the Level 3 models were split out as new cases.  This
    problem was reported by Ilya Kiselev from DevelopmentOnTheEdge.com.


Version 2.1.0 release (2012-08-06)
---------------------------------------

* Bug fixes:

  - Models 00056, 00112, 00288, 00293, and 00294 had reactions with
    kinetic laws that went negative, but which were flagged
    `reversible=false`.  They are now correctly flagged as
    `reversible=true`.

  - Several L1 versions of tests with algebraic rules were ambiguous
    due to L1 not having the `constant` flag.  These models have been
    removed.

* Changes since last update:

  - Several new models tagged ReversibleReaction where a kinetic law
    now goes negative.

  - Tests with assigned stoichiometries have been split into two tests:
    one for the L2 versions using the StoichiometryMath construct,
    and one for the L3 version using rules and events to assign values
    to the speciesReference id.

  - The definitions of several tags have been tweaked to be clearer
    and more useful.  See docs\src\tags-documentation\Tags.txt for
    the new definitions.

  - As part of the above, many test cases have been re-tagged to
    reflect the new definitions.

  - Tests that assigned stoichiometries to speciesReference elements
    have been split into L2 versions (using the StoichiometryMath
    construct) and L3 versions (which use the speciesReference id in
    rules and events to change or set the stoichiometry).

  - A new program, `generateTestsFrom` was written which takes an SBML
    model as input and outputs the different translations of that model,
    a generic `settings` file, and a model description file with what
    tags it can deduce from the SBML model.  See src\utilities\c++\

  - A new program, `checkTestCases` was written which analyzes the
    contents of a test case directory and determines whether the tags
    are correct, the models are valid and present, and the settings
    file is of the right basic format.  See src\utilities\c++\

  - We changed some cases' tolerances, because some developers
    reported the cases had excessively stringent tolerances.

* New cases:

  - New cases with the AssignedVariableStoichiometry tag (both L2
    and L3 versions)

  - New cases with the FastReaction tag

  - New cases with delay equations with variable delays.

  - New cases with both the HasOnlySubstanceUnits and
    NonUnityCompartment tags.

  - New cases with the ReversibleReaction tag where the kinetic law
    goes negative during the requested simulation (as per the new
    definition of the ReversibleReaction tag).

  - One new `kitchen sink` test (1000) which tests almost all tags
    in one complicated model.

Note: the syntax of some gnuplot commands changed from version 4.4
to 4.6, and the script in src/utilities/plotresults/plotresults.sh
will currently not work in gnuplot 4.6.  Make sure to use 4.4 if you
attempt to recreate the plots using the scripts provided in the
SBML Test Suite source directory.


Version 2.0.2 release (2011-11-15) 
---------------------------------------

* Bug fixes:

  - The settings files for cases 536 and 537 had the order of
    the variables listed in the "variables:" field reversed compared
    to the order in which the variables actually appeared in the .csv
    results file.  Fixed.

  - Case 00939 had incorrect results.


Version 2.0.1 release (2011-06-16) 
---------------------------------------

* Bug fixes since last update:

  - Frank fixed an issue in the generation of identifiers in SED-ML
    files wherein it could generate the same id for different
    elements.  The problem was not picked up by the Schema validator,
    so he created a generator that has specific treatment for this
    situation.

* Changes since last update:

  - Added plots of results in SVG format.


Version 2.0.0 release (2011-06-01) 
---------------------------------------

This is a release to introduce the SBML Level 3 Version 1 Core test
cases, the SED-ML files, and the updated Online SBML Test Suite.

* Bug fixes since last update:

  - The online interface had a number of bugs left having to do with
    management of multiple results, catching nulls, etc.  Hopefully
    these have all been fixed.


Update (2011-05-26) 
---------------------------------------

* New features:

  - Now includes SED-ML files for the test cases, and a utility
    program to generate them from the case settings files.

  - The online interface now supports L3v1 Core, plus has an improved
    user interface for the case selection/exclusion form.  (E.g.,
    there is now tooltip help text to explain the meaning of the
    different options that can be checked.)


Update of test cases (2010-10-06) 
---------------------------------------

  - All L3 models have been updated to comply with the final
    release of the SBML L3V1 core specification.
    
  - There are examples of models that use the new Event semantics.


Update of test cases (2010-04-20) 
---------------------------------------

* New features

  - Cases where an initial value is reassigned by math within the
    model have been tagged with an `InitialValueReassigned` tag.

  - New cases that involve just a non-varying parameter whose value
    is set by initialAssignment or assignmentRule have been added.
  
  - New cases that involve only a compartment that is varying have
    been added.
    
  - All models that can be encoded in SBML L3V1 core (in accordance
    with the Release 1 Candidate specification) have an L3 sbml file.
    
* Bug fixes

  - Description of case 00875 incorrectly stated that A2 was a 
    boundary species when it is actually A1.  Thanks to Chris
    Myers for reporting this.

  - Tracker issue #2961844.
    The data for case 00873 was inaccurate.  The data now reflects
    results verified with VCell and iBioSim.

  - Tracker issue #2961843.
    The data for case 00874 was inaccurate.  The data now reflects
    results verified with VCell and iBioSim.

  - Tracker issue #2941625.
    The data for this case raised concerns.  Testing with Copasi,
    RoadRunner and iBioSim showed the results to be slightly
    inaccurate.  These have been changed.  Thanks to Chris Myers
    for reporting this.

  - Tracker issue #2941621.
    The data for case 00873 was inaccurate.  The data now reflects
    results verified with VCell and iBioSim.

  - Tracker issue #2941618.
    The order of data did not match the order in the sbml files.
    This has been fixed.  Thanks to Chris Myers for reporting this.	       

Version 2.0.0 alpha 4 (test-cases only release 2010-01-17) 
---------------------------------------

* Corrections to disputed results involving event test cases.

* Corrections to misordering of data in results file for some cases
  involving algebraic rules.  (Actual results were correct, but the
  data and labels were misordered.)

* Minor changes to SVN organization of test files and corresponding
  changes to the Makefile; however, the changes do not come through in
  the test cases zip archive.


Version 2.0.0 alpha 3 (limited release 2008-08-27) 
---------------------------------------

* Fixed a problem in the Standalone Application in unpackaging the
  archive of test cases.


Version 2.0.0 alpha 1 (limited release 2008-08-22) 
---------------------------------------

* We redesigned and reimplemented the SBML Test Suite from the ground
  up.  The new system features a very large corpus of 900 carefully
  hand-crafted models, a new standalone test runner, and a web-based
  test runner available at http://sbml.org/Facilities/.

  All test cases have component tags indicating which SBML components
  are present in the model.  Basic models have either Compartment,
  Species and Reaction or Parameter and RateRule.  Additional
  complexity is added to the models by including other components:
  FunctionDefinition, InitialAssignment, AssignmentRule,
  AlgebraicRule, RateRule (in cases where Reactions are used),
  EventNoDelay and EventWithDelay.  At the time of this release, the
  test suite contains models with the basic components and up to two
  further components, with approximately 100 cases for each component.
  More complexity can be introduced with the addition of further
  components within a given model.

  At present UnitDefinition and Constraint are not supported.
  
  Each test case lists the test tags related to the SBML feature being
  tested.  Again, these start with the basic cases where the test tag
  is InitialAmount (in cases where there are Reactions) and
  NonConstantParameter (in rules based models).  Additional tags are
  added to increase the complexity and in some cases test the
  interaction between different features.  At present there are up to
  4 test tags on models within the Test Suite.
  
  All the test tags in the Tags.txt file have some coverage (an
  average of 50 cases per tag) WITH THE EXCEPTION of CSymbolDelay,
  Units, MassUnits.  Some tags have only a few related test cases eg
  FastReaction, MultiCompartment; this is mainly due to the lack of
  simulators supporting these and thus difficulty in generating and
  verifying test data.
  
  The data has in most cases been generated by MathSBML and verified
  with SBMLToolbox.  VCell and SBToolbox2 have been used for a few
  cases.  7% of cases have analytical solutions derived using the
  Maple symbolic engine via MATLAB.
  


######################################################################
# notice ## notice ## notice ## notice ## notice ## notice ## notice #
######################################################################
#                                                                    #
#  The rest of this file pertains to a much older and different      #
#  version of the test suite, previously called the SBML Semantic    #
#  Test Suite.  The information is kept here for historical purposes #
#  and is largely irrelevant to the current SBML Test Suite.         #
#                                                                    #
######################################################################
# notice ## notice ## notice ## notice ## notice ## notice ## notice #
######################################################################


Version 1.4 (2007-06-24, never released publicly) 
---------------------------------------

* Merged the previous test suite (which in reality consisted of
  nothing more than sample models) with a slightly reorganized version
  of the "SBML Semantic Test Suite", with the intention of renaming
  the semantic test suit as the SBML Test Suite and redesigning
  everything for release 3.0.  The reorganization and additional
  CVS-related work was done by Sarah Keating and Michael Hucka.

  A beta test version of the "SBML Semantic Test Suite" in the days of
  SBML Level 2 Version 1 was written principally by Andrew Finney,
  (then at the University of Hertfordshire, UK) and released in
  October 2004.  The suite consisted of a set of SBML models each with
  correct simulation output.  The test suite came with automation
  scripts allowing a simulator to be systematically tested against the
  test suite.  The automation scripts were designed to be portable and
  run on Windows, Unix, and Unix-like platforms.  In its beta version,
  the test suite was reasonably comprehensive covering most SBML Level
  2 Version 1 constructs: all the MathML functions and operators, all
  types of rules, all possible configurations of reactions and
  different configurations of local and global parameters.
  
  A substantial reorganization and reconstruction was begun in 2007 as
  part of NIH/NIGMS grant #GM077671.  We expect virtually everything in
  the suite to be changed and improved.
  
* The contents of the previous directory "sample-models" have been
  moved to "extras/sample-models".

* "extras/sample-models" now also contains samples from SBML Level 2
  Versions 2 and 3.


Version 1.3 
---------------------------------------

Released 7 September 2004.

* If you are getting this via CVS, make sure to use the -P (prune)
  option to the cvs update command, so that you don't get delete
  directories and files.  The current release contains only one
  subdirectory, "sample-models".

* We now have CVS commit auto-notification via email.  Visit the page
  https://lists.sourceforge.net/lists/listinfo/sbml-cvs-tstsuite to
  subscribe to notifications.  This is a very good idea if you get
  this test suite via CVS.

* Removed the SBML and MathML schema files from the distribution.
  Interested users should obtain them from the master locations.
  Keeping a separate copy in this test suite was a maintenance
  liability, because they would surely fall out of date eventually.

* Reinstated the SBML Level 1 Version 1 files except for the files
  converted from KEGG (i.e., the directory sbml-l1v1/from-kegg).
  After some discussions, we concluded that there may still be some
  value in providing sample models in L1v1 format for developers who
  need to test their software's backward compatibility.


Version 1.2 
---------------------------------------

* We have removed the Level 1 Version 1 files from this set, because
  L1v1 is officially deprecated.  Users are encouraged to examine L1v2
  or better yet, Level 2.


Version 1.1 
---------------------------------------

* The KEGG translated files are no longer included with the test-suite
  distribution.  They can be obtained separately from the following site:

  http://systems-biology.org/001/


Version 1.0.1 
---------------------------------------

* The following SBML Level 2 version 1 errata have been corrected in
  sbml-files/sbml-l2v1/from-spec:

    - l2v1-2D-compartments.xml: the "JO" in the MathML should really
      be a "J0" (the numeral zero, no the letter `oh`).

    - l2v1-boundary.xml: the one rule in listOfRules should not use
      <apply> ... </apply>; these tags should be omitted. Thanks to
      Bruce Shapiro for catching this.

    - l2v1-delay.xml: The definitionURL for the csymbol delay should
      be http://www.sbml.org/sbml/symbols/delay, not
      http://www.sbml.org/symbols/delay (the incorrect form has "sbml"
      omitted). Thanks to Marc Vass for catching this.

    - l2v1-mc-ode.xml: the MathML in the two rateRule definitions
      should not use <apply> ... </apply>; these tags should be
      omitted. Thanks to Bruce Shapiro for catching this.

* Released  7 August 2003


Version 1.0.0 
---------------------------------------

* Initial release, 29 June 2003.
