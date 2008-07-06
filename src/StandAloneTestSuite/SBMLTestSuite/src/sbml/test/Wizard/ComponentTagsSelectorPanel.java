// @file    ComponentTagSelectorPanel.java
// @brief   ComponentTagSelectorPanel class for SBML Standalone application
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
// Class and methods for the component tag selection from the new test wizard.
//

package sbml.test.Wizard;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.HashMap;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;

public class ComponentTagsSelectorPanel extends WizardPanel implements ItemListener {
    
    private JCheckBox[] buttons;
    private JCheckBox fd_button, rr_button, cmpt_button, ud_button, ar_button, spe_button, ia_button, cst_button, rct_button, asr_button, ewd_button, end_button, par_button;
   // private final String[] buttonNames = {"FunctionDefinition", "RateRule", "Compartment", "UnitDefinition", "AlgebraicRule", "Species", "InitialAssignment", "Constraint", "Reaction", "AssignmentRule", "EventWithDelay", "Parameter", "EventNoDelay"};
    // add something here to get the selections map and make the buttonNames equal to those component tags equal to 0
    public HashMap<String, Object> selections = new HashMap <String, Object>();
    private CreateTestWizard createTestWizard;
    
    
    public ComponentTagsSelectorPanel(CreateTestWizard createTestWizard) {
        this.createTestWizard = createTestWizard;
        initComponents();

    }

    private void initComponents() {
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(getQualifiedName());
        nameLabel.setFont(new Font("Helvetica", Font.BOLD, 16));
        topPanel.add(nameLabel, BorderLayout.PAGE_START);
        topPanel.add(new JSeparator(), BorderLayout.PAGE_END);
        add(topPanel, BorderLayout.PAGE_START);

        JPanel contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);
        
        fd_button = new JCheckBox("FunctionDefinition");
        rr_button = new JCheckBox("RateRule");
        cmpt_button = new JCheckBox("Compartment");
     //   ud_button = new JCheckBox("UnitDefinition");
        ar_button = new JCheckBox("AlgebraicRule");
        spe_button = new JCheckBox("Species");
        ia_button = new JCheckBox("InitialAssignment");
    //    cst_button = new JCheckBox("Constraint");
        rct_button = new JCheckBox("Reaction");
        asr_button = new JCheckBox("AssignmentRule");
        ewd_button = new JCheckBox("EventWithDelay");
        end_button = new JCheckBox("EventNoDelay");
        par_button = new JCheckBox("Parameter");
        
        fd_button.addItemListener(this);
        spe_button.addItemListener(this);
        cmpt_button.addItemListener(this);
        rct_button.addItemListener(this);
        rr_button.addItemListener(this);
    //    ud_button.addItemListener(this);
        ar_button.addItemListener(this);
        ia_button.addItemListener(this);
    //    cst_button.addItemListener(this);
        asr_button.addItemListener(this);
        ewd_button.addItemListener(this);
        end_button.addItemListener(this);
        par_button.addItemListener(this);
        
     /*   buttons = new JCheckBox[buttonNames.length];
        for(int i = 0; i < buttonNames.length; i++) {
            buttons[i] = new JCheckBox(buttonNames[i]);
           // buttonNames[i].addItemListener(this);
        }
     */   // put listeners on the checkboxes that call the validate function 
      
        JPanel contentPanel1 = new JPanel(new GridLayout(0,2,10,10)); 
        
        contentPanel1.add(fd_button);
        contentPanel1.add(rr_button);
        contentPanel1.add(cmpt_button);
     //   contentPanel1.add(ud_button);
        contentPanel1.add(ar_button);
        contentPanel1.add(spe_button);
        contentPanel1.add(ia_button);
     //   contentPanel1.add(cst_button);
        contentPanel1.add(rct_button);
        contentPanel1.add(asr_button);
        contentPanel1.add(ewd_button);
        contentPanel1.add(end_button);
        contentPanel1.add(new JLabel());
        contentPanel1.add(par_button);
        

     /*    for(int i = 0; i < buttonNames.length; i++) {
            if(i != 12) {
                contentPanel1.add(buttons[i]);
            }
            else {
                contentPanel1.add(new JLabel());
                contentPanel1.add(buttons[i]);
            }
        }
        */
        
