/**
 * @file    testSuiteUtil.cpp
 * @brief   Functions used in both generateTestsFrom and checkTestCases.
 * @author  Lucian Smith
 * 
 * This file is part of libSBML.  Please visit http://sbml.org for more
 * information about SBML, and the latest version of libSBML.
 */


#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <algorithm>
#include <map>
#include <set>
#include <sstream>

#include <sbml/SBMLTypes.h>

using namespace std;
LIBSBML_CPP_NAMESPACE_USE

bool variesIn(string id,  const map<string, vector<double> >& results) {
  map<string, vector<double> >::const_iterator entry = results.find(id);
  if (entry != results.end()) {
    vector<double> results = entry->second;
    if (results.size() > 0) {
      double first = results[0];
      for (size_t v=0; v<results.size(); v++) {
        if (results[v] != first) return true;
      }
    }
  }
  return false;
}

vector<string> getStrings(string line) {
  vector<string> ret;
  size_t comma = line.find(',');
  while (comma != string::npos) {
    ret.push_back(line.substr(0, comma));
    line.erase(0, comma+1);
    comma = line.find(',');
  }
  ret.push_back(line);
  return ret;
}

void insertStrings(string linestr, map<string, vector<double> >& results, vector<string>& keys) {
  stringstream line(linestr);
  double value;
  size_t keyn = 0;
  while (isdigit(line.peek())) {
    line >> value;
    string key = keys[keyn];
    results.find(key)->second.push_back(value);
    line.get(); //The comma.
    keyn++;
  }
}

map<string, vector<double> > getResults(string filename) {
  map<string, vector<double> > ret;
  ifstream infile(filename);
  string tmp;
  if (!infile.good()) return ret;
  getline(infile, tmp);
  vector<string> headers = getStrings(tmp);
  for (size_t h=0; h<headers.size(); h++) {
    vector<double> results;
    ret.insert(make_pair(headers[h], results));
  }
  while (!infile.eof() && infile.good()) {
    getline(infile, tmp);
    insertStrings(tmp, ret, headers);
  }
  return ret;
}

bool hasActualErrors(SBMLDocument* document)
{
  document->checkConsistency();
  for (unsigned int e=0; e<document->getNumErrors(); e++) {
    if (document->getError(e)->getSeverity() >= LIBSBML_SEV_ERROR) return true;
  }
  return false;
}

bool variesIn(string id, ListOfSpeciesReferences* srs,  const map<string, vector<double> >& results)
{
  if (variesIn(id, results)) return true;
  for (unsigned long sr=0; sr<srs->size(); sr++) {
    SpeciesReference* spref = static_cast<SpeciesReference*>(srs->get(sr));
    if (spref->getSpecies() == id) return true;
  }
  return false;
}

bool variesIn(const ASTNode* math, Model* model,  const map<string, vector<double> >& results);

bool getConstant(string id, Model* model,  const map<string, vector<double> >& results)
{
  if (variesIn(id, results)) return false;
  SBase* element = model->getElementBySId(id);
  Compartment* comp = static_cast<Compartment*>(element);
  Species* species = static_cast<Species*>(element);
  Parameter* param = static_cast<Parameter*>(element);
  Reaction* rxn = static_cast<Reaction*>(element);
  SpeciesReference* sr= static_cast<SpeciesReference*>(element);
  switch (element->getTypeCode()) {
  case SBML_COMPARTMENT:
    if (!comp->getConstant()) return false;
    break;
  case SBML_SPECIES:
    if (!species->getConstant()) return false;
    break;
  case SBML_PARAMETER:
    if (!param->getConstant()) return false;
    break;
  case SBML_REACTION:
    if (rxn->isSetKineticLaw()) {
      if (variesIn(rxn->getKineticLaw()->getMath(), model, results)) return false;
    }
    break;
  case SBML_SPECIES_REFERENCE:
    if (!sr->getConstant()) return false;
    break;
  default:
    assert(false); //Uncaught type!
    return true;
    break;
  }
  return true;
}

bool variesIn(const ASTNode* math, Model* model,  const map<string, vector<double> >& results)
{
  if (math->getType() == AST_NAME_TIME) return true;
  if (math->getType() == AST_NAME) {
    string id = math->getName();
    if (!getConstant(id, model, results)) return true;
  }
  for (unsigned int c=0; c<math->getNumChildren(); c++) {
    ASTNode* submath = math->getChild(c);
    if (variesIn(submath, model, results)) return true;
  }
  return false;
}

