// @file    TestCaseUpdater.java
// @brief   TestCaseUpdater class for SBML Standalone application
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
// Classes and methods for updating the test cases.
//

package sbml.test;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;



public class TestCaseUpdater extends JDialog {
    JProgressBar progressBar;
    JButton cancelButton;
    
    public TestCaseUpdater() {
        setLayout(new BorderLayout());
        //setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
    }
    
}
