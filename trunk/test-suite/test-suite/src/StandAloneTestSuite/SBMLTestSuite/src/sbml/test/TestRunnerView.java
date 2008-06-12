
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
import javax.help.HelpBroker;
import javax.help.HelpSet;



public class TestRunnerView extends JFrame implements WindowListener {

    /** Creates new form TestRunnerView */
    public TestRunnerView() {
        super("SBML TestRunner");
        initComponents();

    }
    public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
    public static int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();
    public static TestCaseListModel testCaseListModel;
    public static FailedTestCaseListModel failedTestCaseListModel;
    public static PassedTestCaseListModel passedTestCaseListModel;
    public static SkippedTestCaseListModel skippedTestCaseListModel;
    public static boolean logging;
    
    private void initComponents() {
        testCaseListModel = new TestCaseListModel();
        failedTestCaseListModel = new FailedTestCaseListModel();
        passedTestCaseListModel = new PassedTestCaseListModel();
        skippedTestCaseListModel = new SkippedTestCaseListModel();
        menuBar = new JMenuBar();
        fileMenu = new JMenu();
        quitAction = new quitActionClass("Quit");
        quitMenuItem = new JMenuItem(quitAction);
        editMenu = new JMenu("Edit");
        //helpMenu = new JMenu("Help");
        helpAction = new helpActionClass("Help");
        helpMenuItem = new JMenuItem(helpAction);
        //preferencesAction = new preferencesActionClass("Preferences");
        preferencesMenu = new JMenu("Preferences");
       // preferencesMenu.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, shortcutKeyMask));
        loggingMenuItem = new JCheckBoxMenuItem("Generate Debugging log");
        preferencesMenu.add(loggingMenuItem);
        loggingMenuItem.addActionListener(loggingListener);
       // aboutMenuItem = new JMenuItem("About");
        aboutAction = new aboutActionClass("About");
        aboutMenuItem = new JMenu(aboutAction);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        fileMenu.setText("File");

        quitMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutKeyMask));

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        newAction = new newActionClass("New test", this);
        newMenuItem = new JMenuItem(newAction);
        fileMenu.add(newMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(preferencesMenu);

        checkUpdatesAction = new checkUpdatesActionClass("Check for test case updates");
        checkUpdatesMenuItem = new JMenuItem(checkUpdatesAction);
        fileMenu.addSeparator();
        fileMenu.add(checkUpdatesMenuItem);
        fileMenu.addSeparator();
        fileMenu.add(helpMenuItem);
        

        //  newMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, shortcutKeyMask));



        if (!MAC_OS_X) {
            fileMenu.add(preferencesMenu);
            fileMenu.addSeparator();
            fileMenu.add(quitMenuItem);

            
            menuBar.add(helpMenuItem);
            menuBar.add(aboutMenuItem);
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
          if(source == loggingMenuItem){
              if(loggingMenuItem.isSelected()){
                  // set the logging value to 1               
                  logging = true;
              }
              else {
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

    public boolean quit() {
        int option = JOptionPane.showConfirmDialog(this, "Are you sure you want to quit?", "Quit?", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }

        return (option == JOptionPane.YES_OPTION);
    }

    public void about() {
        //    aboutBox.setLocation((int)this.getLocation().getX() + 22, (int)this.getLocation().getY() + 22);
        //    aboutBox.setVisible(true);
        // Display the about box here
        ImageIcon icon = new ImageIcon(getClass().getResource("resources/SBML.png"));
        JOptionPane.showMessageDialog(this, "<html><p>SBML Test Suite V1.0 2008</p></html>", "About SBML Test Suite", JOptionPane.PLAIN_MESSAGE, icon);
    }
    
    public void openHelp() {
       
    String pathToHS = "/testsuitehelp/docs/testsuitehelp-hs.xml";
        //Create a URL for the location of the help set
    
        try {
            
            URL hsURL = getClass().getResource(pathToHS);
            HelpSet hs = new HelpSet(null, hsURL);
            // Create a HelpBroker object for manipulating the help set
            HelpBroker hb = hs.createHelpBroker();
            //Display help set
            hb.setDisplayed(true);
        } catch (Exception ee) {
            // Print info to the console if there is an exception
            System.out.println("HelpSet " + ee.getMessage());
            System.out.println("Help Set " + pathToHS + " not found");
            return;
        }

        
    }

    // General preferences dialog; fed to the OSXAdapter as the method to call when
    // "Preferences..." is selected from the application menu
    public void preferences() {
        //    prefs.setLocation((int)this.getLocation().getX() + 22, (int)this.getLocation().getY() + 22);
        //    prefs.setVisible(true);
        System.out.println("inside the preferences");
        // we want this to contain a checkbox for logging along with editable path for log file
    }

    public void performUpdate() {
        // connect to server and get latest copy of zip file if newer than packaged copy
    }

    public void windowOpened(WindowEvent e) {
        performUpdate();
    //   throw new UnsupportedOperationException("Not supported yet.");
    }

    public void windowClosing(WindowEvent e) {
        if (quit()) {
            System.exit(0);
        }
    //throw new UnsupportedOperationException("Not supported yet.");
    }

    public void updateProgress(int progress, int failed, int skipped, int passed, int finish) {
       
            testTabPane.updateProgress(progress, failed, skipped, passed, finish); 
    }
    public void setSelections(HashMap<String, Object> tconfiguration){
        testTabPane.setConfigurations(tconfiguration);
    }
    public void setStartButton() {
        testTabPane.enableStart();
    }
    public void disableStartButton() {
        testTabPane.disableStart();
    }
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
    public void registerForMacOSXEvents() {
        if (MAC_OS_X) {
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[]) null));
                OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[]) null));
               // OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[]) null));
            //           OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[]{String.class}));
            } catch (Exception e) {
                System.err.println("Error while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }

    public void startTest(TestConfiguration testConfiguration) {
        
      //  final TestConfiguration finalTestConfig = testConfiguration;
   //    TestCaseListModel list = new TestCaseListModel();
        testCaseListModel.removeAllElements();
        failedTestCaseListModel.removeAllElements();
        passedTestCaseListModel.removeAllElements();
        skippedTestCaseListModel.removeAllElements();
        if(logging){
            testConfiguration.set("logging", 1);
        }
        else testConfiguration.set("logging",0);
        TestRunnerWorker worker = new TestRunnerWorker(this, testConfiguration);
        worker.start();
    }

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

    public class checkUpdatesActionClass extends AbstractAction {

        public checkUpdatesActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_U, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            System.out.println("New...");
            // connect to sever here and look for updated tests
        }
    }

    public class quitActionClass extends AbstractAction {

        public quitActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_Q, shortcutKeyMask));
        }

        public void actionPerformed(ActionEvent e) {
            quit();
        }
    }
    
    public class aboutActionClass extends AbstractAction {
        
        public aboutActionClass(String text) {
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_A, shortcutKeyMask));
        }
        
        public void actionPerformed(ActionEvent e){
            about();
        }
    }
    public class helpActionClass extends AbstractAction {
        public helpActionClass(String text){
            super(text);
            putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H,shortcutKeyMask));
        }
        public void actionPerformed(ActionEvent e){
            openHelp();
        }
    }
     
    
    private newActionClass newAction;
    private quitActionClass quitAction;
    private aboutActionClass aboutAction;
    private helpActionClass helpAction;
    private checkUpdatesActionClass checkUpdatesAction;
   
    private JMenu fileMenu;
    private JMenu editMenu;
    //private JMenu helpMenu;
    private JMenuBar menuBar;
    private JMenuItem quitMenuItem;
    private JMenu preferencesMenu;
    private JCheckBoxMenuItem loggingMenuItem;
    private JMenuItem aboutMenuItem;
    private JMenuItem helpMenuItem;
    private JMenuItem newMenuItem;
    private JMenuItem checkUpdatesMenuItem;
    private TestTabPane testTabPane;
}