bool appearsIn(string id, const ASTNode* math)
{
  if (math->getType() == AST_NAME) {
    if (id==math->getName()) return true;
  }
  for (unsigned int c=0; c<math->getNumChildren(); c++) {
    ASTNode* submath = math->getChild(c);
    if (appearsIn(id, submath)) return true;
  }
  return false;
}

bool variesBesides(string mayvar, const ASTNode* math, Model* model,  const map<string, vector<double> >& results)
{
  if (math->getType() == AST_NAME_TIME) return true;
  if (math->getType() == AST_NAME) {
    string id = math->getName();
    if (id != mayvar && !getConstant(id, model, results)) return true;
  }
  for (unsigned int c=0; c<math->getNumChildren(); c++) {
    ASTNode* submath = math->getChild(c);
    if (variesBesides(mayvar, submath, model, results)) return true;
  }
  return false;
}

bool variesIn(string id, Model* model,  const map<string, vector<double> >& results)
{
  if (variesIn(id, results)) return true;
  Rule* rule = model->getRule(id);
  if (rule != NULL) {
    if (rule->getTypeCode() == SBML_RATE_RULE) return true;
    return variesIn(rule->getMath(), model, results);
  }
  if (!getConstant(id, model, results)) {
    //The variable might be set by an algebraic rule
    for (unsigned long r=0; r<model->getNumRules(); r++) {
      if (model->getRule(r)->getTypeCode() == SBML_ALGEBRAIC_RULE) {
        if (appearsIn(id, model->getRule(r)->getMath()) &&
            variesBesides(id, model->getRule(r)->getMath(), model, results)) return true;
      }
    }
  }
  for (unsigned long e=0; e<model->getNumEvents(); e++) {
    Event* event = model->getEvent(e);
    for (unsigned long ea=0; ea<event->getNumEventAssignments(); ea++) {
      if (event->getEventAssignment(ea)->getVariable() == id) return true;
    }
  }
  Species* species = model->getSpecies(id);
  if (species==NULL) return false;
  if (species->isSetBoundaryCondition() && species->getBoundaryCondition()==true) return false;
  for (unsigned long r=0; r<model->getNumReactions(); r++) {
    Reaction* rxn = model->getReaction(r);
    if (variesIn(id, rxn->getListOfReactants(), results)) return true;
    if (variesIn(id, rxn->getListOfProducts(), results)) return true;
  }
  return false;
}

bool getInitialResultFor(string id, const map<string, vector<double> >& results, double& initialResult)
{
  map<string, vector<double> >::const_iterator entry = results.find(id);
  if (entry == results.end()) return false;
  if (entry->second.size()==0) return false;
  initialResult = entry->second[0];
  return true;
}

bool getSetValue(string id, Model* model, const set<string>& tests, double& setValue) 
{
  SBase* element = model->getElementBySId(id);
  if (element==NULL) return false;
  Compartment* comp = static_cast<Compartment*>(element);
  Species* species = static_cast<Species*>(element);
  Parameter* param = static_cast<Parameter*>(element);
  Reaction* rxn = static_cast<Reaction*>(element);
  SpeciesReference* sr= static_cast<SpeciesReference*>(element);
  switch(element->getTypeCode()) {
  case SBML_COMPARTMENT:
    if (comp->isSetSize()) {
      setValue = comp->getSize();
      return true;
    }
    break;
  case SBML_SPECIES:
    if (species->isSetInitialAmount()) {
      setValue = species->getInitialAmount();
      if (tests.find("Concentration") != tests.end()) {
        comp = model->getCompartment(species->getCompartment());
        if (comp->isSetSize()) {
          setValue = setValue/comp->getSize();
        }
      }
      return true;
    }
    if (species->isSetInitialConcentration()) {
      setValue = species->getInitialConcentration();
      if (tests.find("Amount") != tests.end()) {
        comp = model->getCompartment(species->getCompartment());
        if (comp->isSetSize()) {
          setValue = setValue*comp->getSize();
        }
      }
      return true;
    }
    break;
  case SBML_PARAMETER:
    if (param->isSetValue()) {
      setValue = param->getValue();
      return true;
    }
    break;
  case SBML_REACTION:
    return false; //We'd have to calculate the math here, which I'm not going to do.
  case SBML_SPECIES_REFERENCE:
    if (sr->isSetStoichiometry()) {
      setValue = sr->getStoichiometry();
      return true;
    }
    break;
  default:
    assert(false); //Uncaught type!
    return false;
    break;
  }
  return false;
}

bool hasAssignmentOrAlgebraic(Model* model)
{
  for (unsigned int i=0; i<model->getNumRules(); i++) {
    int type = model->getRule(i)->getTypeCode();
    if (type==SBML_ASSIGNMENT_RULE || type==SBML_ALGEBRAIC_RULE) return true;
  }
  return false;
}

