

package sbml.test;

import javax.swing.*;
import java.util.Vector;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class SkippedTestCaseListModel extends AbstractListModel implements ListDataListener{
    
    
    Vector<TestResultDetails> skippedData;
    
    
    SkippedTestCaseListModel() {
        
        skippedData = new Vector<TestResultDetails>();        

    }
    
     public void addElement(TestResultDetails o) {
        skippedData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }
    public TestResultDetails remove(int index) {
        TestResultDetails o = skippedData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }
    
    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            skippedData.removeAllElements();

        }
    }

    public int getSize() {
        return skippedData.size();
    }

    public Object getElementAt(int index) {
        return (skippedData.get(index)).getTestname();
    }
    
    public TestResultDetails getRawElementAt(int index) {
        return skippedData.get(index);
    }

    public void intervalAdded(ListDataEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void intervalRemoved(ListDataEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void contentsChanged(ListDataEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}

