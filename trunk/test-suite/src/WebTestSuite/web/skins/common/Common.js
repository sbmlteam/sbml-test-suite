/*
 * Description       : MediaWiki:Common.js
 * Original author(s): MediaWiki
 * $Id$
 * $Source$
 *
 * Downloaded 2007-12-12 from http://en.wikipedia.org/wiki/MediaWiki:Common.js
 * by M. Hucka.
 */


 /** Import module *************************************************************
  *
  *  Description: Includes a raw wiki page as javascript or CSS, 
  *               used for including user made modules.
  *  Maintainers: [[User:AzaToth]]
  */
 importedScripts = {}; // object keeping track of included scripts, so a script ain't included twice
 function importScript( page ) {
     if( importedScripts[page] ) {
         return;
     }
     importedScripts[page] = true;
     var url = wgScriptPath
             + '/index.php?title='
             + encodeURIComponent( page.replace( / /g, '_' ) )
             + '&action=raw&ctype=text/javascript';
     var scriptElem = document.createElement( 'script' );
     scriptElem.setAttribute( 'src' , url );
     scriptElem.setAttribute( 'type' , 'text/javascript' );
     document.getElementsByTagName( 'head' )[0].appendChild( scriptElem );
 }
 
 function importStylesheet( page ) {
     var sheet = '@import "'
               + wgScriptPath
               + '/index.php?title='
               + encodeURIComponent( page.replace( / /g, '_' ) )
               + '&action=raw&ctype=text/css";'
     var styleElem = document.createElement( 'style' );
     styleElem.setAttribute( 'type' , 'text/css' );
     styleElem.appendChild( document.createTextNode( sheet ) );
     document.getElementsByTagName( 'head' )[0].appendChild( styleElem );
 }
 
 /* Test if an element has a certain class **************************************
  *
  * Description: Uses regular expressions and caching for better performance.
  * Maintainers: [[User:Mike Dillon]], [[User:R. Koot]], [[User:SG]]
  */
 
 var hasClass = (function () {
     var reCache = {};
     return function (element, className) {
         return (reCache[className] ? reCache[className] : (reCache[className] = new RegExp("(?:\\s|^)" + className + "(?:\\s|$)"))).test(element.className);
     };
 })();
 
 /** Internet Explorer bug fix **************************************************
  *
  *  Description: Fixes IE horizontal scrollbar bug
  *  Maintainers: [[User:Tom-]]?
  */
 
 if (navigator.appName == "Microsoft Internet Explorer" && document.compatMode == "CSS1Compat")
 {
   var oldWidth;
   var docEl = document.documentElement;
 
   function fixIEScroll()
   {
     if (!oldWidth || docEl.clientWidth > oldWidth)
       doFixIEScroll();
     else
       setTimeout(doFixIEScroll, 1);
 
     oldWidth = docEl.clientWidth;
   }
 
   function doFixIEScroll() {
     docEl.style.overflowX = (docEl.scrollWidth - docEl.clientWidth < 4) ? "hidden" : "";
   }
 
   document.attachEvent("onreadystatechange", fixIEScroll);
   attachEvent("onresize", fixIEScroll);
 }
 
 /** Interwiki links to featured articles ***************************************
  *
  *  Description: Highlights interwiki links to featured articles (or
  *               equivalents) by changing the bullet before the interwiki link
  *               into a star.
  *  Maintainers: [[User:R. Koot]]
  */
 
 function LinkFA() 
 {
     if ( document.getElementById( "p-lang" ) ) {
         var InterwikiLinks = document.getElementById( "p-lang" ).getElementsByTagName( "li" );
 
         for ( var i = 0; i < InterwikiLinks.length; i++ ) {
             if ( document.getElementById( InterwikiLinks[i].className + "-fa" ) ) {
                 InterwikiLinks[i].className += " FA"
                 InterwikiLinks[i].title = "This is a featured article in another language.";
             }
         }
     }
 }
 
 addOnloadHook( LinkFA );
 
 /** Collapsible tables *********************************************************
  *
  *  Description: Allows tables to be collapsed, showing only the header. See
  *               [[Wikipedia:NavFrame]].
  *  Maintainers: [[User:R. Koot]]
  */
 
 var autoCollapse = 2;
 var collapseCaption = "collapse"; // was "hide" [mhucka 2007-12-27]
 var expandCaption = "show";
 
 function collapseTable( tableIndex )
 {
     var Button = document.getElementById( "collapseButton" + tableIndex );
     var Table = document.getElementById( "collapsibleTable" + tableIndex );
 
     if ( !Table || !Button ) {
         return false;
     }
 
     var Rows = Table.rows;
 
     if ( Button.firstChild.data == collapseCaption ) {
         for ( var i = 1; i < Rows.length; i++ ) {
             Rows[i].style.display = "none";
         }
         Button.firstChild.data = expandCaption;
     } else {
         for ( var i = 1; i < Rows.length; i++ ) {
             Rows[i].style.display = Rows[0].style.display;
         }
         Button.firstChild.data = collapseCaption;
     }
 }
 
 function createCollapseButtons()
 {
     var tableIndex = 0;
     var NavigationBoxes = new Object();
     var Tables = document.getElementsByTagName( "table" );
 
     for ( var i = 0; i < Tables.length; i++ ) {
         if ( hasClass( Tables[i], "collapsible" ) ) {
 
             /* only add button and increment count if there is a header row to work with */
             var HeaderRow = Tables[i].getElementsByTagName( "tr" )[0];
             if (!HeaderRow) continue;
             var Header = HeaderRow.getElementsByTagName( "th" )[0];
             if (!Header) continue;
 
             NavigationBoxes[ tableIndex ] = Tables[i];
             Tables[i].setAttribute( "id", "collapsibleTable" + tableIndex );
 
             var Button     = document.createElement( "span" );
             var ButtonLink = document.createElement( "a" );
             var ButtonText = document.createTextNode( collapseCaption );
 
             Button.style.styleFloat = "right";
             Button.style.cssFloat = "right";
             Button.style.fontWeight = "normal";
             Button.style.textAlign = "right";
             Button.style.width = "6em";
 
             ButtonLink.style.color = Header.style.color;
             ButtonLink.setAttribute( "id", "collapseButton" + tableIndex );
             ButtonLink.setAttribute( "href", "javascript:collapseTable(" + tableIndex + ");" );
             ButtonLink.appendChild( ButtonText );
 
             Button.appendChild( document.createTextNode( "[" ) );
             Button.appendChild( ButtonLink );
             Button.appendChild( document.createTextNode( "]" ) );
 
             Header.insertBefore( Button, Header.childNodes[0] );
             tableIndex++;
         }
     }
 
     for ( var i = 0;  i < tableIndex; i++ ) {
         if ( hasClass( NavigationBoxes[i], "collapsed" ) || ( tableIndex >= autoCollapse && hasClass( NavigationBoxes[i], "autocollapse" ) ) ) {
             collapseTable( i );
         }
     }
 }
 
 addOnloadHook( createCollapseButtons );
 
 /** Dynamic Navigation Bars (experimental) *************************************
  *
  *  Description: See [[Wikipedia:NavFrame]].
  *  Maintainers: UNMAINTAINED
  */
 
  // set up the words in your language
  var NavigationBarHide = '[' + collapseCaption + ']';
  var NavigationBarShow = '[' + expandCaption + ']';
 
  // shows and hides content and picture (if available) of navigation bars
  // Parameters:
  //     indexNavigationBar: the index of navigation bar to be toggled
  function toggleNavigationBar(indexNavigationBar)
  {
     var NavToggle = document.getElementById("NavToggle" + indexNavigationBar);
     var NavFrame = document.getElementById("NavFrame" + indexNavigationBar);
 
     if (!NavFrame || !NavToggle) {
         return false;
     }
 
     // if shown now
     if (NavToggle.firstChild.data == NavigationBarHide) {
         for (
                 var NavChild = NavFrame.firstChild;
                 NavChild != null;
                 NavChild = NavChild.nextSibling
             ) {
             if ( hasClass( NavChild, 'NavPic' ) ) {
                 NavChild.style.display = 'none';
             }
             if ( hasClass( NavChild, 'NavContent') ) {
                 NavChild.style.display = 'none';
             }
         }
     NavToggle.firstChild.data = NavigationBarShow;
 
     // if hidden now
     } else if (NavToggle.firstChild.data == NavigationBarShow) {
         for (
                 var NavChild = NavFrame.firstChild;
                 NavChild != null;
                 NavChild = NavChild.nextSibling
             ) {
             if (hasClass(NavChild, 'NavPic')) {
                 NavChild.style.display = 'block';
             }
             if (hasClass(NavChild, 'NavContent')) {
                 NavChild.style.display = 'block';
             }
         }
     NavToggle.firstChild.data = NavigationBarHide;
     }
  }
 
  // adds show/hide-button to navigation bars
  function createNavigationBarToggleButton()
  {
     var indexNavigationBar = 0;
     // iterate over all < div >-elements 
     var divs = document.getElementsByTagName("div");
     for(
             var i=0; 
             NavFrame = divs[i]; 
             i++
         ) {
         // if found a navigation bar
         if (hasClass(NavFrame, "NavFrame")) {
 
             indexNavigationBar++;
             var NavToggle = document.createElement("a");
             NavToggle.className = 'NavToggle';
             NavToggle.setAttribute('id', 'NavToggle' + indexNavigationBar);
             NavToggle.setAttribute('href', 'javascript:toggleNavigationBar(' + indexNavigationBar + ');');
 
             var NavToggleText = document.createTextNode(NavigationBarHide);
             for (
                  var NavChild = NavFrame.firstChild;
                  NavChild != null;
                  NavChild = NavChild.nextSibling
                 ) {
                 if ( hasClass( NavChild, 'NavPic' ) || hasClass( NavChild, 'NavContent' ) ) {
                     if (NavChild.style.display == 'none') {
                         NavToggleText = document.createTextNode(NavigationBarShow);
                         break;
                     }
                 }
             }
 
             NavToggle.appendChild(NavToggleText);
             // Find the NavHead and attach the toggle link (Must be this complicated because Moz's firstChild handling is borked)
             for(
               var j=0; 
               j < NavFrame.childNodes.length; 
               j++
             ) {
               if (hasClass(NavFrame.childNodes[j], "NavHead")) {
                 NavFrame.childNodes[j].appendChild(NavToggle);
               }
             }
             NavFrame.setAttribute('id', 'NavFrame' + indexNavigationBar);
         }
     }
  }
 
  addOnloadHook( createNavigationBarToggleButton );
 
 /** Main Page layout fixes *********************************************************
  *
  *  Description:        Various layout fixes for the main page, including an
  *                      additional link to the complete list of languages available
  *                      and the renaming of the 'Article' to to 'Main Page'.
  *  Maintainers:        [[User:AzaToth]], [[User:R. Koot]], [[User:Alex Smotrov]]
  */
 
 function mainPageRenameNamespaceTab() {
     try {
         var Node = document.getElementById( 'ca-nstab-main' ).firstChild;
         if ( Node.textContent ) {      // Per DOM Level 3
             Node.textContent = 'Main Page';
         } else if ( Node.innerText ) { // IE doesn't handle .textContent
             Node.innerText = 'Main Page';
         } else {                       // Fallback
             Node.replaceChild( Node.firstChild, document.createTextNode( 'Main Page' ) ); 
         }
     } catch(e) {
         // bailing out!
     }
 }
 
 if ( wgTitle == 'Main Page' && ( wgNamespaceNumber == 0 || wgNamespaceNumber == 1 ) ) {
        addOnloadHook( mainPageRenameNamespaceTab );
 }
 
 if ( wgTitle == 'Main Page' && wgNamespaceNumber == 0 ) {
        addOnloadHook( mainPageAppendCompleteListLink );
 }
 
 function mainPageAppendCompleteListLink() {
     addPortletLink('p-lang', 'http://meta.wikimedia.org/wiki/List_of_Wikipedias', 
     'Complete list', 'interwiki-completelist', 'Complete list of Wikipedias')
 }
 
 /** Extra toolbar options ******************************************************
  *
  *  Description: UNDOCUMENTED
  *  Maintainers: [[User:MarkS]]?, [[User:Voice of All]], [[User:R. Koot]]
  */
 
 //This is a modified copy of a script by User:MarkS for extra features added by User:Voice of All.
 // This is based on the original code on Wikipedia:Tools/Editing tools
 // To disable this script, add <code>mwCustomEditButtons = [];<code> to [[Special:Mypage/monobook.js]]
 
 if (mwCustomEditButtons) {
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/c/c8/Button_redirect.png",
     "speedTip": "Redirect",
     "tagOpen": "#REDIRECT [[",
     "tagClose": "]]",
     "sampleText": "Insert text"};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/c/c9/Button_strike.png",
     "speedTip": "Strike",
     "tagOpen": "<s>",
     "tagClose": "</s>",
     "sampleText": "Strike-through text"};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/1/13/Button_enter.png",
     "speedTip": "Line break",
     "tagOpen": "<br />",
     "tagClose": "",
     "sampleText": ""};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/8/80/Button_upper_letter.png",
     "speedTip": "Superscript",
     "tagOpen": "<sup>",
     "tagClose": "</sup>",
     "sampleText": "Superscript text"};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/7/70/Button_lower_letter.png",
     "speedTip": "Subscript",
     "tagOpen": "<sub>",
     "tagClose": "</sub>",
     "sampleText": "Subscript text"};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/5/58/Button_small.png",
     "speedTip": "Small",
     "tagOpen": "<small>",
     "tagClose": "</small>",
     "sampleText": "Small Text"};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/3/34/Button_hide_comment.png",
     "speedTip": "Insert hidden Comment",
     "tagOpen": "<!-- ",
     "tagClose": " -->",
     "sampleText": "Comment"};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/1/12/Button_gallery.png",
     "speedTip": "Insert a picture gallery",
     "tagOpen": "\n<gallery>\n",
     "tagClose": "\n</gallery>",
     "sampleText": "Image:Example.jpg|Caption1\nImage:Example.jpg|Caption2"};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/f/fd/Button_blockquote.png",
     "speedTip": "Insert block of quoted text",
     "tagOpen": "<blockquote>\n",
     "tagClose": "\n</blockquote>",
     "sampleText": "Block quote"};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/en/6/60/Button_insert_table.png",
     "speedTip": "Insert a table",
     "tagOpen": '{| class="wikitable"\n|-\n',
     "tagClose": "\n|}",
     "sampleText": "! header 1\n! header 2\n! header 3\n|-\n| row 1, cell 1\n| row 1, cell 2\n| row 1, cell 3\n|-\n| row 2, cell 1\n| row 2, cell 2\n| row 2, cell 3"};
 
   mwCustomEditButtons[mwCustomEditButtons.length] = {
     "imageFile": "http://upload.wikimedia.org/wikipedia/commons/7/79/Button_reflink.png",
     "speedTip": "Insert a reference",
     "tagOpen": "<ref>",
     "tagClose": "</ref>",
     "sampleText": "Insert footnote text here"};
 
 }
 
 /*</nowiki>*/
 
 /** pageview counter ***********************************************************
  *
  *  Description: Please talk to de:User:LeonWeber before changing anything or 
  *               if there are any issues with that.
  *  Maintainers: [[:de:User:LeonWeber]]?
  */
 
  // this should be adjusted to a good value.
  // BE CAREFUL, you will break zedler if it's too low!
  // And then DaB. will kill Leon :-(
  var disable_counter = 0;
  var counter_factor = 6000;
 
  function pgcounter_setup()
  {
  	if(disable_counter == 0)
  	{
  		var url = window.location.href;
  		if(Math.floor(Math.random()*counter_factor)==42)  // the probability thing
   		{
   			if(wgIsArticle==true || wgArticleId==0) // do not count history pages etc.
  			{
  				var pgcountNs = wgCanonicalNamespace;
  				if(wgCanonicalNamespace=="")
  				{
  					pgcountNs = "0";
  				}
   				var cnt_url = "http://pgcount.wikimedia.de/index.png?ns=" + pgcountNs + "&title=" + encodeURI(wgTitle) + "&factor=" + counter_factor +"&wiki=enwiki";
  				var img = new Image(); 
  				img.src = cnt_url;
  			}
  		}
  	}
  }
  // Do not use aOnloadFunctions[aOnloadFunctions.length] = pgcounter_setup;, some browsers don't like that.
  pgcounter_setup();
 
 /** "Technical restrictions" title fix *****************************************
 *
 * Description:
 * Maintainers: User:Interiot, User:Mets501, User:Freakofnurture
 */
 //
 // For pages that have something like Template:Lowercase, replace the title, but only if it is cut-and-pasteable as a valid wikilink.
 // (for instance iPod's title is updated. But [[C#]] is not an equivalent
 // wikilink, so [[C Sharp]] doesn't have its main title changed)
 // Likewise for users who have selected the U.K. date format ("1 March") the  
 // titles of day-of-the-year articles will appear in that style. Users with any
 // other date setting are not affected.
 //
 // The function looks for a banner like this: 
 // &lt;div id="RealTitleBanner"&gt;  ... &lt;span id="RealTitle"&gt;title&lt;/span&gt; ... &lt;/div&gt;
 // An element with id=DisableRealTitle disables the function.
 //
 var disableRealTitle = 0; // users can set disableRealTitle = 1 locally to disable.
 if (wgIsArticle) { // don't display the RealTitle when editing, since it is apparently inconsistent (doesn't show when editing sections, doesn't show when not previewing)
  addOnloadHook(function() {
    try {
        var realTitleBanner = document.getElementById("RealTitleBanner");
        if (realTitleBanner && !document.getElementById("DisableRealTitle") && !disableRealTitle ) {
            var realTitle = document.getElementById("RealTitle");
            if (realTitle) {
                var realTitleHTML = realTitle.innerHTML;
                realTitleText = pickUpText(realTitle);
 
                var isPasteable = 0;
                //var containsHTML = /</.test(realTitleHTML);    // contains ANY HTML
                var containsTooMuchHTML = /</.test( realTitleHTML.replace(/<\/?(sub|sup|small|big)>/gi, "") ); // contains HTML that will be ignored when cut-n-pasted as a wikilink
                // calculate whether the title is pasteable
                var verifyTitle = realTitleText.replace(/^ +/, "");       // trim left spaces
                verifyTitle = verifyTitle.charAt(0).toUpperCase() + verifyTitle.substring(1, verifyTitle.length);    // uppercase first character
 
                // if the namespace prefix is there, remove it on our verification copy. If it isn't there, add it to the original realValue copy.
                if (wgNamespaceNumber != 0) {
                    if (wgCanonicalNamespace == verifyTitle.substr(0, wgCanonicalNamespace.length).replace(/ /g, "_") && verifyTitle.charAt(wgCanonicalNamespace.length) == ":") {
                        verifyTitle = verifyTitle.substr(wgCanonicalNamespace.length + 1);
                    } else {
                        realTitleText = wgCanonicalNamespace.replace(/_/g, " ") + ":" + realTitleText;
                        realTitleHTML = wgCanonicalNamespace.replace(/_/g, " ") + ":" + realTitleHTML;
                    }
                }
 
                // verify whether wgTitle matches
                verifyTitle = verifyTitle.replace(/[\s_]+/g, " ");      // underscores and multiple spaces to single spaces
                verifyTitle = verifyTitle.replace(/^\s+/, "").replace(/\s+$/, "");        // trim left and right spaces
                verifyTitle = verifyTitle.charAt(0).toUpperCase() + verifyTitle.substring(1, verifyTitle.length);    // uppercase first character
                if ( (verifyTitle == wgTitle) || (verifyTitle == wgTitle.replace(/^(.+)?(January|February|March|April|May|June|July|August|September|October|November|December)\s+([12]?[0-9]|3[0123])([^\d].*)?$/g, "$1$3 $2$4") )) isPasteable = 1;
                var h1 = document.getElementsByTagName("h1")[0];
                if (h1 && isPasteable) {
                    h1.innerHTML = containsTooMuchHTML ? realTitleText : realTitleHTML;
                    if (!containsTooMuchHTML)
                        realTitleBanner.style.display = "none";
                }
                document.title = realTitleText + " - Wikipedia, the free encyclopedia";
            }
        }
    } catch (e) {
        /* Something went wrong. */
    }
  });
 }
 
 // similar to innerHTML, but only returns the text portions of the insides, excludes HTML
 function pickUpText(aParentElement) {
 var str = "";
 
 function pickUpTextInternal(aElement) {
  var child = aElement.firstChild;
  while (child) {
   if (child.nodeType == 1)     // ELEMENT_NODE 
    pickUpTextInternal(child);
   else if (child.nodeType == 3)   // TEXT_NODE
    str += child.nodeValue;
 
   child = child.nextSibling;
  }
 }
 
  pickUpTextInternal(aParentElement);
  return str;
 }
 
 //fix edit summary prompt for undo
 //this code fixes the fact that the undo function combined with the "no edit summary prompter" causes problems if leaving the
 //edit summary unchanged
 //this was added by [[User:Deskana]], code by [[User:Tra]]
 addOnloadHook(function () {
   if (document.location.search.indexOf("undo=") != -1
   && document.getElementsByName('wpAutoSummary')[0]) {
     document.getElementsByName('wpAutoSummary')[0].value='1';
   }
 })
 
 /** Add dismiss button to watchlist-message *************************************
  *
  *  Description: Hide the watchlist message for one week.
  *  Maintainers: [[User:Ruud Koot|Ruud Koot]]
  */
 
 function addDismissButton() {
    var watchlistMessage = document.getElementById("watchlist-message");
    if ( watchlistMessage == null ) return;
    var watchlistCookieID = watchlistMessage.className.replace(/cookie\-ID\_/ig,'');
 
    if ( document.cookie.indexOf( "hidewatchlistmessage-" + watchlistCookieID + "=yes" ) != -1 ) {
        watchlistMessage.style.display = "none";
    }
 
    var Button     = document.createElement( "span" );
    var ButtonLink = document.createElement( "a" );
    var ButtonText = document.createTextNode( "dismiss" );
 
    ButtonLink.setAttribute( "id", "dismissButton" );
    ButtonLink.setAttribute( "href", "javascript:dismissWatchlistMessage();" );
    ButtonLink.setAttribute( "title", "Hide this message for one week" );
    ButtonLink.appendChild( ButtonText );
 
    Button.appendChild( document.createTextNode( "[" ) );
    Button.appendChild( ButtonLink );
    Button.appendChild( document.createTextNode( "]" ) );
 
    watchlistMessage.appendChild( Button );
 }
 
 function dismissWatchlistMessage() {
    var e = new Date();
    e.setTime( e.getTime() + (7*24*60*60*1000) );
    var watchlistMessage = document.getElementById("watchlist-message");
    var watchlistCookieID = watchlistMessage.className.replace(/cookie\-ID\_/ig,'');
    document.cookie = "hidewatchlistmessage-" + watchlistCookieID + "=yes; expires=" + e.toGMTString() + "; path=/";
    watchlistMessage.style.display = "none";
 }
 
 addOnloadHook( addDismissButton );
 
 /** Main Page deletion image *******************************************************
   *
   *  Description: If the Main Page does not exist (i.e., it's been deleted) then insert an image
   *               instead of showing the "page does not exist" text.
   *  Created by: [[User:Mark]], with invaluable help from [[User:Pathoschild]]
   */
 
 function MainPageDeletedImage() {
   try {
 
     //If the article does not exist and it is the Main Page, proceed
     if ( document.getElementById( "noarticletext" ) && wgTitle == 'Main Page' ) {
 
       // Insert a protected commons image at the end of the document explaining it.
       var contentbox = document.getElementById('content');
       var newimg = document.createElement('img');
       newimg.setAttribute('src','http://upload.wikimedia.org/wikipedia/commons/9/99/WikipediaTechnical.png');
       contentbox.appendChild(newimg);
 
       // Hide the article-does-not-exist text
       var NoArticleMessage = document.getElementById('noarticletext');
       NoArticleMessage.style.display="none";
 
       // Hide the edit button
       var EditThisPageButton = document.getElementById('ca-edit');
       EditThisPageButton.style.display="none";
     }
   } catch(e) {
       // In case it does not work, do nothing
       return;
   }
 }
 
 addOnloadHook( MainPageDeletedImage );
 
 /** Change Special:Search to use a drop-down menu *******************************************************
   *
   *  Description: Change Special:Search to use a drop-down menu, with the default being
   *               the internal MediaWiki engine
   *  Created and maintained by: [[User:Gracenotes]]
   */
 
 if (wgPageName == "Special:Search") {
         var searchEngines = [];
         addOnloadHook(SpecialSearchEnhanced);
 }
 
 function SpecialSearchEnhanced() {
         var createOption = function(site, action, mainQ, addQ, addV) {
                 var opt = document.createElement('option');
                 opt.appendChild(document.createTextNode(site));
                 searchEngines[searchEngines.length] = [action, mainQ, addQ, addV];
                 return opt;
         }
         var searchForm = document.forms['search'];
         var selectBox = document.createElement('select');
         selectBox.id = 'searchEngine';
         searchForm.onsubmit = function() {
                 var optSelected = searchEngines[document.getElementById('searchEngine').selectedIndex];
                 searchForm.action = optSelected[0];
                 searchForm.lsearchbox.name = optSelected[1];
                 searchForm.title.value = optSelected[3];
                 searchForm.title.name = optSelected[2];
         }
         selectBox.appendChild(createOption('MediaWiki search', wgScriptPath + '/index.php', 'search', 'title', 'Special:Search'));
         selectBox.appendChild(createOption('Google', 'http://www.google.com/search', 'q', 'sitesearch', 'en.wikipedia.org'));
         selectBox.appendChild(createOption('Yahoo', 'http://search.yahoo.com/search', 'p', 'vs', 'en.wikipedia.org'));
         selectBox.appendChild(createOption('Windows Live', 'http://search.live.com/results.aspx', 'q', 'q1', 'site:http://en.wikipedia.org'));
         selectBox.appendChild(createOption('Wikiwix', 'http://www.wikiwix.com/', 'action', 'lang', 'en'));
         selectBox.appendChild(createOption('Exalead', 'http://www.exalead.com/wikipedia/results', 'q', 'language', 'en'));
         searchForm.lsearchbox.style.marginLeft = '0px';
         var lStat = document.getElementById('loadStatus');
         lStat.parentNode.insertBefore(selectBox, lStat);
 }
 
 /** Geo-targeted watchlist notice *******************************************************
   *
   *  Description: Allows for geographic targeting of watchlist notices. See [[Wikipedia:Geonotice]] for more information.
   *  Created by: [[User:Gmaxwell]]
   */
 
 if (wgPageName == "Special:Watchlist")
     addOnloadHook((function (){document.write('<script type="text/javascript" src="http://tools.wikimedia.de/~gmaxwell/cgi-bin/geonotice.py"><\/script>')}));
 
