<%-- 
 * @file    selecttests.jsp
 * @brief   JSP file to take user test case selections using an HTML form.
 * @author  Kimberly Begley and Michael Hucka
 * @date    Created Jul 29, 2008, 9:25:21 AM
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
<%@ page import="java.util.regex.*" %>
<%@ page import="java.awt.*" %>
<%@ page import="java.lang.*" %>
<%@ page import="sbml.test.sbmlTestselection" %>
<%@ page import="sbml.test.sbmlTestcase" %>
<%@ page import="java.io.*" %>
<%@ page import="javax.servlet.*" %>
<%@ page import="javax.servlet.http.*"%>
<%@ page import="javax.swing.*" %>

<%@ include file="sbml-head.html"%>

<script type="text/javascript">
<!--
function uncheck(field, both) {
  if (both) {
    uncheckAll(document.testselect.ctags);
    uncheckAll(document.testselect.ttags);
  } else {
    uncheckAll(field);
  }
  Validate();
}

function uncheckAll(field) {
  for (i = 0; i < field.length; i++) {
    field[i].checked = false;
  }
}

function checkme() {
  if (document.testselect.testtype.checked == false) {
      document.testselect.testtype.checked = true;
  }
}

function ValidateLevels() {

// for level1v2
  if (document.testselect.levels[0].checked == true) {

      document.testselect.ttags[0].checked = false;  //2D-Compartment
      document.testselect.ttags[0].disabled = true;
      document.testselect.ttags[1].checked = false;  // 1D-Compartment
      document.testselect.ttags[1].disabled = true;
      document.testselect.ttags[2].checked = false;  // 0D-Compartment
      document.testselect.ttags[2].disabled = true;
      document.testselect.ttags[3].disabled = false; // NonConstantCompartment
      document.testselect.ttags[4].disabled = false; // NonUnityCompartment
      document.testselect.ttags[5].disabled = false; // MulipleCompartment
      document.testselect.ttags[6].disabled = false; // InitialAmount
      document.testselect.ttags[7].checked = false;  // InitialConcentration
      document.testselect.ttags[7].disabled = true;
      document.testselect.ttags[8].checked = false;  //HasOnlySubstanceUnits
      document.testselect.ttags[8].disabled = true;
      document.testselect.ttags[9].disabled = false; // BoundaryCondition
      document.testselect.ttags[10].checked = false;  // ConstantSpecies
      document.testselect.ttags[10].disabled = true;
      document.testselect.ttags[11].disabled = false; // Non Constant Parameter
      document.testselect.ttags[12].disabled = false; // Fast Reaction
      document.testselect.ttags[13].disabled = false; // Reversible Reaction
      document.testselect.ttags[14].disabled = false; // Non Unit Stoichiometry
      document.testselect.ttags[16].checked = false;  // StoichiometryMath
      document.testselect.ttags[16].disabled = true;  // Local Parameter
      document.testselect.ttags[16].disabled = false; // Local Parameter
      document.testselect.ttags[17].checked = false;  // CSymbolTime
      document.testselect.ttags[17].disabled = true;

      document.testselect.ctags[0].checked = false;  // FUncitonDefinition
      document.testselect.ctags[0].disabled = true;
      document.testselect.ctags[1].checked = false;  // InitialAssignment
      document.testselect.ctags[1].disabled = true;
      document.testselect.ctags[2].disabled = false;
      document.testselect.ctags[3].disabled = false;
      document.testselect.ctags[4].disabled = false;
      document.testselect.ctags[5].checked = false;  // EventWithDelay
      document.testselect.ctags[5].disabled = true;
      document.testselect.ctags[6].checked = false;  // EventNoDelay
      document.testselect.ctags[6].disabled = true;
      document.testselect.ctags[7].disabled = false;
      document.testselect.ctags[8].disabled = false;
      document.testselect.ctags[9].disabled = false;
      document.testselect.ctags[10].disabled = false;
  }
  if (document.testselect.levels[1].checked == true) {

      document.testselect.ctags[0].disabled = false;
      document.testselect.ctags[1].checked = false;
      document.testselect.ctags[1].disabled = true;
      document.testselect.ctags[2].disabled = false;
      document.testselect.ctags[3].disabled = false;
      document.testselect.ctags[4].disabled = false;
      document.testselect.ctags[5].disabled = false;
      document.testselect.ctags[6].disabled = false;
      document.testselect.ctags[7].disabled = false;
      document.testselect.ctags[8].disabled = false;
      document.testselect.ctags[9].disabled = false;
      document.testselect.ctags[10].disabled = false;

     for(j=0;j<document.testselect.ttags.length;j++) {
                document.testselect.ttags[j].disabled = false;
        }

  }
  if(document.testselect.levels[2].checked == true) {
      var k=0;
      for(k=0;k<document.testselect.ttags.length;k++) {
                document.testselect.ttags[k].disabled = false;
        }
      var l=0;
      for(l=0;l<document.testselect.ctags.length;l++) {
                document.testselect.ctags[l].disabled = false;
        }


  }
  if(document.testselect.levels[3].checked == true) {
      for(i=0;i<document.testselect.ttags.length;i++) {
                document.testselect.ttags[i].disabled = false;
        }
      for(i=0;i<document.testselect.ctags.length;i++) {
                document.testselect.ctags[i].disabled = false;
        }


  }

}

