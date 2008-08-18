## @file    Makefile
## @brief   Top-level Makefile for the SBML Test Suite
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

# -----------------------------------------------------------------------------
# Commads for generating case descriptions in HTML format.
# -----------------------------------------------------------------------------
# Run 'make html' for regenerating HTML documentation of every test case.

define make_html
  ./src/utilities/desc2html/desc2html.sh $(addsuffix .m,$(basename $(1))) $(1)
endef

cases-m-files    = $(wildcard cases/semantic/*/*-model.m)
cases-html-files = $(addsuffix .html,$(basename $(cases-m-files)))

to_make_html     = $(addprefix make-,$(cases-html-files))

html: $(to_make_html)

$(to_make_html):
	$(call make_html,$(subst make-,,$@))


# -----------------------------------------------------------------------------
# Case archive, for the test runner
# -----------------------------------------------------------------------------
# Use 'make cases-dist' for generating the zip archive of just the test
# cases, for update distributions by the test runner.  The file
# ".zipexcludes" in this directory contains a list of files to be excluded
# from the zip archive created.

today    = $(shell date +"%F")
ts-file  = .cases-archive-date
contents = cases/semantic \
	   $(ts-file)     \
           COPYING.html   \
           COPYING.txt    \
           LICENSE.txt

cases-dist: html
	@echo $(today) > $(ts-file)
	zip -r sbml-test-cases-$(today).zip $(contents) -x@.zipexcludes
	@echo "---------------------------------------------------------------"
	@echo "Next: upload zip file to SourceForge as updated test cases dist."
	@echo "Please don't forget to do 'svn commit' for the time-stamp file."
	@echo "---------------------------------------------------------------"


# -----------------------------------------------------------------------------
# make docs
# -----------------------------------------------------------------------------
# This generates user & programmer documentation for the test suite.

docs:; 
ifneq "$(MAKEFLAGS)" "" 
	$(MAKE) -w -C docs/src -$(MAKEFLAGS) $(MAKECMDGOALS) 
else 
	$(MAKE) -w -C docs/src $(MAKECMDGOALS) 
endif 


# -----------------------------------------------------------------------------
# Common special targets
# -----------------------------------------------------------------------------

.PHONY: docs


# ----------------------------------------------------------------------------- 
# End. 
# ----------------------------------------------------------------------------- 
 
## The following is for [X]Emacs users.  Please leave in place. 
## Local Variables: 
## mode: Makefile 
## End: 
 
