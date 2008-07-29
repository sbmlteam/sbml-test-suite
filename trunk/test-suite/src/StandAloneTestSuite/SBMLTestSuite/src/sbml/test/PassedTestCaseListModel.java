

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

/**
 * PassedTestCaseListModel is a class that creates a list model of the passed test cases.
 * @author Kimberly Begley
 * @version 2.0
 */
public class PassedTestCaseListModel extends AbstractListModel implements ListDataListener {

    Vector<TestResultDetails> passedData;
  
/**
 * PassedTestCaseListModel has one constructor that creates a new vector of TestResultDetails for the test case that have passed.
 */
    PassedTestCaseListModel() {
       
        passedData = new Vector<TestResultDetails>();

    }
/**
 * addElement adds a TestResultDetails object to the list anf fires an event to record this.
 * @param o the TestResultDetails object to add to the list.
 */
    public void addElement(TestResultDetails o) {
        passedData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }
    /**
     * remove removes a TestResultObject from the passed test case list model at the specified index.
     * @param index Index of the item to remove
     * @return returns the TestResultDetails object that was removed.
     */
    public TestResultDetails remove(int index) {
        TestResultDetails o = passedData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }
    /**
     * removeAllElements removes all elements from the PassedTestCaseListModel list.
     */
    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            passedData.removeAllElements();

        }
    }
    /**
     * getSize returns the size of the passed result list.
     * @return returns an integer value of the size of the PassedTestCaseListModel.
     */
    public int getSize() {
        return passedData.size();
    }
    /**
     * getElementAt returns the string value of the testname of the object at the specified index.
     * @param index the index of the list 
     * @return returns a string value of the test name.
     */
    public Object getElementAt(int index) {
        return (passedData.get(index)).getTestname();
    }
    /**
     * getRawElementAt returns the TestResultDetails object at the selected index.
     * @param index the index for the item in the list.
     * @return returns a TestResultDetails object of the specified index value.
     */
    public TestResultDetails getRawElementAt(int index) {
        return passedData.get(index);
    }
    /**
     * 
     * @param e
     */
    public void intervalAdded(ListDataEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * 
     * @param e
     */
    public void intervalRemoved(ListDataEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * 
     * @param e
     */
    public void contentsChanged(ListDataEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

