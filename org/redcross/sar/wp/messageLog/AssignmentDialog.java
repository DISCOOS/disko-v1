package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.gui.renderers.IconRenderer.AssignmentIcon;
import org.redcross.sar.mso.data.AssignmentImpl;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.ICommunicatorIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;
import org.redcross.sar.util.except.IllegalOperationException;

import com.esri.arcgis.trackinganalyst.ActionProcessor;
import com.swiftmq.admin.explorer.ButtonFormatter;

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
	
	protected JPanel m_nextAssignmentsPanel = null;
	protected JScrollPane m_nextAssignmentScrollPane = null;
	protected ButtonGroup m_nextAssignmentButtonGroup = null;
	
	protected JPanel m_assignmentQueuePanel = null;
	protected JScrollPane m_assignmentQueueScrollPane = null;
	protected ButtonGroup m_assignmentQueueButtonGroup = null;
	
	protected JButton m_okButton = null;
	protected JButton m_cancelButton = null;
	
	protected IAssignmentIf m_selectedAssignment = null;
	protected boolean m_messageLineAdded = false;
	
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
		
		JPanel actionButtonPanel = new JPanel();
		actionButtonPanel.setLayout(new BoxLayout(actionButtonPanel, BoxLayout.PAGE_AXIS));
		
		m_okButton = DiskoButtonFactory.createSmallOKButton();
		m_okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(m_selectedAssignment != null)
				{
					// Have selected an assignment, add assignment and update unit/message
					m_messageLineAdded = true;
					
					IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
					// ? unit.setStatus(UnitStatus.INITIALIZING);
					
					IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, true);
					try
					{
						m_selectedAssignment.setStatus(AssignmentStatus.ASSIGNED);
					} 
					catch (IllegalOperationException e1)
					{
						e1.printStackTrace();
					}
					messageLine.setLineAssignment(m_selectedAssignment);
					
					m_selectedAssignment = null;
			
				}
				else
				{
					m_messageLineAdded = false;
				}
				fireDialogFinished();
			}
		});
		actionButtonPanel.add(m_okButton);
		
		m_cancelButton = DiskoButtonFactory.createSmallCancelButton();
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(m_messageLineAdded)
				{
					IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
					messageLine.deleteObject();
				}
				
				m_wpMessageLog.getMsoManager().rollback();
				
				fireDialogCanceled();
			}
		});
		actionButtonPanel.add(m_cancelButton);
		
		initShowAssignmentPanel();
		initAssignmentQueuePanel();
		initNextAssignmentPanel();
		
		m_contentsPanel.add(actionButtonPanel);
		this.add(m_contentsPanel);
	}
	
	private void initNextAssignmentPanel()
	{
		m_nextAssignmentsPanel = new JPanel();
		m_nextAssignmentsPanel.setLayout(new BoxLayout(m_nextAssignmentsPanel, BoxLayout.PAGE_AXIS));
		m_nextAssignmentScrollPane = new JScrollPane(m_nextAssignmentsPanel);
		m_nextAssignmentButtonGroup = new ButtonGroup();
	}

	private void initAssignmentQueuePanel()
	{
		m_assignmentQueuePanel = new JPanel();
		m_assignmentQueueScrollPane = new JScrollPane(m_assignmentQueuePanel);
		m_assignmentQueueButtonGroup = new ButtonGroup();
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
			if(messageHasAssignment())
			{
				showHasAssignment();
			}
			else if(hasNextAssignment())
			{
				showNextAssignment();
			}
			else
			{
				showAssignmentQueue();
			}
		}
		this.setVisible(true);	
	}

	/**
	 * Show all assignments
	 */
	private void showAssignmentQueue()
	{
		m_assignmentQueuePanel.removeAll();
		
		for(final IAssignmentIf assignment : m_wpMessageLog.getMsoManager().getCmdPost().getAssignmentListItems())
		{
			JToggleButton button = DiskoButtonFactory.createSmallAssignmentToggleButton(assignment);
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					m_selectedAssignment = assignment;
				}
			});
		}
		m_assignmentQueuePanel.setVisible(true);
	}

	/**
	 * Show assignments currently in unit's assirnment queue
	 */
	private void showNextAssignment()
	{
		m_nextAssignmentsPanel.removeAll();
		m_nextAssignmentButtonGroup.clearSelection();
		
		// Get assignments in receiving unit's queue
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		for(final IAssignmentIf assignment : unit.getAllocatedAssignments())
		{
			JToggleButton button = DiskoButtonFactory.createAssignmentToggleButton(assignment);
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					m_selectedAssignment = assignment;
				}
			});
			m_nextAssignmentButtonGroup.add(button);
			m_nextAssignmentsPanel.add(button);
		}
		
		m_nextAssignmentsPanel.setVisible(true);
	}

	/**
	 * Show current message assignment
	 */
	private void showHasAssignment()
	{
		m_showAssignmentPanel.setVisible(true);
	}

	private boolean hasNextAssignment()
	{
		// TODO
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		return unit.getAssignedAssignments().size() != 0;
	}

	/**
	 * @return Whether the current message already has a assigned message line
	 */
	private boolean messageHasAssignment()
	{
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
		return messageLine != null;
	}

}
