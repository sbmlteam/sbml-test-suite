## @file    docs/src/Makefile
## @brief   Makefile for the SBML Test Suite documentation 
## @author  Michael Hucka
## 
## $Id$
## $HeadURL$
##
## ----------------------------------------------------------------------------
## This file is part of the SBML Test Suite.  Please visit http://sbml.org for
## more information about SBML, and the latest version of the SBML Test Suite.
## 
## Copyright 2008      California Institute of Technology.
## Copyright 2004-2007 California Institute of Technology (USA) and
##                     University of Hertfordshire (UK).
## 
## The SBML Test Suite is free software; you can redistribute it and/or
## modify it under the terms of the GNU Lesser General Public License as
## published by the Free Software Foundation.  A copy of the license
## agreement is provided in the file named "LICENSE.txt" included with
## this software distribution and also available at
## http://sbml.org/Software/SBML_Test_Suite/license.html
## ----------------------------------------------------------------------------

docs: docs-software docs-suite

version = `cat ../../VERSION.txt`

# Overall SBML Test Suite documentation

docs-suite: # (Not yet done.)

# Application documentation

docs-software: docs-standalone

# This runs Javadoc on the .java files in src/front-ends/standalone and
# doesn't need doxygen at all.
 
sa-src-dir        = ../../src/front-ends/standalone/src
sa-doc-output-dir = ../formatted/standalone
 
sa-doctitle       = SBML Test Suite Standalone Application
sa-header         = <B>$(sa-doctitle) version $(version)<B>
sa-footer         = `cat api/javadoc-footer.html`
sa-windowtitle    = $(sa-doctitle) Programmer's Manual

docs-standalone:
	javadoc -use -public -version -author -sourcetab 4 -keywords \
	    -header "$(sa-header)" \
	    -bottom "$(sa-footer)" \
	    -windowtitle "$(sa-windowtitle)" \
	    -doctitle "$(sa-doctitle)" \
	    -d $(sa-doc-output-dir) \
	    -sourcepath $(sa-src-dir) sbml.test

# ----------------------------------------------------------------------------- 
# End. 
# ----------------------------------------------------------------------------- 
 
## The following is for [X]Emacs users.  Please leave in place. 
## Local Variables: 
## mode: Makefile 
## End: 
 
