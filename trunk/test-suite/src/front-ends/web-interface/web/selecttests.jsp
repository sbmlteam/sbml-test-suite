<%-- 
 * @file    selecttests.jsp
 * @brief   JSP file to take user test case selections using an HTML form.
 * @author  Michael Hucka
 * @author  Kimberly Begley
 * @date    Created Jul 29, 2008, 9:25:21 AM
 *
 * $Id$
 * $HeadURL$
 * ----------------------------------------------------------------------------
 * This file is part of the SBML Test Suite.  Please visit http://sbml.org for 
 * more information about SBML, and the latest version of the SBML Test Suite.
 *
 * Copyright 2008-2010 California Institute of Technology.
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

<%@ page errorPage="/web/error.jsp" %>

<%@ include file="sbml-head.html"%>

<script language="javascript" type="text/javascript">
<!--

dojo.require("dojo.fx");

function enableAllTags(tags)
{
  for (var i = 0; i < tags.length; i++)
    tags[i].disabled = false;
}

function setEnabled(tagName, enabled, tags)
{
  for (var i = 0; i < tags.length; i++)  
    if (tags[i].value == tagName)
    {
      tags[i].disabled = (enabled ? false : true);
      if (!enabled)
        tags[i].checked = false;        // "checked" means excluded
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
      return tags[i].checked;
}

function resetAvailableTags()
{
  var ctags = document.options.ctags;
  var ttags = document.options.ttags;

  var index = document.options.levelAndVersion.selectedIndex;

  enableAllTags(ctags);
  enableAllTags(ttags);

  switch (document.options.levelAndVersion[index].value)
  {
  case "1.2":
    // components
    setEnabled("FunctionDefinition",    false, ctags);
    setEnabled("InitialAssignment",     false, ctags);
    setEnabled("EventWithDelay",        false, ctags);
    setEnabled("EventNoDelay",          false, ctags);
    // tests
    setEnabled("0D-Compartment",        false, ttags);
    setEnabled("1D-Compartment",        false, ttags);
    setEnabled("2D-Compartment",        false, ttags);
    setEnabled("InitialConcentration",  false, ttags);
    setEnabled("HasOnlySubstanceUnits", false, ttags);
    setEnabled("ConstantSpecies",       false, ttags);
    setEnabled("StoichiometryMath",     false, ttags);
    setEnabled("CSymbolTime",           false, ttags);
    break;

  case "2.1":
    setEnabled("InitialAssignment",     false, ctags);
    break;

  }
  propagate();
  warn("", "init");
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
    setExcluded("2D-Compartment",         true, ttags);
    setExcluded("1D-Compartment",         true, ttags);
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
    setExcluded("InitialAmount",          true, ttags);
    setExcluded("InitialConcentration",   true, ttags);
    setExcluded("HasOnlySubstanceUnits",  true, ttags);
    setExcluded("BoundaryCondition",      true, ttags);
    setExcluded("ConstantSpecies",        true, ttags);
  }

  if (isExcluded("Reaction", ctags))
  {
    setExcluded("FastReaction",           true, ttags);
    setExcluded("ReversibleReaction",     true, ttags);
    setExcluded("NonUnityStoichiometry",  true, ttags);
    setExcluded("StoichiometryMath",      true, ttags);
    setExcluded("LocalParameters",        true, ttags);
  }

  if (isExcluded("Parameter", ctags))
  {
    setExcluded("NonConstantParameter",   true, ttags);
  }

  // If all rules and all events are removed, then one can't construct a
  // model with variable sized compartments or parameter values.

  if (isExcluded("AssignmentRule", ctags) && isExcluded("AlgebraicRule", ctags)
      && isExcluded("RateRule", ctags)
      && (lv == "1.2"
          || (isExcluded("EventWithDelay", ctags)
              && isExcluded("EventNoDelay", ctags))))
  {
    setExcluded("NonConstantCompartment", true, ttags);
    setExcluded("NonConstantParameter",   true, ttags);
  }

  // If there are no parameters, species or compartments, it's impossible
  // to test certain other SBML components.

  if (isExcluded("Parameter", ctags) && isExcluded("Compartment", ctags)
      && isExcluded("Species", ctags))
  {
    setExcluded("InitialAssignment",      true, ctags);
    setExcluded("AssignmentRule",         true, ctags);
    setExcluded("RateRule",               true, ctags);
    setExcluded("EventWithDelay",         true, ctags);
    setExcluded("EventNoDelay",           true, ctags);
  }

  // If none of the following are present, you can't test the use of
  // function definitions either.

  if (isExcluded("InitialAssignment", ctags) && isExcluded("RateRule", ctags)
      && isExcluded("AssignmentRule", ctags) && isExcluded("AlgebraicRule", ctags)
      && isExcluded("EventNoDelay", ctags) && isExcluded("EventWithDelay", ctags)
      && isExcluded("Reaction"))
  {
    setExcluded("FunctionDefinition",     true, ctags);    
  }

  // If certain conditions hold, we can't test anything.

  if (isExcluded("Parameter", ctags) && isExcluded("Compartment", ctags)
      && isExcluded("Species", ctags) && isExcluded("Reaction", ctags))
  {
    document.options.submit.disabled = true;
    warn("not-available", "hide"); // Just in case.
    warn("no-components", "show");
  }

  // State resets -- these should come last.

  if (document.options.submit.disabled
      && (! isExcluded("Species", ctags)
          || ! isExcluded("Parameter", ctags)
          || ! isExcluded("Compartment", ctags)))
  {
    document.options.submit.disabled = false;
    warn("no-components", "hide");
  }

}

