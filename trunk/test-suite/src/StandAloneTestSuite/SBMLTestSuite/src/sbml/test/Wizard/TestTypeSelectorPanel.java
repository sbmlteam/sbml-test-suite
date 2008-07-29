// @file    TestTypeSelectorPanel.java
// @brief   TestTypeSelectorPanel class for SBML Standalone application
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
// Generate test type panel for the new test wizard.
//
package sbml.test.Wizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

/**
 * TestTypeSelectorPanel generates the test type selection panel for the new test wizard.
 * @author Kimberly Begley
 * @version 2.0
 */
public class TestTypeSelectorPanel extends WizardPanel implements ItemListener {

    JCheckBox timeCourseCheckBox;
    private CreateTestWizard createTestWizard;
    public HashMap<String, Object> selections = new HashMap <String, Object>();
    /**
     * TestTypeSelectorPanel has one constructor that initializes the components for the TestTypeSelectorPanel
     * @param createTestWizard the createTestWizard instance associated with the wizard.
     */
    public TestTypeSelectorPanel(CreateTestWizard createTestWizard) {
        this.createTestWizard = createTestWizard;
        initComponents();
    }
    /**
     * initComponents initializes the components for the Test Type Selector Panel in the new test wizard.
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(getQualifiedName());
        nameLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        topPanel.add(nameLabel, BorderLayout.PAGE_START);
        topPanel.add(new JSeparator(), BorderLayout.PAGE_END);
        add(topPanel, BorderLayout.PAGE_START);

        JPanel contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        ButtonGroup buttonGroup = new ButtonGroup();


        JPanel contentPanel1 = new JPanel(new GridLayout(0, 2, 80, 0));

        timeCourseCheckBox = new JCheckBox("TimeCourse");
        timeCourseCheckBox.setSelected(true);
        
        timeCourseCheckBox.addItemListener(this);
        
        contentPanel1.add(timeCourseCheckBox);
        JPanel contentPanel2 = new JPanel(new BorderLayout());
        contentPanel.add(contentPanel2, BorderLayout.NORTH);
        contentPanel2.add(contentPanel1, BorderLayout.WEST);
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        messagePanel.add(new JLabel("Please select the desired Test Class:"), BorderLayout.NORTH);
        contentPanel2.add(messagePanel, BorderLayout.NORTH);
    }
    /**
     * itemStateChanged gets called as a result of the listener on the test type checkbox at the moment there is only one test type to choose from
     * so if the user deselects the test type it automatically selects itself again.
     * @param ie the itemEvent associated with the selection/deselection of the checkbox.
     */
    public void itemStateChanged (ItemEvent ie) {
      selections =  createTestWizard.getSelections();
      Object source = ie.getItemSelectable();
      int state = ie.getStateChange();
      
       if (source == timeCourseCheckBox){
          if(state == ItemEvent.SELECTED){
              updateSelections("timecourse",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              //System.out.println("You must select timecourse at this point");
              timeCourseCheckBox.setSelected(true);
              updateSelections("timecourse",1);
              createTestWizard.setSelections(selections);
              
              // add these lines when there are more tests types to choose from
              //updateSelections("timecourse",0);
              // createTestWizard.setSelections(selections);
          }
      }
    }
    /**
     * getQualifiedName gets the name of the test type panel
     * @return returns text "Select Test Type"
     */
    public String getQualifiedName() {
        return "Select Test Type";
    }
    /**
     * getIdentifier gets the identifier for the test type panel.
     *
     * @return returns the text "TESTTYPE"
     */
    public String getIdentifier() {
        return "TESTTYPE";
    }
    /**
     * getPrevious gets the previous panel in the wizard
     * @return returns the text "LEVELSELECTOR"
     */
    public String getPrevious() {
        return "LEVELSELECTOR";
    }
    /**
     * getNext gets the next panel in the wizard
     * @return returns the text "CTSELECTOR"
     */
    public String getNext() {
        return "CTSELECTOR";
    }
    /**
     * isLast indicates if the test type panel is the last in the sequence for the new test wizard.
     * @return returns false.
     */
    public boolean isLast() {
        return false;
    }
    /**
     * isFirst indicates if the test type panel is the first in the sequence for the new text wizard
     * @return returns false
     */
    public boolean isFirst() {
        return false;
    }
    /**
     * mayFinish indicates if the test type panel may activate the finish button in the new test wizard
     * @return returns false
     */
    public boolean mayFinish() {
        return false;
    }
    /**
     * not in use 
     * @param selections
     */
    public void validateSelections(HashMap<String, Object> selections) {
   //     throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * updateSelections updates the fields in the selections hashmap as needed.
     * @param field the field to update.
     * @param value the value to update the field to.
     */
    private void updateSelections(String field, Integer value) {
        selections.put(field, value);
    }
}
