## @file    Makefile
## @brief   Top-level Makefile for the SBML Test Suite
## @author  Michael Hucka
## 
## $Id$
## $URL$
##
## ----------------------------------------------------------------------------
## This file is part of the SBML Test Suite.  Please visit http://sbml.org for
## more information about SBML, and the latest version of the SBML Test Suite.
##
## Copyright (C) 2010-2011 jointly by the following organizations: 
##     1. California Institute of Technology, Pasadena, CA, USA
##     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
##
## Copyright (C) 2008-2009 California Institute of Technology (USA).
##
## Copyright (C) 2004-2007 jointly by the following organizations:
##     1. California Institute of Technology (USA) and
##     2. University of Hertfordshire (UK).
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
# Run 'make plots' for regenerating the JPG plot images from CSV files.

#
# 'make html'
#

define make_html
  ./src/utilities/desc2html/desc2html.sh $(addsuffix .m,$(basename $(1))) $(1)
endef

cases-m-files    = $(wildcard cases/semantic/*/*-model.m)
cases-html-files = $(addsuffix .html,$(basename $(cases-m-files)))

html: $(cases-html-files)

cases/semantic/%-model.html: cases/semantic/%-model.m
	$(call make_html,$@)

#
# 'make plots'
#

define make_plot
  ./src/utilities/plotresults/plotresults.sh $(addsuffix .csv,$(basename $(1)))
endef

cases-csv-files = $(wildcard cases/semantic/*/*-results.csv)
cases-jpg-files = $(patsubst %-results.csv,%-plot.jpg,$(cases-csv-files))

plots: $(cases-jpg-files)

cases/semantic/%-plot.jpg: cases/semantic/%-results.csv
	$(call make_plot,$(patsubst %-plot.jpg,%-results.csv,$@))


# -----------------------------------------------------------------------------
# Case archive, for the test runner
# -----------------------------------------------------------------------------
# Use 'make cases-dist' for generating the zip archive of just the test
# cases, for update distributions by the test runner.  The file
# ".zipexcludes" in this directory contains a list of files to be excluded
# from the zip archive created.

today    = $(shell date +"%F")
ts-file  = .cases-archive-date
map-file = cases/semantic/.cases-tags-map
contents = cases/semantic \
	   $(ts-file)     \
	   $(map-file)    \
           COPYING.html   \
           COPYING.txt    \
           NEWS.txt       \
           LICENSE.txt

cases-dist: html plots
	@echo $(today) > $(ts-file)
	make $(map-file)
	zip -r sbml-test-cases-$(today).zip $(contents) -x@.zipexcludes
	@echo "---------------------------------------------------------------"
	@echo "Next: upload zip file to SourceForge as updated test cases dist."
	@echo "Please don't forget to do 'svn commit' for the time-stamp file."
	@echo "---------------------------------------------------------------"


$(map-file): $(ts-file)
	@echo "Making tags map file:"
	src/utilities/make-tag-map/make-tag-map.sh $(map-file)


# -----------------------------------------------------------------------------
# Standalone Application distribution
# -----------------------------------------------------------------------------

dist-dir   = SBMLTestSuite-$(shell cat VERSION.txt)
dist-files = README.txt \
	     AUTHORS.txt \
	     COPYING.html \
	     COPYING.txt \
	     FUNDING.txt \
	     LICENSE.txt \
	     NEWS-OLD.txt \
	     NEWS.txt \
	     VERSION.txt \
	     docs/formatted/standalone-user-manual
dist-jar   = src/front-ends/standalone/dist/SBMLTestSuite.jar

standalone-dist:
	mkdir -p $(dist-dir)
	cp -r $(dist-files) $(dist-dir)
	cp $(dist-jar) $(dist-dir)
	zip -r $(dist-dir).zip $(dist-dir)

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
# Miscellaneous developers' operations
# -----------------------------------------------------------------------------

#
# 'make svn-ignores'
#
# This is to set up svn properties to ignore files that are generated
# via this makefile but (when generated) may be left in the developer's
# svn sandbox.  If left around, svn then complains the files are unknown/new,
# which is annoying if you're looking at several hundred of them.  This only
# needs to be executed when new case files are added to the test suite.
# 

cases-dirs = $(wildcard cases/semantic/*)
tmpfile    = .tmp.make.ignores

svn-ignores: $(cases-html-files) $(cases-jpg-files)
	@list='$(cases-dirs)'; for dir in $$list; do \
	  name=`basename $$dir`; \
          svn propget svn:ignore $$dir |\
	    grep -v $$name-plot.jpg | grep -v $$name-model.html |\
            egrep -v '^$$' >| $(tmpfile); \
	    echo $$name-plot.jpg >> $(tmpfile); \
	    echo $$name-model.html >> $(tmpfile); \
	  svn propset -F $(tmpfile) svn:ignore $$dir; \
	done
	@rm -f $(tmpfile)


# -----------------------------------------------------------------------------
# Common special targets
# -----------------------------------------------------------------------------

.PHONY: docs html plots

.SUFFIXES: .jpg .csv .html .xml .txt

# ----------------------------------------------------------------------------- 
# End. 
# ----------------------------------------------------------------------------- 
 
## The following is for [X]Emacs users.  Please leave in place. 
## Local Variables: 
## mode: Makefile 
## End: 
 
