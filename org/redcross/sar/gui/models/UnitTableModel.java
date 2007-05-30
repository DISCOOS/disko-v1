package org.redcross.sar.gui.models;

import java.util.EnumSet;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

public class UnitTableModel extends AbstractTableModel implements
		IMsoUpdateListenerIf {

	private static final long serialVersionUID = 1L;
	private EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;
	private ArrayList<ArrayList<IUnitIf>> rows = null;
	private int numColumns = 0;

	public UnitTableModel(IMsoModelIf msoModel, int numColumns) {
		this.numColumns = numColumns;
		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT);
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		rows = new ArrayList<ArrayList<IUnitIf>>(4);
		rows.add(new ArrayList<IUnitIf>(numColumns));
		
		// populate from MSO
		ICmdPostIf cmdPost = msoModel.getMsoManager().getCmdPost();
		Iterator iter = cmdPost.getUnitListItems().iterator();
		while (iter.hasNext()) {
			add((IUnitIf)iter.next());
		}
		super.fireTableDataChanged();
	}
	
	@SuppressWarnings("unchecked")
	private void add(IUnitIf unit) {
		ArrayList row = (ArrayList)rows.get(rows.size()-1);
		if (row.size() == numColumns) {
			row = new ArrayList(numColumns);
			rows.add(row);
		}
		row.add(unit);
	}
	
	private void remove(IUnitIf unit) {
		for (int i = 0; i < rows.size(); i++) {
			ArrayList row = (ArrayList)rows.get(i);
			for (int j = 0; j <  row.size(); j++) {
				if (row.get(j).equals(unit)) {
					row.remove(unit);
				}
			}
		}
	}

	public void handleMsoUpdateEvent(Update e) {
		int type = e.getEventTypeMask();
		IUnitIf unit = (IUnitIf)e.getSource();
		if (type == EventType.ADDED_REFERENCE_EVENT.maskValue()) {
			add(unit);
		}
		else if (type == EventType.REMOVED_REFERENCE_EVENT.maskValue()) {
			remove(unit);
		}
		super.fireTableDataChanged();
	}

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}

	public int getColumnCount() {
		return numColumns;
	}

	public int getRowCount() {
		return rows.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rowIndex < rows.size()) {
			ArrayList row = (ArrayList)rows.get(rowIndex);
			if (columnIndex < row.size()) {
				return row.get(columnIndex);
			}
		}
		return null;
	}
}
