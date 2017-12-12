## @file    Makefile
## @brief   Top-level Makefile for the SBML Test Suite
## @author  Michael Hucka
##
## ----------------------------------------------------------------------------
## This file is part of the SBML Test Suite.  Please visit http://sbml.org for
## more information about SBML, and the latest version of the SBML Test Suite.
##
## Copyright (C) 2010-2017 jointly by the following organizations: 
##     1. California Institute of Technology, Pasadena, CA, USA
##     2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK.
##     3. University of Heidelberg, Heidelberg, Germany
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
  ./dev/utilities/desc2html/desc2html.sh $(addsuffix .m,$(basename $(1))) $(1)
endef

semantic-cases-m-files    = $(wildcard cases/semantic/*/*-model.m)
semantic-cases-html-files = $(addsuffix .html,$(basename $(semantic-cases-m-files)))

html: $(semantic-cases-html-files)

cases/semantic/%-model.html: cases/semantic/%-model.m
	$(call make_html,$@)

clean-html:
	rm -f $(semantic-cases-html-files)

#
# 'make plots'
#

plotter = ./dev/utilities/plotresults/plotresults.py

# Semantic test suite

semantic-cases-csv-files       = $(wildcard cases/semantic/*/*-results.csv)
semantic-cases-html-plot-files = $(patsubst %-results.csv,%-plot.html,$(semantic-cases-csv-files))
semantic-cases-png-plot-files  = $(patsubst %-results.csv,%-plot.png,$(semantic-cases-csv-files))
semantic-cases-jpg-plot-files  = $(patsubst %-results.csv,%-plot.jpg,$(semantic-cases-csv-files))

semantic-cases-plot-files = \
	$(semantic-cases-html-plot-files) \
	$(semantic-cases-png-plot-files) \
	$(semantic-cases-jpg-plot-files)

cases/semantic/%-plot.html: cases/semantic/%-results.csv
	if test -n "`grep 'packagesPresent: *fbc' $(patsubst %-plot.html,%-model.m,$@)`"; then \
	  flag="-t steadystate"; \
	fi; \
	$(plotter) -q $$flag -d $(patsubst %-plot.html,%-results.csv,$@) -o $@

cases/semantic/%-plot.png: cases/semantic/%-plot.html
	if test -n "`grep 'packagesPresent: *fbc' $(patsubst %-plot.png,%-model.m,$@)`"; then \
	  flag="-t steadystate"; \
	fi; \
	$(plotter) -n -q $$flag -d $(patsubst %-plot.html,%-results.csv,$<) -o /tmp/$(notdir $<)
	phantomjs ./dev/utilities/rasterize/rasterize.js /tmp/$(notdir $<) $@

cases/semantic/%-plot.jpg: cases/semantic/%-plot.html cases/semantic/%-plot.png
	convert -quality 90 $(patsubst %-plot.jpg,%-plot.png,$@) $@

# Stochastic test suite

