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

#include "../testSuiteUtil/testSuiteUtil.cpp"

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
    if (translatedDoc->setLevelAndVersion(1, 2, true) && !hasActualErrors(translatedDoc)) {
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
  for (int v=1; v<6; v++) {
    if (level==2 && version==v) {
      ret.push_back("2." + toString(v));
    }
    else {
      translatedDoc = document->clone();
      if (translatedDoc->setLevelAndVersion(2, v, true) && !hasActualErrors(translatedDoc)) {
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

  //L3v*
  for (int v = 1; v<=2; v++) {
	  if (level == 3 && version == v) {
		  ret.push_back("3." + toString(v));
	  }
	  else {
		  translatedDoc = document->clone();
		  if (translatedDoc->setLevelAndVersion(3, v, true) && !hasActualErrors(translatedDoc)) {
			  string thislv = "l3v" + toString(v);
			  string newfilename = filename.replace(lxvx_place, 4, thislv);
			  if (newfilename == oldfilename) {
				  ret.push_back("3." + toString(v));
			  }
			  else if (writeSBMLToFile(translatedDoc, newfilename.c_str()) == 1) {
				  translatedDoc->setLocationURI("file:" + newfilename);
				  cout << "Successfully wrote translation of model to level 3 version " << v << endl;
				  ret.push_back("3." + toString(v));
			  }
		  }
		  delete translatedDoc;
	  }
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

vector<string> getLocalParameterIdListFrom(const Model* model)
{
  vector<string> ret;
  for (unsigned int rxn=0; rxn<model->getNumReactions(); rxn++) {
    const Reaction* reaction = model->getReaction(rxn);
    string rxnid = reaction->getId();
    const KineticLaw* kl = reaction->getKineticLaw();
    if (kl==NULL) continue;
    for (unsigned int lp=0; lp<kl->getNumLocalParameters(); lp++) {
      ret.push_back(rxnid + "." + kl->getLocalParameter(lp)->getId());
    }
  }
  return ret;
}

vector<string> getSpeciesReferenceIdListFrom(const Model* model)
{
  vector<string> ret;
  for (unsigned int rxn = 0; rxn<model->getNumReactions(); rxn++) {
    const Reaction* reaction = model->getReaction(rxn);
    for (unsigned int sr = 0; sr < reaction->getNumReactants(); sr++) {
      const SpeciesReference* specref = reaction->getReactant(sr);
      if (specref->isSetId()) {
        ret.push_back(specref->getId());
      }
    }
    for (unsigned int sr = 0; sr < reaction->getNumProducts(); sr++) {
      const SpeciesReference* specref = reaction->getProduct(sr);
      if (specref->isSetId()) {
        ret.push_back(specref->getId());
      }
    }
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
        FbcReactionPlugin* fbcrxnplug = static_cast<FbcReactionPlugin*>(rxn->getPlugin("fbc"));
        if (fbcrxnplug != NULL && fbcrxnplug->getPackageVersion() == 2) {
          string lower = "(unset)";
          string upper = "(unset)";
          if (fbcrxnplug->isSetLowerFluxBound()) {
            lower = fbcrxnplug->getLowerFluxBound();
          }
          if (fbcrxnplug->isSetUpperFluxBound()) {
            upper = fbcrxnplug->getUpperFluxBound();
          }
          ret << "$" << lower << " <= " << rxn->getId() << " <= " << upper << "$";
          nobounds = false;
        }
        else {
          for (unsigned int b = 0; b < fbc->getNumFluxBounds(); b++) {
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
              ret << " ";
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
      if (rxn->isSetKineticLaw() && rxn->getKineticLaw()->isSetMath()) {
        ret << "$" << SBML_formulaToL3String(rxn->getKineticLaw()->getMath()) << "$";
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
  bool someConversionFactors = false;
  if (model->isSetConversionFactor()) {
    someConversionFactors = true;
    ret << "The conversion factor for the model is '" << model->getConversionFactor() << "'" << endl;
  }
  for (unsigned long sp = 0; sp < model->getNumSpecies(); sp++) {
    Species* species = model->getSpecies(sp);
    if (species->isSetConversionFactor()) {
      someConversionFactors = true;
      ret << "The conversion factor for the species '" << species->getId() << "' is '" << species->getConversionFactor() << "'" << endl;
    }
  }
  if (someConversionFactors) {
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
    if (trigger == NULL) {
      return;
    }
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
      string math = "(unset)";
      if (event->isSetTrigger() && event->getTrigger()->isSetMath()) {
        math = SBML_formulaToL3String(event->getTrigger()->getMath());
      }
      ret << "$" << math << "$ | ";
      if (priority) {
        if (event->isSetPriority() && event->getPriority()->isSetMath()) {
          ret << "$" << SBML_formulaToL3String(event->getPriority()->getMath()) << "$";
        }
        else ret << "(unset)";
        ret << " | ";
      }
      if (persistent && event->isSetTrigger()) {
        ret << getString(event->getTrigger()->getPersistent()) << " | ";
      }
      if (t0 && event->isSetTrigger()) {
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
        if (event->isSetDelay() && event->getDelay()->isSetMath()) {
          ret << "$" << SBML_formulaToL3String(event->getDelay()->getMath()) << "$ | ";
        }
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
          string math = "";
          if (ea->isSetMath()) {
            math = SBML_formulaToL3String(ea->getMath());
          }
          ret << "$" << ea->getVariable() << " = " << math << "$ |";
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
      string math = "";
      if (rule->isSetMath()) {
        math = SBML_formulaToL3String(rule->getMath());
      }
      ret << " | " << var << " | $" << math << "$ |";
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
    if ((rule != NULL && rule->getTypeCode() != SBML_RATE_RULE && rule->isSetMath()) || (ia != NULL &&  ia->isSetMath())) {
      if (species->getHasOnlySubstanceUnits()) {
        ret << "Initial amount of species ";
      }
      else {
        ret << "Initial concentration of species ";
      }
      ret << id << " | $"; 
      if (ia != NULL && ia->isSetMath()) {
        ret << SBML_formulaToL3String(ia->getMath()) << "$ |";
      }
      else {
        ret << SBML_formulaToL3String(rule->getMath()) << "$ |";
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
    if ((rule != NULL && rule->getTypeCode() != SBML_RATE_RULE) && rule->isSetMath() || ia != NULL && ia->isSetMath()) {
      if (ia != NULL) {
        ret << string(SBML_formulaToL3String(ia->getMath())) << "$ |";
      }
      else {
        ret << string(SBML_formulaToL3String(rule->getMath())) << "$ |";
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
    if ((rule != NULL && rule->getTypeCode() != SBML_RATE_RULE) || ia != NULL) {
      if (ia != NULL) {
        ret << SBML_formulaToL3String(ia->getMath()) << "$ |";
      }
      else {
        ret << SBML_formulaToL3String(rule->getMath()) << "$ |";
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

string getInitialLocalParameterLevels(const Model* model)
{
  stringstream ret;
  for (unsigned long r=0; r<model->getNumReactions(); r++) {
    const Reaction* rxn = model->getReaction(r);
    string id = rxn->getId();
    const KineticLaw* kl = rxn->getKineticLaw();
    if (kl==NULL) {
      continue;
    }
    for (unsigned long lp=0; lp<kl->getNumLocalParameters(); lp++) {
      const LocalParameter* localparam = kl->getLocalParameter(lp);
      ret << endl << "| Initial value of local parameter '" << id << "." << localparam->getId() << "' | $"; 
      if (localparam->isSetValue()) {
        ret << localparam->getValue() << "$ |";
      }
      else {
        ret << "unknown$ |";
      }
      ret << " constant |";
    }
  }
  return ret.str();
}

string writeSpeciesReferenceInitLevels(const Model* model, const SpeciesReference* sr)
{
  stringstream ret;
  string id = sr->getIdAttribute();
  const InitialAssignment* ia = model->getInitialAssignment(id);
  const AssignmentRule* ar = model->getAssignmentRule(id);
  const StoichiometryMath* sm = sr->getStoichiometryMath();
  bool haveSomething = false;
  if (ia != NULL && ia->isSetMath()) {
    ret << endl << "| Initial value of species reference '" << id << "' | $";
    char* math = SBML_formulaToL3String(ia->getMath());
    ret << math << "$ |";
    delete(math);
    haveSomething = true;
  }
  else if (ar != NULL && ar->isSetMath()) {
    ret << endl << "| Initial value of species reference '" << id << "' | $";
    char* math = SBML_formulaToL3String(ar->getMath());
    ret << math << "$ |";
    delete(math);
    haveSomething = true;
  }
  else if (sm != NULL && sm->isSetMath()) {
    ret << endl << "| Initial value of species reference '" << id << "' | $";
    char* math = SBML_formulaToL3String(sm->getMath());
    ret << math << "$ |";
    delete(math);
    haveSomething = true;
  }
  else if (sr->isSetStoichiometry()) {
    ret << endl << "| Initial value of species reference '" << id << "' | $" << sr->getStoichiometry() << "$ |";
    haveSomething = true;
  }

  if (haveSomething) {
    if (sr->getConstant()) {
      ret << " constant |";
    }
    else {
      ret << " variable |";
    }
  }
  return ret.str();
}

string getInitialSpeciesReferenceLevels(const Model* model)
{
  stringstream ret;
  for (unsigned long r = 0; r<model->getNumReactions(); r++) {
    const Reaction* rxn = model->getReaction(r);
    for (unsigned long rt = 0; rt < rxn->getNumReactants(); rt++) {
      const SpeciesReference* sr = rxn->getReactant(rt);
      if (sr->isSetId()) {
        ret << writeSpeciesReferenceInitLevels(model, sr);
      }
    }
    for (unsigned long pr = 0; pr < rxn->getNumProducts(); pr++) {
      const SpeciesReference* sr = rxn->getProduct(pr);
      if (sr->isSetId()) {
        ret << writeSpeciesReferenceInitLevels(model, sr);
      }
    }
  }
  return ret.str();
}

string getInitialAssignmentTable(Model* model, bool fbc)
{
  stringstream ret;
  if (model->getNumParameters() + model->getNumSpecies() + model->getNumCompartments() == 0) return "";
  if (fbc && model->getNumParameters() == 0) return "";
  ret << "The initial conditions are as follows:" << endl << endl;
  ret << "[{width:35em,margin: 1em auto}|       | *Value* | *Constant* |";
  if (!fbc) {
    ret << getInitialSpeciesLevels(model, true);
    ret << getInitialSpeciesLevels(model, false);
  }
  ret << getInitialParameterLevels(model, true);
  ret << getInitialParameterLevels(model, false);
  if (!fbc) {
    ret << getInitialLocalParameterLevels(model);
    ret << getInitialCompartmentLevels(model, true);
    ret << getInitialCompartmentLevels(model, false);
  }
  ret << getInitialSpeciesReferenceLevels(model);
  ret << "]" << endl;
  return ret.str();
}

bool allSpeciesSetAmountUsedConcentration(Model* model)
{
  if (model->getNumSpecies() == 0) return false;
  for (unsigned int s=0; s<model->getNumSpecies(); s++) {
    Species* sp = model->getSpecies(s);
    if (!sp->isSetInitialAmount() && sp->isSetInitialConcentration()) return false;
    if (sp->getHasOnlySubstanceUnits()) return false;
  }
  return true;
}

string getFunctionDefinitionTable(Model* model)
{
  stringstream ret;
  unsigned int fdnum = model->getNumFunctionDefinitions();
  if (fdnum == 0) return "";
  ret << "The model contains the following function definition";
  if (fdnum > 1) {
    ret << "s";
  }
  ret << ":" << endl << endl;
  ret << "[{width:30em,margin: 1em auto}|  *Id*  |  *Arguments*  |  *Formula*  |" << endl;
  for (unsigned int fd = 0; fd < fdnum; fd++) {
    FunctionDefinition* functiondef = model->getFunctionDefinition(fd);
    ret << " | " << functiondef->getId() << " | ";
    for (unsigned int arg = 0; arg < functiondef->getNumArguments(); arg++) {
      if (arg > 0) {
        ret << ", ";
      }
      ret << SBML_formulaToL3String(functiondef->getArgument(arg));
    }
    string math = "";
    if (functiondef->isSetMath()) {
      math = SBML_formulaToL3String(functiondef->getBody());
    }
    ret << " | $" << math << "$ |" << endl;
  }
  ret << "]" << endl;
  return ret.str();
}

string getModelSummary(Model* model,  const map<string, vector<double> >& results, bool flat, int type)
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
  namelist = getLocalParameterIdListFrom(model);
  if (namelist.size()>0) {
    ret << "* " << namelist.size() << " local parameter";
    if (namelist.size() > 1) ret << "s";
    ret << " (" << getString(namelist) << ")\n";
  }
  namelist = getIdListFrom(model->getListOfCompartments());
  if (namelist.size()>0) {
    ret << "* " << namelist.size() << " compartment";
    if (namelist.size() > 1) ret << "s";
    ret << " (" << getString(namelist) << ")\n";
  }
  namelist = getSpeciesReferenceIdListFrom(model);
  if (namelist.size()>0) {
    ret << "* " << namelist.size() << " species reference";
    if (namelist.size() > 1) ret << "s";
    ret << " (" << getString(namelist) << ")\n";
  }

  if (model->getNumConstraints() > 0) {
    ret << endl << "It also contains ";
    if (model->getNumConstraints() > 0) {
      ret << model->getNumConstraints() << " constraints (";
      for (unsigned int c=0; c<model->getNumConstraints(); c++) {
        if (c>1) ret << ", ";
        if (model->getConstraint(c)->isSetMath()) {
          ret << SBML_formulaToL3String(model->getConstraint(c)->getMath());
        }
        else {
          ret << "(unset)";
        }
      }
      ret << ") ";
      if (model->getNumFunctionDefinitions() > 0) ret << "and ";
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
  ret << getFunctionDefinitionTable(model);
  ret << getInitialAssignmentTable(model, fbc);
  ret << endl;

  if (allSpeciesSetAmountUsedConcentration(model)) {
    ret << "The species' initial quantities are given in terms of substance units to" << endl;
    ret << "make it easier to use the model in a discrete stochastic simulator, but" << endl;
    ret << "their symbols represent their values in concentration units where they" << endl;
    ret << "appear in expressions." << endl << endl;
  }

  if (type!=2) {
    ret << "{Keep this next line if 'generatedBy' is 'Analytic':}" << endl;
  }
  ret << "Note: The test data for this model was generated from an analytical" << endl;
  ret << "solution of the system of equations." << endl;

  return ret.str();
}

string getSuiteHeaders(vector<string> levelsandversions, Model* model,  const map<string, vector<double> >& results, int type)
{
  set<string> components;
  set<string> tests;
  set<string> packages;
  SBMLDocument* doc = model->getSBMLDocument();
  string testType = "TimeCourse";
  string generatedBy = "Analytic||Numeric";
  if (type==1) {
    testType = "FluxBalanceSteadyState";
  }
  else if (type==2) {
    testType = "StochasticTimeCourse";
    generatedBy = "Analytic";
  }

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

#ifdef USE_FBC
  FbcModelPlugin* fbcmodplug = static_cast<FbcModelPlugin*>(model->getPlugin("fbc"));
  if (fbcmodplug != NULL) {
    packages.insert("fbc");
    checkFbc(fbcmodplug, components, tests, results);
    testType = "FluxBalanceSteadyState";
    switch(fbcmodplug->getPackageVersion()) {
    case 1:
      packages.insert("fbc_v1");
      break;
    case 2:
      packages.insert("fbc_v2");
      break;
    default:
      assert(false);
    }
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
  checkReactions(model, components, tests, results, type);
  checkSpecies(model, components, tests, results, type);
  if (model->isSetConversionFactor()) {
    tests.insert("ConversionFactors");
  }
  string modxml = model->toSBML();
  checkMathML(modxml, components, tests);
  checkConstraints(model, components, tests);
  checkFunctionDefinitions(model, components, tests);

  checkBooleans(doc, components, tests);

  string ret = "";
  ret += "category:        Test\n";
  ret += "synopsis:        [[Write description here.]]\n";
  ret += "componentTags:   " + getString(components) + "\n";
  ret += "testTags:        " + getString(tests) + "\n";
  ret += "testType:        " + testType + "\n";
  ret += "levels:          " + getString(levelsandversions) + "\n";
  ret += "generatedBy:     " + generatedBy + "\n";
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
    if (analytic==1 && keepline != string::npos) {
      contents.replace(keepline, 54, "");
    }
    else if (analytic==2 && keepline != string::npos) {
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

void writeSettingsFile(string modfilename, int type, Model* model)
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
  string amount = "";
  string concentration = "";
  string output = "";
  if (type==1) {
    //FBC test
    start = "";
    duration = "";
    steps = "";
    abs = "";
  }
  if (type==2) {
    //Stochastic
    duration = "50";
    steps = "50";
    abs = "";
    rel = "";
    variables = "";
    for (unsigned int s=0; s<model->getNumSpecies(); s++) {
      if (s>0) {
        variables += ", ";
        output += ", ";
      }
      string id = model->getSpecies(s)->getId();
      variables += id;
      output += id + "-mean, " + id + "-sd";
    }
    amount = variables;
  }
  ofstream file(filename);
  file << "start: " << start << endl;
  file << "duration: " << duration << endl;
  file << "steps: " << steps << endl;
  file << "variables: " << variables << endl;
  file << "absolute: " << abs << endl;
  file << "relative: " << rel << endl;
  file << "amount: " << amount << endl;
  file << "concentration: " << concentration << endl;
  if (type==2) {
    //Stochastic
    file << "output: " << output << endl;
    file << "meanRange: (-3, 3)" << endl;
    file << "sdRange: (-5, 5)" << endl;
  }
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
  int type = 0;
  string filename = "";
  for (int a=1; a<argc; a++) {
    string option = argv[a];
    if (option == "-t") {
      translateonly = true;
    }
    else if (option== "-s") {
      type = 2;
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
  if (lxvx_place == string::npos) {
    cerr << "The filename '" << filename << "does not have the correct level/version in it (" << lxvx << ").";
    return 1;
  }
  string resultsfilename = filename;
  resultsfilename.replace(lxvx_place,13,"results.csv");
  map<string, vector<double> >& results = getResults(resultsfilename);

  vector<string> levelsandversions = createTranslations(document, filename);
  if (!translateonly) {
#ifdef USE_FBC
    FbcModelPlugin* fbcmodplug = static_cast<FbcModelPlugin*>(document->getModel()->getPlugin("fbc"));
    if (fbcmodplug != NULL) {
      type = 1;
    }
#endif
    string mfile = "(*\n\n";
    mfile += getSuiteHeaders(levelsandversions, model, results, type);
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
    mfile += getModelSummary(model, results, flat, type);
    mfile += "\n*)";
    writeMFile(mfile, filename);
    writeSettingsFile(filename, type, model);
  }

  delete document;
  return 0;
}
