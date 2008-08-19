<%-- 
 * @file    testdetails.jsp
 * @brief   Display the details of a single test case result.
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
<%@ page import="java.lang.*" %>
<%@ page import="sbml.test.sbmlTestselection" %>
<%@ page import="sbml.test.TestResultDetails" %>
<%@ page import="sbml.test.sbmlTestcase" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<% 
	String testname = request.getParameter("testname"); 
	String result = request.getParameter("result");
	String plot = request.getParameter("plot");
	String html = request.getParameter("html");
	String description = request.getParameter("description");
	String warnings = request.getParameter("warnings");
	String ctags = request.getParameter("ctags");
	String ttags = request.getParameter("ttags");
	String tpoints = request.getParameter("tpoints");
%>

<%@ include file="sbml-head.html"%>

<style type="text/css">/*<![CDATA[*/
table td, th {
  font-size: 9pt;  
  padding: 2px;
}

table {
  margin: 1em 0 1em 0;
}
/*]]>*/</style>

<%@ include file="sbml-top.html"%>

<div id='pagetitle'><h1 class='pagetitle'>Details for SBML Test Case #<%=testname%></h1>
</div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>

<p><em>Result</em>:
<%
	int r = Integer.parseInt(result);
	if(r>0) {
%>
	<font color="darkred"><b><font color="darkred">Failed</font></b> at
	<%=result%> of <%=tpoints%> data points</font><BR>
<%	}
	if(r==0) {
%>
	<font color="green"><b>Passed!</b></font><BR>
<%	}

	if(warnings != "") {
%>
	<b>Skipped</b> due to the following reason(s):<br>
	<blockquote style="width: 80%; border: 1px solid gold; padding: 0.5em"><%=warnings%></blockquote>
<% } %>

<p>
	<em>Component tags involved in test case</em>: <%=ctags%> <BR>
</p><p>
	<em>Test tags involved in test case</em>     : <%=ttags%> <BR>
</p><p>
	<em>Synopsis of test case</em>: <%=description%><BR>
	</p>

<center>
	<IMG style="margin-left: -80px" SRC="http://sbml.org:8080/test_suite/servlet/OpenFile?plot=<%=plot%>" align="center" ALT="plot"> 
</center>

<p>
<style type='text/css'>
table { width: 75% !important; }
</style>

<%      String fileh = "file://" + html;
%>
	<c:import url="<%=fileh%>" />
</p>

<p>	
<center>
  <a href="/Facilities/Online_SBML_Test_Suite">
    <img border="0" align="center" src="/images/8/83/Icon-red-left-arrow.jpg">
    Return to the Online SBML Test Suite front page.
  </a>
</center>
</p>

<%@ include file="sbml-bottom.html"%>