stochastic-cases-csv-files       = $(wildcard cases/stochastic/*/*-results.csv)
stochastic-cases-html-plot-files = $(patsubst %-results.csv,%-plot.html,$(stochastic-cases-csv-files))
stochastic-cases-png-plot-files  = $(patsubst %-results.csv,%-plot.png,$(stochastic-cases-csv-files))
stochastic-cases-jpg-plot-files  = $(patsubst %-results.csv,%-plot.jpg,$(stochastic-cases-csv-files))

stochastic-cases-plot-files = \
	$(stochastic-cases-html-plot-files) \
	$(stochastic-cases-png-plot-files) \
	$(stochastic-cases-jpg-plot-files)

cases/stochastic/%-plot.html: cases/stochastic/%-results.csv
	$(plotter) -q -2 -g mean -d $(patsubst %-plot.html,%-results.csv,$@) -o $@ ;\

cases/stochastic/%-plot.png: cases/stochastic/%-plot.html
	$(plotter) -n -2 -g mean -q -d $(patsubst %-plot.html,%-results.csv,$<) -o /tmp/$(notdir $<) ;\
	phantomjs ./dev/utilities/rasterize/rasterize.js /tmp/$(notdir $<) $@

cases/stochastic/%-plot.jpg: cases/stochastic/%-plot.html cases/stochastic/%-plot.png
	convert -quality 90 $(patsubst %-plot.jpg,%-plot.png,$@) $@

plots: html-plots jpg-plots png-plots

html-plots: $(semantic-cases-html-plot-files) $(stochastic-cases-html-plot-files)

png-plots: $(semantic-cases-png-plot-files) $(stochastic-cases-png-plot-files)

jpg-plots: $(semantic-cases-jpg-plot-files) $(stochastic-cases-jpg-plot-files)

clean-plots:
	rm -f $(semantic-cases-plot-files)
	rm -f $(stochastic-cases-plot-files)

#
# 'make sedml'
#

ifeq "$(shell uname)" "Darwin"
  define make_sedml_files
    @echo "Creating SED-ML for $(1)"
    env DYLD_LIBRARY_PATH="$(abspath dev/utilities/sedml):$(DYLD_LIBRARY_PATH)" \
	mono ./dev/utilities/sedml/GenerateSedML.exe -c $(dir $(1)) -a
  endef
else
  define make_sedml_files
    @echo "Creating SED-ML for $(1)"
    env LD_LIBRARY_PATH="$(abspath dev/utilities/sedml):$(DYLD_LIBRARY_PATH)" \
	mono ./dev/utilities/sedml/GenerateSedML.exe -c $(dir $(1)) -a
  endef
endif

semantic-cases-sbml-files = $(wildcard cases/semantic/*/*-sbml-l[0-9]v[0-9].xml)

all-sedml-files	:= $(patsubst %-l1v2.xml,%-l1v2-sedml.xml,$(semantic-cases-sbml-files))
all-sedml-files	:= $(patsubst %-l2v1.xml,%-l2v1-sedml.xml,$(all-sedml-files))
all-sedml-files	:= $(patsubst %-l2v2.xml,%-l2v2-sedml.xml,$(all-sedml-files))
all-sedml-files	:= $(patsubst %-l2v3.xml,%-l2v3-sedml.xml,$(all-sedml-files))
all-sedml-files	:= $(patsubst %-l2v4.xml,%-l2v4-sedml.xml,$(all-sedml-files))
all-sedml-files	:= $(patsubst %-l2v5.xml,%-l2v5-sedml.xml,$(all-sedml-files))
all-sedml-files	:= $(patsubst %-l3v1.xml,%-l3v1-sedml.xml,$(all-sedml-files))

%-sbml-l1v2-sedml.xml: %-sbml-l1v2.xml dev/utilities/sedml/GenerateSedML.exe
	$(call make_sedml_files,$@)

%-sbml-l2v1-sedml.xml: %-sbml-l2v1.xml dev/utilities/sedml/GenerateSedML.exe
	$(call make_sedml_files,$@)

%-sbml-l2v2-sedml.xml: %-sbml-l2v2.xml dev/utilities/sedml/GenerateSedML.exe
	$(call make_sedml_files,$@)

%-sbml-l2v3-sedml.xml: %-sbml-l2v3.xml dev/utilities/sedml/GenerateSedML.exe
	$(call make_sedml_files,$@)

%-sbml-l2v4-sedml.xml: %-sbml-l2v4.xml dev/utilities/sedml/GenerateSedML.exe
	$(call make_sedml_files,$@)

%-sbml-l2v5-sedml.xml: %-sbml-l2v5.xml dev/utilities/sedml/GenerateSedML.exe
	$(call make_sedml_files,$@)

%-sbml-l3v1-sedml.xml: %-sbml-l3v1.xml dev/utilities/sedml/GenerateSedML.exe
	$(call make_sedml_files,$@)

sedml: $(all-sedml-files)

