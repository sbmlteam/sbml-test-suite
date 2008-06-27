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


public class TestTypeSelectorPanel extends WizardPanel implements ItemListener {

    JCheckBox timeCourseCheckBox;
    private CreateTestWizard createTestWizard;
    public HashMap<String, Object> selections = new HashMap <String, Object>();

    public TestTypeSelectorPanel(CreateTestWizard createTestWizard) {
        this.createTestWizard = createTestWizard;
        initComponents();
    }

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
              System.out.println("You must select timecourse at this point");
              timeCourseCheckBox.setSelected(true);
              updateSelections("timecourse",1);
              createTestWizard.setSelections(selections);
              
              // add these lines when there are more tests types to choose from
              //updateSelections("timecourse",0);
              // createTestWizard.setSelections(selections);
          }
      }
    }

    public String getQualifiedName() {
        return "Select Test Type";
    }

    public String getIdentifier() {
        return "TESTTYPE";
    }

    public String getPrevious() {
        return "LEVELSELECTOR";
    }

    public String getNext() {
        return "CTSELECTOR";
    }

    public boolean isLast() {
        return false;
    }

    public boolean isFirst() {
        return false;
    }

    public boolean mayFinish() {
        return false;
    }

    public void validateSelections(HashMap<String, Object> selections) {
   //     throw new UnsupportedOperationException("Not supported yet.");
    }

    private void updateSelections(String field, Integer value) {
        selections.put(field, value);
    }
}
