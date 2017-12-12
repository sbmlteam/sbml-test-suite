                     Some explanations about the code
                       of the Online SBML Test Suite
                               Michael Hucka
                                2011-09-11


General code organization
-------------------------

The Online SBML Test Suite is a JSP application.  There are two main parts
to the code: the JSP interface files and Java code that implements the core
functions.  The subdirectories are as follows:

  web/                      -- subdirectory containing the JSP files
  WEB-INF/src/sbml/test/    -- subdirectory containing the Java sources

Once installed in the Tomcat webapps directory, the test cases are put into
a 3rd important directory alongside the others:

  test-cases                -- subdirectory containing the test cases


General code logic
------------------

To understand the JSP and Java code, it may be easiest to start with the
JSP side and follow the line of execution there, and then to examine the
method performAnalysis() in "UploadUnzipTest.java" and the entire classes
"TestCase" and "CaseSummaryVector" in the Java directory.  (The JSP files
admittedly weren't written in the clearest way; for example, they don't
just provide a presentation layer -- they do computation too, and that
should probably have all been moved to the Java files.  However, they are
not terribly long or complicated, so figuring them out, even in their
suboptimal form, will not take forever.)

Here's a summary of the overall sequence of events.

  1. The user visits "web/selecttests.jsp".  This file implements the logic
     of selecting the subset of test cases that are downloaded to the user,
     so that the user can run them off-line in their application.  Here are
     the overall performed by this file:

     a. It reads all the cases from the "test-cases" subdirectory and
        builds an internal vector of information about them.  (This is the
        CaseSummaryVector class object.)

     b. It builds a vector of known tags that are found in the test cases.

     c. It constructs an array of bit masks indexed by case number.  For
        each case, the bits in the bit mask indicate which tags the case
        contains.

     d. It creates an HTML form and displays it.  The form uses Javascript
        logic to implement graying out of options and other features based
        on options selected by the user.  The form sets values in session
        variables "ctags", "ttags", and "levelAndVersion".

     e. When the user clicks on the "submit" button, the HTML form action
        invokes "web/process-selections.jsp".

  2. "web/process-selections.jsp" processes the selections made by the user
     in "selecttests.jsp" to create a vector of the specific cases to
     package up and download.

     a. "process-selections.jsp" begins by reading the session variables
        "ctags", "ttags", and "levelAndVersion".

     b. It creates a CaseSummaryVector object containing all possible test
        cases, then uses methods defined on CaseSummaryVector to
        selectively remove those cases that (i) don't match the selected
        Level+Version of SBML and (ii) contain tags that the user requested
        be omitted.

     c. It creates a vector of strings called "casesToReturn", consisting
        of the numbers (converted to strings) of the test cases to return.

     d. It puts "casesToReturn" and some other data into the session, and
        invokes the Java program ZipServlet.  The code to that is found in 
        WEB-INF/src/sbml/test/ZipServlet.java.

  3. ZipServelet uses the values in "casesToReturn" to create a zip file
     containing the test case files that the user selected, and it starts a
     download in the user's browser.

  4. The user presumably runs the test cases somehow, offline, and creates
     a new archive containing the results of their run as a collection of
     .csv files.

  5. The user visits "web/uploadresults.jsp".  This file really only acts
     as an interface for providing an upload button and invoking the Java
     program in UploadUnzipTest.  The code to the latter is found in 
     WEB-INF/src/sbml/test/UploadUnzipTest.java.

  6. UploadUnzipTest executes.  This implements the comparison between the
     user's results and the reference values in the Test Suite.

     a. It creates a temporary directory on the server and upacks the
        user's zip archive.

     b. For every case included by the user, it compares the results data
        to the reference data.  The reference data are stored in objects of
        class TestCase, and the user's results are stored in a class of
        object derived from TestCase, called UserTestCase.

     c. It stores the results of the comparisons along with some overall
        data in a vector of objects of class UserTestResult.

     d. It sets the session variable "testResults" to the vector of
        UserTestResult objects and dispatches "web/showresults.jsp".

  7. The file "web/showresults.jsp" is automatically displayed after
     UploadUnzipTest ends, and displays the summary of the results to the
     user.


Selected information about the Java code in WEB-INF/src/sbml/test
-----------------------------------------------------------------

Information about the test cases available in the suite is cached at the
time the test case archive is created.  The info is stored in a file called
".cases-tags-map".  This file is read by the class CaseSummaryVector.

The class CaseSummaryVector is a vector of CaseSummary class objects.  In
other words, when CaseSummaryVector is instantiated, all the usual Java
Vector class operations apply to it.

A CaseSummary object is a simple object that gathers several pieces of
information about a test case.  It stores the number, a "name" (really the
number as a string), the SBML Level and Version, and a tag bit mask.  It
does not store the actual test case data, such as the numerical data,
because in the contexts where it's used, this is unnecessary.  Separate
object classes (TestCase and UserTestCase) are used to store the actual
test case data.

Tag bit masks are represented by objects of class TagBits.  This is an
extension of the Java class BitSet.

The comparison of reference data versus user results data, for a single
case, is stored in memory as an object of class UserTestResult.  Among
other things, this class stores an array of differences in the values
between the reference and user data.  The class implements the Java
Comparable interface, so that vectors of these objects can be sorted.

There is a significant amount of logging done all over the place so that
the server logs (in the "catalina.out" file, wherever it's put by the
server) contains run-time information down to a pretty detailed level.
This makes it possible to watch for possible problems in the system after
it's put in the production environment.
