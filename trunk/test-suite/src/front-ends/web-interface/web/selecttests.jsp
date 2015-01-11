<%-- 
 * @file    selecttests.jsp
 * @brief   JSP file to take user test case selections using an HTML form.
 * @author  Michael Hucka
 * @author  Kimberly Begley
 * @date    Created Jul 29, 2008, 9:25:21 AM
 *
 * ----------------------------------------------------------------------------
 * This file is part of the SBML Test Suite.  Please visit http://sbml.org for
 * more information about SBML, and the latest version of the SBML Test Suite.
 *
 * Copyright (C) 2010-2015 jointly by the following organizations: 
 *     1. California Institute of Technology, Pasadena, CA, USA
 *     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
 *     3. University of Heidelberg, Heidelberg, Germany
 *
 * Copyright (C) 2008-2009 California Institute of Technology (USA).
 *
 * Copyright (C) 2004-2007 jointly by the following organizations:
 *     1. California Institute of Technology (USA) and
 *     2. University of Hertfordshire (UK).
 * 
 * The SBML Test Suite is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation.  A copy of the license
 * agreement is provided in the file named "LICENSE.txt" included with
 * this software distribution and also available on the Web at
 * http://sbml.org/Software/SBML_Test_Suite/License
 * ----------------------------------------------------------------------------
--%>

<%@ page import="java.io.*" %>
<%@ page import="java.util.*" %>
<%@ page import="sbml.test.*" %>

<%@ page errorPage="/web/error.jsp" %>

<%
// Start by logging that we've been invoked.

OnlineSTS.init();
OnlineSTS.logInvocation(request);

// Get our cases summary and related info.

ServletContext context = getServletContext();
File casesRootDir      = new File(context.getRealPath("/test-cases"));
CaseSummaryMap cases   = new CaseSummaryMap(casesRootDir);

int totalCases         = cases.getHighestCaseNumber();
Long[] uniqueTagCodes  = cases.getUniqueTagCombinations();

// The tag strings are actually hardwired into this logic of this web page
// (e.g., in the form below), but for list operations we work off the data
// gathered by the Java-based underlying case handling code.  There's
// obvious potential for mismatch.  This whole page probably needs to be
// done differently, maybe so the logic is done all in Java, but then I
// don't know how we would do the hand-organized layout of the forms
// tables.  I don't have time to change everything now.

Vector<String> knownTags = cases.getKnownTags();

// We can't access these vectors and arrays in Javascript directly.  We'll
// need to turn them into a format we can slurp into Javascript later.

StringBuffer knownTagNames = new StringBuffer();
for (String tag : knownTags)
  knownTagNames.append('"').append(tag).append("\",");

StringBuffer uniqueTagCodesValues = new StringBuffer();
for (Long tagBitCode : uniqueTagCodes)
  uniqueTagCodesValues.append('"').append(tagBitCode).append("\",");

// Read the date of the archive (which doubles as the version of this
// particular set of tests), so that we can advertise it.

File casesDateFile = new File(context.getRealPath("/.cases-archive-date"));

if (casesDateFile == null || ! casesDateFile.exists())
    OnlineSTS.logError(".cases-archive-date file doesn't appear to exist.");

if (! casesDateFile.canRead())
    OnlineSTS.logError(".cases-archive-date file is unreadable.");

if (! casesDateFile.isFile())
    OnlineSTS.logError(".cases-archive-date is not a file.");

Scanner fileReader = new Scanner(casesDateFile);
String casesDate   = null;

if (! fileReader.hasNext())
    OnlineSTS.logError(".cases-archive-date is empty.");
else
    casesDate = fileReader.nextLine();
%>

<%@ include file="sbml-head.html"%>

<script type="text/javascript" src="imported/dojo/dojo.js"></script>
<script src="http://cdn.jquerytools.org/1.2.5/full/jquery.tools.min.js"></script>
<script type="text/javascript">
/*<![CDATA[*/
// Convert some of our Java arrays into a form we can use in Javascript.

var allTagNames     = [ <%= knownTagNames %> ];
var uniqueTagCombos = [ <%= uniqueTagCodesValues %> ];

