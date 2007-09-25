package org.redcross.sar.wp.unit;

import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IPersonnelIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IPersonnelIf.PersonnelStatus;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;
import org.redcross.sar.util.except.IllegalOperationException;

/**
 * Handles logic in units
 * 
 * TODO Common utility class?
 * 
 * @author thomasl
 */
public class UnitUtilities
{
	private final static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.wp.unit.unit");
	/**
	 * Toggles pause status for given unit
	 * @param unit
	 * @throws IllegalOperationException Thrown if pause status can't be toggled (i.e. unit is initializing)
	 */
	public static void toggleUnitPause(IUnitIf unit) throws IllegalOperationException
	{
		// Only possible from status ready
		if(unit.getStatus() == UnitStatus.INITIALIZING)
		{
			throw new IllegalOperationException();
		}
		
		// Toggle pause
		if(unit.getStatus() == UnitStatus.PAUSED)
		{
			unit.setStatus(UnitStatus.WORKING);
		}
		else
		{
			unit.setStatus(UnitStatus.PAUSED);
		}
	}
	
	/**
	 * Releases a unit
	 * @param unit The unit
	 * @throws IllegalOperationException Thrown if unit can not be released
	 */
	public static void releaseUnit(IUnitIf unit) throws IllegalOperationException
	{
		if(unit.getStatus() != UnitStatus.RELEASED)
		{
			IAssignmentIf activeAssignment = unit.getActiveAssignment();

			// Get user confirmation
			boolean releaseUnit;
			if(activeAssignment != null)
			{
				String[] options = {m_resources.getString("Yes.text"), m_resources.getString("No.text")};
				releaseUnit = JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(null,
						m_resources.getString("ReleaseUnitWithAssignment.text"), 
						m_resources.getString("ReleaseUnitWithAssignment.header"), 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, 
						null,  
						options, 
						options[0]);
			}
			else
			{
				String[] options = {m_resources.getString("Yes.text"), m_resources.getString("No.text")};
				releaseUnit = JOptionPane.YES_OPTION == JOptionPane.showOptionDialog(null,
						m_resources.getString("ReleaseUnit.text"), 
						m_resources.getString("ReleaseUnit.header"), 
						JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, 
						null, 
						options, 
						options[0]);
			}

			// Release unit
			if(releaseUnit)
			{
				// Abort assignment
				if(activeAssignment != null)
				{
					activeAssignment.setStatus(AssignmentStatus.ABORTED);
				}

				// Release personnel, keep references for history
				for(IPersonnelIf personnel : unit.getUnitPersonnelItems())
				{
					personnel.setStatus(PersonnelStatus.RELEASED);
				}

				// Release unit
				unit.setStatus(UnitStatus.RELEASED);
			}
		}
	}
	
	/**
	 * Deletes a unit, unit is completely removed, no history is kept. Changes are not committed
	 * @param unit The unit
	 * @throws IllegalOperationException Thrown if unit can not be deleted
	 */
	public static void deleteUnit(IUnitIf unit) throws IllegalOperationException
	{
		// Check validity of delete
		if(unit.getStatus() != UnitStatus.EMPTY)
		{
			throw new IllegalOperationException();
		}
		
		if(unit.getUnitAssignments().size() != 0)
		{
			throw new IllegalOperationException();
		}
		
		// TODO 
		
		unit.deleteObject();
	}
}