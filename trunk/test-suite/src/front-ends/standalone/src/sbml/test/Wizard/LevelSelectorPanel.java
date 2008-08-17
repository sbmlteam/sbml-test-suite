// @file    LevelSelectorPanel.java
// @brief   LevelSelectorPanel class for SBML Standalone application
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
// Generates the panel for level selection in the wizard.
//
package sbml.test.Wizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSeparator;

/**
 * LevelSelectorPanel generates the panel for level selection in the test suite wizard.
 * @author Kimberly Begley
 * @version 2.0
 */
public class LevelSelectorPanel extends WizardPanel implements ItemListener {

    private JRadioButton L1V2radioButton,  L2V1radioButton,  L2V2radioButton,  L2V3radioButton;
    //ButtonGroup buttonGroup = new ButtonGroup();
    String level;
    private CreateTestWizard createTestWizard;
    private HashMap<String, Object> selections = new HashMap<String, Object>();
    /**
     * LevelSelectorPanel has one constructor that initializes the components for the panel.
     * @param createTestWizard is the createTestWizard instance passed as input to the constructor.
     */
    public LevelSelectorPanel(CreateTestWizard createTestWizard) {
        this.createTestWizard = createTestWizard;
        initComponents();

    }
    /**
     * initComponents initializes the compoenents for the level selector panel.
     */
    private void initComponents() {
        selections = createTestWizard.getSelections();
        
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(getQualifiedName());
        nameLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        topPanel.add(nameLabel, BorderLayout.PAGE_START);
        topPanel.add(new JSeparator(), BorderLayout.PAGE_END);
        add(topPanel, BorderLayout.PAGE_START);

        JPanel contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        L1V2radioButton = new JRadioButton("Version 2");
        L1V2radioButton.setActionCommand("L1V2radioButton");
        L2V1radioButton = new JRadioButton("Version 1");
        L2V1radioButton.setActionCommand("L2V1radioButton");
        L2V2radioButton = new JRadioButton("Version 2");
        L2V2radioButton.setActionCommand("L2V2radioButton");
        L2V3radioButton = new JRadioButton("Version 3");
        L2V3radioButton.setActionCommand("L2V3radioButton");
        L2V3radioButton.setSelected(true);
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(L1V2radioButton);
        buttonGroup.add(L2V1radioButton);
        buttonGroup.add(L2V2radioButton);
        buttonGroup.add(L2V3radioButton);
        
        L1V2radioButton.addItemListener(this);
        L2V1radioButton.addItemListener(this);
        L2V2radioButton.addItemListener(this);
        L2V3radioButton.addItemListener(this);
        
        JPanel contentPanel1 = new JPanel(new GridLayout(0,2,80,10)); 
        contentPanel1.add(new JLabel("Level 1"));
        contentPanel1.add(new JLabel("Level 2"));
        contentPanel1.add(L1V2radioButton);        
        contentPanel1.add(L2V1radioButton);
        contentPanel1.add(new JLabel());
        contentPanel1.add(L2V2radioButton);
        contentPanel1.add(new JLabel());
        contentPanel1.add(L2V3radioButton);
        JPanel contentPanel2 = new JPanel(new BorderLayout()); 
        contentPanel.add(contentPanel2, BorderLayout.NORTH);
        contentPanel2.add(contentPanel1, BorderLayout.WEST);
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        messagePanel.add(new JLabel("Please select the SBML Level and Version " +
                "for the tests:"), BorderLayout.NORTH);
        contentPanel2.add(messagePanel, BorderLayout.NORTH);       
    }
    

    public void itemStateChanged(ItemEvent e){
     int state = e.getStateChange();
     if(state == ItemEvent.SELECTED) {
      if (e.getSource() == L1V2radioButton){
          // change the selections hash
          updateSelections("L1V2radiobutton",1);
          updateSelections("L2V1radiobutton",0);
          updateSelections("L2V2radiobutton",0);
          updateSelections("L2V3radiobutton",0);
          //createTestWizard.setSelections(selections);
          //propertyChangeSupport.firePropertyChange();
      }
      else if (e.getSource() == L2V1radioButton) {
          updateSelections("L1V2radiobutton",0);
          updateSelections("L2V1radiobutton",1);
          updateSelections("L2V2radiobutton",0);
          updateSelections("L2V3radiobutton",0);
          //createTestWizard.setSelections(selections);
      }
      else if (e.getSource() == L2V2radioButton) {
          updateSelections("L1V2radiobutton",0);
          updateSelections("L2V1radiobutton",0);
          updateSelections("L2V2radiobutton",1);
          updateSelections("L2V3radiobutton",0);
         // createTestWizard.setSelections(selections);
      }
      else if(e.getSource() == L2V3radioButton){
          updateSelections("L1V2radiobutton",0);
          updateSelections("L2V1radiobutton",0);
          updateSelections("L2V2radiobutton",0);
          updateSelections("L2V3radiobutton",1);
          //createTestWizard.setSelections(selections);
      }
   }
     createTestWizard.setSelections(selections);

    } 
   /**
    * updateSelections sets a field and value in the hashmap
    * @param field the string field to set
    * @param value the integer value to set the field to.
    */
    public void updateSelections(String field, Integer value){
        // get the selection hashmap, and replace the field with the new value and write it back
        selections.put(field, value);
    }
  
    /**
     * getQualifiedName returns the name for the panel
     * @return returns the text "Select Levels"
     */
    public String getQualifiedName() {
        return "SBML Level & Version";
    }
    /**
     * getIdentifier returns the identifier for the panel
     * @return returns the text "LEVELSELECTOR"
     */
    public String getIdentifier() {
        return "LEVELSELECTOR";
    }
    /**
     * getPrevious indicates the previous panel
     * @return returns null in this case as it is the first panel.
     */
    public String getPrevious() {
        return null;
    }
    /**
     * getNext indicates the next panel in sequence
     * @return the text of the next panel - "TESTTYPE"
     */
    public String getNext() {
        
        return "TESTTYPE";
    }
    /**
     * isLast indicates if the levelseletor panel is the last panel in the wizard.
     * @return boolean - returns false.
     */
    public boolean isLast() {
        return false;
    }
    /**
     * isFirst indicates if the levelselectorpanel is the first panel in the wizard.
     * @return boolean returns true
     */
    public boolean isFirst() {
        return true;
    }
    /**
     * mayFinish indicates if the panel may allow the user to use the finish button
     * @return returns false
     */
    public boolean mayFinish() {
        return false;
    }
    /**
     * not in use yet
     * @param selections
     */
    public void validateSelections(HashMap<String, Object> selections) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

   
}
