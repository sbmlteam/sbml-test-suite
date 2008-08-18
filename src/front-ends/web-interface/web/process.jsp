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
%>

<%
    // Get the user's selection from the previous screen.

    level    = formHandler.getLevels();
    testtype = formHandler.getTesttype();
    ctags    = formHandler.getCtags();
    ttags    = formHandler.getTtags();

    // Get the listing from the test cases directory and build the list
    // of test cases to hand off to ZipServlet.

    File testdir = new File(getServletContext().getRealPath("/test-cases"));
    String testdir_path = testdir.toString();
    String testdir_listing [] = testdir.list();

    sbmlTestselection t1          = new sbmlTestselection();
    Vector<String> selected_cases = new Vector<String>();
    String tcase                  = new String(); 
    String tmodelfile             = new String();

    for (int i = 0; i < testdir_listing.length; i++) {
        tcase = testdir_listing[i];
        
        // check here that the test name conforms
        Pattern p = Pattern.compile("\\d{5}$");
        Matcher matcher = p.matcher(tcase);
        if (matcher.find()) {
            tmodelfile = t1.getModelFile(tcase, testdir_path);
            t1.readModelFile(tmodelfile);

            int itsout = 0;

            if (t1.containsLevel(level[0])) {

                for (int j = 0; j < ctags.length; j++) {
                    if (t1.containsComponent(ctags[j])) {
                        itsout++;
                    }
                }
                
                for (int k = 0; k < ttags.length; k++) {
                    if (t1.containsTag(ttags[k])) {
                        itsout++;
                    }
                }
                        
                if (itsout == 0) {
                    selected_cases.addElement(tcase);
                }
            } // if t1.containsLevel
        } // if matcher.find
    }

    session.putValue("path", testdir_path);
    session.putValue("tcases", selected_cases);
    response.setHeader("Refresh", "1; URL=http://sbml.org:8080/test_suite/servlet/ZipServlet");
%>

<%@ include file="sbml-top.html"%>

<div id='pagetitle'><h1 class='pagetitle'>Downloading selected test cases</h1></div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>
<p>
Based on your selections, an archive containing the selected SBML test
should begin downloading automatically in a few seconds.  After receiving
and unpacking the archive, proceed to Step 2.
<center style="margin: 1.5em">
  <a href="/Software/SBML_Test_Suite/Step_2:_Running_the_tests">
    <img border="0" align="center" src="/images/e/ec/Icon-red-right-arrow.jpg">
    Go to the instructions for Step 2 (running the tests).
  </a>
</center>
</p>

<p> If an archive does not begin downloading, <b>please</b> let us know by
<a href="mailto:sbml-team@caltech.edu">sending mail to
sbml-team@caltech.edu</a> and please provide information about the
operating system and web browser you are using.

</body>
<%@ include file="sbml-bottom.html"%>

