## @file    Makefile
## @brief   Makefile for deploying web interface
## @author  Michael Hucka
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

## Usge notes:
##
## "make" or "make classes": recompile .class files that are out of date.
## This looks at the sources in WEB-INF/src/sbml/test/ and puts the
## compiled class files in WEB-INF/classes/sbml/test/
##
## "make install": (WARNING: HAS NOT BEEN USED IN A LONG TIME) install the
## web interface files into the Tomcat 5 webapps directory.  The destination
## is set by the variable $webapp-dir somewhere below.
##
## This can be used to recompile the files on the server without having to
## make a .war file and deploy that.  Run "make" or "make classes" in the
## Test Suite webapp subdirectory, and then reload the application from the
## Tomcat control interface (http://...SOMETHING...:8080/manager/html).

default: classes

# -----------------------------------------------------------------------------
# Compile Java class files
# -----------------------------------------------------------------------------

src-dir     = WEB-INF/src/sbml/test
class-dir   = WEB-INF/classes/sbml/test
web-dir     = web
javac-flags = -g -Xlint:unchecked -d WEB-INF/classes -sourcepath WEB-INF/src

classes: $(class-dir)/CaseSummary.class           \
         $(class-dir)/CaseSummaryMap.class        \
         $(class-dir)/FormBean.class              \
         $(class-dir)/OnlineSTS.class             \
         $(class-dir)/OnlineSTSLogFormatter.class \
         $(class-dir)/TagBits.class               \
         $(class-dir)/TestCase.class              \
         $(class-dir)/UploadUnzipTest.class       \
         $(class-dir)/UserTestCase.class          \
         $(class-dir)/UserTestResult.class        \
         $(class-dir)/ZipServlet.class

jsp-jar      = /var/lib/tomcat5/common/lib/[jsp].jar
servlet-jar  = /var/lib/tomcat5/common/lib/[servlet].jar
fload-jar    = WEB-INF/lib/commons-fileupload-1.2.jar
io-jar       = WEB-INF/lib/commons-io-1.4.jar
local-copies = $(wildcard WEB-INF/tomcat-classes/*.jar)

jars         = $(jsp-jar):$(servlet-jar):$(fload-jar):$(io-jar):$(local-copies)

$(class-dir)/%.class: $(src-dir)/%.java
	javac $(javac-flags) -cp "$(jars):WEB-INF/src" $<

__dummy := $(shell [ -d $(class-dir) ] || mkdir -p $(class-dir)) 


# -----------------------------------------------------------------------------
# Install updates into the webapps directory
# -----------------------------------------------------------------------------

webapp-dir = /var/lib/tomcat5/webapps/test-suite
iflags = -pv --backup=numbered --owner=tomcat --group=tomcat

install: 
	install $(iflags) web $(webapp-dir)/web
	install $(iflags) META-INF $(webapp-dir)/
	install $(iflags) WEB-INF $(webapp-dir)/
	install $(iflags) index.html $(webapp-dir)/


# -----------------------------------------------------------------------------
# TAGS
# -----------------------------------------------------------------------------

ETAGS       ?= etags
ETAGS_FLAGS ?= --ignore-indentation --members

tags etags: etags-version-check TAGS 

etags-version-check:  
	@if test -z "`$(ETAGS) --version |2>&1 grep 'Free Software'`"; then \
	  echo "Your 'etags' command is not the GNU [X]Emacs version,"; \
	  echo "and I'm afraid I don't know how to work with it. Quitting."; \
	  exit 2; \
	fi 

files-to-tag = $(wildcard $(src-dir)/*.java) $(wildcard $(web-dir)/*.jsp)

TAGS: $(files-to-tag)
	$(ETAGS) $(ETAGSFLAGS) -l java $(files-to-tag)


# -----------------------------------------------------------------------------
# Common special targets
# -----------------------------------------------------------------------------

.PHONY: classes install

# ----------------------------------------------------------------------------- 
# End. 
# ----------------------------------------------------------------------------- 
 
## The following is for [X]Emacs users.  Please leave in place. 
## Local Variables: 
## mode: Makefile 
## End: 
 
