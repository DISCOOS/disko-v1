package org.redcross.sar.wp.unit;

import java.util.Comparator;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IPersonnelListIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;
import org.redcross.sar.util.mso.Selector;

/**
 * Table model for the personnel overview panel
 * 
 * @author thomasl
 */
public class PersonnelOverviewTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
	private static final long serialVersionUID = 1L;
	private List<IPersonnelIf> m_displayPersonnel;
	private IPersonnelListIf m_allPersonnel;
	
	/**
	 * Select personnel at the end of the history chain
	 */
	private static Selector<IPersonnelIf> m_activePersonnelSelector = new Selector<IPersonnelIf>()
	{
		public boolean select(IPersonnelIf personnel)
		{
			return personnel.getNextOccurence() == null;
		}
	};
	
	/**
	 * Sort personnel on name
	 */
	private static final Comparator<IPersonnelIf> m_personnelComparator = new Comparator<IPersonnelIf>()
	{
		public int compare(IPersonnelIf o1, IPersonnelIf o2)
		{
			return o1.getFirstname().compareTo(o2.getFirstname());
		}
	};
	
	public PersonnelOverviewTableModel(IDiskoWpUnit wp)
	{
		wp.getMsoModel().getEventManager().addClientUpdateListener(this);
		m_allPersonnel = wp.getMsoManager().getCmdPost().getAttendanceList();
		m_displayPersonnel = new LinkedList<IPersonnelIf>();
		m_displayPersonnel.addAll(m_allPersonnel.selectItems(m_activePersonnelSelector, m_personnelComparator));
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
		return m_displayPersonnel.size();
	}

	public Object getValueAt(int row, int column)
	{
		IPersonnelIf personnel = m_displayPersonnel.get(row);
		switch(column)
		{
		case 0:
			return personnel.getFirstname() + " " + personnel.getLastname();
		}
		return null;
	}
	
	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) 
	{
		return columnIndex == 1 || columnIndex == 2;
	}

	/**
	 * Table data changed if personnel data changed in MSO
	 */
	public void handleMsoUpdateEvent(Update e)
	{
		m_displayPersonnel.clear();
		m_displayPersonnel.addAll(m_allPersonnel.selectItems(m_activePersonnelSelector, m_personnelComparator));
		fireTableDataChanged();
	}

	EnumSet<IMsoManagerIf.MsoClassCode> myInterests = EnumSet.of(
			IMsoManagerIf.MsoClassCode.CLASSCODE_PERSONNEL);
	/**
	 * Interested in personnel changes
	 */
	public boolean hasInterestIn(IMsoObjectIf msoObject)
	{
		return myInterests.contains(msoObject.getMsoClassCode());
	}

	/**
	 * @param clickedRow
	 * @return Personnel at given row in table
	 */
	public IPersonnelIf getPersonnel(int clickedRow)
	{
		if(clickedRow >= 0)
		{
			return clickedRow < m_displayPersonnel.size() ? m_displayPersonnel.get(clickedRow) : null;
		}
		else
		{
			return null;
		}
	}
}
