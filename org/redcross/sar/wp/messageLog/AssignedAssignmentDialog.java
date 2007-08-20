package org.redcross.sar.wp.messageLog;

import java.awt.CardLayout;
import java.util.Calendar;

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.util.AssignmentTransferUtilities;
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
			showHasAssignment();
		}
		else if(unitHasNextAssignment() && !unitHasAssignedAssignment())
		{
			showNextAssignment();
		}
		else if(!unitHasNextAssignment() && !unitHasAssignedAssignment())
		{
			showAssignmentQueue();
		}
	}

	@Override
	protected void updateMessage()
	{
		// Assigning an assignment
		IMessageLineIf messageLine = null;
		
		if(m_selectedAssignment != null)
		{
			IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
			//unit.setStatus(UnitStatus.INITIALIZING); TODO initializing not a valid unit status
			
			// Check if unit can accept assignment
			if(!AssignmentTransferUtilities.unitCanAccept(unit, AssignmentStatus.ASSIGNED))
			{
				// TODO warning dialog?
				System.err.println(unit.getTypeAndNumber() + " can not accept " + m_selectedAssignment.getTypeAndNumber());
				return;
			}
			
			// Have selected an assignment, add assignment and update unit/message
			messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, true);
			messageLine.setLineAssignment(m_selectedAssignment);
			m_lineAdded = true;
			
			try
			{
				unit.addUnitAssignment(m_selectedAssignment, AssignmentStatus.ASSIGNED);
			} 
			catch (IllegalOperationException e)
			{
				e.printStackTrace();
			}
			
			
			m_selectedAssignment = null;
			
			// Show assignment in edit mode
			showDialog();
		}
		else
		{
			// Working on existing message line, update current message line
			messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
			m_lineAdded = false;
			fireDialogFinished();
		}
		
		// Set assign time
		try
		{
			Calendar time = DTG.DTGToCal(m_timeTextField.getText());
			// TODO no mso update event on setOperationTime?
			messageLine.setOperationTime(time);
		} 
		catch (IllegalMsoArgumentException e1)
		{
			//System.err.println("Not a valid DTG format");
			messageLine.setOperationTime(Calendar.getInstance());
		}
		
		MessageLogTopPanel.showListDialog();
	}

	@Override
	protected void showHasAssignment()
	{
		CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
		layout.show(m_cardsPanel, HAS_ASSIGNMENT_ID);
	}

	@Override
	public void newMessageSelected(IMessageIf message)
	{
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
		if(messageLine != null)
		{
			IAssignmentIf assignment = messageLine.getLineAssignment();
			if(assignment != null)
			{
				m_assignmentLabel.setText(m_wpMessageLog.getText("AssignmentLabel.text") + ": " + assignment.getTypeAndNumber());
			}
			
			m_timeTextField.setText(DTG.CalToDTG(messageLine.getOperationTime()));
		}
	}

}