bool initialOverriddenIn(string id, Model* model, const map<string, vector<double> >& results, const set<string>& tests)
{
  double initialResult = 0;
  if (getInitialResultFor(id, results, initialResult)) {
    double setValue;
    if (getSetValue(id, model, tests, setValue)) {
      if (abs(setValue-initialResult)/max(0.00000000001, abs(setValue)+abs(initialResult)) > .0001) {
        if (model->getNumInitialAssignments() > 0 || hasAssignmentOrAlgebraic(model)) {
          return true;
        }
      }
      return false;
    }
  }
  //There was no initial result in the results file, or we couldn't find the 'set value' so we have to guess.
  if (model->getInitialAssignment(id) != NULL) return true;
  Rule* rule = model->getRule(id);
  if (rule==NULL && !getConstant(id, model, results)) {
    //The variable might be set by an algebraic rule
    for (unsigned long r=0; r<model->getNumRules(); r++) {
      if (model->getRule(r)->getTypeCode() == SBML_ALGEBRAIC_RULE) {
        if ( appearsIn(id, model->getRule(r)->getMath()) ) return true;
      }
    }
  }
  if (rule != NULL && rule->getTypeCode()==SBML_ASSIGNMENT_RULE) return true;
  return false;
}

void checkRules(Model* model, set<string>& components, set<string>& tests)
{
  for (unsigned int r=0; r<model->getNumRules(); r++) {
    Rule* rule = model->getRule(r);
    int typecode = rule->getTypeCode();
    if (typecode == SBML_ALGEBRAIC_RULE) {
      components.insert("AlgebraicRule");
    }
    else if (typecode == SBML_ASSIGNMENT_RULE) {
      components.insert("AssignmentRule");
    }
    else if (typecode == SBML_RATE_RULE) {
      components.insert("RateRule");
    }
  }
}

void checkCompartments(Model* model, set<string>& components, set<string>& tests,  const map<string, vector<double> >& results)
{
  if (model->getNumCompartments() > 0) {
    components.insert("Compartment");
    if (model->getNumCompartments() > 1) {
      tests.insert("MultiCompartment");
    }
    for (unsigned int c=0; c<model->getNumCompartments(); c++) {
      Compartment* compartment = model->getCompartment(c);
      double initialResult;
      if (compartment->isSetSpatialDimensions() && compartment->getSpatialDimensions()==0) {
        tests.insert("0D-Compartment");
      }
      if (compartment->isSetId() && variesIn(compartment->getId(), model, results)) {
        tests.insert("NonConstantCompartment");
        tests.insert("NonUnityCompartment");
      }
      else if (getInitialResultFor(compartment->getId(), results, initialResult)) {
        if (initialResult != 1) {
          tests.insert("NonUnityCompartment");
        }
      }
      else if (compartment->isSetId() && initialOverriddenIn(compartment->getId(), model, results, tests)) {
        tests.insert("NonUnityCompartment");
      }
      else if (compartment->isSetSize()) {
        double size = compartment->getSize();
        if (size != 1.0) {
          tests.insert("NonUnityCompartment");
        }
      }
      if (compartment->isSetId() && initialOverriddenIn(compartment->getId(), model, results, tests)) {
        tests.insert("InitialValueReassigned");
      }
    }
  }
}

void checkEvents(Model* model, set<string>& components, set<string>& tests)
{
  for (unsigned int e=0; e<model->getNumEvents(); e++) {
    Event* event = model->getEvent(e);
    if (event->isSetDelay()) {
      components.insert("EventWithDelay");
    }
    else {
      components.insert("EventNoDelay");
    }
    if (event->isSetPriority()) {
      components.insert("EventPriority");
    }
    if (event->isSetTrigger()) {
      Trigger* trigger = event->getTrigger();
      if (event->isSetDelay() || model->getNumEvents() > 1) {
        if (trigger->isSetPersistent()) {
          if (trigger->getPersistent()) {
            tests.insert("EventIsPersistent [?]");
          }
          else {
            tests.insert("EventIsNotPersistent [?]");
          }
        }
        if (event->isSetUseValuesFromTriggerTime()) {
          if (event->getUseValuesFromTriggerTime()) {
            tests.insert("EventUsesTriggerTimeValues [?]");
          }
          else {
            tests.insert("EventUsesAssignmentTimeValues [?]");
          }
        }
      }
      if (trigger->isSetInitialValue() && trigger->getInitialValue()==false) {
        tests.insert("EventT0Firing [?]");
      }
    }
  }
}

