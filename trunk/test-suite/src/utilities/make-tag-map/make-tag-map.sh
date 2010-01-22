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
specific tags.  It then constructs a file that lists every tag followed by
every case that contains that tag in its model definition file.  The
format of this tags map file is

   tagname: XXXXX YYYYY ZZZZZ ...

where XXXXX, YYYYY, ZZZZZ and so on are case numbers.  The algorithm used
to do this is extremely simple minded and not at all efficient, but this
operation is infrequently needed (roughly every time a new distribution of
case files is made by the developers) so efficiency is not deemed to be
an important concern.  Just start it up, go get a beverage, and when you
return it should be done :-).

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

levels="1.2 2.1 2.2 2.3 2.4"

comptags="FunctionDefinition InitialAssignment AssignmentRule RateRule AlgebraicRule EventWithDelay EventNoDelay Compartment Species Reaction Parameter"

testtags="2D-Compartment 1D-Compartment 0D-Compartment NonConstantCompartment NonUnityCompartment InitialAmount InitialConcentration HasOnlySubstanceUnits BoundaryCondition ConstantSpecies NonConstantParameter FastReaction ReversibleReaction NonUnityStoichiometry"


# The following is because the stupid shell's built-in echo doesn't seem to
# understand the -n option, at least on MacOS 10.5.  Foo.

e=/bin/echo

# -----------------------------------------------------------------------------
# Main body.
# -----------------------------------------------------------------------------

rm -f $mapfile
cd cases/semantic

$e -n "Looking at levels tags "
for tag in $levels; do
  $e -n "."; \
  list=""; \
  for case in *; do \
    if test -n "`egrep $tag $case/$case-model.m`"; then \
      list="$list $case"; \
    fi; \
  done; \
  $e "$tag:$list" >> $mapfile; \
done
$e ""

$e -n "Looking at component tags "
for tag in $comptags; do
  $e -n "."; \
  list=""; \
  for case in *; do \
    if test -n "`egrep $tag $case/$case-model.m`"; then \
      list="$list $case"; \
    fi; \
  done; \
  $e "$tag:$list" >> $mapfile; \
done
$e ""

$e -n "Looking at test tags "
for tag in $testtags; do
  $e -n "."; \
  list=""; \
  for case in *; do \
    if test -n "`egrep $tag $case/$case-model.m`"; then \
      list="$list $case"; \
    fi; \
  done; \
  $e "$tag:$list" >> $mapfile; \
done
$e ""

$e "Done. The output is in '$mapfile'."
