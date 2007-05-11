package org.redcross.sar.gui.models;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.AssignmentImpl;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

public class AssignmentTableModel extends AbstractTableModel implements
		IMsoUpdateListenerIf {

	private static final long serialVersionUID = 1L;
	private EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;
	private ArrayList rows = null;

	public AssignmentTableModel(IMsoModelIf msoModel) {
		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT);
		rows = new ArrayList();
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		
		// pupulates from mso
		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
		Iterator iter = cmdPost.getAssignmentListItems().iterator();
		while (iter.hasNext()) {
			add((IAssignmentIf)iter.next());
		}
		super.fireTableDataChanged();
	}

	public void handleMsoUpdateEvent(Update e) {
		int type = e.getEventTypeMask();
		IAssignmentIf assignment = (IAssignmentIf)e.getSource();
		if (type == EventType.ADDED_REFERENCE_EVENT.maskValue()) {
			add(assignment);
		}
		else if (type == EventType.REMOVED_REFERENCE_EVENT.maskValue()) {
			remove(assignment);
		}
		super.fireTableDataChanged();
	}
	
	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}
	
	private void add(IAssignmentIf assignment) {
		if (getRow(assignment) == -1) {
			Object[] row = new Object[4];
			row[0] = new Boolean(false);
			row[1] = assignment;
			row[2] = assignment;
			row[3] = new EditAction();
			rows.add(row);
		}
	}
	
	private void remove(IAssignmentIf assignment) {
		int rowIndex = getRow(assignment);
		if (rowIndex > -1) {
			rows.remove(rowIndex);
		}
	}
	
	private int getRow(IAssignmentIf assignment) {
		for (int i = 0; i < rows.size(); i++) {
			Object[] row = (Object[])rows.get(i);
			if (row[1].equals(assignment)) {
				return i;
			}
		}
		return -1;
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return rows.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex >= 0 && rowIndex < rows.size() && 
				columnIndex >= 0 && columnIndex < 4) {
			Object[] row = (Object[])rows.get(rowIndex);
			return row[columnIndex];
		}
		return null;
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch(columnIndex) {
			case 0: return Boolean.class;
			case 1: return AssignmentImpl.class;
			case 2: return AssignmentImpl.class;
			case 3: return EditAction.class;
			default: return Object.class;
		}
	}

	@Override
	public String getColumnName(int column) {
		switch(column) {
		case 0: return "Velg";
		case 1: return "Oppdragsinfo";
		case 2: return "Status";
		case 3: return "Handling";
		default: return null;
	}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		if (columnIndex == 3)  {
			return true;
		}
		return false;
	}

	// dummy class works as placeholder for edit buttons
	public class EditAction {
		public String toString() {
			return null;
		}
	}
}
