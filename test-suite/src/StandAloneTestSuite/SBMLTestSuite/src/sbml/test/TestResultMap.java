package sbml.test;

import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.MouseListener;
import java.util.Vector;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class TestResultMap extends JPanel implements ListDataListener, ItemSelectable  {
	
	Vector<JPanel> grids;
	TestCaseListModel testCaseListModel;
        int selectedindex;
        
	// the number of squares is static in this example. if you need it to be dynamic
	// you can just modify the constructor and/or the updateData method
	
	TestResultMap(){
            this.testCaseListModel = TestRunnerView.testCaseListModel;
            testCaseListModel.addListDataListener(this);
		setLayout(new GridLayout(0,70,1,1));
		setBackground(Color.gray);
		setBorder(BorderFactory.createLineBorder(Color.black,1));
		grids = new Vector<JPanel>();
		
	}
	

//	
	public void resetDisplay() {
            if(grids.size() > 0){
                grids.removeAllElements();
            }
	}

    public void intervalAdded(ListDataEvent e) {
        for (int i = e.getIndex0(); i <= e.getIndex1(); i++) {
            this.addMapElement(testCaseListModel.getRawElementAt(i).getResult(), i);
            
        }

    }

    public void intervalRemoved(ListDataEvent e) {
        for (int i = e.getIndex1(); i >= e.getIndex0(); i--) {
            System.out.println("removing an element");
            this.removeMapElement(i);

        }
    }

    public void contentsChanged(ListDataEvent e) {
        System.out.println("inside contents changed - testresultmap");
    }

    private void addMapElement(int result, int index) {
    /*    while(index > getMapSize()-1) {
            grids.add(new JPanel());
            ((GridLayout) getLayout()).setRows(getMapSize()/4+1);

        } */
        grids.add(new JPanel());
        add(grids.get(index));
        grids.get(index).setToolTipText("Test case " + (testCaseListModel.getRawElementAt(index).getTestname()));
        grids.get(index).addMouseListener(new FieldListener(index));
        
        if (result == 0) {
            //set it green
            grids.get(index).setBackground(Color.green);
        }
        else if (result > 0) {
            // set it red
            grids.get(index).setBackground(Color.red);
        }
        else if (result == -1) {
            // set it black
            grids.get(index).setBackground(Color.blue);
        } 
        else {
            grids.get(index).setBackground(Color.GRAY);
        }

    }


    private int getMapSize() {
        return grids.size();
    }

    private void removeMapElement(int index) {
        remove(grids.get(index));
        grids.remove(index);
     /*   if (getMapSize() < 200) {
            grids.add(new JPanel());

            grids.get(getMapSize()-1).setBackground(Color.white);
            add(grids.get(getMapSize()-1));
        }
        else
            ((GridLayout) getLayout()).setRows(4);
*/
    }
    
    
    public class FieldListener implements MouseListener {
        int index;
        
        FieldListener (int index) {
            this.index = index;
        }
        
        public void mouseClicked(MouseEvent e) {
            selectedindex = index;
            fireItemEvent(index,true);
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }
        
    }

    public Object[] getSelectedObjects() {
        Object[] returnvalue = new Object[1];
        returnvalue[1] = testCaseListModel.getRawElementAt(selectedindex);
        return returnvalue;
    }
    
    public Object getSelectedObject() {
        return testCaseListModel.getRawElementAt(selectedindex);
    }

    public void addItemListener(ItemListener l) {
        listenerList.add(ItemListener.class, l);
    }

    public void removeItemListener(ItemListener l) {
        listenerList.remove(ItemListener.class, l);
    }

    void fireItemEvent(Object item, boolean sel) {
        //System.out.println("Mouse event fired in field "+item);
        ItemEvent evt = new ItemEvent(this, ItemEvent.ITEM_STATE_CHANGED,
                item, sel ? ItemEvent.SELECTED : ItemEvent.DESELECTED);

        // Get list of listeners
        Object[] listeners = listenerList.getListenerList();
        
        // Send event to all listeners
        for (int i = 0; i < listeners.length; i += 2) {
            
            if (listeners[i] == ItemListener.class) {
                ((ItemListener) listeners[i + 1]).itemStateChanged(evt);
            }
        }
    }
}
