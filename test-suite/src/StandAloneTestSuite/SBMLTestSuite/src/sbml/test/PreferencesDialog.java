
package sbml.test;

import java.awt.Frame;
import java.io.File;
import java.util.HashMap;
import javax.swing.JDialog;


public class PreferencesDialog extends JDialog {
    HashMap<String, Object> settings;
    /** Creates new form PreferencesDialog */
    public PreferencesDialog(Frame parent, HashMap<String, Object> currentsettings) {
        super(parent, "Preferences",true);
        initComponents();
        this.settings = currentsettings;
        Boolean log = (Boolean)settings.get("logging");
        String path = (String)settings.get("configpath");
        if(log != null)
            loggingCheckBox.setSelected(log);
        if(path != null)
            pathTextField.setText(path);
        this.setLocationRelativeTo(parent);
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        applyButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        loggingCheckBox = new javax.swing.JCheckBox();
        jPanel5 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        pathTextField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setName("Form"); 
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanel1.setName("jPanel1"); 
        jPanel1.setLayout(new java.awt.BorderLayout());

        jPanel2.setName("jPanel2"); 

        cancelButton.setText("Cancel"); 
        cancelButton.setName("cancelButton"); 
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        jPanel2.add(cancelButton);

        applyButton.setText("Apply"); 
        applyButton.setName("applyButton"); 
        applyButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                applyButtonActionPerformed(evt);
            }
        });
        jPanel2.add(applyButton);

        jPanel1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setLayout(new java.awt.BorderLayout());

        jLabel1.setText(""); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N
        jPanel3.add(jLabel1, java.awt.BorderLayout.CENTER);

        jPanel1.add(jPanel3, java.awt.BorderLayout.PAGE_START);

        jPanel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(10, 0, 10, 0));
        jPanel4.setName("jPanel4"); // NOI18N
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.PAGE_AXIS));

        jPanel6.setName("jPanel6"); // NOI18N
        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        loggingCheckBox.setText("Log all activity"); // NOI18N
        loggingCheckBox.setName("loggingCheckBox"); // NOI18N
        jPanel6.add(loggingCheckBox);

        jPanel5.setName("jPanel5"); // NOI18N
        jPanel6.add(jPanel5);

        jPanel4.add(jPanel6);

        jPanel7.setName("jPanel7"); // NOI18N
        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel2.setText("<html>" + "Enter a path for log file.<br>Settings will take effect when the<br>next test is started."); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N
        jPanel7.add(jLabel2);

        pathTextField.setColumns(30);
        String defaultlogpath = System.getProperty("user.home") + File.separator + ".sbmltestrunner" + File.separator + "sbml-test-suite.log";
        pathTextField.setText(defaultlogpath); // NOI18N
        pathTextField.setName("pathTextField"); // NOI18N
        jPanel7.add(pathTextField);

        jPanel4.add(jPanel7);

        jPanel1.add(jPanel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(jPanel1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

private void applyButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_applyButtonActionPerformed
    settings.put("logging", loggingCheckBox.isSelected());
    settings.put("configpath", pathTextField.getText());
    
    this.setVisible(false);// TODO add your handling code here:
}//GEN-LAST:event_applyButtonActionPerformed

private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
    this.setVisible(false);// TODO add your handling code here:
}//GEN-LAST:event_cancelButtonActionPerformed

    public static HashMap<String,Object> getValue(Frame parent, HashMap<String, Object> currentsettings) {
        PreferencesDialog dlg = new PreferencesDialog(parent, currentsettings);
        dlg.setVisible(true);
        return dlg.getReturnValue();
    }

    private HashMap<String,Object> getReturnValue() {
        return settings;
    }
    
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton applyButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JCheckBox loggingCheckBox;
    private javax.swing.JTextField pathTextField;
    // End of variables declaration//GEN-END:variables

}
