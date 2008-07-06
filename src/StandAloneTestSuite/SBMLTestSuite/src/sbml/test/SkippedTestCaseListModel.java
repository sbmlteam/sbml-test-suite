// @file    SkippedTestCaseListModel.java
// @brief   SkippedTestCaseListModel class for SBML Standalone application
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
// Generate a list model of Skipped cases.
//

package sbml.test;

import javax.swing.*;
import java.util.Vector;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class SkippedTestCaseListModel extends AbstractListModel implements ListDataListener{
    
    
    Vector<TestResultDetails> skippedData;
    
    
    SkippedTestCaseListModel() {
        
        skippedData = new Vector<TestResultDetails>();        

    }
    
     public void addElement(TestResultDetails o) {
        skippedData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }
    public TestResultDetails remove(int index) {
        TestResultDetails o = skippedData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }
    
    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            skippedData.removeAllElements();

        }
    }

    public int getSize() {
        return skippedData.size();
    }

    public Object getElementAt(int index) {
        return (skippedData.get(index)).getTestname();
    }
    
    public TestResultDetails getRawElementAt(int index) {
        return skippedData.get(index);
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