# Note #1: a simple rm -f $(all-sedml-files) doesn't work -- arg list is too long.
#
# Note #2: make sure the command line under the clean-sedml directive is
# preceeded by a tab, not spaces, or you will experience first-hand insane
# make behavior #1,346, wherein make runs this next command every time you
# run "make sedml".  Why does it do that?  Who knows?  It's an insane make
# behavior!  (Incidentally, insane make behavior #1,346 is one of a long list
# of insane make behaviors, the most noteworthy of which is insane make
# behavior #1: spaces versus tabs make such a difference in makefiles.)
clean-sedml:
	$(foreach f,$(wildcard cases/semantic/*/*-sedml.xml),$(shell rm -f $f))


#
# 'make omex'
#

ifeq "$(shell uname)" "Darwin"
  define make_omex_files
    @echo "Creating COMBINE Archive for $(dir $(1))"
    env DYLD_LIBRARY_PATH="$(abspath dev/utilities/sedml):$(DYLD_LIBRARY_PATH)" \
    mono ./dev/utilities/sedml/GenerateSedML.exe -c $(dir $(1)) -a -o
  endef
else
  define make_omex_files
    @echo "Creating COMBINE Archive for $(dir $(1))"
    env LD_LIBRARY_PATH="$(abspath dev/utilities/sedml):$(DYLD_LIBRARY_PATH)" \
    mono ./dev/utilities/sedml/GenerateSedML.exe -c $(dir $(1)) -a -o
  endef
endif

all-omex-files := $(patsubst %-results.csv,%.omex,$(semantic-cases-csv-files))

cases/semantic/%.omex: cases/semantic/%-results.csv dev/utilities/sedml/GenerateSedML.exe
	$(call make_omex_files,$@)

omex: html plots $(all-omex-files)

# Note: a simple rm -f $(all-omex-files) doesn't work -- arg list is too long.
clean-omex:
	$(foreach f,$(wildcard cases/semantic/*/*.omex),$(shell rm -f $f))


# -----------------------------------------------------------------------------
# Case archive, for the test runner
# -----------------------------------------------------------------------------
# Use 'make cases-dist' for generating the zip archives of test cases.  The
# file ".zipexcludes" in this directory contains a list of files to be
# excluded from the zip archive created.
#
# Note: the date is purposefully set to one day in the future, because of the
# following problem.  The RSS feed published by sf.net for new files uses the
# UTC time zone for the <pubdate> field, not *your* time zone.  So what can
# happen (and has happened) is that if you create an archive at some point
# during a given day, then upload it, if that moment happens to be already
# the next day in the UTC time zone, the file will get a <pubdate> value of
# the following day.  This will contradict the .cases-archive-date value, and
# confuse the SBML Test Suite Test Runner: it will repeatedly think that a
# new archive is available, because the archive it downloads is for date X
# but the RSS feed will claim an archive of date X+1 is available.

today   = $(shell date -v +1d +"%F")
ts-file = .cases-archive-date

semantic-cases-dist-name   = sbml-semantic-test-cases-$(today).zip
semantic-map-file	   = cases/semantic/.cases-tags-map
semantic-contents	   = cases/semantic	  \
		 	     cases/LICENSE.txt	  \
		 	     cases/NEWS.md        \
		 	     cases/README.md 	  \
			     $(ts-file)		  \
#			     $(semantic-map-file)


stochastic-cases-dist-name = sbml-stochastic-test-cases-$(today).zip
stochastic-contents	   = cases/stochastic  \
		 	     cases/LICENSE.txt \
		 	     cases/NEWS.md     \
		 	     cases/README.md   \
			     $(ts-file)

