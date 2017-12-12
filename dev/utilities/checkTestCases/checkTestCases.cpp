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

#include <sbml/extension/SBMLExtensionRegister.h>
#include <sbml/conversion/SBMLConverterRegistry.h>
#include <sbml/SBMLTypes.h>

using namespace std;
LIBSBML_CPP_NAMESPACE_USE

#include "../testSuiteUtil/testSuiteUtil.cpp"

//Finds the list of tags or whatever in the .m file, separated by commas, after the ':'.
set<string> getList(string line)
{
  set<string> ret;
  //Remove everything before the first colon
  size_t colon = line.find(":");
  if (colon==string::npos) return ret;
  line.erase(0, colon+1);
  //Remove spaces
  size_t space = line.find(" ");
  while (space != string::npos) {
    line.erase(space, 1);
    space = line.find(" ");
  }
  //Split the rest into a vector
  stringstream str(line);
  string item;
  while(getline(str, item, ',')) {
    ret.insert(item);
  }
  return ret;
}

bool
parseMFile(string modfilename, set<string>& levels, set<string>& componenttags, set<string>& testtags, bool& fbc)
{
  size_t mod = modfilename.find("-model.m");
  if (mod ==string::npos) {
    cerr << "Error:  the filename '" << modfilename
         << "' doesn't have the substring '-model.m' in it." << endl;
    return true;
  }
  ifstream infile(modfilename);
  if (!infile.good()) {
    cerr << "Error:  unable to read file '" << modfilename << "." << endl;
    return true;
  }
  string line;
  bool foundcat = false;
  bool foundttype = false;
  bool foundgenby = false;
  bool isNumeric = false;
  while (!infile.eof() && infile.good()) {
    getline(infile, line);
    if (line.find("componentTags:") != string::npos) {
      componenttags = getList(line);
    }
    else if (line.find("testTags:") != string::npos) {
      testtags = getList(line);
    }
    else if (line.find("levels:") != string::npos) {
      levels = getList(line);
    }
    else if (line.find("category:") != string::npos) {
      set<string> split = getList(line);
      if (split.size() != 1 || *split.begin() != "Test") {
        cerr << "Error in " << modfilename << ": line is malformed ('" << line << "')" << endl;
        cerr << "'" << *split.begin() << "'" << endl;
      }
      foundcat = true;
    }
    else if (line.find("testType") != string::npos) {
      set<string> split = getList(line);
      if (split.size() != 1) {
        cerr << "Error in " << modfilename << ": line is malformed ('" << line << "')" << endl;
      }
      else if (*split.begin() == "TimeCourse") {
        fbc = false;
      }
      else if (*split.begin() == "FluxBalanceSteadyState") {
        fbc = true;
      }
      else {
        cerr << "Error in " << modfilename << ": unknown test type ";
        cerr << "'" << *split.begin() << "'" << endl;
      }
      foundttype = true;
    }
    else if (line.find("generatedBy:") != string::npos) {
      set<string> split = getList(line);
      if (split.size() != 1 || (*split.begin() != "Analytic" && *split.begin() != "Numeric")) {
        cerr << "Error in " << modfilename << ": line is malformed ('" << line << "')" << endl;
      }
      if (split.size() ==1 && *split.begin() == "Numeric") isNumeric = true;
      foundgenby = true;
    }
    else if (line.find("{Keep this next line") != string::npos) {
      cerr << "Error in " << modfilename << ": forgot to delete the line '" << line << "'" << endl;
      if (isNumeric) {
        cerr << "  (Also, don't forget to delete the following lines as well, as these results were generated numerically.)" << endl;
      }
    }
    else if (line.find("{Write general description") != string::npos) {
      cerr << "Error in " << modfilename << ": forgot to replace the line '" << line << "'" << endl;
    }
  }
  if (!foundcat) {
    cerr << "Error in " << modfilename << ": unable to find the 'Category:  Test' line." << endl;
  }
  if (!foundttype) {
    cerr << "Error in " << modfilename << ": unable to find the 'testType:  TimeCourse' line." << endl;
  }
  if (!foundcat) {
    cerr << "Error in " << modfilename << ": unable to find the 'generatedBy:  [Analytic|Numeric]' line." << endl;
  }
  if (levels.size()==0 || componenttags.size()==0) {
    cerr << "Error:  no levels or component tags in " << modfilename << endl;
  }
  return false;
}

