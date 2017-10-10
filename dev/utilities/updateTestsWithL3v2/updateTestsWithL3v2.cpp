/**
 * @file    updateTestsWithL3v2.cpp
 * @brief   Takes a test case model and generates the translated versions of the model plus the .m file.
 * @author  Lucian Smith
 * 
 * This file is part of libSBML.  Please visit http://sbml.org for more
 * information about SBML, and the latest version of libSBML.
 */


#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <set>
#include <sstream>

#include <sbml/SBMLTypes.h>
#include <sbml/extension/SBMLExtensionRegister.h>
#include <sbml/conversion/SBMLConverterRegistry.h>

#ifdef USE_COMP
#include <sbml/packages/comp/common/CompExtensionTypes.h>
#endif

using namespace std;
LIBSBML_CPP_NAMESPACE_USE

template <class T>
inline std::string toString (const T& t)
{
  std::stringstream ss;
  ss << t;
  return ss.str();
}

#include "../testSuiteUtil/testSuiteUtil.cpp"

bool createTranslation(const SBMLDocument* document, const string oldfilename)
{
  string filename = oldfilename;
  vector<string> ret;
  assert(document->getLevel  () == 3);
  assert(document->getVersion() == 1);

  string lxvx = "l3v1";
  size_t lxvx_place = filename.find(lxvx);
  if (lxvx_place==string::npos) {
    cerr << "Error:  the filename '" << filename
         << "' doesn't have the substring '" << lxvx << "' in it." << endl;
    return false;
  }

  //L3v2

  SBMLDocument* translatedDoc = document->clone();
  string newfilename = filename.replace(lxvx_place, 4, "l3v2");
  if (translatedDoc->setLevelAndVersion(3, 2, true) && !hasActualErrors(translatedDoc)) {
    if (writeSBMLToFile(translatedDoc, newfilename.c_str()) == 1) {
      translatedDoc->setLocationURI("file:" + newfilename);
      std::cout << "Successfully wrote translation of model to level 3 version 2" << endl;
      delete translatedDoc;
      return true;
    }
  }
  else {
    cerr << "Encountered the following SBML errors when trying to create " << newfilename << ":" << endl;
    printActualErrors(translatedDoc);

  }
  delete translatedDoc;
  return false;
}


bool updateMFileForL3v2(string modfilename)
{
  ifstream infile(modfilename);
  string newversion(""), tmp;
  while (!infile.eof() && infile.good()) {
    getline(infile, tmp);
    if (tmp.find("levels:") != string::npos) {
      if (tmp.find("3.1") != string::npos && tmp.find("3.2") == string::npos) {
        tmp += ", 3.2";
      }
      else {
        std::cout << "Model description file " << modfilename << " does not include l3v1; not updating to l3v2" << endl;
        return false;
      }
    }
    newversion += tmp + "\n";
  }
  ofstream file(modfilename);
  file << newversion;
  file.close();
  std::cout << "Successfully updated model description file " << modfilename << " to include l3v2." << endl;
  return true;
}

int
main (int argc, char* argv[])
{
  if (argc < 2)
  {
    cerr << endl << "Usage: updateTestsWithL3v2 [list of #####-model.m files]" << endl << endl;
    return 1;
  }

  bool translateonly = false;
  int type = 0;
  for (int a = 1; a < argc; a++) {
    string modfilename = argv[a];
    string sbml = modfilename;
    size_t modpos = sbml.find("-model");
    if (modpos == string::npos) {
      cerr << "Unable to find 'model' in filename " << modfilename << ".";
      continue;
    }
    sbml.replace(modpos, 8, "-sbml-l3v1.xml");
    ifstream f(sbml.c_str());
    if (!f.good()) {
      cout << "No L3v1 file for test " << modfilename << "." << endl;
      continue;
    }
    SBMLDocument* document = readSBML(sbml.c_str());
    Model* model = document->getModel();

    if (model == NULL)
    {
      cout << "No model present." << endl;
      delete document;
      continue;
    }

    if (hasActualErrors(document))
    {
      cerr << "Encountered the following SBML errors:" << endl;
      printActualErrors(document);
      delete document;
      continue;
    }

    if (!createTranslation(document, sbml)) {
      cout << "The L3v2 version of the model for test " << modfilename << " is not valid." << endl;
      delete document;
      continue;
    }
    delete document;
    if (!updateMFileForL3v2(modfilename)) {
      continue;
    }
  }
  return 0;
}
