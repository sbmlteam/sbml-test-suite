// @file    TestCaseListModel.java
// @brief   TestCaseListModel class for SBML Standalone application
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
// Generate a list model and it's methods.
//

package sbml.test;

import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class TestCaseListModel extends AbstractListModel implements ListDataListener {

    Vector<TestResultDetails> rawData;

    public TestCaseListModel() {
        rawData = new Vector<TestResultDetails>();
    }

    public TestResultDetails remove(int index) {
        TestResultDetails o = rawData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }

    public void addElement(TestResultDetails o) {
        rawData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }

    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            rawData.removeAllElements();

        }
    }

    public int getSize() {
        return rawData.size();
    }

    public Object getElementAt(int index) {
        return rawData.get(index).getTestname();
    }

    TestResultDetails getRawElementAt(int index) {
        return rawData.get(index);
    }

    public void intervalAdded(ListDataEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void intervalRemoved(ListDataEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void contentsChanged(ListDataEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }
}