void checkLevels(string filename, set<string> levels) {
  if (levels.size()==0) {
    cerr << "Error in " << filename << ": no SBML levels present." << endl;
  }
  size_t mod = filename.find("-model");
  filename.erase(mod);
  for (set<string>::iterator l = levels.begin(); l != levels.end(); l++) {
    string lv = *l;
    lv.replace(lv.find("."), 1, "v");
    string newfile = filename + "-sbml-l" + lv + ".xml";
    SBMLDocument* doc = readSBML(newfile.c_str());
    if (doc->getModel() == NULL) {
      cerr << "Error:  unable to read file '" << newfile << ".  Perhaps this level and version does not actually exist?" << endl;
    }
    if (hasActualErrors(doc))
    {
      cerr << "Error:  the test model '" << newfile << "' is invalid.  Encountered the following SBML errors:" << endl;
      printActualErrors(doc);
      return;
    }
    string testnum = filename;
    if (testnum.size() > 5) {
      testnum.erase(0, testnum.size()-5);
      string modid = doc->getModel()->getId();
      if (modid.find(testnum) == string::npos) {
        cout << "Warning:  the test model '" << newfile << "' does not have the number of the test (" << testnum << ") in it." << endl;
      }
    }
  }
  set<string> allLevels;
  allLevels.insert("1.2");
  allLevels.insert("2.1");
  allLevels.insert("2.2");
  allLevels.insert("2.3");
  allLevels.insert("2.4");
  allLevels.insert("3.1");
  for (set<string>::iterator l=allLevels.begin(); l!=allLevels.end(); l++) {
    string lv = *l;
    if (levels.find(lv) != levels.end()) continue;
    lv.replace(lv.find("."), 1, "v");
    string newfile = filename + "-sbml-l" + lv + ".xml";
    ifstream lvfile(newfile);
    if (lvfile.good()) {
      cerr << "Warning:  the file " << newfile << " exists, but is not listed as one of the tested levels." << endl;
    }
  }
}

void deduceTags(Model* model, set<string>& components, set<string>& tests,  const map<string, vector<double> >& results, bool fbc)
{
  checkRules(model, components, tests);
  checkCompartments(model, components, tests, results);
  checkEvents(model, components, tests);
  if (model->getNumFunctionDefinitions() > 0) {
    components.insert("FunctionDefinition");
  }
  if (model->getNumInitialAssignments() > 0) {
    components.insert("InitialAssignment");
  }
  checkParameters(model, components, tests, results);
  checkReactions(model, components, tests, results, fbc);
  checkSpecies(model, components, tests, results, fbc);
  if (model->isSetConversionFactor()) {
    tests.insert("ConversionFactors");
  }
  string modxml = model->toSBML();
  checkMathML(modxml, components, tests);
  checkConstraints(model, components, tests);
  checkFunctionDefinitions(model, components, tests);
  checkBooleans(model->getSBMLDocument(), components, tests);
}

void removeIdenticals(set<string>& set1, set<string>& set2)
{
  for (set<string>::iterator item=set1.begin(); item != set1.end(); item++) {
    string itemstr = *item;
    if (set2.find(itemstr) != set2.end()) {
      set1.erase(item);
      set2.erase(itemstr);
      return removeIdenticals(set1, set2);
    }
  }
}

void compareComponents(set<string>& components, set<string>& known_components, const string& filename)
{
  removeIdenticals(components, known_components);
  for (set<string>::iterator c=components.begin(); c != components.end(); c++) {
    cerr << "Error in " << filename << ":  component " << *c << " found in the model, but not listed as a testTag." << endl;
  }
  for (set<string>::iterator c=known_components.begin(); c != known_components.end(); c++) {
    cerr << "Error in " << filename << ":  component " << *c << " listed, but that component was not found in the model." << endl;
  }
}