syntactic-cases-dist-name  = sbml-syntactic-test-cases-$(today).zip
syntactic-contents	   = cases/syntactic/*.txt         \
			     cases/syntactic/[0-9]*        \
			     cases/syntactic/[a-z]*-[0-9]* \
		 	     cases/LICENSE.txt		   \
		 	     cases/NEWS.md		   \
		 	     cases/README.md 		   \
			     $(ts-file)

all-cases-dist-name       = sbml-all-test-cases-$(today).zip

semantic-cases-dist: html plots sedml omex # tags-map
	@echo $(today) > $(ts-file)
	@echo $(today) > cases/semantic/$(ts-file)
#	make $(semantic-map-file)
	zip -r $(semantic-cases-dist-name) $(semantic-contents) -x@.zipexcludes

stochastic-cases-dist: $(stochastic-cases-plot-files)
	@echo $(today) > $(ts-file)
	@echo $(today) > cases/stochastic/$(ts-file)
	zip -r $(stochastic-cases-dist-name) $(stochastic-contents) -x@.zipexcludes

syntactic-cases-dist:
	@echo $(today) > $(ts-file)
	@echo $(today) > cases/syntactic/$(ts-file)
	zip -r $(syntactic-cases-dist-name) $(syntactic-contents) -x@.zipexcludes
	zip -d $(syntactic-cases-dist-name) cases/syntactic/uniqueErrors.txt

all-cases-dist: semantic-cases-dist stochastic-cases-dist syntactic-cases-dist
	zip -r $(all-cases-dist-name) $(semantic-contents) \
	   $(stochastic-contents) $(syntactic-contents) -x@.zipexcludes

cases-dist: all-cases-dist
	@echo "---------------------------------------------------------------"
	@echo "Next: release the zip file."
	@echo "Please don't forget to do 'git commit' the time-stamp file."
	@echo "---------------------------------------------------------------"

tags-map $(semantic-map-file): $(semantic-cases-m-files)
	@echo "Making tags map file:"
	dev/utilities/make-tag-map/make-tag-map.sh $(semantic-map-file)

clean-cases-dist: clean-html clean-plots clean-sedml
	rm -f $(semantic-cases-dist-name)
	rm -f $(stochastic-cases-dist-name)
	rm -f $(syntactic-cases-dist-name)


# -----------------------------------------------------------------------------
# Standalone Application distribution
# -----------------------------------------------------------------------------

dist-dir   = SBMLTestSuite-$(shell cat VERSION.txt)
dist-files = README.md  \
	     AUTHORS.txt \
	     COPYING.html \
	     COPYING.txt \
	     LICENSE.txt \
	     NEWS-OLD.txt \
	     NEWS.md \
	     VERSION.txt \
	     docs/formatted/standalone-user-manual
dist-jar   = src/test-runner/dist/SBMLTestSuite.jar
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

cases-dirs = $(wildcard cases/semantic/*) $(wildcard cases/stochastic/*)
tmpfile    = .tmp.make.ignores

svn-ignores: \
  $(semantic-cases-html-plot-files) $(semantic-cases-jpg-plot-files) \
  $(stochastic-cases-html-plot-files) $(stochastic-cases-jpg-plot-files)
	@list='$(cases-dirs)'; for dir in $$list; do \
	  if [ -d $$dir ]; then \
	    name=`basename $$dir`; \
	    echo $$name-model.html > $(tmpfile); \
	    echo $$name-plot'.*' >> $(tmpfile); \
	    echo '*-sedml.xml' >> $(tmpfile); \
	    echo '*.omex' >> $(tmpfile); \
	    svn propset -F $(tmpfile) svn:ignore $$dir; \
	  fi \
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

clean: clean-readme clean-dist clean-cases-dist clean-html clean-plots \
	clean-sedml clean-omex


# -----------------------------------------------------------------------------
# Common special targets.
# -----------------------------------------------------------------------------

.PHONY: docs html plots sedml readme-html clean \
	clean-readme clean-dist clean-cases-dist clean-html \
	clean-plots clean-sedml

.SUFFIXES: .png .svg .jpg .csv .html .xml .txt .m

# ----------------------------------------------------------------------------- 
# End. 
# ----------------------------------------------------------------------------- 
