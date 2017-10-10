<%-- 
 * @file    report.jsp
 * @brief   Produce a report page summarizing the user's test results.
 * @author  Michael Hucka
 * @author  Kimberly Begley
 * @date    Created Feb 27, 2008, 9:25:21 AM
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
 *
 * This file writes a summary of the skipped and failed cases during a test
 * suitable for printing.  It gets the session values from the calling
 * page, showresults.jsp
 */
--%>

<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.math.*" %>

<%@ page import="sbml.test.*" %>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<%
// Log that we've been invoked.

OnlineSTS.init();
OnlineSTS.logInvocation(request);

// Start by checking that this session hasn't timed out.

if (session == null || session.getAttribute("sessionResults") == null)
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

// Get the result set data from the session variable.  The session will
// have a hashmap stored in sessionResults, and the data for this result
// set will be stored under key "resultsID".

String resultsID = (String) request.getParameter("resultsID");
if (resultsID == null)
{
    OnlineSTS.logError("Null resultsID");
    return;
}

HashMap sessionResults = (HashMap) session.getAttribute("sessionResults");
HashMap testResultsMap = (HashMap) sessionResults.get(resultsID);
if (testResultsMap == null)
{
    OnlineSTS.logError("Null testResultsMap");
}

OnlineSTS.logInfo(request, "Showing report for resultsID " + resultsID);

TreeMap<Integer, UserTestResult> results
    = (TreeMap<Integer, UserTestResult>) testResultsMap.get("testResults");

int highestCaseNumber = results.lastKey();

int countPassed   = ((Integer) testResultsMap.get("countPassed")).intValue();
int countFailed   = ((Integer) testResultsMap.get("countFailed")).intValue();
int countProblems = ((Integer) testResultsMap.get("countProblems")).intValue();
int countMissing  = ((Integer) testResultsMap.get("countMissing")).intValue();

String timeOfRun  = (String) testResultsMap.get("timeOfRun");
File casesRootDir = (File) testResultsMap.get("casesRootDir");

%>

    <h2>Online SBML Test Suite test report</h2>
    <b>Date and time: <%= timeOfRun.toCharArray() %></b>
    
    <h3 style="width: 100%; border-bottom: 1px solid #ddd; margin-top: 2em">
    Test result summary</h3>
    
    <p>
    Total number of files in the uploaded archive: 
    <%= countPassed + countFailed + countProblems %>
    
    <p>
    Number of <b><font color="green">passed</font></b> cases: <%=countPassed%><br>
    Number of <b><font color="darkred">failed</font></b> cases: <%=countFailed%><br>
    Number of <b><font color="black">problem</font></b> cases: <%=countProblems%><br>
    Number of <b><font color="gray">missing</font></b> cases: <%=countMissing%><br>
    
<%
if (countFailed == 0 && countProblems == 0)
{
%>
    <p> <font color="green"><b>All uploaded test cases passed</b></font>
<%
}

if (countFailed > 0)
{
%>
    <h3 style="width: 100%; border-bottom: 1px solid #ddd; margin-top: 2em">
    Details of failed cases (total: <%=countFailed%>)</h3>

    <p>
    The following table lists the cases in your uploaded archive that
    failed to match the reference results.  Clicking on the case number
    will bring up a separate page providing the details of that particular
    test case.
    
    <table class="borderless-table sm-padding alt-row-colors"
           style="margin-top: 1.5em; border-bottom: 1px solid #999"
           width="90%" align="center">
    <tr style="border-top: 1px solid #999; border-bottom: 1px solid #bbb">
        <th valign="bottom" width="60px">Case #</th>
        <th valign="bottom">Synopsis of what is being tested</th>
        <th valign="bottom" width="100px">Total failed data points</th>
        <th valign="bottom" width="100px">Total data points in test</th>
    </tr>
    
<%
    for (UserTestResult thisResult : results.values())
    {
        if (thisResult != null && thisResult.getNumDifferences() > 0)
        {
            UserTestCase theCase = thisResult.getUserTestCase();
            String name          = theCase.getCaseName();
            int numRows          = theCase.getTestNumRows();
            int numVars          = theCase.getTestNumVars();
            int points           = numRows * numVars;
    %>
        <tr>
        <td valign="top" style="padding: 2px">
            <a title="Test case <%=name%>"
                href="<%=OnlineSTS.getServiceRootURL(request)%>/web/testdetails.jsp?testName=<%=name%>&resultsID=<%=resultsID%>"
                target="_blank"><%= name %>
        </td>
        <td valign="top" style="padding: 5px 5em 5px 5px"><%= theCase.getSynopsis()%></td>
        <td valign="top" style="padding: 5px"><font color="darkred"><%= thisResult.getNumDifferences() %></font></td>
        <td valign="top" style="padding: 5px"><%= points%></td>
        </tr>
    
    <%
        }
    }
%>
    </table>
<%
}

if (countProblems > 0)
{
%>
    <h3 style="width: 100%; border-bottom: 1px solid #ddd; margin-top: 2em">
    Details of problem cases (total: <%=countProblems%>)</h3>

    <p> The following table lists the cases in your uploaded archive that were
    not tested because of problems encountered.  (Unfortunately, the problems
    listed here many not always be the true root cause, because some problems
    can cause a ripple effect that results in a different error being raised
    rather than the true underlying error.)
    
    <table class="borderless-table sm-padding alt-row-colors"
           style="margin-top: 1.5em; border-bottom: 1px solid #999; margin-bottom: 2em"
           width="90%" align="center">
    <tr style="border-top: 1px solid #999; border-bottom: 1px solid #bbb">
        <th valign="bottom" width="60px">Case #</th>
        <th valign="bottom" width="45%">Synopsis of what is being tested</th>
        <th valign="bottom" width="45%">Reason for skipping case</th>
    </tr>

<%
    for (UserTestResult thisResult : results.values())
    {
        if (thisResult != null && thisResult.hasError())
        {
            UserTestCase theCase = thisResult.getUserTestCase();
            String name          = theCase.getCaseName();
%>
    <tr>
    <td valign="top" style="padding: 5px"><%= name %></td>
    <td valign="top" style="padding: 5px 5em 5px 5px"><%= theCase.getSynopsis()%></td>
    <td valign="top" style="padding: 5px"><font color="darkred"><%= thisResult.getErrorMessage() %></font></td>
    </tr>
<%
         }
    }
}
%>
</table>

<%@ include file="sbml-bottom.html"%>