// Assign binary codes to every tag by their position in the array.
// The 1st tag is position 0 in a binary encoding, the 2nd tag is position 2,
// etc.  The result is something like this:
//   0000....0001 for "0D-Comparment"
//   0000....0010 for "Amount"
//   0000....0100 for "AssignedConstantStoichiometry"
//   ...etc...
// This code is stored as an integer in the "tagCodes" array.  This works
// because in Javascript, all integers are 64-bit.  We exchange them as
// long ints with the Java-based code.
//
// IMPORTANT * IMPORTANT * IMPORTANT * IMPORTANT * IMPORTANT * IMPORTANT
//
// Owing to Javacript's number representation format, which is always 64-bit
// floats, the largest integer it can reliably store is 15 digits long
// (9e15 = 205,891,132,094,649), according to
// http://www.hunlock.com/blogs/The_Complete_Javascript_Number_Reference
// The consequence of this for us is that the largest bit string we can
// store as an integer is 48 positions.  At the time of this writing, we
// have 41 tags.  If we ever need more than 48, we either have to change 
// the approach or use a "big integer" library.  Here are a couple:
// http://www-cs-students.stanford.edu/~tjw/jsbn/
// http://silentmatt.com/biginteger/
//
// IMPORTANT * IMPORTANT * IMPORTANT * IMPORTANT * IMPORTANT * IMPORTANT

function makeCodeArray()
{
  var codes = [];
  for (var i = 0; i < allTagNames.length; i++)
    codes[i] = Math.pow(2, i);
  return codes;
}

var allTagCodes = makeCodeArray();

function enableAllTags(tags)
{
  var span;

  for (var i = 0; i < tags.length; i++)
  {
    tags[i].disabled = false;
    span = document.getElementById(tags[i].value);
    if (span) span.style.removeProperty("color");
  }
}

function setEnabled(tagName, enabled, tags)
{
  // We will want to change the text attributes associated with the item:
  var span = document.getElementById(tagName);

  // Inefficient way of finding the item's index, but our lists are short:
  for (var i = 0; i < tags.length; i++)  
    if (tags[i].value == tagName)
    {
      tags[i].disabled = !enabled;
      if (enabled)
      {
        if (span) span.style.removeProperty("color");
      }
      else
      {
        tags[i].checked = false;        // "checked" means excluded
        if (span) span.style.setProperty("color", "#ccc", "");
      }
      return;
    }
}

function isEnabled(tagName, tags)
{
  for (var i = 0; i < tags.length; i++)  
    if (tags[i].value == tagName)
      return !tags[i].disabled;
}

/*
 * Sets an option to be excluded or not.
 * 
 * Our notion of "excluded" is implemented using the "checked" property
 * on the checkboxes in the form.
 *    (checked == true) means "excluded from testing"
 */
function setExcluded(tagName, state, tags)
{
  for (var i = 0; i < tags.length; i++)  
    if (tags[i].value == tagName)
    {
      if (!tags[i].disabled || state == false)
      {
        tags[i].checked = state;
      }
      return;
    }  
}

function isExcluded(tagName, tags)
{
  for (var i = 0; i < tags.length; i++)  
    if (tags[i].value == tagName)
      return (tags[i].checked || tags[i].disabled);
}

