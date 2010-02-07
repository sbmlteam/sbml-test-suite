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
<%@ page import="java.util.regex.*" %>
<%@ page import="java.awt.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="sbml.test.TestReference" %>

<%@ page errorPage="/web/error.jsp" %>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<%
    String[] totals         = (String[]) session.getAttribute("totals");
    Vector<String> failures = (Vector<String>) session.getAttribute("failures");
    Vector<String> problems = (Vector<String>) session.getAttribute("problems");
    String timeOfRun        = (String) session.getAttribute("timeOfRun");
    File casesRootDir       = (File) session.getAttribute("casesRootDir");
    
    Iterator it;
%>

<h2>Online SBML Test Suite test report</h2>
<b>Date and time: <%= timeOfRun.toCharArray() %></b>

<h3 style="width: 100%; border-bottom: 1px solid #ddd">
Test result summary</h3>

<p>
Number of <font color="green">passed</font> cases: <%=totals[0]%><br>
Number of <font color="darkred">failed</font> cases: <%=totals[1]%><br>
Number of <font color="black">problem</font> cases: <%=totals[2]%><br>
Number of <font color="gray">missing</font> cases: <%=totals[3]%><br>

<h3 style="width: 100%; border-bottom: 1px solid #ddd">
Details of failed cases</h3>

<%
    it = failures.iterator(); 
    if (! it.hasNext())
    {
%>

There were no failures.

<%
    }
    else
    {
%>
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
        while (it.hasNext())
        {
            String failed      = (String) it.next();
            String[] field     = failed.split("\\?");
            String caseName    = field[0];
            String result      = field[1];
            String totalpoints = field[2];
        
            TestReference ref = new TestReference(casesRootDir, caseName);
        
            String args = "&testname=" + caseName + "&result=" + result
                           + "&tpoints=" + totalpoints;
    
%>		

    <tr>
    <td valign="top" style="padding: 2px">
        <a title="Test case <%=caseName%>"
            href="http://sbml.org:8080/test_suite/web/testdetails.jsp?<%=args%>"
            target="_blank"><%= caseName %>
    </td>
    <td valign="top" style="padding: 2px 2em 2px 2px"><%= ref.getSynopsis()%></td>
    <td valign="top" style="padding: 2px"><%= result %></td>
    <td valign="top" style="padding: 2px"><%= totalpoints%></td>
    </tr>

<%
        }
    }
%>

</table>

<h3 style="width: 100%; border-bottom: 1px solid #ddd; margin-top: 2em">
Details of problem cases</h3>

<%
    it = problems.iterator(); 
    if (! it.hasNext())
    {
%>

No cases had problems.

<%
    }
    else
    {
%>

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
        it = problems.iterator(); 
        while (it.hasNext())
        {
            String failed = (String) it.next();
            String[] field = failed.split("\\?");
            String caseName = field[0];
        
            TestReference ref = new TestReference(casesRootDir, caseName);
%>		

    <tr>
    <td valign="top" style="padding: 2px"><%= caseName %></td>
    <td valign="top" style="padding: 2px 2em 2px 2px"><%= ref.getSynopsis()%></td>
    <td valign="top" style="padding: 2px"><%= field[1] %></td>
    </tr>

<%
        }
    }
%>

</table>

<%@ include file="sbml-bottom.html"%>
