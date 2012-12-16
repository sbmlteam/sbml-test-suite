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

# -----------------------------------------------------------------------------
# Command-line argument handling.
# -----------------------------------------------------------------------------

__desc = '''Create a plot of the data stored in an SBML Test Suite case results
CSV file.  The plot is in the form of an HTML file that uses JavaScript to
draw the curves and provide additional capabilities.'''

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
                        help="omit interactive buttons from the HTML output")

    parser.add_argument("-d", "--data", required=True,
                        help="specify the CSV file containing the data to plot")

    parser.add_argument("-s", "--second", 
                        help="(optional) specify second data CSV file to plot")

    parser.add_argument("-o", "--output", required=True,
                        help="specify the file where to write the plot")

    parser.add_argument("-l", "--library", default="highcharts",
                        help="library to use: 'highcharts' (default) or 'flot'")

    return parser.parse_args(direct_args)


def get_data_file_name(direct_args = None):
    return expanded_path(direct_args.data)


def get_output_file_name(direct_args = None):
    return expanded_path(direct_args.output)


def get_second_file_name(direct_args = None):
    return expanded_path(direct_args.second)


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

    def write_html_start(self):
        if self.complete:
            self.file.write('<html>\n<body>')

    def write_code_start(self, column_labels, buttons):
        self.generator.write_code_start(column_labels, buttons)

    def write_data(self, column_labels, time, values):
        for col in range(1, len(column_labels)):
            if col > 1: self.file.write(',')
            label = column_labels[col]
            self.generator.write_series_start(label)
            for row in range(0, len(values[label])):
                if row: self.file.write(',')
                self.file.write('\n[' + time[row] + ', ' + values[label][row] + ']')
            self.generator.write_series_stop()

    def write_code_end(self, column_labels):
        self.generator.write_code_end(column_labels)

    def write_html_end(self):
        self.file.write('<div id="placeholder"></div>\n<div id="legend"></div>\n')
        if self.complete:
            self.file.write('</body>\n</html>\n')

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
<script type="text/javascript">
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
<script language="javascript" src="http://code.jquery.com/jquery-1.8.3.min.js"></script>
<script language="javascript" src="http://code.highcharts.com/highcharts.js"></script>
<script language="javascript" src="http://code.highcharts.com/modules/exporting.js"></script>
<script type="text/javascript">
$(function () {
    var chart;
    $(document).ready(function() {
        chart = new Highcharts.Chart({
            chart: {
                renderTo: 'placeholder',
                type: 'line',
                marginRight: 5,
                zoomType: 'xy',
                height: 500,
                width: 600,
                spacingTop: 25
            },
            colors: [
            '#4572A7', 
            '#AA4643', 
            '#89A54E', 
            '#80699B', 
            '#3D96AE', 
            '#DB843D', 
            '#92A8CD', 
            '#A47D7C', 
            '#B5CA92',
            '#F9B7B0',
            '#A9BDE6',
            '#A6EBB5',
            '#F9E0B0',
            '#F97A6D',
            '#7297E6',
            '#67EB84',
            '#F9C96D',
            '#E62B17',
            '#1D4599',
            '#11AD34',
            '#E69F17',
            '#8F463F',
            '#2F3F60',
            '#2F6C3D',
            '#8F743F',
            '#6D0D03',
            '#031A49',
            '#025214',
            '#6D4903'
            ],
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
        if buttons:
            self.file.write('''
            subtitle: {
                floating: true,
                y: -10,
                x: 25,
                text: 'Drag the mouse to zoom in on a rectangular region',
                style: { color: '#bbb' }
            },''')
        self.file.write('''
            xAxis: {
                gridLineWidth: 1,
                gridLineDashStyle: 'ShortDot',
                tickPosition: 'inside',
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
                itemWidth: 100,
                itemMarginBottom: 5,
                symbolWidth: 50
            },
            credits: {
                enabled: false
            },''')
        if buttons:
            self.file.write('''
            exporting: {
                buttons: {
                    exportButton: { y: 0 },
                    printButton: { y: 0 }
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


    def write_series_start(self, label):
        self.file.write('\n{ name: "' + label + '", shadow: false, data: [')


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

# -----------------------------------------------------------------------------
# Body
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
    sys.exit(1)


def parse_data_file(csv_file):
    # Read the contents into 3 structures and return all 3:
    #   column_labels: a vector of the column labels.  The first one
    #       will (or should be) 'time'
    #
    #   time: a vector of the time points.
    # 
    #   values: a dictionary of data values keyed by the column labels.
    #       We skip the first column because we keep it in the separate
    #       'time' vector, so this dictionary stores just columns 2-n,
    #       where n is the total number of columns in the CSV file.
    column_labels = []
    time = []
    values = {}
    with open(csv_file, 'r') as csvfile:
        contents = csv.reader(csvfile, delimiter = ',')

        column_labels = contents.next();
        for label in column_labels[1:]:
            values[label] = []

        data_column_range = range(1, len(column_labels))
        for row in contents:
            time.append(row[0])
            for i in data_column_range:
                values[column_labels[i]].append(row[i])

    return column_labels, time, values


def main():
    # Start by reading the command line arguments.

    args              = parse_cmdline()
    data_fname        = get_data_file_name(args)
    second_data_fname = get_second_file_name(args)
    plot_fname        = get_output_file_name(args)
    library_name      = get_library_flag(args)
    quietly           = get_quiet_flag(args)
    complete          = get_complete_flag(args)
    show_buttons      = not get_no_buttons_flag(args)

    # Sanity-check the arguments.

    if not valid_file(data_fname):
        stop("cannot read file '" + data_fname + "'", quietly)
    elif re.search('.csv$', data_fname) == None:
        stop("'" + data_fname + "' is not a .csv file", quietly)
    elif re.search('.html$', plot_fname) == None:
        stop("output file name should end in .html", quietly)

    if second_data_fname != None:
        if not valid_file(second_data_fname):
            stop("cannot read file '" + second_data_fname + "'", quietly)
        elif re.search('.csv$', second_data_fname) == None:
            stop("'" + second_data_fname + "' is not a .csv file", quietly)

    # Parse the expected results CSV data file.
    # FIXME -- STILL TO DO: parse the second CSV file.

    column_labels, time, values = parse_data_file(data_fname)

    # Get down to business and write the output.

    writer = PlotWriter(library_name, complete)
    writer.open(plot_fname)
    writer.write_html_start()
    writer.write_code_start(column_labels, show_buttons)
    writer.write_data(column_labels, time, values)
    writer.write_code_end(column_labels)
    writer.write_html_end()
    writer.close()

    # Close out by printing some general information.

    if not quietly:
        print "Plotted " + str(len(column_labels) - 1) + " variables at " \
            + str(len(time)) + " time points to file '" + plot_fname + "'."


if __name__ == '__main__':
    main()
