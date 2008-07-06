package sbml.test;
// @file    FailedTestCaseListModel.java
// @brief   FailedTestCaseListModel class for SBML Standalone application
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
// Generate a list model of Failed cases.
//




import javax.swing.*;
import java.util.Vector;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * This class provides a list model for the list of failed test cases. It is referenced in the 
 * list view of the SBML Test Suite.
 * 
 * @author Kimberly Begley
 * @version 1.0
 */

public class FailedTestCaseListModel extends AbstractListModel implements ListDataListener{
    
    
    Vector<TestResultDetails> failedData;
   
    /**
     * Initializes a new ListModel instance with an empty vector list of failed tests.
     * 
     * @param output New failed list list model.
     * @param input  None
     */
    
    FailedTestCaseListModel() {
       
        failedData = new Vector<TestResultDetails>();
        

    }
    
    // FailedTestListModel has 9 methods
    /**
     * Adds a new data structure of result details to the failed list.
     * @param o Failed data structure to be added to the list of failed results.
     */
    public void addElement(TestResultDetails o) {
        failedData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }
    /**
     * 
     * @param index
     * @return
     */
    public TestResultDetails remove(int index) {
        TestResultDetails o = failedData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }
    
    /**
     * 
     */
    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            failedData.removeAllElements();

        }
    }
    /**
     * 
     * @return
     */
    public int getSize() {
        return failedData.size();
    }
    /**
     * 
     * @param index
     * @return
     */
    public Object getElementAt(int index) {
        return (failedData.get(index)).getTestname();
    }
    /**
     * 
     * @param index
     * @return
     */
    public TestResultDetails getRawElementAt(int index) {
        return failedData.get(index);
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
