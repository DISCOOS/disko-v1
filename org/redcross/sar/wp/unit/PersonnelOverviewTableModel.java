package org.redcross.sar.wp.unit;

import java.util.EnumSet;

import javax.swing.table.AbstractTableModel;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IPersonnelListIf;
import org.redcross.sar.mso.event.IMsoUpdateListenerIf;
import org.redcross.sar.mso.event.MsoEvent.Update;

/**
 * Table model for the personnel overview panel
 * 
 * @author thomasl
 */
public class PersonnelOverviewTableModel extends AbstractTableModel implements IMsoUpdateListenerIf
{
	private static final long serialVersionUID = 1L;
	private IPersonnelListIf m_personnel;
	
	public PersonnelOverviewTableModel(IDiskoWpUnit wp)
	{
		// TODO attendance list correct?
		wp.getMsoModel().getEventManager().addClientUpdateListener(this);
		m_personnel = wp.getMsoManager().getCmdPost().getAttendanceList(); 
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
		return m_personnel.size();
	}

	public Object getValueAt(int row, int column)
	{
		IPersonnelIf personnel = (IPersonnelIf)m_personnel.getItems().toArray()[row];
		switch(column)
		{
		case 0:
			return personnel.getFirstname() + " " + personnel.getLastname();
		case 2:
			// TODO
			break;
		}
		return null;
	}

	/**
	 * Table data changed if personnel data changed in MSO
	 */
	public void handleMsoUpdateEvent(Update e)
	{
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

}
