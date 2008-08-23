package sbml.test;

// @file    MapViewPane.java
// @brief   MapViewPane class for SBML Standalone application
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
// Creates the map view for the main page of the SBML stand alone application.
//
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

/**
 * This class provides the map view pane of the stand alone SBML Test Suite. It is selected via tab in the default view of the application and contains 
 * a colour coded map of passed failed and skipped test cases whose details can be viewed by mouse clicking on the icons.
 * 
 * @author Kimberly Begley
 * @version 2.0
 */
public class MapViewPane extends JPanel implements ItemListener {

    private JPanel mainPanel,  bottomLeftPanel,  testResultMapPanel,  detailPane,  upperFindPanel,  detailContainer;
    private String[] panelTitles = {"Test result map", "Details", "Find test case result"};
    TestCaseListModel testCaseListModel;
    JTextField searchField;
    JList testList;
    JLabel shortDetailLabel, plotLabel, synopsisLabel;
    JEditorPane summaryEditorPane;
    TestResultMap resultMap;
    JScrollPane scrollPane;

    /**
     * MapViewPane contains one constructor that calls the initComponents method.
     */
    public MapViewPane() {
        initComponents();
    }

    /**
     * initComponents initializes components for the map view.
     */
    private void initComponents() {
        mainPanel = this;
        testResultMapPanel = new JPanel();
        bottomLeftPanel = new JPanel();
        detailPane = new JPanel();
        detailContainer = new JPanel(new BorderLayout());


        //this.setLayout(new BorderLayout(7, 7));

        //detailPane.setPreferredSize(new Dimension(150,800));
        //add(scrollPane, BorderLayout.CENTER);
        //detailContainer.setLayout(new BorderLayout(10,10));
        // detailContainer.setOpaque(false);

        mainPanel.setLayout(new BorderLayout(7, 7));
        mainPanel.add(bottomLeftPanel, BorderLayout.CENTER);

        bottomLeftPanel.setLayout(new BorderLayout(7, 7));
        bottomLeftPanel.add(testResultMapPanel, BorderLayout.PAGE_START);
        //  bottomLeftPanel.add(scrollPane, BorderLayout.CENTER);
        bottomLeftPanel.add(detailContainer, BorderLayout.CENTER);

        bottomLeftPanel.setOpaque(false);
        mainPanel.setOpaque(false);
        //scrollPane.setOpaque(false);


        resultMap = new TestResultMap();
        testResultMapPanel.add(resultMap);


        testCaseListModel = TestRunnerView.testCaseListModel;
        // sortedModel = new SortedTestCaseListModel(testCaseListModel);

        resultMap.addItemListener(this);

        shortDetailLabel = new JLabel("No dataset selected");
        plotLabel = new JLabel("");
        detailPane.setLayout(new BorderLayout(1, 1));
        //  detailContainer.setLayout(new BorderLayout(10,10));

        summaryEditorPane = new JEditorPane();
        summaryEditorPane.setEditable(false);

        //       summaryEditorPane.setPreferredSize(new Dimension(725,600));
        detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
        detailPane.add(plotLabel, BorderLayout.WEST);
        detailPane.add(summaryEditorPane, BorderLayout.PAGE_END);
        detailPane.setBackground(Color.white);


        scrollPane = new JScrollPane(summaryEditorPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setViewPosition(new java.awt.Point(0, 0));
        detailContainer.add(scrollPane, BorderLayout.CENTER);
        summaryEditorPane.setContentType("text/html");
        summaryEditorPane.setText("<html>No dataset selected</html>");

        // add the plot when the user clicks on a square
        //detailPane.add(new JLabel(new ImageIcon(getClass().getResource("resources/testgraph.jpg"))), BorderLayout.PAGE_END);

        // JScrollPane scrollPane = new JScrollPane(detailPane);
        // add(scrollPane, BorderLayout.CENTER);

        JPanel[] namedPanel = new JPanel[3];
        namedPanel[0] = testResultMapPanel;
        namedPanel[1] = detailContainer;
        //   namedPanel[1] = detailPane;
        //   namedPanel[2] = findPanel;
        //namedPanel[1] = scrollPane;
        Border lineborder = new LineBorder(Color.gray, 1, true);
        testResultMapPanel.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(lineborder, "Test Result Map"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        testResultMapPanel.setOpaque(false);

        detailContainer.setBorder(
                BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(lineborder, "Details"),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));
        detailContainer.setOpaque(false);

    /*        for (int i = 0; i < 2; i++) {
    Border lineborder = new LineBorder(Color.gray, 1, true);
    namedPanel[i].setBorder(
    BorderFactory.createCompoundBorder(
    BorderFactory.createTitledBorder(lineborder, panelTitles[i]),
    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
    namedPanel[i].setOpaque(false);
    }
     */

    }

    /**
     * updateMap resets the map display.
     */
    public void updateMap() {
        summaryEditorPane.setText("<html>No dataset selected</html>");
        resultMap.resetDisplay();
    }

    /**
     * itemStateChanged records the ItemEvent that occurs when an icon has been selected.
     * @param evt
     */
    public void itemStateChanged(ItemEvent evt) {
        int index = (Integer) evt.getItem();
        updateDetails(index);
    }

    /**
     * updateDetails is called when an icon has been selected, it updates the details pane of the view to show the details of the selected icon.
     * @param index
     */
    public void updateDetails(int index) {

        String html_content = "";

        StringBuffer buffer = new StringBuffer("");


        int result = testCaseListModel.getRawElementAt(index).getResult();
        if (result == 0) {
            buffer.append("<p>Test Case " + testCaseListModel.getRawElementAt(index).getTestname() + " </p><p>Passed!</p>");
        }
        if (result > 0) {
            buffer.append("<p>Test Case " + testCaseListModel.getRawElementAt(index).getTestname() + " </p><p>Failed at " + result + " points</p>");
        }
        if (result == -1) {
            buffer.append("<p>Test Case " + testCaseListModel.getRawElementAt(index).getTestname() + " </p><p>Skipped: " + testCaseListModel.getRawElementAt(index).getWarnings() + "</p>");
        }

        //detailPane.setLayout(new BorderLayout(10, 10));

        //    detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
        String description_path = testCaseListModel.getRawElementAt(index).getHtml();
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

        //summaryEditorPane = new JLabel("<html>" + html_content + "</html>");
        String plotpath = testCaseListModel.getRawElementAt(index).getPlot();

        buffer.append("<br><p><img src=\"file://" + convertPathToHTML(plotpath) + "\"></img></p><br>");
        buffer.append(html_content);

        summaryEditorPane.setText("<html>" + buffer.toString() + "</html>");

        summaryEditorPane.setCaretPosition(0);


    }

    private String convertPathToHTML(String path) {
        if ((System.getProperty("os.name").toLowerCase().startsWith("windows"))) {
            StringBuffer newpath = new StringBuffer("/");
            newpath.append(path.replaceAll("\\\\", "/").replaceAll(" ", "%20"));

            return newpath.toString();
        } else {
            return path.replaceAll(" ", "%20");
        }
    }
}