function resetAvailableTags()
{
  var ctags    = document.options.ctags;
  var ttags    = document.options.ttags;
  var packages = document.options.packages;

  var index = document.options.levelAndVersion.selectedIndex;

  enableAllTags(ctags);
  enableAllTags(ttags);
  enableAllTags(packages);

  switch (document.options.levelAndVersion[index].value)
  {
  case "1.2":
    // components
    setEnabled("FunctionDefinition",            false, ctags);
    setEnabled("InitialAssignment",             false, ctags);
    setEnabled("EventNoDelay",                  false, ctags);
    setEnabled("EventWithDelay",                false, ctags);
    setEnabled("EventPriority",                 false, ctags);
    setEnabled("CSymbolTime",                   false, ctags);
    setEnabled("CSymbolDelay",                  false, ctags);
    setEnabled("CSymbolAvogadro",               false, ctags);
    // tests
    setEnabled("0D-Compartment",                false, ttags);
    setEnabled("ConversionFactors",             false, ttags);
    setEnabled("Concentration",                 false, ttags);
    setEnabled("HasOnlySubstanceUnits",         false, ttags);
    setEnabled("ConstantSpecies",               false, ttags);
    setEnabled("AssignedConstantStoichiometry", false, ttags);
    setEnabled("AssignedVariableStoichiometry", false, ttags);
    setEnabled("RandomEventExecution",          false, ttags);
    setEnabled("SpeciesReferenceInMath",        false, ttags);
    setEnabled("EventIsNotPersistent",          false, ttags);
    setEnabled("EventIsPersistent",             false, ttags);
    setEnabled("EventUsesTriggerTimeValues",    false, ttags);
    setEnabled("EventUsesAssignmentTimeValues", false, ttags);
    setEnabled("EventT0Firing",                 false, ttags);
    // packages
    setEnabled("comp",                          false, packages);
    setEnabled("fbc",                           false, packages);
    break;

  case "2.1":
    // components
    setEnabled("InitialAssignment",             false, ctags);
    setEnabled("EventPriority",                 false, ctags);
    setEnabled("CSymbolAvogadro",               false, ctags);
    setEnabled("ConversionFactors",             false, ttags);
    // tests
    setEnabled("RandomEventExecution",          false, ttags);
    setEnabled("SpeciesReferenceInMath",        false, ttags);
    setEnabled("EventIsNotPersistent",          false, ttags);
    setEnabled("EventUsesAssignmentTimeValues", false, ttags);
    setEnabled("EventT0Firing",                 false, ttags);
    // packages
    setEnabled("comp",                          false, packages);
    setEnabled("fbc",                           false, packages);
    break;

  case "2.2":
  case "2.3":
    // components
    setEnabled("EventPriority",                 false, ctags);
    setEnabled("CSymbolAvogadro",               false, ctags);
    // tests
    setEnabled("ConversionFactors",             false, ttags);
    setEnabled("RandomEventExecution",          false, ttags);
    setEnabled("SpeciesReferenceInMath",        false, ttags);
    setEnabled("EventIsNotPersistent",          false, ttags);
    setEnabled("EventUsesAssignmentTimeValues", false, ttags);
    setEnabled("EventT0Firing",                 false, ttags);
    // packages
    setEnabled("comp",                          false, packages);
    setEnabled("fbc",                           false, packages);
    break;

  case "2.4":
    // components
    setEnabled("EventPriority",                 false, ctags);
    setEnabled("CSymbolAvogadro",               false, ctags);
    // tests
    setEnabled("ConversionFactors",             false, ttags);
    setEnabled("RandomEventExecution",          false, ttags);
    setEnabled("SpeciesReferenceInMath",        false, ttags);
    setEnabled("EventIsNotPersistent",          false, ttags);
    setEnabled("EventT0Firing",                 false, ttags);
    // packages
    setEnabled("comp",                          false, packages);
    setEnabled("fbc",                           false, packages);
    break;

  case "3.1":
    // components
    setEnabled("StoichiometryMath",             false, ctags);
    break;
  }
  warn("", "init");
  propagate();
}

/*
 * propagate()
 * 
 * Called when a tag is checked (= excluded) to propagate dependencies.
 * This is admittedly inefficient because every call to isExcluded()
 * invokes a loop across all the tag elements, but we are dealing with so
 * few tags that it's not an issue.  More important is for this code to be
 * easily understandable and maintainable.
 */
