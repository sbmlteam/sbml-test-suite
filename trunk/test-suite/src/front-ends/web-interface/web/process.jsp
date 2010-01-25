<%-- 
 * @file    process.jsp
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
<%@ page import="javax.servlet.http.*"%>

<%@ page import="java.text.*" %>

<%@ page errorPage="/web/error.jsp" %>

<%@ include file="sbml-head.html"%>

<jsp:useBean id="formHandler" class="sbml.test.FormBean" scope="request">
    <jsp:setProperty name="formHandler" property="*"/>
</jsp:useBean>

<%@ include file="sbml-top.html"%>

<%
// 1. Construct boolean arrays mapping tags to cases containing those tags.
//
// We read in the tag info from the file ".cases-tags-map" into a vector
// equal in size to the number of cases.  Each entry is indexed by a case
// number, and contains another vector; this second vector contains all the
// tags mentioned in that case's model definition file.  I.e.,
//
//     ["tag1", "tag2", ...]
//
// Since tags are unique no matter what kind they are (component, test,
// level), they don't need to be distinguished for our purposes here.

File testdir = new File(getServletContext().getRealPath("/test-cases"));
if (testdir == null || !testdir.exists())
{
  throw new JspException("STS cannot find test cases dir");
}   

File mapfile = new File(testdir, ".cases-tags-map");
if (mapfile == null || !mapfile.exists())
{
  throw new JspException("STS cannot find cases tags map file");
}   

Scanner fileReader = new Scanner(mapfile);
int highest = 0;

// The first line in the tags map file is the highest case number.
// If that's not the case, something is wrong.

if (fileReader.hasNext() && fileReader.hasNextInt())
 {
   highest = fileReader.nextInt();
   fileReader.nextLine();               // Skip past end of line.
 }
 else
 {
   throw new JspException("STS tags map file not in expected format");
 }

// Each subsequent line in the tags map file is a case number followed by
// all its tags.  We're going to read each line, parse each item in the
// line as a token using Scanner methods, and store the line's contents in
// a vector called "cases".  The entries in "cases" are indexed by case
// numbers.  (I.e., entry 1 in the vector of cases is case 00001, entry 2
// is case 00002, etc.)  "Cases" is thus a vector of vectors.  

Vector cases = new Vector();
cases.setSize(highest);

while (fileReader.hasNext())
{
    Scanner tagreader = new Scanner(fileReader.nextLine());
    String caseNum    = tagreader.next();
    Vector data       = new Vector();

    // As a minor optimization, because we want the case number as a string
    // later, we store the string-form of the case number as the first
    // element in the tags data vector.    

    data.add(caseNum);

    // Now read the contents of the rest of the line and append each item
    // (which will be a tag) to the end of the data vector.

    while (tagreader.hasNext())
      data.add(tagreader.next());

    // Store it under the index number "caseNum".

    cases.insertElementAt(data, Integer.parseInt(caseNum));
}


// 2. Get the user's selection from the previous screen.

String[] ctags         = formHandler.getCtags();
String[] ttags         = formHandler.getTtags();
String levelAndVersion = formHandler.getLevelAndVersion()[0];


// 3. Build up a list of case files that we will zip up and return.

Vector casesToReturn = new Vector();

outer:
for (int i = 1; i <= highest; i++)
 {
   Vector caseData = (Vector) cases.get(i);
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

// 4. Call our zip file builder with the results.

session.putValue("internalError", null);
session.putValue("path", testdir);
session.putValue("cases", casesToReturn);
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
