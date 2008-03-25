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
%>

<HTML>
	<BODY>
	
	<CENTER><BIG>SBML Test <%=testname%> Details</BIG></CENTER>

	<IMG SRC="/test_suite/servlet/OpenFile?plot=<%=plot%>" align="left" ALT="plot">
	Syhopsis: <%=description%><BR>
	Failed at <%=result%> points<BR>
	
	

	

</BODY>
</HTML>
