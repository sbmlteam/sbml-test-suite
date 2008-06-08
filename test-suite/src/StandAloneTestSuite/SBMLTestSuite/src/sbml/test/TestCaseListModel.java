
package sbml.test;

import java.util.Vector;
import javax.swing.AbstractListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class TestCaseListModel extends AbstractListModel implements ListDataListener {

    Vector<TestResultDetails> rawData;

    public TestCaseListModel() {
        rawData = new Vector<TestResultDetails>();
    }

    public TestResultDetails remove(int index) {
        TestResultDetails o = rawData.remove(index);
        fireContentsChanged(this, getSize() - 1, getSize() - 1);
        return o;
    }

    public void addElement(TestResultDetails o) {
        rawData.add(o);
        fireIntervalAdded(this, getSize() - 1, getSize() - 1);

    }

    public void removeAllElements() {
        if (getSize() > 0  ) {
            this.fireIntervalRemoved(this, 0, getSize() - 1);
            rawData.removeAllElements();

        }
    }

    public int getSize() {
        return rawData.size();
    }

    public Object getElementAt(int index) {
        return rawData.get(index).getTestname();
    }

    TestResultDetails getRawElementAt(int index) {
        return rawData.get(index);
    }

    public void intervalAdded(ListDataEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void intervalRemoved(ListDataEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }

    public void contentsChanged(ListDataEvent e) {
       // throw new UnsupportedOperationException("Not supported yet.");
    }
}
