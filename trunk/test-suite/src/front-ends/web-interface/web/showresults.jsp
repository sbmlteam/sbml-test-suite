<%-- 
 * @file    showresults.jsp
 * @brief   Display a summary map of the user's test results
 * @author  Kimberly Begley
 * @date    Created Jul 30, 2008, 9:25:21 AM
 *
 * $Id$
 * $HeadURL$
 * ----------------------------------------------------------------------------
 * This file is part of the SBML Test Suite.  Please visit http://sbml.org for 
 * more information about SBML, and the latest version of the SBML Test Suite.
 *
 * Copyright 2008      California Institute of Technology.
 * Copyright 2004-2007 California Institute of Technology (USA) and
 *                     University of Hertfordshire (UK).
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.  A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available at http://sbml.org/Software/SBML_Test_Suite/License
 * ----------------------------------------------------------------------------
--%>

<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="sbml.test.TestResultDetails" %>
<%@ page import="sbml.test.CasesTagsMap" %>

<%@ page errorPage="/web/error.jsp" %>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<div id='pagetitle'><h1 class='pagetitle'>Outcome of tests</h1>
</div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="http://sbml.org/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>

<%!

String baseURL = "http://sbml.org:8080/test_suite/web/";

// The time stamp acts as a simple way to differentiate different runs.
// We also store this in the session variables later on.

SimpleDateFormat sdf = new SimpleDateFormat("dd MMM. yyyy 'at' KK:mm a (z)");
String timeOfRun = sdf.format(new Date());

%>

<p> The archive of test results was successfully uploaded and analyzed on
<%= timeOfRun.toCharArray() %>.
</p>

<p> The following map shows the results of comparing each uploaded data
file to the expected data for that test case.  Each case is represented by
a colored icon: <b><font color="green">green</font></b> to indicate
success, <b><font color="darkred">red</font></b> to indicate failure,
<b><font color="black">black</font></b> to indicate a problem with that
case, and <b><font color="gray">gray</font></b> to indicate the case was
not included in the uploaded results.
</p>

<p> You can hover your mouse over an icon to find out its test case number.
To get more information about a specific case, including a plot of the
expected results, click on the icon&mdash;the information will be presented
in a new window.  </p>
	
<div style="margin: 1em 3em 2em 3em">
<form name="report" action="<%=baseURL%>report.jsp" method="post">
<table id="resultsmap" class="borderless-table">
<tr>

<%
//
// First, find out how many total test cases there exist.
//

// FIXME: try to cache cases tags map

File casesRootDir    = new File(getServletContext().getRealPath("/test-cases"));
CasesTagsMap caseMap = new CasesTagsMap(casesRootDir);
int highestNumber    = caseMap.getHighestCaseNumber();

//
// Loop through the entire set of possible case numbers, and
// if it's a case in the set uploaded by the user, get the results.
//

Vector results          = (Vector) request.getAttribute("tests");
Vector<String> failures = new Vector<String>();
Vector<String> problems = new Vector<String>();

Map<String, Integer> tmap = new HashMap<String, Integer>();
Map<String, Integer> cmap = new HashMap<String, Integer>();

int countFailed   = 0;
int countPassed   = 0;
int countProblems = 0;
int countMissing  = 0;
int totalpoints   = 0;

DecimalFormat caseNumFormatter = new DecimalFormat("00000");

// To make the iteration easier, we make a vector of objects which are
// indexed by case numbers.  If the user's uploaded set doesn't include
// a particular case result, the corresponding entry in the vector is null.

Vector cases = new Vector();
cases.setSize(highestNumber + 1);       // +1 because of the unused 0th elem.

for (int i = 0; i < results.size(); i++)
{
    TestResultDetails test = (TestResultDetails) results.elementAt(i);
    cases.insertElementAt(test, Integer.parseInt(test.getTestname()));
}

// Now go to it.

