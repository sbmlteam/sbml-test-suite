#!/bin/sh
#
# @file    make-tag-map.sh
# @brief   Make a map of case files having specific tags
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

USAGE_TEXT="Usage: `basename $0`  MAPFILE

This program assumes it is being run from the root of the SBML Test Suite
source tree.  It decends into the 'cases/semantic' subdirectory and looks
through every case model definition file, searching for the presence of
specific tags.  It then writes to MAPFILE, on the first line by itself, the
highest numbered case number, followed on each subsequent line by a case
number and all the component tags, test tags and level tags contained in the
case's model definition file.  The result looks like

00900
00001 Compartment Species Reaction Parameter InitialAmount 1.2 2.1 2.2 2.3 2.4 
00002 Compartment Species Reaction Parameter InitialAmount 1.2 2.1 2.2 2.3 2.4 
....

and so on.  The number of lines in the file equals the number of test cases.

This free program is part of the SBML Test Suite and is distributed under
the terms of the LGPL.  For more information, please visit http://sbml.org/.
"

# -----------------------------------------------------------------------------
# Initialization
# -----------------------------------------------------------------------------

# Did we get the required number of arguments?
# Or did the user ask for help?

if [ -z "$1" ] || [ "$1" = "-h" ] || [ "$1" = "--help" ]; then
    echo "$USAGE_TEXT"
    exit 1
fi

mapfile=`pwd`/$1

# The following is because the stupid shell's built-in echo doesn't seem to
# understand the -n option, at least on MacOS 10.5.  Foo.

e=/bin/echo


# -----------------------------------------------------------------------------
# Main body.
# -----------------------------------------------------------------------------

rm -f $mapfile
cd cases/semantic

alltags=`egrep -h '^componentTags:|testTags:' */*.m | tr -d '\015' |\
    tr ',' '\012' | sed -e 's/componentTags://g;s/testTags://g' |\
    awk '{print $1}' | sort | uniq | grep -v '^$'`
$e $alltags > $mapfile

for case in *; do
    tags=`egrep '^componentTags:|^testTags:|^levels:' $case/$case-model.m |\
	tr -d '\015' | tr '\012' '\040' |\
        sed -e 's/componentTags://g;s/testTags://g;s/levels://g' |\
        sed -e 's/  */ /g;s/,//g'`; \
    $e "$case$tags" >> $mapfile; \
    $e -n "."; \
done; \

$e ""
$e "Done. The output is in '$mapfile'."
