<%-- 
 * @file    process.jsp
 * @brief   JSP file to process user's test results.
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

<%@ include file="sbml-head.html"%>

<jsp:useBean id="formHandler" class="sbml.test.FormBean" scope="request">
    <jsp:setProperty name="formHandler" property="*"/>
</jsp:useBean>

<%!  
     String[] testtype;
     String[] ctags;
     String[] ttags;
     String[] level;
     
     String testdir;
     String testdir_listing [];
     String test;   
%>

<%
    // Get the input from the test selections form
    level = formHandler.getLevels();
    testtype = formHandler.getTesttype();
    ctags = formHandler.getCtags();
    ttags = formHandler.getTtags();

    sbmlTestselection t1 = new sbmlTestselection();
%>

<%  	String testdir = new String();
	// Get the local directory for the test cases from the configuration file    
	ServletContext context = getServletContext();
	InputStream is = context.getResourceAsStream("/WEB-INF/classes/sbml_config_file.txt");
	try{
		BufferedReader d = new BufferedReader(new InputStreamReader(is));
		String line; 
   		if  (   (  line = d.readLine (  )   )  != null  )   {  
     			testdir=line ; 
    		}  
	}
	finally{
		if(is != null) {
			is.close();
		}
	}
%> 
<%
	String testdir_listing [];

	// get presence of a header line from the user when they upload maybe?
	int header = 1; 
	// Get directory listings 
	File f = new File(testdir);
	testdir_listing = f.list();
	String tcase = new String(); 
	String tmodelfile = new String();
	Vector<String> selected_cases = new Vector<String>();
       // for each test case check if it should be included in the download for the user
	for(int i=0;i<testdir_listing.length;i++) {	
  		tcase = testdir_listing[i];
		
		// check here that the test name conforms
		Pattern p = Pattern.compile("\\d{5}$");
		Matcher matcher = p.matcher(tcase);
		if(matcher.find()) {
			tmodelfile = t1.getModelFile(tcase,testdir);
			t1.readModelFile(tmodelfile);
			
			int itsout = 0;
	//		if(t1.containsTesttype(testtype[0]) && t1.containsLevel(level[0])) {
			// Check the test is of the right level specification
			if(t1.containsLevel(level[0])) {
				for(int j=0;j<ctags.length;j++) {
					if(t1.containsComponent(ctags[j])) {
					itsout++;
					}
				}
				for(int k=0;k<ttags.length;k++) {
					if(t1.containsTag(ttags[k])) {
					itsout++;
					}
				}
				// if it hasn't been eliminated add it to the vector of cases to download
				if(itsout==0) {
					selected_cases.addElement(tcase);
					
				}
			

			} // end of if
		}
	}

	// add the variables to pass along to the next page - zipservlet
	session.putValue("path",f);
	session.putValue("tcases",selected_cases);
	response.setHeader("Refresh", "1; URL=http://sbml.org:8080/test_suite/servlet/ZipServlet");
//	response.setHeader("Refresh", "1; URL=/test_suite/servlet/ZipServlet");		
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.0//EN">
<head>
<base href="http://sbml.org">
<link rel="Stylesheet" href="/skins/SBML/sbml.css">
<style type='text/css'><!--
body { background: #ffffff; }
--></style>
</head>

   <body>
<p>

<center style="margin: 1.5em">
  <a href="/Software/SBML_Test_Suite/Step_2:_Running_the_tests">
    <img border="0" align="center" src="/images/e/ec/Icon-red-right-arrow.jpg">
    Go to the instructions for Step 2 (running the tests).
  </a>
</center>
</p>
</body>
<%@ include file="sbml-bottom.html"%>

