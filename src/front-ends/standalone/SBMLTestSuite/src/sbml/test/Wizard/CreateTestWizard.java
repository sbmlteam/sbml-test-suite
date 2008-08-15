// @file    CreateTestWizard.java
// @brief   CreateTestWizard class for SBML Standalone application
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
// Main class for creating the new test wizard.
//

package sbml.test.Wizard;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.HashMap;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;
import javax.swing.border.EmptyBorder;
import sbml.test.TestConfiguration;
import sbml.test.TestRunnerView;
import sbml.test.TestTabPane;

/**
 * CreateTestWizard is the main class for creating a new test wizard
 * @author Kimberly Begley
 * @version 2.0
 */
public class CreateTestWizard {

//        private WizardModel wizardModel;
//    private WizardController wizardController;
    private JDialog Wizard;
    private JPanel cardPanel;
    private CardLayout cardLayout;
    private JButton backButton;
    private JButton nextButton;
    private JButton cancelButton;
    private JButton finishButton;
    private int returnCode;
    private TestRunnerView owner;
    private TestTabPane testTabPane;
    private WizardPanelInterface[] panels;
    private int currentPanel;
    public static int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    private Action nextAction,  previousAction,  cancelAction,  finishAction;
    private JLabel[] overviewLabel;
    public HashMap<String, Object> selections = new HashMap<String, Object>();
    private final PropertyChangeSupport propertyChangeSupport;

