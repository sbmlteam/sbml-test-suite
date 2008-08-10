<%-- 
 * @file    showresults.jsp
 * @brief   Display a summary map of the user's test results
 * @author  Kimberly Begley
 * @date    Created Jul 30, 2008, 9:25:21 AM
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
<%@ page import="java.text.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="sbml.test.sbmlTestselection" %>
<%@ page import="sbml.test.TestResultDetails" %>
<%@ page import="sbml.test.sbmlTestcase" %>
<%@ page import="java.io.*" %>

<%
Vector results = (Vector)request.getAttribute("tests");
%>
<%! 	TestResultDetails test;
	String name;
	String plot;
	String html;
	String description;
	String warnings;
	int result;
	int fail_count;
	int abort_count;
	int pass_count;
	int totalpoints;
	Vector<String> ctags;
	Vector<String> ttags;
	String[] totals = new String[3];
	Vector<String> failures = new Vector<String>();
	Vector<String> skips = new Vector<String>();
	
	Map<String, Integer> cmap = new HashMap<String, Integer>();
	Map<String, Integer> tmap = new HashMap<String, Integer>();
	
	SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy 'at' KK:mm a (z)");
	String now = sdf.format(new Date());
%>

<%@ include file="sbml-head.html"%>
<%@ include file="sbml-top.html"%>

<div id='pagetitle'><h1 class='pagetitle'>Outcome of tests</h1>
</div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>

<p> The archive of test results was successfully uploaded and analyzed on
<%= now.toCharArray() %>.
</p>

<p>
The following map summaries the outcome of comparing each uploaded test case
result to the expected results for that particular test case.  A <b><font
color="green">green</font></b> icon indicates it passed, a <b><font
color="darkred">red</font></b> icon indicates it failed, a <b><font
color="black">black</font></b> icon indicates a test was skipped, and a
<b><font color="#555">gray</font></b> icon is a space filler.
</p>

<p> You may click on any of the green, red or black icons in the map to see
details about the specific test case involved.  The information will be
presented in a new window.
</p>
	
<div style="margin: 2em 3em 2em 3em">
<form name="resultreport" action="http://sbml.org:8080/test_suite/web/report.jsp" method=post>
<TABLE class="borderless-table" CELLSPACING="0" WIDTH="90%" ALIGN="center">
<TR>
<%-- For each test in the test vector - get the testname, description, plot path, result --%>
	<%-- implement a counter and when counter mod 45 = 0 start a new row --%>
	
<%
	fail_count=0;
	abort_count=0;
	pass_count=0;
	cmap.clear();
	tmap.clear();
	failures.clear();
	skips.clear();
	
	for(int i=0;i<results.size() ; i++) {	
	  if(i % 45 ==0) {
		// start a new row
		out.println("</TR>");
		out.println("<TR>");
	  }
	
		test = (TestResultDetails)results.elementAt(i);
		name = test.getTestname();
		plot = test.getPlot();
		html = test.getHtml();
		description = test.getDescription();
		result = test.getResult();
		warnings = test.getWarnings();
		ctags = test.getCtags();
		ttags = test.getTtags();
		totalpoints = test.getTotalpoints();

		
	  	String s = "";
		for(int j=0;j<ctags.size();j++) {	
			s= s + ctags.elementAt(j) + ", ";
	  	}
	  	String t = "";
		for(int k=0;k<ttags.size();k++) {	
			t= t + ttags.elementAt(k) + ", ";
		}	
	
		if(result>0) {	
			for(int j=0;j<ctags.size();j++) {
				if(cmap.containsKey(ctags.elementAt(j))){
					cmap.put(ctags.elementAt(j),(cmap.get(ctags.elementAt(j))+1));
				}
				else{
					cmap.put((ctags.elementAt(j)),1);
				}
			}
			for(int l=0;l<ttags.size();l++) {
				if(tmap.containsKey(ttags.elementAt(l))){
					tmap.put(ttags.elementAt(l),(tmap.get(ttags.elementAt(l))+1));
				}
				else{
					tmap.put((ttags.elementAt(l)),1);
				}			
			} 
			out.println("<TD>");
			out.println("<a title=" + name + " href=\"/test-suite/web/testdetails.jsp?testname=" + name +"&result=" + result + "&plot=" + plot + "&description=" +description  + "&warnings=" + warnings + "&html=" +html + "&ctags=" +s + "&ttags=" +t + "&tpoints=" + totalpoints +"\" target=\"_blank\">");
			out.println("<IMG SRC=\"/test-suite/web/images/red.jpg\"  border=\"0\"/>");
			out.println("</a>");
			out.println("</TD>");
			fail_count++;
			failures.addElement(name +"," + description + "," + result + "," +totalpoints);
			
		}	
		if(result == 0) {			
	
			out.println("<TD>");
			out.println("<a title=" +name +" href=\"/test-suite/web/testdetails.jsp?testname=" + name +"&result=" + result + "&plot=" + plot + "&description=" +description + "&warnings=" + warnings + "&html=" +html + "&ctags=" +s + "&ttags=" +t + "&tpoints=" + totalpoints +"\" target=\"_blank\">");
			out.println("<IMG SRC=\"/test-suite/web/images/green.jpg\" border=\"0\"/>");
			out.println("</a>");
			out.println("</TD>");
			pass_count++;
		}
		if(result == -1) {			
	
			out.println("<TD>");
			out.println("<a title=" +name +" href=\"/test-suite/web/testdetails.jsp?testname=" + name +"&result=" + result + "&plot=" + plot + "&description=" +description + "&warnings=" + warnings + "&html=" +html + "&ctags=" +s + "&ttags=" +t + "&tpoints=" + totalpoints +"\" target=\"_blank\">");
			out.println("<IMG SRC=\"/test-suite/web/images/black.jpg\" border=\"0\"/>");
			out.println("</a>");
			out.println("</TD>");
			abort_count++;
			skips.addElement(name + "?" + description + "?" + warnings);
			
		}		
     } // end of for loop
     // if size of vector is not equally dividable by 45  - fill in in the remaining table with grey squares
     
     
	for(int m = results.size()%45; m<45; m++) {
			
		out.println("<TD BGCOLOR=\"white\">");
	//	out.println("<IMG SRC=\"/test-suite/web/images/grey.jpg\" border=\"0\"/>");
		
		out.println("</TD>");
     }	
