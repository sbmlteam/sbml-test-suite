// @file    WizardPanel.java
// @brief   WizardPanel class for SBML Standalone application
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
// Generate a new wizard panel for the new test wizard.
//

package sbml.test.Wizard;

import java.beans.PropertyChangeSupport;
import javax.swing.JPanel;


public abstract class WizardPanel extends JPanel implements WizardPanelInterface {
    
    protected final PropertyChangeSupport propertyChangeSupport;
    
    public WizardPanel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    
    
}
