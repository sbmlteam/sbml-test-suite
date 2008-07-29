// @file    TestTabPane.java
// @brief   TestTabPane class for SBML Standalone application
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
// Creates the test main view of application.
//


package sbml.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.event.*;
import javax.swing.JOptionPane;
// import java.awt.Desktop;
/**
 * TestTabPane class creates the main test window
 * @author Kimberly Begley
 * @version 2.0
 */
public class TestTabPane extends JPanel implements DocumentListener, ListSelectionListener {

    private JPanel buttonPanel;
    private JTabbedPane tabbedPane;
    private MapViewPane mapViewPane;
    private ListViewPane listViewPane;
    TestRunnerWorker testRunnerWorker;
    JButton startButton, stopButton, resetButton;
    private TestConfiguration testConfiguration;
    private TestRunnerView owner;
  //  TestCaseListModel testCaseListModel = new TestCaseListModel();
   // JList testList;
    private JLabel passedLabel,  skippedLabel,  failedLabel;
    JProgressBar progressBar;
    public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
   
    /**
     * TestTabPane has one constructor creates the GUI for the main test window
     * @param owner the instance of TestRunnerView for this thread
     */
    public TestTabPane(TestRunnerView owner) {
        super();
        this.owner = owner;
        createGUI();
        setPreferredSize(new Dimension(850, 700));

    }
    /**
     * createGUI creates the main frame in which the test views reside. It creates the start, stop, configuration and reset buttons as well as the progress bar.
     */
    private void createGUI() {
        ToolTipManager.sharedInstance().setInitialDelay(5);
        setOpaque(false);
        setLayout(new BorderLayout());
        if (!MAC_OS_X) {
            setBorder(BorderFactory.createEmptyBorder(10, 3, 3, 3));
        }
        buttonPanel = new JPanel();
        tabbedPane = new JTabbedPane();

        add(buttonPanel, BorderLayout.PAGE_START);
        add(tabbedPane, BorderLayout.CENTER);

        listViewPane = new ListViewPane();
        tabbedPane.add("List View", listViewPane);

        mapViewPane = new MapViewPane();
        tabbedPane.add("Map View", mapViewPane);

        buttonPanel.setLayout(new GridLayout(1, 5, 10, 10));
        buttonPanel.setOpaque(false);

        ButtonGroup controlButtons = new ButtonGroup();

    
        JButton configureButton = new JButton(new ImageIcon(getClass().getResource("resources/config.png")));

        if (MAC_OS_X) {
            configureButton.putClientProperty("JButton.buttonType", "segmentedCapsule");
            configureButton.putClientProperty("JButton.segmentPosition", "first");
            configureButton.putClientProperty("JComponent.sizeVariant", "regular");
        }
        configureButton.setToolTipText("Configure");

        configureButton.setAction(new settingsActionClass("", new ImageIcon(getClass().getResource("resources/config.png"))));
        
        startButton = new JButton(new ImageIcon(getClass().getResource("resources/start.png")));

        if (MAC_OS_X) {
            startButton.putClientProperty("JButton.buttonType", "segmentedCapsule");
            startButton.putClientProperty("JButton.segmentPosition", "first");
            startButton.putClientProperty("JComponent.sizeVariant", "regular");
        }
        startButton.setToolTipText("Start");
        startButton.setAction(new startActionClass("", new ImageIcon(getClass().getResource("resources/start.png"))));
        startButton.setEnabled(false);

        stopButton = new JButton(new ImageIcon(getClass().getResource("resources/stop.png")));

        if (MAC_OS_X) {
            stopButton.putClientProperty("JButton.buttonType", "segmentedCapsule");
            stopButton.putClientProperty("JButton.segmentPosition", "last");
            stopButton.putClientProperty("JComponent.sizeVariant", "regular");
        }

        stopButton.setToolTipText("Stop");
        stopButton.setAction(new stopActionClass("", new ImageIcon(getClass().getResource("resources/stop.png"))));
        stopButton.setEnabled(false);

        resetButton = new JButton(new ImageIcon(getClass().getResource("resources/reset.png")));

        if (MAC_OS_X) {
            resetButton.putClientProperty("JButton.buttonType", "segmentedCapsule");
            resetButton.putClientProperty("JButton.segmentPosition", "last");
            resetButton.putClientProperty("JComponent.sizeVariant", "regular");
        }

        resetButton.setToolTipText("Reset");
        resetButton.setAction(new resetActionClass("", new ImageIcon(getClass().getResource("resources/reset.png"))));
        resetButton.setEnabled(false);


        controlButtons.add(startButton);
        controlButtons.add(stopButton);
//        controlButtons.add(resetButton);
        //    Box layoutBox = Box.createHorizontalBox();
        JPanel layoutBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        layoutBox.setOpaque(false);

        progressBar = new JProgressBar();
        progressBar.setString("0%");
        //progressBar.setValue(100);
        progressBar.setStringPainted(true);

        //  progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        progressBar.setIndeterminate(false);
        if (MAC_OS_X) {
            //    progressBar.putClientProperty("JComponent.sizeVariant", "mini");
        }
        progressBar.setPreferredSize(new Dimension(250, progressBar.getPreferredSize().height));

        Box buttonBox1 = Box.createHorizontalBox();
        Box buttonBox2 = Box.createHorizontalBox();
        buttonBox1.add(startButton);
        buttonBox1.add(stopButton);
        layoutBox.add(buttonBox1);
        layoutBox.add(progressBar);
        // layoutBox.add(Box.createHorizontalStrut(50));
        buttonBox2.add(configureButton);
        buttonBox2.add(resetButton);
        layoutBox.add(buttonBox2);
        //       layoutBox.add(websiteButton);

        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
        buttonPanel.add(layoutBox);

        //   JPanel progressBarPane = new JPanel(new FlowLayout());
        //    progressBarPane.add(progressBar);
        //   progressBarPane.setOpaque(false);
        //   buttonPanel.add(progressBarPane);
        JPanel infoPane = new JPanel();
        infoPane.setLayout(new FlowLayout(FlowLayout.CENTER, 30, 10));
        infoPane.setOpaque(false);

        passedLabel = new JLabel("0 (0%)");
        passedLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        //    passedLabel.setPreferredSize(new Dimension(80, passedLabel.getPreferredSize().height));
        skippedLabel = new JLabel("0 (0%)");
        skippedLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        //   skippedLabel.setPreferredSize(new Dimension(80, skippedLabel.getPreferredSize().height));
        failedLabel = new JLabel("0 (0%)");
        failedLabel.setHorizontalTextPosition(SwingConstants.RIGHT);
        //    failedLabel.setPreferredSize(new Dimension(80, passedLabel.getPreferredSize().height));

        infoPane.add(new JLabel("Passed:"));
        infoPane.add(passedLabel);

        infoPane.add(new JLabel("Failed:"));
        infoPane.add(failedLabel);
        infoPane.add(new JLabel("Skipped:"));
        infoPane.add(skippedLabel);
        buttonPanel.add(infoPane);

    }
    /**
     * enableStart enables the start button
     */
    public void enableStart() {
        this.startButton.setEnabled(true);
        
    }
    /**
     * disableStart disables the start button
     */
    public void disableStart() {
        this.startButton.setEnabled(false);
    }
    /**
     * enableStop enables the stop button
     */
    public void enableStop() {
        this.stopButton.setEnabled(true);
        
    }
    /**
     * disableStop disables the stop button
     */
    public void disableStop() {
        this.stopButton.setEnabled(false);
    }
    /**
     * enableReset enables the reset button
     */
    public void enableReset() {
        this.resetButton.setEnabled(true);
        
    }
    /**
     * disableReset disables the reset button
     */
    public void disableReset() {
        this.resetButton.setEnabled(false);
    }
    /**
     * wrapperError pops up a dialog box indicating a wrapper error
     */
    public void wrapperError() {
        JOptionPane.showMessageDialog(null, "Wrapper did not execute successfully - edit it and try again", "Wrapper error...", JOptionPane.WARNING_MESSAGE);
    }
    /**
     * setConfigurations sets the testConfiguration hashmap equal to the input test configurations
     * @param tconfiguration a hashmap of configurations for the tests
     */
    public void setConfigurations(HashMap<String, Object> tconfiguration) {
        this.testConfiguration = new TestConfiguration(tconfiguration);
    }
    /**
     * updateDisplay - no longer in use
     * @param data
     */
    private void updateDisplay(TestResultDetails data) {
        //      resultMap.updateData(data);
        //       shortDetailLabel.setText("You are viewing dataset no. " + data.getName());
    }
    /**
     * updateData - no longer in use
     * @param data
     */
    public void updateData(TestResultDetails[] data) {
            //  testCaseListModel.updateData(data);
    }
    /**
     * insertUpdate - no longer in use
     * @param ev
     */
    public void insertUpdate(DocumentEvent ev) {
        
        //     testCaseListModel.setSearchString(searchField.getText());
    }
    /**
     * removeUpdate - not in use
     */ 
    public void removeUpdate(DocumentEvent ev) {
        //     testCaseListModel.setSearchString(searchField.getText());
    }
    /**
     * changedUpdate - not in use
     * @param ev
     */
    public void changedUpdate(DocumentEvent ev) {
    }
    /**
     * valueChanged - not in use
     * @param e
     */
    public void valueChanged(ListSelectionEvent e) {
        
//        if (e.getValueIsAdjusting() == false) {
//
//            if (testList.getSelectedIndex() == -1) {
//                resultMap.resetDisplay();
//                shortDetailLabel.setText("No dataset selected");
//            } else {
//                updateDisplay(testCaseListModel.getRawElementAt(testList.getSelectedIndex()));
//            }
//        }
    }
    /**
     * updateProgress passes values to update the progress bar details
     * @param total total number of tests taken so far  
     * @param failed total number of failed tests so far
     * @param skipped total number of skipped tests so far
     * @param passed total number of passed tests so far
     * @param finish - 1 if test if finished, 0 otherwise - no longer in use
     */
    public void updateProgress(int total, int failed, int skipped, int passed, int finish) {
      //  if(finish == 1){
      //      progressBar.setIndeterminate(false);
      //      progressBar.setValue(100);
      //      progressBar.setString("100%");
      //  }
       // else{
       //     progressBar.setIndeterminate(true);
       // }
        
        
       //int progress = total +skipped+failed+passed;
       int progress = 100*(failed+skipped+passed)/total;
       //String progress = Integer.toString(100*((failed+skipped+passed)/total));
        
        progressBar.setValue(progress);
        progressBar.setString(Integer.toString(progress)+"%");
        failedLabel.setText(Integer.toString(failed) + "/"+ Integer.toString(total)+" ("+ Integer.toString(100*failed/(failed+passed+skipped))+"%)");
        skippedLabel.setText(Integer.toString(skipped) + "/"+ Integer.toString(total)+" ("+ Integer.toString(100*skipped/(failed+passed+skipped))+"%)");
        passedLabel.setText(Integer.toString(passed) +"/"+ Integer.toString(total)+ " ("+ Integer.toString(100*passed/(failed+passed+skipped))+"%)"); 
    }
    /**
     * settingsActionClass is the class for the configration button
     */
    public class settingsActionClass extends AbstractAction {
      //  private TestRunnerView owner;
      /**
       * settingsActionClass constructor
       * @param text text for configuration button
       * @param icon
       */
        public settingsActionClass(String text, Icon icon) {
            super(text, icon);
          //  this.owner = owner;
            
        //         putValue(ACCELERATOR_KEY, shortcut);
        }
        /**
         * actionPerformed - action performed when the configuration button is pushed - it launches a new test wizard
         * @param e the action event when the configuation button is selected
         */
        public void actionPerformed(ActionEvent e) {
           
            Thread.currentThread().interrupt();
            sbml.test.Wizard.CreateTestWizard wizard = new sbml.test.Wizard.CreateTestWizard(owner);
            wizard.runWizard();
        }
    }
    /**
     * startActionClass is the class for the start button
     */
    public class startActionClass extends AbstractAction {
       // TestConfiguration testConfiguration;
        /**
         * startActionClass constructor - sets the text for the start button
         * @param text
         * @param icon
         */
        public startActionClass(String text, Icon icon) {
            super(text, icon);
          //  this.testConfiguration = testConfiguration;
        //         putValue(ACCELERATOR_KEY, shortcut);
        }
        /**
         * actionPerformed - sets the action for the start button - it starts a new test with the same configurations as indicated in the wizard
         * @param e the action event when the start button is pressed
         */
        public void actionPerformed(ActionEvent e) {
            //     JFrame mainFrame = TestRunnerApp.getApplication().getMainFrame();
            // want this to call the start method in TestRunnerView which calls start in testrunnerworker
            //JOptionPane.showMessageDialog(null, "Nothing to resume.", "Resume...", JOptionPane.WARNING_MESSAGE);
           // mapViewPane.updateMap();
            owner.startTest(testConfiguration);
        }
    }
    /**
     * stopActionClass the class responsible for the stop button
     */
    public class stopActionClass extends AbstractAction {
        /**
         * stopActionClass has one constructor that sets the text for the stop button
         * @param text
         * @param icon
         */
        public stopActionClass(String text, Icon icon) {
            super(text, icon);
        //         putValue(ACCELERATOR_KEY, shortcut);
        }
        /**
         * actionPerformed - sets the action for the stop button - by calling the stopTest method.
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            //   JFrame mainFrame = TestRunnerApp.getApplication().getMainFrame();
           // JOptionPane.showMessageDialog(null, "Test Stopped.", "Stop...", JOptionPane.WARNING_MESSAGE);
           // testRunnerWorker.interruptWorkerThread();
            //Thread.currentThread().interrupt();
            owner.stopTest();
        }
    }
    /**
     * resetActionClass is the class responsible for the reset button
     */
    public class resetActionClass extends AbstractAction {
        /**
         * resetActionClass is the constructor - it sets the text for the reset button
         * @param text
         * @param icon
         */
        public resetActionClass(String text, Icon icon) {
            super(text, icon);
        //         putValue(ACCELERATOR_KEY, shortcut);
        }
        /**
         * actionPerformed for the reset button - it stops the current thread and enables the start button
         * @param e
         */
        public void actionPerformed(ActionEvent e) {
            //     JFrame mainFrame = TestRunnerApp.getApplication().getMainFrame();
            // want this to pull up wizard to reset selections
            Thread.currentThread().interrupt();
            // start a new thread and start test with previously saved selections
            //owner.startTest(testConfiguration);
            //JOptionPane.showMessageDialog(null, "Nothing to reset.", "Reset...", JOptionPane.WARNING_MESSAGE);
            enableStart();
        }
    }
    /** not in use
     * 
     */
    public class openBrowserActionClass extends AbstractAction {

        public openBrowserActionClass(String text, Icon icon) {
            super(text, icon);
        //         putValue(ACCELERATOR_KEY, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            //       JFrame mainFrame = TestRunnerApp.getApplication().getMainFrame();
            JOptionPane.showMessageDialog(null, "Not implemented.", "Open Webbrowser...", JOptionPane.WARNING_MESSAGE);
//            try {
//                Desktop.browse(new URL("http://sbml.org"));
//            } catch (MalformedURLException e1) {
//                e1.printStackTrace();
//            } catch (DesktopException e2) {
//                e2.printStackTrace();
//            }
        }
    }
}
