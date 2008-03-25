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

<jsp:useBean id="formHandler" class="sbml.test.FormBean" scope="request">
    <jsp:setProperty name="formHandler" property="*"/>
</jsp:useBean>

<%!  String[] levels;
     String[] testtype;
     String[] ctags;
     String[] ttags;
     Vector<String> levelvector;
     
     String testdir;
     String testdir_listing [];
     String test;
     
%>
end of line 25
<%
    levels = formHandler.getLevels();
    testtype = formHandler.getTesttype();
    ctags = formHandler.getCtags();
    ttags = formHandler.getTtags();

    
    sbmlTestselection t3 = new sbmlTestselection();
    String testdir = t3.getSbmlTestdir();

%>
<%--    testdir = "/home/kimberly/test-suite/cases/semantic"; --%>

<%    testdir = t3.getSbmlTestdir(); %>

just got the testdir it is <%= testdir %>

<%  File f = new File(testdir); 
     String mapfile;   
     testdir_listing = f.list();
%>


