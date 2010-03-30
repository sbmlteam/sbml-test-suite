<%-- 
 * @file    report.jsp
 * @brief   Produce a report page summarizing the user's test results.
 * @author  Kimberly Begley
 * @date    Created Feb 27, 2008, 9:25:21 AM
 * @id      $Id$
 * $HeadURL$
 *
 * ----------------------------------------------------------------------------
 * This file is part of the SBML Test Suite.  Please visit http://sbml.org for 
 * more information about SBML, and the latest version of the SBML Test Suite.
 *
 * Copyright 2008-2010 California Institute of Technology.
 * Copyright 2004-2007 California Institute of Technology (USA) and
 *                     University of Hertfordshire (UK).
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.  A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available at http://sbml.org/Software/SBML_Test_Suite/License
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

<%@ page import="sbml.test.UserTestCase" %>
<%@ page import="sbml.test.UserTestResult" %>
<%@ page import="sbml.test.TestReference" %>
<%@ page import="sbml.test.CasesTagsMap" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<%
String baseURL    = "http://sbml.org:8080/test_suite/web/";

int countPassed   = ((Integer) session.getAttribute("countPassed")).intValue();
int countFailed   = ((Integer) session.getAttribute("countFailed")).intValue();
int countProblems = ((Integer) session.getAttribute("countProblems")).intValue();
int countMissing  = ((Integer) session.getAttribute("countMissing")).intValue();

String timeOfRun  = (String) session.getAttribute("timeOfRun");
File casesRootDir = (File) session.getAttribute("casesRootDir");
    
Vector<UserTestResult> results
    = (Vector<UserTestResult>) session.getAttribute("testResults");

int highestCaseNumber = results.size() - 1;
%>

    <h2>Online SBML Test Suite test report</h2>
    <b>Date and time: <%= timeOfRun.toCharArray() %></b>
    
    <h3 style="width: 100%; border-bottom: 1px solid #ddd; margin-top: 2em">
    Test result summary</h3>
    
    <p>
    Total number of files in the uploaded archive: 
    <%= countPassed + countFailed + countProblems %>
    
    <p>
    Number of <font color="green">passed</font> cases: <%=countPassed%><br>
    Number of <font color="darkred">failed</font> cases: <%=countFailed%><br>
    Number of <font color="black">problem</font> cases: <%=countProblems%><br>
    Number of <font color="gray">missing</font> cases: <%=countMissing%><br>
    
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
    // If a particular case result is missing from the user's uploaded set, the
    // corresponding entry in the vector will be null.  Consequently, it's
    // easier to loop over the vector using index numbers.
    
    for (int caseNum = 1; caseNum <= highestCaseNumber; caseNum++)
    {
        UserTestResult thisResult = results.get(caseNum);
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
                href="<%=baseURL%>/testdetails.jsp?testname=<%=name%>"
                target="_blank"><%= name %>
        </td>
        <td valign="top" style="padding: 2px 2em 2px 2px"><%= theCase.getSynopsis()%></td>
        <td valign="top" style="padding: 2px"><font color="darkred"><%= thisResult.getNumDifferences() %></font></td>
        <td valign="top" style="padding: 2px"><%= points%></td>
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
    // If a particular case result is missing from the user's uploaded set, the
    // corresponding entry in the vector will be null.  Consequently, it's
    // easier to loop over the vector using index numbers.
    
    for (int caseNum = 1; caseNum <= highestCaseNumber; caseNum++)
    {
        UserTestResult thisResult = results.get(caseNum);
        if (thisResult != null && thisResult.hasError())
        {
            UserTestCase theCase = thisResult.getUserTestCase();
            String name          = theCase.getCaseName();
%>
    <tr>
    <td valign="top" style="padding: 2px"><%= name %></td>
    <td valign="top" style="padding: 2px 2em 2px 2px"><%= theCase.getSynopsis()%></td>
    <td valign="top" style="padding: 2px"><font color="darkred"><%= thisResult.getErrorMessage() %></font></td>
    </tr>
<%
         }
    }
}
%>
</table>

<%@ include file="sbml-bottom.html"%>
