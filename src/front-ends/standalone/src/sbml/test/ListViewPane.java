package sbml.test;

// @file    ListViewPane.java
// @brief   ListViewPane class for SBML Standalone application
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
// Creates the list view pane for the main Stand alone application view.
//
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * This class provides the list view pane of the stand alone SBML Test Suite. It is the default view of the application and contains 
 * a selectable listing of passed, failed and skipped tests with case lists on the left hand side of the pane and a details panel to
 * be filled in when a user selects a particular case from the displayed list.
 * @author Kimberly Begley
 * @version 2.0
 */
public class ListViewPane extends JPanel implements ListSelectionListener, ItemListener {

    /**
     * 
     */
    private JPanel bottomLeftPanel,  selectPanel,  detailPane,  findPanel,  detailContainer;
    private String[] panelTitles = {"Listing selection", "Details", "Jump to test case result"};
    PassedTestCaseListModel passedList;
    FailedTestCaseListModel failedList;
    SkippedTestCaseListModel skippedList;
    JScrollPane scrollList, scrollPane;
    //  JTextPane summaryEditorPane;
    JEditorPane summaryEditorPane;
    JTextField searchField;
    JList testList, passList, failList, skipList;
    JLabel shortDetailLabel, plotLabel, synopsisLabel;
    JRadioButton passedRadioButton, failedRadioButton, skippedRadioButton;

    /**
     * ListViewPane has one constructor that initializes the components of the panel.
     */
    public ListViewPane() {
        super();
        initComponents();
    }

