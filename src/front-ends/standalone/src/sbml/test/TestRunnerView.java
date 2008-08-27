// @file    TestRunnerView.java
// @brief   TestRunnerView class for SBML Standalone application
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
// Creates menu items etc for application.
//
package sbml.test;

import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import apple.dts.samplecode.osxadapter.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.HashMap;

public class TestRunnerView extends JFrame implements WindowListener {

    /** 
     * Creates new form for TestRunnerView
     */
    public TestRunnerView() {
        super("SBML Test Runner");
        initComponents();

    }
    public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
    public static int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    public static TestCaseListModel testCaseListModel;
   // TestCaseUpdater testCaseUpdater;
    public static FailedTestCaseListModel failedTestCaseListModel;
    public static PassedTestCaseListModel passedTestCaseListModel;
    public static SkippedTestCaseListModel skippedTestCaseListModel;
    public static boolean logging;
    public static HashMap<String, Object> currentvalues;
    //  public static HelpDialog helpdialog;
    TestRunnerWorker worker;
    /**
     * initComponents - initializes the components for the TestRunnerView
     */
    private void initComponents() {
        testCaseListModel = new TestCaseListModel();
        failedTestCaseListModel = new FailedTestCaseListModel();
        passedTestCaseListModel = new PassedTestCaseListModel();
        skippedTestCaseListModel = new SkippedTestCaseListModel();

        currentvalues = new HashMap<String, Object>();
        currentvalues.put("logging", false);
        String userdir = System.getProperty("user.home") + File.separator + ".sbmltestrunner" + File.separator + "sbml-test-suite.log";
        currentvalues.put("configpath", userdir);

        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        quitAction = new quitActionClass("Quit");
        quitMenuItem = new JMenuItem(quitAction);
        editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");
        helpAction = new helpActionClass("Help");
        helpMenuItem = new JMenuItem(helpAction);
        helpMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, shortcutKeyMask));

        preferencesAction = new preferencesActionClass("Preferences");
        preferencesMenuItem = new JMenuItem(preferencesAction);
        preferencesMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, shortcutKeyMask));
        // aboutMenuItem = new JMenuItem("About");
        aboutAction = new aboutActionClass("About");
        aboutMenuItem = new JMenuItem(aboutAction);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        fileMenu.setText("File");

        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutKeyMask));

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);
        // editMenu.add(preferencesAction);


        newAction = new newActionClass("New Test Run", this);
        newMenuItem = new JMenuItem(newAction);
        fileMenu.add(newMenuItem);
        //fileMenu.addSeparator();
        //fileMenu.add(preferencesMenu);

        checkUpdatesAction = new checkUpdatesActionClass("Check for test case updates");
        checkUpdatesMenuItem = new JMenuItem(checkUpdatesAction);
        fileMenu.addSeparator();
        fileMenu.add(checkUpdatesMenuItem);
