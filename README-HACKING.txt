<center>

Notes about the SBML Test Suite codebase
========================================
  *Michael Hucka*                          <br>
  *California Institute of Technology*     <br>
  *September 2011*

</center>

The following is a short explanation of some of the aspects of the
SBML Test Suite code as it stands at the time of this writing.  The
topics are divided into separate sections below.


The top-level Makefile
----------------------

The top-level Makefile uses GNU Makefile syntax and scripts a number
of operations.  Executing "make _something_" (where the possible
"_something_" targets are explained below) will perform the operation
in the relevant subdirectory.

  * _Generation of HTML files for test case descriptions_

    In the SVN repository, we don't store the HTML files in the cases/
    subdirectories.  Instead, they are generated as needed, for
    example the time when someone generates a distribution archive.
    The reason for using this approach is that the case descriptions,
    tags, and in some cases, a script to generate the SBML code, are
    all stored in a single common file named "XXXXX-model.m" in the
    cases/semantic/XXXXX directories.  An advantage of this is that it
    keeps the descriptions and other information together and reduces
    the chances that they will diverge (e.g., by someone forgetting to
    edit one file after making changes to another).  The .m file is
    processed to produce an HTML version.  In the top-level Makefile,
    the target "make html" will regenerate the HTML files for those
    cases whose .m files are more recent than the corresponding .html
    file.  This target is also executed automatically when "make
    cases-dist" is executed.

  * _Generation of plot files for test case results_
  
    Similar to the approach for the HTML files containing descriptions
    of the models, the plots of the test results are not stored in the
    SVN directory because they can be generated from the .csv data
    files.  The process for this involves using the script
    src/utilities/plotresults/plotresults.py.  In the top-level
    Makefile, the target "make plots" will regenerate HTML and JPEG
    plot files for those cases whose .csv files are more files are
    more recent than the corresponding HTML and JPEG plot files.  This
    target is also executed automatically when "make cases-dist" is
    executed.

  * _Generation of SED-ML files_

    Similar to the approach for the HTML files, the SED-ML files are
    also not stored in the SVN repository, and instead are generated
    by the top-level Makefile using the target "make sedml".  This
    target is also executed automatically when "make cases-dist" is
    executed.

  * _Generation of test case archives/distributions_

    The command "make cases-dist" will pull together all the cases
    files, plus additional files, into a zip file suitable for
    distribution on SourceForge.  Besides the case archives, some of
    the other files includes the NEWS.txt file and the cases map file,
    explained in a separate section below.

  * _Updating the list of files ignored by SVN_

    SVN has a stupid way of handling files that are ignored from
    check-ins: there is no option for creating a local file that
    determines what's ignored, and instead, the ignored files must be
    listed on specific property of the SVN directory containing them.
    To make it possible to easily set up the right properties when a
    new case is added under cases/semantic/, the top-level Makefile
    provides the target "svn-ignores".  Run "make svn-ignores"
    whenever you add a new cases directory in cases/semantic/.



The different subdirectories
----------------------------

There are three top-level subdirectories: 

  * _cases_: this contains all the case files.

  * _docs_: this contains documentation about the Test Suite.
    (The contents are currently incomplete.)

  * _src_: this contains all the source files.

The first two are easy enough to understand by inspection, but the
_src_ directory has quite a lot underneath it and merits more
explanation:

  * _front-ends_: there are different front ends for the Test Suite.
    This directory contains the sources for a stand-alone runner and
    the web interface.  (The latter is the system served at
    http://sbml.org/Facilities/Online_SBML_Test_Suite .)

  * _imported_: open-source software from other projects that we use
    in the Test Suite code.  We keep a local snapshot to make sure we
    have a stable, known version.  The _imported_ directory contains
    full distributions of the original files, so that it is easy to
    trace where they came from and determine the original authors to
    whom credit is due.

  * _graphics-originals_: source files to icons and other graphics.

  * _simulation_: scripts used to generate SBML files for the test
    cases.

  * _testing_: files used to test the Test Suite.

  * _utilities_: utilities used for the Test Suite.  These include
    such things as the scripts used to generate the HTML files and the
    plot image files.



The "cases tags map" file
-------------------------

Doing a validation run requires selecting options such as which
Level/Version combination of SBML should be used, which specific
characteristics should be tested, and so on.  This information can be
determined by reading the test case descriptions (the "XXXXX-model.m"
files), but doing so for every validation run becomes time-consuming
when there are a thousand description files.  Some form of caching is
obviously called for.

The scheme used currently in the Test Suite is simple and works for
the both the online and the standalone versions.  It involves
precomputing some properties of the test suite and storing the results
in a plain-text file bundled with the test suite archive.  This file
is called ".cases-tags-map".  It is generated by the top-level
Makefile when case archives are created.  The format of the file is
currently the following:

* The first line is a list of all component tags and test tags
  encountered in all the Test Suite cases files.  The tag names are
  separated by whitespace.

* The next and subsequent lines consist of, first, a five-digit case
  number, followed by a list of all the tags present in that case.
  The list of tags consists of not only the component and test tags,
  but also the SBML Level/Version combinations (expressed in the form
  "X.Y") for which the case is available.  That last part may seem
  strange, but it turns out that the Level/Version names expressed as
  "X.Y" are distinct from all component and test tag names, which
  makes it possible to manipulate all of these aspects of a case using
  the same tags code.

The cases tags map is read by the Test Suite code and used to
construct a vector indexed by case numbers.  Various methods are
available to test cases and search the data in the Java code of the
Test Suite.  The file can also be parsed quickly enough that it can be
scanned every time the Test Suite is invoked (e.g., by the web
interface code).

Finally, the simple text file format of the ".cases-tags-map" file
makes it also possible for humans to use Unix tools like grep and
other features for quick command-line explorations of the test cases
in the Test Suite.



Colophon
--------

This text file uses Markdown syntax and can be converted to HTML using
any of a number of Markdown processors.  The target "make readme-html"
in the top-level Makefile will do this.






<!-- -----------------------------------------------------------------
The following is for [X]Emacs users.  Please leave in place.
Local Variables:
fill-column: 70
End:
-->
