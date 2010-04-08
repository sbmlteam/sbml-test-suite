## @file    Makefile
## @brief   Makefile for deploying web interface
## @author  Michael Hucka
## 
## $Id$
## $HeadURL$
##
## ----------------------------------------------------------------------------
## This file is part of the SBML Test Suite.  Please visit http://sbml.org for
## more information about SBML, and the latest version of the SBML Test Suite.
## 
## Copyright 2008-2010 California Institute of Technology.
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

default: classes

# -----------------------------------------------------------------------------
# Compile Java class files
# -----------------------------------------------------------------------------

src-dir     = WEB-INF/src/sbml/test
class-dir   = WEB-INF/classes/sbml/test
web-dir     = web
javac-flags = -g -Xlint:unchecked -d WEB-INF/classes -sourcepath WEB-INF/src

classes: $(class-dir)/CasesTagsMap.class    \
         $(class-dir)/FormBean.class        \
         $(class-dir)/OnlineSTS.class       \
         $(class-dir)/TestCase.class        \
         $(class-dir)/UploadUnzipTest.class \
         $(class-dir)/UserTestCase.class    \
         $(class-dir)/UserTestResult.class  \
         $(class-dir)/ZipServlet.class

jsp-jar        = /var/lib/tomcat5/common/lib/[jsp].jar
servlet-jar    = /var/lib/tomcat5/common/lib/[servlet].jar
fileupload-jar = WEB-INF/lib/commons-fileupload-1.2.jar
io-jar         = WEB-INF/lib/commons-io-1.4.jar

jars           = $(jsp-jar):$(servlet-jar):$(fileupload-jar):$(io-jar)

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
 