%>	
</TR>
</TABLE> 

<p style="margin-top: 2em">
	Total number of test cases analyzed: <b><%=results.size()%></b>
</p>
<p>
	<IMG SRC="/test-suite/web/images/green.jpg" border="0" valign="top"/> Number of test cases passed: <%=pass_count%><BR>
	<IMG SRC="/test-suite/web/images/red.jpg"  border="0" valign="top"/> Number of test cases failed: <%=fail_count%><BR>
	<IMG SRC="/test-suite/web/images/black.jpg" border="0" valign="top"/> Number of test cases skipped: <%=abort_count%><BR>

<%	if(fail_count>0){
%>	
	<BR>
	Component tags and their associated counts in failed tests:<BR>

<%	Set set = cmap.entrySet();
	Iterator setIter = set.iterator();
	
	while(setIter.hasNext()) {
		out.println(setIter.next()+ "<BR>");
	}

%>	<BR>
	Test tags and their associated counts in failed tests: <BR>
<% 	Set tset = tmap.entrySet();
	Iterator tsetIter = tset.iterator();
	while(tsetIter.hasNext()) {
		out.println(tsetIter.next() + "<BR>");
	}
	}
%>
<%

	totals[0] = (String.valueOf(pass_count));
	totals[1] = (String.valueOf(fail_count));
	totals[2] = (String.valueOf(abort_count));

	session.setAttribute("totals",totals);
	session.setAttribute("failures",failures);
	session.setAttribute("skips",skips);

%>	

<%-- <form name="resultreport" action=<%=response.encodeURL("http://sbml.org/test-suite/web/report.jsp")%> method=post>
--%>
<input type="submit" value="View Report"> (The report summarizes the results in a more convenient format for printing.) 
</form>
</div>

<p> This concludes the test run.  To upload another set of results without
selecting and downloading a different set of test cases, please return to the
upload page, select another zip archive on your computer, and upload it.
<p>

<center style="margin:1em">
  <a href="/test-suite/web/uploadresults.jsp">
    <img border="0" align="center" src="http://sbml.org/images/8/83/Icon-red-left-arrow.jpg">
    Return to the test results upload page.
  </a>
</center>

<p> To start over afresh, please return to the front page of the Online SBML
Test Suite.
</p>

<center style="margin:1em">
  <a href="/Facilities/Online_SBML_Test_Suite">
    <img border="0" align="center" src="http://sbml.org/images/8/83/Icon-red-left-arrow.jpg">
    Return to the front page for the Online SBML Test Suite.
  </a>
</center>


<%@ include file="sbml-bottom.html"%>
