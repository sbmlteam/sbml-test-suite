// @file    TestTagSelectionPanel.java
// @brief   SkippedTestCaseListModel class for SBML Standalone application
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
// Generate panel for the test tag selection in the new test wizard.
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
import javax.swing.JSeparator;


public class TestTagSelectionPanel extends WizardPanel implements ItemListener{

//    private JCheckBox[] buttons;
    private JCheckBox tdc_button, odc_button, zdc_button, ncc_button, nuc_button, mc_button, ia_button, ic_button, hosu_button, bc_button, cs_button, ncp_button, fr_button, rr_button, zr_button, nus_button, sm_button, lp_button, csd_button, cst_button, mu_button, u_button, mml_button, d_button;
    private CreateTestWizard createTestWizard;
//    private final String[] buttonNames = {"2D-Compartment", "HasOnlySubstanceUnits", "StoichiometryMath", "1D-Compartment", "BoundaryCondition", "LocalParameters", "0D-Compartment", "ConstantSpecies", "CSymbolDelay", "NonConstantCompartment", "NonConstantParameter", "CSymbolTime", "NonUnityCompartment", "FastReaction", "MassUnits", "MultiCompartment", "ReversibleReaction", "Units", "InitialAmount", "ZeroRate", "MathML", "InitialConcentration", "NonUnityStoichiometry", "Discontinuity"};
    public HashMap<String, Object> selections = new HashMap <String, Object>();
    
    public TestTagSelectionPanel(CreateTestWizard createTestWizard) {
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

        tdc_button = new JCheckBox("2D-Compartment");
        odc_button = new JCheckBox("1D-Compartment");
        zdc_button = new JCheckBox("0D-Compartment");
        ncc_button = new JCheckBox("NonConstantCompartment");
        nuc_button = new JCheckBox("NonUnityCompartment");
        mc_button = new JCheckBox("MultiCompartment");
        ia_button = new JCheckBox("InitialAmount");
        ic_button = new JCheckBox("InitialConcentration");
        hosu_button = new JCheckBox("HasOnlySubstanceUnits");
        bc_button = new JCheckBox("BoundaryCondition");
        cs_button = new JCheckBox("ConstantSpecies");
        ncp_button = new JCheckBox("NonConstantParameter");
        fr_button = new JCheckBox("FastReaction");
        rr_button = new JCheckBox("ReversibleReaction");
       
        nus_button = new JCheckBox("NonUnitStoichiometry");
        sm_button = new JCheckBox("StoichiometryMath");
        lp_button = new JCheckBox("LocalParameters");
       // csd_button = new JCheckBox("CSymbolDelay");
        cst_button = new JCheckBox("CSymbolTime");
       // mu_button = new JCheckBox("MassUnits");
        //u_button = new JCheckBox("Units");
       
        
        // Add the Item listeners
        tdc_button.addItemListener(this);
        odc_button.addItemListener(this);
        zdc_button.addItemListener(this);
        ncc_button.addItemListener(this);
        nuc_button.addItemListener(this);
        mc_button.addItemListener(this);
        ia_button.addItemListener(this);
        ic_button.addItemListener(this);
        hosu_button.addItemListener(this);
        bc_button.addItemListener(this);
        cs_button.addItemListener(this);
        ncp_button.addItemListener(this);
        fr_button.addItemListener(this);
        rr_button.addItemListener(this);
       
        nus_button.addItemListener(this);
        sm_button.addItemListener(this);
        lp_button.addItemListener(this);
      //  csd_button.addItemListener(this);
        cst_button.addItemListener(this);
      //  mu_button.addItemListener(this);
      //  u_button.addItemListener(this);
       
        
        
        
        
        
      //  buttons = new JCheckBox[buttonNames.length];
     //   for (int i = 0; i < buttonNames.length; i++) {
      //      buttons[i] = new JCheckBox(buttonNames[i]);
      //  }


        JPanel contentPanel1 = new JPanel(new GridLayout(0, 2, 10, 10));

      //  for (int i = 0; i < buttonNames.length; i++) {
      //      contentPanel1.add(buttons[i]);
      //  }
        
        contentPanel1.add(tdc_button);
        contentPanel1.add(odc_button);
        contentPanel1.add(zdc_button);
        contentPanel1.add(ncc_button);
        contentPanel1.add(nuc_button);
        contentPanel1.add(mc_button);
        contentPanel1.add(ia_button);
        contentPanel1.add(ic_button);
        contentPanel1.add(hosu_button);
        contentPanel1.add(bc_button);
        contentPanel1.add(cs_button);
        contentPanel1.add(ncp_button);
        contentPanel1.add(fr_button); 
        contentPanel1.add(rr_button);
     
        contentPanel1.add(nus_button);
        contentPanel1.add(sm_button); 
        contentPanel1.add(lp_button); 
     //   contentPanel1.add(csd_button); 
        contentPanel1.add(cst_button); 
     //   contentPanel1.add(mu_button); 
     //   contentPanel1.add(u_button); 
     


        JPanel contentPanel2 = new JPanel(new BorderLayout());
        contentPanel.add(contentPanel2, BorderLayout.NORTH);
        contentPanel2.add(contentPanel1, BorderLayout.WEST);
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        messagePanel.add(new JLabel("<html>By default all SBML tests are tested to <b>exclude</b> specific tests, select the test tag to be excluded from the following list:"), BorderLayout.NORTH);
        contentPanel2.add(messagePanel, BorderLayout.NORTH);
        
        // call validateSelections

    }
    
