<%@ page import="java.util.*" %>
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
		String description;
		int result;
		int fail_count;
		
	%>

	<HTML>
	<BODY>
	
	<CENTER><BIG>SBML Test Suite Results</BIG></CENTER>
	<TABLE BORDER="0" CELLSPACING="0" WIDTH="80%" ALIGN="center">
	<TR>
	<%-- For each test in the test vector - get the testname, description, plot path, result --%>
		<%-- implement a counter and when counter mod 30 = 0 start a new row --%>
		
	<%
		fail_count=0;
		for(int i=0;i<results.size() ; i++) {	
		  if(i % 30 == 0) {
			// start a new row
			out.println("</TR>");
			out.println("<TR>");
		  }
		
			test = (TestResultDetails)results.elementAt(i);
			name = test.getTestname();
			plot = test.getPlot();
			description = test.getDescription();
			result = test.getResult();
		
		
			if(result>0) {			
		
				out.println("<TD>");
				out.println("<a href=\"/test_suite/web/testdetails.jsp?testname=" + name +"&result=" + result + "&plot=" + plot +"\" target=\"_blank\">");
				out.println("<IMG SRC=\"/test_suite/web/red.jpg\" border=\"0\"/>");
				out.println("</a>");
				out.println("</TD>");
				fail_count++;
			}	
			if(result == 0) {			
		
				out.println("<TD>");
				out.println("<a href=\"" + plot + "\" target=\"_parent\">");
				out.println("<IMG SRC=\"/test_suite/web/green.jpg\" border=\"0\"/>");
				out.println("</a>");
				out.println("</TD>");
			}	
	     } // end of for loop
	     // if size of vector is not equally dividable by 30  - fill in in the remaining table with grey squares
	     
	     for(int m = results.size()%30; m<31; m++) {
				
			out.println("<TD>");
			out.println("<IMG SRC=\"/test_suite/web/grey.jpg\" border=\"0\"/>");
			out.println("</TD>");
	     }	
	%>	
	</TR>
	</TABLE>
	<BR>
	<BR>
	Number of test taken : <%=results.size()%><BR>
	Number of test failed: <%=fail_count%>
	</BODY>
	</HTML>
