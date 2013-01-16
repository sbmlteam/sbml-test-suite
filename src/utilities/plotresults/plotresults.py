#!/usr/bin/env python
#
# @file    plotresults.py
# @brief   Make a plot of the SBML Test Suite results for a test case
# @author  Michael Hucka
#
# Purpose:
#
# This generates an HTML file with a plot of the results from an SBML Test
# Suite test case.  The input file must be in the CSV format used in the Test
# Suite, which is as follows:
#
#   1. The first line lists the variables whose values are given
#   2. The first column on every subsequent line is a time point
#   3. The remaining column on every line is a data value, one per column,
#      for each variable at that time point.
#
# The resulting HTML file makes use of JavaScript libraries for the actual
# plotting process.  Currently, two libraries are supported: Highcharts JS
# and Flot.
#
# <!--------------------------------------------------------------------------
# This file is part of the SBML Test Suite.  Please visit http://sbml.org for
# more information about SBML, and the latest version of the SBML Test Suite.
#
# Copyright (C) 2010-2012 jointly by the following organizations: 
#     1. California Institute of Technology, Pasadena, CA, USA
#     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
#
# Copyright (C) 2008-2009 California Institute of Technology (USA).
#
# Copyright (C) 2004-2007 jointly by the following organizations:
#     1. California Institute of Technology (USA) and
#     2. University of Hertfordshire (UK).
#
# The SBML Test Suite is free software; you can redistribute it and/or
# modify it under the terms of the GNU Lesser General Public License as
# published by the Free Software Foundation.  A copy of the license
# agreement is provided in the file named "LICENSE.txt" included with
# this software distribution and also available at
# http://sbml.org/Software/SBML_Test_Suite/license.html
# ---------------------------------------------------------------------- -->*/

import argparse
import csv
import sys
import os.path
import re
from itertools import cycle

# -----------------------------------------------------------------------------
# Command-line argument handling.
# -----------------------------------------------------------------------------

__desc = '''Create a plot of the data stored in an SBML Test Suite case results
CSV file.  The plot is in the form of an HTML file that uses JavaScript to
draw the curves and provide additional capabilities.  The HTML file produced 
has the same name as the given DATA file, but with .html as the suffix.'''

__desc_end = '''This file is part of the SBML Test Suite.  Please visit
http://sbml.org for more information about SBML, and the latest version of
the SBML Test Suite.'''


def parse_cmdline(direct_args = None):
    parser = argparse.ArgumentParser(description=__desc, epilog=__desc_end)

    parser.add_argument("-q", "--quiet", action="store_const", const=True,
                        help="be quiet; don't print error or warning messages")

    parser.add_argument("-c", "--complete", action="store_const", const=True,
                        help="write a complete HTML page, not just a fragment")

    parser.add_argument("-n", "--no-buttons", action="store_const", const=True, 
                        help="omit interactive buttons and text from the HTML output")

    parser.add_argument("-d", "--data", required=True,
                        help="the CSV file containing the data to plot")

    parser.add_argument("-o", "--output", required=True,
                        help="the HTML file where the output should be written")

    parser.add_argument("-r", "--results", 
                        help="(optional) a second CSV file to overlay over the data")

    parser.add_argument("-l", "--library", default="highcharts",
                        help="(optional) library to use: 'highcharts' (default) or 'flot'")

    return parser.parse_args(direct_args)


def get_data_file_name(direct_args = None):
    return expanded_path(direct_args.data)


def get_results_file_name(direct_args = None):
    return expanded_path(direct_args.results)


def get_output_file_name(direct_args = None):
    return expanded_path(direct_args.output)


def get_complete_flag(direct_args = None):
    return direct_args.complete


def get_library_flag(direct_args = None):
    return direct_args.library


def get_no_buttons_flag(direct_args = None):
    return direct_args.no_buttons


def get_quiet_flag(direct_args = None):
    return direct_args.quiet


