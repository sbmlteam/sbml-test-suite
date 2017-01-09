/**
* @file    checkTestCases.cpp
* @brief   Checks the test suite .m file to make sure the tags and things are correct.
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
#include <stdlib.h>

#include <sbml/extension/SBMLExtensionRegister.h>
#include <sbml/conversion/SBMLConverterRegistry.h>
#include <sbml/SBMLTypes.h>

using namespace std;
LIBSBML_CPP_NAMESPACE_USE

#include "../testSuiteUtil/testSuiteUtil.cpp"

//Finds the list of tags or whatever in the .m file, separated by commas, after the ':'.
void getList(string line, set<string>& tags)
{
  set<string> ret;
  //Remove everything before the first colon
  size_t colon = line.find(":");
  if (colon == string::npos) {
    return;
  }
  line.erase(0, colon + 1);
  //Remove spaces
  size_t space = line.find(" ");
  while (space != string::npos) {
    line.erase(space, 1);
    space = line.find(" ");
  }
  //Split the rest into a vector
  stringstream str(line);
  string item;
  while (getline(str, item, ',')) {
    tags.insert(item);
  }
}

bool
parseMFile(string modfilename, map<string, vector<int> >& tagmap, map<string, vector<int> >& tagpairmap)
{
  size_t mod = modfilename.find("-model.m");
  if (mod == string::npos) {
    cerr << "Error:  the filename '" << modfilename
      << "' doesn't have the substring '-model.m' in it." << endl;
    return true;
  }
  ifstream infile(modfilename);
  if (!infile.good()) {
    cerr << "Error:  unable to read file '" << modfilename << "." << endl;
    return true;
  }
  if (mod <= 5) {
    cerr << "Error:  the filename '" << modfilename
      << "' is not long enough to contain a test ID in it (5 characters)." << endl;
    return true;
  }
  int testNum = atoi(modfilename.substr(mod - 5, mod).c_str());
  string line;
  set<string> tags;
  while (!infile.eof() && infile.good()) {
    getline(infile, line);
    if (line.find("componentTags:") != string::npos) {
      getList(line, tags);
    }
    else if (line.find("testTags:") != string::npos) {
      getList(line, tags);
    }
  }
  if (tags.size() == 0) {
    cerr << "Error:  no component or test tags in " << modfilename << endl;
    return true;
  }
  for (set<string>::iterator tag = tags.begin(); tag != tags.end(); tag++) {
    tagmap[*tag].push_back(testNum);
    for (set<string>::iterator tagpair = tags.begin(); tagpair != tags.end(); tagpair++) {
      if (*tag == *tagpair) {
        continue;
      }
      string doubled = *tag + "\t" + *tagpair;
      tagpairmap[doubled].push_back(testNum);
    }
  }
  return false;
}

  int
    main(int argc, char* argv[])
  {
    if (argc < 2)
    {
      cerr << endl << "Usage: checkTestCases filename.m [filename2.m] [...]" << endl << endl;
      return 1;
    }

    map<string, vector<int> > tagmap, tagpairmap;
    for (int file = 1; file<argc; file++) {
      string filename = argv[file];
      set<string> levels, componenttags, testtags;
      if (parseMFile(filename, tagmap, tagpairmap)) {
        cerr << "Unable to parse .m file " << filename << endl;
        continue;
      }
      //cout << "Successfully parsed model description file " << filename << endl;
    }
    for (map<string, vector<int> >::iterator tagfound = tagmap.begin(); tagfound != tagmap.end(); tagfound++) {
      cout << (*tagfound).first;
      cout << "\t*\t";
      cout << ((*tagfound).second).size();
      cout << endl;
    }
    for (map<string, vector<int> >::iterator tag1 = tagmap.begin(); tag1 != tagmap.end(); tag1++) {
      for (map<string, vector<int> >::iterator tag2 = tagmap.begin(); tag2 != tagmap.end(); tag2++) {
        if (*tag1 == *tag2) {
          continue;
        }
        string doubled = (*tag1).first + "\t" + (*tag2).first;
        if (tagpairmap.find(doubled) == tagpairmap.end()) {
          vector<int> emptyvec;
          tagpairmap[doubled] = emptyvec;
        }
      }
    }
    for (map<string, vector<int> >::iterator tagfound = tagpairmap.begin(); tagfound != tagpairmap.end(); tagfound++) {
      cout << (*tagfound).first;
      cout << "\t";
      cout << ((*tagfound).second).size();
      cout << endl;
    }
    return 0;
  }
