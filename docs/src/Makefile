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
 
sa-doctitle       = SBML Test Suite<BR>Standalone Application
sa-header         = <B>$(sa-doctitle)<BR>version $(version)<B>
sa-footer         = `cat api/standalone-footer.html`
sa-windowtitle    = $(sa-doctitle) Programmer's Manual

define merge_html
  cat api/standalone-top.html $(1) api/standalone-bottom.html api/standalone-footer.html \
  | sed -e "s/%%version%%/$(version)/g" > $(2)
endef

docs-standalone:
	javadoc -use -public -version -author -sourcetab 4 -keywords \
	    -header "$(sa-header)" \
	    -bottom "$(sa-footer)" \
	    -windowtitle "$(sa-windowtitle)" \
	    -doctitle "$(sa-doctitle)" \
	    -overview api/standalone-fake-overview.html \
	    -stylesheetfile api/standalone.css \
	    -d $(sa-doc-output-dir) \
	    -sourcepath $(sa-src-dir) sbml.test
	cp -f api/sbml-logo-long-124x56.gif $(sa-doc-output-dir)/sbml/test
	cp -f api/standalone.css $(sa-doc-output-dir)
	$(call merge_html,api/standalone-overview.html,$(sa-doc-output-dir)/overview-summary.html)

# ----------------------------------------------------------------------------- 
# End. 
# ----------------------------------------------------------------------------- 
 
## The following is for [X]Emacs users.  Please leave in place. 
## Local Variables: 
## mode: Makefile 
## End: 
 