function Validate() {

  if(document.testselect.ctags[7].checked == true && document.testselect.ctags[8].checked == false) {
          document.testselect.ctags[8].checked = true;
  }

  if(document.testselect.ctags[7].checked == true ) { 
  // If compartment is omitted then species is omitted and reaction is omitted
         document.testselect.ctags[8].checked = true;
         document.testselect.ctags[9].checked = true;
  }
  
  if(document.testselect.ctags[8].checked == true && document.testselect.ctags[9].checked == false) { 
  // If species is omitted then reaction is omitted
          document.testselect.ctags[9].checked = true;
  }
  
  if(document.testselect.ctags[7].checked == true) {i // Compartment
        document.testselect.ttags[0].checked = false; // 2D-Compartment
      	document.testselect.ttags[0].disabled = true;
	document.testselect.ttags[1].checked = false; // 1D-Compartment
	document.testselect.ttags[1].disabled = true;
	document.testselect.ttags[2].checked = false; // 0D-Compartment
	document.testselect.ttags[2].disabled = true;
	document.testselect.ttags[3].checked = false; // NonConstantCompartment
	document.testselect.ttags[3].disabled = true;
	document.testselect.ttags[4].checked = false; // NonUnityCompartment
	document.testselect.ttags[4].disabled = true;
	document.testselect.ttags[5].checked = false; // MultiCompartment
	document.testselect.ttags[5].disabled = true;
  }
  else if(document.testselect.ctags[7].checked == false && document.testselect.levels[0].checked == false) {
	document.testselect.ttags[0].disabled = false; 
	document.testselect.ttags[1].disabled = false;
	document.testselect.ttags[2].disabled = false;
	document.testselect.ttags[3].disabled = false;
	document.testselect.ttags[4].disabled = false;
	document.testselect.ttags[5].disabled = false;
  }
 else if(document.testselect.ctags[7].checked == false && document.testselect.levels[0].checked == true) { 
	document.testselect.ttags[0].disabled = true; 
	document.testselect.ttags[0].checked = false;
        document.testselect.ttags[1].disabled = true;  
	document.testselect.ttags[1].checked = false;      
       document.testselect.ttags[2].disabled = true;
	document.testselect.ttags[2].checked = false;

        document.testselect.ttags[3].disabled = false;
        document.testselect.ttags[4].disabled = false;
        document.testselect.ttags[5].disabled = false;

}


  if(document.testselect.ctags[8].checked == true) { // Species
	document.testselect.ttags[6].checked = false; // InitialAmount
	document.testselect.ttags[6].disabled = true;
	document.testselect.ttags[7].checked = false; // InitialConcentration
	document.testselect.ttags[7].disabled = true;
	document.testselect.ttags[8].checked = false; // HasOnlySubstanceUnits
	document.testselect.ttags[8].disabled = true;
	document.testselect.ttags[9].checked = false; // BoundaryCondition
	document.testselect.ttags[9].disabled = true;
	document.testselect.ttags[10].checked = false; // ConstantSpecies
	document.testselect.ttags[10].disabled = true;
  }
  else if(document.testselect.ctags[8].checked == false && document.testselect.levels[0].checked == false) {
	document.testselect.ttags[6].disabled = false;
	document.testselect.ttags[7].disabled = false;
	document.testselect.ttags[8].disabled = false;
	document.testselect.ttags[9].disabled = false;
	document.testselect.ttags[10].disabled = false;
  }
else if(document.testselect.ctags[8].checked == false && document.testselect.levels[0].checked == true) {
         document.testselect.ttags[6].disabled = false;
        document.testselect.ttags[10].disabled = false;
	document.testselect.ttags[9].disabled = false;
}
  if(document.testselect.ctags[9].checked == true) { // Reaction
	document.testselect.ttags[12].checked = false; // FastReaction
	document.testselect.ttags[12].disabled = true;
	document.testselect.ttags[13].checked = false; // ReversibleReaction
	document.testselect.ttags[13].disabled = true;
	document.testselect.ttags[14].checked = false; // NonUnityStoichiometry
	document.testselect.ttags[14].disabled = true;
	document.testselect.ttags[15].checked = false; // Stoichiometry
	document.testselect.ttags[15].disabled = true;
	document.testselect.ttags[16].checked = false; // LocalParameter
	document.testselect.ttags[16].disabled = true;
  }
  else if(document.testselect.ctags[9].checked == false && document.testselect.levels[0].checked == false) {
	document.testselect.ttags[12].disabled = false;
	document.testselect.ttags[13].disabled = false;
	document.testselect.ttags[14].disabled = false;
	document.testselect.ttags[15].disabled = false;
	document.testselect.ttags[16].disabled = false;
  }
else if(document.testselect.ctags[9].checked == false && document.testselect.levels[0].checked == true) {
        document.testselect.ttags[12].disabled = false;
        document.testselect.ttags[13].disabled = false;
        document.testselect.ttags[14].disabled = false;
	document.testselect.ttags[15].disabled = true;
        document.testselect.ttags[16].disabled = false;
  }
  if(document.testselect.ctags[10].checked == true) { // Parameter
	document.testselect.ttags[11].checked = false; // NonConstantParameter
 	document.testselect.ttags[11].disabled = true;
  }
  else if(document.testselect.ctags[10].checked == false) {
 	document.testselect.ttags[11].disabled = false;
  }
	



if (document.testselect.ctags[7].checked == true && document.testselect.ctags[8].checked == true && document.testselect.ctags[9].checked == true && document.testselect.ctags[10].checked == true) {
	// disabled all test tags
	for(m=0;m<document.testselect.ttags.length;m++) {
		document.testselect.ttags[m].checked = false;
		document.testselect.ttags[m].disabled = true;
	}

}
}



