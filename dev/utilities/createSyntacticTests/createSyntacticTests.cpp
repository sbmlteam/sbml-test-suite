/**
 * \file   createSyntacticTests.cpp
 * \brief  Create syntactic tests from the libsbml source validation files.
 * \author Lucian Smith
 * 
 * <!--------------------------------------------------------------------------
 * This file is part of libSBML.  Please visit http://sbml.org for more
 * information about SBML, and the latest version of libSBML.
 *
 * Copyright (C) 2013-2014 jointly by the following organizations:
 *     1. California Institute of Technology, Pasadena, CA, USA
 *     2. EMBL European Bioinformatics Institute (EMBL-EBI), Hinxton, UK
 *     3. University of Heidelberg, Heidelberg, Germany
 *
 * Copyright (C) 2009-2013 jointly by the following organizations: 
 *     1. California Institute of Technology, Pasadena, CA, USA
 *     2. EMBL European Bioinformatics Institute (EMBL-EBI), Hinxton, UK
 *  
 * Copyright (C) 2006-2008 by the California Institute of Technology,
 *     Pasadena, CA, USA 
 *  
 * Copyright (C) 2002-2005 jointly by the following organizations: 
 *     1. California Institute of Technology, Pasadena, CA, USA
 *     2. Japan Science and Technology Agency, Japan
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation.  A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available online as http://sbml.org/software/libsbml/license.html
 * ---------------------------------------------------------------------- -->*/

#include <iostream>
#include <fstream>
#include <set>

#include <algorithm>

#include "TestFile.h"
#include "TestValidator.h"
#include <direct.h>

#include <sbml/SBMLTypes.h>

#ifdef LIBSBML_USE_VLD
  #include <vld.h>
#endif

/** @cond doxygenIgnored */

using namespace std;
LIBSBML_CPP_NAMESPACE_USE

/** @endcond */

void copyFile(const string& infilename, const string& outfilename)
{
  ifstream infile(infilename.c_str());
  ofstream outfile(outfilename.c_str());
  if (!infile.good()) {
    cout << "Unable to open file " << infilename << " for reading." << endl;
    return;
  }
  if (!outfile.good()) {
    cout << "Unable to open file " << outfilename << " for writing.  Check that the directory exists, and if it does not, create it first." << endl;
    return;
  }
  string str;
  while(getline(infile,str)){
    outfile << str << endl;
  }
  infile.close();
  outfile.close();
}

void createConstraintsFile(const string& sbmlfilename, const SBMLErrorLog* errlog, vector<SBMLError*>& uniqueErrors, set<unsigned int>& uniqueErrorIDs)
{
  string constraintfilename = sbmlfilename;
  size_t xml = constraintfilename.find(".xml");
  constraintfilename.replace(xml, 4, ".txt");
  ofstream cfile(constraintfilename.c_str());
  if (!cfile.good()) {
    cout << "Unable to open file " << constraintfilename << " for writing.  Check that the directory exists, and if it does not, create it first." << endl;
    return;
  }
  for (unsigned int err=0; err<errlog->getNumErrors(); err++) {
    const SBMLError* error = errlog->getError(err);
    cfile << "------------------" << endl;
    cfile << "Validation id    :\t" << TestFile::getConstraintIdString(error->getErrorId()) << endl;
    cfile << "Validation number:\t" << error->getErrorId() << endl;
    cfile << "Severity         :\t" << error->getSeverityAsString() << endl;
    cfile << "Line number      :\t" << error->getLine() << endl;
    //cfile << "Column number    :\t" << error->getColumn() << endl;
    cfile << "Package          :\t" << error->getPackage() << endl;
    cfile << "Short message    :\t" << error->getShortMessage() << endl;
    cfile << "Full message     :\t" << error->getMessage() << endl;

    //Now store the unique error IDs
    if (uniqueErrorIDs.insert(error->getErrorId()).second) {
      uniqueErrors.push_back(error->clone());
    }
  }
  if (errlog->getNumErrors()>0) {
    cfile << "------------------" << endl;
  }
}

