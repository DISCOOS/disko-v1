package org.redcross.sar.wp.unit;

import java.util.EnumSet;

import javax.swing.table.AbstractTableModel;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitListIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;

public class UnitOverviewTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
	private static final long serialVersionUID = 1L;

	private IUnitListIf m_units;
	
	public UnitOverviewTableModel(IDiskoWpUnit wp)
	{
		wp.getMsoModel().getEventManager().addClientUpdateListener(this);
		m_units = wp.getMsoManager().getCmdPost().getUnitList();
	}
	
	public void handleMsoUpdateEvent(Update e)
	{
		fireTableDataChanged();
	}
	
	EnumSet<IMsoManagerIf.MsoClassCode> interestedIn = EnumSet.of(IMsoManagerIf.MsoClassCode.CLASSCODE_UNIT);
	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		return interestedIn.contains(msoObject.getMsoClassCode());
	}
	
	@Override
    public String getColumnName(int column)
    {
    	return null;
    }

	public int getColumnCount()
	{
		return 3;
	}

	public int getRowCount()
	{
		return m_units.size();
	}

	public Object getValueAt(int row, int column)
	{
		IUnitIf unit = (IUnitIf)m_units.getItems().toArray()[row];
		switch(column)
		{
		case 0:
			return unit.getTypeText() + " " + unit.getUnitNumber();
		}
		return null;
	}

}