    /**
     * CreateTestWizard constructor initializes componenet for the wizard and adds a listener for the selections
     * @param owner the TestRunnerView instance
     */
    public CreateTestWizard(TestRunnerView owner) {

        Wizard = new JDialog(owner, "Start new test", true);
        this.owner = owner;
        propertyChangeSupport = new PropertyChangeSupport(this);
        propertyChangeSupport.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent e) {
                updatePanels();
                //System.out.println("inside the propertyChange");
            }
        });

        initComponents();
    }
    /**
     * updatePanels is called by the propertychange event to validate the selections as changes are made on the hashmap
     */
    private void updatePanels() {
        // call the validate functions of all panels
        //System.out.println("updating panels here soon");
        panels[2].validateSelections(selections);
        panels[3].validateSelections(selections);

    }
    /**
     * initializeSelections initializes the selection hashmap for the wizard
     * @param selections a hashmap of all the selectable options of the wizard - set to one or zero for being selected or not
     * One idea was to set to -1 for tags that were not yet implemented that would eliminate having to comment them out 
     */
    public void initializeSelections(HashMap<String, Object> selections) {
        selections.put("L1V2radiobutton", 0);
        selections.put("L2V1radiobutton", 0);
        selections.put("L2V2radiobutton", 0);
        selections.put("L2V3radiobutton", 1);
        // timecourse set to 1 initially since it is default as being checked
        selections.put("timecourse", 1);
        selections.put("FunctionDefinition", 0);
    //    selections.put("UnitDefinition", 0);
        selections.put("InitialAssignment", 0);
        selections.put("AssignmentRule", 0);
        selections.put("RateRule", 0);
        selections.put("AlgebraicRule", 0);
    //    selections.put("Constraint", 0);
        selections.put("EventWithDelay", 0);
        selections.put("EventNoDelay", 0);
        selections.put("Compartment", 0);
        selections.put("Species", 0);
        selections.put("Reaction", 0);
        selections.put("Parameter", 0);
        selections.put("2D-Compartment", 0);
        selections.put("1D-Compartment", 0);
        selections.put("0D-Compartment", 0);
        selections.put("NonConstantCompartment", 0);
        selections.put("NonUnityCompartment", 0);
        selections.put("MultiCompartment", 0);
        selections.put("InitialAmount", 0);
        selections.put("InitialConcentration", 0);
        selections.put("HasOnlySubstanceUnits", 0);
        selections.put("BoundaryCondition", 0);
        selections.put("ConstantSpecies", 0);
        selections.put("NonConstantParameter", 0);
        selections.put("FastReaction", 0);
        selections.put("ReversibleReaction", 0);
        selections.put("NonUnityStoichiometry", 0);
        selections.put("StoichiometryMath", 0);
        selections.put("LocalParameters", 0);
 //       selections.put("CSymbolDelay", 0);
        selections.put("CSymbolTime", 0);
 //       selections.put("MassUnits", 0);
 //       selections.put("Units", 0);
        selections.put("logging",0);
        
    }
    /**
     * getSelections gets the selection hashmap
     * @return returns the selections hashmap
     */
    public HashMap<String, Object> getSelections() {
        return selections;
    }
    /**
     * addPropertyChangeListener - adds a new propertychangelistener
     * @param listener the listener to add
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }
    /**
     * removePropertyChangeListener - removes a propertychangelistener
     * @param listener the listener to remove
     */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.removePropertyChangeListener(listener);
    }
    /**
     * setSelections sets the selections hashmap to a new hashmap and fires a property change so that the new map can be validated
     * @param newSelections the new hashmap to replace the selections hashmap with
     */
    public void setSelections(HashMap<String, Object> newSelections) {

        selections = newSelections;
        this.propertyChangeSupport.firePropertyChange("selections", 0, 1);
    }
    /**
     * initComoponentents initializes the components for the test wizard
     */
    private void initComponents() {


        initializeSelections(selections);

        JPanel buttonPanel = new JPanel();
        Box buttonBox = new Box(BoxLayout.X_AXIS);

        cardPanel = new JPanel();
        cardPanel.setBorder(new EmptyBorder(new Insets(15, 15, 5, 15)));

        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        nextAction = new nextActionClass("Next");
        previousAction = new previousActionClass("Previous");
        cancelAction = new cancelActionClass("Cancel");
        finishAction = new finishActionClass("Finish");

        backButton = new JButton(previousAction);
        nextButton = new JButton(nextAction);
        cancelButton = new JButton(cancelAction);
        finishButton = new JButton(finishAction);

//    backButton.addActionListener(wizardController);
//    nextButton.addActionListener(wizardController);
//    cancelButton.addActionListener(wizardController);

        buttonPanel.setLayout(new BorderLayout());
        buttonPanel.add(new JSeparator(), BorderLayout.NORTH);

        buttonBox.setBorder(new EmptyBorder(new Insets(5, 10, 5, 10)));
        buttonBox.add(backButton);
        buttonBox.add(Box.createHorizontalStrut(10));
        buttonBox.add(nextButton);
        buttonBox.add(Box.createHorizontalStrut(30));
        buttonBox.add(cancelButton);
        buttonBox.add(Box.createHorizontalStrut(30));
        buttonBox.add(finishButton);

        buttonPanel.add(buttonBox, java.awt.BorderLayout.EAST);
        Wizard.getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        Wizard.getContentPane().add(cardPanel, java.awt.BorderLayout.CENTER);

        JPanel overviewPanel = new JPanel(new BorderLayout());
        overviewPanel.setPreferredSize(new Dimension(170, 100));
        overviewPanel.setBackground(Color.gray);
        JPanel innerOverviewPanel = new JPanel(new GridLayout(0, 1, 0, 15));
        innerOverviewPanel.setOpaque(false);
        overviewPanel.add(innerOverviewPanel, BorderLayout.NORTH);
        overviewPanel.setBorder(BorderFactory.createEmptyBorder(15, 5, 0, 5));

        Wizard.getContentPane().add(overviewPanel, BorderLayout.WEST);

        panels = new WizardPanelInterface[5];
        overviewLabel = new JLabel[5];


        panels[0] = new LevelSelectorPanel(this);
        panels[1] = new TestTypeSelectorPanel(this);
        panels[2] = new ComponentTagsSelectorPanel(this);
        panels[3] = new TestTagSelectionPanel(this);
        panels[4] = new WrapperConfigurationPanel(this);


        for (int i = 0; i < panels.length; i++) {
            cardPanel.add(panels[i].getIdentifier(), (WizardPanel) panels[i]);
            overviewLabel[i] = new JLabel("" + (i + 1) + ". " + panels[i].getQualifiedName());
            innerOverviewPanel.add(overviewLabel[i]);
            if (i == 0) {
                overviewLabel[i].setForeground(Color.WHITE);
            } else {
                overviewLabel[i].setForeground(Color.BLACK);
            }
        }
        currentPanel = 0;


        Wizard.pack();

        Wizard.setModal(true);
        Wizard.setSize(new Dimension(750, 500));
        Wizard.setResizable(false);
        Wizard.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
        Wizard.setLocationRelativeTo(null);
    }
    /**
     * runWizard sets the wizard to run
     */
    public void runWizard() {
        Wizard.setVisible(true);

    }
    /**
     * changeCard changes the viewable pages on the wizard
     * @param cardName
     */
    public void changeCard(String cardName) {
        cardLayout.show(cardPanel, cardName);
        if (panels[currentPanel].isLast()) {
            nextAction.setEnabled(false);
        } else {
            nextAction.setEnabled(true);
        }
        if (panels[currentPanel].isFirst()) {
            previousAction.setEnabled(false);
        } else {
            previousAction.setEnabled(true);
        }

        if (panels[currentPanel].mayFinish()) {
            finishAction.setEnabled(true);
        } else {
            finishAction.setEnabled(false);
        }
        for (int i = 0; i < overviewLabel.length; i++) {
            if (i == currentPanel) {
                overviewLabel[i].setForeground(Color.WHITE);
            } else {
                overviewLabel[i].setForeground(Color.BLACK);
            }
        }

    }
    /**
     * nextActionClass sets the next page on the wizard
     */
    public class nextActionClass extends AbstractAction {

        public nextActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            currentPanel++;
            changeCard(panels[currentPanel].getIdentifier());
        }
    }
    /**
     * previousActionClass sets the previous page on the wizard
     */
    public class previousActionClass extends AbstractAction {

        public previousActionClass(String text) {
            super(text);
            setEnabled(false);

            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            currentPanel--;
            changeCard(panels[currentPanel].getIdentifier());
        }
    }
    /**
     * cancelActionClass - closes the wizard
     */
    public class cancelActionClass extends AbstractAction {

        public cancelActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_C, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            Wizard.setVisible(false);
        }
    }
    /**
     * finishActionClass completes the wizard and closes it and passes variables onto the test.
     */
    public class finishActionClass extends AbstractAction {

        public finishActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F, shortcutKeyMask));
            setEnabled(false);
        }

        public void actionPerformed(ActionEvent e) {
            selections.put("WrapperPath", ((WrapperConfigurationPanel) panels[4]).getWrapperPath());
          //  selections.put("InputPath", ((WrapperConfigurationPanel) panels[4]).getInputPath());
          //  selections.put("OutputPath", ((WrapperConfigurationPanel) panels[4]).getOutputPath());

            Wizard.setVisible(false);
            TestConfiguration testConfiguration = new TestConfiguration(selections);
            owner.setSelections(selections); 
            owner.setStartButton();
            //owner.startTest(testConfiguration); // just for testing purposes
           // TestTabPane.startActionClass = 

        }
    }
}
