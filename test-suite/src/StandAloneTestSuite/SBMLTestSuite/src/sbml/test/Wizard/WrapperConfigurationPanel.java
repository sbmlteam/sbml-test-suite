
package sbml.test.Wizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;


public class WrapperConfigurationPanel extends WizardPanel {

    JTextField wrapCommand;
    JTextField inputPath;
    JTextField outputPath;

    public WrapperConfigurationPanel(CreateTestWizard createTestWizard) {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        wrapCommand = new JTextField(40);
      //  inputPath = new JTextField(40);
      //  outputPath = new JTextField(40);
        
        wrapCommand.setText("/Users/kimberlybegley/bin/my_wrapper %d %n %o");
      //  inputPath.setText("/Users/kimberlybegley/Desktop/caltech/test-suite/cases/semantic");
      //  outputPath.setText("/Users/kimberlybegley/Desktop/output");
        

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(getQualifiedName());
        nameLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        topPanel.add(nameLabel, BorderLayout.PAGE_START);
        topPanel.add(new JSeparator(), BorderLayout.PAGE_END);
        add(topPanel, BorderLayout.PAGE_START);

        JPanel contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        //ButtonGroup buttonGroup = new ButtonGroup();
        JPanel inputPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        JPanel contentPanel1 = new JPanel(new GridLayout(0, 2, 80, 0));
        contentPanel1.add(wrapCommand);
        JPanel contentPanel2 = new JPanel(new BorderLayout());
        contentPanel2.add(contentPanel1, BorderLayout.WEST);
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        messagePanel.add(new JLabel("Please enter the wrapper command for this test:"), BorderLayout.NORTH);
        contentPanel2.add(messagePanel, BorderLayout.NORTH);
        
        
  /*      JPanel inputPathPanel1 = new JPanel(new GridLayout(0, 2, 80, 0));
        inputPathPanel1.add(inputPath);
        JPanel inputPathPanel2 = new JPanel(new BorderLayout());
        inputPathPanel2.add(inputPathPanel1, BorderLayout.WEST);
        JPanel inputPathPanel3 = new JPanel(new BorderLayout());
        inputPathPanel3.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        inputPathPanel3.add(new JLabel("Please enter the input path:"), BorderLayout.NORTH);
        inputPathPanel2.add(inputPathPanel3, BorderLayout.NORTH);        
        
        
        JPanel outputPathPanel1 = new JPanel(new GridLayout(0, 2, 80, 0));
        outputPathPanel1.add(outputPath);
        JPanel outputPathPanel2 = new JPanel(new BorderLayout());
        outputPathPanel2.add(outputPathPanel1, BorderLayout.WEST);
        JPanel outputPathPanel3 = new JPanel(new BorderLayout());
        outputPathPanel3.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        outputPathPanel3.add(new JLabel("Please enter the output path:"), BorderLayout.NORTH);
        outputPathPanel2.add(outputPathPanel3, BorderLayout.NORTH);         
     */   

        inputPanel.add(contentPanel2);
      //  inputPanel.add(inputPathPanel2);
      //  inputPanel.add(outputPathPanel2);
        contentPanel.add(inputPanel, BorderLayout.NORTH);


    }
    
    public String getWrapperPath() {
        return wrapCommand.getText();
    }
    
    public String getInputPath() {
        return inputPath.getText();
    }    
    
    public String getOutputPath() {
        return outputPath.getText();
    }    
    
    
    public String getQualifiedName() {
        return "Configure Wrapper";
    }

    public String getIdentifier() {
        return "WRAPPER";
    }

    public String getPrevious() {
        return "LEVELSELECTOR";
    }

    public String getNext() {
        return null;
    }

    public boolean isLast() {
        return true;
    }

    public boolean isFirst() {
        return false;
    }

    public boolean mayFinish() {
        return true;
    }

    public void validateSelections(HashMap<String, Object> selections) {
        //  throw new UnsupportedOperationException("Not supported yet.");
    }
}
