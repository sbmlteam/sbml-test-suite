<%-- 
 * @file    report.jsp
 * @brief   Produce a report page summarizing the user's test results.
 * @author  Kimberly Begley
 * @date    Created Feb 27, 2008, 9:25:21 AM
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
<%@ page import="java.util.regex.*" %>
<%@ page import="java.awt.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="sbml.test.sbmlTestselection" %>
<%@ page import="sbml.test.sbmlTestcase" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="javax.swing.*" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN">
<head>
<base href="http://sbml.org">
<link rel="Stylesheet" href="/skins/SBML/sbml.css">
<style type='text/css'><!--
body { background: #ffffff; }
--></style>
</head>

<%
// This file writes a summary of the skipped and failed cases during a test suitable for printing.
// Get the session values from the previous page showresults.jsp

	String[] totals = (String[])session.getAttribute("totals");
	Vector<String> failures = (Vector<String>)session.getAttribute("failures");
	Vector<String> skips = (Vector<String>)session.getAttribute("skips");


	Iterator it = failures.iterator(); 
	Iterator is = skips.iterator(); 

%>

	SBML Test Suite Report <BR>
	<BR>
<%
 out.println("Date:" +  new java.util.Date() + "<br>");
 %>
	TOTALS<BR>
	Number of passed cases:<%=totals[0]%><BR>
	Number of failed cases:<%=totals[1]%><BR>
	Number of skipped cases:<%=totals[2]%><BR>
	<BR>
	<BR>
	Failed Cases Detailed<BR>
	<TABLE border="1" class="sm-padding">
	<TR><TH>Case<TH>Synopsis<TH>Failed points count<TH>Total points</TR>
<%
	while(it.hasNext()) {
		String failed = (String)it.next();
		String[] field = failed.split(",");
%>		
		<TR><TD><%=field[0]%></TD><TD><%=field[1]%></TD><TD><%=field[2]%></TD><TD><%=field[3]%></TD></TR>
<%	}
%>	</TABLE>
<BR>
	<BR>
	Skipped Cases Detailed<BR>
	<TABLE border="1" class="sm-padding">
	<TR><TH>Case<TH>Synopsis<TH>Warnings</TR>
<%	while(is.hasNext()) {
		String skipped = (String)is.next();
		String[] sfield = skipped.split("\\?");
%>
		<TR><TD><%=sfield[0]%></TD><TD><%=sfield[1]%></TD><TD><%=sfield[2]%></TD></TR>	
<%	}
%>
	</TABLE>
	


