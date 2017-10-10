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
#include <iostream>
#include <experimental/filesystem> // C++-standard header file name  
#include <filesystem> // Microsoft-specific implementation header file name  
#include <sbml/extension/SBMLExtensionRegister.h>
#include <sbml/conversion/SBMLConverterRegistry.h>
#include <sbml/SBMLTypes.h>

using namespace std;
using namespace std::experimental::filesystem::v1;
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

set<string> getTagsWithValues()
{
  set<string> ret;
  ret.insert("Compartment");
  ret.insert("Parameter");
  ret.insert("Reaction");
  ret.insert("Species");
  return ret;
}

set<string> getTagsAssigningValues()
{
  set<string> ret;
  ret.insert("AlgebraicRule");
  ret.insert("AssignmentRule");
  ret.insert("EventNoDelay");
  ret.insert("EventWithDelay");
  ret.insert("InitialAssignment");
  ret.insert("RateRule");
  ret.insert("StoichiometryMath");
  return ret;
}

set<string> getTagsAffectingMath()
{
  set<string> ret;
  ret.insert("ConversionFactors");
  ret.insert("CSymbolAvogadro");
  ret.insert("CSymbolDelay");
  ret.insert("CSymbolRateOf");
  ret.insert("CSymbolTime");
  ret.insert("FunctionDefinition");
  ret.insert("HasOnlySubstanceUnits");
  ret.insert("LocalParameters");
  ret.insert("NoMathML");
  ret.insert("SpeciesReferenceInMath");
  ret.insert("UncommonMathML");
  ret.insert("VolumeConcentrationRates");
  return ret;
}

set<string> getTagsAffectingReactions()
{
  set<string> ret;
  ret.insert("BoundaryCondition");
  ret.insert("AssignedConstantStoichiometry");
  ret.insert("AssignedVariableStoichiometry");
  ret.insert("FastReaction");
  ret.insert("LocalParameters");
  return ret;
}

set<string> getTagsAffectingEvents()
{
  set<string> ret;
  ret.insert("EventIsNotPersistent");
  ret.insert("EventIsPersistent");
  ret.insert("EventPriority");
  ret.insert("EventT0Firing");
  ret.insert("EventUsesAssignmentTimeValues");
  ret.insert("EventUsesTriggerTimeValues");
  ret.insert("RandomEventExecution");
  return ret;
}

set<string> getHierarchyTags()
{
  set<string> ret;
  ret.insert("comp:ConversionFactor");
  ret.insert("comp:Deletion");
  ret.insert("comp:ExtentConversionFactor");
  ret.insert("comp:ExternalModelDefinition");
  ret.insert("comp:ModelDefinition");
  ret.insert("comp:Port");
  ret.insert("comp:ReplacedBy");
  ret.insert("comp:ReplacedElement");
  ret.insert("comp:SBaseRef");
  ret.insert("comp:Submodel");
  ret.insert("comp:SubmodelOutput");
  ret.insert("comp:TimeConversionFactor");
  return ret;
}

set<string> getFBCTags()
{
  set<string> ret;
  ret.insert("fbc:BoundEqual");
  ret.insert("fbc:BoundGreaterEqual");
  ret.insert("fbc:BoundLessEqual");
  ret.insert("fbc:MaximizeObjective");
  ret.insert("fbc:MinimizeObjective");
  return ret;
}

set<string> getNuisanceTags()
{
  set<string> ret;
  ret.insert("Amount");
  ret.insert("Concentration");
  ret.insert("InitialValueReassigned");
  ret.insert("NonConstantCompartment");
  ret.insert("NonConstantParameter");
  ret.insert("NonUnityCompartment");
  ret.insert("NonUnityStoichiometry");
  ret.insert("ReversibleReaction");
  ret.insert("0D-Compartment");
  ret.insert("ConstantSpecies");
  ret.insert("MultiCompartment");
  return ret;
}

void checkForInternalPairs(const string& msg, const set<string>& taglist, const map<string, vector<int> >& tagpairmap)
{
  set<string> pairlist;
  cout << msg << endl;
  for (set<string>::iterator tag1 = taglist.begin(); tag1 != taglist.end(); tag1++) {
    for (set<string>::iterator tag2 = tag1; tag2 != taglist.end(); tag2++) {
      if (tag1 == tag2) {
        continue;
      }
      string pair = *tag1 + "\t" + *tag2;
      map<string, vector<int> >::const_iterator result;
      result = tagpairmap.find(pair);
      if (result == tagpairmap.end()) {
        cout << pair << "\t" << "0" << endl;
      }
      else if ((*result).second.size() == 0) {
        cout << pair << "\t" << (*result).second.size() << endl;
      }
    }
  }
}

