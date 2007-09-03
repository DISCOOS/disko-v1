package org.redcross.sar.gui;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.wp.messageLog.DiskoButtonFactory;

/**
 * 
 * @author thomasl
 *
 */
public class TaskDialog extends DiskoDialog
{
	private static final long serialVersionUID = 1L;
	private IDiskoApplication m_application = null;
	private JPanel m_contentsPanel = null;
	
	private ITaskIf m_currentTask = null;
	
	private JButton m_finishedButton = null;
	private JButton m_cancelButton = null;
	
	private JTextField m_taskTextField = null;
	private JComboBox m_typeComboBox = null;
	private JComboBox m_priorityComboBox = null;
	private JTextField m_dueTextField = null;
	private JComboBox m_responsibleComboBox = null;
	private JTextField m_alertTextField = null;
	private JComboBox m_statusComboBox = null;
	private JSpinner m_progressSpinner = null;
	private JTextArea m_descriptionTextArea = null;
	private JTextArea m_sourceTextArea = null;
	
	public TaskDialog(IDiskoApplication application)
	{
		super(application.getFrame());
		m_application = application;
		initialize();
	}
	
	private void initialize()
	{
		m_contentsPanel = new JPanel(new GridBagLayout());
		m_contentsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 4, 4, 4);
		
		// Task
		m_taskTextField = new JTextField();
		gbc.gridwidth = 3;
		addComponent(0, m_application.getProperty("Task.text"), m_taskTextField, 1, gbc);
		
		// Type
		Object[] types = {m_application.getProperty("TaskType.TRANSPORT.text"),
				m_application.getProperty("TaskType.RESOURCE.text"), 
				m_application.getProperty("TaskType.INTELLIGENCE.text"), 
				m_application.getProperty("TaskType.GENERAL.text")};
		m_typeComboBox = new JComboBox(types);
		m_typeComboBox.setSelectedIndex(3);
		gbc.gridwidth = 3;
		addComponent(0, m_application.getProperty("TaskType.text"), m_typeComboBox, 1, gbc);
		
		// Priority
		Object[] priorities = {m_application.getProperty("TaskPriority.HIGH.text"),
				m_application.getProperty("TaskPriority.NORMAL.text"),
				m_application.getProperty("TaskPriority.LOW.text")};
		m_priorityComboBox = new JComboBox(priorities);
		m_priorityComboBox.setSelectedIndex(1);
		addComponent(0, m_application.getProperty("TaskPriority.text"), m_priorityComboBox, 0, gbc);
		
		// Due
		m_dueTextField = new JTextField(3);
		m_dueTextField.setText(String.valueOf(30));
		addComponent(2, m_application.getProperty("TaskDue.text"), m_dueTextField, 1, gbc);
		
		// Responsible
		Object[] responsible = {};
		try
		{
			 // TODO Get roles that has task work process
			responsible = m_application.getDiskoModuleLoader().getRoleTitles();
		} 
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		m_responsibleComboBox = new JComboBox(responsible);
		addComponent(0, m_application.getProperty("TaskResponsible.text"), m_responsibleComboBox, 0, gbc);
		
		// Alert
		m_alertTextField = new JTextField(3);
		m_alertTextField.setText(String.valueOf(5));
		addComponent(2, m_application.getProperty("TaskAlert.text"), m_alertTextField, 1, gbc);
		
		// Status
		Object[] statuses = {m_application.getProperty("TaskStatus.UNTREATED.text"), 
				m_application.getProperty("TaskStatus.STARTED.text"), 
				m_application.getProperty("TaskStatus.POSTPONED.text"), 
				m_application.getProperty("TaskStatus.FINISHED.text")};
		m_statusComboBox = new JComboBox(statuses);
		m_statusComboBox.setSelectedIndex(0);
		addComponent(0, m_application.getProperty("TaskStatus.text"), m_statusComboBox, 0, gbc);
		
		// Progress
		m_progressSpinner = new JSpinner();
//		m_progressTextField.setText(String.valueOf(0));
		addComponent(2, m_application.getProperty("TaskProgress.text"), m_progressSpinner, 1, gbc);
		
		// Description
		m_descriptionTextArea = new JTextArea();
		m_descriptionTextArea.setRows(10);
		m_descriptionTextArea.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		gbc.gridwidth = 3;
		addComponent(0, m_application.getProperty("TaskDescription.text"), m_descriptionTextArea, 10, gbc);
		
		// Source
		m_sourceTextArea = new JTextArea();
		m_sourceTextArea.setRows(4);
		m_sourceTextArea.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		gbc.gridwidth = 3;
		addComponent(0, m_application.getProperty("TaskSource.text"), m_sourceTextArea, 4, gbc);
		
		// Finish button
		JPanel actionButtonPanel = new JPanel();
		m_finishedButton = DiskoButtonFactory.createSmallButton(m_application.getProperty("ApplyButton.text"));
		m_finishedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateTask();
				setVisible(false);
			}
		});
		actionButtonPanel.add(m_finishedButton);
		
		// Cancel button
		m_cancelButton = DiskoButtonFactory.createSmallButton(m_application.getProperty("CancelButton.text"));
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				fireDialogCanceled();
				setVisible(false);
			}
		});
		actionButtonPanel.add(m_cancelButton);
		
		gbc.gridwidth = 4;
		gbc.gridy++;
		m_contentsPanel.add(actionButtonPanel, gbc);
		
		this.add(m_contentsPanel);
		this.pack();
	}
	
	private void addComponent(int column, String labelText, JComponent component, int numRows, GridBagConstraints gbc)
	{
		gbc.gridheight = Math.max(1, numRows);
		gbc.gridx = column + 1;
		m_contentsPanel.add(component, gbc);
		
		JLabel label = new JLabel(labelText);
		gbc.gridx = column;
		gbc.gridwidth = 1;
		m_contentsPanel.add(label, gbc);
		
		gbc.gridy += numRows;
	}
	
	public void setTask(ITaskIf task)
	{
		m_currentTask = task;
		updateFields();
	}
	
	/**
	 * Extracts values from swing components and updates MSO task object
	 */
	public void updateTask()
	{
		// TODO
	}
	
	/**
	 * Updates swing component contents with values stored in current task
	 */
	private void updateFields()
	{
		// TODO
	}
}
