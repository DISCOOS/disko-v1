package org.redcross.sar.wp.messageLog;

import java.awt.CardLayout;

import javax.swing.JOptionPane;

import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;
import org.redcross.sar.util.except.IllegalOperationException;

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
		
		m_timeLabel.setText(m_wpMessageLog.getText("StartedTimeLabel.text") + ": ");
	}

	@Override
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

	@Override
	public void showDialog()
	{
		this.setVisible(true);
		
		// Started button pressed i top panel
		IAssignmentIf assignment = getUnitAssignment();
		IUnitIf unit =  (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		if(messageHasStartedAssignment())
		{
			System.err.println("Has started assignment line");
			showHasAssignment();
		}
		else if(assignment != null)
		{
			Object[] options = {m_wpMessageLog.getText("yes.text"), m_wpMessageLog.getText("no.text")};
			// The receiving unit has an assignment, prompt user whether unit has started or not
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
				// Set assignment started
				startAssignment(assignment);
			}
			else
			{
				fireDialogCanceled();
			}
		}
		else
		{
			// Unit does not have an assignment. Have to assign assignment first
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
	}

	@Override
	protected void updateMessage()
	{
		// Starting an assignment
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.STARTED, false);
		assert messageLine != null;
		IAssignmentIf assignment = messageLine.getLineAssignment();
		try
		{
			// TODO sjekk
			assignment.setStatus(AssignmentStatus.EXECUTING);
		}
		catch (IllegalOperationException e1)
		{
			e1.printStackTrace();
		}
		
		// TODO sjekk
		IUnitIf unit = assignment.getOwningUnit();
		unit.setStatus(UnitStatus.WORKING);
		
		// Perform action show in list
		MessageLogTopPanel.showListDialog();
	}

	@Override
	protected void showHasAssignment()
	{
		// TODO Auto-generated method stub
		
		CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
		layout.show(m_cardsPanel, HAS_ASSIGNMENT_ID);
	}

}
