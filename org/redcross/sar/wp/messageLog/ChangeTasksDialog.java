package org.redcross.sar.wp.messageLog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;

import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.mso.data.IMessageIf;

public class ChangeTasksDialog extends DiskoDialog implements IEditMessageDialogIf
{
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
	
	
//	TODO protected DiskoTaskDialog m_taskDialog = null;
	
	public ChangeTasksDialog(IDiskoWpMessageLog wp)
	{
		super(wp.getApplication().getFrame());
		
		m_wpMessageLog = wp;
		
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
		m_sendTransportButton = DiskoButtonFactory.createLargeToggleButton(m_wpMessageLog.getText("SendTransport.text"));
		m_sendTransportButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				IMessageIf message = MessageLogTopPanel.getCurrentMessage();
				JToggleButton button = (JToggleButton)arg0.getSource();
				if(button.isSelected())
				{
				}
				else
				{
					
				}
			}
		});
		m_changeSendTransportButton = DiskoButtonFactory.createSmallButton("", m_wpMessageLog.getText("ChangeButton.icon"));
		addButtonPair(m_sendTransportButton, m_changeSendTransportButton);
		
		// Get team
		m_getTeamButton = DiskoButtonFactory.createLargeToggleButton(m_wpMessageLog.getText("GetTeam.text"));
		m_changeGetTeamButton = DiskoButtonFactory.createSmallButton("", m_wpMessageLog.getText("ChangeButton.icon"));
		addButtonPair(m_getTeamButton, m_changeGetTeamButton);
		
		// Create assignment
		m_createAssignmentButton = DiskoButtonFactory.createLargeToggleButton(m_wpMessageLog.getText("CreateAssignment.text"));
		m_changeCreateAssignmentButton = DiskoButtonFactory.createSmallButton("", m_wpMessageLog.getText("ChangeButton.icon"));
		addButtonPair(m_createAssignmentButton, m_changeCreateAssignmentButton);
		
		// Confirm intelligence
		m_confirmIntelligenceButton = DiskoButtonFactory.createLargeToggleButton(m_wpMessageLog.getText("ConfirmIntelligence.text"));
		m_changeConfirmIntelligenceButton = DiskoButtonFactory.createSmallButton("", m_wpMessageLog.getText("ChangeButton.icon"));
		addButtonPair(m_confirmIntelligenceButton, m_changeConfirmIntelligenceButton);
		
		// Silent witness
		m_silentWitnessButton = DiskoButtonFactory.createLargeToggleButton(m_wpMessageLog.getText("SilentWitness.text"));
		m_changeSilentWitnessButton = DiskoButtonFactory.createSmallButton("", m_wpMessageLog.getText("ChangeButton.icon"));
		addButtonPair(m_silentWitnessButton, m_changeSilentWitnessButton);
		
		m_contentsPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
		// General
		m_generalTaskButton = DiskoButtonFactory.createLargeToggleButton(m_wpMessageLog.getText("GeneralTask.text"));
		m_changeGeneralTaskButton = DiskoButtonFactory.createSmallButton("", m_wpMessageLog.getText("ChangeButton.icon"));
		addButtonPair(m_generalTaskButton, m_changeGeneralTaskButton);
	}
	
	/**
	 * Adds a pair of task selection and change action button for that task
	 * @param task
	 * @param change
	 */
	private void addButtonPair(JToggleButton task, JButton change)
	{
		JPanel sendTransportPanel = new JPanel();
		sendTransportPanel.setLayout(new BoxLayout(sendTransportPanel, BoxLayout.LINE_AXIS));
		sendTransportPanel.add(task);
		sendTransportPanel.add(change);
		m_contentsPanel.add(sendTransportPanel);
	}

	public void hideDialog()
	{
		this.setVisible(false);
	}

	public void newMessageSelected(IMessageIf message)
	{
		// Loop through all tasks in new message
		// Mark message tasks as selected in button panel
	}

	public void showDialog()
	{
		this.setVisible(true);
	}

	public void clearContents()
	{
	}
}