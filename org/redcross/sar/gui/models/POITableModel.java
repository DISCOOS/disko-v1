package org.redcross.sar.gui.models;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

import javax.swing.table.AbstractTableModel;

import org.redcross.sar.app.Utils;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.IAreaIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPOIIf;
import org.redcross.sar.mso.data.IPOIIf.POIType;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent;
import org.redcross.sar.mso.event.MsoEvent.EventType;
import org.redcross.sar.mso.event.MsoEvent.Update;

public class POITableModel extends AbstractTableModel implements
		IMsoUpdateListenerIf {

	private static final long serialVersionUID = 1L;
	private EnumSet<IMsoManagerIf.MsoClassCode> myInterests = null;
	private IAreaIf area = null;
	private ArrayList<Object[]> rows = null;
	
	public POITableModel(IMsoModelIf msoModel) {
		myInterests = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_POI);
		IMsoEventManagerIf msoEventManager = msoModel.getEventManager();
		msoEventManager.addClientUpdateListener(this);
		rows = new ArrayList<Object[]>();
	}
	
	public void setArea(IAreaIf area) {
		this.area = area;
		if (area != null) {
			Iterator iter = area.getAreaPOIs().getItems().iterator();
			while (iter.hasNext()) {
				IPOIIf poi = (IPOIIf)iter.next();
				add(poi);
			}
		}
		else {
			if (rows != null) {
				rows.clear();
			}
		}
		super.fireTableDataChanged();
	}

	public void handleMsoUpdateEvent(Update e) {
		int mask = e.getEventTypeMask();
		boolean addedReference = (mask & MsoEvent.EventType.ADDED_REFERENCE_EVENT.maskValue()) != 0;
        boolean deletedObject  = (mask & MsoEvent.EventType.DELETED_OBJECT_EVENT.maskValue()) != 0;
        boolean modifiedObject = (mask & MsoEvent.EventType.MODIFIED_DATA_EVENT.maskValue()) != 0;
		IPOIIf poi = (IPOIIf)e.getSource();
		
		if (addedReference && area != null && area.getAreaPOIs().contains(poi)) {
			add(poi);
			super.fireTableDataChanged();
		}
		else if (deletedObject) {
			remove(poi);
			super.fireTableDataChanged();
		}
		else if (modifiedObject && area != null && area.getAreaPOIs().contains(poi)) {
			int index = getRow(poi);
			if (index > -1) {
				update(poi,rows.get(index));
				super.fireTableDataChanged();
			}
		}
	}

	public boolean hasInterestIn(IMsoObjectIf aMsoObject) {
		return myInterests.contains(aMsoObject.getMsoClassCode());
	}
	
	private void update(IPOIIf poi, Object[] row) {
		row[0] = new Integer(rows.size());
		row[1] = Utils.translate(poi.getType());
		row[2] = poi.getRemarks();
		row[3] = poi;
	}
	
	private void updateSortNumber() {
		for (int i = 0; i < rows.size(); i++) {
			Object[] row = rows.get(i);
			row[0] = i;
		}
	}
	
	private void add(IPOIIf poi) {
		if (getRow(poi) == -1) {
			Object[] row = new Object[4];
			if (poi.getType() == POIType.START)
				rows.add(0, row);
			else if (poi.getType() == POIType.STOP) {
				rows.add(row);
			}
			else {
				if (rows.size() > 1)
					rows.add(rows.size()-1, row);
				else rows.add(row);
			}
			update(poi,row);
			updateSortNumber();
		}
	}
	
	private void remove(IPOIIf poi) {
		int rowIndex = getRow(poi);
		if (rowIndex > -1) {
			rows.remove(rowIndex);
			updateSortNumber();
		}
	}
	
	private int getRow(IPOIIf poi) {
		for (int i = 0; i < rows.size(); i++) {
			Object[] row = (Object[])rows.get(i);
			if (row[3].equals(poi)) {
				return i;
			}
		}
		return -1;
	}

	public int getColumnCount() {
		return 3;
	}

	public int getRowCount() {
		return rows == null ? 0 : rows.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		if (rows == null) {
			return null;
		}
		Object[] row = (Object[])rows.get(rowIndex);
		return row[columnIndex];
	}
	
	@Override
	public Class<?> getColumnClass(int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		default:
			return Object.class;
		}
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Rekkefølge";
		case 1:
			return "Type";
		case 2:
			return "Merknad";
		default:
			return null;
		}
	}
}