    /**
     * initComponents initializes the viewable panel for the list view tab.
     */
    private void initComponents() {
        setOpaque(false);
        selectPanel = new JPanel();
        selectPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));
        bottomLeftPanel = new JPanel();
        findPanel = new JPanel();



        //    detailPane = new JPanel(new BorderLayout(10, 10));
        detailPane = new JPanel();
        detailPane.setLayout(new BoxLayout(detailPane, BoxLayout.PAGE_AXIS));
        detailContainer = new JPanel(new BorderLayout());


        this.setLayout(new BorderLayout(7, 7));


        bottomLeftPanel.setLayout(new BorderLayout(7, 7));
        this.add(bottomLeftPanel, BorderLayout.CENTER);
        bottomLeftPanel.add(selectPanel, BorderLayout.PAGE_START);
        // bottomLeftPanel.add(scrollPane, BorderLayout.CENTER);
        bottomLeftPanel.add(detailContainer, BorderLayout.CENTER);
        this.add(findPanel, BorderLayout.LINE_START);

        bottomLeftPanel.setOpaque(false);
        this.setOpaque(false);

        findPanel.setLayout(new BorderLayout(5, 10));

        passedList = TestRunnerView.passedTestCaseListModel;
        failedList = TestRunnerView.failedTestCaseListModel;
        skippedList = TestRunnerView.skippedTestCaseListModel;



        passList = new JList(passedList);
        passList.addListSelectionListener(this);
        passList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        passList.setLayoutOrientation(JList.VERTICAL);


        scrollList = new JScrollPane(passList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollList.setPreferredSize(new Dimension(150, 80));

        findPanel.add(scrollList, BorderLayout.CENTER);
        // findPanel.add(new JLabel("<html>Jump to test case result</html>"), BorderLayout.PAGE_START);


        shortDetailLabel = new JLabel("No dataset selected");


        detailPane.setLayout(new BorderLayout(10, 10));
        plotLabel = new JLabel("");
        synopsisLabel = new JLabel("");

        summaryEditorPane = new JEditorPane();
        summaryEditorPane.setEditable(false);
        //    summaryEditorPane.setPreferredSize(new Dimension(525,600));

        //detailPane.add(sdlPanel,BorderLayout.PAGE_START);
        detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
        //detailPane.add(synopsisLabel, BorderLayout.PAGE_END);
        detailPane.add(plotLabel, BorderLayout.WEST);
        //    detailPane.add(summaryEditorPane, BorderLayout.PAGE_END);
        detailPane.setBackground(Color.white);
        summaryEditorPane.setContentType("text/html");
        summaryEditorPane.setText("<html>No dataset selected</html>");


        scrollPane = new JScrollPane(summaryEditorPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setViewPosition(new java.awt.Point(0, 0));
        detailContainer.add(scrollPane, BorderLayout.CENTER);


        //   summaryEditorPane.setLineWrap(true);

        JPanel[] namedPanel = new JPanel[3];
        namedPanel[0] = selectPanel;
        namedPanel[1] = detailContainer;
        namedPanel[2] = findPanel;

        for (int i = 0; i < 3; i++) {
            Border lineborder = new LineBorder(Color.gray, 1, true);
            namedPanel[i].setBorder(
                    BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(lineborder, panelTitles[i]),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            namedPanel[i].setOpaque(false);
        }

        ButtonGroup listingButtonGroup = new ButtonGroup();
        passedRadioButton = new JRadioButton("Passed");
        failedRadioButton = new JRadioButton("Failed");
        skippedRadioButton = new JRadioButton("Skipped");
        passedRadioButton.setSelected(true);
        passedRadioButton.setOpaque(false);
        failedRadioButton.setOpaque(false);
        skippedRadioButton.setOpaque(false);

        // RadioListener radioListener = new RadioListener();
        failedRadioButton.addItemListener(this);
        passedRadioButton.addItemListener(this);
        skippedRadioButton.addItemListener(this);

        listingButtonGroup.add(passedRadioButton);
        listingButtonGroup.add(failedRadioButton);
        listingButtonGroup.add(skippedRadioButton);

        selectPanel.add(passedRadioButton);
        selectPanel.add(failedRadioButton);
        selectPanel.add(skippedRadioButton);

    }

    /**
     * valueChanged records the ListSelectionEvent that occurs when the user selects a test case from selection list.
     * @param e
     */
    public void valueChanged(ListSelectionEvent e) {

        if (e.getValueIsAdjusting() == false) {
            
            JList list = (JList) e.getSource();
            int index = list.getSelectedIndex();              
            updateDetails(index, list);
                
            
        }
    }

    /**
     * updateDetails updates the list in the selection panel to reflect the list selected in the list, called by valueChanged.
     * @param index The index of the list of the selected item.
     * @param list The name of the list in the list selections - ie passed, failed or skipped.
     */
    public void updateDetails(int index, JList list) {
        
        String html_content = "";
        String plotpath = "";
        String description_path = "";

        StringBuffer buffer = new StringBuffer();

        if (list == passList) {
          if(index>=0){
              
            //    shortDetailLabel.setText("<html><p>Test Case " + passedList.getElementAt(index) + "</p><p>Passed!</p></html>");
            buffer.append("<p><b>Test Case</b> " + passedList.getElementAt(index) + "</p><p>Passed!</p>");
            //      detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
            String synopsis = passedList.getRawElementAt(index).getDescription();
            synopsisLabel.setText(synopsis);
            plotpath = passedList.getRawElementAt(index).getPlot();
            description_path = passedList.getRawElementAt(index).getHtml();
          }
          else{
              buffer.append("<p>No dataset selected!</p>");
          }

        } else if (list == failList) {
           if(index>=0) {
            //     shortDetailLabel.setText("<html><p>Test Case " + failedList.getElementAt(index) + "</p><p>Failed at " + failedList.getRawElementAt(index).getResult() + " points</p></html>");
            buffer.append("<p>Test Case " + failedList.getElementAt(index) + "</p><p>Failed at " + failedList.getRawElementAt(index).getResult() + " points</p>");
            //     detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
            String synopsis = failedList.getRawElementAt(index).getDescription();
            synopsisLabel.setText(synopsis);
            plotpath = failedList.getRawElementAt(index).getPlot();
            description_path = failedList.getRawElementAt(index).getHtml();
           } else {
               buffer.append("<p>No dataset selected!</p>");
           }

        } else if (list == skipList) {
           if(index>=0){
            //     shortDetailLabel.setText("<html><p>Test Case " + skippedList.getRawElementAt(index).getTestname() + " </p><p>Skipped: " + skippedList.getRawElementAt(index).getWarnings() + "</p></html>");
            buffer.append("<p>Test Case " + skippedList.getRawElementAt(index).getTestname() + " </p><p>Skipped: " + skippedList.getRawElementAt(index).getWarnings() + "</p>");

            //    detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
            String synopsis = skippedList.getRawElementAt(index).getDescription();
            synopsisLabel.setText(synopsis);
            plotpath = skippedList.getRawElementAt(index).getPlot();
            description_path = skippedList.getRawElementAt(index).getHtml();
           }
           else {
               buffer.append("<p>No dataset selected.</p>");
           }

        }
       if(index >=0){
        try {
            FileInputStream fis = new FileInputStream(description_path);
            int x = fis.available();
            byte b[] = new byte[x];
            fis.read(b);
            html_content = new String(b);

        } catch (FileNotFoundException e) {
            System.out.println("didn't find the html file");
        } catch (IOException e) {
            System.out.println("caught an io exception opening html file");
        }

        buffer.append("<br><p><img src=\"file://" + convertPathToHTML(plotpath) + "\"></img></p><br>");
        buffer.append(html_content);
       }
        summaryEditorPane.setText("<html>" + buffer.toString() + "</html>");


        summaryEditorPane.setCaretPosition(0);

    }

    /**
     * itemStateChanged records the ItemEvent triggered by a change in the radio button selection list.
     * @param e The event object containing details of the change.
     */
    private String convertPathToHTML(String path) {
        if ((System.getProperty("os.name").toLowerCase().startsWith("windows"))) {
            StringBuffer newpath = new StringBuffer("/");
            newpath.append(path.replaceAll("\\\\", "/").replaceAll(" ", "%20"));

            return newpath.toString();
        } else {
            return path.replaceAll(" ", "%20");
        }
    }

    public void itemStateChanged(ItemEvent e) {

        int state = e.getStateChange();
        if (state == ItemEvent.DESELECTED) {
            if (e.getSource() == passedRadioButton) {
                passList.removeListSelectionListener(this);
            }
            if (e.getSource() == failedRadioButton) {
                failList.removeListSelectionListener(this);
            }
            if (e.getSource() == skippedRadioButton) {
                skipList.removeListSelectionListener(this);
            }
        }

        if (state == ItemEvent.SELECTED) {
            if (e.getSource() == failedRadioButton) {

                findPanel.remove(scrollList);
                failList = new JList(failedList);
                failList.addListSelectionListener(this);
                failList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                failList.setLayoutOrientation(JList.VERTICAL);

                scrollList = new JScrollPane(failList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollList.setPreferredSize(new Dimension(150, 80));

                findPanel.add(scrollList, BorderLayout.CENTER);

            }
            if (e.getSource() == passedRadioButton) {

                findPanel.remove(scrollList);
                passList = new JList(passedList);
                passList.addListSelectionListener(this);
                passList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                passList.setLayoutOrientation(JList.VERTICAL);


                scrollList = new JScrollPane(passList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollList.setPreferredSize(new Dimension(150, 80));

                findPanel.add(scrollList, BorderLayout.CENTER);

            }
            if (e.getSource() == skippedRadioButton) {

                findPanel.remove(scrollList);
                skipList = new JList(skippedList);
                skipList.addListSelectionListener(this);
                skipList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
                skipList.setLayoutOrientation(JList.VERTICAL);


                scrollList = new JScrollPane(skipList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
                scrollList.setPreferredSize(new Dimension(150, 80));

                findPanel.add(scrollList, BorderLayout.CENTER);


            }


        }
    }
}

 
