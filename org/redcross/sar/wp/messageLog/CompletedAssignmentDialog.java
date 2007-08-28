package org.redcross.sar.wp.messageLog;

import java.util.Calendar;

import javax.swing.JOptionPane;

import org.redcross.sar.gui.ErrorDialog;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.util.AssignmentTransferUtilities;

/**
 * Dialog for setting assignment to complete
 * See {@link AbstractAssignmentDialog} for details
 * 
 * @author thomasl
 *
 */
public class CompletedAssignmentDialog extends AbstractAssignmentDialog
{

	public CompletedAssignmentDialog(IDiskoWpMessageLog wp)
	{
		super(wp);
		
		m_timeLabel.setText(m_wpMessageLog.getText("CompletedTimeLabel.text") + ": ");
	}

	public void cancelUpdate()
	{
		if(linesAdded())
		{
			// Remove added lines
			for(IMessageLineIf line : m_addedLines)
			{
				line.deleteObject();
				m_addedLines.remove(line);
			}

			// Cancel assign and started message lines if added
			MessageLogTopPanel.cancelAssign();
			MessageLogTopPanel.cancelStarted();
		}
	}

	@Override
	protected void updateMessageLine()
	{	
		super.updateMessageLine();
		
		// Perform action show in list
		MessageLogTopPanel.showListDialog();
	}

	protected void updateAssignmentLineList()
	{
		AssignmentListModel model = (AssignmentListModel)m_assignmentLineList.getModel();
		model.setMessageLineType(MessageLineType.COMPLETE);
	}

	protected void addNewMessageLine()
	{
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		IUnitIf unit = (IUnitIf)message.getSingleReceiver();
		
		
		IAssignmentIf assignment = unit.getActiveAssignment();
		if(assignment != null)
		{
			// If unit has assigned or started assignment, ask user if this is completed
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
//				m_lineAdded = true;
				showDialog();
			}
			else
			{
				fireDialogCanceled();
			}
		}
		else if(unitHasNextAssignment())
		{
			// Else unit may have completed allocated assignment
			showNextAssignment();
		}
		else
		{
			// Unit may have completed assignment in assignment pool
			showAssignmentPool();
		}
	}

	protected void addSelectedAssignment()
	{
		if(m_selectedAssignment != null)
		{
			IMessageIf message = MessageLogTopPanel.getCurrentMessage();
			// TODO Add completed line with selected assignment
			AssignmentTransferUtilities.createAssignmentChangeMessageLines(message,
					MessageLineType.ASSIGNED,
					MessageLineType.COMPLETE,
					Calendar.getInstance(),
					m_selectedAssignment);
		}
		
		MessageLogTopPanel.showCompleteDialog();
	}
}
