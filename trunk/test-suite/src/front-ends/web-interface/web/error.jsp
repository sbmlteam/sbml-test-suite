<%-- 
 * @file    error.jsp
 * @brief   Error output form.
 * @author  Kimberly Begley
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

<%@ include file="sbml-head.html"%>

<% 
    String error = (String)request.getAttribute("error"); 
%>

<%@ include file="sbml-top.html"%>


<div id='pagetitle'><h1 class='pagetitle'>Upload Error</h1></div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>
<p>
The Online SBML Test Suite system could not complete this step because it
encountered the following problem:
</p>

<blockquote class="error">
<%=error%><br>
</blockquote>

<p> You can use your browser's back button to return the previous page and
try again.
</p>

<p> If the problem persists or does not appear to be due to something about
the input you provided, please let us know by <a
href="mailto:sbml-team@caltech.edu">sending mail to
sbml-team@caltech.edu</a>.  </p>

<%@ include file="sbml-bottom.html"%>
