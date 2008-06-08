

package sbml.test;

import javax.swing.*;
import java.util.Vector;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class FailedTestCaseListModel extends AbstractListModel implements ListDataListener{
    
    
    Vector<TestResultDetails> failedData;
    //TestCaseListModel testCaseListModel;
    
    FailedTestCaseListModel() {
       // this.testCaseListModel = testCaseListModel;
       // testCaseListModel.addListDataListener(this);
        failedData = new Vector<TestResultDetails>();
        

    }
    public void addElement(TestResultDetails o) {
        failedData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }
    public TestResultDetails remove(int index) {
        TestResultDetails o = failedData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }
    
    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            failedData.removeAllElements();

        }
    }

    public int getSize() {
        return failedData.size();
    }

    public Object getElementAt(int index) {
        return (failedData.get(index)).getTestname();
    }
    
    public TestResultDetails getRawElementAt(int index) {
        return failedData.get(index);
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
