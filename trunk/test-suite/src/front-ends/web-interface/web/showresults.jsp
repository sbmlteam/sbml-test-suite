<%-- 
 * @file    showresults.jsp
 * @brief   Display a summary map of the user's test results
 * @author  Kimberly Begley
 * @date    Created Jul 30, 2008, 9:25:21 AM
 *
 * ----------------------------------------------------------------------------
 * This file is part of the SBML Test Suite.  Please visit http://sbml.org for
 * more information about SBML, and the latest version of the SBML Test Suite.
 *
 * Copyright (C) 2010-2015 jointly by the following organizations: 
 *     1. California Institute of Technology, Pasadena, CA, USA
 *     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
 *     3. University of Heidelberg, Heidelberg, Germany
 *
 * Copyright (C) 2008-2009 California Institute of Technology (USA).
 *
 * Copyright (C) 2004-2007 jointly by the following organizations:
 *     1. California Institute of Technology (USA) and
 *     2. University of Hertfordshire (UK).
 * 
 * The SBML Test Suite is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation.  A copy of the license
 * agreement is provided in the file named "LICENSE.txt" included with
 * this software distribution and also available on the Web at
 * http://sbml.org/Software/SBML_Test_Suite/License
 * ----------------------------------------------------------------------------
--%>

<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>

<%@ page import="sbml.test.*" %>

<%@ page errorPage="/web/error.jsp" %>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<%
// Start by logging that we've been invoked.

OnlineSTS.init();
OnlineSTS.logInvocation(request);

// Check that this session hasn't timed out.

if (session == null)
{
    OnlineSTS.logError(request, "Null sessionResults; assuming timeout.");

    // Can't rely on OnlineSTS being able to pull values out of the
    // request, so we have to go really low-level here.

    String rootURL  = request.getScheme() + "://"
        + request.getServerName() + ":" + request.getServerPort()
        + request.getContextPath();

    String imageURL = rootURL + "/web/images";

    String homeURL  = "http://sbml.org/Software/SBML_Test_Suite";
%>

   <div id='pagetitle'><h1 class='pagetitle'><font color="darkred">
        SBML Test Suite session error</font></h1></div><!-- id='pagetitle' -->
        <div style="float: right; margin: 0 0 1em 2em; padding: 0 0 0 5px">
        <img src="<%=imageURL%>/Icon-online-test-suite-64px.jpg">
    </div>

    <p>
    We regret that your session has expired.  For performance reasons, the
    duration of sessions is limited to <%= session.getMaxInactiveInterval()/60 %>
    minutes.  Please re-upload your results and proceed.

    <p>	
    <center>
        <a href="<%=homeURL%>">
        <img border="0" align="center" src="<%=imageURL%>/Icon-red-left-arrow.jpg">
        Return to the Online SBML Test Suite front page.
        </a>
    </center>
    </p>
<%
    return;
}
%>

<div id='pagetitle'><h1 class='pagetitle'>Outcome of tests</h1>
</div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="<%=OnlineSTS.getImageURL(request)%>/Icon-online-test-suite-64px.jpg" border="0">
</div>

<%
// Get some basic values that we'll need soon.

String baseURL = OnlineSTS.getServiceRootURL(request) + "/web";

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
<table id="resultsmap" class="borderless-table">
<tr>

<%
//
// 1. Extract the results passed to us by UploadUnzipText.  The TreeMap will
// have an entry for every known test case, with either a value object (of
// class UserTestResult) or a null.  If it's null, it means that the user's
// uploaded results didn't include values for that particular case.

TreeMap<Integer, UserTestResult> results
    = (TreeMap<Integer, UserTestResult>) request.getAttribute("testResults");

if (results == null)
{
    OnlineSTS.logError(request, "Null testResults.");
}

//
// 2. Store the results in a per-invocation variable in this session.  Later
// below, the invocation identifier is handed to calls to testdetails.jsp.
// This makes it possible for users to keep multiple results up in separate
// browser windows and still have testdetails.jsp get the results from the
// right invocation.

HashMap sessionResults = (HashMap) session.getAttribute("sessionResults");
if (sessionResults == null)
{
    sessionResults = new HashMap();
    session.setAttribute("sessionResults", sessionResults);
}

Long uuid = Math.abs(UUID.randomUUID().getLeastSignificantBits());
String resultsID = uuid.toString();

OnlineSTS.logInfo(request, "This resultsID = " + resultsID);

HashMap testResultsMap = new HashMap();
testResultsMap.put("resultsID"  , resultsID);
testResultsMap.put("testResults", results);

sessionResults.put(resultsID, testResultsMap);

//
// 3. Produce a summary map of the results, with pointers to the details.
// Along the way, compute some general statistics about the results.
//

Map<String, Integer> tmap = new HashMap<String, Integer>();
Map<String, Integer> cmap = new HashMap<String, Integer>();

int countTotal        = 0;
int countMissing      = 0;
int countFailed       = 0;
int countPassed       = 0;
int countProblems     = 0;

DecimalFormat caseNumFormatter = new DecimalFormat("00000");

// The test results map will contain an entry for every known test case,
// and if the user's results set didn't include a particular case, then
// the entry's value in this map will be null.