function checkscript() {
	if (document.testselect.ctags[9].checked == true && document.testselect.ctags[10].checked == true && document.testselect.ctags[11].checked == true && document.testselect.ctags[12].checked == true) {
		// something is wrong
		alert('Empty zip file. You cannot exclude all of Species, Reaction, Compartment and Parameter');
		return false;
	}

	// If the script makes it to here, everything is OK,
	// so you can submit the form

	return true;
}

//-->
</script>

<%@ include file="sbml-top.html"%>

<div id='pagetitle'><h1 class='pagetitle'>Step 1: Select SBML Tests </h1></div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>
<p>
The first step in using the online Test Suite is obtaining a set of test
models.  This page is designed to help you do that.  Once you have answered
the following questions and obtained a collection of test models, you will
next have to arrange to obtain and record the simulation results and store
them in files according to <a
href="/Software/SBML_Test_Suite/Step_2:_Running_the_tests">specific
guidelines</a> described separately.
</p>

<p> If you prefer, you can also skip the rest of this page and download the
entire test set as a single <a
href="http://sourceforge.net/project/showfiles.php?group_id=71971&package_id=90209">zip
archive</a>.
</p>

<form name="testselect" action="/test-suite/web/process.jsp" onsubmit="return checkscript()" method=post>

<p style="margin-top: 2em; padding-top: 1em; border-top: 1px solid rgb(170,
170, 170);"> To begin, please select the SBML Level and Version to use.  (The
following choices are mutually exclusive.)
</p>

