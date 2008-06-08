
package sbml.test;

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

    private JPanel mainPanel,  bottomLeftPanel,  testResultMapPanel,  detailPane, upperFindPanel;
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
        scrollPane = new JScrollPane(detailPane);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.getViewport().setViewPosition(new java.awt.Point(0,0));
        
        //detailPane.setPreferredSize(new Dimension(150,800));
        //add(scrollPane, BorderLayout.CENTER);
        
        mainPanel.setLayout(new BorderLayout(7, 7));
        mainPanel.add(bottomLeftPanel, BorderLayout.CENTER);
        
        bottomLeftPanel.setLayout(new BorderLayout(7, 7));
        bottomLeftPanel.add(testResultMapPanel, BorderLayout.PAGE_START);
        bottomLeftPanel.add(scrollPane, BorderLayout.CENTER);

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
        detailPane.setLayout(new BorderLayout(10, 10));
        
        summaryLabel = new JTextPane();
        summaryLabel.setPreferredSize(new Dimension(725,400));
        detailPane.add(shortDetailLabel, BorderLayout.PAGE_START);
        detailPane.add(plotLabel,BorderLayout.WEST);
        detailPane.add(summaryLabel,BorderLayout.PAGE_END);
        // add the plot when the user clicks on a square
        //detailPane.add(new JLabel(new ImageIcon(getClass().getResource("resources/testgraph.jpg"))), BorderLayout.PAGE_END);
        
       // JScrollPane scrollPane = new JScrollPane(detailPane);
       // add(scrollPane, BorderLayout.CENTER);

        JPanel[] namedPanel = new JPanel[3];
        namedPanel[0] = testResultMapPanel;
        namedPanel[1] = detailPane;
     //   namedPanel[2] = findPanel;

        for (int i = 0; i < 2; i++) {
            Border lineborder = new LineBorder(Color.gray, 1, true);
            namedPanel[i].setBorder(
                    BorderFactory.createCompoundBorder(
                    BorderFactory.createTitledBorder(lineborder, panelTitles[i]),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)));
            namedPanel[i].setOpaque(false);
        }

       
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
