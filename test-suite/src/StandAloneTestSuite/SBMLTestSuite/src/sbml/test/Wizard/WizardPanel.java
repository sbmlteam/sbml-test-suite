

package sbml.test.Wizard;

import java.beans.PropertyChangeSupport;
import javax.swing.JPanel;


public abstract class WizardPanel extends JPanel implements WizardPanelInterface {
    
    protected final PropertyChangeSupport propertyChangeSupport;
    
    public WizardPanel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    
    
}
