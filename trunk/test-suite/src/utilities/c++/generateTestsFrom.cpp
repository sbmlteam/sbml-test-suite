/**
 * @file    generateTestsFrom.cpp
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

#include "testSuiteUtil.cpp"

vector<string> createTranslations(SBMLDocument* document, const string oldfilename)
{
  string filename = oldfilename;
  vector<string> ret;
  unsigned int level   = document->getLevel  ();
  unsigned int version = document->getVersion();

  string lxvx = "l" + toString(level) + "v" + toString(version);
  size_t lxvx_place = filename.find(lxvx);
  if (lxvx_place==string::npos) {
    cerr << "Error:  the filename '" << filename
         << "' doesn't have the substring '" << lxvx << "' in it." << endl;
    return ret;
  }

  //L1v2
  SBMLDocument* translatedDoc = document->clone();
  if (level==1 && version==2) {
    ret.push_back("1.2");
  }
  else {
    if (translatedDoc->setLevelAndVersion(1, 2) && !hasActualErrors(translatedDoc)) {
      string newfilename = filename.replace(lxvx_place,4,"l1v2");
      if (newfilename == oldfilename) {
        ret.push_back("1.2");
      }
      else if (writeSBMLToFile(translatedDoc, newfilename.c_str())==1) {
        translatedDoc->setLocationURI("file:" + newfilename);
        cout << "Successfully wrote translation of model to level 1 version 2" << endl;
        ret.push_back("1.2");
      }  
    }
    delete translatedDoc;
  }
  //L2v*
  for (int v=1; v<5; v++) {
    if (level==2 && version==v) {
      ret.push_back("2." + toString(v));
    }
    else {
      translatedDoc = document->clone();
      if (translatedDoc->setLevelAndVersion(2, v) && !hasActualErrors(translatedDoc)) {
        string thislv = "l2v" + toString(v);
        string newfilename = filename.replace(lxvx_place,4,thislv);
        if (newfilename == oldfilename) { 
          ret.push_back("2." + toString(v));
        }
        else if (writeSBMLToFile(translatedDoc, newfilename.c_str())==1) {
          translatedDoc->setLocationURI("file:" + newfilename);
          cout << "Successfully wrote translation of model to level 2 version " << v << endl;
          ret.push_back("2." + toString(v));
        }
      }
      delete translatedDoc;
    }
  }

  //L3v1
  if (level==2 && version==1) {
    ret.push_back("3.1");
  }
  else {
    translatedDoc = document->clone();
    translatedDoc->setLevelAndVersion(3, 1);
    if (!hasActualErrors(translatedDoc)) {
      string newfilename = filename.replace(lxvx_place,4,"l3v1");
      if (newfilename == oldfilename) {
        ret.push_back("3.1");
      }
      else if (writeSBMLToFile(translatedDoc, newfilename.c_str())==1) {
        translatedDoc->setLocationURI("file:" + newfilename);
        cout << "Successfully wrote translation of model to level 3 version 1" << endl;
        ret.push_back("3.1");
      }  
    }
    delete translatedDoc;
  }
  return ret;
}

string getString(vector<string> input)
{
  string ret = "";
  for (size_t i=0; i<input.size(); i++) {
    if (i>0) ret += ", ";
    ret += input[i];
  }
  return ret;
}

string getString(set<string> input)
{
  string ret = "";
  for (set<string>::iterator i=input.begin(); i != input.end(); i++) {
    if (i != input.begin()) ret += ", ";
    ret += *i;
  }
  return ret;
}

string getString(bool input)
{
  if (input) return "true";
  return "false";
}

vector<string> getIdListFrom(ListOf* list)
{
  vector<string> ret;
  for (unsigned int l=0; l<list->size(); l++) {
    ret.push_back(list->get(l)->getId());
  }
  return ret;
}

string getHalfReaction(ListOfSpeciesReferences* srs, vector<string>& stoichrefs, Model* model,  const map<string, vector<double> >& results)
{
  stringstream ret;
  set<string> notests;
  for (unsigned int s = 0; s<srs->size(); s++) {
    if (s > 0) ret << "+ ";
    SpeciesReference* sr = static_cast<SpeciesReference*>(srs->get(s));
    string species = sr->getSpecies();
    if (sr->isSetStoichiometryMath()) {
      if (sr->isSetId()) {
        ret << sr->getId() << " ";
        stoichrefs.push_back(sr->getId());
      }
      else {
        ret << species << "_ext ";
        stoichrefs.push_back(species + "_ext");
      }
    }
    else if (sr->isSetId() && (initialOverriddenIn(sr->getId(), model, results, notests) || variesIn(sr->getId(), model, results))) {
      ret << sr->getId() << " ";
      stoichrefs.push_back(sr->getId());
    }
    else if (sr->isSetStoichiometry() && sr->getStoichiometry() != 1.0) {
      ret << sr->getStoichiometry();
    }
    ret << species << " ";
  }
  return ret.str();
}

string getReactionTable(Model* model,  const map<string, vector<double> >& results)
{
#ifdef USE_FBC
  FbcModelPlugin* fbc = static_cast<FbcModelPlugin*>(model->getPlugin("fbc"));
#endif
  stringstream ret;
  bool anyfast = false;
  for (unsigned int r=0; r<model->getNumReactions(); r++) {
    if (model->getReaction(r)->getFast()) anyfast = true;
  }
  vector<string> stoichrefs;
  unsigned int numrxns = model->getNumReactions();
  if (numrxns > 0) {
    ret << endl << "There ";
    if (numrxns>1) {
      ret << "are " << numrxns << " reactions";
    }
    else {
      ret << "is one reaction";
    }

#ifdef USE_FBC
    FbcModelPlugin* fmp = static_cast<FbcModelPlugin*>(model->getPlugin("fbc"));
    if (fmp != NULL && fmp->getNumFluxBounds()>0) {
      ret << ", and " << fmp->getNumFluxBounds() << " flux bound";
      if (fmp->getNumFluxBounds() > 1) {
        ret << "s";
      }
    }
#endif

   ret << ":";
    ret << endl << endl;
    ret << "[{width:30em,margin: 1em auto}|  *Reaction*  |  *Rate*  |";
    if (anyfast) {
      ret << "  *Fast*  |";
    }
    for (unsigned int r=0; r<model->getNumReactions(); r++) {
      Reaction* rxn = model->getReaction(r);
      ret  << endl<< "| ";
      if (rxn->isSetId()) {
        ret << rxn->getId() << ": ";
      }
      ret << getHalfReaction(rxn->getListOfReactants(), stoichrefs, model, results);
      ret << "-> ";
      ret << getHalfReaction(rxn->getListOfProducts(), stoichrefs, model, results);
      ret << "| ";
#ifdef USE_FBC
      if (fbc != NULL) {
        bool nobounds = true;
        for (unsigned int b=0; b<fbc->getNumFluxBounds(); b++) {
          FluxBound* fb = fbc->getFluxBound(b);
          if (fb->getReaction() == rxn->getId()) {
            if (nobounds) {
              nobounds = false;
              ret << "$";
            }
            else {
              ret << " && ";
            }
            ret << rxn->getId() << " ";
            if (fb->getOperation() == "greaterEqual") {
              ret << ">=";
            }
            else if (fb->getOperation() == "lessEqual") {
              ret << "<=";
            }
            else if (fb->getOperation() == "equal") {
              ret << "==";
            }
            else {
              assert(false);
              ret << "??";
            }
            ret << " " ;
            double fbval = fb->getValue();
            if (fbval == numeric_limits<double>::infinity()) {
              ret << "INF";
            }
            else if (fbval == -numeric_limits<double>::infinity()) {
              ret << "-INF";
            }
            else {
              ret << fbval;
            }
          }
        }
        if (nobounds) {
          ret << "$(not set)$";
        }
        else {
          ret << "$";
        }
      }
      else 
#endif
      if (rxn->isSetKineticLaw()) {
        ret << "$" << SBML_formulaToString(rxn->getKineticLaw()->getMath()) << "$";
      }
      else {
        ret << "$(not set)$";
      }
      ret << " |";
      if (anyfast) {
        ret << " ";
        if (rxn->getFast()) {
          ret << "fast";
        }
        else {
          ret << "slow";
        }
        ret << " |";
      }
    }
    ret << "]" << endl;
    string srlist = getString(stoichrefs);
    if (!srlist.empty()) {
      ret << "Note:  the following stoichiometries are set separately:  " << srlist << endl;
    }
    ret << endl;
  }
  return ret.str();
}

void setEventFlags(Model* model, bool& priority, bool& persistent, bool& t0, bool& assignmenttime, bool& delay)
{
  for (unsigned int e=0; e<model->getNumEvents(); e++) {
    Event* event = model->getEvent(e);
    if (event->isSetPriority()) {
      priority = true;
    }
    if (event->isSetDelay()) {
      delay = true;
    }
    if (event->isSetUseValuesFromTriggerTime() && event->getUseValuesFromTriggerTime()==false) {
      assignmenttime = true;
    }
    Trigger* trigger = event->getTrigger();
    if (trigger->isSetInitialValue() && trigger->getInitialValue()==false) {
      t0 = true;
    }
    if (trigger->isSetPersistent() && trigger->getPersistent()==false) {
      persistent = true;
    }
  }
}

string getEventTable(Model* model)
{
  stringstream ret;
  unsigned int numevents = model->getNumEvents();
  if ( numevents > 0) {
    bool priority(false), persistent(false), t0(false), assignmenttime(false), delay(false);
    setEventFlags(model, priority, persistent, t0, assignmenttime, delay);
    ret << endl << "There ";
    if (numevents>1) {
      ret << "are " << numevents << " events:";
    }
    else {
      ret << "is one event:";
    }
    ret << endl << endl;
    string topline = "|  *Event*  |  *Trigger*  |";
    int numextras = 0;
    if (priority) {
      topline += "  *Priority*  |";
      numextras++;
    }
    if (persistent) {
      topline += "  *Persistent*  |";
      numextras++;
    }
    if (t0) {
      topline += "  *initialValue*  |";
      numextras++;
    }
    if (assignmenttime) {
      topline += "  *Use values from:*  |";
      numextras++;
    }
    if (delay) {
      topline += "  *Delay*  |";
      numextras++;
    }
    topline += " *Event Assignments* |";
    ret << "[{width:" << 30 + numextras*5 << "em,margin: 1em auto}" << topline;
    for (unsigned int e = 0; e<model->getNumEvents(); e++) {
      ret << endl << "| ";
      Event* event = model->getEvent(e);
      if (event->isSetId()) ret << event->getId();
      ret << " | ";
      ret << "$" << SBML_formulaToString(event->getTrigger()->getMath()) << "$ | ";
      if (priority) {
        if (event->isSetPriority()) ret << "$" << SBML_formulaToString(event->getPriority()->getMath()) << "$";
        else ret << "(unset)";
        ret << " | ";
      }
      if (persistent) {
        ret << getString(event->getTrigger()->getPersistent()) << " | ";
      }
      if (t0) {
        ret << getString(event->getTrigger()->getInitialValue()) << " | ";
      }
      if (assignmenttime) {
        if (event->getUseValuesFromTriggerTime()) {
          ret << "Trigger time | ";
        }
        else {
          ret << "Assignment time | ";
        }
      }
      if (delay) {
        if (event->isSetDelay()) ret << "$" << SBML_formulaToString(event->getDelay()->getMath()) << "$ | ";
        else ret << "$0$ | ";
      }
      if (event->getNumEventAssignments()==0) {
        ret << " |";
      }
      else {
        for (unsigned long e=0; e<event->getNumEventAssignments(); e++) {
          if (e > 0) {
            ret << endl << "|  |  | ";
            for (int pipes = 0; pipes<numextras; pipes++) {
              ret << " | ";
            }
          }
          EventAssignment* ea = event->getEventAssignment(e); 
          ret << "$" << ea->getVariable() << " = " << SBML_formulaToString(ea->getMath()) << "$ |";
        }
      }
    }
    ret << "]" << endl << endl;
  }
  return ret.str();
}

string getRuleTable(Model* model)
{
  stringstream ret;
  unsigned int numrules = model->getNumRules();
  if (numrules > 0) {
    ret << endl << "There ";
    if (numrules>1) {
      ret << "are " << numrules << " rules:";
    }
    else {
      ret << "is one rule:";
    }
    ret << endl << endl;
    ret << "[{width:30em,margin: 1em auto}|  *Type*  |  *Variable*  |  *Formula*  |";
    for (unsigned int r=0; r<model->getNumRules(); r++) {
      Rule* rule = model->getRule(r);
      ret << endl << "| ";
      string var = rule->getVariable();
      switch(rule->getTypeCode()) {
      case SBML_ALGEBRAIC_RULE:
        ret << "Algebraic";
        var = "$0$";
        break;
      case SBML_RATE_RULE:
        ret << "Rate";
        break;
      case SBML_ASSIGNMENT_RULE:
        ret << "Assignment";
        break;
      }
      ret << " | " << var << " | $" << SBML_formulaToString(rule->getMath()) << "$ |";
    }
    ret << "]" << endl;
    ret << endl;
  }
  return ret.str();
}

string getInitialSpeciesLevels(Model* model, bool isconst)
{
  stringstream ret;
  for (unsigned long s=0; s<model->getNumSpecies(); s++) {
    Species* species = model->getSpecies(s);
    if (species->getConstant() != isconst) continue;
    string id = species->getId();
    Rule* rule = model->getRule(id);
    InitialAssignment* ia = model->getInitialAssignment(id);
    ret << endl << "| ";
    if (rule != NULL || ia != NULL) {
      if (species->getHasOnlySubstanceUnits()) {
        ret << "Initial amount of species ";
      }
      else {
        ret << "Initial concentration of species ";
      }
      ret << id << " | $"; 
      if (rule != NULL) {
        ret << SBML_formulaToString(rule->getMath()) << "$ |";
      }
      else {
        ret << SBML_formulaToString(ia->getMath()) << "$ |";
      }
    }
    else if (species->isSetInitialAmount()) {
      ret << "Initial amount of species " << id << " | $" << species->getInitialAmount() << "$ |";
    }
    else if (species->isSetInitialConcentration()) {
      ret << "Initial concentration of species " << id << " | $" << species->getInitialConcentration() << "$ |";
    }
    else {
      ret << "Initial level of species " << id << " | $unknown$ |";
    }
    if (isconst) {
      ret << " constant |";
    }
    else {
      ret << " variable |";
    }
  }
  return ret.str();
}

string getInitialParameterLevels(Model* model, bool isconst)
{
  stringstream ret;
  for (unsigned long p=0; p<model->getNumParameters(); p++) {
    Parameter* param = model->getParameter(p);
    if (param->getConstant() != isconst) continue;
    string id = param->getId();
    ret << endl << "| Initial value of parameter " << id << " | $"; 
    Rule* rule = model->getRule(id);
    InitialAssignment* ia = model->getInitialAssignment(id);
    if (rule != NULL || ia != NULL) {
      if (rule != NULL) {
        ret << SBML_formulaToString(rule->getMath()) << "$ |";
      }
      else {
        ret << SBML_formulaToString(ia->getMath()) << "$ |";
      }
    }
    else if (param->isSetValue()) {
      ret << param->getValue() << "$ |";
    }
    else {
      ret << "unknown$ |";
    }
    if (isconst) {
      ret << " constant |";
    }
    else {
      ret << " variable |";
    }
  }
  return ret.str();
}

string getInitialCompartmentLevels(Model* model, bool isconst)
{
  stringstream ret;
  for (unsigned long c=0; c<model->getNumCompartments(); c++) {
    Compartment* compartment = model->getCompartment(c);
    if (compartment->getConstant() != isconst) continue;
    string id = compartment->getId();
    ret << endl << "| Initial volume of compartment '" << id << "' | $"; 
    Rule* rule = model->getRule(id);
    InitialAssignment* ia = model->getInitialAssignment(id);
    if (rule != NULL || ia != NULL) {
      if (rule != NULL) {
        ret << SBML_formulaToString(rule->getMath()) << "$ |";
      }
      else {
        ret << SBML_formulaToString(ia->getMath()) << "$ |";
      }
    }
    else if (compartment->isSetVolume()) {
      ret << compartment->getVolume() << "$ |";
    }
    else {
      ret << "unknown$ |";
    }
    if (isconst) {
      ret << " constant |";
    }
    else {
      ret << " variable |";
    }
  }
  return ret.str();
}

string getInitialAssignmentTable(Model* model)
{
  stringstream ret;
  if (model->getNumParameters() + model->getNumSpecies() + model->getNumCompartments() == 0) return "";
  ret << "The initial conditions are as follows:" << endl << endl;
  ret << "[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |";
  ret << getInitialSpeciesLevels(model, true);
  ret << getInitialSpeciesLevels(model, false);
  ret << getInitialParameterLevels(model, true);
  ret << getInitialParameterLevels(model, false);
  ret << getInitialCompartmentLevels(model, true);
  ret << getInitialCompartmentLevels(model, false);
  ret << "]" << endl;
  return ret.str();
}

bool allSpeciesSetAmountUsedConcentration(Model* model)
{
  if (model->getNumSpecies() == 0) return false;
  for (unsigned int s=0; s<model->getNumSpecies(); s++) {
    Species* sp = model->getSpecies(s);
    if (!sp->isSetInitialAmount()) return false;
    if (sp->getHasOnlySubstanceUnits()) return false;
  }
  return true;
}

string getModelSummary(Model* model,  const map<string, vector<double> >& results, bool flat)
{
  stringstream ret;
  if (flat) {
    ret << "The 'flattened' version of this hierarchical model contains:\n";
  }
  else {
    ret << "The model contains:\n";
  }
  vector<string> namelist;
  namelist = getIdListFrom(model->getListOfSpecies());
  if (namelist.size()>0) {
    ret << "* " << namelist.size() << " species (" << getString(namelist) << ")\n";
  }
  namelist = getIdListFrom(model->getListOfParameters());
  if (namelist.size()>0) {
    ret << "* " << namelist.size() << " parameter";
    if (namelist.size() > 1) ret << "s";
    ret << " (" << getString(namelist) << ")\n";
  }
  namelist = getIdListFrom(model->getListOfCompartments());
  if (namelist.size()>0) {
    ret << "* " << namelist.size() << " compartment";
    if (namelist.size() > 1) ret << "s";
    ret << " (" << getString(namelist) << ")\n";
  }

  if (model->getNumConstraints() > 0 || model->getNumFunctionDefinitions() > 0) {
    ret << endl << "It also contains ";
    if (model->getNumConstraints() > 0) {
      ret << model->getNumConstraints() << " constraints (";
      for (unsigned int c=0; c<model->getNumConstraints(); c++) {
        if (c>1) ret << ", ";
        ret << SBML_formulaToString(model->getConstraint(c)->getMath());
      }
      ret << ") ";
      if (model->getNumFunctionDefinitions() > 0) ret << "and ";
    }
    if (model->getNumFunctionDefinitions() > 0) {
      ret << model->getNumFunctionDefinitions() << " function definition(s):\n";
      for (unsigned int fd=0; fd<model->getNumFunctionDefinitions(); fd++) {
        const FunctionDefinition* func = model->getFunctionDefinition(fd);
        ret << "; " << func->getId() << ": $" 
            << SBML_formulaToString(func->getMath()->getRightChild()) << "$" << endl;
      }
    }
  }

  bool fbc = false;
#ifdef USE_FBC
  FbcModelPlugin* fmp = static_cast<FbcModelPlugin*>(model->getPlugin("fbc"));
  if (fmp != NULL) {
    namelist = getIdListFrom(fmp->getListOfObjectives());
    if (namelist.size()>0) {
      fbc = true;
      ret << "* " << namelist.size() << " objective";
      if (namelist.size() > 1) ret << "s";
      ret << " (" << getString(namelist) << ")";
      ret << endl << endl;
      ret << "The active objective is ";
      Objective* obj = fmp->getActiveObjective();
      if (obj == NULL) {
        ret << "unset (which is an error)." << endl;
      }
      else {
        ret << obj->getId() << ", which is to be ";
        switch (obj->getObjectiveType()) {
        case OBJECTIVE_TYPE_MAXIMIZE:
          ret << "maximized";
          break;
        case OBJECTIVE_TYPE_MINIMIZE:
          ret << "minimized";
          break;
        case OBJECTIVE_TYPE_UNKNOWN:
          ret << "maximized or minimized, but it's unset which (this is an error)";
          break;
        }
        ret << ":" << endl;
        for (unsigned int fo = 0; fo<obj->getNumFluxObjectives(); fo++) {
          FluxObjective* fluxobj = obj->getFluxObjective(fo);
          double coeff = fluxobj->getCoefficient();
          if (coeff < 0) {
            ret << "  ";
          }
          else {
            ret << "  + ";
          }
          ret << coeff << " " << fluxobj->getReaction() << endl;
        }
      }
    }
  }
#endif

  ret << getReactionTable(model, results);
  ret << getEventTable(model);
  ret << getRuleTable(model);
  if (!fbc) {
    ret << getInitialAssignmentTable(model);
  }
  ret << endl;

  if (allSpeciesSetAmountUsedConcentration(model)) {
    ret << "The species' initial quantities are given in terms of substance units to" << endl;
    ret << "make it easier to use the model in a discrete stochastic simulator, but" << endl;
    ret << "their symbols represent their values in concentration units where they" << endl;
    ret << "appear in expressions." << endl << endl;
  }

  ret << "{Keep this next line if 'generatedBy' is 'Analytic':}" << endl;
  ret << "Note: The test data for this model was generated from an analytical" << endl;
  ret << "solution of the system of equations." << endl;

  return ret.str();
}

string getSuiteHeaders(vector<string> levelsandversions, Model* model,  const map<string, vector<double> >& results)
{
  set<string> components;
  set<string> tests;
  set<string> packages;
  SBMLDocument* doc = model->getSBMLDocument();
  string testType = "timeCourse";

#ifdef USE_COMP
  CompSBMLDocumentPlugin* compdoc = static_cast<CompSBMLDocumentPlugin*>(doc->getPlugin("comp"));
  unsigned int nummds = 0;
  unsigned int numxmds = 0;
  if (compdoc != NULL) {
    packages.insert("comp");
    if (!compdoc->getRequired()) {
      tests.insert("comp:NotRequired");
    }
    checkComp(compdoc, components, tests, results);
    nummds = compdoc->getNumModelDefinitions();
    numxmds = compdoc->getNumExternalModelDefinitions();
  }
  CompModelPlugin* modplug = static_cast<CompModelPlugin*>(model->getPlugin("comp"));
  SBMLDocument newdoc = *doc;
  if (modplug != NULL) {
    ConversionProperties* props = new ConversionProperties();
    props->addOption("flatten comp");
    SBMLConverter* converter = 
      SBMLConverterRegistry::getInstance().getConverterFor(*props);
    converter->setDocument(&newdoc);
    converter->convert();
    //Only draw other tags from the flattened model
    model = newdoc.getModel();
  }
#endif

  bool fbc = false;
#ifdef USE_FBC
  FbcModelPlugin* fbcmodplug = static_cast<FbcModelPlugin*>(model->getPlugin("fbc"));
  if (fbcmodplug != NULL) {
    packages.insert("fbc");
    checkFbc(fbcmodplug, components, tests, results);
    testType = "FluxBalanceSteadyState";
    fbc = true;
  }
#endif

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
  if (modxml.find("http://www.sbml.org/sbml/symbols/avogadro") != string::npos) {
    components.insert("CSymbolAvogadro");
  }
  if (modxml.find("http://www.sbml.org/sbml/symbols/delay") != string::npos) {
    components.insert("CSymbolDelay");
  }
  if (modxml.find("http://www.sbml.org/sbml/symbols/time") != string::npos) {
    components.insert("CSymbolTime");
  }

  string ret = "";
  ret += "category:        Test\n";
  ret += "synopsis:        [[Write description here.]]\n";
  ret += "componentTags:   " + getString(components) + "\n";
  ret += "testTags:        " + getString(tests) + "\n";
  ret += "testType:        " + testType + "\n";
  ret += "levels:          " + getString(levelsandversions) + "\n";
  ret += "generatedBy:     Analytic||Numeric\n";
  ret += "packagesPresent: " + getString(packages) + "\n";
  ret += "\n";
  ret += "{Write general description of why you have created the model here.}\n";
  ret += "\n";
  return ret;
}

void
writeMFile(string contents, string modfilename)
{
  size_t sbml_place = modfilename.find("-sbml-l");
  if (sbml_place==string::npos) {
    cerr << "Error:  the filename '" << modfilename
         << "' doesn't have the substring '-sbml-l' in it.  Can't write .m file" << endl;
    return;
  }
  string filename = modfilename.replace(sbml_place, 14, "-model.m");
  ifstream infile(filename);
  string oldfile(""), tmp;
  bool commented = false;
  bool writeup = false;
  int analytic = 0;
  string description = "";
  while (!infile.eof() && infile.good()) {
    getline(infile, tmp);
    oldfile += tmp + "\n";
    if (tmp.find("/*") != string::npos) {
      commented = true;
    }
    if (!commented) {
      if (tmp.find("synopsis:") != string::npos) {
        size_t newsynopsis = contents.find("synopsis:");
        size_t endsynop = contents.find("\n", newsynopsis);
        contents.replace(newsynopsis, endsynop-newsynopsis, tmp);
      }
      if (tmp.find("model contains:") != string::npos) {
        writeup = false;
      }
      if (writeup) {
        if (tmp.find("packagesPresent")==string::npos &&
            tmp.size() > 1) {
              description += tmp;
        }
      }
      if (tmp.find("generatedBy:") != string::npos) {
        writeup = true;
        size_t newsynopsis = contents.find("generatedBy:");
        size_t endsynop = contents.find("\n", newsynopsis);
        contents.replace(newsynopsis, endsynop-newsynopsis, tmp);
        if (tmp.find("Analytic||Numeric") != string::npos) {
          analytic = 0;
        }
        else if (tmp.find("Analytic") != string::npos) {
          analytic = 1;
        }
        else if (tmp.find("Numeric") != string::npos) {
          analytic = 2;
        }
      }
    }
  }
  if (description.size() > 0) {
    size_t gendisc = contents.find("{Write general");
    contents.replace(gendisc, 67, description);
  }
  if (analytic > 0) {
    size_t keepline = contents.find("{Keep this next");
    if (analytic==1) {
      contents.replace(keepline, 54, "");
    }
    else if (analytic==2) {
      contents.replace(keepline, 54+68+38, "");
    }
  }
  ofstream file(filename);
  file << contents << endl;

  //if (!oldfile.empty()) {
  //  file << "/*" << endl
  //       << "Previous version of this file:  " << endl
  //       << oldfile << endl
  //       << "*/" << endl;
  //}
  file.close();
  cout << "Successfully wrote model description file " << modfilename << endl;
}

