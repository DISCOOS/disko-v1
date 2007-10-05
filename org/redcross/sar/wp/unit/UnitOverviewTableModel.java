package org.redcross.sar.wp.unit;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IUnitListIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.Selector;

import javax.swing.table.AbstractTableModel;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

public class UnitOverviewTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
	private static final long serialVersionUID = 1L;

	private IUnitListIf m_allUnits;
	private List<IUnitIf> m_units;

	private static final Selector<IUnitIf> m_unitSelector = new Selector<IUnitIf>()
	{
		public boolean select(IUnitIf anObject)
		{
			return true;
		}
	};

	private static final Comparator<IUnitIf> m_unitComparator = new Comparator<IUnitIf>()
	{
		public int compare(IUnitIf arg0, IUnitIf arg1)
		{
			if(arg0.getType() == arg1.getType())
			{
				return arg0.getNumber() - arg1.getNumber();
			}
			else
			{
				return arg0.getType().ordinal() - arg1.getType().ordinal();
			}
		}
	};

	public UnitOverviewTableModel(IDiskoWpUnit wp)
	{
		wp.getMsoModel().getEventManager().addClientUpdateListener(this);
		m_allUnits = wp.getMsoManager().getCmdPost().getUnitList();
		m_units = new LinkedList<IUnitIf>();                                     // todo use linked list directly
		m_units.addAll(m_allUnits.selectItems(m_unitSelector, m_unitComparator));
	}

	public void handleMsoUpdateEvent(Update e)
	{
		// Rebuild list
		m_units.clear();                                                         // todo use linked list directly
		m_units.addAll(m_allUnits.selectItems(m_unitSelector, m_unitComparator));
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

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex)
	{
		return columnIndex == 1 || columnIndex == 2;
	}

	public int getRowCount()
	{
		return m_units.size();
	}

	public Object getValueAt(int row, int column)
	{
		IUnitIf unit = m_units.get(row);
		switch(column)
		{
		case 0:
			return unit.getTypeText() + " " + unit.getUnitNumber();
		}
		return null;
	}

	/*
	 * Return unit at given row in table model
	 */
	public IUnitIf getUnit(int clickedRow)
	{
		return clickedRow < m_units.size() ? m_units.get(clickedRow): null;
	}

}
