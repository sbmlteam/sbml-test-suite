## @file    Makefile
## @brief   Top-level Makefile for the SBML Test Suite
## @author  Michael Hucka
##
## ----------------------------------------------------------------------------
## This file is part of the SBML Test Suite.  Please visit http://sbml.org for
## more information about SBML, and the latest version of the SBML Test Suite.
##
## Copyright (C) 2010-2012 jointly by the following organizations: 
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
# Run 'make plots' for regenerating the PNG plot images from CSV files.

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

clean-html:
	rm -f $(cases-html-files)

#
# 'make plots'
#

# 2012-12-17 [MH] Completely changed the way the plots are done.  We now
# create an HTML page with the help of JavaScript plotting libraries (by
# default, Highcharts JS) and generate PNG images using PhantomJS.

cases-csv-files       = $(wildcard cases/semantic/*/*-results.csv)
cases-html-plot-files = $(patsubst %-results.csv,%-plot.html,$(cases-csv-files))
cases-png-plot-files  = $(patsubst %-results.csv,%-plot.png,$(cases-csv-files))

cases/semantic/%-plot.html: cases/semantic/%-results.csv \
		./src/utilities/plotresults/plotresults.sh
	./src/utilities/plotresults/plotresults.py -n -q -d $(patsubst %-plot.html,%-results.csv,$@) -o $@

cases/semantic/%-plot.png: cases/semantic/%-plot.html \
		./src/utilities/rasterize/rasterize.js
	phantomjs ./src/utilities/rasterize/rasterize.js $(patsubst %-plot.png,%-plot.html,$@) $@

htmlplots: $(cases-html-plot-files)

pngplots: $(cases-png-plot-files)

clean-plots:
	rm -f $(cases-html-plot-files)
	rm -f $(cases-png-plot-files)

#
# 'make sedml'
#

ifeq "`uname`" "Darwin"
  define make_sedml_files
    @echo "Creating SED-ML for $(1)"
    env DYLD_LIBRARY_PATH="src/utilities/sedml:$(DYLD_LIBRARY_PATH)" \
	mono ./src/utilities/sedml/GenerateSedML.exe -c `dirname $(1)` -a
  endef
else
  define make_sedml_files
    @echo "Creating SED-ML for $(1)"
    env LD_LIBRARY_PATH="src/utilities/sedml:$(DYLD_LIBRARY_PATH)" \
	mono ./src/utilities/sedml/GenerateSedML.exe -c `dirname $(1)` -a
  endef
endif

cases-sbml-files = $(wildcard cases/semantic/*/*-sbml-l[1234]v[0-9].xml)
all-sedml-files	:= $(patsubst %-l1v2.xml,%-l1v2-sedml.xml,$(cases-sbml-files))
all-sedml-files	:= $(patsubst %-l2v1.xml,%-l2v1-sedml.xml,$(all-sedml-files))
all-sedml-files	:= $(patsubst %-l2v2.xml,%-l2v2-sedml.xml,$(all-sedml-files))
all-sedml-files	:= $(patsubst %-l2v3.xml,%-l2v3-sedml.xml,$(all-sedml-files))
all-sedml-files	:= $(patsubst %-l2v4.xml,%-l2v4-sedml.xml,$(all-sedml-files))
all-sedml-files	:= $(patsubst %-l3v1.xml,%-l3v1-sedml.xml,$(all-sedml-files))

cases/semantic/%-sedml.xml: cases/semantic/%.xml src/utilities/sedml/GenerateSedML.exe
	$(call make_sedml_files,$@)

sedml: $(all-sedml-files)


# Note: a simple rm -f $(all-sedml-files) doesn't work -- arg list is too long.
clean-sedml:
	$(foreach f,$(wildcard cases/semantic/*/*-sedml.xml),$(shell rm -f $f))


# -----------------------------------------------------------------------------
# Case archive, for the test runner
# -----------------------------------------------------------------------------
# Use 'make cases-dist' for generating the zip archive of just the test
# cases, for update distributions by the test runner.  The file
# ".zipexcludes" in this directory contains a list of files to be excluded
# from the zip archive created.

today		= $(shell date +"%F")
cases-dist-name = sbml-test-cases-$(today).zip
ts-file		= .cases-archive-date
map-file	= cases/semantic/.cases-tags-map
contents	= cases/semantic \
	   $(ts-file)     \
	   $(map-file)    \
           COPYING.html   \
           COPYING.txt    \
           NEWS.txt       \
           LICENSE.txt

cases-dist: html plots sedml tags-map
	@echo $(today) > $(ts-file)
	make $(map-file)
	zip -r $(cases-dist-name) $(contents) -x@.zipexcludes
	@echo "---------------------------------------------------------------"
	@echo "Next: upload zip file to SourceForge as updated test cases dist."
	@echo "Please don't forget to do 'svn commit' for the time-stamp file."
	@echo "---------------------------------------------------------------"


tags-map $(map-file): $(cases-m-files)
	@echo "Making tags map file:"
	src/utilities/make-tag-map/make-tag-map.sh $(map-file)

clean-cases-dist:
	rm -f $(cases-dist-name)


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
dist-zip   = $(dist-dir).zip

standalone-dist:
	mkdir -p $(dist-dir)
	cp -r $(dist-files) $(dist-dir)
	cp $(dist-jar) $(dist-dir)
	zip -r $(dist-zip) $(dist-dir)

clean-dist:
	rm -f $(dist-zip)
	rm -rf $(dist-dir)


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

svn-ignores: $(cases-html-files) $(cases-svg-files)
	@list='$(cases-dirs)'; for dir in $$list; do \
	  name=`basename $$dir`; \
	  echo $$name-model.html > $(tmpfile); \
	  echo $$name-plot'.*' >> $(tmpfile); \
	  echo '*-sedml.xml' >> $(tmpfile); \
	  svn propset -F $(tmpfile) svn:ignore $$dir; \
	done
	@rm -f $(tmpfile)


#
# 'make readme-html'
#
# This converts README-HACKING.txt to HTML format.
# 

readme-html: README-HACKING.html

README-HACKING.html: README-HACKING.txt
	markdown README-HACKING.txt > README-HACKING.html

clean-readme:
	rm -f README-HACKING.html


# -----------------------------------------------------------------------------
# Cleaning.
# -----------------------------------------------------------------------------

clean: clean-readme clean-dist clean-cases-dist clean-html clean-plots clean-sedml


# -----------------------------------------------------------------------------
# Common special targets.
# -----------------------------------------------------------------------------

.PHONY: docs html plots sedml readme-html clean \
	clean-readme clean-dist clean-cases-dist clean-html \
	clean-plots clean-sedml

.SUFFIXES: .png .svg .jpg .csv .html .xml .txt

# ----------------------------------------------------------------------------- 
# End. 
# ----------------------------------------------------------------------------- 
