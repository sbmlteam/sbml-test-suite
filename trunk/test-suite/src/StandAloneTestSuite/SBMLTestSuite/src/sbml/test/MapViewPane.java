
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
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;


public class MapViewPane extends JPanel implements ItemListener {

    private JPanel mainPanel,  bottomLeftPanel,  testResultMapPanel,  detailPane, upperFindPanel, detailContainer;
    private String[] panelTitles = {"Test result map", "Details", "Find test case result"};
    TestCaseListModel testCaseListModel;
    
    JTextField searchField;
    JList testList;
    JLabel shortDetailLabel, plotLabel, synopsisLabel;
    
    JTextPane summaryLabel;
    TestResultMap resultMap;
    
    JScrollPane scrollPane;

    public MapViewPane() {
        initComponents();
    }

    private void initComponents() {
        mainPanel = this;
        testResultMapPanel = new JPanel();
        bottomLeftPanel = new JPanel();
        detailPane = new JPanel();
        detailContainer = new JPanel(new BorderLayout());
        
        scrollPane = new JScrollPane(detailPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setViewPosition(new java.awt.Point(0,0));
        //this.setLayout(new BorderLayout(7, 7));
        
        //detailPane.setPreferredSize(new Dimension(150,800));
        //add(scrollPane, BorderLayout.CENTER);
        //detailContainer.setLayout(new BorderLayout(10,10));
        detailContainer.add(scrollPane, BorderLayout.CENTER);
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
        
        summaryLabel = new JTextPane();
        summaryLabel.setPreferredSize(new Dimension(725,600));
        detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
        detailPane.add(plotLabel,BorderLayout.WEST);
        detailPane.add(summaryLabel,BorderLayout.PAGE_END);
        detailPane.setBackground(Color.white);
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

  public void updateMap() {
      resultMap.resetDisplay();
  }

  public void itemStateChanged(ItemEvent evt) {
        int index = (Integer)evt.getItem();
        updateDetails(index);
    }

  public void updateDetails(int index) {
      detailPane.remove(shortDetailLabel);
      detailPane.remove(plotLabel);
      detailPane.remove(summaryLabel);
      String html_content = "";
      
      int result = testCaseListModel.getRawElementAt(index).getResult();
      if(result == 0) {
        shortDetailLabel = new JLabel("<html><p>Test Case " + testCaseListModel.getRawElementAt(index).getTestname() + " </p><p>Passed!</p></html>");
      }
      if(result > 0) {
          shortDetailLabel = new JLabel("<html><p>Test Case " + testCaseListModel.getRawElementAt(index).getTestname() + " </p><p>Failed at " + result +" points</p></html>");
      }
      if(result == -1) {
          shortDetailLabel = new JLabel("<html><p>Test Case " + testCaseListModel.getRawElementAt(index).getTestname() + " </p><p>Skipped: " + testCaseListModel.getRawElementAt(index).getWarnings() + "</p></html>");
      }
      
        //detailPane.setLayout(new BorderLayout(10, 10));

      detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
      String description_path = testCaseListModel.getRawElementAt(index).getHtml();
      try {
        FileInputStream fis = new FileInputStream(description_path);
        int x = fis.available();
        byte b[] = new byte[x];
        fis.read(b);
        html_content = new String(b);
       
      }
      catch(FileNotFoundException e){
          System.out.println("didn't find the html file");
      }
      catch(IOException e) {
          System.out.println("caught an io exception opening html file");
      }
      
      //summaryLabel = new JLabel("<html>" + html_content + "</html>");
      String plotpath = testCaseListModel.getRawElementAt(index).getPlot();
      summaryLabel.setContentType("text/html");
      summaryLabel.setText("<html>" + html_content + "</html>");
     
      detailPane.add(summaryLabel, BorderLayout.PAGE_END);
      
      plotLabel = new JLabel(new ImageIcon(plotpath));
      detailPane.add(plotLabel, BorderLayout.WEST);
      scrollPane.getViewport().setViewPosition(new java.awt.Point(0,0));
      
  }
    


   

}
