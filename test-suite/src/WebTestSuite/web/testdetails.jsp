<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="sbml.test.sbmlTestselection" %>
<%@ page import="sbml.test.TestResultDetails" %>
<%@ page import="sbml.test.sbmlTestcase" %>
<%@ page import="java.io.*" %>

<% 
	String testname = request.getParameter("testname"); 
	String result = request.getParameter("result");
	String plot = request.getParameter("plot");
	String description = request.getParameter("description");
	String warnings = request.getParameter("warnings");
%>

<HTML>
	<BODY>
	
	<CENTER><BIG>SBML Test <%=testname%> Details</BIG></CENTER>

	<IMG SRC="/test_suite/servlet/OpenFile?plot=<%=plot%>" width="300" height="200" align="left" ALT="plot">
	Synopsis: <%=description%><BR>
	Failed at <%=result%> points<BR>
<%	if(warnings != "") {
%>
	Test Aborted due to: <%=warnings%>
<% } %>
	
	

	

</BODY>
</HTML>