void checkForPairs(const string& msg, const set<string>& taglist1, const set<string>& taglist2, const map<string, vector<int> >& tagpairmap)
{
  set<string> pairlist;
  cout << msg << endl;
  for (set<string>::iterator tag1 = taglist1.begin(); tag1 != taglist1.end(); tag1++) {
    for (set<string>::iterator tag2 = taglist2.begin(); tag2 != taglist2.end(); tag2++) {
      string pair = *tag1 + "\t" + *tag2;
      map<string, vector<int> >::const_iterator result;
      result = tagpairmap.find(pair);
      if (result == tagpairmap.end()) {
        cout << pair << "\t" << "0" << endl;
      }
      else if ((*result).second.size() == 0) {
        cout << pair << "\t" << (*result).second.size() << endl;
      }
    }
  }
}

int
    main(int argc, char* argv[])
{
  map<string, vector<int> > tagmap, tagpairmap;
  if (argc < 2) {
    vector<string> filelist;
    for (auto& p : recursive_directory_iterator("C:/Users/Lucian/Desktop/test-suite/cases/semantic")) {
      stringstream fname;
      fname << p;
      if (fname.str().find("-model.m") != string::npos 
          && fname.str().find("01000") == string::npos
          && fname.str().find("01121") == string::npos
          && fname.str().find("01122") == string::npos
          && fname.str().find("01123") == string::npos
        ) {
        filelist.push_back(fname.str());
      }
    }

    for (size_t file = 0; file < filelist.size(); file++) {
      string filename = filelist[file];
      if (parseMFile(filename, tagmap, tagpairmap)) {
        cerr << "Unable to parse .m file " << filename << endl;
        continue;
      }
      //cout << "Successfully parsed model description file " << filename << endl;
    }
  }
  else {
    for (int file = 1; file < argc; file++) {
      string filename = argv[file];
      if (parseMFile(filename, tagmap, tagpairmap)) {
        cerr << "Unable to parse .m file " << filename << endl;
        continue;
      }
      //cout << "Successfully parsed model description file " << filename << endl;
    }
  }
  //for (map<string, vector<int> >::iterator tagfound = tagmap.begin(); tagfound != tagmap.end(); tagfound++) {
  //  cout << (*tagfound).first;
  //  cout << "\t*\t";
  //  cout << ((*tagfound).second).size();
  //  cout << endl;
  //}
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
  set<string> valueTags = getTagsWithValues();
  set<string> assigningTags = getTagsAssigningValues();
  set<string> mathTags = getTagsAffectingMath();
  set<string> rxnTags = getTagsAffectingReactions();
  set<string> eventTags = getTagsAffectingEvents();
  set<string> hierarchyTags = getHierarchyTags();
  set<string> fbcTags = getFBCTags();
  set<string> nuisanceTags = getNuisanceTags();

  checkForInternalPairs("These tests should check what happens when both tags assign to the same variable (if possible.)", assigningTags, tagpairmap);
  checkForInternalPairs("These tests should check what happens when both tags affect the same reaction, and when both tags affect reactions that affect the same species.", rxnTags, tagpairmap);
  checkForInternalPairs("These tests should check the use of multiple element that affect MathML in the same expression.", mathTags, tagpairmap);
  checkForInternalPairs("These tests should check what happens when both tags affect the same event, and/or when they are from different events that affect each other.", eventTags, tagpairmap);
  checkForInternalPairs("These tests ensure that the various bits of 'comp' are tested against each other.", hierarchyTags, tagpairmap);
  checkForInternalPairs("These tests ensure that the various bits of 'fbc' are tested against each other.", fbcTags, tagpairmap);
  checkForPairs("These tests ensure that everything that assigns to something is tested assigning to everything.", valueTags, assigningTags, tagpairmap);
  checkForPairs("These tests ensure that everything that assigns to something checks all the various things that can affect the math used.", mathTags, assigningTags, tagpairmap);
  checkForPairs("These tests ensure that events are tested in concert with elements that affect the MathML.", mathTags, eventTags, tagpairmap);
  checkForPairs("These tests ensure that reactions are tested in concert with elements that affect the MathML.", mathTags, rxnTags, tagpairmap);
  checkForPairs("These tests should check cases where a reaction and an assignment affect the same thing (if possible).", assigningTags, rxnTags, tagpairmap);
  checkForPairs("These tests should check cases where an event and an assignment affect the same thing (if possible).", assigningTags, eventTags, tagpairmap);
  checkForPairs("These tests should ensure that all the various elements are included in FBC tests.", fbcTags, valueTags, tagpairmap);
  checkForPairs("These tests should ensure that all the ways to affect reactions are included in FBC tests.", fbcTags, rxnTags, tagpairmap);


  //for (map<string, vector<int> >::iterator tagfound = tagpairmap.begin(); tagfound != tagpairmap.end(); tagfound++) {
  //  cout << (*tagfound).first;
  //  cout << "\t";
  //  cout << ((*tagfound).second).size();
  //  cout << endl;
  //}
  return 0;
}