<!-- 2008-07-29 mhucka@caltech.edu not yet available:
<h3>Class of tests</h3>
<p>
<table class="borderless-table" width="100%" style="padding-bottom: 1em;">	
  <tr>
    <td valign="top" style="padding: 0 0 1em 1em">
      <input type="checkbox" name="testtype" value="TimeCourse" onchange="checkme()" CHECKED >Time-course simulation
    </td>
  </tr>
</table>
</p>

<h3>SBML Level and Version</h3> 
<p>
-->
<p>
<table class="borderless-table" width="100%" style="padding: 1em 0 1em 0; margin-top: 1em">
  <tr>
    <td valign="top" width="35%" style="padding: 0 0 1em 1em">
      <em>SBML Level 1:</em><br>
      <input type="radio" name="levels" value="1.2" onchange="ValidateLevels()">Version 2
    </td>
    <td valign="top" colspan="2" style="padding-bottom: 1em">
      <em>SBML Level 2:</em><br>
      <input type="radio" name="levels" value="2.1" onchange="ValidateLevels()">Version 1<br>
      <input type="radio" name="levels" value="2.2" onchange="ValidateLevels()">Version 2<br>
      <input type="radio" name="levels" value="2.3" onchange="ValidateLevels()" CHECKED >Version 3
    </td>
  </tr>
</table>
</p>

<p style="margin-top: 1em; padding-top: 1em; border-top: 1px solid rgb(170,
170, 170);"> The default action is to provide you with all test cases
<em>unless</em> you specifically <em>exclude</em> some.  If you want to
exclude some tests (perhaps because you already know the software you are
testing does not support certain features), then using the check-boxes
below, please select all SBML components or types of tests that you want to
<em>exclude</em>.  Leave all boxes unchecked if you want to get all
possible tests.
</p>

<h3>Select the component tags you would like to exclude
  <a style="margin-left: 1.5em; font-style: normal;"
     href="javascript:uncheck(document.testselect.ctags, true)">[Clear all checked]</a>            
</h3>

<p>
<table class="borderless-table" width="100%"> 
  <tr>
    <td width="33%" valign="top" style="padding: 0 0 1em 1em">
        <input type="checkbox" name="ctags" value="FunctionDefinition" onchange="Validate()">Function definition<br> 
        <input type="checkbox" name="ctags" value="InitialAssignment" onchange="Validate()">Initial assignment<br>
        <input type="checkbox" name="ctags" value="AssignmentRule" onchange="Validate()">Assignment rules<br>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 1em 1em">
        <input type="checkbox" name="ctags" value="RateRule" onchange="Validate()">Rate rules<br>
        <input type="checkbox" name="ctags" value="AlgebraicRule" onchange="Validate()">Algebraic rules<br>
        <input type="checkbox" name="ctags" value="EventWithDelay" onchange="Validate()">Events with delays<br>
	<input type="checkbox" name="ctags" value="EventNoDelay" onchange="Validate()">Events without delays<br>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 1em 1em">
        <input type="checkbox" name="ctags" value="Compartment" onchange="Validate()">Compartments<br>
        <input type="checkbox" name="ctags" value="Species" onchange="Validate()">Species<br>
        <input type="checkbox" name="ctags" value="Reaction" onchange="Validate()">Reactions<br>
        <input type="checkbox" name="ctags" value="Parameter" onchange="Validate()">Parameters<br>
    </td>   
  </tr>