# -----------------------------------------------------------------------------
# Helper classes
# -----------------------------------------------------------------------------
# This provides handling for specific plotting libraries.  Currently we know
# about Highcharts and Flot.  The library-specific bits are separated into
# classes, one for each of the plotting libraries.

class PlotWriter():
    our_colors = [ '#4572A7' 
                   , '#AA4643' 
                   , '#89A54E' 
                   , '#80699B' 
                   , '#3D96AE' 
                   , '#DB843D' 
                   , '#92A8CD' 
                   , '#A47D7C' 
                   , '#B5CA92'
                   , '#F9B7B0'
                   , '#A9BDE6'
                   , '#A6EBB5'
                   , '#F9E0B0'
                   , '#F97A6D'
                   , '#7297E6'
                   , '#67EB84'
                   , '#F9C96D'
                   , '#E62B17'
                   , '#1D4599'
                   , '#11AD34'
                   , '#E69F17'
                   , '#8F463F'
                   , '#2F3F60'
                   , '#2F6C3D'
                   , '#8F743F'
                   , '#6D0D03'
                   , '#031A49'
                   , '#025214'
                   , '#6D4903'
                   ]


    def __init__(self, library_name, complete):
        self.library   = library_name
        self.complete  = complete


    def open(self, file_name):
        self.file_name = file_name
        self.file = open(file_name, 'w')
        if self.library == 'flot':
            self.generator = FlotPlotGenerator(self.file)
        else:
            self.generator = HighchartsPlotGenerator(self.file)


    def close(self):
        self.file.close()


    def write_html_start(self, title = ''):
        if self.complete:
            self.file.write('<html><title>' + title + '</title>\n<body>')


    def write_code_start(self, column_labels, buttons):
        self.generator.write_code_start(column_labels, buttons)


    def write_data(self, column_labels, time, expected_values, other_values):
        colors = cycle(self.our_colors)
        for col in range(1, len(column_labels)):
            if col > 1: self.file.write(',')
            label = column_labels[col]
            color = colors.next()
            self.generator.write_series_start(label, 'Solid', color)
            for row in range(0, len(expected_values[label])):
                if row: self.file.write(',')
                self.file.write('\n[' + time[row] + ', '
                                + expected_values[label][row] + ']')
            self.generator.write_series_stop()
            if any(other_values):
                self.file.write(',')
                self.generator.write_series_start(label + ' (u)', 'ShortDash', color)
                for row in range(0, len(other_values[label])):
                    if row: self.file.write(',')
                    self.file.write('\n[' + time[row] + ', '
                                    + other_values[label][row] + ']')
                self.generator.write_series_stop()


    def write_code_end(self, column_labels):
        self.generator.write_code_end(column_labels)


    def write_html_body(self):
        self.file.write('''
<div id="plot-wrapper">
<div id="info-text" class="no-print">
Drag the mouse to zoom in on a rectangular region.<br>
Click on variable names in the legend to toggle their visibility.
</div>
<div id="placeholder"></div>
<div id="legend"></div>
</div>
''')


    def write_html_end(self):
        if self.complete:
            self.file.write('</body>\n</html>\n')


    def write_empty_result(self):
        self.file.write('''
<svg version="1.1" id="Layer_1"
     xmlns="http://www.w3.org/2000/svg"
     xmlns:xlink="http://www.w3.org/1999/xlink"
     width="66.906738" height="66.906738"
     viewBox="0 0 66.906738 66.906738"
     style="overflow:visible;enable-background:new 0 0 66.906738 66.906738;"
     xml:space="preserve">
    <g>
     <circle style="fill:#B51729;" cx="33.453613" cy="33.453369" r="33.453091"/>
     <circle style="fill:#FFFFFF;" cx="33.453125" cy="33.453125" r="26.499931"/>
    <polyline style="fill:#B51729;"
     points="15.185547,10.589355 55.844727,51.248535 51.249023,55.844727 10.589844,15.185547"/>
    </g>
</svg>
''')


