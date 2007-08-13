package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IUnitIf;

/**
 * Dialog handling all updates to assignment. 
 * 
 * @author thomasl
 *
 */
public class AssignmentDialog extends DiskoDialog implements IEditMessageDialogIf
{
	public enum AssignmentAction {ASSIGN, START, COMPLETE};
	
	protected IDiskoWpMessageLog m_wpMessageLog = null;
	protected AssignmentAction m_action = AssignmentAction.ASSIGN;
	
	protected JPanel m_contentsPanel = null;
	
	protected JPanel m_showAssignmentPanel = null;
	protected JLabel m_assignmentLabel = null;
	protected JLabel m_assignedTimeLabel = null;
	protected JTextField m_assignedTimeTextField = null;
	
	protected JPanel m_assignmentQueuePanel = null;
	protected JPanel m_readyAssignmentsPanel = null;
	
	public AssignmentDialog(IDiskoWpMessageLog wp, AssignmentAction action)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		m_action = action;
		
		initialize();
	}
	
	private void initialize()
	{
		m_contentsPanel = new JPanel(new BorderLayout());
		
		initShowAssignmentPanel();
		initAssignmentQueuePanel();
		initReadyAssignmentPanel();
		
		this.add(m_contentsPanel);
	}
	
	private void initReadyAssignmentPanel()
	{
		// TODO Auto-generated method stub
		
	}

	private void initAssignmentQueuePanel()
	{
		// TODO Auto-generated method stub
		
	}

	private void initShowAssignmentPanel()
	{
		m_showAssignmentPanel = new JPanel();
		m_showAssignmentPanel.setLayout(new BoxLayout(m_showAssignmentPanel, BoxLayout.PAGE_AXIS));
		
		m_assignmentLabel = new JLabel("Oppdrag: "); // TODO internasjonaliser
		m_showAssignmentPanel.add(m_assignmentLabel);
		
		JPanel assignmentTimePanel = new JPanel();
		m_assignedTimeLabel = new JLabel("Tildelt når: "); // TODO internasjonaliser
		assignmentTimePanel.add(m_assignedTimeLabel);
		m_assignedTimeTextField = new JTextField(12);
		assignmentTimePanel.add(m_assignedTimeTextField);
		assignmentTimePanel.add(Box.createHorizontalGlue());
		m_showAssignmentPanel.add(assignmentTimePanel);
		
		m_showAssignmentPanel.add(Box.createVerticalGlue());
		
		m_contentsPanel.add(m_showAssignmentPanel, BorderLayout.CENTER);
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
	}

	public void hideDialog()
	{
		this.setVisible(false);
	}

	public void newMessageSelected(IMessageIf message)
	{
		// TODO Auto-generated method stub
		
	}

	public void showDialog()
	{
		// Show components based on current message state
		if(m_action == AssignmentAction.ASSIGN)
		{
			if(hasAssignment())
			{
				showHasAssignment();
			}
			else if(hasNextAssignment())
			{
				
			}
			else
			{
				
			}
		}
		this.setVisible(true);	
	}

	private void showHasAssignment()
	{
		// TODO Auto-generated method stub
		
	}

	private boolean hasNextAssignment()
	{
		// TODO
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		return unit.getAssignedAssignments().size() != 0;
	}

	private boolean hasAssignment()
	{
		// TODO
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		return unit.getActiveAssignment() != null;
	}

}
