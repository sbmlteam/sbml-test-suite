#!/bin/bash
DIRECTORY=$(cd `dirname $0`  && pwd)
INSTALLER=/home/fbergmann/installbuilder-8.5.0/bin/builder
OUTPUTDIR=$DIRECTORY/linux

$INSTALLER build sbmltestsuite.xml --setvars project.outputDirectory=$OUTPUTDIR

$INSTALLER build sbmltestsuite.xml deb --setvars project.outputDirectory=$OUTPUTDIR
$INSTALLER build sbmltestsuite.xml rpm --setvars project.outputDirectory=$OUTPUTDIR