#
# Base class for the library-specific plot generators.
#

class PlotGenerator():

    def __init__(self, file):
        self.file = file


#
# Generator for Flot (http://flotcharts.org)
# 
 
class FlotPlotGenerator(PlotGenerator):

    # Most, but not everything in Flot, is easily styled using CSS.  For some
    # other things, Flot takes arguments to FLot function calls and outputs
    # style="..."  attributes on the HTML elements.  Thus, there are two
    # places where styling is controlled.

    def write_code_start(self, column_labels, buttons):
        self.file.write('''
<script language="javascript" src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script language="javascript" src="http://cdnjs.cloudflare.com/ajax/libs/flot/0.7/jquery.flot.min.js"></script>
<style>
body {
   background-color: white;   
}

#placeholder {
   height: 400px;
   width: 600px;
   background-color: white;
}

.tickLabels, #legend {
    font-family: Helvetica, Verdana, sans-serif;
}

.xAxis, .yAxis {
    color: #545454 !important;
}

#legend {
    margin: 10 10 10 0;
}

.legendLabel {
    color: #666 !important;
    padding-right: 10px;
}

#tooltip {
    position: absolute;
    display: none;
    border: 1px solid #fdd;
    padding: 2px;
    background-color: #fee;
    opacity: 0.80;
    font-family: Helvetica, Verdana, sans-serif;
}
</style>
<script>
$(function () {
''')

    def write_series_start(self, label):
        self.file.write('var ' + label + ' = [')


    def write_series_stop(self):
        self.file.write('];\n')


    def write_code_end(self, column_labels):
        self.file.write('''
function doPlot() {
    $.plot($("#placeholder"),
           [
''')
        for label in column_labels[1:]:
            self.file.write(15*' ' + '{ data: ' + label + ', label: \"'
                            + label + '\", shadowSize: 0 },\n')
        self.file.write('''           ],
           { 
               series: { points: { show: false, radius: 2 },
                         lines: { show: true } },
               legend: { container: "#legend", noColumns: 8 },
               crosshair: { mode: "x" },
               grid: { backgroundColor: "#fff", color: "#999",
                       borderColor: "#ccc", borderWidth: 1,
                       hoverable: true, clickable: true}
           });
    }

    function showTooltip(x, y, contents) {
        $('<div id="tooltip">' + contents + '</div>').css( {
            top: y + 5,
            left: x + 5
        }).appendTo("body").fadeIn(200);
    }

    var previousPoint = null;
    $("#placeholder").bind("plothover", function (event, pos, item) {
        $("#x").text(pos.x.toFixed(2));
        $("#y").text(pos.y.toFixed(2));

        if (item) {
            if (previousPoint != item.dataIndex) {
                previousPoint = item.dataIndex;
                
                $("#tooltip").remove();
                var x = item.datapoint[0].toFixed(2),
                    y = item.datapoint[1].toFixed(2);
                
                showTooltip(item.pageX, item.pageY,
                            item.series.label + " at time " + x + " = " + y);
            }
        }
        else {
            $("#tooltip").remove();
            previousPoint = null;            
        }
    });

    doPlot();
    $("button").click(function () { doPlot(); });

});
</script>
''')

#
# Generator for Highcharts JS (http://highcharts.com)
#