</table> 
</p>

<h3>Select the test tags you would like to exclude
  <a style="margin-left: 2em; font-style: normal;"
     href="javascript:uncheck(document.testselect.ttags, true)">[Clear all checked]</a>            
</h3>

<p>
<table class="borderless-table" width="100%">
  <tr>
    <td width="33%" valign="top" style="padding: 0 0 1em 1em">
        <input type="checkbox" name="ttags" value="2D-Compartment" onchange="Validate()">2-D compartments<br>
        <input type="checkbox" name="ttags" value="1D-Compartment" onchange="Validate()">1-D compartments<br>
        <input type="checkbox" name="ttags" value="0D-Compartment" onchange="Validate()">0-D compartments<br>
        <input type="checkbox" name="ttags" value="NonConstantCompartment" onchange="Validate()">Varying-size compartments<br>
        <input type="checkbox" name="ttags" value="NonUnityCompartment" onchange="Validate()">Compartments with size &ne; 1<br>
        <input type="checkbox" name="ttags" value="MultiCompartment" onchange="Validate()">Multiple compartments<br>
        <input type="checkbox" name="ttags" value="InitialAmount" onchange="Validate()">Species using initial amounts<br>
        <input type="checkbox" name="ttags" value="InitialConcentration" onchange="Validate()">Species using initial concentration</br>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 1em 1em">
        <input type="checkbox" name="ttags" value="HasOnlySubstanceUnits" onchange="Validate()">Species with <font size='-2'>'hasOnlySubstanceUnits'</font><br>
        <input type="checkbox" name="ttags" value="BoundaryCondition" onchange="Validate()">Species as boundary conditions<br>
        <input type="checkbox" name="ttags" value="ConstantSpecies" onchange="Validate()">Species declared as constant<br>
        <input type="checkbox" name="ttags" value="NonConstantParameter" onchange="Validate()">Parameters declared non-constant<br>
        <input type="checkbox" name="ttags" value="FastReaction" onchange="Validate()">'Fast' reactions<br>
        <input type="checkbox" name="ttags" value="ReversibleReaction" onchange="Validate()">Reversible reactions<br>
        <input type="checkbox" name="ttags" value="NonUnityStoichiometry" onchange="Validate()">Stoichiometries &ne; 1<br>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 1em 1em">
        <input type="checkbox" name="ttags" value="StoichiometryMath" onchange="Validate()">Use of formulas in stoichiometries<br>
        <input type="checkbox" name="ttags" value="LocalParameters" onchange="Validate()">Reactions with local parameters<br>
        <input type="checkbox" name="ttags" value="CSymbolTime" onchange="Validate()">Use of csymbol for 'time'<br>
    </td>         
  </tr>
</table> 
</p>

<p style="margin-top: 1em; border-top: 1px solid rgb(170,
170, 170);"> 
</p><p>
That's all!  Download a customized zip archive of the test cases you have
selected by clicking on the button below:
</p>

<center style="margin: 1.5em">
  <input type="submit" value="Get test cases">
</center>

</form> 

<p> Your browser should start downloading the zip archive immediately after
you click the button.  After it has finished downloading, unzip the archive
file into a directory on your computer and follow the instructions for
running the tests.
</p>

<p>
<center style="margin: 1.5em">
  <a href="/Software/SBML_Test_Suite/Step_2:_Running_the_tests">
    <img border="0" align="center" src="/images/e/ec/Icon-red-right-arrow.jpg">
    Go to the instructions for Step 2 (running the tests).
  </a>
</center>
</p>

<%@ include file="sbml-bottom.html"%>