void checkParameters(Model* model, set<string>& components, set<string>& tests,  const map<string, vector<double> >& results)
{
  if (model->getNumParameters() > 0) {
    components.insert("Parameter");
    for (unsigned int p=0; p<model->getNumParameters(); p++) {
      Parameter* param = model->getParameter(p);
      if (!param->isSetValue()) {
        tests.insert("InitialValueReassigned");
      }
      else if (param->isSetId() && initialOverriddenIn(param->getId(), model, results, tests)) {
        tests.insert("InitialValueReassigned");
      }
      if (param->isSetId() && variesIn(param->getId(), model, results)) {
        tests.insert("NonConstantParameter");
      }
    }
  }
}

bool foundInMath(string id, const ASTNode* astn)
{
  if (astn->getType()==AST_NAME && astn->getName() == id) return true;
  for (unsigned long c=0; c<astn->getNumChildren(); c++) {
    if (foundInMath(id, astn->getChild(c))) return true;
  }
  return false;
}

bool foundInMath(string id, Model* model)
{
  for (unsigned long ia=0; ia<model->getNumInitialAssignments(); ia++) {
    const ASTNode* astn = model->getInitialAssignment(ia)->getMath();
    if (foundInMath(id, astn)) return true;
  }
  for (unsigned long r=0;  r<model->getNumRules(); r++) {
    const ASTNode* astn = model->getRule(r)->getMath();
    if (foundInMath(id, astn)) return true;
  }
  for (unsigned long rxn=0; rxn<model->getNumReactions(); rxn++) {
    if (model->getReaction(rxn)->getKineticLaw()->getParameter(id) == NULL) {
      const ASTNode* astn = model->getReaction(rxn)->getKineticLaw()->getMath();
      if (foundInMath(id, astn)) return true;
    }
  }
  for (unsigned long e=0;  e<model->getNumEvents(); e++) {
    Event* event = model->getEvent(e);
    const ASTNode* astn = event->getTrigger()->getMath();
    if (foundInMath(id, astn)) return true;
    if (event->isSetPriority()) {
      astn = event->getPriority()->getMath();
      if (foundInMath(id, astn)) return true;
    }
    if (event->isSetDelay()) {
      astn = event->getDelay()->getMath();
      if (foundInMath(id, astn)) return true;
    }
    for (unsigned long ea=0; ea<event->getNumEventAssignments(); ea++) {
      astn = event->getEventAssignment(ea)->getMath();
      if (foundInMath(id, astn)) return true;
    }
  }
  for (unsigned long c=0;  c<model->getNumConstraints(); c++) {
    const ASTNode* astn = model->getConstraint(c)->getMath();
    if (foundInMath(id, astn)) return true;
  }
  return false;
}


void checkSpeciesRefs(Model* model, ListOfSpeciesReferences* losr, set<string>& components, set<string>& tests,  const map<string, vector<double> >& results)
{
  for (unsigned int rp=0; rp<losr->size(); rp++) {
    SpeciesReference* sr = static_cast<SpeciesReference*>(losr->get(rp));
    if (sr->isSetStoichiometry() && sr->getStoichiometry() != 1) {
      tests.insert("NonUnityStoichiometry");
    }
    if (sr->isSetStoichiometryMath()) {
      tests.insert("NonUnityStoichiometry");
      components.insert("StoichiometryMath");
      if (variesIn(sr->getStoichiometryMath()->getMath(), model, results)) {
        tests.insert("AssignedVariableStoichiometry");
      }
      else {
        tests.insert("AssignedConstantStoichiometry");
      }
    }
    else if (sr->isSetId()) {
      double initialResult = 1;
      if (results.find(sr->getId()) != results.end()) {
        tests.insert("SpeciesReferenceOutput");
      }
      if (variesIn(sr->getId(), model, results)) {
        tests.insert("AssignedVariableStoichiometry");
        tests.insert("NonUnityStoichiometry");
      }
      else if (initialOverriddenIn(sr->getId(), model, results, tests)) {
        tests.insert("AssignedConstantStoichiometry");
        if (getInitialResultFor(sr->getId(), results, initialResult)) {
          if (initialResult != 1) {
            tests.insert("NonUnityStoichiometry");
          }
        }
        else {
          //Don't know what the actual initial result is, so we'll assume it's not 1.0.
          tests.insert("NonUnityStoichiometry");
        }
      }
      if (initialOverriddenIn(sr->getId(), model, results, tests)) {
        tests.insert("InitialValueReassigned");
      }
      if (foundInMath(sr->getId(), model)) {
        tests.insert("SpeciesReferenceInMath");
      }
    }
  }
}

