/*
** SBML skin custom JavaScript.
** This depends on Common.js and wikibits.js having been loaded first.
*/

/*
 * From www.surfmind.com/musings/2003/09/15
 * Copied 2007-12-12 and modified slightly.
 */

function alternateRowColors()
{
  var className = 'alt-row-colors';
  var rowcolor = '#f3f3f3';
  var rows, arow;
  var tables = document.getElementsByTagName("table");
  var rowCount = 0;
  for (var i = 0; i < tables.length; i++) {
    // dump(tables.item(i).className + " " + tables.item(i).nodeName + "\n");
    if (hasClass(tables.item(i), className)) {
      atable = tables.item(i);
      rows = atable.getElementsByTagName("tr");
      for (var j = 0; j < rows.length; j++) {
	arow = rows.item(j);
	if (arow.nodeName == "TR") {
	  if (rowCount % 2) {
	    arow.style.backgroundColor = rowcolor;
	  } else {
	    // default case
	  }
	  rowCount++;
	}
      }
      rowCount = 0;
    }
  }
}

hookEvent("load", alternateRowColors);


/*
 * Set up our styles for curvy corners.
 */
function SBMLCurvyCorners()
{
  settings15 = {
    tl: { radius: 15 },
    tr: { radius: 15 },
    bl: { radius: 15 },
    br: { radius: 15 },
    antiAlias: true,
    autoPad: false
  } 
  settings12 = {
    tl: { radius: 12 },
    tr: { radius: 12 },
    bl: { radius: 12 },
    br: { radius: 12 },
    antiAlias: true,
    autoPad: false
  } 
  settings9 = {
    tl: { radius: 9 },
    tr: { radius: 9 },
    bl: { radius: 9 },
    br: { radius: 9 },
    antiAlias: true,
    autoPad: false
  } 
  settings6 = {
    tl: { radius: 6 },
    tr: { radius: 6 },
    bl: { radius: 6 },
    br: { radius: 6 },
    antiAlias: true,
    autoPad: false
  } 
  var c15 = new curvyCorners(settings15, "rounded15");
  var c12 = new curvyCorners(settings12, "rounded12");
  var c9  = new curvyCorners(settings9, "rounded9");
  var c6  = new curvyCorners(settings6, "rounded6");
  c15.applyCornersToAll();
  c12.applyCornersToAll();
  c9.applyCornersToAll();
  c6.applyCornersToAll();
}

hookEvent("load", SBMLCurvyCorners);


/*
 * From http://www.brandspankingnew.net/
 * Copied 2008-02-08 and modified slightly.
 */

if (!applesearch)
  var applesearch = {};

applesearch.init = function ()
{
  // add applesearch css for non-safari, dom-capable browsers
  if ( navigator.userAgent.toLowerCase().indexOf('safari') < 0  && document.getElementById )
    {
      this.clearBtn = false;
		
      // add style sheet if not safari
      var dummy = document.getElementById("dummy_css");
      if (dummy)	dummy.href = "osx-searchbox/applesearch.css";
    }
}

// called when on user input - toggles clear fld btn
applesearch.onChange = function (fldID, btnID)
{
  // check whether to show delete button
  var fld = document.getElementById( fldID );
  var btn = document.getElementById( btnID );
  if (fld.value.length > 0 && !this.clearBtn)
    {
      btn.style.background = "white url('osx-searchbox/srch_r_f2.gif') no-repeat top left";
      btn.fldID = fldID; // btn remembers it's field
      btn.onclick = this.clearBtnClick;
      this.clearBtn = true;
    }
  else if (fld.value.length == 0 && this.clearBtn)
    {
      btn.style.background = "white url('osx-searchbox/srch_r.gif') no-repeat top left";
      btn.onclick = null;
      this.clearBtn = false;
    }
}


// clears field
applesearch.clearFld = function (fldID,btnID)
{
  var fld = document.getElementById( fldID );
  fld.value = "";
  this.onChange(fldID,btnID);
}

// called by btn.onclick event handler - calls clearFld for this button
applesearch.clearBtnClick = function ()
{
  applesearch.clearFld(this.fldID, this.id);
}


hookEvent("load", function () { applesearch.init(); } );


/*
 * IFrame SSI script II- © Dynamic Drive DHTML code library (http://www.dynamicdrive.com)
 * Visit DynamicDrive.com for hundreds of original DHTML scripts
 * This notice must stay intact for legal use.
 *
 * 2008-02-13 <mhucka@caltech.edu> Modified.
 */

// Input the IDs of the IFRAMES you wish to dynamically resize to match its
// content height: Separate each ID with a comma. Examples: ["myframe1",
// "myframe2"] or ["myframe"] or [] for none:

var iframeids = ["auto-resized-iframe"];

// Should script hide iframe from browsers that don't support this script
// (non IE5+/NS6+ browsers. Recommended):

var getFFVersion = navigator.userAgent.substring(navigator.userAgent.indexOf("Firefox")).split("/")[1];
var tmp = navigator.appName == 'Microsoft Internet Explorer' && navigator.userAgent.indexOf('Opera') < 1 ? 1 : 0;
if (tmp) var isIE = document.namespaces ? 1 : 0;

// Extra height in px below to add to iframe in FireFox 1.0+ browsers.
// Additional hacks for IE by M. Hucka.

var FFextraHeight = 0;

if (parseFloat(getFFVersion) >= 0.1)
  FFextraHeight = 25;
else if (isIE)
  FFextraHeight = 50;

function resizeCaller()
{
  var dyniframe = new Array();
  
  for (i=0; i<iframeids.length; i++)
    {
      if (document.getElementById)
	resizeIframe(iframeids[i]);
    }
}

function resizeIframe(frameid)
{
  var currentfr = document.getElementById(frameid);
  if (currentfr && !window.opera)
    {
      currentfr.style.display = "block";

      if (currentfr.contentDocument && currentfr.contentDocument.body.offsetHeight) //ns6 syntax
	currentfr.height = currentfr.contentDocument.body.offsetHeight + FFextraHeight; 
      else if (currentfr.Document && currentfr.Document.body.scrollHeight) //ie5+ syntax
	currentfr.height = currentfr.Document.body.scrollHeight + FFextraHeight;

      if (currentfr.addEventListener)
	currentfr.addEventListener("load", readjustIframe, false);
      else if (currentfr.attachEvent)
	{
	  currentfr.detachEvent("onload", readjustIframe); // Bug fix line
	  currentfr.attachEvent("onload", readjustIframe);
	}
    }
}

function readjustIframe(loadevt)
{
  var crossevt = (window.event) ? event : loadevt;
  var iframeroot = (crossevt.currentTarget) ? crossevt.currentTarget : crossevt.srcElement;
  if (iframeroot)
      resizeIframe(iframeroot.id);
}

function loadintoIframe(iframeid, url)
{
  if (document.getElementById)
    document.getElementById(iframeid).src=url;
}

if (window.addEventListener)
  window.addEventListener("load", resizeCaller, false);
else if (window.attachEvent)
  window.attachEvent("onload", resizeCaller);
else
  hookEvent("load", resizeCaller);