/**
 * Parse the files in the given set of directories, copy the files out (renamed) if they only contain the errors they claim they do, and report what we're doing.
 */
bool
parseDirectories ( const vector<string>& directories, const string& outdir, const string& package, ofstream& report, vector<SBMLError*>& uniqueErrors, set<unsigned int>& uniqueErrorIDs, bool fullreport)
{
  for (size_t d=0; d<directories.size(); d++) {
    set<TestFile> files    = TestFile::getFilesIn(directories[d]);
    if (files.size() > 0) {
      cout << files.size() << " files found in directory " << directories[d] << ":" << endl;
      for (set<TestFile>::iterator file=files.begin(); file != files.end(); file++) {
        stringstream reportline;
        if (file->getConstraintId() >= 90500 && file->getConstraintId() < 90600) {
          //The 905xx constraints are duplicates, and don't need to go into a test suite.
          continue;
        }
        if (fullreport) {
          reportline << file->getDirectory() 
            << "," << file->getFilename() << ",";
        }
        reportline
            << file->getConstraintId()
            << "," << file->getNumFailures()
            << "," << file->getSequenceId()
            << "," << file->getAdditionalFailId();
        string fullname = file->getFullname();
        string outfilename = file->getNewFilename();
        if (fullname.find("pass")) {
          reportline << "," << "pass";
        }
        else {
          reportline << "," << "fail";
        }
        reportline << "," << package;
        SBMLDocument* document = readSBMLFromFile(fullname.c_str());
        document->checkConsistency();
        const SBMLErrorLog* errlog = document->getErrorLog();
        bool copy = true;
        if (errlog->getNumFailsWithSeverity(LIBSBML_SEV_ERROR) > file->getNumFailures() + (file->getAdditionalFailId() > 0)) {
          cout << "Not copying document " << file->getFilename() << ", since it has more errors than the expected " << file->getNumFailures() << "." <<  endl;
          if (file->getNumFailures() == 0) {
            file = file;
          }
          copy = false;
        }
        stringstream sevlv;
        if (errlog->getNumFailsWithSeverity(LIBSBML_SEV_FATAL) > 0) {
          sevlv << "-sev" << LIBSBML_SEV_FATAL;
          reportline << ",Fatal";
        }
        else if (errlog->getNumFailsWithSeverity(LIBSBML_SEV_ERROR) > 0) {
          sevlv << "-sev" << LIBSBML_SEV_ERROR;
          reportline << ",Error";
        }
        else if (errlog->getNumFailsWithSeverity(LIBSBML_SEV_WARNING) > 0) {
          sevlv << "-sev" << LIBSBML_SEV_WARNING;
          reportline << ",Warning";
        }
        else if (errlog->getNumFailsWithSeverity(LIBSBML_SEV_INFO) > 0) {
          sevlv << "-sev" << LIBSBML_SEV_INFO;
          reportline << ",Info";
        }
        else if (fullname.find("pass") == string::npos) {
          cout << "Not copying document " << file->getFilename() << ", since it has no errors at all, but should." << endl;
          reportline << ",None" ;
          copy = false;
        }
        else {
          reportline << ",None" ;
        }
        if (fullname.find("fail") != string::npos && !errlog->contains(file->getConstraintId())) {
          cout << "Not copying document " << file->getFilename() << ", since the error it is supposed to have did not appear." << endl;
          copy = false;
        }
        sevlv << "-l" << document->getLevel() << "v" << document->getVersion();
        outfilename.insert(outfilename.size()-4, sevlv.str());
        if (copy) {
          if (fullreport) {
            reportline << ",copy";
          }
          mkdir((outdir + "/" + file->getConstraintIdString()).c_str());
          string fulloutfilename = outdir + "/" + file->getConstraintIdString() + "/" + outfilename;
          copyFile(fullname, fulloutfilename);
          createConstraintsFile(fulloutfilename, errlog, uniqueErrors, uniqueErrorIDs);
          if (file->getConstraintId()==1020310) {
            //These are models that refer to external model definitions, and need to be copied verbatim.
            fulloutfilename = outdir + "/" + file->getConstraintIdString() + "/" + file->getFilename();
            copyFile(fullname, fulloutfilename);
          }
        }
        else if (fullreport) {
          reportline << ",no_copy";
        }
        for (unsigned int e=0; e<errlog->getNumErrors(); e++)
        {
          reportline << "," << errlog->getError(e)->getErrorId();
        }
        reportline << endl;
        if (fullreport || copy) {
          report << outfilename << "," << reportline.str();
        }
        delete document;
      }
    }
    else {
      cout << "No test files found in " << directories[d] << endl;
    }
  }
  return false;
}

