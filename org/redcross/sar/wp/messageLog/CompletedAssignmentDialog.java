package org.redcross.sar.wp.messageLog;

import java.awt.CardLayout;
import java.util.Calendar;

import javax.swing.JOptionPane;

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
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
		
		// TODO Set time text field
		//m_timeTextField.setText(t)
	
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		
		if(messageHasCompletedAssignment())
		{
			showHasAssignment();
		}
		else if(unit.getStatus() == UnitStatus.WORKING)
		{
			// Unit has started assignment, show dialog box enabling the user to mark an assignment as completed
			IAssignmentIf activeAssignment = unit.getActiveAssignment();
			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			int n = JOptionPane.showOptionDialog(m_wpMessageLog.getApplication().getFrame(), 
					String.format(m_wpMessageLog.getText("UnitCompletedAssignment.text"),  activeAssignment.getTypeAndNumber()),
					m_wpMessageLog.getText("UnitCompletedAssignment.header"), 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					options, 
					options[0]);
			
			if(n == JOptionPane.YES_OPTION)
			{
				showHasAssignment();
				m_timeTextField.setText(DTG.CalToDTG(Calendar.getInstance()));
			}
			else
			{
				fireDialogCanceled();
			}
		}
		else if(unit.getAssignedAssignments().size() != 0)
		{
			// Unit has an assignment, but it is not started
			IAssignmentIf activeAssignment = unit.getActiveAssignment();
			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			int n = JOptionPane.showOptionDialog(m_wpMessageLog.getApplication().getFrame(), 
					String.format(m_wpMessageLog.getText("UnitHasNotStartedAssignment.text"), unit.getTypeAndNumber(), activeAssignment.getTypeAndNumber()),
					m_wpMessageLog.getText("UnitHasNotStartedAssignment.header"), 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					options, 
					options[0]);
			
			if(n == JOptionPane.YES_OPTION)
			{
				// User wishes to start an assignment
				MessageLogTopPanel.showStartDialog();
			}
			else
			{
				fireDialogCanceled();
			}
		}
		else
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
				// User wishes to assign an assignment first
				MessageLogTopPanel.showAssignDialog();
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
		// Get started assignment and unit
		IMessageLineIf startedMessageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.STARTED, false);
		IAssignmentIf assignment = startedMessageLine.getLineAssignment();
		IUnitIf unit = assignment.getOwningUnit();
		
		// Check validity of transfer
		if(!AssignmentTransferUtilities.unitCanAccept(unit, AssignmentStatus.FINISHED))
		{
			//TODO warning dialog?
			System.err.println(unit.getTypeAndNumber() + " can not accept " + assignment.getTypeAndNumber());
			return;
		}
		
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.COMPLETE, false);
		// Keep track of whether a new line is added or not
		if(messageLine == null)
		{
			messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.COMPLETE, true);
			m_lineAdded = true;
		}
		
		
		messageLine.setLineAssignment(assignment);
		try
		{
			assignment.setStatus(AssignmentStatus.FINISHED);
		} 
		catch (IllegalOperationException e)
		{
			e.printStackTrace();
		}
		
		unit.setStatus(UnitStatus.READY);
		
		// Set completed time
		try
		{
			messageLine.setOperationTime(DTG.DTGToCal(m_timeTextField.getText()));
		} 
		catch (IllegalMsoArgumentException e)
		{
			System.err.println("Not a valid DTG format");
			messageLine.setOperationTime(Calendar.getInstance());
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
