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
		
	%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >
<head>
	<base href="http://localhost:8080/test_suite/web/">
	<title>Software/SBMLTestSuite - SBML.org</title>
	<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
	<meta name="keywords" content="Software/SBMLToolbox,Software/SBMLToolbox/SBMLToolbox License,Software/SBMLToolbox/SBMLToolbox Screenshots,Software/libSBML" />
	<link rel="shortcut icon" href="skins/common/images/favicon.ico" />
	<link rel="search" type="application/opensearchdescription+xml" href="opensearch_desc.php" title="SBML.org (English)" />
	<link rel='stylesheet' type='text/css' media='print' href='skins/common/wikiprintable.css?97' />                   
		<script type= "text/javascript">/*<![CDATA[*/
			var skin = "SBML";
			var stylepath = "skins";
			var wgArticlePath = "/$1";
			var wgScriptPath = "/index/";
			var wgScript = "index.php";
			var wgServer = "http://sbml.org";
			var wgCanonicalNamespace = "";
			var wgCanonicalSpecialPageName = false;
			var wgNamespaceNumber = 0;
			var wgPageName = "Software/SBMLToolbox";
			var wgTitle = "Software/SBMLToolbox";
			var wgAction = "view";
			var wgRestrictionEdit = [];
			var wgRestrictionMove = [];
			var wgArticleId = "317";
			var wgIsArticle = true;
			var wgUserName = null;
			var wgUserGroups = null;
			var wgUserLanguage = "en";
			var wgContentLanguage = "en";
			var wgBreakFrames = false;
			var wgCurRevisionId = "2013";  
		/*]]>*/</script>
		<script type="text/javascript" src="skins/common/wikibits.js?97"></script>

		<script type="text/javascript" src="skins/common/Common.js"></script>
		<script type="text/javascript" src="skins/SBML/corner/corner.js"></script>
		<script type="text/javascript" src="skins/SBML/rounded_corners_lite.inc.js"></script>
		<script type="text/javascript" src="skins/SBML/SBML.js"></script>                                 
		<!--[if IE 7]><link rel="stylesheet" type="text/css" href="skins/SBML/IE70Fixes.css" /><![endif]-->
	<style type='text/css'>
		/*/*/ /*<![CDATA[*/
		@import "skins/common/oldshared.css?97";
		@import "skins/SBML/sbml.css?97";

		/*]]>*/ /* */
	</style>    
	</head>         