class HighchartsPlotGenerator(PlotGenerator):

    def write_code_start(self, column_labels, buttons):
        self.file.write('''
<script src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script src="http://code.highcharts.com/highcharts.js"></script>
<script src="http://code.highcharts.com/modules/exporting.js"></script>
<style>
body {
   background-color: white;   
   font-family: Helvetica, Verdana, sans-serif;
   font-size: 10pt;
}
@media print {
  .no-print { display: none; }
}
#plot-wrapper {
  display: inline-block;
}
#placeholder {
  margin-top: 0.25em;
}
#info-text {
  text-align: center;
  position: absolute;
  z-index: 100;
  left: 170px;
  color: #bbb;''')
        if not buttons:
            self.file.write('''
  display: none;''')
        self.file.write('''
}
</style>
<script>
$(function () {
    var chart;
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                backgroundColor: '#fff',
                renderTo: 'placeholder',
                type: 'line',
                zoomType: 'xy',
                height: 500,
                width: 600,
                spacingTop: 37
            },
            plotOptions: {
                line: {
                    dashStyle: 'Solid',
                    marker: {
                        enabled: false
                    }
                }
            },
            title: {
                text: null
            },''')
        self.file.write('''
            xAxis: {
                gridLineWidth: 1,
                gridLineDashStyle: 'ShortDot',
                tickPosition: 'inside',
                maxPadding: 0,
                lineWidth: 0
            },
            yAxis: {
                gridLineWidth: 1,
                gridLineDashStyle: 'ShortDot',
                tickPosition: 'inside',
                title: {
                    text: null
                }
            },
            tooltip: {
                borderWidth: 1,
                formatter: function() {
                    return 'At time ' + this.x + '<br><b>' + this.series.name
                           + '<\/b> = ' + this.y;
                }
            },
            legend: {
                borderWidth: 0,
                margin: 10,
                itemWidth: 140,
                itemMarginBottom: 10,
                symbolWidth: 45,
                symbolPadding: 5,
                x: 20
            },
            credits: {
                enabled: false
            },''')
        if buttons:
            self.file.write('''
            exporting: {
                buttons: {
                    exportButton: { y: 5 },
                    printButton: { y: 5 }
                }
            },''')
        else:
            self.file.write('''
            exporting: {
                buttons: {
                    exportButton: { enabled: false },
                    printButton: { enabled: false }
                }
            },''')
        self.file.write('''
            series: [
''')


    def write_series_start(self, label, style, color):
        self.file.write('\n{ name: "' + label + '"' 
                        + ', color: "' + color + '"'                        
                        + ', dashStyle: "' + style + '"'
                        + ('' if style == 'Solid' else ', lineWidth: 4')
                        + ', shadow: false, data: [')


    def write_series_stop(self):
        self.file.write('] }')


    def write_code_end(self, column_labels):
        self.file.write('''
]
        });
    });
    
});
</script>
''')

# '''


# -----------------------------------------------------------------------------
# Utility functions and misc. things.
# -----------------------------------------------------------------------------

def valid_file(file):
    if not os.path.exists(file):   return False
    elif not os.path.isfile(file): return False
    else:                          return True


def expanded_path(path):
    if path: return os.path.expanduser(os.path.expandvars(path))
    else:    return None


def stop(message, be_quiet):
    if not be_quiet:
        print("Error: " + message)
        print("Quitting.")
    sys.exit(1)


def parse_data_file(csv_file):
    # Read the contents into 3 structures and return all 3:
    #   column_labels: a vector of unique column labels.  The first one
    #       will (or should be) 'time'
    #
    #   time: a vector of the time points.
    # 
    #   values: a dictionary of data values keyed by the column labels.
    #       We skip the first column because we keep it in the separate
    #       'time' vector, so this dictionary stores just columns 2-n,
    #       where n is the total number of columns in the CSV file.
    #
    # If the data contains an infinite value ("INF" or a variant), then
    # this returns an empty array for the "values", as an indicator.
    #
    # This also checks for the possibility that more than one column with
    # the same label exists; if that's the case, it skips subsequent columns
    # and returns the value from the first one only.
    #
    column_labels = []
    time = []
    values = {}
    duplicate_columns = []
    with open(csv_file, 'r') as csvfile:
        contents = csv.reader(csvfile, delimiter = ',')
        seen_labels = []
        unique_column_labels = []

        column_labels = contents.next();
        # In the next line, the fake 1st entry is just to make indexing
        # correspond 1-1 with column_labels, whose first column we skip.
        duplicate_columns = [False]
        for label in column_labels[1:]:
            values[label] = []
            duplicate_columns.append(label in seen_labels)
            seen_labels.append(label)

        data_column_range = range(1, len(column_labels))
        for row in contents:
            if not any(row):
                continue
            time.append(row[0])
            for i in data_column_range:
                if not duplicate_columns[i]:
                    if str(row[i]).upper().find("INF") != -1:
                        # We can't plot "INF" values, so bail out.
                        return column_labels, time, []
                    else:
                        values[column_labels[i]].append(row[i])

        for i in range(0, len(column_labels)):
            if not duplicate_columns[i]:
                unique_column_labels.append(column_labels[i])

    return unique_column_labels, time, values