/*
 * The forms built-in "reset" action clears the checkboxes, but also
 * messes with the user's menubox selection, which is not right.  So
 * we need our own custom extra bit of handling code.
 */
function resetAll()
{
  var lv = document.options.levelAndVersion;
  var selected = lv.selectedIndex;
  for (var i = 0; i < lv.length; i++)
    lv[i].defaultSelected = false;
  lv[selected].defaultSelected = true;

  // Hide the warning panel and reenable the "get test cases" button.
  if (document.options.submit.disabled)
  {
    document.options.submit.disabled = false;
    warn("", "init");
  }

  resetAvailableTags();
  return true;
}

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
    break;
  }
}

hookEvent("load", warn);

//-->
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
</style>

<%@ include file="sbml-top.html"%>

<div id='pagetitle'><h1 class='pagetitle'>Step 1: Select SBML Tests </h1></div><!-- id='pagetitle' -->
<div style="float: right; margin-top: 0; padding: 0 0 0 5px">
  <img src="/images/8/80/Icon-online-test-suite-64px.jpg" border="0">
</div>
<p>
The first step in using the online Test Suite is to obtain a set of test
models using the form on this page.  Once you have answered the questions
below and downloaded a collection of tests, you must then record the
results of simulating each model in your application and put the results in
files according to the guidelines in <a
href="/Software/SBML_Test_Suite/Step_2:_Running_the_tests">step 2</a>.
</p>

<p> If you prefer, you can also skip the rest of this page and download the
entire test set as a single <a
href="http://sourceforge.net/project/showfiles.php?group_id=71971&package_id=90209">zip
archive</a>.
</p>

<form name="options" action="/test-suite/web/process-selections.jsp" method="post">

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
       <option value="2.4" selected>SBML Level 2 Version 4</option>
     </optgroup>
   </select>
</h3>

<p> By default, you will be provided with all test cases <em>unless</em>
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

<h3 style="margin-top: 1.5em">(b) Select the component tags you would like to exclude:</h3>

