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
import org.redcross.sar.util.AssignmentTransferUtilities;
import org.redcross.sar.util.mso.DTG;

/**
 * Panel for starting an assignment
 * See {@link AbstractAssignmentPanel} for details
 * 
 * @author thomasl
 */
public class StartedAssignmentPanel extends AbstractAssignmentPanel
{
	private static final long serialVersionUID = 1L;

	/**
	 * @param wp Message log work process
	 */
	public StartedAssignmentPanel(IDiskoWpMessageLog wp)
	{
		super(wp);
		
		m_timeLabel.setText(m_wpMessageLog.getText("StartedTimeLabel.text") + ": ");
	}

	/**
	 * Remove added message lines of type started. If any assigned lines were added, these are removed as well
	 */
	public void cancelUpdate()
	{
		if(linesAdded())
		{
			IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.STARTED, false);
			messageLine.deleteObject();
//			TODO m_lineAdded = false;
			
			// Delete assigned message line if added
			MessageLogTopPanel.cancelAssign();
		}
	}

	/**
	 *
	 */
	@Override
	protected void updateMessageLine()
	{
		super.updateMessageLine();
		
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
		layout.show(m_cardsPanel, EDIT_ASSIGNMENT_ID);
	}

	protected void updateAssignmentLineList()
	{
		AssignmentListModel model = (AssignmentListModel)m_assignmentLineList.getModel();
		model.setMessageLineType(MessageLineType.STARTED);
	}

	protected void addNewMessageLine()
	{
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		IUnitIf unit = (IUnitIf)message.getSingleReceiver();
		IAssignmentIf assignment = null;
		
		if(unitHasAssignedAssignment())
		{
			// If unit has assigned, ask if this is started
			assignment = unit.getActiveAssignment();

			// Check that unit can accept assignment
			if(!AssignmentTransferUtilities.unitCanAccept(unit, AssignmentStatus.EXECUTING))
			{
				ErrorDialog error = new ErrorDialog(m_wpMessageLog.getApplication().getFrame());
				error.showError(m_wpMessageLog.getText("CanNotStartError.details"),
						m_wpMessageLog.getText("CanNotStartError.header"));
				this.hideComponent();
				return;
			}

			hideComponent();

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

				// Set assignment started
				AssignmentTransferUtilities.createAssignmentChangeMessageLines(message, MessageLineType.STARTED, 
						MessageLineType.STARTED, Calendar.getInstance(), assignment);
				// TODO Keep track of added lines

				showComponent();

			}
			else
			{
				// TODO
			}
		}
		else if(unitHasNextAssignment())
		{
			// Else unit could have started from allocated buffer
			showNextAssignment();
		}
		else
		{
			// Unit could have started from assignment pool
			showAssignmentPool();
		}
	}

	protected void addSelectedAssignment()
	{
		if(m_selectedAssignment != null)
		{
			IMessageIf message = MessageLogTopPanel.getCurrentMessage();
			// TODO Add started line with selected assignment
			AssignmentTransferUtilities.createAssignmentChangeMessageLines(message, 
					MessageLineType.ASSIGNED, 
					MessageLineType.STARTED, 
					Calendar.getInstance(), 
					m_selectedAssignment);
		}
		
		MessageLogTopPanel.showStartDialog();
	}
}
