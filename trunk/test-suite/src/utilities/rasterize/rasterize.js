/*
** @file    rasterize.js
** @brief   Javascript utility for creating image files from HTML plots.
** @author  Michael Hucka
**
** This is a variation on a standard example provided as part of PhantomJS
** (http://phantomjs.org) for rasterizing an HTML page.  This is different
** only in using a longer timeout (which is hardwired into the code, which
** in turn is the reason we can't use the original example directly from the
** PhantomJS distribution).  
*/

var page = require('webpage').create();
var system = require('system');
var address, output, size;

if (system.args.length < 3 || system.args.length > 5) {
    console.log('Usage: rasterize.js URL filename [paperwidth*paperheight|paperformat] [zoom]');
    console.log('  paper (pdf output) examples: "5in*7.5in", "10cm*20cm", "A4", "Letter"');
    phantom.exit(1);
} else {
    address = system.args[1];
    output = system.args[2];
    page.viewportSize = { width: 600, height: 400 };
    if (system.args.length > 3 && system.args[2].substr(-4) === ".pdf") {
        size = system.args[3].split('*');
        page.paperSize = size.length === 2 ? { width: size[0], height: size[1], margin: '0px' }
                                           : { format: system.args[3], orientation: 'portrait', margin: '1cm' };
    }
    if (system.args.length > 4) {
        page.zoomFactor = system.args[4];
    }
    page.open(address, function (status) {
        if (status !== 'success') {
            console.log('Unable to load the address!');
        } else {
            window.setTimeout(function () {
                page.render(output);
                phantom.exit();
            }, 2000);
        }
    });
}
