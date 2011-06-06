<%-- 
 * @file    session-mising-state.jsp
 * @brief   Tell the user something is missing from the session
 * @author  Michael Hucka
 * @date    Created 2010-03-31 <mhucka@caltech.edu>
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
%>

<div id='pagetitle'><h1 class='pagetitle'><font color="darkred">
SBML Test Suite session state error</font></h1></div><!-- id='pagetitle' -->
<div style="float: right; margin: 0 0 1em 2em; padding: 0 0 0 5px">
  <img src="<%=OnlineSTS.getImageURL(request)%>/Icon-online-test-suite-64px.jpg">
</div>

<p>
Uh-oh.  Please don't panic, but something has gone amiss in this particular
SBML Test Suite invocation, and there seems to be browser or session data
missing.  We regret the only safe thing to do at this point is to send you
back to the Test Suite front page.

<p>	
<center>
  <a href="http://sbml.org/Facilities/Online_SBML_Test_Suite">
    <img border="0" align="center" 
         src="<%=OnlineSTS.getImageURL(request)%>/Icon-red-left-arrow.jpg">
    Return to the Online SBML Test Suite front page.
  </a>
</center>
</p>

<%@ include file="sbml-bottom.html"%>
