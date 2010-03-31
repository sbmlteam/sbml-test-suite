<%-- 
 * @file    process-selections.jsp
 * @brief   JSP file to process user's test results.
 * @author  Michael Hucka
 * @author  Kimberly Begley
 * @date    Created Feb 27, 2008, 9:25:21 AM
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
 * This file handles interpreting the user's selections from selecttests.jsp
 * and building a zip file of test cases to return to the user.
 *
--%>

<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.net.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*" %>
<%@ page import="sbml.test.CasesTagsMap" %>

<%@ page errorPage="/web/error.jsp" %>

<jsp:useBean id="formHandler" class="sbml.test.FormBean" scope="request">
    <jsp:setProperty name="formHandler" property="*"/>
</jsp:useBean>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<%
//
// 1. Get the user's selection from the previous screen.
//

String[] ctags         = formHandler.getCtags();
String[] ttags         = formHandler.getTtags();
String levelAndVersion = formHandler.getLevelAndVersion()[0];


//
// 2. Build up a list of case files that we will zip up and return.
//

File casesRootDir    = new File(getServletContext().getRealPath("/test-cases"));
CasesTagsMap caseMap = new CasesTagsMap(casesRootDir);
int highest          = caseMap.getHighestCaseNumber();
Vector casesToReturn = new Vector();

outer:
for (int i = 1; i <= highest; i++)
{
     Vector<String> caseData = (Vector<String>) caseMap.get(i);
     if (caseData.contains(levelAndVersion))
     {
         for (int j = 0; j < ctags.length; j++)
         {
             if (caseData.contains(ctags[j]))
                 continue outer;
         }
         for (int j = 0; j < ttags.length; j++)
         {
             if (caseData.contains(ttags[j]))
                 continue outer;
         }
         casesToReturn.add(caseData.get(0));
     }
}

if (casesToReturn.size() < 1)
{
    throw new JspException("STS has no cases to put into archive");
}

//
// 4. Call our zip file builder with the results and some additional param.
//

session.putValue("casesRootDir"   , casesRootDir);
session.putValue("returnedCases"  , casesToReturn);
session.putValue("levelAndVersion", levelAndVersion);
response.setHeader("Refresh",
                   "1; URL=http://sbml.org:8080/test_suite/servlet/ZipServlet");

%>

<div id='pagetitle'><h1 class='pagetitle'>
Assembling and downloading archive of selected test cases</h1></div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>
<p>
Based on your selections, an archive containing the selected SBML test
should begin downloading automatically in a few seconds.  After receiving
and unpacking the archive, please proceed to Step 2.
<center style="margin: 1.5em">
  <a href="/Software/SBML_Test_Suite/Step_2:_Running_the_tests">
    <img border="0" align="center" src="/images/e/ec/Icon-red-right-arrow.jpg">
    Go to the instructions for Step 2 (running the tests).
  </a>
</center>
</p>

<p> If the download does not begin soon, <b>please</b> let 
us know by sending mail to <a href="mailto:sbml-team@caltech.edu">sbml-team@caltech.edu</a> 
and please provide information about the operating system and web browser you are using.

<%@ include file="sbml-bottom.html"%>
