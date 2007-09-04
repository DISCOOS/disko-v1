package org.redcross.sar.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.BevelBorder;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.gui.DiskoButtonFactory.ButtonType;
import org.redcross.sar.gui.renderers.DTGListCellRenderer;
import org.redcross.sar.gui.renderers.SimpleListCellRenderer;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.mso.data.ITaskIf.TaskPriority;
import org.redcross.sar.mso.data.ITaskIf.TaskStatus;
import org.redcross.sar.mso.data.ITaskIf.TaskType;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.DTG;

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
	private JComboBox m_dueComboBox = null;
	private JComboBox m_responsibleComboBox = null;
	private JComboBox m_alertComboBox = null;
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
		m_typeComboBox = new JComboBox(TaskType.values());
		m_typeComboBox.setSelectedIndex(3);
		m_typeComboBox.setRenderer(new SimpleListCellRenderer());
		gbc.gridwidth = 3;
		addComponent(0, m_application.getProperty("TaskType.text"), m_typeComboBox, 1, gbc);
		
		// Priority
		m_priorityComboBox = new JComboBox(TaskPriority.values());
		m_priorityComboBox.setSelectedIndex(1);
		m_priorityComboBox.setRenderer(new SimpleListCellRenderer());
		addComponent(0, m_application.getProperty("TaskPriority.text"), m_priorityComboBox, 0, gbc);
		
		// Due
		Calendar[] dueItems = {
				Calendar.getInstance(),
				Calendar.getInstance(),
				Calendar.getInstance(), 
				Calendar.getInstance()};
		dueItems[0].add(Calendar.MINUTE, 15);
		dueItems[1].add(Calendar.MINUTE, 30);
		dueItems[2].add(Calendar.MINUTE, 45);
		dueItems[3].add(Calendar.MINUTE, 60);
		m_dueComboBox = new JComboBox(dueItems);
//		m_dueComboBox.setEditable(true);
		m_dueComboBox.setSelectedIndex(1);
		m_dueComboBox.setRenderer(new DTGListCellRenderer());
		addComponent(2, m_application.getProperty("TaskDue.text"), m_dueComboBox, 1, gbc);
		
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
		Calendar[] alertItems = {Calendar.getInstance(),
				Calendar.getInstance(),
				Calendar.getInstance(),
				Calendar.getInstance()};
		alertItems[0].add(Calendar.MINUTE, 25);
		alertItems[1].add(Calendar.MINUTE, 20);
		alertItems[2].add(Calendar.MINUTE, 15);
		alertItems[3].add(Calendar.MINUTE, 10);
		m_alertComboBox = new JComboBox(alertItems);
//		m_alertComboBox.setEditable(true);
		m_alertComboBox.setSelectedIndex(0);
		m_alertComboBox.setRenderer(new DTGListCellRenderer());
		addComponent(2, m_application.getProperty("TaskAlert.text"), m_alertComboBox, 1, gbc);
		
		// Status
		m_statusComboBox = new JComboBox(TaskStatus.values());
		m_statusComboBox.setSelectedIndex(0);
		m_statusComboBox.setRenderer(new SimpleListCellRenderer());
		addComponent(0, m_application.getProperty("TaskStatus.text"), m_statusComboBox, 0, gbc);
		
		// Progress
		m_progressSpinner = new JSpinner();
		m_progressSpinner.setModel(new SpinnerNumberModel(0, 0, 100, 1));
//		m_progressTextField.setText(String.valueOf(0));
		addComponent(2, m_application.getProperty("TaskProgress.text"), m_progressSpinner, 1, gbc);
		
		// Description
		m_descriptionTextArea = new JTextArea();
		m_descriptionTextArea.setRows(10);
		m_descriptionTextArea.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		m_descriptionTextArea.setLineWrap(true);
		m_descriptionTextArea.setWrapStyleWord(true);
		gbc.gridwidth = 3;
		addComponent(0, m_application.getProperty("TaskDescription.text"), m_descriptionTextArea, 10, gbc);
		
		// Source
		m_sourceTextArea = new JTextArea();
		m_sourceTextArea.setRows(4);
		m_sourceTextArea.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		m_sourceTextArea.setLineWrap(true);
		m_sourceTextArea.setWrapStyleWord(true);
		gbc.gridwidth = 3;
		addComponent(0, m_application.getProperty("TaskSource.text"), m_sourceTextArea, 4, gbc);
		
		// Finish button
		JPanel actionButtonPanel = new JPanel();
		m_finishedButton = DiskoButtonFactory.createSmallButton(ButtonType.FinishedButton);
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
		m_cancelButton = DiskoButtonFactory.createSmallButton(ButtonType.CancelButton);
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
	 * Extracts values from swing components and updates/creates MSO task object
	 */
	public void updateTask()
	{
		// Get due time
		Calendar dueTime = (Calendar)m_dueComboBox.getSelectedItem();
		
		// Create new task if current is set to null
		if(m_currentTask == null)
		{
			m_currentTask = m_application.getMsoModel().getMsoManager().createTask(dueTime);
		}
		
		// Get text
		String text = m_taskTextField.getText();
		m_currentTask.setTaskText(text);
	
		// Get type
		TaskType type = (TaskType)m_typeComboBox.getSelectedItem();
		m_currentTask.setType(type);
		
		// Get priority
		TaskPriority priority = (TaskPriority)m_priorityComboBox.getSelectedItem();
		m_currentTask.setPriority(priority);
		
		// Get responsible
		
		// Get alert
		Calendar alert = (Calendar)m_alertComboBox.getSelectedItem();
//		m_currentTask.set TODO
		
		// Get status
		TaskStatus status = (TaskStatus)m_statusComboBox.getSelectedItem();
		try
		{
			m_currentTask.setStatus(status);
		} 
		catch (IllegalOperationException e)
		{
			System.err.println("Can not accept status");
		}
		
		// Get progress
		int progress = (Integer)m_progressSpinner.getValue();
		m_currentTask.setProgress(progress);
		
		// Get description
		String description = m_descriptionTextArea.getText();
		m_currentTask.setDescription(description);
		
		// Get source
		
		// Clean up
		m_currentTask = null;
	}
	
	/**
	 * Updates swing component contents with values stored in current task
	 */
	private void updateFields()
	{
		if(m_currentTask != null)
		{
			// Task text
			String taskText = m_currentTask.getTaskText();
			m_taskTextField.setText(taskText);
			
			// Type
			
			// Priority
			TaskPriority priority = m_currentTask.getPriority();
			m_priorityComboBox.setSelectedItem(priority);
			
			// Due
			Calendar due = m_currentTask.getDueTime();
			m_dueComboBox.addItem(due);
			
			// Responsible
			String responsible = m_currentTask.getResponsibleRole();
			m_responsibleComboBox.setSelectedItem(responsible);
			
			// Alert
//			Calendar alert = m_currentTask.get
			
			// Status
			TaskStatus status = m_currentTask.getStatus();
			m_statusComboBox.setSelectedItem(status);
			
			// Progress
			int progress = m_currentTask.getProgress();
			m_progressSpinner.setValue(progress);
			
			// Description
			String description = m_currentTask.getDescription();
			m_descriptionTextArea.setText(description);
			
			// Source
//			String source = m_currentTask.get TODO
		}
	}
}
