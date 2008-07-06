

package sbml.test;

// @file    PassedTestCaseListModel.java
// @brief   PassedTestCaseListModel class for SBML Standalone application
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
// Generate a list model of Passed cases.
//

import javax.swing.*;
import java.util.Vector;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class PassedTestCaseListModel extends AbstractListModel implements ListDataListener {

    Vector<TestResultDetails> passedData;
  

    PassedTestCaseListModel() {
       
        passedData = new Vector<TestResultDetails>();

    }

    public void addElement(TestResultDetails o) {
        passedData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }
    public TestResultDetails remove(int index) {
        TestResultDetails o = passedData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }
    
    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            passedData.removeAllElements();

        }
    }
    public int getSize() {
        return passedData.size();
    }

    public Object getElementAt(int index) {
        return (passedData.get(index)).getTestname();
    }

    public TestResultDetails getRawElementAt(int index) {
        return passedData.get(index);
    }

    public void intervalAdded(ListDataEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void intervalRemoved(ListDataEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void contentsChanged(ListDataEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

