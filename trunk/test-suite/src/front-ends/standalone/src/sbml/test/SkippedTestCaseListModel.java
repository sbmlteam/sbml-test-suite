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

/**
 * SkippedTestCaseListModel is a class that creates a list model for the skipped test cases.
 * @author Kimberly Begley
 * @version 2.0
 */

public class SkippedTestCaseListModel extends AbstractListModel implements ListDataListener{
    
    
    Vector<TestResultDetails> skippedData;
    
    /**
     * SkippedTestCaseListModel has one constructor that creates a vector of skipped test cases.
     */
    SkippedTestCaseListModel() {
        
        skippedData = new Vector<TestResultDetails>();        

    }
    /**
     * addElement - adds an element to the skipped list and fires an interval change.
     * @param o the skipped test case to be added
     */
     public void addElement(TestResultDetails o) {
        skippedData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }
     /**
      * remove - removes a test case from the skpped test case list model.
      * @param index index of the item to be removed from the list
      * @return returns the removed item.
      */
    public TestResultDetails remove(int index) {
        TestResultDetails o = skippedData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }
    /**
     * removeAllElements removes all elements from the skipped test case list model. 
     */
    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            skippedData.removeAllElements();

        }
    }
    /**
     * Gets the size of the skipped list
     * @return the size of the list
     */
    public int getSize() {
        return skippedData.size();
    }
    /**
     * getElementAt gets the test name of the test case in the list a the specified index
     * @param index index of the test case to retrieve
     * @return the testname of the oject in the list
     */
    public Object getElementAt(int index) {
        return (skippedData.get(index)).getTestname();
    }
    /**
     * getRawElementAt gets the whole TestResultDetails object from the list
     * @param index the index of the list
     * @return the TestResultDetails object to be returned
     */
    public TestResultDetails getRawElementAt(int index) {
        return skippedData.get(index);
    }
    
    public boolean isEmpty() {
        if(skippedData.size() == 0) { return true; }
        else return false;
    }
    /**
     * 
     * @param e
     */
    public void intervalAdded(ListDataEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void intervalRemoved(ListDataEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        
    }

    public void contentsChanged(ListDataEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}

