<%-- 
 * @file    user-error.jsp
 * @brief   Error handling for internal errors.
 * @author  Michael Hucka
 *
 * $Id$
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
 * This code handles user errors in conjunction with UploadUnzipTest.java.
 * A different page (error.jsp) handles reporting problems arising from
 * internal errors. 
 *
--%>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.text.*" %>
<%@ page import="javax.servlet.jsp.*" %>
<%@ page import="javax.servlet.http.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<%

//
// Find out the type of error we're dealing with.  This attribute
// is set by the caller, UploadUnzipTest.java.
//

String errorType     = (String) request.getAttribute("errorType");

//
// Miscellaneous other variables and constants.
//

Calendar cal         = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("dd MMM. yyyy 'at' KK:mm a (z)");
String timestamp     = sdf.format(cal.getTime());

%>

<div id='pagetitle'><h1 class='pagetitle'><font color="darkred">
SBML Test Suite error</font></h1></div><!-- id='pagetitle' -->
<div style="float: right; margin: 0 0 1em 2em; padding: 0 0 0 5px">
  <img src="http://sbml.org/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>
<h3>Results produced at <%=timestamp%></h3>

<p>
We're sorry, but the Online SBML Test Suite encountered a problem with the uploaded
results:
<%
if (errorType.equals("bad file names"))
{
%>
<font color="darkred">the result files in the archive do not have the
correct file name pattern.</font> Please name the CSV files such that they
<i>end</i> with case numbers.  For example, the files could be named like
this:

<p>
<dl><dd> <code>myresults00001.csv</code>
</dd><dd> <code>myresults00002.csv</code>
</dd><dd> <code>myresults00003.csv</code>
</dd><dd> <code>myresults00004.csv</code>
</dd><dd> &hellip;
</dd><dd> <code>myresultsNNNNN.csv</code>
</dd></dl>

<p>
They could even be simply named after the case numbers, without any prefix:
<p>
<dl><dd> <code>00001.csv</code>
</dd><dd> <code>00002.csv</code>
</dd><dd> <code>00003.csv</code>
</dd><dd> <code>00004.csv</code>
</dd><dd> &hellip;
</dd><dd> <code>NNNNN.csv</code>
</dd></dl>

<p>
For more information, please consult the
<a href="http://sbml.org/Software/SBML_Test_Suite/Instructions_for_running_the_tests#Gathering_the_results_of_many_tests_for_uploading_to_the_Online_SBML_Test_Suite">
page of instructions</a> for running the Online SBML Test Suite.

<%
}
%>


<center style="margin: 1em">
  <a href="http://sbml.org:8080/test-suite/web/uploadresults.jsp">
    <img align="center" src="http://sbml.org/images/8/83/Icon-red-left-arrow.jpg">
    Return to the upload page. 
  </a>
</center>

<%@ include file="sbml-bottom.html"%>

%>

