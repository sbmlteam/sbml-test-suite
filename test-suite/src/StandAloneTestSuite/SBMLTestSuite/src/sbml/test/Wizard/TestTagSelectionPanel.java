
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
        zr_button = new JCheckBox("ZeroRate");
        nus_button = new JCheckBox("NonUnitStoichiometry");
        sm_button = new JCheckBox("StoichiometryMath");
        lp_button = new JCheckBox("LocalParameters");
        csd_button = new JCheckBox("CSymbolDelay");
        cst_button = new JCheckBox("CSymbolTime");
        mu_button = new JCheckBox("MassUnits");
        u_button = new JCheckBox("Units");
        mml_button = new JCheckBox("MathML");
        d_button = new JCheckBox("Discontinuity");
        
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
        zr_button.addItemListener(this);
        nus_button.addItemListener(this);
        sm_button.addItemListener(this);
        lp_button.addItemListener(this);
        csd_button.addItemListener(this);
        cst_button.addItemListener(this);
        mu_button.addItemListener(this);
        u_button.addItemListener(this);
        mml_button.addItemListener(this);
        d_button.addItemListener(this);
        
        
        
        
        
      //  buttons = new JCheckBox[buttonNames.length];
     //   for (int i = 0; i < buttonNames.length; i++) {
      //      buttons[i] = new JCheckBox(buttonNames[i]);
      //  }


        JPanel contentPanel1 = new JPanel(new GridLayout(0, 3, 10, 10));

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
        contentPanel1.add(zr_button);
        contentPanel1.add(nus_button);
        contentPanel1.add(sm_button); 
        contentPanel1.add(lp_button); 
        contentPanel1.add(csd_button); 
        contentPanel1.add(cst_button); 
        contentPanel1.add(mu_button); 
        contentPanel1.add(u_button); 
        contentPanel1.add(mml_button); 
        contentPanel1.add(d_button); 


        JPanel contentPanel2 = new JPanel(new BorderLayout());
        contentPanel.add(contentPanel2, BorderLayout.NORTH);
        contentPanel2.add(contentPanel1, BorderLayout.WEST);
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 15, 0));
        messagePanel.add(new JLabel("Select test tags you would like to exclude:"), BorderLayout.NORTH);
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
       if (source == zr_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("ZeroRate",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("ZeroRate",0);
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
       if (source == csd_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("CSymbolDelay",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("CSymbolDelay",0);
              createTestWizard.setSelections(selections);
          }
      }
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
       if (source == mu_button){
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
      }
       if (source == mml_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("MathML",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("MathML",0);
              createTestWizard.setSelections(selections);
          }
      }
       if (source == d_button){
          if(state == ItemEvent.SELECTED){
              updateSelections("Discontinuity",1);
              createTestWizard.setSelections(selections);
          }
          if(state == ItemEvent.DESELECTED){
              updateSelections("Discontinuity",0);
              createTestWizard.setSelections(selections);
          }
      } 
        
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
            selections.put("CSymbolDelay",0);
            csd_button.setEnabled(false);
            selections.put("MassUnits",0);
            mu_button.setEnabled(false);
            selections.put("MathML",0);
            mml_button.setEnabled(false);
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
        if((Integer)selections.get("Reaction")==1){
            selections.put("FastReaction", 0);
            fr_button.setEnabled(false);
            selections.put("ReversibleReaction", 0);
            rr_button.setEnabled(false);
            selections.put("ZeroRate", 0);
            zr_button.setEnabled(false);
            selections.put("NonUnitStoichiometry", 0);
            nus_button.setEnabled(false);
            selections.put("StoichiometryMath", 0);
            sm_button.setEnabled(false);
            selections.put("LocalParameters", 0);
            lp_button.setEnabled(false);
        }
        if((Integer)selections.get("Parameter")==1){
            selections.put("NonConstantParameter", 0);
            ncp_button.setEnabled(false);
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


