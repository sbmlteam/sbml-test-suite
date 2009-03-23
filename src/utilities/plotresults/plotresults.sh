#!/bin/sh
#
# @file    plotresults.sh
# @brief   Program for plotting SBML Test Suite case results using gnuplot
# @author  Michael Hucka <mhucka@caltech.edu>
# 
# $Id$
# $HeadURL$
#
# ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
# This file is part of the SBML Test Suite.  Please visit http://sbml.org for
# more information about SBML, and the latest version of the SBML Test Suite.
#
# Copyright 2008 California Institute of Technology.
# 
# This library is free software; you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation.  A copy of the license agreement is provided
# in the file named "LICENSE.txt" included with this software distribution
# and also available at http://sbml.org/Software/SBML_Test_Suite/license.html
# ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

USAGE_TEXT="Usage: `basename $0`  XXXXX-results.csv

This program takes the input file (assumed be a comma-separated value file)
and runs it through gnuplot to produce a plot in a JPEG file as output.
The output file is named after the basename of the input file.  The input
file should normally have names of the form XXXXX-results.csv.

This free program is part of the SBML Test Suite and is distributed under
the terms of the LGPL.  For more information, please visit http://sbml.org/.
"

# -----------------------------------------------------------------------------
# Initialization
# -----------------------------------------------------------------------------

GNUPLOT=gnuplot

# -----------------------------------------------------------------------------
# Main body.
# -----------------------------------------------------------------------------

# Did we get the required number of arguments?
# Or did the user ask for help?

if [ -z "$1" ] || [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    echo "$USAGE_TEXT"
    exit 1
fi

# Does it look like we were provided with a .csv file?

CSV_FILE=$1

if [ ! "${CSV_FILE/*./}" = "csv" ]; then
    echo \"$1\" does not appear to be a CSV file.
    echo ""
    echo "$USAGE_TEXT"
    exit 1
fi

# Make sure we have a sufficient version of gnuplot

VERS=`gnuplot --version | tr -d '\015'`
major_vers=`expr "$VERS" : 'gnuplot \([0-9]*\)\.[0-9]*'`
minor_vers=`expr "$VERS" : 'gnuplot [0-9]*\.\([0-9]*\)'`

if test 4 -gt $major_vers || test $major_vers -eq 4 && test 2 -gt $minor_vers
then
    echo Very sorry, but this script needs gnuplot version 4.2 or higher.
    echo Quitting.
    exit 1
fi

# OK, let's get started.

INPUTFILE="`echo $CSV_FILE | sed -e 's/-results/-plot/'`"

trap "rm -f $INPUTFILE; exit" INT TERM EXIT

# 1) Windows line-ending conventions screw up gnuplot, which reads the ^M
# at the end of the title line and ends up thinking it's a character in a
# column name.  (You end up with this weird little angle-bracket character
# as part of the last column's title.)  The following removes trailing
# ^M's.
#
# 2) While we're mucking with the content, let's rename the column titles
# from the Mathematica pattern of, e.g., "case00350`S1" to just "S1".

sed -e 's/case[0-9][0-9][0-9][0-9][0-9][`]//g' < $CSV_FILE | tr -d '\015' > $INPUTFILE

# OK, run gnuplot.  The last line (the plot command) is hackacious,
# but without more work, it's hard to see how to limit the number of
# plot lines to exactly what's in the data file.  The result of
# overdoing it like this is warnings by gnuplot about "Skipping data
# file with no valid points".  For now, this just ignores all warnings
# from gnuplot for this reason.

$GNUPLOT 2> /dev/null 1> /dev/null -<<EOF
set datafile separator ","
set key spacing 1.2
set key height 3
set key width 10
set key below
set rmargin 3
set bmargin 8
set lmargin 15
set size 0.9,0.9
set terminal jpeg
set output "${INPUTFILE/%.csv}.jpg"
plot "$INPUTFILE" using 1:2 title 2 with lines, "" using 1:3 title 3 with lines, "" using 1:4 title 4 with lines, "" using 1:5 title 5 with lines, "" using 1:6 title 6 with lines, "" using 1:7 title 7 with lines, "" using 1:8 title 8 with lines, "" using 1:9 title 9 with lines
EOF