void checkUnaddableTests(set<string>& tests, set<string>& components, const string& filename) 
{
  //Random event execution requries there to be event priorities, at least.
  if (tests.find("RandomEventExecution") != tests.end()) {
    if (components.find("EventPriority") == components.end()) {
      cerr << "Error in " << filename << ":  The testTag RandomEventExecution was present, but there are no event priority objects in the model, which are required for that test." << endl;
    }
    tests.erase("RandomEventExecution");
  }

  //UncommonMathML is not yet testable
  tests.erase("UncommonMathML");
}

bool checkLine(ifstream& infile, const string& begin, const string& settingsfile)
{
  string line;
  getline(infile, line);
  if (line.find(begin + ":") == string::npos) {
    cerr << "Error:  no '" + begin + "' line in the correct place in settings file " << settingsfile << endl;
    return true;
  }
  line.erase(0, begin.size()+2);
  if (line.size()==0) {
    cerr << "Error: no value for '" + begin + "' value in settings file " << settingsfile << endl;
    return true;
  }
  return false;
}

bool checkVariables(ifstream& infile, Model* model, const string& settingsfile, bool checkVariableReporting)
{
  string line;
  getline(infile, line);
  if (line.find("variables:") == string::npos) {
    cerr << "Error:  no 'variables' line in the correct place in settings file " << settingsfile << endl;
    return true;
  }
  line.erase(0, 11);
  if (line.size() == 0) {
    cerr << "Error: no value for 'variables' value in settings file " << settingsfile << endl;
    return true;
  }
  set<string> variables;
  size_t comma = line.find(',');
  while (comma != string::npos) {
    string var = line.substr(0, comma);
    line.erase(0, comma+1);
    if (line[0] == ' ') {
      line.erase(0, 1);
    }
    pair<set<string>::iterator, bool> ret = variables.insert(var);
    if (ret.second == false) {
      cerr << "Error:  the settings document requested output for variable '" << var << "' twice." << endl;
      return true;
    }
    comma = line.find(',');
  }
  variables.insert(line);

  for (set<string>::iterator var = variables.begin(); var != variables.end(); var++) {
    if (model->getElementBySId(*var) == NULL) {
      cerr << "Error:  the settings document " << settingsfile << " requested output for variable '" << *var << "', but no such variable could be found in the SBML document." << endl;
      return true;
    }
  }
  size_t setspot = settingsfile.find("settings.txt");
  if (setspot == string::npos) {
    cerr << "Error: The settings file doesn't have 'settings.txt' in it.";
    return true;
  }
  string results = settingsfile.substr(0, setspot) + "results.csv";
  ifstream resfile(results);
  if (!resfile.good()) {
    cerr << "Error:  could not open the results file " << results << endl;
    return true;
  }
  getline(resfile, line);
  comma = line.find(',');
  set<string> resvars;
  while (comma != string::npos) {
    string var = line.substr(0, comma);
    line.erase(0, comma + 1);
    if (line[0] == ' ') {
      line.erase(0, 1);
    }
    pair<set<string>::iterator, bool> ret = resvars.insert(var);
    if (ret.second == false) {
      cerr << "Error:  the results file lists variable '" << var << " twice.";
      return true;
    }
    comma = line.find(',');
  }
  resvars.insert(line);

  bool hasproblems = false;
  
  for (set<string>::iterator var = variables.begin(); var != variables.end(); var++) {
    if (resvars.find(*var) == resvars.end()) {
      cerr << "Error:  the settings document requested output for variable '" << *var << "', but that variable was not found in the results file " << results << "." << endl;
      hasproblems = true;
    }
  }
  for (set<string>::iterator var = resvars.begin(); var != resvars.end(); var++) {
    if (variables.find(*var) == variables.end()) {
      if (*var != "time" && *var != "Time") {
        cerr << "Error:  the results file contains output for variable '" << *var << "', but that variable was not requested in the settings file " << settingsfile << "." << endl;
        hasproblems = true;
      }
    }
  }

  if (checkVariableReporting) {
    for (unsigned long sp = 0; sp < model->getNumSpecies(); sp++) {
      Species* species = model->getSpecies(sp);
      if (species->isSetId() && variables.find(species->getId()) == variables.end()) {
        cerr << "Error:  the SBML model contains the species '" << species->getId() << "', but that species is not requested in the settings file " << settingsfile << "." << endl;
      }
    }

    for (unsigned long c = 0; c < model->getNumCompartments(); c++) {
      Compartment* compartment = model->getCompartment(c);
      if (compartment->isSetId() && variables.find(compartment->getId()) == variables.end()) {
        cerr << "Error:  the SBML model contains the compartment '" << compartment->getId() << "', but that compartment is not requested in the settings file " << settingsfile << "." << endl;
        hasproblems = true;
      }
    }

    for (unsigned long p = 0; p < model->getNumParameters(); p++) {
      Parameter* parameter = model->getParameter(p);
      if (parameter->isSetId() && variables.find(parameter->getId()) == variables.end()) {
        cerr << "Error:  the SBML model contains the parameter '" << parameter->getId() << "', but that parameter is not requested in the settings file " << settingsfile << "." << endl;
        hasproblems = true;
      }
    }
  }
  return hasproblems;
}

