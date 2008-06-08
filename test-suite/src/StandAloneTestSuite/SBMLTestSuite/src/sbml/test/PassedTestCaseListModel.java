

package sbml.test;

import javax.swing.*;
import java.util.Vector;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

public class PassedTestCaseListModel extends AbstractListModel implements ListDataListener {

    Vector<TestResultDetails> passedData;
  

    PassedTestCaseListModel() {
       
        passedData = new Vector<TestResultDetails>();

    }

    public void addElement(TestResultDetails o) {
        passedData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }
    public TestResultDetails remove(int index) {
        TestResultDetails o = passedData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }
    
    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            passedData.removeAllElements();

        }
    }
    public int getSize() {
        return passedData.size();
    }

    public Object getElementAt(int index) {
        return (passedData.get(index)).getTestname();
    }

    public TestResultDetails getRawElementAt(int index) {
        return passedData.get(index);
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

