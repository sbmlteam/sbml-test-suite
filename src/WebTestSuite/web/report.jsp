<%-- 
    Document   : process.jsp
    Created on : Feb 27, 2008, 9:25:21 AM
    Author     : Kimberly Begley
    Description: JSP file to take user selections from html form.
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


<%

        String[] totals = (String[]) session.getValue("totals");
	Vector<String> failures = (Vector<String>) session.getValue("failures");
	Vector<String> skips = (Vector<String>) session.getValue("skips");

    
%>

<%
	Iterator it = failures.iterator(); 
	

	Iterator is = skips.iterator(); 
	
	
%>
    

    
    
    


<html>
   <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
       <title>SBML Test Suite Results Report Page</title>
   </head>
   <body>
	SBML Test Suite Report <BR>
	<BR>
<%
 out.println("Date:" +  new java.util.Date() + "<br>");
 %>
	TOTALS<BR>
	Number of passed cases:<%=totals[0]%><BR>
	Number of failed cases:<%=totals[1]%><BR>
	Number of skipped cases:<%=totals[2]%><BR>
	<BR>
	<BR>
	Failed Cases Detailed<BR>
	<TABLE border="1">
	<TR><TH>Case<TH>Synopsis<TH>Failed points count<TH>Total points</TR>
<%
	while(it.hasNext()) {
		String failed = (String)it.next();
		String[] field = failed.split(",");
%>		
		<TR><TD><%=field[0]%></TD><TD><%=field[1]%></TD><TD><%=field[2]%></TD><TD><%=field[3]%></TD></TR>
<%	}
%>	</TABLE>
<BR>
	<BR>
	Skipped Cases Detailed<BR>
	<TABLE border="1">
	<TR><TH>Case<TH>Synopsis<TH>Warnings</TR>
<%	while(is.hasNext()) {
		String skipped = (String)is.next();
		String[] sfield = skipped.split("\\?");
%>
		<TR><TD><%=sfield[0]%></TD><TD><%=sfield[1]%></TD><TD><%=sfield[2]%></TD></TR>	
<%	}
%>
	</TABLE>
	
   </body>
</html>


