package org.redcross.sar.wp.messageLog;

import java.awt.CardLayout;
import java.util.Calendar;

import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ICommunicatorIf;
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

	public void cancelUpdate()
	{
		if(m_lineAdded)
		{
			IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
			messageLine.deleteObject();
			m_lineAdded = false;
		}
	}

	public void showDialog()
	{
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
		
		this.setVisible(true);
	}

	protected void updateMessage()
	{
		if(m_selectedAssignment != null)
		{
			IMessageIf message = MessageLogTopPanel.getCurrentMessage();
			IUnitIf unit = (IUnitIf)message.getSingleReceiver();
			
			// Check if unit can accept assignment
			if(AssignmentTransferUtilities.unitCanAccept(unit, AssignmentStatus.ASSIGNED))
			{
				AssignmentTransferUtilities.createAssignmentChangeMessageLines(message, MessageLineType.ASSIGNED, MessageLineType.ASSIGNED,
						Calendar.getInstance(), m_selectedAssignment);
				m_lineAdded = true;
				
				m_selectedAssignment = null;
			}
			else
			{
				ErrorDialog error = new ErrorDialog(m_wpMessageLog.getApplication().getFrame());
				error.showError("Can not assign assignment" , 
						unit.getTypeAndNumber() + " can not accept " + m_selectedAssignment.getTypeAndNumber());
				return;
			}
			
			// Show assignment in edit mode
			showDialog();
		}
		else
		{
			// Working on existing message line, update current message line
			m_lineAdded = false;
			fireDialogFinished();
		}
		
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
		
		if(messageLine != null)
		{
			// Set assign time
			try
			{
				Calendar time = DTG.DTGToCal(m_timeTextField.getText());
				messageLine.setOperationTime(time);
			} 
			catch (IllegalMsoArgumentException e1)
			{
				messageLine.setOperationTime(Calendar.getInstance());
			}
		}
		
		MessageLogTopPanel.showListDialog();
	}

	protected void showHasAssignment()
	{	
		CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
		layout.show(m_cardsPanel, HAS_ASSIGNMENT_ID);
	}

	public void newMessageSelected(IMessageIf message)
	{
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
		IAssignmentIf assignment = null;
		Calendar time = null;
		if(messageLine != null)
		{
			assignment = messageLine.getLineAssignment();
			time = messageLine.getOperationTime();
		}
		else
		{
			ICommunicatorIf communicator = MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
			if(communicator != null && communicator instanceof IUnitIf)
			{
				IUnitIf unit = (IUnitIf)communicator;
				if(!unit.getAssignedAssignments().isEmpty())
				{
					assignment = unit.getActiveAssignment();
					time = assignment.getTimeAssigned();
				}
			}
		}
		
		if(assignment != null)
		{
			m_assignmentLabel.setText(m_wpMessageLog.getText("AssignmentLabel.text") + ": " + assignment.getTypeAndNumber());
		}
		else
		{
			// No assign message line, receiving unit doesn't have an assignment assigned
			this.setVisible(false);
		}
		
		m_timeTextField.setText(DTG.CalToDTG(time));
	}
}
