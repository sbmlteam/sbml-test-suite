<%-- 
    Document   : process.jsp
    Created on : Feb 27, 2008, 9:25:21 AM
    Author     : Kimberly Begley
    Description: JSP file to take user selections from html form.
--%>

<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="sbml.test.sbmlTestselection" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*"%>

<jsp:useBean id="formHandler" class="sbml.test.FormBean" scope="request">
    <jsp:setProperty name="formHandler" property="*"/>
</jsp:useBean>

<%!  
     String[] testtype;
     String[] ctags;
     String[] ttags;
     String[] level;
     
     String testdir;
     String testdir_listing [];
     String test;
     
%>
end of line 25
<%
    level = formHandler.getLevels();
    testtype = formHandler.getTesttype();
    ctags = formHandler.getCtags();
    ttags = formHandler.getTtags();

    
    sbmlTestselection t3 = new sbmlTestselection();
    

%>
level is <%=level[0]%> testtype is <%=testtype[0]%> ctags are <%=ctags%> ttags are <%=ttags%>



<%  	String testdir = new String();
	    
	ServletContext context = getServletContext();
	InputStream is = context.getResourceAsStream("/WEB-INF/classes/sbml_config_file.txt");
	try{
		BufferedReader d = new BufferedReader(new InputStreamReader(is));
		String line; 
   		if  (   (  line = d.readLine (  )   )  != null  )   {  
     			testdir=line ; 
    		}  
	}
	finally{
		if(is != null) {
			is.close();
		}
	}
%>
testdir is <%=testdir%>