void writeSettingsFile(string modfilename, bool fbc)
{
  size_t sbml_place = modfilename.find("-sbml-l");
  if (sbml_place==string::npos) {
    cerr << "Error:  the filename '" << modfilename
         << "' doesn't have the substring '-sbml-l' in it.  Can't write settings file" << endl;
    return;
  }
  string filename = modfilename.replace(sbml_place, 14, "-settings.txt");
  ifstream infile(filename);
  if (infile.good()) return; //Don't overwrite old settings files.
  string start = "0";
  string duration = "__";
  string steps = "__";
  string variables = "__";
  string abs = "0.0001";
  string rel = "0.0001";
  if (fbc) {
    start = "";
    duration = "";
    steps = "";
    abs = "";
  }
  ofstream file(filename);
  file << "start: " << start << endl;
  file << "duration: " << duration << endl;
  file << "steps: " << steps << endl;
  file << "variables: " << variables << endl;
  file << "absolute: " << abs << endl;
  file << "relative: " << rel << endl;
  file << "amount: " << endl;
  file << "concentration: " << endl;
}

int
main (int argc, char* argv[])
{
  if (argc < 2)
  {
    cerr << endl << "Usage: generateTestsFrom [-t] filename-sbml-l[#]v[#].xml" << endl << endl;
    return 1;
  }

  bool translateonly = false;
  string filename = "";
  for (int a=1; a<argc; a++) {
    string option = argv[a];
    if (option == "-t") {
      translateonly = true;
    }
    else {
      filename = option;
    }
  }
  SBMLDocument* document = readSBML(filename.c_str());
  Model* model = document->getModel();

  if (model == NULL)
  {
    cout << "No model present." << endl;
    return 1;
  }

  if (hasActualErrors(document))
  {
    cerr << "Encountered the following SBML errors:" << endl;
    printActualErrors(document);
    return 1;
  }

  //Get the results file, if one exists.
  unsigned int level   = document->getLevel  ();
  unsigned int version = document->getVersion();
  string lxvx = "sbml-l" + toString(level) + "v" + toString(version);
  size_t lxvx_place = filename.find(lxvx);
  string resultsfilename = filename;
  resultsfilename.replace(lxvx_place,13,"results.csv");
  map<string, vector<double> >& results = getResults(resultsfilename);

  vector<string> levelsandversions = createTranslations(document, filename);
  if (!translateonly) {
    string mfile = "(*\n\n";
    mfile += getSuiteHeaders(levelsandversions, model, results);
    bool flat = false;
#ifdef USE_COMP
    SBMLDocument newdoc = *document;
    CompModelPlugin* cmp = static_cast<CompModelPlugin*>(model->getPlugin("comp"));
    if (cmp != NULL && cmp->getNumSubmodels() > 0) {
      ConversionProperties* props = new ConversionProperties();
      props->addOption("flatten comp");
      SBMLConverter* converter = 
        SBMLConverterRegistry::getInstance().getConverterFor(*props);
      converter->setDocument(&newdoc);
      converter->convert();
      //Only draw other tags from the flattened model
      model = newdoc.getModel();
      flat = true;
      //Write out the flattened version of the model.
      model->disablePackage(CompExtension::getXmlnsL3V1V1(), "comp");
      SBMLDocument flatdoc(3,1);
      flatdoc.setModel(model);
      string flatfilename = filename;
      size_t sbml_place = flatfilename.find(".xml");
      flatfilename = flatfilename.insert(sbml_place, "-flat");
      writeSBMLToFile(&flatdoc, flatfilename.c_str());
    }
#endif
    mfile += getModelSummary(model, results, flat);
    mfile += "\n*)";
    writeMFile(mfile, filename);
    bool fbc = false;
#ifdef USE_FBC
    FbcModelPlugin* fbcmodplug = static_cast<FbcModelPlugin*>(document->getModel()->getPlugin("fbc"));
    if (fbcmodplug != NULL) {
      fbc = true;
    }
#endif
    writeSettingsFile(filename, fbc);
  }

  delete document;
  return 0;
}
