

package sbml.test.Wizard;

import java.util.HashMap;


public interface WizardPanelInterface {
 public void validateSelections(HashMap<String, Object> selections);
    
 public String getQualifiedName();
 
 public String getIdentifier();
 
 public String getPrevious();
 
 public String getNext();
 
 public boolean isLast();
 
 public boolean isFirst();
 
 public boolean mayFinish();
}
