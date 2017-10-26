#!/bin/sh
#
# @file    desc2html.sh
# @brief   Converts embedded comments in SBML Test Suite .m files to HTML
# @author  Michael Hucka <mhucka@caltech.edu>
# 
# $Id$
# $HeadURL$
#
# ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~
# This file is part of the SBML Test Suite.  Please visit http://sbml.org for
# more information about SBML, and the latest version of the SBML Test Suite.
#
# Copyright 2008-2010 California Institute of Technology.
# 
# This library is free software; you can redistribute it and/or modify it
# under the terms of the GNU Lesser General Public License as published by
# the Free Software Foundation.  A copy of the license agreement is provided
# in the file named "LICENSE.txt" included with this software distribution
# and also available at http://sbml.org/Software/SBML_Test_Suite/license.html
# ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~ ~

USAGE_TEXT="Usage: `basename $0`  INPUT-FILE.m  OUTPUT-FILE.html

This program takes the input file (assumed be a .m file), extracts text
contained between the first pair of Mathematica comment delimiter sequences
encountered in the file, then strips any SBML Test Suite tags, converts the
rest to HTML using the Wiky processor (http://goessner.net/articles/wiky/),
and finally writes the result to the output file.

This free program is part of the SBML Test Suite and is distributed under
the terms of the LGPL.  For more information, please visit http://sbml.org/.
"

# -----------------------------------------------------------------------------
# Initialization
# -----------------------------------------------------------------------------

JAVA=java
AWK=awk
RHINO=src/imported-libs/rhino/js.jar
CONVERTER=src/imported-libs/wiky/wiky.js

# -----------------------------------------------------------------------------
# Main body.
# -----------------------------------------------------------------------------

# Convenience variables -- not for configuration:

MAIN=org.mozilla.javascript.tools.shell.Main
TMPFILE=/tmp/sbmltestconvert$$

# Did we get the required number of arguments?

if [ -z "$1" ] || [ -z "$2" ]; then
    echo "$USAGE_TEXT"
    exit 1
fi

INFILE=$1
OUTFILE=$2

# Set trap for cleanup

trap '
  rm -rf $TMPFILE
  exit 1
' 1 2 3 7 13 15

# The awk part pulls out the content of the file between (* and *).
# The (* and *) must be the first characters on a line.
# When lines beginning with "category:", etc., are encountered, it starts
# skipping until the next blank line encountered.

$AWK '/^(\*\))/            { exit };         \
      /^category:/         { printing = 0 }; \
      /^synopsis:/         { printing = 0 }; \
      /^component[Tt]ags:/ { printing = 0 }; \
      /^test[Tt]ags:/      { printing = 0 }; \
      /^test[Tt]ype:/      { printing = 0 }; \
      /^levels:/           { printing = 0 }; \
      printing == 1        { print $0 };     \
      /^[[:space:]]*$/     { printing = 1};  \
      /^(\(\*)/            { printing = 1 } ' < $INFILE > $TMPFILE

# Feed the result to the converter:

$JAVA -cp $RHINO $MAIN $CONVERTER $TMPFILE > $OUTFILE

# Clean up:

rm $TMPFILE
exit 0
