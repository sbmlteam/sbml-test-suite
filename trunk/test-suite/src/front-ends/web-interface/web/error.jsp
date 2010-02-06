<%-- 
 * @file    error.jsp
 * @brief   Error handling for internal errors.
 * @author  Michael Hucka
 * @author  Kimberly Begley
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
 * This handles both internal errors and errors arising from user mistakes.

--%>

<%
// The string isErrorPage="true" causes the Web container to define a new
// implicit variable, "exception", which we can query below to get some
// more information about what happened.
%>
<%@ page isErrorPage="true" %>

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

final class InternalUtility
{
    /**
     * Encodes the passed String as UTF-8 using an algorithm that's
     * compatible with JavaScript's <code>encodeURIComponent</code> function.
     * Returns <code>null</code> if the given parameter is <code>null</code>.
     * (Function based on code posted by John Topley at
     * http://stackoverflow.com/questions/607176)
     */
    public String encode(String s)
    {
        try
        {
            return URLEncoder.encode(s, "UTF-8")
            .replaceAll("\\+", "%20")
            .replaceAll("\\%21", "!")
            .replaceAll("\\%27", "'")
            .replaceAll("\\%28", "(")
            .replaceAll("\\%29", ")")
            .replaceAll("\\%7E", "~");
        }
        catch (UnsupportedEncodingException e)
        {
            // This exception should never actually occur.
            return s;
        }
    }

    public String getRequestData(HttpServletRequest r)
    {
        String text = new String();
        String[] vars = {
            "status_code",
            "exception_type",
            "message",
            "request_uri",
            "error"                           // Not standard JSP -- we add it.
        };

        for (int i = 0; i < vars.length; i++)
        {
            String fullname = "javax.servlet.error." + vars[i];
            text += fullname + ": " + r.getAttribute(fullname) + "\n";
        }

        text += "Servlet path: " + r.getServletPath() + "\n";
        text += "Method: " + r.getMethod() + "\n";

        return "Session data: \n" +  text;
    }

    public String getStackTrace(java.lang.Throwable e)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw  = new PrintWriter(sw, true);
        e.printStackTrace(pw);
        return "Stack trace: \n" + sw.toString();
    }
}
%>

<%
// If the request environment has the attribute "userError" set, it means
// the error we're dealing with is something that is the user's fault.
// The attribute will be set by whatever calls this JSP page (usually a
// Java utility program).  The value of "userError" is a string code
// that we use below as the basis of branching to an explanation.

String userError     = (String) request.getAttribute("userError");

// Additional data we're going to need eventually.

Calendar cal         = Calendar.getInstance();
SimpleDateFormat sdf = new SimpleDateFormat("dd MMM. yyyy 'at' HH:mm:ss [z]");
String timestamp     = sdf.format(cal.getTime());
%>

<div id='pagetitle'><h1 class='pagetitle'><font color="darkred">
SBML Test Suite error</font></h1></div><!-- id='pagetitle' -->
<div style="float: right; margin: 0 0 1em 2em; padding: 0 0 0 5px">
  <img src="http://sbml.org/images/8/80/Icon-online-test-suite-64px.jpg">
</div>
<h3>Results produced at <%=timestamp%></h3>

<%
// Now switch based on what kind of error we're dealing with.  For user
// errors, we try to provide detailed explanations.  The following blocks
// are a bit messy and hard to read because of the interspersed JSP and
// HTML.  (FIXME: there's got to be a better way to write this stuff.)

if (userError != null)
{
%>

<p>
We're sorry, but the Online SBML Test Suite encountered a problem.
<%
    if (userError.equals("bad file names"))
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

<center style="margin: 1em">
<a href="http://sbml.org:8080/test-suite/web/uploadresults.jsp">
  <img align="center" src="http://sbml.org/images/8/83/Icon-red-left-arrow.jpg">
  Return to the upload page. 
</a>
</center>

<%
    }
    else if (userError.equals("unrecognized files"))
    {

    }
}
else  // It's not a user error, it's an internal error.
{
    // Try to get extra information from the calling environment and
    // set up variables used in constructing an email message later.

    InternalUtility util = new InternalUtility();
    String subject = util.encode("SBML Test Suite error (" + timestamp + ")" );
    String body    = util.encode("\n\n\n"
        + "-----------------------------------------------------------------\n"
        + "Please write your comments above the line and leave the rest of\n"
        + "the text below as-is.  The rest of this message is debugging data.\n"
        + "\n"
        + "Thank you very much for taking the time to report this error.\n"
        + "-----------------------------------------------------------------\n"
        + "\n"
        + "Time stamp: " + timestamp + "\n"
        + "\n"
        + "Session path value: " + session.getValue("path") + "\n"
        + "Session cases value: " + session.getValue("cases") + "\n"
        + "\n"
        + util.getRequestData(request) + "\n"
        + (exception != null ? util.getStackTrace(exception) : "" ));
    
    String mailto   = "mailto:sbml-team@caltech.edu"
                      + "?subject=" + subject
                      + "&body=" + body;
%>

<div id='pagetitle'><h1 class='pagetitle'><font color="darkred">
SBML Test Suite error</font></h1></div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="http://sbml.org/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>

<p> We're very sorry, but the Online SBML Test Suite encountered an error.
Would you please contact us and let us know what lead up to this?  Simply
clicking the button below will invoke your mail program with pre-filled
data.  Before sending the message, if you could please <b>add an
explanation about what happened</b>, it will help us understand the
circustances better. </p>

<center style="margin: 1.5em auto 2em auto">
<a href="<%=mailto%>">
<img src="http://sbml.org/images/8/83/Icon-send-mail-64px.jpg" border="0"><br>
(Click to send an email report.)
</a>
</center>

<p>
If instead you would like to return to the previous page, please use
your browser's back button.
</p>

<%
}
%>

<%@ include file="sbml-bottom.html"%>
