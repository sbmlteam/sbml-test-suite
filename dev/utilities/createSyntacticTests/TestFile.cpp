/**
 * \file   TestFile.cpp
 * \brief  Enscapsulates an XML file in the test-data/ directory
 * \author Ben Bornstein
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

#include <cstdlib>

#if defined(WIN32) && !defined(CYGWIN)
#  include "tps/dirent.h"
#  include "tps/dirent.c"
#else
#  include <sys/types.h>
#  include <dirent.h>
#endif  /* WIN32 */

#include <iostream>
#include <sstream>
#include <iomanip>
#include <sbml/common/libsbml-config-common.h>
#include "TestFile.h"

/** @cond doxygenIgnored */

using namespace std;

/** @endcond */


/**
 * TestFiles (e.g. in the test-data/ directory) have the following naming
 * convention:
 *
 *   ccccc-pass-00-nn.xml, or
 *   ccccc-fail-ff-nn.xml
 *
 *   ccccc-fail-ff-nn-xxxxx.xml
 *
 * Where:
 *
 *   ccccc  is the five digit constraint id the file is designed to test
 *
 *   pass   indicates the file must pass validation without error
 *
 *   fail   indicates the file must fail validation with extactly ff errors
 *          all with constraint id ccccc.
 *
 *   nn     is the sequence id (to allow multiple test files per constraint).
 *
 *   xxxxx  is the number of an additional constraint that this test fails
 *
 *
 * Offsets within mFilename:
 *
 *           1        1
 * 012345678901234567890123456
 * ccccc-pass-00-nn.xml
 * ccccc-fail-ff-nn.xml
 * ccccc-fail-ff-nn-xxxxx.xml
 */


TestFile::TestFile (const std::string& directory, const std::string& filename) 
  : mDirectory(directory)
  , mFilename(filename) 
  , mNewFilename(filename)
{ 
  size_t dash1 = mFilename.find('-');
  size_t dash2 = mFilename.find('-', dash1+1);
  size_t dash3 = mFilename.find('-', dash2+1);
  size_t dash4 = mFilename.find('-', dash3+1);
  size_t dash5 = mFilename.find('.');
  mConstraintId = atol( mFilename.substr(0, dash1).c_str() );
  mSequenceId   = atol( mFilename.substr(dash3+1, 2).c_str() );
  mAdditionalFailId = 0;
  if (dash4 != string::npos)
  {
    mAdditionalFailId = atol( mFilename.substr(dash4+1, dash5).c_str() );
  }
  mNumFailures = atol( mFilename.substr(dash2+1, 2).c_str() );
  if (mAdditionalFailId > 0)
  {
    mNumFailures++;
  }
  if (mAdditionalFailId > 99999) {
    string afistring = getConstraintIdString(mAdditionalFailId);
    mNewFilename.replace(dash4+1, 7, afistring);
  }
  if (mConstraintId > 99999) {
    string cidstring = getConstraintIdString(mConstraintId);
    mNewFilename.replace(0,7,cidstring);
  }
}

std::string
TestFile::getFullname () const
{
  return mDirectory + "/" + mFilename;
}


unsigned int
TestFile::getConstraintId () const
{
  return mConstraintId;
}

string
TestFile::getConstraintIdString() const
{
  return getConstraintIdString(mConstraintId);
}

string
TestFile::getConstraintIdString(int idnum)
{
  stringstream idstream;
  idstream << setw(5) << setfill('0') << idnum;
  string id = idstream.str();
  if (idnum <= 99999) {
    //no-op
  }
  else if (idnum <= 1099999) {
    id.replace(0,2,"comp-");
  }
  else if (idnum <= 2099999) {
    id.replace(0,2,"fbc-");
  }
  else if (idnum <= 3099999) {
    id.replace(0,2,"qual-");
  }
  else if (idnum <= 4099999) {
    id.replace(0,2,"groups-");
  }
  else if (idnum <= 5099999) {
    //id.replace(0,2,"comp-");
  }
  else if (idnum <= 6099999) {
    id.replace(0,2,"layout-");
  }
  else if (idnum <= 7099999) {
    id.replace(0, 2, "multi-");
  }
  return id;
}

unsigned int
TestFile::getSequenceId () const
{
  return mSequenceId;
}


unsigned int
TestFile::getNumFailures () const
{
  return mNumFailures;
}

unsigned int
TestFile::getAdditionalFailId () const
{
  return mAdditionalFailId;
}


/**
 * @return true if filename adheres to the TestFile naming convention,
 * false otherwise.
 */
bool
TestFile::isValid (const string& filename)
{
  if (filename.find(".xml") != filename.size()-4) return false;
  return (filename[0] >= '0' && filename[0] <= '9');
}


/**
 * @return the set of TestFiles in the given directory.
 *
 * You may optionally limit to the TestFiles returned to only those with
 * ConstraintIds in the range [begin, end] (if begin == end == 0, all
 * TestFiles in the given directory will be returned).
 */
set<TestFile>
TestFile::getFilesIn ( const string& directory)
{
  DIR*           dir;
  struct dirent* entry;
  set<TestFile>  result;

  dir = opendir( directory.c_str() );

  if (dir == NULL)
  {
    cerr << "Could not obtain a list of files in directory "
         << "[" << directory << "]." << endl;
    return result;
  }

  for (entry = readdir(dir); entry != NULL; entry = readdir(dir))
  {
    string filename(entry->d_name);

    if ( TestFile::isValid(filename) )
    {
      TestFile     file(directory, filename);
      result.insert( TestFile(directory, filename) );
    }
  }

  closedir(dir);

  return result;
}
