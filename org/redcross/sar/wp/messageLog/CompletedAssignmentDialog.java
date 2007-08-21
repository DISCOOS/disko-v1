package org.redcross.sar.wp.messageLog;

import java.awt.CardLayout;
import java.util.Calendar;

import javax.swing.JOptionPane;

import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentType;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;
import org.redcross.sar.util.AssignmentTransferUtilities;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.except.IllegalOperationException;
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
		
//		m_timeLabel.setText(m_wpMessageLog.getText("CompletedTimeLabel.text") + ": ");
	}

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

	public void showDialog()
	{
		this.setVisible(true);
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		IUnitIf unit = (IUnitIf)message.getSingleReceiver();
		IAssignmentIf assignment = null;
		
		if(messageHasCompletedAssignment())
		{
			showHasAssignment();
		}
		else if(!messageHasAssignedAssignment())
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
		else if(!messageHasStartedAssignment())
		{
			// Message does not have a started assignment line
			assignment = message.findMessageLine(MessageLineType.ASSIGNED, false).getLineAssignment();
			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			int n = JOptionPane.showOptionDialog(m_wpMessageLog.getApplication().getFrame(), 
					String.format(m_wpMessageLog.getText("UnitStartedAssignment.text"), unit.getTypeAndNumber(),  assignment.getTypeAndNumber()),
					m_wpMessageLog.getText("UnitStartedAssignment.header"), 
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
		else
		{
			// Message does not have completed message line
			assignment = message.findMessageLine(MessageLineType.STARTED, false).getLineAssignment();
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
				if(AssignmentTransferUtilities.unitCanAccept(unit, assignment.getStatus()))
				{
					AssignmentTransferUtilities.createAssignmentChangeMessageLines(message, MessageLineType.COMPLETE, MessageLineType.COMPLETE,
							Calendar.getInstance(), assignment);
				}
				else
				{
					ErrorDialog error = new ErrorDialog(m_wpMessageLog.getApplication().getFrame());
					error.showError("Can not complete assignment", "Unit can not complete assignment");
				}
			}
			else
			{
				fireDialogCanceled();
			}
		}
		
	}

	protected void updateMessage()
	{	
		// Perform action show in list
		MessageLogTopPanel.showListDialog();
	}


	protected void showHasAssignment()
	{	
		CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
		layout.show(m_cardsPanel, HAS_ASSIGNMENT_ID);
	}


	public void newMessageSelected(IMessageIf message)
	{
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.COMPLETE, false);
		if(messageLine != null)
		{
			IAssignmentIf assignment = messageLine.getLineAssignment();
			if(assignment != null)
			{
				m_assignmentLabel.setText(m_wpMessageLog.getText("AssignmentLabel.text") + ": " + assignment.getTypeAndNumber());
			}
			
			m_timeTextField.setText(DTG.CalToDTG(messageLine.getOperationTime()));
			m_timeLabel.setText(m_wpMessageLog.getText("CompletedTimeLabel.text") + ": ");
		}
	}

}