/** Sysop Javascript *******************************************************
 *
 *  Description: Allows for sysop-specific Javascript at [[MediaWiki:Sysop.js]].
 *  Created by: [[User:^demon]]
 */
function sysopFunctions() {
	if ( wgUserGroups && !window.disableSysopJS ) {
		for ( var g = 0; g < wgUserGroups.length; ++g ) {
			if ( wgUserGroups[g] == "sysop" ) {
				importScript( "MediaWiki:Sysop.js" );
				break;
			}
		}
	}
}
 
addOnloadHook( sysopFunctions );
 
 /** WikiMiniAtlas *******************************************************
   *
   *  Description: WikiMiniAtlas is a popup click and drag world map.
   *               This script causes all of our coordinate links to display the WikiMiniAtlas popup button.
   *               The script itself is located on meta because it is used by many projects.
   *               See [[Meta:WikiMiniAtlas]] for more information. 
   *  Created by: [[User:Dschwen]]
   */
 
 document.write('<script type="text/javascript" src="' 
     + 'http://meta.wikimedia.org/w/index.php?title=MediaWiki:Wikiminiatlas.js' 
     + '&action=raw&ctype=text/javascript&smaxage=21600&maxage=86400"></script>');
 
 /** IE 6 Z-index bug workaround for anonnotice **************************
   *
   *  Description: This implements a work around for the Z-index bug found in Internet Explorer.
   *               It correctly places the anon notice on the page, even under IE6.
   *               See this Google search for more information about the bug:
   *               http://www.google.com/search?hl=en&client=firefox-a&rls=org.mozilla%3Aen-US%3Aofficial&hs=q74&q=z-index+ie6+bug&btnG=Search
   *  Created by: [[User:Gmaxwell]]
   */
 addOnloadHook((function (){
     if (wgUserName == null) {
 
         var messageEdu=new Array();
            messageEdu[0]='<a href="http://en.wikipedia.org/wiki/Wikipedia:Researching_with_Wikipedia" title="Wikipedia:Researching with Wikipedia">Learn&nbsp;more&nbsp;about&nbsp;using&nbsp;Wikipedia&nbsp;for&nbsp;research</a>';
            messageEdu[1]='<a href="http://en.wikipedia.org/wiki/Wikipedia:Ten_things_you_may_not_know_about_Wikipedia" title="Wikipedia:Ten things you may not know about Wikipedia">Ten&nbsp;things&nbsp;you&nbsp;may&nbsp;not&nbsp;know&nbsp;about&nbsp;Wikipedia</a>';
            messageEdu[2]='<a href="http://en.wikipedia.org/wiki/Wikipedia:Ten_things_you_may_not_know_about_images_on_Wikipedia" title="Wikipedia:Ten things you may not know about images on Wikipedia">Ten&nbsp;things&nbsp;you&nbsp;may&nbsp;not&nbsp;know&nbsp;about&nbsp;images&nbsp;on&nbsp;Wikipedia</a>';
            messageEdu[3]='<a href="http://en.wikipedia.org/wiki/Wikipedia:Citing_Wikipedia" title="Wikipedia:Citing Wikipedia">Learn&nbsp;more&nbsp;about&nbsp;citing&nbsp;Wikipedia</a>';
            messageEdu[4]='Have&nbsp;questions?&nbsp;<a href="http://en.wikipedia.org/wiki/Wikipedia:Questions" title="Wikipedia:Questions">Find&nbsp;out&nbsp;how&nbsp;to&nbsp;ask&nbsp;questions&nbsp;and&nbsp;get&nbsp;answers.</a>';
            messageEdu[5]='<a href="http://en.wikipedia.org/wiki/Wikipedia:Basic_navigation" title="Wikipedia:Basic navigation">Find&nbsp;out&nbsp;more&nbsp;about&nbsp;navigating&nbsp;Wikipedia&nbsp;and&nbsp;finding&nbsp;information</a>';
            messageEdu[6]='<a href="http://en.wikipedia.org/wiki/Wikipedia:Contributing_to_Wikipedia" title="Wikipedia:Contributing to Wikipedia">Interested&nbsp;in&nbsp;contributing&nbsp;to&nbsp;Wikipedia?</a>';
         var whichMessageEdu = Math.floor(Math.random()*(messageEdu.length));
 
 
/**         document.getElementById("contentSub").innerHTML +='<div style="position:absolute; z-index:100; right:100px; top:0px;" class="metadata" id="anontip"><div style="text-align:right; font-size:87%">¥&nbsp;<i>' + messageEdu[whichMessageEdu] + '</i>&nbsp;¥</div></div>';
 */
     }
 }));
 