function propagate()
{
  var index    = document.options.levelAndVersion.selectedIndex;
  var lv       = document.options.levelAndVersion[index].value;
  var ctags    = document.options.ctags;
  var ttags    = document.options.ttags;

  if (isExcluded("Compartment", ctags))
  {
    // If compartment is excluded, there can't be species or reactions...
    setExcluded("Species",                true, ctags);
    setExcluded("Reaction",               true, ctags);
    // ... and certain tests become unavailable too.
    setExcluded("0D-Compartment",         true, ttags);
    setExcluded("NonConstantCompartment", true, ttags);
    setExcluded("NonUnityCompartment",    true, ttags);
    setExcluded("MultiCompartment",       true, ttags);
  }

  if (isExcluded("Species", ctags))
  {
    // If species is excluded, there can't be any reactions...
    setExcluded("Reaction",               true, ctags);
    // ... and we can't talk about various species properties.
    setExcluded("ConversionFactors",      true, ttags);
    setExcluded("Amount",                 true, ttags);
    setExcluded("Concentration",          true, ttags);
    setExcluded("HasOnlySubstanceUnits",  true, ttags);
    setExcluded("BoundaryCondition",      true, ttags);
    setExcluded("ConstantSpecies",        true, ttags);
  }

  if (isExcluded("Reaction", ctags))
  {
    setExcluded("ConversionFactors",      true, ttags);
    setExcluded("FastReaction",           true, ttags);
    setExcluded("ReversibleReaction",     true, ttags);
    setExcluded("NonUnityStoichiometry",  true, ttags);
    setExcluded("AssignedVariableStoichiometry", true, ttags);
    setExcluded("AssignedConstantStoichiometry", true, ttags);
    setExcluded("LocalParameters",        true, ttags);
    setExcluded("SpeciesReferenceInMath", true, ttags);
  }

  if (isExcluded("Parameter", ctags))
  {
    setExcluded("ConversionFactors",      true, ttags);
    setExcluded("NonConstantParameter",   true, ttags);
    setExcluded("LocalParameters",        true, ttags);
  }

  if (isExcluded("AssignmentRule", ctags) && isExcluded("AlgebraicRule", ctags)
      && isExcluded("RateRule", ctags))
  {
    // In 1.2, where there are no events, if you also don't have reactions,
    // then you can't do anything at all.

    if (lv == "1.2" && isExcluded("Reaction", ctags))
    {
      document.options.submit.disabled = true;
      warn("not-available", "hide"); // Just in case.
      warn("no-components", "hide"); // Just in case.
      warn("no-components-l1v2", "show");
    }
    else if (lv == "1.2" || (isExcluded("EventNoDelay", ctags)
                             && isExcluded("EventWithDelay", ctags)
                             && isExcluded("EventPriority", ctags)))
    {
      // If all rules and all events are removed, then one can't construct
      // a model with variable compartments or parameter values, or test

      setExcluded("NonConstantCompartment",        true, ttags);
      setExcluded("NonConstantParameter",          true, ttags);
      setExcluded("InitialValueReassigned",        true, ttags);
    }
  }

  // If there are no parameters, species or compartments, it's impossible
  // to test certain other SBML components.

  if (isExcluded("Parameter", ctags) && isExcluded("Compartment", ctags)
      && isExcluded("Species", ctags))
  {
    setExcluded("InitialAssignment",             true, ctags);
    setExcluded("AssignmentRule",                true, ctags);
    setExcluded("RateRule",                      true, ctags);
    setExcluded("EventWithDelay",                true, ctags);
    setExcluded("EventNoDelay",                  true, ctags);
    setExcluded("EventPriority",                 true, ctags);
  }

  // If InitialAssignment is excluded, AssignedConstantStoichiometry in
  // Level 3 isn't tested.

  if (isExcluded("InitialAssignment", ctags) && lv == "3.1")
  {
    setExcluded("AssignedConstantStoichiometry", true, ttags);
  }

  // If none of the following are present, you can't test the use of
  // function definitions either.

  if (isExcluded("InitialAssignment", ctags) && isExcluded("RateRule", ctags)
      && isExcluded("AssignmentRule", ctags) && isExcluded("AlgebraicRule", ctags)
      && isExcluded("EventNoDelay", ctags) && isExcluded("EventWithDelay", ctags)
      && isExcluded("EventPriority", ctags)
      && isExcluded("Reaction", ctags))
  {
    setExcluded("FunctionDefinition",     true, ctags);    
  }

  // If no event objects are defined, then event tests can't be done.

  if (isExcluded("EventNoDelay", ctags)
      && isExcluded("EventWithDelay", ctags))
  {
      setExcluded("EventPriority",                 true, ctags);
      setExcluded("RandomEventExecution",          true, ttags);
      setExcluded("EventUsesTriggerTimeValues",    true, ttags);
      setExcluded("EventUsesAssignmentTimeValues", true, ttags);
      setExcluded("EventIsPersistent",             true, ttags);
      setExcluded("EventIsNotPersistent",          true, ttags);
      setExcluded("EventT0Firing",                 true, ttags);
  }
  
  // If certain conditions hold, we can't test anything.  To be more helpful,
  // we do a special case for excluding param, species, comp, & reactions.

  if (isExcluded("Parameter", ctags) && isExcluded("Compartment", ctags)
      && isExcluded("Species", ctags) && isExcluded("Reaction", ctags))
  {
      document.options.submit.disabled = true;
      warn("not-available", "hide"); // Just in case.
      warn("no-components-l1v2", "hide"); // Just in case.
      warn("no-components", "show");
  }
  else
  {
      // For other possible combinations that leave nothing, we do generic
      // processing & generic messages.

      var left = uniqueTagCombos.length;
outerLoop:
      for (var i = 0; i < uniqueTagCombos.length; i++)
          for (var j = 0; j < allTagNames.length; j++)
              if (isExcluded(allTagNames[j], ctags) || isExcluded(allTagNames[j], ttags))
                  if (bitwiseAnd(allTagCodes[j], uniqueTagCombos[i]))
                  {
                      --left;
                      continue outerLoop;
                  }

      if (left <= 0)
      {
          document.options.submit.disabled = true;
          warn("no-components-l1v2", "hide");
          warn("no-components", "hide");
          warn("not-available", "show");
      }
      else if (document.options.submit.disabled) // State resets.
      {
          document.options.submit.disabled = false;
          warn("no-components-l1v2", "hide");
          warn("no-components", "hide");
          warn("not-available", "hide");
      }
  }
}

/*
 * Bitwise AND operation that works for more than 32 bits.
 * Obtained from a posting by user "palswim" at
 * http://stackoverflow.com/questions/3637702/how-to-do-bitwise-and-in-javascript-on-variables-that-are-longer-than-32-bit
 */