void checkReactions(Model* model, set<string>& components, set<string>& tests,  const map<string, vector<double> >& results)
{
  if (model->getNumReactions() > 0) {
    components.insert("Reaction");
    for (unsigned int r=0; r<model->getNumReactions(); r++) {
      Reaction* rxn = model->getReaction(r);
      if (rxn->isSetFast() && rxn->getFast()) {
        tests.insert("FastReaction");
      }
      if (rxn->isSetReversible() && rxn->getReversible()) {
        tests.insert("ReversibleReaction [?]");
      }
      ListOfSpeciesReferences* reactants = rxn->getListOfReactants();
      checkSpeciesRefs(model, reactants, components, tests, results);
      ListOfSpeciesReferences* products = rxn->getListOfProducts();
      checkSpeciesRefs(model, products, components, tests, results);
      if (rxn->isSetKineticLaw()) {
        KineticLaw* kl = rxn->getKineticLaw();
        if (kl->getNumParameters() > 0) {
          tests.insert("LocalParameters");
        }
      }
    }
  }
}

void checkSpecies(Model* model, set<string>& components, set<string>& tests,  const map<string, vector<double> >& results)
{
  //Must call this after 'checkCompartments' because we look in 'tests' for 'NonUnityCompartment'.
  if (model->getNumSpecies() > 0) {
    components.insert("Species");
    tests.insert("Amount||Concentration");
    for (unsigned int s=0; s<model->getNumSpecies(); s++) {
      Species* species = model->getSpecies(s);
      if (species->isSetBoundaryCondition() && species->getBoundaryCondition()) {
        tests.insert("BoundaryCondition");
      }
      if (species->getConstant()) {
        tests.insert("ConstantSpecies");
      }
      if (species->isSetConversionFactor()) {
        tests.insert("ConversionFactors");
      }
      if (species->isSetHasOnlySubstanceUnits() && species->getHasOnlySubstanceUnits()) {
        tests.insert("HasOnlySubstanceUnits");
      }
      if (!species->isSetInitialAmount() && !species->isSetInitialConcentration()) {
        tests.insert("InitialValueReassigned");
      }
      else if (species->isSetId() && initialOverriddenIn(species->getId(), model, results, tests)) {
        tests.insert("InitialValueReassigned");
      }
    }
  }
}

#ifdef USE_COMP
void checkComp(CompSBMLDocumentPlugin* compdoc, set<string>& components, set<string>& tests,  const map<string, vector<double> >& results)
{
  //if (compdoc->getRequired()) {
  //  components.insert("comp:Required");
  //}
  //else {
  //  components.insert("comp:NotRequired");
  //}

  SBMLDocument* doc = compdoc->getSBMLDocument();
  List* allElements = doc->getAllElements();
  for (unsigned int e=0; e<allElements->getSize(); e++) {
    SBase* element = static_cast<SBase*>(allElements->get(e));
    ReplacedElement* re;
    Submodel* submod;
    switch(element->getTypeCode()) {
    case SBML_COMP_SUBMODEL:
      components.insert("comp:Submodel");
      submod = static_cast<Submodel*>(element);
      if (submod->isSetExtentConversionFactor()) {
        tests.insert("comp:ExtentConversionFactor");
      }
      if (submod->isSetTimeConversionFactor()) {
        tests.insert("comp:TimeConversionFactor");
      }
      break;
    case SBML_COMP_MODELDEFINITION:
      components.insert("comp:ModelDefinition");
      break;
    case SBML_COMP_EXTERNALMODELDEFINITION:
      components.insert("comp:ExternalModelDefinition");
      break;
    case SBML_COMP_SBASEREF:
      components.insert("comp:SBaseRef");
      break;
    case SBML_COMP_DELETION:
      components.insert("comp:Deletion");
      break;
    case SBML_COMP_REPLACEDELEMENT:
      components.insert("comp:ReplacedElement");
      re = static_cast<ReplacedElement*>(element);
      if (re->isSetConversionFactor()) {
        tests.insert("comp:ConversionFactor");
      }
      break;
    case SBML_COMP_REPLACEDBY:
      components.insert("comp:ReplacedBy");
      break;
    case SBML_COMP_PORT:
      components.insert("comp:Port");
      break;
    default:
      break;
    }
  }
  for (map<string, vector<double> >::const_iterator result=results.begin(); 
    result != results.end(); result++) {
      string id = result->first;
      if (id.find("__") != string::npos) {
        //It is probably a submodel element, renamed
        tests.insert("comp:SubmodelOutput");
      }
  }
}
#endif