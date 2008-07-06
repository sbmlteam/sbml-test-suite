// @file    WizardPanelInterface.java
// @brief   WizardPanelInterface class for SBML Standalone application
// @author  Kimberly Begley
// 

//
//<!---------------------------------------------------------------------------
// This file is part of the SBML Test Suite.  Please visit http://sbml.org for
// more information about SBML, and the latest version of the SBML Test Suite.
// 
// Copyright 2008      California Institute of Technology.
// Copyright 2004-2007 California Institute of Technology (USA) and
//                     University of Hertfordshire (UK).
// 
// The SBML Test Suite is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License as
// published by the Free Software Foundation.  A copy of the license
// agreement is provided in the file named "LICENSE.txt" included with
// this software distribution and also available at
// http://sbml.org/Software/SBML_Test_Suite/license.html
//------------------------------------------------------------------------- -->
// Generate the wizard panel methods for the new test wizard.
//

package sbml.test.Wizard;

import java.util.HashMap;


public interface WizardPanelInterface {
 public void validateSelections(HashMap<String, Object> selections);
    
 public String getQualifiedName();
 
 public String getIdentifier();
 
 public String getPrevious();
 
 public String getNext();
 
 public boolean isLast();
 
 public boolean isFirst();
 
 public boolean mayFinish();
}
