// @file    WrapperConfigurationPanel.java
// @brief   WrapperConfigurationPanel class for SBML Standalone application
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
// Generate a panel for the wrapper details in the new test wizard.
//

package sbml.test.Wizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

/**
 * WrapperConfigurationPanel creates the panel for the wrapper details in the wizard.
 * @author Kimberly Begley
 * @version 2.0
 */
public class WrapperConfigurationPanel extends WizardPanel {

    JTextField wrapCommand;
    JTextField inputPath;
    JTextField outputPath;
    /**
     * WrapperConfigurationPanel initializes the components for the wrapper panel
     * @param createTestWizard the createTestWizard instance for the wizard.
     */
    public WrapperConfigurationPanel(CreateTestWizard createTestWizard) {
        initComponents();
    }
    /**
     * initComponents initializes the components for the wrapper panel 
     */
    private void initComponents() {
        setLayout(new BorderLayout());

        wrapCommand = new JTextField(40);
      
        wrapCommand.setText("/Users/kimberlybegley/bin/my_wrapper %d %n %o");
      
        

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(getQualifiedName());
        nameLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        topPanel.add(nameLabel, BorderLayout.PAGE_START);
        topPanel.add(new JSeparator(), BorderLayout.PAGE_END);
        add(topPanel, BorderLayout.PAGE_START);

        JPanel contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        //ButtonGroup buttonGroup = new ButtonGroup();
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JPanel contentPanel1 = new JPanel(new GridLayout(0, 2, 80, 0));
        contentPanel1.add(wrapCommand);
        JPanel contentPanel2 = new JPanel(new BorderLayout());
        contentPanel2.add(contentPanel1, BorderLayout.WEST);
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        messagePanel.add(new JLabel("<html>Please enter the wrapper command for this test substituting <b>%d</b> for the test case path,</html>"), BorderLayout.NORTH);
        messagePanel.add(new JLabel("<html> <b>%n</b> for the test case and <b>%o</b> for the output path:</html>"), BorderLayout.WEST);
        contentPanel2.add(messagePanel, BorderLayout.NORTH);
        
   /*     
        JPanel inputPathPanel1 = new JPanel(new GridLayout(0, 2, 80, 0));
        //inputPathPanel1.add(inputPath);
        JPanel inputPathPanel2 = new JPanel(new BorderLayout());
        inputPathPanel2.add(inputPathPanel1, BorderLayout.WEST);
        JPanel inputPathPanel3 = new JPanel(new BorderLayout());
        inputPathPanel3.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        inputPathPanel3.add(new JLabel("Please enter the input path:"), BorderLayout.NORTH);
        inputPathPanel2.add(inputPathPanel3, BorderLayout.NORTH);        
        
        
        JPanel outputPathPanel1 = new JPanel(new GridLayout(0, 2, 80, 0));
        outputPathPanel1.add(outputPath);
        JPanel outputPathPanel2 = new JPanel(new BorderLayout());
        outputPathPanel2.add(outputPathPanel1, BorderLayout.WEST);
        JPanel outputPathPanel3 = new JPanel(new BorderLayout());
        outputPathPanel3.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        outputPathPanel3.add(new JLabel("Please enter the output path:"), BorderLayout.NORTH);
        outputPathPanel2.add(outputPathPanel3, BorderLayout.NORTH);         
     */   

        inputPanel.add(contentPanel2);
      //  inputPanel.add(inputPathPanel2);
      //  inputPanel.add(outputPathPanel2);
        contentPanel.add(inputPanel, BorderLayout.NORTH);


    }
    /**
     * getWrapperPath gets the text value of the wrapper path
     * @return returns the text for the wrapper command
     */
    public String getWrapperPath() {
        return wrapCommand.getText();
    }
    /**
     * no longer in use
     * @return
     */
    public String getInputPath() {
        return inputPath.getText();
    }    
    /**
     * no longer in use
     * @return
     */
    public String getOutputPath() {
        return outputPath.getText();
    }    
    
    /**
     * getQualifiedName gets the name of the panel
     * @return returns the text "Configure Wrapper"
     */
    public String getQualifiedName() {
        return "Configure Wrapper";
    }
    /**
     * getIdentifier gets the identifier for the panel
     * @return returns "WRAPPER"
     */
    public String getIdentifier() {
        return "WRAPPER";
    }
    /**
     * getPrevious gets the previous panel for the new test wizard
     * @return returns the text "LEVELSELECTOR"
     */
    public String getPrevious() {
        return "LEVELSELECTOR";
    }
    /**
     * getNext gets the next panel for the new test wizard
     * @return returns null as this is the last panel.
     */
    public String getNext() {
        return null;
    }
    /**
     * isLast indicates if this is the last panel in the wizard
     * @return returns true
     */
    public boolean isLast() {
        return true;
    }
    /**
     * isFirst indicates if this is the first panel in the wizard
     * @return returns false 
     */
    public boolean isFirst() {
        return false;
    }
    /**
     * mayFInish indicates if the finish button may be activated on this panel.
     * @return returns true
     */
    public boolean mayFinish() {
        return true;
    }
    /**
     * validateSelections - not in use here
     * @param selections
     */
    public void validateSelections(HashMap<String, Object> selections) {
        //  throw new UnsupportedOperationException("Not supported yet.");
    }
}
