package org.redcross.sar.wp.messageLog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.TaskDialog;
import org.redcross.sar.mso.data.IMessageIf;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.mso.data.ITaskListIf;

/**
 * Dialog for changing task in current message. 
 * Initializes and shows a {@link TaskDialog} when changing task fields
 * 
 * @author thomasl
 */
public class ChangeTasksDialog extends DiskoDialog implements IEditMessageComponentIf
{
	private static final long serialVersionUID = 1L;

	IDiskoWpMessageLog m_wpMessageLog = null;
	
	protected JPanel m_contentsPanel = null;
	
	protected JToggleButton m_sendTransportButton = null;
	protected JButton m_changeSendTransportButton = null;
	
	protected JToggleButton m_getTeamButton = null;
	protected JButton m_changeGetTeamButton = null;
	
	protected JToggleButton m_createAssignmentButton = null;
	protected JButton m_changeCreateAssignmentButton = null;
	
	protected JToggleButton m_confirmIntelligenceButton = null;
	protected JButton m_changeConfirmIntelligenceButton = null;
	
	protected JToggleButton m_silentWitnessButton = null;
	protected JButton m_changeSilentWitnessButton = null;
	
	protected JToggleButton m_generalTaskButton = null;
	protected JButton m_changeGeneralTaskButton = null;
	
	protected HashMap<JToggleButton, JButton> m_buttonMap = null;
	
	protected TaskDialog m_taskDialog = null;
	
	/**
	 * @param wp Message log work process reference
	 */
	public ChangeTasksDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		m_taskDialog = wp.getApplication().getUIFactory().getTaskDialog();
		m_buttonMap = new HashMap<JToggleButton, JButton>();
		initialize();
	}

	private void initialize()
	{
		// Initialize contents panel
		m_contentsPanel = new JPanel();
		m_contentsPanel.setLayout(new BoxLayout(m_contentsPanel, BoxLayout.PAGE_AXIS));		
		m_contentsPanel.setPreferredSize(new Dimension(DiskoButtonFactory.LARGE_BUTTON_SIZE.width + DiskoButtonFactory.SMALL_BUTTON_SIZE.width + 4,
				DiskoButtonFactory.LARGE_BUTTON_SIZE.height*6 + 6));
		m_contentsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		initButtons();
		
		this.add(m_contentsPanel);
	
		this.pack();
	}
	
	private void initButtons()
	{
		// Send transport
		m_sendTransportButton = createToggleButton(m_wpMessageLog.getText("SendTransport.text"));
		m_changeSendTransportButton = createChangeButton();
		addButtonPair(m_sendTransportButton, m_changeSendTransportButton);
		
		// Get team
		m_getTeamButton = createToggleButton(m_wpMessageLog.getText("GetTeam.text"));
		m_changeGetTeamButton = createChangeButton();
		addButtonPair(m_getTeamButton, m_changeGetTeamButton);
		
		// Create assignment
		m_createAssignmentButton = createToggleButton(m_wpMessageLog.getText("CreateAssignment.text"));
		m_changeCreateAssignmentButton = createChangeButton();
		addButtonPair(m_createAssignmentButton, m_changeCreateAssignmentButton);
		
		// Confirm intelligence
		m_confirmIntelligenceButton = createToggleButton(m_wpMessageLog.getText("ConfirmIntelligence.text"));
		m_changeConfirmIntelligenceButton = createChangeButton();
		addButtonPair(m_confirmIntelligenceButton, m_changeConfirmIntelligenceButton);
		
		// Silent witness
		m_silentWitnessButton = createToggleButton(m_wpMessageLog.getText("SilentWitness.text"));
		m_changeSilentWitnessButton = createChangeButton();
		addButtonPair(m_silentWitnessButton, m_changeSilentWitnessButton);
		
		m_contentsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		
		// General
		m_generalTaskButton = createToggleButton(m_wpMessageLog.getText("GeneralTask.text"));
		m_changeGeneralTaskButton = createChangeButton();
		addButtonPair(m_generalTaskButton, m_changeGeneralTaskButton);
	}
	
	/**
	 * Remove/add tasks as task  buttons are toggled
	 * @param ae Event generated by button
	 * @param type The task type for the given button
	 */
	private void toggleTask(ActionEvent ae/*, TaskType type*/)
	{
//		IMessageIf message = MessageLogTopPanel.getCurrentMessage();
		
		JToggleButton button = (JToggleButton)ae.getSource();
		JButton changeButton = m_buttonMap.get(button);
		
		if(button.isSelected())
		{
			// TODO Button is selected, add task
			
			// Make corresponding change button active
			changeButton.setEnabled(true);
		}
		else
		{
			// TODO Button is deselected, remove task
			
			// Make corresponding change button inactive
			changeButton.setEnabled(false);
		}
	}
	
	/**
	 * Adds a pair of task selection and change action button for that task
	 * @param task
	 * @param change
	 */
	private void addButtonPair(JToggleButton task, JButton change)
	{
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.LINE_AXIS));
		buttonPanel.add(task);
		buttonPanel.add(change);
		m_contentsPanel.add(buttonPanel);
		
		change.setEnabled(false);
		m_buttonMap.put(task, change);
	}
	
	/**
	 * Shows the edit task dialog, dialog should have been initialized before this is called
	 */
	private void showEditTaskDialog()
	{
		Point location = getLocationOnScreen();
		m_taskDialog.setLocation(location);
		m_taskDialog.setVisible(true);
	}

	/**
	 *
	 */
	public void hideComponent()
	{
		this.setVisible(false);
	}

	/**
	 * Updates button selection based on which tasks exists in the new message
	 */
	public void newMessageSelected(IMessageIf message)
	{
		// Loop through all tasks in new/updated message
		ITaskListIf tasks = message.getMessageTasks();
		for(ITaskIf task : tasks.getItems())
		{
			// TODO Mark message tasks as selected in button panel
		}	
	}

	/**
	 *
	 */
	public void showComponent()
	{
		this.setVisible(true);
	}

	/**
	 * 
	 */
	public void clearContents()
	{
	}
	
	/**
	 * @return The change button for the given task. 
	 */
	private JButton createChangeButton(/*TaskType type*/)
	{
		JButton button = DiskoButtonFactory.createSmallButton("", m_wpMessageLog.getText("ChangeButton.icon"));
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
//				TODO m_taskDialog.setTask(task);
				showEditTaskDialog();
			}
		});
		
		return button;
	}
	
	/**
	 * Creates a button that toggles a task in the current message
	 * @param text Button text
	 * @return Toggle button that add/remove task from current message
	 */
	private JToggleButton createToggleButton(String text)
	{
		JToggleButton button = DiskoButtonFactory.createLargeToggleButton(text);
		button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				toggleTask(ae);
			}
		});
		
		return button;
	}
}
