package org.redcross.sar.gui.models;

import java.util.EnumSet;

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
	private Object[] rows = null;
	private ICmdPostIf cmdPost = null;

	public AssignmentTableModel(IMsoModelIf msoModel) {
		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT);
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		cmdPost = msoModel.getMsoManager().getCmdPost();
		update(cmdPost.getAssignmentListItems().toArray());
	}

	public void handleMsoUpdateEvent(Update e) {
		int type = e.getEventTypeMask();
		if (type == EventType.ADDED_REFERENCE_EVENT.maskValue() ||
			type == EventType.REMOVED_REFERENCE_EVENT.maskValue() ||
			type == EventType.MODIFIED_DATA_EVENT.maskValue()) {
				update(cmdPost.getAssignmentListItems().toArray());
		}
	}
	
	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}
	
	private void update(Object[] data) {
		rows = new Object[data.length];
		for (int i = 0; i < data.length; i++) {
			IAssignmentIf assignment = (IAssignmentIf)data[i];
			Object[] row = new Object[4];
			row[0] = new Boolean(false);
			row[1] = assignment;
			row[2] = assignment;
			row[3] = new EditAction();
			rows[i] = row;
		}
		super.fireTableDataChanged();
	}

	public int getColumnCount() {
		return 4;
	}

	public int getRowCount() {
		return rows.length;
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Object[] row = (Object[]) rows[rowIndex];
		return row[columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Boolean.class;
		case 1:
			return AssignmentImpl.class;
		case 2:
			return AssignmentImpl.class;
		case 3:
			return EditAction.class;
		default:
			return Object.class;
		}
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Velg";
		case 1:
			return "Oppdragsinfo";
		case 2:
			return "Status";
		case 3:
			return "Handling";
		default:
			return null;
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