bool checkPossible(ifstream& infile, const string& begin, bool shouldbe, const string& settingsfile)
{
  string line;
  getline(infile, line);
  if (line.find(begin + ":") == string::npos) {
    cerr << "Error:  no '" + begin + "' line in the correct place in settings file " << settingsfile << endl;
    return true;
  }
  line.erase(0, begin.size()+2);
  if (line.size()==0 && shouldbe) {
    cerr << "Error: in " << settingsfile << ": no entries for '" + begin + "', but " << begin << " was used as a test tag in the .m file." << endl;
    return true;
  }
  if (line.size()!=0 && !shouldbe) {
    cerr << "Error: in " << settingsfile << ": entries present for '" + begin + "', but " << begin << " was not used as a test tag in the .m file." << endl;
    return true;
  }
  return false;
}

void checkSettingsFile(const string& filename, bool known_amount, bool known_conc, Model* model, bool fbc, bool checkVariableReporting)
{
  string settingsfile = filename;
  size_t mod = settingsfile.find("-model");
  settingsfile.erase(mod);
  settingsfile += "-settings.txt";
  ifstream infile(settingsfile);
  if (!infile.good()) {
    cerr << "Error:  could not open the settings file " << settingsfile << endl;
    return;
  }
  if (fbc) {
    string line;
    getline(infile, line);
    getline(infile, line);
    getline(infile, line);
  }
  else {
  if (checkLine(infile, "start", settingsfile)) return;
  if (checkLine(infile, "duration", settingsfile)) return;
  if (checkLine(infile, "steps", settingsfile)) return;
}

  if (checkVariables(infile, model, settingsfile, checkVariableReporting)) return;
  if (checkLine(infile, "absolute", settingsfile)) return;
  if (checkLine(infile, "relative", settingsfile)) return;
  if (checkPossible(infile, "amount", known_amount, settingsfile)) return;
  if (checkPossible(infile, "concentration", known_conc, settingsfile)) return;
}