function bitwiseAnd(val1, val2)
{
    var shift = 0, result = 0;
    var mask = ~((~0) << 30); 
    var divisor = 1 << 30; 
    while ((val1 != 0) && (val2 != 0))
    {
        var rs = (mask & val1) & (mask & val2);
        val1 = Math.floor(val1 / divisor); // val1 >>> 30
        val2 = Math.floor(val2 / divisor); // val2 >>> 30
        for (var i = shift++; i--;)
            rs *= divisor;              // rs << 30
        result += rs;
    }
    return result;
}

/*
 * The forms built-in "reset" action clears the checkboxes, but also
 * messes with the user's menubox selection, which is not right.  So
 * we need our own custom extra bit of handling code.
 */
function resetAll(ask)
{
  if (ask && ! confirm("Online SBML Test Suite: Clear all selections and reset the form?"))
    return false;

  var lv = document.options.levelAndVersion;
  var selected = lv.selectedIndex;
  for (var i = 0; i < lv.length; i++)
    lv[i].defaultSelected = false;
  lv[selected].defaultSelected = true;

  resetAvailableTags();

  // Hide the warning panel and reenable the "get test cases" button.
  warn("", "init");
  document.options.submit.disabled = false;

  // Hide the link to the next step.
  changeNextLink('hide');

  return true;
}

function resetAllUnconditionally()
{
  return resetAll(false);
}

dojo.require("dojo.fx");

/*
 * Note about "init" action: I haven't found a way to make the slide
 * warning be hidden when the page is first visited except to stick a
 * method call on the "onload" property.  This method is attached via a
 * "hookEvent" call below.
 */
function warn(which, action)
{
  var id, param;
  
  switch (which)
  {
  case "not-available":
    id = "warningNotAvailable";
    param = {node: "warningNotAvailable", duration: 200};
    break;

  case "no-components-l1v2":
    id = "warningNoneLeftL1v2";
    param = {node: "warningNoneLeftL1v2", duration: 200};
    break;

  default:
  case "no-components":
    id = "warningNoneLeft"
    param = {node: "warningNoneLeft", duration: 200};
    break;
  }

  switch (action)
  {
  case "show":
    dojo.fx.wipeIn(param).play();
    break;

  case "hide":
    // Avoid spurious jump if we try to hide it and it's already hidden.
    // dojo.fx hides the div by setting its display property to "none".
    //
    var prop = document.getElementById(id).style;
    if (prop.getPropertyValue("display") != "none")
      dojo.fx.wipeOut(param).play();
    break;

  case "init":
  default:
    dojo.fx.wipeOut({node: "warningNotAvailable", duration: 0}).play();
    dojo.fx.wipeOut({node: "warningNoneLeft", duration: 0}).play();
    dojo.fx.wipeOut({node: "warningNoneLeftL1v2", duration: 0}).play();
    break;
  }
}

function changeNextLink(showOrHide)
{
  var elem = document.getElementById('nextStepInstructions');

  switch (showOrHide)
  {
  case "show": elem.style.setProperty("display", "block", ""); break;
  case "hide": elem.style.setProperty("display", "none", ""); break;
  }
}

hookEvent("load", resetAllUnconditionally);
/*]]>*/
</script>

<style type="text/css">
.warningBox {
  font-weight: bold;
  color: darkred;
  background-color: #ffeeee;
  width: 500px;
  border: 1px solid #BABABA;
  padding: 5px 10px;
  margin: 10px auto 1em auto;
  -o-border-radius: 10px;
  -moz-border-radius: 12px;
  -webkit-border-radius: 10px;
  -webkit-box-shadow: 0px 3px 7px #adadad;
  border-radius: 10px;
  -moz-box-sizing: border-box;
  -opera-sizing: border-box;
  -webkit-box-sizing: border-box;
  -khtml-box-sizing: border-box;
  box-sizing: border-box;
  overflow: hidden;
}

.hiddenText {
  display: none;
}

