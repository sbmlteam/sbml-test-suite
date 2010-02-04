<%-- 
 * @file    testdetails.jsp
 * @brief   Display the details of a single test case result.
 * @author  Kimberly Begley
 * @author  Michael Hucka
 * @date    Created Jul 30, 2008, 9:25:21 AM
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
--%>

<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="sbml.test.sbmlTestselection" %>
<%@ page import="sbml.test.TestResultDetails" %>
<%@ page import="sbml.test.TestReference" %>
<%@ page import="sbml.test.sbmlTestcase" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%@ include file="sbml-head.html"%>

<% 
// We get a number of things via the URL handed to us in the link
// that the user clicked.  The rest we get from the context.
// Note that for some cases, there are no results from the user;
// we might be getting called to just display the reference data.

String testdir  = getServletContext().getRealPath("/test-cases");
String testname = request.getParameter("testname"); 
String result   = request.getParameter("result");
String warnings = request.getParameter("warnings");
String tpoints  = request.getParameter("tpoints");

TestReference reference = new TestReference(testdir, testname);
%>

<%@ include file="sbml-top.html"%>

<div id='pagetitle'>
<h1 class='pagetitle'>Details for SBML Test Case #<%=testname%></h1>
</div><!-- id='pagetitle' -->
<div style="float: right; margin: 0 0 1em 2em; padding: 0 0 0 5px">
  <img src="http://sbml.org/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>

<%
if (result == null)
{
    out.println("<p><b><font color=\"GoldenRod\">");
    out.println("Note: Results for this case were <i>not</i> part of your uploaded ");
    out.println("test results.</font></b></p>");
}
%>

<p>
<em>Synopsis of test case</em>: <%=reference.getSynopsis()%><BR>
</p>

<%
if (result != null)
{
    out.println("<p><em>Result</em>:\n");

    int r = Integer.parseInt(result);
    if (r > 0)
    {
%>
        <font color="darkred"><b><font color="darkred">Failed</font></b> at
        <%=result%> of <%=tpoints%> data points</font><BR>
<%
    }
    else if (r == 0)
    {
%>
        <b><font color="green">Passed!</font></b><BR>
<%
    }

    if (warnings != null && warnings != "")
    {
%>
        <b>Skipped</b> due to the following reason(s):<br>
        <blockquote style="width: 80%; border: 1px solid gold; padding: 0.5em">
            <%=warnings%>
        </blockquote>
<%
    }
}
%>

<p>
<em>Component tags involved in test case</em>: <%=reference.getComponentTagsAsString()%>. <BR>
</p>
<p>
<em>Test tags involved in test case</em>: <%=reference.getTestTagsAsString()%>.<BR>
</p>

<p>
The following is a plot of the expected results:
</p>

<center>
    <img style="margin-left: -80px" 
         src="http://sbml.org:8080/test_suite/servlet/OpenFile?plot=<%=reference.getPlotFile().getPath()%>"
         align="center" alt="plot"> 
</center>

<p>
<style type='text/css'>
table td, th {
  font-size: 9pt;  
  padding: 2px;
}

table {
  margin: 1em auto 1em auto;
  width: 75% !important;
}
</style>
<c:import url="<%="file://" + reference.getHTMLFile().getPath()%>" />
</p>

<p>	
<center>
  <a href="http://sbml.org/Facilities/Online_SBML_Test_Suite">
    <img border="0" align="center" 
         src="http://sbml.org/images/8/83/Icon-red-left-arrow.jpg">
    Return to the Online SBML Test Suite front page.
  </a>
</center>
</p>

<%@ include file="sbml-bottom.html"%>