# -----------------------------------------------------------------------------
# The front-end interface.
# -----------------------------------------------------------------------------

def main():
    # Start by reading the command line arguments.

    args          = parse_cmdline()
    data_fname    = get_data_file_name(args)
    results_fname = get_results_file_name(args)
    plot_fname    = get_output_file_name(args)
    library_name  = get_library_flag(args)
    quietly       = get_quiet_flag(args)
    complete      = get_complete_flag(args)
    show_buttons  = not get_no_buttons_flag(args)

    # Sanity-check the arguments.

    if not valid_file(data_fname):
        stop("cannot read file '" + data_fname + "'", quietly)
    elif re.search('.csv$', data_fname) == None:
        stop("'" + data_fname + "' is not a .csv file", quietly)
    elif re.search('.html$', plot_fname) == None:
        stop("'" + plot_fname + "' is not a .html file", quietly)

    if results_fname != None:
        if not valid_file(results_fname):
            stop("cannot read file '" + results_fname + "'", quietly)
        elif re.search('.csv$', results_fname) == None:
            stop("'" + results_fname + "' is not a .csv file", quietly)

    # Parse the CSV data files and do some simple error checking to
    # make sure the results look like they belong together.

    column_labels, time, data_values = parse_data_file(data_fname)

    # Only read the 2nd data file if we read some data:
    r_column_labels = []
    r_time          = []
    r_values        = []
    if results_fname != None and any(data_values):
        r_column_labels, r_time, r_values = parse_data_file(results_fname)

        if set(r_column_labels).symmetric_difference(column_labels) != set():
            stop("'" + data_fname + "' and '" + results_fname \
                 + " do not have the same column labels.", quietly)

        # Time steps are trickier to compare because they're floating point
        # numbers.  In the code for the rest of the SBML Test Suite, we use
        # tolerances read from the .m files, but here we don't have those
        # values and therefore can't apply the same algorithm.  So all we can
        # do is compare the number of time steps and hope for the best.

        if len(time) != len(r_time):
            stop("'" + data_fname + "' and '" + results_fname \
                 + " do not have the same number of time steps/rows.", quietly)

    if not quietly:
        if results_fname != None:
            print "Plotting '" + data_fname + "' and '" + results_fname + "':"
        else:
            print "Plotting '" + data_fname + "':"

        if not any(data_values):
            print "   Data contains unplottable values (e.g., INF)."
        else:
            print "   " + str(len(time)) + " time points"
            print "   " + str(len(column_labels) - 1) + " variables: " \
                + ' '.join(column_labels[1:])

    # Get down to business and write the output.

    writer = PlotWriter(library_name, complete)
    writer.open(plot_fname)
    writer.write_html_start(data_fname)
    if data_values:
        writer.write_code_start(column_labels, show_buttons)
        writer.write_data(column_labels, time, data_values, r_values)
        writer.write_code_end(column_labels)
        writer.write_html_body()
    else:
        writer.write_empty_result()
    writer.write_html_end()
    writer.close()

    if not quietly:
        print "Wrote '" + plot_fname + "'."


if __name__ == '__main__':
    main()
