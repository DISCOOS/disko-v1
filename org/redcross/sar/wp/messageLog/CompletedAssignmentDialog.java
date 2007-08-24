package org.redcross.sar.wp.messageLog;

import java.awt.CardLayout;
import java.util.Calendar;

import javax.swing.JOptionPane;

import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.util.AssignmentTransferUtilities;
import org.redcross.sar.util.mso.DTG;

/**
 * Dialog for setting assignment to complete
 * See {@link AssignmentDialog} for details
 * 
 * @author thomasl
 *
 */
public class CompletedAssignmentDialog extends AssignmentDialog
{

	public CompletedAssignmentDialog(IDiskoWpMessageLog wp)
	{
		super(wp);
		
		m_timeLabel.setText(m_wpMessageLog.getText("CompletedTimeLabel.text") + ": ");
	}

	@Override
	public void cancelUpdate()
	{
		if(m_lineAdded)
		{
			IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.COMPLETE, false);
			messageLine.deleteObject();
			m_lineAdded = false;
			
			// Cancel assign and started message lines if added
			MessageLogTopPanel.cancelAssign();
			MessageLogTopPanel.cancelStarted();
		}
	}

	@Override
	public void showDialog()
	{
		this.setVisible(true);
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		IUnitIf unit = (IUnitIf)message.getSingleReceiver();
		
		IAssignmentIf assignment = null;
		boolean hasAssigned = (messageHasAssignedAssignment() || unitHasAssignedAssignment());
		boolean hasStarted = (messageHasStartedAssignment() || unitHasStartedAssignment());
		
		if(messageHasCompletedAssignment())
		{
			showHasAssignment();
		}
		else if(!hasStarted && hasAssigned)
		{
			// Assigned assignment might be registered in message, or unit can have a pre-existing assigned assingment
			IMessageLineIf line = message.findMessageLine(MessageLineType.ASSIGNED, false);
			if(line != null)
			{
				assignment = line.getLineAssignment();
			}
			else
			{
				assignment = unit.getAssignedAssignments().get(0);
			}
			
			// Message does not have a started assignment line, or unit 
			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			int n = JOptionPane.showOptionDialog(m_wpMessageLog.getApplication().getFrame(), 
					String.format(m_wpMessageLog.getText("UnitHasNotStartedAssignment.text"), unit.getTypeAndNumber(),  assignment.getTypeAndNumber()),
					m_wpMessageLog.getText("UnitHasNotStartedAssignment.header"), 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					options, 
					options[0]);
			
			if(n == JOptionPane.YES_OPTION)
			{
				MessageLogTopPanel.showStartDialog();
			}
			else
			{
				fireDialogCanceled();
			}
		}
		else if(!hasAssigned)
		{
			// Unit does not have an assignment
			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			int n = JOptionPane.showOptionDialog(m_wpMessageLog.getApplication().getFrame(), 
					String.format(m_wpMessageLog.getText("UnitHasNoAssignment.text"), unit.getTypeAndNumber()),
					m_wpMessageLog.getText("UnitHasNoAssignment.header"), 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					options, 
					options[0]);
			
			if(n == JOptionPane.YES_OPTION)
			{
				MessageLogTopPanel.showAssignDialog();
			}
			else
			{
				fireDialogCanceled();
			}
		}
		else
		{
			// Message does not have completed message line
			IMessageLineIf startedLine = message.findMessageLine(MessageLineType.STARTED, false);
			if(startedLine != null)
			{
				assignment = startedLine.getLineAssignment();
			}
			else
			{
				assignment = unit.getActiveAssignment();
			}
			
			if(!AssignmentTransferUtilities.unitCanAccept(unit, AssignmentStatus.FINISHED))
			{
				ErrorDialog error = new ErrorDialog(m_wpMessageLog.getApplication().getFrame());
				error.showError(m_wpMessageLog.getText("CanNotCompleteError.details"),
						m_wpMessageLog.getText("CanNotCompleteError.header"));
				this.hideDialog();
				return;
			}
			this.hideDialog();
			
			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			int n = JOptionPane.showOptionDialog(m_wpMessageLog.getApplication().getFrame(), 
					String.format(m_wpMessageLog.getText("UnitCompletedAssignment.text"), unit.getTypeAndNumber(), assignment.getTypeAndNumber()),
					m_wpMessageLog.getText("UnitCompletedAssignment.header"), 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					options, 
					options[0]);
			
			if(n == JOptionPane.YES_OPTION)
			{
				AssignmentTransferUtilities.createAssignmentChangeMessageLines(message, MessageLineType.COMPLETE, MessageLineType.COMPLETE,
						Calendar.getInstance(), assignment);
				m_lineAdded = true;
				showDialog();
			}
			else
			{
				fireDialogCanceled();
			}
		}
	}

	@Override
	protected void updateMessage()
	{	
		// Update time
		IMessageLineIf line = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.COMPLETE, false);
		if(line != null)
		{
			try
			{
				line.setOperationTime(DTG.DTGToCal(m_timeTextField.getText()));
			}
			catch(Exception e){}
		}
		// Perform action show in list
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
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.COMPLETE, false);
		IAssignmentIf assignment = null;
		Calendar time = null;
		
		if(messageLine != null)
		{
			assignment = messageLine.getLineAssignment();	
			time = messageLine.getOperationTime();
		}
		else
		{
			ICommunicatorIf communicator = message.getSingleReceiver();
			if(communicator != null && communicator instanceof IUnitIf)
			{
				IUnitIf unit = (IUnitIf)communicator;
				assignment = unit.getActiveAssignment();	
			}
		}
		
		if(assignment != null)
		{
			if(time == null)
			{
				time = assignment.getTimeAssigned();
			}
			m_assignmentLabel.setText(m_wpMessageLog.getText("AssignmentLabel.text") + ": " + assignment.getTypeAndNumber());
			m_timeTextField.setText(DTG.CalToDTG(time));
		}
		else
		{
			this.hideDialog();
		}
		
	}
}