for (int caseNum = 1; caseNum <= highestNumber; caseNum++)
{
    if (cases.get(caseNum) != null)
    {
        TestResultDetails test = (TestResultDetails) cases.get(caseNum);
        int result = test.getResult();
        String name = test.getTestname();
        String warnings = test.getWarnings();
        totalpoints = test.getTotalpoints();
	
        out.print("<td>");
        out.print("<a title=\"Test case " + name + "\" "
                  + " href=\"" + baseURL + "testdetails.jsp?testname=" + name
                  + "&result=" + result + "&warnings=" + warnings
                  + "&tpoints=" + totalpoints +"\" target=\"_blank\">");

        switch (result)
        {
        case 0:
            out.print("<img src=\"" + baseURL + "images/green.jpg\"/>");
            countPassed++;
            break;

        case -1:
            out.print("<img src=\"" + baseURL + "images/black.jpg\"/>");
            countProblems++;
            problems.addElement(name + "?" + warnings);
            break;

        default:                        // result > 0 is count of failed points
	    for (String tag : (Vector<String>) test.getTtags())
            {
		tmap.put(tag, (tmap.containsKey(tag) ? tmap.get(tag) + 1 : 1));
            }
	    for (String tag : (Vector<String>) test.getCtags())
            {
		cmap.put(tag, (cmap.containsKey(tag) ? cmap.get(tag) + 1 : 1));
            }
            out.print("<img src=\"" + baseURL + "images/red.jpg\"/>");
            countFailed++;
            failures.addElement(name + "?" + result + "?" + totalpoints);
        }	

        out.print("</a>");
        out.print("</td>");

    }
    else // There wasn't a returned case with this index number.
    {
        String caseName = caseNumFormatter.format(caseNum);
        out.println("<td><a title=\"Test case " + caseName + "\" "
                    + "href=\"" + baseURL + "testdetails.jsp?testname="
                    + caseName + "\" " + "target=\"_blank\">" 
                    + "<img src=\"" + baseURL + "images/gray.jpg\"/></a></td>");
        countMissing++;
    }

    if (caseNum % 45 == 0)              // Start a new row in the HTML table.
    {
        out.print("</TR>");
        out.print("<TR>");
    }

} // end of for loop

%>	
</tr>
</table> 

<p style="margin-top: 2em">
    Total number of test cases analyzed: <b><%=results.size()%></b>
</p>
<p>
    <img src="<%=baseURL%>images/green.jpg" valign="top"/>
    Number of test cases <b><font color="green">passed</font></b>: <%= countPassed %><br>

    <img src="<%=baseURL%>images/red.jpg" valign="top"/>
    Number of test cases <b><font color="darkred">failed</font></b>: <%= countFailed %><br>

    <img src="<%=baseURL%>images/black.jpg" valign="top"/>
    Number of test cases with <b><font color="black">problems</font></b>: <%= countProblems %><br>

    <img src="<%=baseURL%>images/gray.jpg" valign="top"/>
    Number of test cases <b><font color="gray">missing</font></b> from the uploaded results: <%= countMissing %><br>

<%
if (countFailed > 0)
{
%>	
<br>
<p>
The following is a list of the component and test tags present in the cases
that failed.  Each tag name is listed with the number of cases that contained
that tag.  A pattern in this set may help you identify the problem(s) causing
the test failures.
</p>

<table class="borderless-table alt-row-colors sm-padding" 
       align="center" width="60%" 
       style="border-top: 1px solid #bbb; border-bottom: 1px solid #bbb">
  <tr>
    <th valign="bottom">Test tag</th>
    <th width="150px" valign="bottom">Number of cases<br>with this tag</th>
  </tr>

<%
    for (String key : (Set<String>) tmap.keySet())
        out.println("<tr><td>" + key + "</td><td>" + tmap.get(key) + "</td></tr>");
%>

  <tr>
    <th valign="bottom">Component tag</th>
    <th width="150px" valign="bottom">Number of cases<br>with this tag</th>
  </tr>

<%
    for (String key : (Set<String>) cmap.keySet())
        out.println("<tr><td>" + key + "</td><td>" + cmap.get(key) + "</td></tr>");

} // end of if (countFailed > 0)
%>

</table>

<%
// Store various parts of the results into the session variable
// so that report.jsp can pull them out.

String[] totals = new String[4];
totals[0]       = (String.valueOf(countPassed));
totals[1]       = (String.valueOf(countFailed));
totals[2]       = (String.valueOf(countProblems));
totals[3]       = (String.valueOf(countMissing));

session.setAttribute("totals"       , totals);
session.setAttribute("failures"     , failures);
session.setAttribute("problems"     , problems);
session.setAttribute("timeOfRun"    , timeOfRun);
session.setAttribute("casesRootDir" , casesRootDir);
%>

<br><br>
<p>
<input type="submit" value="View Report">
(The report summarizes the results in a more convenient format for printing.) 
</form>
</div>

<hr>

<p style="margin-top: 1em"> This concludes the test run.  To upload
another set of results without selecting and downloading a different set of
test cases, please return to the upload page, select another zip archive on
your computer, and upload it.
<p>

<center style="margin: 1em">
  <a href="<%=baseURL%>uploadresults.jsp">
    <img align="center" src="http://sbml.org/images/8/83/Icon-red-left-arrow.jpg">
    Return to the test results upload page.
  </a>
</center>

<p> To start over afresh, please return to the front page of the Online SBML
Test Suite.
</p>

<center style="margin: 1em">
  <a href="http://sbml.org/Facilities/Online_SBML_Test_Suite">
    <img align="center" src="http://sbml.org/images/8/83/Icon-red-left-arrow.jpg">
    Return to the front page for the Online SBML Test Suite.
  </a>
</center>

<%@ include file="sbml-bottom.html"%>
