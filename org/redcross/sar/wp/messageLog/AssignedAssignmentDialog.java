package org.redcross.sar.wp.messageLog;

import java.awt.CardLayout;
import java.util.Calendar;

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.DTG;

/**
 * Dialog for assigning unit an assignment
 * See {@link AssignmentDialog}
 * @author thomasl
 *
 */
public class AssignedAssignmentDialog extends AssignmentDialog
{

	public AssignedAssignmentDialog(IDiskoWpMessageLog wp)
	{
		super(wp);
		
		m_timeLabel.setText(m_wpMessageLog.getText("AssignedTimeLabel.text") + ": ");
	}

	@Override
	public void cancelUpdate()
	{
		if(m_lineAdded)
		{
			IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
			messageLine.deleteObject();
			m_lineAdded = false;
		}
	}

	@Override
	public void showDialog()
	{
		this.setVisible(true);
		
		if(messageHasAssignedAssignment())
		{
			// TODO vises også om enhet har oppdrag?
			System.err.println("Has assigned assignment line");
			showHasAssignment();
		}
		else if(unitHasNextAssignment() && !unitHasAssignedAssignment())
		{
			System.err.println("Has next assignment");
			showNextAssignment();
		}
		else if(!unitHasNextAssignment() && !unitHasAssignedAssignment())
		{
			System.err.println("Assignment queue");
			showAssignmentQueue();
		}
	}

	@Override
	protected void updateMessage()
	{
		// Assigning an assignment
		if(m_selectedAssignment != null)
		{
			// Have selected an assignment, add assignment and update unit/message
			m_lineAdded = true;
			
			IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
			//unit.setStatus(UnitStatus.INITIALIZING); TODO initializing not a valid unit status
			try
			{
				unit.addUnitAssignment(m_selectedAssignment, AssignmentStatus.ASSIGNED);
			} 
			catch (IllegalOperationException e)
			{
				e.printStackTrace();
			}
			
			IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, true);
			messageLine.setLineAssignment(m_selectedAssignment);
			m_selectedAssignment = null;
			
			// Show assignment in edit mode
			showDialog();
		}
		else
		{
			// Working on existing message line, update current
			IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
			IAssignmentIf assignment = messageLine.getLineAssignment();
			
			try
			{
				Calendar time = DTG.DTGToCal(m_timeTextField.getText());
				// TODO Set assignment time
				//assignment.set
			} 
			catch (IllegalMsoArgumentException e1)
			{
				System.err.println("Not a valid DTG format");
				m_timeTextField.setText("");
			}
			
			m_lineAdded = false;
			fireDialogFinished();
		}
	}

	@Override
	protected void showHasAssignment()
	{
		// TODO Set assigned time in time text field
		m_timeTextField.setText("");
		
		CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
		layout.show(m_cardsPanel, HAS_ASSIGNMENT_ID);
	}

}