void checkSimilarTests(set<string>& tests, set<string>& known_tests, const string& filename, Model* model, bool fbc) 
{
  bool checkVariableReporting = !fbc;
  if (known_tests.find("RandomEventExecution") != known_tests.end()) {
    checkVariableReporting = false;
  }
  bool known_amount = known_tests.find("Amount") != known_tests.end();
  bool known_conc = known_tests.find("Concentration") != known_tests.end();
  if (tests.find("Amount||Concentration") != tests.end()) {
    if (!known_amount && !known_conc && checkVariableReporting) {
      cerr << "Error in " << filename << ":  there should be an 'Amount' or a 'Concentration' testTag, but neither were found." << endl;
    }
    known_tests.erase("Amount");
    known_tests.erase("Concentration");
    tests.erase("Amount||Concentration");
  }
  checkSettingsFile(filename, known_amount, known_conc, model, fbc, checkVariableReporting);

  //EventIsNotPersistent
  if(tests.find("EventIsNotPersistent [?]") != tests.end() && known_tests.find("EventIsNotPersistent") == known_tests.end()) {
    cerr << "Warning in " << filename << ":  An event was found with 'persistent=false', but the tag EventIsNotPersistent was not present." << endl;
  }
  if(tests.find("EventIsNotPersistent [?]") == tests.end() && known_tests.find("EventIsNotPersistent") != known_tests.end()) {
    cerr << "Error in " << filename << ":  EventIsNotPersistent tag found, but no such event was found in the model." << endl;
  }
  tests.erase("EventIsNotPersistent [?]");
  known_tests.erase("EventIsNotPersistent");
  
  //EventIsPersistent
  if(tests.find("EventIsPersistent [?]") == tests.end() && known_tests.find("EventIsPersistent") != known_tests.end()) {
    cerr << "Error in " << filename << ":  EventIsPersistent tag found, but no such event was found in the model." << endl;
  }
  tests.erase("EventIsPersistent [?]");
  known_tests.erase("EventIsPersistent");

  //EventT0Firing
  if(tests.find("EventT0Firing [?]") != tests.end() && known_tests.find("EventT0Firing") == known_tests.end()) {
    cerr << "Warning in " << filename << ":  an event was found in the model with 'initialValue=false' but no EventT0Firing tag was found." << endl;
  }
  tests.erase("EventT0Firing [?]");
  known_tests.erase("EventT0Firing");

  //EventUsesAssignmentTimeValues
  if(tests.find("EventUsesAssignmentTimeValues [?]") != tests.end() && known_tests.find("EventUsesAssignmentTimeValues") == known_tests.end()) {
    cerr << "Warning in " << filename << ":  an event was found in the model with 'useValuesFromTriggerTime=false' but no EventUsesAssignmentTimeValues tag was found." << endl;
  }
  if(tests.find("EventUsesAssignmentTimeValues [?]") == tests.end() && known_tests.find("EventUsesAssignmentTimeValues") != known_tests.end()) {
    cerr << "Error in " << filename << ":  EventUsesAssignmentTimeValues tag found, but no event was found in the model with 'useValuesFromTriggerTime=false'." << endl;
  }
  tests.erase("EventUsesAssignmentTimeValues [?]");
  known_tests.erase("EventUsesAssignmentTimeValues");

  //EventUsesTriggerTimeValues
  if(tests.find("EventUsesTriggerTimeValues [?]") == tests.end() && known_tests.find("EventUsesTriggerTimeValues") != known_tests.end()) {
    cerr << "Error in " << filename << ":  EventUsesTriggerTimeValues tag found, but no event was found in the model with 'useValuesFromTriggerTime=true'." << endl;
  }
  tests.erase("EventUsesTriggerTimeValues [?]");
  known_tests.erase("EventUsesTriggerTimeValues");

  //ReversibleReaction
  if(tests.find("ReversibleReaction [?]") == tests.end() && known_tests.find("ReversibleReaction") != known_tests.end()) {
    cerr << "Error in " << filename << ":  ReversibleReaction tag found, but no reaction was found in the model with 'reversible=true'." << endl;
  }
  tests.erase("ReversibleReaction [?]");
  known_tests.erase("ReversibleReaction");

  //NonUnityCompartment
  if(tests.find("NonUnityCompartment") != tests.end() && known_tests.find("NonUnityCompartment") == known_tests.end()) {
    cerr << "Warning in " << filename << ":  no NonUnityCompartment tag found, but there is a compartment with an assigned value or non-1 size in the model.  If the assigned value is not always '1.0', the tag needs to be added to the test." << endl;
    tests.erase("NonUnityCompartment");
    known_tests.erase("NonUnityCompartment");
  }

}