    public void itemStateChanged (ItemEvent ie) {
      selections =  createTestWizard.getSelections();
      Object source = ie.getItemSelectable();
      int state = ie.getStateChange();
      
       if (source == tdc_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("2D-Compartment",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("2D-Compartment",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == odc_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("1D-Compartment",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("1D-Compartment",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == zdc_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("0D-Compartment",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("0D-Compartment",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == ncc_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("NonConstantCompartment",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("NonConstantCompartment",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == nuc_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("NonUnityCompartment",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("NonUnityCompartment",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == mc_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("MultiCompartment",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("MultiCompartment",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == ia_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("InitialAmount",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("InitialAmount",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == ic_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("InitialConcentration",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("InitialConcentration",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == hosu_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("HasOnlySubstanceUnits",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("HasOnlySubstanceUnits",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == bc_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("BoundaryCondition",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("BoundaryCondition",0);
              createTestWizard.setSelections(selections);
          }
      }
      if (source == cs_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("ConstantSpecies",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("ConstantSpecies",0);
              createTestWizard.setSelections(selections);
          }
      }
       if (source == ncp_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("NonConstantParameter",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("NonConstantParameter",0);
              createTestWizard.setSelections(selections);
          }
      }
       if (source == fr_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("FastReaction",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("FastReaction",0);
              createTestWizard.setSelections(selections);
          }
      }
      
       if (source == nus_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("NonUnitStoichiometry",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("NonUnitStoichiometry",0);
              createTestWizard.setSelections(selections);
          }
      }
       if (source == sm_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("StoichiometryMath",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("StoichiometryMath",0);
              createTestWizard.setSelections(selections);
          }
      }
       if (source == lp_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("LocalParameters",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("LocalParameters",0);
              createTestWizard.setSelections(selections);
          }
      }
 /*      if (source == csd_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("CSymbolDelay",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("CSymbolDelay",0);
              createTestWizard.setSelections(selections);
          }
      } */
       if (source == cst_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("CSymbolTime",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("CSymbolTime",0);
              createTestWizard.setSelections(selections);
          }
      }
 /*      if (source == mu_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("MassUnits",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("MassUnits",0);
              createTestWizard.setSelections(selections);
          }
      }
       if (source == u_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("Units",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("Units",0);
              createTestWizard.setSelections(selections);
          }
      } */
      
        
    }
    
    public void updateSelections(String field, Integer value){
        selections.put(field, value);
    }
    
    public void validateSelections(HashMap<String, Object> selections){
        if((Integer)selections.get("L1V2radiobutton") == 1) {
         // these are all for test tags - should add them there instead
            selections.put("2D-Compartment",0);
            tdc_button.setEnabled(false);
            selections.put("1D-Compartment",0);
            odc_button.setEnabled(false);
            selections.put("0D-Compartment",0);
            zdc_button.setEnabled(false);
            selections.put("InitialConcentration",0);
            ic_button.setEnabled(false);
            selections.put("HasOnlySubstanceUnits",0);
            hosu_button.setEnabled(false);
            selections.put("ConstantSpecies",0);
            cs_button.setEnabled(false);
            selections.put("StoichiometryMath",0);
            sm_button.setEnabled(false);
            selections.put("CSymbolTime",0);
            cst_button.setEnabled(false);
          //  selections.put("CSymbolDelay",0);
          //  csd_button.setEnabled(false);
          //  selections.put("MassUnits",0);
          //  mu_button.setEnabled(false);
           
        }
        else {
            tdc_button.setEnabled(true);
            odc_button.setEnabled(true);
            zdc_button.setEnabled(true);
            ic_button.setEnabled(true);
            hosu_button.setEnabled(true);
            cs_button.setEnabled(true);
            sm_button.setEnabled(true);
            cst_button.setEnabled(true);
        }
        if((Integer)selections.get("Compartment") == 1) {
            selections.put("2D-Compartment",0);
            tdc_button.setEnabled(false);
            selections.put("1D-Compartment",0);
            odc_button.setEnabled(false);
            selections.put("0D-Compartment",0);
            zdc_button.setEnabled(false);
            selections.put("NonConstantCompartment",0);
            ncc_button.setEnabled(false);
            selections.put("NonUnityCompartment",0);
            nuc_button.setEnabled(false);
            selections.put("MultiCompartment",0);
            mc_button.setEnabled(false);
        }
        else if((Integer)selections.get("Compartment") ==0 && (Integer)selections.get("L1V2radiobutton") == 1) {
            tdc_button.setEnabled(false);
            odc_button.setEnabled(false);
            zdc_button.setEnabled(false);
            ncc_button.setEnabled(true);
            nuc_button.setEnabled(true);
            mc_button.setEnabled(true);
            
        }
        else if((Integer)selections.get("Compartment") ==0 && (Integer)selections.get("L1V2radiobutton") == 0) {
            tdc_button.setEnabled(true);
            odc_button.setEnabled(true);
            zdc_button.setEnabled(true);
            ncc_button.setEnabled(true);
            nuc_button.setEnabled(true);
            mc_button.setEnabled(true);
        }
        if((Integer)selections.get("Species") == 1) {
            selections.put("InitialAmount", 0);
            ia_button.setEnabled(false);
            selections.put("InitialConcentration", 0);
            ic_button.setEnabled(false);
            selections.put("HasOnlySubstanceUnits", 0);
            hosu_button.setEnabled(false);
            selections.put("BoundaryCondition", 0);
            bc_button.setEnabled(false);
            selections.put("ConstantSpecies", 0);
            cs_button.setEnabled(false);
            
        }
        else if((Integer)selections.get("Species") == 0 && (Integer)selections.get("L1V2radiobutton") == 1) {
            ia_button.setEnabled(true);
            cs_button.setEnabled(true);
            bc_button.setEnabled(true);
            hosu_button.setEnabled(false);
            ic_button.setEnabled(false);
        }
        else if((Integer)selections.get("Species") == 0 && (Integer)selections.get("L1V2radiobutton") == 0) {
            ia_button.setEnabled(true);
            cs_button.setEnabled(true);
            bc_button.setEnabled(true);
            hosu_button.setEnabled(true);
            ic_button.setEnabled(true);
        }
        if((Integer)selections.get("Reaction")==1){
            selections.put("FastReaction", 0);
            fr_button.setEnabled(false);
            selections.put("ReversibleReaction", 0);
            rr_button.setEnabled(false);
            
            selections.put("NonUnitStoichiometry", 0);
            nus_button.setEnabled(false);
            selections.put("StoichiometryMath", 0);
            sm_button.setEnabled(false);
            selections.put("LocalParameters", 0);
            lp_button.setEnabled(false);
        }
        else if ((Integer)selections.get("Reaction") == 0 && (Integer)selections.get("L1V2radiobutton") == 0) {
            fr_button.setEnabled(true);
            rr_button.setEnabled(true);
            nus_button.setEnabled(true);
            sm_button.setEnabled(true);
            lp_button.setEnabled(true);
            
        }
         else if ((Integer)selections.get("Reaction") == 0 && (Integer)selections.get("L1V2radiobutton") == 1) {
            fr_button.setEnabled(true);
            rr_button.setEnabled(true);
            nus_button.setEnabled(true);
            sm_button.setEnabled(false);
            lp_button.setEnabled(true);
            
        }
        if((Integer)selections.get("Parameter")==1){
            selections.put("NonConstantParameter", 0);
            ncp_button.setEnabled(false);
        }
        else {
            ncp_button.setEnabled(true);
        }
        if((Integer)selections.get("Compartment")==1 && (Integer)selections.get("Species")==1 && (Integer)selections.get("Reaction")==1 && (Integer)selections.get("Parameter")==1){
            // Disable all test tags is compartment, species, reaction and parameter are omitted
            tdc_button.setEnabled(false);
            odc_button.setEnabled(false);
            zdc_button.setEnabled(false);
            ncc_button.setEnabled(false);
            nuc_button.setEnabled(false);
            mc_button.setEnabled(false);
            ia_button.setEnabled(false);
            ic_button.setEnabled(false);
            hosu_button.setEnabled(false);
            bc_button.setEnabled(false);
            cs_button.setEnabled(false);
            ncp_button.setEnabled(false);
            fr_button.setEnabled(false);
            rr_button.setEnabled(false);
            nus_button.setEnabled(false);
            sm_button.setEnabled(false);
            lp_button.setEnabled(false);
            cst_button.setEnabled(false);
        }
        
        
    }

    public String getQualifiedName() {
        return "Test Tags";
    }

    public String getIdentifier() {
        return "TTSELECTOR";
    }

    public String getPrevious() {
        return "CTSELECTOR";
    }

    public String getNext() {
        
        return "WRAPPER";
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


