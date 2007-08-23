package org.redcross.sar.wp.messageLog;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.renderers.IconRenderer;
import org.redcross.sar.mso.data.IAssignmentIf;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.IMessageLineIf;
import org.redcross.sar.mso.data.IUnitIf;
import org.redcross.sar.mso.data.IAssignmentIf.AssignmentStatus;
import org.redcross.sar.mso.data.IMessageLineIf.MessageLineType;
import org.redcross.sar.mso.data.IUnitIf.UnitStatus;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.DTG;

/**
 * Abstract dialog handling all updates to assignment. Template pattern. 
 * 
 * @author thomasl
 *
 */
public abstract class AssignmentDialog extends DiskoDialog implements IEditMessageDialogIf
{
	protected static final String HAS_ASSIGNMENT_ID = "HAS ASSIGNMENT";
	protected static final String NEXT_ASSIGNMENT_ID = "NEXT ASSIGNMENT";
	protected static final String ASSIGNMENT_QUEUE_ID = "ASSIGNMENT QUEUE";
	
	protected IDiskoWpMessageLog m_wpMessageLog = null;
	
	protected JPanel m_contentsPanel = null;
	protected JPanel m_cardsPanel = null;
	
	protected JPanel m_showAssignmentPanel = null;
	protected JLabel m_assignmentLabel = null;
	protected JLabel m_timeLabel = null;
	protected JTextField m_timeTextField = null;
	
	protected JPanel m_nextAssignmentsPanel = null;
	protected JScrollPane m_nextAssignmentScrollPane = null;
	protected ButtonGroup m_nextAssignmentButtonGroup = null;

	protected JPanel m_assignmentQueuePanel = null;
	protected JScrollPane m_assignmentQueueScrollPane = null;
	protected ButtonGroup m_assignmentQueueButtonGroup = null;
	
	protected JButton m_okButton = null;
	protected JButton m_cancelButton = null;
	
	protected IAssignmentIf m_selectedAssignment = null;
	
	protected boolean m_lineAdded = false;
	
	public AssignmentDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		
		initialize();
	}
	
	private void initialize()
	{
		m_contentsPanel = new JPanel(new BorderLayout());
		
		m_cardsPanel = new JPanel(new CardLayout());
		
		JPanel actionButtonPanel = new JPanel();
		actionButtonPanel.setLayout(new BoxLayout(actionButtonPanel, BoxLayout.PAGE_AXIS));
		
		m_okButton = DiskoButtonFactory.createSmallOKButton();
		m_okButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateMessage();
			}
		});
		actionButtonPanel.add(m_okButton);
		
		m_cancelButton = DiskoButtonFactory.createSmallCancelButton();
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				cancelUpdate();
				
