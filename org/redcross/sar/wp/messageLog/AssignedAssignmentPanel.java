package org.redcross.sar.wp.messageLog;

import java.util.Calendar;

import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.util.AssignmentTransferUtilities;

/**
 * Dialog for assigning unit an assignment
 * See {@link AbstractAssignmentPanel}
 * @author thomasl
 *
 */
public class AssignedAssignmentPanel extends AbstractAssignmentPanel
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * @param wp Message log work process
	 */
	public AssignedAssignmentPanel(IDiskoWpMessageLog wp)
	{
		super(wp);
		
		m_timeLabel.setText(m_wpMessageLog.getText("AssignedTimeLabel.text") + ": ");
	}

	/**
	 * {@link AbstractAssignmentPanel#cancelUpdate()}
	 */
	public void cancelUpdate()
	{
		if(linesAdded())
		{
			//TODO Loop through added lines, delete ASSIGNED, should also delete STARTED and COMPLETED added in this message
//			IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
//			messageLine.deleteObject();
		}
	}


	/**
	 * {@link AbstractAssignmentPanel#updateMessageLine()}
	 */
	@Override
	protected void updateMessageLine()
	{
		super.updateMessageLine();
		MessageLogTopPanel.showListDialog();
	}

	/**
	 * {@link AbstractAssignmentPanel#updateAssignmentLineList()}
	 */
	protected void updateAssignmentLineList()
	{
		AssignmentListModel model = (AssignmentListModel)m_assignmentLineList.getModel();
		model.setMessageLineType(MessageLineType.ASSIGNED);
	}

	/**
	 * {@link AbstractAssignmentPanel#addNewMessageLine()}
	 */
	protected void addNewMessageLine()
	{
		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		IUnitIf unit = (IUnitIf)message.getSingleReceiver();
		
		// Assure that unit can accept assignment
		if(AssignmentTransferUtilities.unitCanAccept(unit, AssignmentStatus.ASSIGNED))
		{
			if(unitHasNextAssignment())
			{
				// If unit has next in assignment buffer, let user choose from these
				showNextAssignment();
			}
			else
			{
				// Else get assignments from assignment pool
				showAssignmentPool();
			}
		}
		else
		{
			//TODO Display error message?
			System.err.println("Unit can not accept assignment");
		}
	}

	/**
	 * {@link AbstractAssignmentPanel#addSelectedAssignment()}
	 */
	protected void addSelectedAssignment()
	{
		if(m_selectedAssignment != null)
		{
			IMessageIf message = MessageLogTopPanel.getCurrentMessage();
			// TODO Add selected assignment to message
			AssignmentTransferUtilities.createAssignmentChangeMessageLines(message, 
					MessageLineType.ASSIGNED, 
					MessageLineType.ASSIGNED, 
					Calendar.getInstance(),
					m_selectedAssignment);
			
		}
		
		MessageLogTopPanel.showAssignDialog();
	}
}