.tooltip {
  display: none;
  background: transparent url(http://sbml.org/skins/SBML/white_arrow.png);
  font-size: 11px;
  line-height: 120%;
  height: 90px;
  width: 180px;
  padding: 13px;
  color: black;
}

.gray-back {
  background-color: #ddd;
  color: black;
}

.white-back {
  background-color: white;
}

.smaller-font {
  font-size: 8pt !important;
}
</style>

<%@ include file="sbml-top.html"%>

<div id='pagetitle'><h1 class='pagetitle'>Step 1: Select SBML Tests </h1></div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="<%=OnlineSTS.getImageURL(request)%>/Icon-online-test-suite-64px.jpg" border="0"/>
</div>
<p>
The first step in using the online Test Suite is to obtain a set of test
models using the form on this page.  Once you have answered the questions
below and downloaded a set of tests, you must then record the
results of simulating each model in your application and put the results in
files according to the guidelines in <a
href="<%=OnlineSTS.getHomeURL(request)%>/Step_2:_Running_the_tests">step 2</a>.
</p>

<p> You can also skip this page and download the
entire <%=totalCases%> cases (version of <%=casesDate%>)
as a <a href="http://sourceforge.net/projects/sbml/files/test-suite">zip
archive</a>.
</p>

<form action="<%=OnlineSTS.getServiceRootURL(request)%>/web/process-selections.jsp"
      name="options" method="post">

<p style="margin-top: 1.5em; padding-top: 0.5em; border-top: 1px solid #999"> 

<h3>(a) Select the SBML Level and Version to use:
   <select name="levelAndVersion" onchange="resetAvailableTags()">
     <optgroup label="SBML Level 1">
       <option value="1.2">SBML Level 1 Version 2</option>
     </optgroup>
     <optgroup label="SBML Level 2">
       <option value="2.1">SBML Level 2 Version 1</option>
       <option value="2.2">SBML Level 2 Version 2</option>
       <option value="2.3">SBML Level 2 Version 3</option>
       <option value="2.4">SBML Level 2 Version 4</option>
     </optgroup>
     <optgroup label="SBML Level 3">
       <option value="3.1" selected>SBML Level 3 Version 1 Core</option>
     </optgroup>
   </select>
</h3>

<p>By default, you will be provided with all test cases <em>unless</em>
you specifically <em>exclude</em> some.  To exclude cases (perhaps
because you already know the software you are testing doesn't support
certain features), use the following check-boxes to select the SBML
components or types of tests that you want <em>excluded</em>.  Leave all
boxes unchecked to get all possible tests.
</p>

<p> Dependencies exist: when certain components are excluded, some tests
must be excluded too, and remain so while the relevant components are
selected for exclusion.  In addition, not all components are available in
all SBML Levels/Versions.  </p>

<h3 style="margin-top: 1.5em">(b) Select the SBML component tags you would like to <u>exclude</u>:</h3>

<p>
<table class="borderless-table smaller-font" width="100%"> 
  <tr>
    <td width="30%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ctags" onchange="propagate()" value="AssignmentRule" />
            <span id="AssignmentRule">Assignment rules</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="RateRule" />
            <span id="RateRule">Rate rules</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="AlgebraicRule" />
            <span id="AlgebraicRule">Algebraic rules</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="FunctionDefinition" />
            <span id="FunctionDefinition">Function definition</span><br/> 
        <input type="checkbox" name="ctags" onchange="propagate()" value="InitialAssignment" />
            <span id="InitialAssignment">Initial assignment</span><br/>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ctags" onchange="propagate()" value="EventNoDelay" />
            <span id="EventNoDelay">Events without delays</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="EventWithDelay" />
            <span id="EventWithDelay">Events with delays</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="EventPriority" />
            <span id="EventPriority">Priorities for event triggers</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="CSymbolDelay" />
            <span id="CSymbolDelay">&lt;csymbol&gt; for 'delay'</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="CSymbolTime" />
            <span id="CSymbolTime">&lt;csymbol&gt; for 'time'</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="CSymbolAvogadro" />
            <span id="CSymbolAvogadro">&lt;csymbol&gt; for 'avogadro'</span><br/>
       </td>
    <td width="33%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ctags" onchange="propagate()" value="StoichiometryMath" />
            <span id="StoichiometryMath">&lt;stoichiometryMath&gt;</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="Compartment" />
            <span id="Compartment">Compartments</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="Species" />
            <span id="Species">Species</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="Reaction" />
            <span id="Reaction">Reactions</span><br/>
        <input type="checkbox" name="ctags" onchange="propagate()" value="Parameter" />
            <span id="Parameter">Parameters</span><br/>
    </td>   
  </tr>
</table> 

<h3>(c) Select the test tags you would like to <u>exclude</u>: <span style="font-weight: normal">(Hover over items for short explanations)</span></h3>

<p>
<div id="tags">
<table class="borderless-table smaller-font" width="100%">
  <tr>
    <td width="30%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ttags" onchange="propagate()" value="InitialValueReassigned"/>
            <span id="InitialValueReassigned" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The initial value of a species, compartment, parameter, or (in SBML Level 3) species reference, is determined by an initial assignment, assignment rule, or algebraic rule."
            >Initial entity values overriden</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="NonConstantParameter"/>
            <span id="NonConstantParameter" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains at least one parameter whose value changes over the course of the simulation."
            >Varying parameter values</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="LocalParameters"/>
            <span id="LocalParameters" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains a kinetic law with a 'listOfParameters' (SBML Levels 1&ndash;2) or 'listOfLocalParameters' (Level 3)."
            >Local parameters in reactions</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="ConversionFactors"/>
            <span id="ConversionFactors" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model itself, or a species it contains, has a value set for the 'conversionFactor' attribute."
            >Conversion factors</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="0D-Compartment"/>
            <span id="0D-Compartment" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	     title="A compartment with a value of 0 for its 'spatialDimensions' attribute is present in the model."
            >0-D compartments</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="NonConstantCompartment"/>
            <span id="NonConstantCompartment" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains at least one compartment whose size changes over the course of the simulation."
            >Varying-size compartments</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="NonUnityCompartment"/>
            <span id="NonUnityCompartment" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains at least one compartment whose size is not 1.0 for at least part of the duration of the simulation."
            >Compartments with size &ne; 1</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="MultiCompartment"/>
            <span id="MultiCompartment" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="There is more than one compartment in the model."
            >Multiple compartments</span><br/>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ttags" onchange="propagate()" value="HasOnlySubstanceUnits"/>
            <span id="HasOnlySubstanceUnits" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="A species with an attribute value of 'true' for 'hasOnlySubstanceUnits' is present in the model."
            >Species with 'hasOnlySubstanceUnits'</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="BoundaryCondition"/>
            <span id="BoundaryCondition" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="A species with a value of 'true' for its 'boundaryCondition' attribute is present in the model."
            >Species as boundary conditions</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="ConstantSpecies"/>
            <span id="ConstantSpecies" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="A species with a value of 'true' for its 'constant' attribute is present in the model."
            >Species declared as constant</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="FastReaction"/>
            <span id="FastReaction" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="A reaction with a value of 'true' for its 'fast' attribute is present in the model."
            >'Fast' reactions</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="ReversibleReaction"/>
            <span id="ReversibleReaction" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains at least one reaction whose kinetic law produces a negative value for at least part of the duration of the simulation."
            >Reversible reactions</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="NonUnityStoichiometry"/>
            <span id="NonUnityStoichiometry" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains at least one species reference with a stoichiometry value that is not 1.0 for at least part of the duration of the simulation."
            >Stoichiometries &ne; 1</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="AssignedConstantStoichiometry"/>
            <span id="AssignedConstantStoichiometry" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains a stoichiometry that is set by 'stoichiometryMath' (Level 2) or an initial assignment or rule (Level 3), but thereafter the value remains constant."
            >Assigned constant stoichiometries</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="AssignedVariableStoichiometry"/>
            <span id="AssignedVariableStoichiometry" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains a stoichiometry that is set by 'stoichiometryMath' (Level 2) or an initial assignment, rule or event (Level 3), and the value is changed during simulation."
            >Assigned variable stoichiometries</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="SpeciesReferenceInMath"/>
            <span id="SpeciesReferenceInMath" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains at least one species reference with an identifier that is used in a mathematical formula elsewhere in the model."
            >Species reference id used in math</span><br/>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ttags" onchange="propagate()" value="RandomEventExecution"/>
            <span id="RandomEventExecution" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="Two or more events execute at the same moment in time, and have, at that moment, identical priorities, so correct simulation requires randomly choosing which event to execute first."
            >Random event execution</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="EventUsesTriggerTimeValues"/>
            <span id="EventUsesTriggerTimeValues" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="At least one event has a 'useValuesFromTriggerTime' attribute set to 'true' (Level 2 V.4 or later), or is assumed to use trigger time values by default (Level 2 prior to V.4)."
            >Event uses trigger-time values</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="EventUsesAssignmentTimeValues"/>
            <span id="EventUsesAssignmentTimeValues" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The model contains at least one event whose 'useValuesFromTriggerTime' attribute is set to 'false', and this makes a difference in the output of the simulation."
            >Event uses assignment-time values</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="EventIsPersistent"/>
            <span id="EventIsPersistent" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The simulation behavior of the model is influenced by an event that has a value of 'true' for its 'persistent' attribute (Level 3) or is assumed to be persistent by default (Level 2)."
            >Persistent events</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="EventIsNotPersistent"/>
            <span id="EventIsNotPersistent" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The simulation behavior of the model is influenced by an event that has a value of 'false' for its 'persistent' attribute (Level 3 only)."
            >Non-persistent events</span><br/>
        <input type="checkbox" name="ttags"  onchange="propagate()" value="EventT0Firing"/>
            <span id="EventT0Firing" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="An event has a trigger that evaluates to 'true' at time 0, and whether it fires at time 0 (possible in SBML Level 3; impossible in Level 2) makes a difference to the simulation."
            >Event triggers at time = 0</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="Amount"/>
            <span id="Amount" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The value of at least one species is requested to be output in units of amount, not concentration."
            >Species using initial amounts</span><br/>
        <input type="checkbox" name="ttags" onchange="propagate()" value="Concentration"/>
            <span id="Concentration" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="The value of at least one species is requested to be output in units of concentration, not amount."
            >Species using initial concentration</span><br/>
    </td>         
  </tr>
</table> 
</div>

<!-- 2014-06-01 [mhucka@caltech.edu] disabled because results processing
isn't implemented yet.

<h3>(d) Select additional optional test cases for SBML Level 3 packages you would like to <u>include</u>:</h3>

<p>
<div id="tags">
<table class="borderless-table smaller-font" width="100%">
  <tr>
    <td width="30%" valign="top" style="padding: 0 0 0 1em">
        <input type="hidden" name="packages" value="core"/>
        <input type="checkbox" name="packages" onchange="propagate()" value="comp"/>
            <span id="comp" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="Include test cases for the SBML Level 3 Hierarchical Model Composition package."
            >SBML Level 3 Hierarchical Model Composition</span><br/>
        <input type="checkbox" name="packages" onchange="propagate()" value="fbc"/>
            <span id="fbc" onmouseover="this.className='gray-back'" onmouseout="this.className='white-back'"
	    title="Include test cases for the SBML Level 3 Flux Balance Constraints package."
            >SBML Level 3 Flux Balance Constraints</span><br/>
    </td>         
  </tr>
</table> 
</div>
-->
<!-- 2014-06-01 [mhucka@caltech.edu] the following are hidden fake entries so 
that the forms on this page still work.
-->
  <input type="hidden" name="packages" value="core"/>
  <input type="hidden" name="packages" value="comp"/>
  <input type="hidden" name="packages" value="fbc"/>
<!-- 2014-06-01 [mhucka@caltech.edu] End of fake hidden fields -->


<p style="margin-top: 1.5em">
When you are finished excluding tests, click the <i>Get test cases</i> button to download a
zip archive of the test cases left:
</p>

<div id="warningNoneLeft" class="warningBox"
     title="Warning&mdash;all possible components have been excluded">
If compartments, species, reactions and parameters are <i>all</i>
excluded, then there are no possible test cases left.  Please deselect
some of the component tags before proceeding.
</div>
<div id="warningNoneLeftL1v2" class="warningBox"
     title="Warning&mdash;all possible components have been excluded">
In SBML Level 1, if reactions are exluded along with all types of rules,
then there are no possible test cases left.  Please deselect
some of the component tags before proceeding.
</div>
<div id="warningNotAvailable" class="warningBox"
     title="Warning&mdash;combination currently not supported">
We regret that the current Test Suite does not yet
cover this particular combination.  We are continually
expanding the test coverage and hope to support this combination in
the future.  In the mean time, please deselect some of the check
boxes before proceeding.
</div>

<center style="margin: 1.5em auto" class="ie-submit-button-fix">
  <input name="submit" type="submit" value="Get test cases" onclick="changeNextLink('show')"/>
  <input type="reset" value="&nbsp; Reset form &nbsp;" onclick="resetAll(true)" style="margin-left: 100pt; color: darkred"/>
</center>
</form> 

<div id="nextStepInstructions" class="hiddenText">
<p> The download of the archive should begin any moment.
When you are ready, please proceed to Step 2.

<center>
  <a href="<%=OnlineSTS.getHomeURL(request)%>/Step_2:_Running_the_tests">
    <img border="0" align="middle" alt="Right arrow"
         src="<%=OnlineSTS.getImageURL(request)%>/Icon-red-right-arrow.jpg"/>
    Go to the instructions for Step 2 (running the tests).
  </a>
</center>

<p> If the download does <i>not</i> begin soon, <b>please</b> let us know by
sending mail to <a
href="mailto:sbml-team@caltech.edu">sbml-team@caltech.edu</a> and please
provide information about the operating system and web browser you are
using.
</div>

<script type="text/javascript">
$("#tags span[title]").tooltip({
opacity: 1, predelay: 100, offset: [0,0], effect: 'toggle', delay: 0
});
</script>

<%@ include file="sbml-bottom.html"%>