<body bgcolor='#FFFFFF' class='ns-0 ltr page-Software_SBMLToolbox'>

	<div id='background'>
		<div id='topbar' class='iradius12'>
			<a name='top' href='/Main_Page'><img class='corner iradius12' src='skins/SBML/banner-3.jpg'></a>
		</div>                                                                                   
		<div id='navbar'>
			<table class='top' cellspacing='0' cellpadding='0'>
				<tr>
					<td class='navbar' valign='middle'>
						<div id='navbartext'><ul>
							<li><a href='/' title='SBML.org Home Page'><img src='skins/SBML/icon-sbml.png' align='middle' alt='SBML.org Home Page' border='0'/></a></li>
							<li><a href="/News" title="News">News</a></li>
							<li><a href="/Documents" title="Documents">Documents</a></li>
							<li><a href="/Downloads" title="Downloads">Downloads</a></li>
							<li><a href='/Forums' title='Forums'>Forums</a></li>
							<li><a href="/Facilities" title="Facilities">Facilities</a></li>

							<li><a href="/Community" title="Community">Community</a></li>
							<li><a href="/Events" title="Events">Events</a></li>
							<li><a href="/SBML.org:About" title="SBML.org:About">About</a></li>
							<li><a href='/index.php?title=News&action=feed' title='RSS Feed'><img src='skins/SBML/icon-rss.gif' width='13' height='13' align='top' alt='RSS Feed' border='0'/></a></li>
							</ul>   
						</div><!-- id='navbartext' --> 
						<div id='searchbox'><img src='skins/SBML/osx-searchbox/srch_l.gif' /><form class="searchform" method="get" action="/Special:Search"><input class='srch_fld' placeholder='Search...' autosave='applestyle_srch' results='5' onkeyup="applesearch.onChange('srch_fld','srch_clear')" name='search' /></form><img src='skins/SBML/osx-searchbox/srch_r.gif' /></div><!-- id='searchbox' -->
					</td> 
				</tr>      
			</table> 
		</div><!-- id='navbar' -->
		<div id='content' class='rounded12'>
			<div id='breadcrumbs'>
				<span class='breadcrumbs'>Parent pages: <a href='http://sbml.org'>SBML.org</a>&nbsp;/&nbsp;<a href="/Software" title="Software">Software</a></span>

			</div><!-- id='breadcrumbs' --> 
			<div id='article'>
				<div id='pagetitle'>
					<h1 class='pagetitle'>SBML TEST SUITE RESULTS</h1>
				</div><!-- id='pagetitle' -->
	
	<form name="resultreport" action="report.jsp" method=post>		
	<TABLE BORDER="0" CELLSPACING="0" WIDTH="80%" ALIGN="center">
	<TR>
	<%-- For each test in the test vector - get the testname, description, plot path, result --%>
		<%-- implement a counter and when counter mod 50 = 0 start a new row --%>
		
	<%
		fail_count=0;
		abort_count=0;
		pass_count=0;
		cmap.clear();
		tmap.clear();
		
		for(int i=0;i<results.size() ; i++) {	
		  if(i % 50==0) {
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
				out.println("<a href=\"/test_suite/web/testdetails.jsp?testname=" + name +"&result=" + result + "&plot=" + plot + "&description=" +description  + "&warnings=" + warnings + "&html=" +html + "&ctags=" +s + "&ttags=" +t + "&tpoints=" + totalpoints +"\" target=\"_blank\">");
				out.println("<IMG SRC=\"/test_suite/web/images/red.jpg\"  border=\"0\"/>");
				out.println("</a>");
				out.println("</TD>");
				fail_count++;
				failures.addElement(name +"," + description + "," + result + "," +totalpoints);
				
			}	
			if(result == 0) {			
		
				out.println("<TD>");
				out.println("<a href=\"/test_suite/web/testdetails.jsp?testname=" + name +"&result=" + result + "&plot=" + plot + "&description=" +description + "&warnings=" + warnings + "&html=" +html + "&ctags=" +s + "&ttags=" +t + "&tpoints=" + totalpoints +"\" target=\"_blank\">");
				out.println("<IMG SRC=\"/test_suite/web/images/green.jpg\" border=\"0\"/>");
				out.println("</a>");
				out.println("</TD>");
				pass_count++;
			}
			if(result == -1) {			
		
				out.println("<TD>");
				out.println("<a href=\"/test_suite/web/testdetails.jsp?testname=" + name +"&result=" + result + "&plot=" + plot + "&description=" +description + "&warnings=" + warnings + "&html=" +html + "&ctags=" +s + "&ttags=" +t + "&tpoints=" + totalpoints +"\" target=\"_blank\">");
				out.println("<IMG SRC=\"/test_suite/web/images/black.jpg\" border=\"0\"/>");
				out.println("</a>");
				out.println("</TD>");
				abort_count++;
				skips.addElement(name + "?" + description + "?" + warnings);
				
			}		
	     } // end of for loop
	     // if size of vector is not equally dividable by 30  - fill in in the remaining table with grey squares
	     
	     
		for(int m = results.size()%50; m<50; m++) {
				
			out.println("<TD>");
			out.println("<IMG SRC=\"/test_suite/web/images/grey.jpg\" border=\"0\"/>");
			out.println("</TD>");
	     }	

	

	%>	
	</TR>
	</TABLE> 
	<BR>
	<BR>
	Number of tests taken  : <%=results.size()%><BR>
	<IMG SRC="/test_suite/web/images/green.jpg" border="0"/>Number of test passed  : <%=pass_count%><BR>
	<IMG SRC="/test_suite/web/images/red.jpg"  border="0"/>Number of tests failed : <%=fail_count%><BR>
	<IMG SRC="/test_suite/web/images/black.jpg" border="0"/>Number of tests skipped: <%=abort_count%><BR>
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
%>	<BR>
<%

	totals[0] = (String.valueOf(pass_count));
	totals[1] = (String.valueOf(fail_count));
	totals[2] = (String.valueOf(abort_count));

	
	session.putValue("totals",totals);
	session.putValue("failures",failures);
	session.putValue("skips",skips);
	
%>	
	 <input type="submit" value="View Report">
	</form>
				</div><!-- id='article' --> 
			<br clear='all' />
			
			<div id='footer'>
				<div id='bottomlinks' class='rounded6'>
					<a href="index.php?title=Software/SBMLToolbox&amp;action=history" title="Software/SBMLToolbox">History</a>&nbsp;&nbsp;&#124;&nbsp;&nbsp;<a href="index.php?title=Special:Userlogin&amp;returnto=Software/SBMLToolbox" title="Special:Userlogin">Log in</a>
				</div><!-- id='bottomlinks' -->
	 			<br clear='all' />
					<div id='pagestats'>
				 		This page was last modified 01:06, 27 February 2008. 
					</div><!-- id='pagestats' -->                            
	 		</div><!-- id='footer' --> 
		</div><!-- id='content' --> 
		
		<div id='siteAttribution'>
			Please contact <email><b>sbml-team@sbml.org</b></email> with any questions or suggestions about this website.
		</div><!-- id='siteAttribution' -->  
		<div id='flashyicons'>
			<table class='icons'>
			   	<tr>
					<td class='icon'>
						<a class='mediawiki-icon' href='http://www.mediawiki.org' title='Powered by MediaWiki'>&nbsp;</a>
					</td><td class='icon'>
						<a class='php-icon' href='http://www.php.net' title='Powered by PHP'>&nbsp;</a>
					</td><td class='icon'>
						<a class='gnu-icon' href='http://www.gnu.org' title='Powered by GNU'>&nbsp;</a>

					</td><td class='icon'>
						<a class='linux-icon' href='http://www.linux.org' title='Powered by Linux'>&nbsp;</a>
					</td><td class='icon'>
						<a class='osi-icon' href='http://opensource.org' title='Open Source'>&nbsp;</a>
					</td><td class='icon'>
						<a class='rss-icon' href='/index.php?title=News&action=feed' title='RSS Feed'>&nbsp;</a>
					</td>   
				</tr>
			</table>   
		</div><!-- id='flashyicons' -->
	</div><!-- id='background' -->

			<script type="text/javascript">if (window.runOnloadHook) runOnloadHook();</script>

	</body>
</html>
