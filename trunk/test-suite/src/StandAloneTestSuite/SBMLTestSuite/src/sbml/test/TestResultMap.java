// @file    TestResultMap.java
// @brief   TestResultMap class for SBML Standalone application
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
// Test result map class and methods.
//

package sbml.test;

import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * TestResultMap class is the class repsonsible for the display of the map view option in the stand alone SBML Test Suite.
 * @author Kimberly Begley
 * @version 2.0
 */
public class TestResultMap extends JPanel implements ListDataListener, ItemSelectable  {
	
	Vector<JPanel> grids;
	TestCaseListModel testCaseListModel;
        int selectedindex;
        
	/**
         * TestResultMap constructor creates a testcaselistmodel and adds a listener to it for mouse clicks, it creates the grid and vector initially at zero elements.
         */
	TestResultMap(){
            this.testCaseListModel = TestRunnerView.testCaseListModel;
            testCaseListModel.addListDataListener(this);
		setLayout(new GridLayout(0,70,1,1));
		setBackground(Color.gray);
		setBorder(BorderFactory.createLineBorder(Color.black,1));
		grids = new Vector<JPanel>();
		
	}
	

        /**
         * resetDisplay removes all elements from the grid
         */
	public void resetDisplay() {
            if(grids.size() > 0){
                grids.removeAllElements();
            }
	}
        /**
         * intervalAdded listens for the addition of an object to the list and calls the addMapElement function
         * @param e the list data event when an item is added to the list
         */
    public void intervalAdded(ListDataEvent e) {
        for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
            this.addMapElement(testCaseListModel.getRawElementAt(i).getResult(), i);
            
        }

    }
/**
 * intervalRemoved listens for the removal of an object from the list and calls the removeMapElement function
 * @param e the list data event when an item is removed from the list
 */
    public void intervalRemoved(ListDataEvent e) {
        for (int i = e.getIndex1(); i >= e.getIndex0(); i--) {
            System.out.println("removing an element");
            this.removeMapElement(i);

        }
    }
    /**
     * contentsChanged - currently has no function
     * @param e
     */
    public void contentsChanged(ListDataEvent e) {
       
    }
    /**
     * addMapElement function adds a new item to the grid
     * @param result integer value of number of failed tests - it determines what colour of square to add to the grid - red=failed, green=passed, blue=skipped
     * @param index
     */
    private void addMapElement(int result, int index) {
    
        grids.add(new JPanel());
        add(grids.get(index));
        grids.get(index).setToolTipText("Test case " + (testCaseListModel.getRawElementAt(index).getTestname()));
        grids.get(index).addMouseListener(new FieldListener(index));
        
        if (result == 0) {
            //set it green
            grids.get(index).setBackground(Color.green);
        }
        else if (result > 0) {
            // set it red
            grids.get(index).setBackground(Color.red);
        }
        else if (result == -1) {
            // set it black
            grids.get(index).setBackground(Color.blue);
        } 
        else {
            grids.get(index).setBackground(Color.GRAY);
        }

    }

    /**
     * getMapSize gets the size of the grid
     * @return an integer indicating size of the grid
     */
    private int getMapSize() {
        return grids.size();
    }
    /**
     * removeMapElement
     * @param index is the index at which to remove the element
     */
    private void removeMapElement(int index) {
        remove(grids.get(index));
        grids.remove(index);
    }
    
    /**
     * FieldListener - listener class for mouse clicks on the grid
     */
    public class FieldListener implements MouseListener {
        int index;
        /**
         * FieldListener constructor
         * @param index integer value of the index of the item that has been selected
         */
        FieldListener (int index) {
            this.index = index;
        }
        /**
         * mouseClicked method to be invoked when an item is clicked on in the grid
         * @param e MouseEvent fired when the mouse clicks on an item
         */
        public void mouseClicked(MouseEvent e) {
            selectedindex = index;
            fireItemEvent(index,true);
        }
        /**
         * mousePressed - not in use
         * @param e
         */
        public void mousePressed(MouseEvent e) {
        }
        /**
         * mouseReleased - not in use
         * @param e
         */
        public void mouseReleased(MouseEvent e) {
        }
        /**
         * mouseEntered - not in use
         * @param e
         */
        public void mouseEntered(MouseEvent e) {
        }
        /**
         * mouseExited - not in use
         * @param e
         */
        public void mouseExited(MouseEvent e) {
        }
        
    }
    /**
     * getSelectedObjects gets a list of selected objects
     * @return an array of selected objects
     */
    public Object[] getSelectedObjects() {
        Object[] returnvalue = new Object[1];
        returnvalue[1] = testCaseListModel.getRawElementAt(selectedindex);
        return returnvalue;
    }
    /**
     * getSelectedObject gets an element of the list
     * @return returns a TestResultDetails object of the selected index
     */
    public Object getSelectedObject() {
        return testCaseListModel.getRawElementAt(selectedindex);
    }
    /**
     * addItemListener - adds an item listener to the list
     * @param l the itemlistener to add
     */
    public void addItemListener(ItemListener l) {
        listenerList.add(ItemListener.class, l);
    }
    /**
     * removeItemListener - removes an item listener from the list
     * @param l the itemlistener to remove
     */
    public void removeItemListener(ItemListener l) {
        listenerList.remove(ItemListener.class, l);
    }
    /**
     * fireItemEvent - fires an item event to listeners
     * @param item the index of hte item that was selected
     * @param sel the true/false value of being selected or not
     */
    void fireItemEvent(Object item, boolean sel) {
        
        ItemEvent evt = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
                item, sel ? ItemEvent.SELECTED : ItemEvent.DESELECTED);

        // Get list of listeners
        Object[] listeners = listenerList.getListenerList();
        
        // Send event to all listeners
        for (int i = 0; i < listeners.length; i += 2) {
            
            if (listeners[i] == ItemListener.class) {
                ((ItemListener) listeners[i + 1]).itemStateChanged(evt);
            }
        }
    }
}
