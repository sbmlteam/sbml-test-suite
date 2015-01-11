<%-- 
 * @file    session-expired.jsp
 * @brief   Tell the user the session has expired.
 * @author  Michael Hucka
 * @date    Created 2010-03-31 <mhucka@caltech.edu>
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
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.math.*" %>

<%@ page import="sbml.test.UserTestCase" %>
<%@ page import="sbml.test.UserTestResult" %>
<%@ page import="sbml.test.TestCase" %>
<%@ page import="sbml.test.CasesTagsMap" %>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<%
// Can't use OnlineSTS class here, because if the session's expired, it's
// not available.

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

<%@ include file="sbml-bottom.html"%>
