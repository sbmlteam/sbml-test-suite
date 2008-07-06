
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
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


public class ListViewPane extends JPanel implements ListSelectionListener, ItemListener {

    private JPanel bottomLeftPanel,  selectPanel,  detailPane,  findPanel, detailContainer;
    private String[] panelTitles = {"Listing selection", "Details", "Jump to test case result"};
    
    PassedTestCaseListModel passedList;
    FailedTestCaseListModel failedList;
    SkippedTestCaseListModel skippedList;
    
    JScrollPane scrollList, scrollPane;
    JTextPane summaryLabel;

    JTextField searchField;
    JList testList, passList, failList, skipList;
    JLabel shortDetailLabel, plotLabel, synopsisLabel;
    JRadioButton passedRadioButton, failedRadioButton, skippedRadioButton;

    public ListViewPane() {
        super();
        initComponents();
    }

    private void initComponents() {
        setOpaque(false);
        selectPanel = new JPanel();
        selectPanel.setLayout(new FlowLayout(FlowLayout.CENTER,20,0));
        bottomLeftPanel = new JPanel();
        findPanel = new JPanel();

        
        
        detailPane = new JPanel();
        detailContainer = new JPanel(new BorderLayout());
        
        scrollPane = new JScrollPane(detailPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setViewPosition(new java.awt.Point(0,0));
        this.setLayout(new BorderLayout(7, 7));
        
        detailContainer.add(scrollPane, BorderLayout.CENTER);

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
        
        summaryLabel = new JTextPane();
        summaryLabel.setPreferredSize(new Dimension(525,600));
        
        //detailPane.add(sdlPanel,BorderLayout.PAGE_START);
        detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
        //detailPane.add(synopsisLabel, BorderLayout.PAGE_END);
        detailPane.add(plotLabel, BorderLayout.WEST);
        detailPane.add(summaryLabel, BorderLayout.PAGE_END);
        detailPane.setBackground(Color.white);
       
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

    public void valueChanged(ListSelectionEvent e) {

        if(e.getValueIsAdjusting() == false) {
            JList list = (JList) e.getSource();
            int index = list.getSelectedIndex();
            updateDetails(index, list);
        }
    }
    public void updateDetails(int index, JList list){
      detailPane.remove(shortDetailLabel);
      detailPane.remove(plotLabel);
      detailPane.remove(summaryLabel);
      //detailPane.remove(synopsisLabel);
      String html_content = "";
      String plotpath = "";
      String description_path = "";
      
      
      if(list == passList){
            shortDetailLabel = new JLabel("<html><p>Test Case " + passedList.getElementAt(index) + "</p><p>Passed!</p></html>");
            detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
            String synopsis = passedList.getRawElementAt(index).getDescription(); 
            synopsisLabel = new JLabel(synopsis);
            plotpath = passedList.getRawElementAt(index).getPlot();
            description_path = passedList.getRawElementAt(index).getHtml();
           
      }
      else if(list == failList){
            shortDetailLabel = new JLabel("<html><p>Test Case " + failedList.getElementAt(index) + "</p><p>Failed at " + failedList.getRawElementAt(index).getResult() + " points</p></html>");
            detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
            String synopsis = failedList.getRawElementAt(index).getDescription(); 
            synopsisLabel = new JLabel(synopsis);
            plotpath = failedList.getRawElementAt(index).getPlot();
            description_path = failedList.getRawElementAt(index).getHtml();
            
      }
      else if(list == skipList){
            shortDetailLabel = new JLabel("<html><p>Test Case " + skippedList.getRawElementAt(index).getTestname() + " </p><p>Skipped: " + skippedList.getRawElementAt(index).getWarnings() + "</p></html>");
            detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
            String synopsis = skippedList.getRawElementAt(index).getDescription(); 
            synopsisLabel = new JLabel(synopsis);
            plotpath = skippedList.getRawElementAt(index).getPlot();
            description_path = skippedList.getRawElementAt(index).getHtml();
            
      }
      
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
      summaryLabel.setContentType("text/html");
      summaryLabel.setText("<html>" + html_content + "</html>");
      
      plotLabel = new JLabel(new ImageIcon(plotpath));
      detailPane.add(summaryLabel, BorderLayout.PAGE_END);
     // detailPane.add(synopsisLabel, BorderLayout.PAGE_END);
      detailPane.add(plotLabel, BorderLayout.WEST);
      scrollPane.getViewport().setViewPosition(new java.awt.Point(0,0));
      
  }

    public void itemStateChanged(ItemEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
      int state = e.getStateChange();
      if(state == ItemEvent.DESELECTED){
          if(e.getSource() == passedRadioButton){
              passList.removeListSelectionListener(this);
          }
          if(e.getSource() == failedRadioButton){
              failList.removeListSelectionListener(this);
          }
          if(e.getSource() == skippedRadioButton){
              skipList.removeListSelectionListener(this);
          }
      }
      
      if(state == ItemEvent.SELECTED) {
        if(e.getSource() == failedRadioButton) {
            
            findPanel.remove(scrollList);
            failList = new JList(failedList);
            failList.addListSelectionListener(this);
            failList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            failList.setLayoutOrientation(JList.VERTICAL);

            scrollList = new JScrollPane(failList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollList.setPreferredSize(new Dimension(150, 80));

            findPanel.add(scrollList, BorderLayout.CENTER);
       
        }
        if(e.getSource() == passedRadioButton){
            
            findPanel.remove(scrollList);
            passList = new JList(passedList);
            passList.addListSelectionListener(this);
            passList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            passList.setLayoutOrientation(JList.VERTICAL);


            scrollList = new JScrollPane(passList, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollList.setPreferredSize(new Dimension(150, 80));

            findPanel.add(scrollList, BorderLayout.CENTER);
            
        }
        if(e.getSource() == skippedRadioButton){
            
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
}}

 