        JPanel contentPanel2 = new JPanel(new BorderLayout()); 
        contentPanel.add(contentPanel2, BorderLayout.NORTH);
        contentPanel2.add(contentPanel1, BorderLayout.WEST);
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        messagePanel.add(new JLabel("<html>By default all SBML components are tested, to <b>exclude</b> specific components select the comoponent tag to be excluded from the following list:</html>"), BorderLayout.NORTH);
        contentPanel2.add(messagePanel, BorderLayout.NORTH);
        
        if(selections.size() > 0 ){
          //  validateSelections(CreateTestWizard.wselections);
            System.out.println("the hash is bigger than nothing!");
        }
    }
    public void itemStateChanged (ItemEvent ie) {
      selections =  createTestWizard.getSelections();
      Object source = ie.getItemSelectable();
      int state = ie.getStateChange();
      if (source == spe_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("Species",1);
              rct_button.setSelected(true);
              updateSelections("Reaction",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              if(!cmpt_button.isSelected()){
                updateSelections("Species",0);
                createTestWizard.setSelections(selections);
              }
              else{
                  spe_button.setSelected(true);
              }
              
              
              
          }
      }
      if (source == rct_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("Reaction",1);
              
              
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("Reaction",0);
              
              createTestWizard.setSelections(selections);
          }
      }
       if (source == fd_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("FunctionDefinition",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("FunctionDefinition",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == cmpt_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("Compartment",1);
              spe_button.setSelected(true);
              updateSelections("Species",1);
              rct_button.setSelected(true);
              updateSelections("Reaction",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              if((Integer)selections.get("Species")==0){
                updateSelections("Compartment",0);
                createTestWizard.setSelections(selections);
              }
          }
      }
      if (source == rr_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("RateRule",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("RateRule",0);
              createTestWizard.setSelections(selections);
          }
      }
/*      if (source == ud_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("UnitDefinition",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("UnitDefinition",0);
              createTestWizard.setSelections(selections);
          }
      } */
      if (source == ar_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("AlgebraicRule",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("AlgebraicRule",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == ia_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("InitialAssignment",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("InitialAssignment",0);
              createTestWizard.setSelections(selections);
          }
      }
/*      if (source == cst_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("Constraint",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("Constraint",0);
              createTestWizard.setSelections(selections);
          }
      } */
      if (source == asr_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("AssignmentRule",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("AssignmentRule",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == ewd_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("EventWithDelay",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("EventWithDelay",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == end_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("EventNoDelay",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("EventNoDelay",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == par_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("Parameter",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("Parameter",0);
              createTestWizard.setSelections(selections);
          }
      }
        
    }
    
    public void updateSelections (String field, Integer value){
        selections.put(field, value);
    }
    
    public  void validateSelections(HashMap<String,Object> selections){
        // call validateselections during initialization - then each time a button is selected - and then when the user hits next
       // System.out.println("at least inside the validate");
       // Integer i = (Integer)(Integer)selections.get("L2V1radiobutton");
        //System.out.println("i is " + i);
        if((Integer)selections.get("L1V2radiobutton") == 1) {
            
           // System.out.println("inside the l1v2 button if in validate");
            selections.put("FunctionDefinition", 0);
            fd_button.setSelected(false);
            fd_button.setEnabled(false);
            selections.put("InitialAssignment", 0);
            ia_button.setSelected(false);
            ia_button.setEnabled(false);
       //     selections.put("Constraint", 0);
       //     cst_button.setSelected(false);
       //     cst_button.setEnabled(false);
            selections.put("EventWithDelay", 0);
            ewd_button.setSelected(false);
            ewd_button.setEnabled(false);
            selections.put("EventNoDelay", 0);
            end_button.setSelected(false);
            end_button.setEnabled(false);
        }
        else if((Integer)selections.get("L2V1radiobutton") == 1) {
            System.out.println("inside the l2v1 button in validate");
            selections.put("InitialAssignment", 0);
            ia_button.setSelected(false);
            ia_button.setEnabled(false);
       //     selections.put("Constraint", 0);
       //     cst_button.setSelected(false);
       //     cst_button.setEnabled(false);
            
            fd_button.setEnabled(true);
            ewd_button.setEnabled(true);
            end_button.setEnabled(true);
        }
        else {
            fd_button.setEnabled(true);
            ia_button.setEnabled(true);
       //     cst_button.setEnabled(true);
            ewd_button.setEnabled(true);
            end_button.setEnabled(true);
        }
        
  /*      if((Integer)selections.get("Compartment")==1 && (Integer)selections.get("Species")==0) {
            spe_button.setSelected(true);
            updateSelections("Species",1);
        }
 /      
        
        if((Integer)selections.get("FunctionDefinition")==0 && (Integer)selections.get("InitialAssignment")==1 && (Integer)selections.get("AssignmentRule")==1 && (Integer)selections.get("RateRule")==1 && (Integer)selections.get("AlgebraicRule")==1 && (Integer)selections.get("Constraint")==1 && (Integer)selections.get("EventWithDelay")==1 && (Integer)selections.get("EventNoDelay")==1 && (Integer)selections.get("Reaction")==1) {
            // show an alert here that to test function definition one of the others needs to be present as well
            JOptionPane.showMessageDialog(null,"To test FunctionDefinition one of InitialAssignment, AssignmentRule, RateRule, AlgebraicRule, Constraint, EventWithDelay, EventNoDelay or Reaction must be present.","WARNING",JOptionPane.WARNING_MESSAGE);
        }
         
        if((Integer)selections.get("InitialAssignment")==0 && (Integer)selections.get("Compartment")==1 && (Integer)selections.get("Species")==1 && (Integer)selections.get("Parameter")==1) {
            // show an alert here that to test initialassignment at least one of compartment, species or parameter must be present
            JOptionPane.showMessageDialog(null, "To test InitialAssignment at least one of Compartment, Species or Parameter must be present.","WARNING",JOptionPane.WARNING_MESSAGE);
        }
          
        if((Integer)selections.get("AssignmentRule")==0 && (Integer)selections.get("Compartment")==1 && (Integer)selections.get("Species")==1 && (Integer)selections.get("Parameter")==1) {
            // show an alert here that to test assignmentrule at least one of compartment, species or parameter must be present
            JOptionPane.showMessageDialog(null, "To test AssignmentRule at least one of Compartment, Species or Parameter must be present.","WARNING",JOptionPane.WARNING_MESSAGE);
        }
        
        if((Integer)selections.get("RateRule")==0 && (Integer)selections.get("Compartment")==1 && (Integer)selections.get("Species")==1 && (Integer)selections.get("Parameter")==1) {
            // show an alert here that to test rate rule at least one of compartment, species or parameter must be present
            JOptionPane.showMessageDialog(null, "To test RateRule at least one of Compartment, Species or Parameter must be present.","WARNING",JOptionPane.WARNING_MESSAGE);
        }
        
        if((Integer)selections.get("EventWithDelay")==0 && (Integer)selections.get("Compartment")==1 && (Integer)selections.get("Species")==1 && (Integer)selections.get("Parameter")==1) {
            // show an alert here that to test eventwithdelay at least one of compartment, species or parameter must be present
            JOptionPane.showMessageDialog(null, "To test EventWithDelay at least one of Compartment, Species or Parameter must be present.","WARNING",JOptionPane.WARNING_MESSAGE);
        } */
        if((Integer)selections.get("Compartment")==1 && (Integer)selections.get("Species")==1 && (Integer)selections.get("Parameter")==1 && (Integer)selections.get("Reaction")==1) {
            // show an alert here that to test eventnodelay at least one of compartment, species or parameter must be present
            JOptionPane.showMessageDialog(null, "To test at least one of Compartment, Species or Parameter must be present.","WARNING",JOptionPane.WARNING_MESSAGE);
        } 
        
    }

    public String getQualifiedName() {
        return "Component Tags";
    }

    public String getIdentifier() {
        return "CTSELECTOR";
    }

    public String getPrevious() {
        return "TESTTYPE";
    }

    public String getNext() {
        return "TTSELECTOR";
    }

    public boolean isLast() {
        return false;
    }

    public boolean isFirst() {
        return false;
    }

    public boolean mayFinish() {
        return false;
    }
}
