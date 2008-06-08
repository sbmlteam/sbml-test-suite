
package sbml.test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.JOptionPane;
// import java.awt.Desktop;

public class TestTabPane extends JPanel implements DocumentListener, ListSelectionListener {

    private JPanel buttonPanel;
    private JTabbedPane tabbedPane;
    private MapViewPane mapViewPane;
    private ListViewPane listViewPane;
    TestRunnerWorker testRunnerWorker;
  //  TestCaseListModel testCaseListModel = new TestCaseListModel();
   // JList testList;
    private JLabel passedLabel,  skippedLabel,  failedLabel;
    JProgressBar progressBar;
    public static boolean MAC_OS_X = (System.getProperty("os.name").toLowerCase().startsWith("mac os x"));
   
    public TestTabPane() {
        super();

        createGUI();
        setPreferredSize(new Dimension(850, 700));

    }

    private void createGUI() {
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

        JButton startButton = new JButton(new ImageIcon(getClass().getResource("resources/start.png")));

        if (MAC_OS_X) {
            startButton.putClientProperty("JButton.buttonType", "segmentedCapsule");
            startButton.putClientProperty("JButton.segmentPosition", "first");
            startButton.putClientProperty("JComponent.sizeVariant", "regular");
        }
        startButton.setToolTipText("Resume");
        startButton.setAction(new startActionClass("", new ImageIcon(getClass().getResource("resources/start.png"))));

        JButton stopButton = new JButton(new ImageIcon(getClass().getResource("resources/stop.png")));

        if (MAC_OS_X) {
            stopButton.putClientProperty("JButton.buttonType", "segmentedCapsule");
            stopButton.putClientProperty("JButton.segmentPosition", "last");
            stopButton.putClientProperty("JComponent.sizeVariant", "regular");
        }

        stopButton.setToolTipText("Stop");
        stopButton.setAction(new stopActionClass("", new ImageIcon(getClass().getResource("resources/stop.png"))));

        JButton resetButton = new JButton(new ImageIcon(getClass().getResource("resources/reset.png")));

        if (MAC_OS_X) {
            resetButton.putClientProperty("JButton.buttonType", "segmentedCapsule");
            resetButton.putClientProperty("JButton.segmentPosition", "last");
            resetButton.putClientProperty("JComponent.sizeVariant", "regular");
        }

        resetButton.setToolTipText("Reset");
        resetButton.setAction(new resetActionClass("", new ImageIcon(getClass().getResource("resources/reset.png"))));


        controlButtons.add(startButton);
        controlButtons.add(stopButton);
//        controlButtons.add(resetButton);
        //    Box layoutBox = Box.createHorizontalBox();
        JPanel layoutBox = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 0));
        layoutBox.setOpaque(false);

        progressBar = new JProgressBar();
        progressBar.setString("0%");
        //     progressBar.setValue(100);
        progressBar.setStringPainted(true);

        //  progressBar.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        progressBar.setIndeterminate(true);
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

    private void updateDisplay(TestResultDetails data) {
        //      resultMap.updateData(data);
        //       shortDetailLabel.setText("You are viewing dataset no. " + data.getName());
    }

    public void updateData(TestResultDetails[] data) {
            //  testCaseListModel.updateData(data);
    }

    public void insertUpdate(DocumentEvent ev) {
        System.out.println("inside the insertupdate - testtabpane");
        //     testCaseListModel.setSearchString(searchField.getText());
    }

    public void removeUpdate(DocumentEvent ev) {
        //     testCaseListModel.setSearchString(searchField.getText());
    }

    public void changedUpdate(DocumentEvent ev) {
    }

    public void valueChanged(ListSelectionEvent e) {
        System.out.println("inside the valuechanged - testtabpane");
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

    public void updateProgress(int progress, int failed, int skipped, int passed) {
        progressBar.setValue(progress);
        progressBar.setString(Integer.toString(progress)+"%");
        failedLabel.setText(Integer.toString(failed) + " ("+ Integer.toString(100*failed/(failed+passed+skipped))+"%)");
        skippedLabel.setText(Integer.toString(skipped) + " ("+ Integer.toString(100*skipped/(failed+passed+skipped))+"%)");
        passedLabel.setText(Integer.toString(passed) + " ("+ Integer.toString(100*passed/(failed+passed+skipped))+"%)"); 
    }

    public class settingsActionClass extends AbstractAction {

        public settingsActionClass(String text, Icon icon) {
            super(text, icon);
        //         putValue(ACCELERATOR_KEY, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            //  JFrame mainFrame = TestRunnerApp.getApplication().getMainFrame();
            String outputdir = testRunnerWorker.getOdir();
            String testdir = testRunnerWorker.getTdir();
            String message = "<html><p>Settings</p><p>Test Case Directory: " + testdir + "</p><p>Wrapper Output Directory: " + outputdir + "</p></html>";
            JOptionPane.showMessageDialog(null, message, "Settings...", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public class startActionClass extends AbstractAction {

        public startActionClass(String text, Icon icon) {
            super(text, icon);
        //         putValue(ACCELERATOR_KEY, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            //     JFrame mainFrame = TestRunnerApp.getApplication().getMainFrame();
            JOptionPane.showMessageDialog(null, "Nothing to resume.", "Resume...", JOptionPane.WARNING_MESSAGE);
        }
    }

    public class stopActionClass extends AbstractAction {

        public stopActionClass(String text, Icon icon) {
            super(text, icon);
        //         putValue(ACCELERATOR_KEY, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            //   JFrame mainFrame = TestRunnerApp.getApplication().getMainFrame();
            JOptionPane.showMessageDialog(null, "Nothing to stop.", "Stop...", JOptionPane.WARNING_MESSAGE);
        }
    }

    public class resetActionClass extends AbstractAction {

        public resetActionClass(String text, Icon icon) {
            super(text, icon);
        //         putValue(ACCELERATOR_KEY, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            //     JFrame mainFrame = TestRunnerApp.getApplication().getMainFrame();
            JOptionPane.showMessageDialog(null, "Nothing to reset.", "Reset...", JOptionPane.WARNING_MESSAGE);
        }
    }

    public class openBrowserActionClass extends AbstractAction {

        public openBrowserActionClass(String text, Icon icon) {
            super(text, icon);
        //         putValue(ACCELERATOR_KEY, shortcut);
        }

        public void actionPerformed(ActionEvent e) {
            //       JFrame mainFrame = TestRunnerApp.getApplication().getMainFrame();
            JOptionPane.showMessageDialog(null, "Not implemented.", "Open Webbrowser...", JOptionPane.WARNING_MESSAGE);
//            try {
//                Desktop.browse(new URL("http://www.mindprod.com/index.html"));
//            } catch (MalformedURLException e1) {
//                e1.printStackTrace();
//            } catch (DesktopException e2) {
//                e2.printStackTrace();
//            }
        }
    }
}