<p>
<table class="borderless-table" width="100%"> 
  <tr>
    <td width="30%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ctags" value="FunctionDefinition" onchange="propagate()">Function definition<br> 
        <input type="checkbox" name="ctags" value="InitialAssignment"  onchange="propagate()">Initial assignment<br>
        <input type="checkbox" name="ctags" value="AssignmentRule"     onchange="propagate()">Assignment rules<br>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ctags" value="RateRule"           onchange="propagate()">Rate rules<br>
        <input type="checkbox" name="ctags" value="AlgebraicRule"      onchange="propagate()">Algebraic rules<br>
        <input type="checkbox" name="ctags" value="EventWithDelay"     onchange="propagate()">Events with delays<br>
        <input type="checkbox" name="ctags" value="EventNoDelay"       onchange="propagate()">Events without delays<br>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ctags" value="Compartment"        onchange="propagate()">Compartments<br>
        <input type="checkbox" name="ctags" value="Species"            onchange="propagate()">Species<br>
        <input type="checkbox" name="ctags" value="Reaction"           onchange="propagate()">Reactions<br>
        <input type="checkbox" name="ctags" value="Parameter"          onchange="propagate()">Parameters<br>
    </td>   
  </tr>
</table> 

<h3>(c) Select the test tags you would like to exclude:</h3>

<p>
<table class="borderless-table" width="100%">
  <tr>
    <td width="30%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ttags" value="2D-Compartment"         onchange="propagate()">2-D compartments<br>
        <input type="checkbox" name="ttags" value="1D-Compartment"         onchange="propagate()">1-D compartments<br>
        <input type="checkbox" name="ttags" value="0D-Compartment"         onchange="propagate()">0-D compartments<br>
        <input type="checkbox" name="ttags" value="NonConstantCompartment" onchange="propagate()">Varying-size compartments<br>
        <input type="checkbox" name="ttags" value="NonUnityCompartment"    onchange="propagate()">Compartments with size &ne; 1<br>
        <input type="checkbox" name="ttags" value="MultiCompartment"       onchange="propagate()">Multiple compartments<br>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ttags" value="InitialAmount"          onchange="propagate()">Species using initial amounts<br>
        <input type="checkbox" name="ttags" value="InitialConcentration"   onchange="propagate()">Species using initial concentration<br>
        <input type="checkbox" name="ttags" value="HasOnlySubstanceUnits"  onchange="propagate()">Species with <font size='-2'>'hasOnlySubstanceUnits'</font><br>
        <input type="checkbox" name="ttags" value="BoundaryCondition"      onchange="propagate()">Species as boundary conditions<br>
        <input type="checkbox" name="ttags" value="ConstantSpecies"        onchange="propagate()">Species declared as constant<br>
        <input type="checkbox" name="ttags" value="NonConstantParameter"   onchange="propagate()">Varying parameter values<br>
    </td>
    <td width="33%" valign="top" style="padding: 0 0 0 1em">
        <input type="checkbox" name="ttags" value="FastReaction"           onchange="propagate()">'Fast' reactions<br>
        <input type="checkbox" name="ttags" value="ReversibleReaction"     onchange="propagate()">Reversible reactions<br>
        <input type="checkbox" name="ttags" value="NonUnityStoichiometry"  onchange="propagate()">Stoichiometries &ne; 1<br>
        <input type="checkbox" name="ttags" value="StoichiometryMath"      onchange="propagate()">Use of formulas in stoichiometries<br>
        <input type="checkbox" name="ttags" value="LocalParameters"
        onchange="propagate()">Local parameters in reactions<br>
        <input type="checkbox" name="ttags" value="CSymbolTime"            onchange="propagate()">Use of csymbol for 'time'<br>
    </td>         
  </tr>
</table> 

<p>
<center style="margin-bottom: 1.5em">
  <input type="reset" value="Reset form" onclick="resetAll()">
</center>

<p style="margin-top: 1em; border-top: 1px solid #999;"> 
</p><p>
That's all!  Download a customized zip archive of the test cases you have
selected by clicking on the button below:
</p>

<div id="warningNoneLeft" class="warningBox"
     title="Warning&mdash;all possible components have been excluded">
If compartments, species, reactions and parameters are <i>all</i>
excluded, then there are no possible test cases left.  Please deselect
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

<center style="margin: 1.5em" class="ie-submit-button-fix">
  <input name="submit" type="submit" value="Get test cases">
</center>

</form> 

<p> Your browser will start downloading the archive shortly after you the
click button, and will automatically move to <a
href="/Software/SBML_Test_Suite/Step_2:_Running_the_tests">the instructions
for step 2</a>.
</p>

<%@ include file="sbml-bottom.html"%>
