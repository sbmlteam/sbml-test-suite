<%@ page import="java.util.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="sbml.test.sbmlTestselection" %>
<%@ page import="sbml.test.TestResultDetails" %>
<%@ page import="sbml.test.sbmlTestcase" %>
<%@ page import="java.io.*" %>
<%@ page import="java.net.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>

<% 
	String testname = request.getParameter("testname"); 
	String result = request.getParameter("result");
	String plot = request.getParameter("plot");
	String html = request.getParameter("html");
	String description = request.getParameter("description");
	String warnings = request.getParameter("warnings");
	String ctags = request.getParameter("ctags");
	String ttags = request.getParameter("ttags");
	String tpoints = request.getParameter("tpoints");
%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en" >
<head>
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
					<h1 class='pagetitle'>SBML TEST <%=testname%></h1>
				</div><!-- id='pagetitle' -->
				

	<p style="margin-left: 6px; margin-right: 6px">
	
<%
	int r = Integer.parseInt(result);
	if(r>0) {
%>
	Failed at <%=result%> of <%=tpoints%> points<BR>
<%	}
	if(r==0) {
%>
	Passed!<BR>
<%	}

	if(warnings != "") {
%>
	Test skipped due to: <%=warnings%><BR>
<% } %>
	Component Tags: <%=ctags%> <BR>
	Test Tags     : <%=ttags%> <BR>

	Synopsis: <%=description%><BR>
	</p>
	

	<IMG SRC="/test_suite/servlet/OpenFile?plot=<%=plot%>" align="center" ALT="plot"> 
	
	<%      String fileh = "file://" + html;
%>
	<c:import url="<%=fileh%>" />



	
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