/** 
  * Correctly handle PNG transparency in Internet Explorer 6.
  * http://homepage.ntlworld.com/bobosola. Updated 18-Jan-2006.
  *  
  * Adapted for Wikipedia by Remember_the_dot and Edokter.
  *  
  * http://homepage.ntlworld.com/bobosola/pnginfo.htm states "This page contains more information for
  * the curious or those who wish to amend the script for special needs", which I take as permission to
  * modify or adapt this script freely. I release my changes into the public domain.
  */  
 
function PngFix()
{
    try
    {
        if (!document.body.filters)
        {
            window.PngFixDisabled = true
        }
    }
    catch (e)
    {
        window.PngFixDisabled = true
    }
    if (!window.PngFixDisabled)
    {
        var documentImages = document.images
        var documentCreateElement = document.createElement
        var funcEncodeURI = encodeURI
 
        for (var i = 0; i < documentImages.length;)
        {
            var img = documentImages[i]
            var imgSrc = img.src
 
            if (imgSrc.substr(imgSrc.length - 3).toLowerCase() == "png" && !img.onclick)
            {
                if (img.useMap)
                {
                    img.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + encodeURI(imgSrc) + "')"
                    img.src = "http://upload.wikimedia.org/wikipedia/commons/c/ce/Transparent.gif"
                    i++
                }
                else
                {
                    var outerSpan = documentCreateElement("span")
                    var innerSpan = documentCreateElement("span")
                    var outerSpanStyle = outerSpan.style
                    var innerSpanStyle = innerSpan.style
                    var imgCurrentStyle = img.currentStyle
 
                    outerSpan.id = img.id
                    outerSpan.title = img.title
                    outerSpan.className = img.className
                    outerSpanStyle.backgroundImage = imgCurrentStyle.backgroundImage
                    outerSpanStyle.borderWidth = imgCurrentStyle.borderWidth
                    outerSpanStyle.borderStyle = imgCurrentStyle.borderStyle
                    outerSpanStyle.borderColor = imgCurrentStyle.borderColor
                    outerSpanStyle.display = "inline-block"
                    outerSpanStyle.fontSize = "0"
                    outerSpanStyle.verticalAlign = "middle"
                    if (img.parentElement.href) outerSpanStyle.cursor = "hand"
 
                    innerSpanStyle.width = "1px"
                    innerSpanStyle.height = "1px"
                    innerSpanStyle.display = "inline-block"
                    innerSpanStyle.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + funcEncodeURI(imgSrc) + "')"
 
                    outerSpan.appendChild(innerSpan)
                    img.parentNode.replaceChild(outerSpan, img)
                }
            }
            else
            {
                i++
            }
        }
    }
}
 
if (navigator.appName == "Microsoft Internet Explorer" && navigator.appVersion.substr(22, 1) == "6")
{
    window.attachEvent("onload", PngFix)
}
 
/**
  * Remove need for CSS hacks regarding MSIE and IPA.
  */
 
if(navigator.userAgent.indexOf("MSIE") != -1 && document.createStyleSheet) {
   document.createStyleSheet().addRule('.IPA', 'font-family: "Doulos SIL", "Charis SIL", Gentium, "DejaVu Sans", Code2000, "TITUS Cyberbit Basic", "Arial Unicode MS", "Lucida Sans Unicode", "Chrysanthi Unicode";');
}
