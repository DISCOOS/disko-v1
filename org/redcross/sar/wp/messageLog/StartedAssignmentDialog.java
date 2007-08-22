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
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;
import org.redcross.sar.util.AssignmentTransferUtilities;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.DTG;

/**
 * Dialog for starting an assignment
 * See {@link AssignmentDialog} for details
 * 
 * @author thomasl
 */
public class StartedAssignmentDialog extends AssignmentDialog
{

	public StartedAssignmentDialog(IDiskoWpMessageLog wp)
	{
		super(wp);
	}

	public void cancelUpdate()
	{
		if(m_lineAdded)
		{
			IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.STARTED, false);
			messageLine.deleteObject();
			m_lineAdded = false;
			
			// Delete assigned message line if added
			MessageLogTopPanel.cancelAssign();
		}
	}

	public void showDialog()
	{
		this.setVisible(true);
		
		// Started button pressed i top panel
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		IAssignmentIf assignment = null;
		IUnitIf unit =  (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		if(messageHasStartedAssignment())
		{
			showHasAssignment();
		}
		else if(!messageHasAssignedAssignment())
		{
			// Message does not have an assignment line. Have to assign assignment first
			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			// The receiving unit has an assignment, prompt user whether unit has started or not
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
				// Show assign assignment dialog
				MessageLogTopPanel.showAssignDialog();
			}
			else
			{
				fireDialogCanceled();
			}
		}
		else
		{
			// Has assigned line, prompt user whether unit has started or not
			assignment = message.findMessageLine(MessageLineType.ASSIGNED, false).getLineAssignment();
			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			int n = JOptionPane.showOptionDialog(m_wpMessageLog.getApplication().getFrame(), 
					String.format(m_wpMessageLog.getText("UnitStartedAssignment.text"), unit.getTypeAndNumber(), assignment.getTypeAndNumber()), 
					m_wpMessageLog.getText("UnitStartedAssignment.header"), 
					JOptionPane.YES_NO_OPTION, 
					JOptionPane.QUESTION_MESSAGE, 
					null, 
					options, 
					options[0]);
			
			if(n == JOptionPane.YES_OPTION)
			{
				// Check that unit can accept assignment
				if(AssignmentTransferUtilities.unitCanAccept(unit, assignment.getStatus()))
				{
					// Set assignment started
					AssignmentTransferUtilities.createAssignmentChangeMessageLines(message, MessageLineType.STARTED, 
							MessageLineType.STARTED, Calendar.getInstance(), assignment);
					m_lineAdded = true;
				}
				else
				{
					ErrorDialog error = new ErrorDialog(m_wpMessageLog.getApplication().getFrame());
					error.showError("Can not start assignment", unit.getTypeAndNumber() + " can not accept " + assignment.getTypeAndNumber());
					return;
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
		// Must have started message line to reach this point
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.STARTED, false);
		
		String time = DTG.CalToDTG(messageLine.getOperationTime());
		if(time.isEmpty())
		{
			time = DTG.CalToDTG(Calendar.getInstance());
		}
		m_timeTextField.setText(time);
		
		CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
		layout.show(m_cardsPanel, HAS_ASSIGNMENT_ID);
	}

	public void newMessageSelected(IMessageIf message)
	{
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.STARTED, false);
		if(messageLine != null)
		{
			IAssignmentIf assignment = messageLine.getLineAssignment();
			if(assignment != null)
			{
				m_assignmentLabel.setText(m_wpMessageLog.getText("AssignmentLabel.text") + ": " + assignment.getTypeAndNumber());
			}
			
			m_timeTextField.setText(DTG.CalToDTG(messageLine.getOperationTime()));
			m_timeLabel.setText(m_wpMessageLog.getText("StartedTimeLabel.text") + ": ");
		}
	}

}