void compareTests(set<string>& tests, set<string>& known_tests, const string& filename, set<string>& components, Model* model, bool fbc)
{
  removeIdenticals(tests, known_tests);
  checkSimilarTests(tests, known_tests, filename, model, fbc);
  checkUnaddableTests(known_tests, components, filename);

  for (set<string>::iterator c=tests.begin(); c != tests.end(); c++) {
    cerr << "Error in " << filename << ":  testTag " << *c << " should be tested by this model, but the tag was not listed." << endl;
  }
  for (set<string>::iterator c=known_tests.begin(); c != known_tests.end(); c++) {
    cerr << "Error in " << filename << ":  testTag " << *c << " listed as a test, but cannot be tested by this model." << endl;
  }
}

void checkTags(const string& filename, set<string> known_components, set<string> known_tests, bool fbc)
{
  //Actually need the model for this
  string l3v2 = filename;
  size_t mod = l3v2.find("-model");
  l3v2.erase(mod);
  string resultsfile = l3v2 + "-results.csv";
  l3v2 = l3v2 + "-sbml-l3v2.xml";
  SBMLDocument* document = readSBML(l3v2.c_str());
  Model* model = document->getModel();
  string l3v1 = l3v2;
  if (model==NULL) {
    l3v1.replace(l3v1.find("l3v2"), 4, "l3v1");
    delete document;
    document = readSBML(l3v1.c_str());
    model = document->getModel();
  }
  if (model == NULL) {
    string l2v4 = l3v2;
    l2v4.replace(l2v4.find("l3v2"), 4, "l2v4");
    delete document;
    document = readSBML(l2v4.c_str());
    model = document->getModel();
    if (model == NULL) {
      cerr << "Error:  unable to read the file " << l3v2 << ", " << l3v1 << " or " << l2v4 << ", or they contain invalid SBML." << endl;
      return;
    }
  }
  map<string, vector<double> > results = getResults(resultsfile);
  set<string> components, tests;
#ifdef USE_COMP
  CompSBMLDocumentPlugin* compdoc = static_cast<CompSBMLDocumentPlugin*>(document->getPlugin("comp"));
  SBMLDocument flat(3,1);
  if (compdoc != NULL) {
    flat = *document;
    checkComp(compdoc, components, tests, results);
    ConversionProperties* props = new ConversionProperties();
    props->addOption("flatten comp");
    SBMLConverter* converter = 
      SBMLConverterRegistry::getInstance().getConverterFor(*props);
    converter->setDocument(&flat);
    int result = converter->convert();
    model = flat.getModel();
  }
#endif
#ifdef USE_FBC
  FbcModelPlugin* fbcmodplug = static_cast<FbcModelPlugin*>(model->getPlugin("fbc"));
  if (fbcmodplug != NULL) {
    checkFbc(fbcmodplug, components, tests, results);
  }
#endif
  deduceTags(model, components, tests, results, fbc);
  //Have to check tests before components because checking the tests uses the components list.
  compareTests(tests, known_tests, filename, components, model, fbc);
  compareComponents(components, known_components, filename);
  delete document;
}

int
main (int argc, char* argv[])
{
  if (argc < 2)
  {
    cerr << endl << "Usage: checkTestCases filename.m [filename2.m] [...]" << endl << endl;
    return 1;
  }

  for (int file=1; file<argc; file++) {
    string filename   = argv[file];
    set<string> levels, componenttags, testtags;
    bool fbc = false;
    if (parseMFile(filename, levels, componenttags, testtags, fbc)) {
      cerr << "Unable to parse .m file " << filename;
      continue;
    }
    checkLevels(filename, levels);
    checkTags(filename, componenttags, testtags, fbc);
    cout << "Successfully parsed model description file " << filename << endl;
  }
  return 0;
}
