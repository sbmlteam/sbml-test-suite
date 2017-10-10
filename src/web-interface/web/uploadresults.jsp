<%-- 
 * @file    uploadresults.jsp
 * @brief   JSP file to let user upload test results
 * @author  Kimberly Begley
 * @date    Created Jul 30, 2008, 9:25:21 AM
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
<%@ page import="java.util.regex.*" %>
<%@ page import="java.awt.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="javax.swing.*" %>

<%@ page import="sbml.test.*" %>

<%@ page errorPage="/web/error.jsp" %>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<%
// Start by logging that we've been invoked.

OnlineSTS.init();
OnlineSTS.logInvocation(request);

response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
response.setHeader("Pragma", "no-cache");       // HTTP 1.0
response.setDateHeader("Expires", 0);           // Prevent caching by proxy server
%>

<script type="text/javascript" type="text/javascript">
/**
 * The onSubmit() JavaScript function does multiple things in one go:
 * 1) checks that the user's file name ends in .zip
 * 2) puts up a progress bar instead of the example pathname text
 * 3) disable the upload button after it's pressed (to prevent
 *    a 2nd submission before the 1st is complete -- happens
 *    often when the user double clicks)
 */
function onSubmit()
{
    var upload_field = document.getElementById('file');
    var filename     = upload_field.value;

    if (filename.search(/\.zip/i) == -1)
    {
        alert("The file name does not appear to have a .zip extension."
              + " We regret that this system can only accept archives"
              + " in zip format.");
        upload_field.form.reset();
        return false;
    }
    var upload_status = document.getElementById('upload_status');
    upload_status.style.display = "block";
    var example = document.getElementById('example');
    example.style.display = "none";
    var upload_button = document.getElementById('upload_button');
    upload_button.disabled = true;
    return true;
}
</script>

<div id='pagetitle'><h1 class='pagetitle'>Step 3: Upload and evaluate results</h1>
</div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="<%=OnlineSTS.getImageURL(request)%>/Icon-online-test-suite-64px.jpg"
       border="0">
</div>

<p> After having run the test cases in the software you are testing according
to the guidelines descripted in the previous step, you can upload the
simulation outputs and have them tallied against the expected results.
				
<div style="padding: 1em 2em 1em 2em;">
<form action="<%=OnlineSTS.getServiceRootURL(request)%>/servlet/UploadUnzipTest" 
      enctype="multipart/form-data" method="post" onSubmit="return onSubmit()">
<b><em>Browse for zip'ed archive of output files:</em></b>
<div style="padding: 1em 0.5em 0 1em;">
<table class="borderless-table" width="75%">
    <tr>
    <td valign="top" style="padding: 0 0 0 1em">
        <input id="file" type="file" size="40" name="result_files"/>
    </td>
    <td align="right">
        <input id="upload_button" type="submit" value="Upload">
    </td>
    </tr>
</table>
</div>
</form>
<p id="example" style="font-size: 8pt; font-style: italic; color: #666">
E.g., C\:Program Files\SBML\MyModels\myresultsfile.zip 
</p>
<div id="upload_status" style="display: none; margin: 1em 0 0 2.1em;">
  <img src="<%=OnlineSTS.getImageURL(request)%>/progressBarLong.gif" 
       alt="Progress bar" width="200" height="14"
       style="border: 1px solid #bbb"  />
  <i>Uploading file, performing analysis, and returning results ...</i>
</div>
</div>

<p> Your archive file will be uploaded into a temporary directory on this
server, where the individual test case results will be validated and compared
to the expected results.  This system will then summarize the comparison
results and provide links to the test details.  The comparison summary and
other results will replace this page.
</p>

<p>
To cancel the process, simply return to the Test Suite front page.
</p>
<center>
  <a href="http://sbml.org/Facilities/Online_SBML_Test_Suite">
    <img border="0" align="center" 
         src="<%=OnlineSTS.getImageURL(request)%>/Icon-red-left-arrow.jpg">
    Return to the Online SBML Test Suite front page.
  </a>
</center>

<%@ include file="sbml-bottom.html"%>