for (Map.Entry<Integer, UserTestResult> result : results.entrySet())
{
    countTotal++;

    int caseNum = result.getKey();
    String name = caseNumFormatter.format(caseNum);
    String color;

    out.print("<td title=\"Test Case #" + name + "\">");
    out.print("<a href=\"" + baseURL + "/testdetails.jsp?testName=" + name
              + "&resultsID=" + resultsID
              + "\" target=\"_blank\">");

    UserTestResult thisResult = result.getValue();
    if (thisResult != null)
    {
        UserTestCase theCase = thisResult.getUserTestCase();

        if (thisResult.hasError())
        {
            countProblems++;
            color = "black";
        }
        else if (thisResult.getNumDifferences() > 0)
        {
            countFailed++;
	    for (String tag : (Vector<String>) theCase.getTestTags())
		tmap.put(tag, (tmap.containsKey(tag) ? tmap.get(tag) + 1 : 1));
	    for (String tag : (Vector<String>) theCase.getComponentTags())
		cmap.put(tag, (cmap.containsKey(tag) ? cmap.get(tag) + 1 : 1));
            color = "red";
        }
        else
        {
            countPassed++;
            color = "green";
        }
    }
    else // There wasn't a returned case with this index number.
    {
        color = "gray";
        countMissing++;
    }
    out.print("<img src=\"" + baseURL + "/images/" + color + ".jpg\"/>");
    out.print("</a>");
    out.print("</td>");

    if (countTotal % 45 == 0)           // Start a new row in the HTML table.
    {
        out.print("</TR>");
        out.print("<TR>");
    }

} // end of for loop

OnlineSTS.logInfo(request, "Results: " + countPassed + " passed, "
                  + countFailed + " failed, " + countProblems + " problems, "
                  + countMissing + " missing.");

// Store various parts of the results into the session variable so that
// testdetails.jsp and report.jsp can pull them out.

testResultsMap.put("countPassed"  , countPassed);
testResultsMap.put("countFailed"  , countFailed);
testResultsMap.put("countProblems", countProblems);
testResultsMap.put("countMissing" , countMissing);

File casesRootDir = new File(getServletContext().getRealPath("/test-cases"));
testResultsMap.put("casesRootDir" , casesRootDir);

testResultsMap.put("timeOfRun"    , timeOfRun);

//
// 3. Follow this with a text summary of the results.  (In HTML, below.)
//
%>	

</tr>
</table> 

<p style="margin-top: 2em">
Total number of test cases analyzed: <b><%=countTotal%></b>
</p>
<p>
<img src="<%=baseURL%>/images/green.jpg" valign="top"/>
Number of test cases <b><font color="green">passed</font></b>: <%= countPassed %><br>

<img src="<%=baseURL%>/images/red.jpg" valign="top"/>
Number of test cases <b><font color="darkred">failed</font></b>: <%= countFailed %><br>

<img src="<%=baseURL%>/images/black.jpg" valign="top"/>
Number of test cases with <b><font color="black">problems</font></b>: <%= countProblems %><br>

<img src="<%=baseURL%>/images/gray.jpg" valign="top"/>
Number of test cases <b><font color="gray">not included</font></b> in the uploaded results: <%= countMissing %><br>

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
           style="border-top: 1px solid #bbb; border-bottom: 1px solid #bbb;">
      <tr>
        <th valign="bottom">Test tag</th>
        <th width="150px" valign="bottom">Number of failed<br>cases with this tag</th>
      </tr>
    
<%
    for (String key : (Set<String>) tmap.keySet())
        out.println("<tr><td>" + key + "</td><td>" + tmap.get(key) + "</td></tr>");
%>

  <tr>
    <th valign="bottom">Component tag</th>
    <th width="150px" valign="bottom">Number of failed<br>cases with this tag</th>
  </tr>

<%
    for (String key : (Set<String>) cmap.keySet())
        out.println("<tr><td>" + key + "</td><td>" + cmap.get(key) + "</td></tr>");

%>
</table>
<%
} // end of if (countFailed > 0)
%>

<p style="margin-top: 1em">
<a href="<%=baseURL%>/report.jsp?resultsID=<%=resultsID%>" target="_blank" style="margin-right: 0.5em">
<img align="center" src="<%=OnlineSTS.getImageURL(request)%>/Icon-specifications-64px.jpg">
View Report</a>
<i>(The report summarizes the results in a more convenient format for printing.)</i>

</div>

<hr>

<p style="margin-top: 1em"> This concludes the test run.  To upload
another set of results without selecting and downloading a different set of
test cases, please return to the upload page, select another zip archive on
your computer, and upload it.
<p>

<center style="margin: 1em">
  <a href="<%=baseURL%>/uploadresults.jsp">
    <img align="center" src="<%=OnlineSTS.getImageURL(request)%>/Icon-red-left-arrow.jpg">
    Return to the test results upload page.
  </a>
</center>

<p> To start over afresh, please return to the front page of the Online SBML
Test Suite.
</p>

<center style="margin: 1em">
  <a href="http://sbml.org/Facilities/Online_SBML_Test_Suite">
    <img align="center" src="<%=OnlineSTS.getImageURL(request)%>/Icon-red-left-arrow.jpg">
    Return to the front page for the Online SBML Test Suite.
  </a>
</center>

<%@ include file="sbml-bottom.html"%>
