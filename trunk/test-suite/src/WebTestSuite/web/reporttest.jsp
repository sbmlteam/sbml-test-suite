<%-- 
 * @file    reporttest.jsp
 * @brief   Display test results
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
<link rel="Stylesheet" href="/skins/SBML/sbml.css">
<style type='text/css'><!--
body { background: #ffffff; }
--></style>
</head>

<%
	if(session.isNew() == true) {
		out.println("the session is new ID is " +session.getId());
	}

        String[] totals = new String[3];
        Vector<String> failures = new Vector<String>();
        Vector<String> skips = new Vector<String>();

        totals = (String[])session.getAttribute("totals");
        failures = (Vector<String>)session.getAttribute("failures");
        skips = (Vector<String>)session.getAttribute("skips");
%>