//				TODO m_wpMessageLog.getMsoManager().rollback();
				fireDialogCanceled();
			}
		});
		actionButtonPanel.add(m_cancelButton);
		
		initShowAssignmentPanel();
		initAssignmentQueuePanel();
		initNextAssignmentPanel();
		
		m_contentsPanel.add(m_cardsPanel, BorderLayout.CENTER);
		m_contentsPanel.add(actionButtonPanel, BorderLayout.EAST);
		this.add(m_contentsPanel);
	}
	
	/**
	 * Overridden by sub-classes in order to perform the correct updates
	 */
	protected abstract void updateMessage();
	
	/**
	 * Overridden by sub-classes in order to revert the correct changes
	 */
	public abstract void cancelUpdate();
	
	protected void initNextAssignmentPanel()
	{
		m_nextAssignmentsPanel = new JPanel();
		m_nextAssignmentsPanel.setLayout(new BoxLayout(m_nextAssignmentsPanel, BoxLayout.PAGE_AXIS));
		m_nextAssignmentScrollPane = new JScrollPane(m_nextAssignmentsPanel);
		m_nextAssignmentButtonGroup = new ButtonGroup();
		m_cardsPanel.add(m_nextAssignmentScrollPane, NEXT_ASSIGNMENT_ID);
	}

	protected void initAssignmentQueuePanel()
	{
		m_assignmentQueuePanel = new JPanel();
		m_assignmentQueueScrollPane = new JScrollPane(m_assignmentQueuePanel);
		m_assignmentQueueButtonGroup = new ButtonGroup();
		m_cardsPanel.add(m_assignmentQueueScrollPane, ASSIGNMENT_QUEUE_ID);
	}

	protected void initShowAssignmentPanel()
	{
		m_showAssignmentPanel = new JPanel();
		m_showAssignmentPanel.setLayout(new BoxLayout(m_showAssignmentPanel, BoxLayout.PAGE_AXIS));
		
		m_assignmentLabel = new JLabel(m_wpMessageLog.getText("AssignmentLabel.text"));
//		m_assignmentLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		m_showAssignmentPanel.add(m_assignmentLabel);
		
		JPanel assignmentTimePanel = new JPanel();
//		assignmentTimePanel.setLayout(new BoxLayout(assignmentTimePanel, BoxLayout.LINE_AXIS));
		m_timeLabel = new JLabel();
//		m_timeLabel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		assignmentTimePanel.add(m_timeLabel);
		m_timeTextField = new JTextField(6);
//		m_timeTextField.setAlignmentX(JComponent.LEFT_ALIGNMENT);
//		m_timeTextField.setMaximumSize(new Dimension(60, 22));
		assignmentTimePanel.add(m_timeTextField);
//		assignmentTimePanel.setAlignmentX(JComponent.LEFT_ALIGNMENT);
		m_showAssignmentPanel.add(assignmentTimePanel);
		
//		m_showAssignmentPanel.add(Box.createVerticalGlue());
		
		m_cardsPanel.add(m_showAssignmentPanel, HAS_ASSIGNMENT_ID);
	}

	public void clearContents()
	{
		// TODO Auto-generated method stub
	}

	public void hideDialog()
	{
		this.setVisible(false);
	}

	public abstract void newMessageSelected(IMessageIf message);

	/**
	 * Determine which UI elements that should be displayed, varies with assignment action
	 */
	public abstract void showDialog();

	/**
	 * Show all assignments available from command post
	 */
	protected void showAssignmentQueue()
	{
		m_assignmentQueuePanel.removeAll();
		Collection<IAssignmentIf> assignments = m_wpMessageLog.getMsoManager().getCmdPost().getAssignmentListItems();
		for(final IAssignmentIf assignment : assignments)
		{
			JToggleButton button = DiskoButtonFactory.createSmallAssignmentToggleButton(assignment);
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Select button
					JToggleButton sourceButton = (JToggleButton)e.getSource();
					selectAssignmentButton(sourceButton, m_assignmentQueueButtonGroup);
					m_selectedAssignment = assignment;
				}
			});
			m_assignmentQueueButtonGroup.add(button);
			m_assignmentQueuePanel.add(button);
		}
		
		// Select button with highest priority
		try
		{
			JToggleButton selectedButton = (JToggleButton)m_assignmentQueueButtonGroup.getElements().nextElement();
			selectAssignmentButton(selectedButton, m_assignmentQueueButtonGroup);
			Iterator<IAssignmentIf> assignmentIt = assignments.iterator();
			m_selectedAssignment = assignmentIt.hasNext() ? assignmentIt.next() : null;
		}
		catch(Exception e){}
		
		CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
		layout.show(m_cardsPanel, ASSIGNMENT_QUEUE_ID);
	}

	/**
	 * Show assignments currently in unit's assignment queue
	 */
	protected void showNextAssignment()
	{
		m_nextAssignmentsPanel.removeAll();
		m_nextAssignmentButtonGroup.clearSelection();
		
		// Get assignments in receiving unit's queue
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		List<IAssignmentIf> assignments = unit.getAllocatedAssignments();
		for(final IAssignmentIf assignment : assignments)
		{
			JToggleButton button = DiskoButtonFactory.createLargeToggleButton(assignment);
			button.addActionListener(new ActionListener()
			{
				public void actionPerformed(ActionEvent e)
				{
					// Mark icon as selected
					JToggleButton sourceButton = (JToggleButton)e.getSource();
					selectAssignmentButton(sourceButton, m_nextAssignmentButtonGroup);
					m_selectedAssignment = assignment;
				}
			});
			m_nextAssignmentButtonGroup.add(button);
			m_nextAssignmentsPanel.add(button);
		}
		
		// Select button with highest priority
		try
		{
			JToggleButton selectedButton = (JToggleButton)m_nextAssignmentButtonGroup.getElements().nextElement();
			m_nextAssignmentButtonGroup.setSelected(selectedButton.getModel(), true);
			selectAssignmentButton(selectedButton, m_nextAssignmentButtonGroup);
			Iterator<IAssignmentIf> assignmentIt = assignments.iterator();
			m_selectedAssignment = assignmentIt.hasNext() ? assignmentIt.next() : null;
		}
		catch(Exception e){}
		
		
		CardLayout layout = (CardLayout)m_cardsPanel.getLayout();
		layout.show(m_cardsPanel, NEXT_ASSIGNMENT_ID);
	}
	
	/**
	 * Updates button selection for the given button group
	 * @param button
	 * @param buttonGroup
	 */
	private void selectAssignmentButton(JToggleButton button, ButtonGroup buttonGroup)
	{
		// Mark one icon as selected
		try
		{
			Enumeration<AbstractButton> buttons = buttonGroup.getElements();
			JToggleButton buttonIt = null;
			while(buttons.hasMoreElements())
			{
				buttonIt = (JToggleButton)buttons.nextElement();
				IconRenderer.AssignmentIcon icon = (IconRenderer.AssignmentIcon)buttonIt.getIcon();
				icon.setSelected(false);
			}
			
			IconRenderer.AssignmentIcon icon = (IconRenderer.AssignmentIcon)button.getIcon();
			icon.setSelected(true);
		}
		catch(Exception e){}
		
		buttonGroup.setSelected(button.getModel(), true);
	}

	/**
	 * Show current message assignment
	 */
	protected abstract void showHasAssignment();

	/**
	 * @return Whether current single receiver has next assignment in assignment queue
	 */
	protected boolean unitHasNextAssignment()
	{
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		return unit.getAllocatedAssignments().size() != 0; 
	}
	
	/**
	 * @return Whether current single receiving unit has any assigned assignments
	 */
	protected boolean unitHasAssignedAssignment()
	{
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		return !unit.getAssignedAssignments().isEmpty(); 
	}
	
	protected boolean unitHasStartedAssignment()
	{
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		return !unit.getExecutingAssigment().isEmpty();
	}

	/**
	 * @return Whether the current message already has a assigned message line
	 */
	protected boolean messageHasAssignedAssignment()
	{
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.ASSIGNED, false);
		return messageLine != null;
	}
	
	/**
	 * @return Whether the current message has a started message line
	 */
	protected boolean messageHasStartedAssignment()
	{
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.STARTED, false);
		return messageLine != null;
	}
	
	protected boolean messageHasCompletedAssignment()
	{
		IMessageLineIf messageLine = MessageLogTopPanel.getCurrentMessage().findMessageLine(MessageLineType.COMPLETE, false);
		return messageLine != null;
	}

	/**
	 * @return Current assignment to receiving unit. null if unit does not have an assignment
	 */
	protected IAssignmentIf getUnitAssignment()
	{
		IAssignmentIf assignment = null;
		IUnitIf unit = (IUnitIf)MessageLogTopPanel.getCurrentMessage().getSingleReceiver();
		List<IAssignmentIf> assignments = unit.getAssignedAssignments();
		try
		{
			assignment = assignments.get(0);
		}
		catch(Exception e){}
		
		return assignment;
	}
	
	public boolean lineAdded()
	{
		return m_lineAdded;
	}
	
	public void lineRemoved()
	{
		m_lineAdded = false;
	}
}