//        fileMenu.addSeparator();
        menuBar.add(helpMenu);
        helpMenu.add(helpMenuItem);



        //  newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutKeyMask));



        if (!MAC_OS_X) {
            fileMenu.add(preferencesMenuItem);
            fileMenu.addSeparator();
            fileMenu.add(quitMenuItem);

            helpMenu.add(aboutMenuItem);
        }


        editMenu.setText("Edit");
        editMenu.add(editMenu);

        setJMenuBar(menuBar);


        setLayout(new BorderLayout());
        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));
        add(mainPane, BorderLayout.CENTER);

        JTabbedPane tabbedPane = new JTabbedPane();

        mainPane.add(tabbedPane, BorderLayout.CENTER);
        // omitted the Test Catalog for now - uncomment it to bring it back
        //    JPanel testTabPane = new JPanel(new BorderLayout());
        //    testTabPane.setOpaque(false);
        testTabPane = new TestTabPane(this);
        //  JPanel testCatalogTabPane = new JPanel(new BorderLayout());
        //  testCatalogTabPane.setOpaque(false);

        tabbedPane.add("Test", testTabPane);
        //  tabbedPane.add("Test case catalog", testCatalogTabPane);


        addWindowListener(this);
        registerForMacOSXEvents();
        pack();
        setSize(new Dimension(850, 600));
        setLocationRelativeTo(null);
    }// </editor-fold>
    ActionListener loggingListener = new ActionListener() {

        public void actionPerformed(ActionEvent e) {
            Object source = e.getSource();
            if (source == loggingMenuItem) {
                if (loggingMenuItem.isSelected()) {
                    // set the logging value to 1               
                    logging = true;
                } else {
                    // set the logging value to 0                  
                    logging = false;

                }

            }
        }
    };

    /**
     * @param args the command line arguments
     */
    public static void showGui(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new TestRunnerView().setVisible(true);
            }
        });
    }
    /**
     * quit - closes the application
     * @return returns a boolean value if user wants to quit
     */
    public boolean quit() {
        ImageIcon icon = new ImageIcon(getClass().getResource("resources/question.png"));
        int option = JOptionPane.showConfirmDialog(this, 
                "Quit the SBML Test Suite?", "Quit?", 
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                icon);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

        return (option == JOptionPane.YES_OPTION);
    }
    /**
     * about - sets the about box contents
     */
    public void about() {
        //    aboutBox.setLocation((int)this.getLocation().getX() + 22, (int)this.getLocation().getY() + 22);
        //    aboutBox.setVisible(true);
        // Display the about box here
        ImageIcon icon = new ImageIcon(getClass().getResource("resources/SBML.png"));
        JOptionPane.showMessageDialog(this, 
                "<html><p>SBML Test Suite V2.0.0a <em>Standalone Application</em><p><br>"
                + "<p>Written by Kimberly Begley.</p><br>"
                + "<p>For more information about the SBML Test Suite,<br>please "
                + "visit the project website at <br><a href='http://sbml.org/Software/SBML_Test_Suite'>"
                + "http://sbml.org/Software/SBML_Test_Suite</a>.</p><br>"
                + "</html>", "About the SBML Test Suite", JOptionPane.PLAIN_MESSAGE, icon);
    }
    /**
     * openHelp opens the help dialog
     */
    public void openHelp() {

        HelpDialog displayHelp = new HelpDialog(this);
    }

    
    /**
     * General preferences dialog; fed to the OSXAdapter as the method to call when
     * "Preferences..." is selected from the application menu
     */
    public void preferences() {

        currentvalues = PreferencesDialog.getValue(this, currentvalues);

    }
    /**
     * getLoggingInfo - gets the hashmap containing the logging info from the dialog
     * @return
     */
    public HashMap<String, Object> getLoggingInfo() {
        return currentvalues;
    }
    /**
     * performUpdate - is run when the user selects to "check for new test cases" from the menu
     */
    public void performUpdate() {
    // connect to server and get latest copy of zip file if newer than packaged copy
        TestCaseUpdater testCaseUpdater = new TestCaseUpdater(this, false);
        testCaseUpdater.checkForUpdates();
        testCaseUpdater.setModal(true);
    }
    /**
     * windowOpened - can uncomment the performUpdate line to have it automatically check for updates as soon as the app is started
     * @param e
     */
    public void windowOpened(WindowEvent e) {
       // performUpdate();
    //   throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * windowClosing - exits the program
     * @param e
     */
    public void windowClosing(WindowEvent e) {
        if (quit()) {
            System.exit(0);
        }
    //throw new UnsupportedOperationException("Not supported yet.");
    }
    /**
     * updateProgress updates the progess bar information
     * @param progress total number of tests taken so far
     * @param failed number of failed tests so far
     * @param skipped number of skipped tests so far
     * @param passed number of passed tests so far
     * @param finish indicates whether at the end of test set or not
     */
    public void updateProgress(int progress, int failed, int skipped, int passed, int finish) {

        testTabPane.updateProgress(progress, failed, skipped, passed, finish);
    }
    /**
     * sets the selections hashmap
     * @param tconfiguration hashmap to set
     */
    public void setSelections(HashMap<String, Object> tconfiguration) {
        testTabPane.setConfigurations(tconfiguration);
    }
    /**
     * setStartButton - enables the start button
     */
    public void setStartButton() {
        testTabPane.enableStart();
    }
    /**
     * disableStartButton - disables the start button
     */
    public void disableStartButton() {
        testTabPane.disableStart();
    }
    /**
     * setStopButton - sets the stop button
     */
    public void setStopButton() {
        testTabPane.enableStop();
    }
    /**
     * disableStopButton - disables the stop button
     */
    public void disableStopButton() {
        testTabPane.disableStop();
    }
    /**
     * setResetButton - sets the reset button
     */
    public void setResetButton() {
        testTabPane.enableReset();
    }
    /**
     * disableResetButton - disables the reset button
     */
    public void disableResetButton() {
        testTabPane.disableReset();
    }
    /** 
     * displayWrapperError - displays the wrapper error diaglog
     */
    public void displayWrapperError() {
        testTabPane.wrapperError();
    }
    /**
     * windowClosed event - nothing happens yet
     * @param e
     */
    public void windowClosed(WindowEvent e) {
    //  throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void windowIconified(WindowEvent e) {
    //   throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeiconified(WindowEvent e) {
    //   throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowActivated(WindowEvent e) {
    //  throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowDeactivated(WindowEvent e) {
    //   throw new UnsupportedOperationException("Not supported yet.");
    }
    // Variables declaration
    // End of variables declaration
    //   public static TestRunnerView getApplication() {
    //       return Application.getInstance(TestRunnerView.class);
    //  }
    /**
     * registerForMacOSXEvents - sets the mac os look and feel for the quit, about and preferences menu items
     */
    public void registerForMacOSXEvents() {
        if (MAC_OS_X) {
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[]) null));
                OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[]) null));
                OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[]) null));
            //           OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[]{String.class}));
            } catch (Exception e) {
                System.err.println("Error while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }
    /**
     * startTest - starts a test
     * @param testConfiguration test parameters for the test
     */
    public void startTest(TestConfiguration testConfiguration) {

        //  final TestConfiguration finalTestConfig = testConfiguration;
        //    TestCaseListModel list = new TestCaseListModel();
        setStopButton();
        disableResetButton();
        testCaseListModel.removeAllElements();
        failedTestCaseListModel.removeAllElements();
        passedTestCaseListModel.removeAllElements();
        skippedTestCaseListModel.removeAllElements();
        if (logging) {
            testConfiguration.set("logging", 1);
        } else {
            testConfiguration.set("logging", 0);
        }
        worker = new TestRunnerWorker(this, testConfiguration);
        worker.start();
    }
    /**
     * stopTest sends an interrupt to the worker thread
     */
    public void stopTest() {
        setResetButton();
        worker.interrupt();
    }
    /**
     * newActionClass is called when a new test is selected from the menu
     */
    public class newActionClass extends AbstractAction {

        TestRunnerView parent;

        public newActionClass(String text, TestRunnerView parent) {
            super(text);
            this.parent = parent;
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutKeyMask));

        }

        public void actionPerformed(ActionEvent e) {
            sbml.test.Wizard.CreateTestWizard wizard = new sbml.test.Wizard.CreateTestWizard(parent);
            wizard.runWizard();
        }
    }
    /**
     * checkUpdatesActionClass is called when the menu is selected to check for new test cases
     */
    public class checkUpdatesActionClass extends AbstractAction {

        public checkUpdatesActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            System.out.println("New...");
        // connect to sever here and look for updated tests
          // boolean itworked = testCaseUpdater.checkForUpdates();
           // if(itworked) { System.out.println("the update worked");}
            performUpdate();
        }
    }
    /**
     * quitActionClass is called when the quit menu is selected
     */
    public class quitActionClass extends AbstractAction {

        public quitActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            quit();
        }
    }
    /**
     * aboutActionClass is called when about is selected from the menu
     */
    public class aboutActionClass extends AbstractAction {

        public aboutActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            about();
        }
    }
    /**
     * helpActionClass is called when help is selected from the menu
     */
    public class helpActionClass extends AbstractAction {

        public helpActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            openHelp();
        }
    }
    /**
     * preferencesActionClass is called when the preferences are selected from the menu
     */
    public class preferencesActionClass extends AbstractAction {

        public preferencesActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_P, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            preferences();
        }
    }
    private newActionClass newAction;
    private quitActionClass quitAction;
    private aboutActionClass aboutAction;
    private helpActionClass helpAction;
    private checkUpdatesActionClass checkUpdatesAction;
    private preferencesActionClass preferencesAction;
    private JMenu fileMenu;
    private JMenu editMenu;
    //private JMenu helpMenu;
    private JMenuBar menuBar;
    private JMenuItem quitMenuItem;
    private JMenuItem preferencesMenuItem;
    private JCheckBoxMenuItem loggingMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenuItem helpMenuItem;
    private JMenuItem newMenuItem;
    private JMenuItem checkUpdatesMenuItem;
    private TestTabPane testTabPane;
}
