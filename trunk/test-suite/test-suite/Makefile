## @file    Makefile
## @brief   Top-level Makefile for the SBML Test Suite
## @author  Michael Hucka
## 
## $Id$
## $HeadURL$
##
##<!---------------------------------------------------------------------------
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
##------------------------------------------------------------------------- -->

#
# Generate case descriptions in HTML format.
#

define make_html
  ./src/desc2html/desc2html.sh $(addsuffix .m,$(basename $(1))) $(1)
endef

cases-m-files    = $(wildcard cases/semantic/*/*-model.m)
cases-html-files = $(addsuffix .html,$(basename $(cases-m-files)))

to_make_html     = $(addprefix make-,$(cases-html-files))

html: $(to_make_html)

$(to_make_html):
	$(call make_html,$(subst make-,,$@))
