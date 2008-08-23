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

/**
 * This class creates a test case list model for use with the map view of the stand alone application.
 * @author Kimberly Begley
 * @version 2.0
 */

public class TestCaseListModel extends AbstractListModel implements ListDataListener {

    Vector<TestResultDetails> rawData;
    /**
     * TestCaseListModel has one constructor that creates a new Vector of TestResultDetails that have been processed.
     */
    public TestCaseListModel() {
        rawData = new Vector<TestResultDetails>();
    }
    /**
     * remove - removes a case from the processed case list at the specified index
     * @param index the index of the list to remove
     * @return returns the removed case object
     */
    public TestResultDetails remove(int index) {
        TestResultDetails o = rawData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }
    /**
     * addElement - adds a case ojbect to the list 
     * @param o the TestResultDetails object to add to the list
     */
    public void addElement(TestResultDetails o) {
        rawData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }
    /**
     * removeAllElements - removes all elements from the list
     */
    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            rawData.removeAllElements();

        }
    }
    /**
     * getSize - gets the size of the case list
     * @return returns an integer value of the size of the vector
     */
    public int getSize() {
        return rawData.size();
    }
    /**
     * getElementAt - gets the testname of the case at the specified index      
     * @param index the index at which to retrieve the testname
     * @return returns a String containing the name of the test case
     */
    public Object getElementAt(int index) {
        return rawData.get(index).getTestname();
    }
    /**
     * getRawElementAt - returns the TestResultDetails object in the list at the specified index
     * @param index the index for which the case object is requested
     * @return returns the TestResultDetails object at the specified index
     */
    TestResultDetails getRawElementAt(int index) {
        return rawData.get(index);
    }
    
    public boolean isEmpty() {
        if(rawData.size() ==0){ return true; }
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
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void contentsChanged(ListDataEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
        
    }
}