int
main (int argc, char* argv[])
{
#ifndef LIBSBML_HAS_PACKAGE_COMP
  cout << "Please compile libsbml with comp enabled to run this program." << endl;
  return 1;
#endif
#ifndef LIBSBML_HAS_PACKAGE_FBC
  cout << "Please compile libsbml with fbc enabled to run this program." << endl;
  return 1;
#endif
#ifndef  LIBSBML_HAS_PACKAGE_LAYOUT
  cout << "Please compile libsbml with layout enabled to run this program." << endl;
  return 1;
#endif
#ifndef  LIBSBML_HAS_PACKAGE_QUAL
  cout << "Please compile libsbml with qual enabled to run this program." << endl;
  return 1;
#endif
#ifndef  LIBSBML_HAS_PACKAGE_GROUPS
  cout << "Please compile libsbml with groups enabled to run this program." << endl;
  return 1;
#endif
#ifndef  LIBSBML_HAS_PACKAGE_MULTI
  cout << "Please compile libsbml with multi enabled to run this program." << endl;
  return 1;
#endif

  string prefix(".");

  char *srcdir = getenv("srcdir");
  if (srcdir != NULL) 
  {
    prefix = srcdir;
  }
  if (argc >= 2)
  {
    prefix = argv[1];
  }

  string outdir(".");

  free(srcdir);
  srcdir = getenv("outdir");
  if (srcdir != NULL) 
  {
    outdir = srcdir;
  }
  if (argc >= 3)
  {
    outdir = argv[2];
  }

  bool fullreport = false;
  if (argc >= 4)
  {
    if ((string)argv[3] == "-full") fullreport = true;
  }

  prefix += "/src/sbml";
  outdir += "/syntactic";
  vector<string> validationDirectories;
  vector<string> compValidationDirectories;
  vector<string> fbcValidationDirectories;
  vector<string> layoutValidationDirectories;
  vector<string> qualValidationDirectories;
  vector<string> groupsValidationDirectories;
  vector<string> multiValidationDirectories;
  validationDirectories.push_back(prefix + "/validator/test/test-data/libsbml-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-annotation-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-general-consistency-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-identifier-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-mathml-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-modeldefinition-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-modeling-practice-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-notes-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-sbo-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-unit-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/sbml-xml-constraints/");
  validationDirectories.push_back(prefix + "/validator/test/test-data/xml-parser-constraints/");
  compValidationDirectories.push_back(prefix + "/packages/comp/validator/test/test-data/general-constraints/");
  compValidationDirectories.push_back(prefix + "/packages/comp/validator/test/test-data/identifier-constraints/");
  compValidationDirectories.push_back(prefix + "/packages/comp/validator/test/test-data/units-constraints/");
  fbcValidationDirectories.push_back(prefix + "/packages/fbc/validator/test/test-data/general-constraints/");
  fbcValidationDirectories.push_back(prefix + "/packages/fbc/validator/test/test-data/identifier-constraints/");
  layoutValidationDirectories.push_back(prefix + "/packages/layout/validator/test/test-data/general-constraints/");
  layoutValidationDirectories.push_back(prefix + "/packages/layout/validator/test/test-data/identifier-constraints/");
  qualValidationDirectories.push_back(prefix + "/packages/qual/validator/test/test-data/general-constraints/");
  qualValidationDirectories.push_back(prefix + "/packages/qual/validator/test/test-data/identifier-constraints/");
  qualValidationDirectories.push_back(prefix + "/packages/qual/validator/test/test-data/math-constraints/");
  groupsValidationDirectories.push_back(prefix + "/packages/groups/validator/test/test-data/general-constraints/");
  groupsValidationDirectories.push_back(prefix + "/packages/groups/validator/test/test-data/identifier-constraints/");
  multiValidationDirectories.push_back(prefix + "/packages/multi/validator/test/test-data/general-constraints/");
  multiValidationDirectories.push_back(prefix + "/packages/multi/validator/test/test-data/identifier-constraints/");
  multiValidationDirectories.push_back(prefix + "/packages/multi/validator/test/test-data/mathml-constraints/");

  cout << endl;
  cout << "Syntactic Test Suite Creation" << endl;
  cout << "=============================" << endl;
  cout << endl;

  string reportfile = outdir + "/summary.csv";
  if (fullreport) {
    reportfile = outdir + "/summary-full.csv";
  }
  ofstream report(reportfile.c_str());
  report << "Filename,";
  if (fullreport) {
    report << "Original directory,";
    report << "Original filename,";
  }
  report << "Validation number,";
  report << "Num Failures,";
  report << "ID,";
  report << "Other error,";
  report << "Pass/fail,";
  report << "Package,";
  report << "Error/Warning,";
  if (fullreport) {
    report << "Copy/no copy,";
  }
  report << "Validation error/warning list" << endl;
  vector<SBMLError*> uniqueErrors;
  set<unsigned int> uniqueErrorIDs;
  if (parseDirectories(validationDirectories, outdir, "", report, uniqueErrors, uniqueErrorIDs, fullreport)) return 1;
  if (parseDirectories(compValidationDirectories, outdir, "comp", report, uniqueErrors, uniqueErrorIDs, fullreport)) return 1;
  if (parseDirectories(fbcValidationDirectories, outdir, "fbc", report, uniqueErrors, uniqueErrorIDs, fullreport)) return 1;
  if (parseDirectories(layoutValidationDirectories, outdir, "layout", report, uniqueErrors, uniqueErrorIDs, fullreport)) return 1;
  if (parseDirectories(qualValidationDirectories, outdir, "qual", report, uniqueErrors, uniqueErrorIDs, fullreport)) return 1;
  if (parseDirectories(groupsValidationDirectories, outdir, "groups", report, uniqueErrors, uniqueErrorIDs, fullreport)) return 1;
  if (parseDirectories(multiValidationDirectories, outdir, "multi", report, uniqueErrors, uniqueErrorIDs, fullreport)) return 1;
  if (fullreport) {
    //Output the unique error messages, so we can read them:
    ofstream cfile((outdir + "/uniqueErrors.txt").c_str());
    if (!cfile.good()) {
      cout << "Unable to open file " << outdir << "/uniqueErrors.txt for writing.  Check that the directory exists, and if it does not, create it first." << endl;
      return false;
    }
    for (size_t err=0; err<uniqueErrors.size(); err++) {
      SBMLError* error = uniqueErrors[err];
      cfile << "------------------" << endl;
      cfile << "Validation id    :\t" << TestFile::getConstraintIdString(error->getErrorId()) << endl;
      cfile << "Validation number:\t" << error->getErrorId() << endl;
      cfile << "Severity         :\t" << error->getSeverityAsString() << endl;
      cfile << "Line number      :\t" << error->getLine() << endl;
      //cfile << "Column number    :\t" << error->getColumn() << endl;
      cfile << "Package          :\t" << error->getPackage() << endl;
      cfile << "Short message    :\t" << error->getShortMessage() << endl;
      cfile << "Full message     :\t" << error->getMessage() << endl;
      delete error;
    }
    cfile << "------------------" << endl;
  }
  return 0;
}

