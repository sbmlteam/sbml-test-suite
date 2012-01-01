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
#include <set>
#include <sstream>

#include <sbml/SBMLTypes.h>

using namespace std;
LIBSBML_CPP_NAMESPACE_USE

bool hasActualErrors(SBMLDocument* document)
{
  for (unsigned int e=0; e<document->getNumErrors(); e++) {
    if (document->getError(e)->getSeverity() >= LIBSBML_SEV_ERROR) return true;
  }
  return false;
}

bool variesIn(string id, ListOfSpeciesReferences* srs)
{
  for (unsigned long sr=0; sr<srs->size(); sr++) {
    SpeciesReference* spref = static_cast<SpeciesReference*>(srs->get(sr));
    if (spref->getSpecies() == id) return true;
  }
  return false;
}

bool variesIn(string id, Model* model)
{
  if (model->getRule(id)) return true;
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
    if (variesIn(id, rxn->getListOfReactants())) return true;
    if (variesIn(id, rxn->getListOfProducts())) return true;
  }
  return false;
}

bool initialOverriddenIn(string id, Model* model)
{
  if (model->getInitialAssignment(id) != NULL) return true;
  Rule* rule = model->getRule(id);
  if (rule==NULL) return false;
  if (rule->getTypeCode()==SBML_ASSIGNMENT_RULE) return true;
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

void checkCompartments(Model* model, set<string>& components, set<string>& tests)
{
  if (model->getNumCompartments() > 0) {
    components.insert("Compartment");
    if (model->getNumCompartments() > 1) {
      tests.insert("MultiCompartment");
    }
    for (unsigned int c=0; c<model->getNumCompartments(); c++) {
      Compartment* compartment = model->getCompartment(c);
      if (compartment->isSetId() && variesIn(compartment->getId(), model)) {
        tests.insert("NonConstantCompartment");
        tests.insert("NonUnityCompartment");
      }
      else if (compartment->isSetVolume()) {
        double volume = compartment->getVolume();
        if (volume != 1.0) {
          tests.insert("NonUnityCompartment");
        }
      }
      if (compartment->isSetVolume() && compartment->isSetId() && initialOverriddenIn(compartment->getId(), model)) {
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

void checkParameters(Model* model, set<string>& components, set<string>& tests)
{
  if (model->getNumParameters() > 0) {
    components.insert("Parameter");
    for (unsigned int p=0; p<model->getNumParameters(); p++) {
      Parameter* param = model->getParameter(p);
      if (param->isSetValue() && param->isSetId() && 
          initialOverriddenIn(param->getId(), model)) {
        tests.insert("InitialValueReassigned");
      }
      if (param->isSetId() && variesIn(param->getId(), model)) {
        tests.insert("NonConstantParameter");
      }
    }
  }
}

void checkSpeciesRefs(Model* model, ListOfSpeciesReferences* losr, set<string>& tests)
{
  for (unsigned int rp=0; rp<losr->size(); rp++) {
    SpeciesReference* sr = static_cast<SpeciesReference*>(losr->get(rp));
    if (sr->isSetStoichiometry() && sr->getStoichiometry() != 1) {
      tests.insert("NonUnityStoichiometry");
    }
    if (sr->isSetStoichiometryMath()) {
      tests.insert("NonUnityStoichiometry");
      if (sr->getConstant()) {
        tests.insert("AssignedConstantStoichiometry");
      }
      else {
        tests.insert("AssignedVariableStoichiometry");
      }
    }
    else if (sr->isSetId()) {
      if (variesIn(sr->getId(), model)) {
        tests.insert("AssignedVariableStoichiometry");
        tests.insert("NonUnityStoichiometry");
      }
      else if (initialOverriddenIn(sr->getId(), model)) {
        tests.insert("AssignedConstantStoichiometry");
        tests.insert("NonUnityStoichiometry");
      }
    }
  }
}

void checkReactions(Model* model, set<string>& components, set<string>& tests)
{
  if (model->getNumReactions() > 0) {
    components.insert("Reaction");
    for (unsigned int r=0; r<model->getNumReactions(); r++) {
      Reaction* rxn = model->getReaction(r);
      if (rxn->isSetFast() && rxn->getFast()) {
        tests.insert("FastReaction");
      }
      ListOfSpeciesReferences* reactants = rxn->getListOfReactants();
      checkSpeciesRefs(model, reactants, tests);
      ListOfSpeciesReferences* products = rxn->getListOfProducts();
      checkSpeciesRefs(model, products, tests);
      if (rxn->isSetKineticLaw()) {
        KineticLaw* kl = rxn->getKineticLaw();
        if (kl->getNumLocalParameters() > 0) {
          tests.insert("LocalParameters");
        }
      }
    }
  }
}

void checkSpecies(Model* model, set<string>& components, set<string>& tests)
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
      if (!variesIn(species->getId(), model)) {
        tests.insert("ConstantSpecies");
      }
      if (species->isSetConversionFactor()) {
        tests.insert("ConversionFactors");
      }
      if (species->isSetHasOnlySubstanceUnits() && species->getHasOnlySubstanceUnits()
          && tests.find("NonUnityCompartment") != tests.end()) {
        tests.insert("HasOnlySubstanceUnits");
      }
      if ((species->isSetInitialAmount() || species->isSetInitialConcentration()) &&
          species->isSetId() && initialOverriddenIn(species->getId(), model)) {
        tests.insert("InitialValueReassigned");
      }
    }
  }
}

