package org.redcross.sar.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.DiskoButtonFactory.ButtonType;
import org.redcross.sar.mso.data.IMsoObjectIf;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.mso.data.TaskImpl;
import org.redcross.sar.mso.data.ITaskIf.TaskPriority;
import org.redcross.sar.mso.data.ITaskIf.TaskStatus;
import org.redcross.sar.mso.data.ITaskIf.TaskType;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.DTG;
import org.redcross.sar.wp.tasks.TaskUtilities;

/**
 * Dialog for editing/creating tasks.
 * If dialog is initiated with task, it will edit this one, else it will create a new task
 *  
 * @author thomasl
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
	private JTextField m_createdTextField = null;
	private JComboBox m_priorityComboBox = null;
	private JComboBox m_dueComboBox = null;
	private JComboBox m_responsibleComboBox = null;
	private JComboBox m_alertComboBox = null;
	private JComboBox m_statusComboBox = null;
	private JComboBox m_progressComboBox = null;
	private JButton m_useSourceButton = null;
	private JTextArea m_descriptionTextArea = null;
	private JTextArea m_sourceTextArea = null;
	private JTextField m_objectTextField = null;
	
	private final static ResourceBundle m_resources = ResourceBundle.getBundle("org.redcross.sar.gui.TaskDialog");
	
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
		addComponent(0, m_resources.getString("Task.text"), m_taskTextField, 1, gbc);
		
		// Type
		m_typeComboBox = new JComboBox(TaskType.values());
		m_typeComboBox.setSelectedIndex(3);
		m_typeComboBox.setRenderer(new TaskEnumListCellRenderer());
		m_typeComboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				updateTaskText();
			}
		});
		gbc.gridwidth = 1;
		addComponent(0, m_resources.getString("TaskType.text"), m_typeComboBox, 0, gbc);
		
		// Created
		m_createdTextField = new JTextField();
		m_createdTextField.setEditable(false);
		addComponent(2, m_resources.getString("TaskCreated.text"), m_createdTextField, 1, gbc);
		
		// Priority
		m_priorityComboBox = new JComboBox(TaskPriority.values());
		m_priorityComboBox.setSelectedIndex(1);
		m_priorityComboBox.setRenderer(new TaskEnumListCellRenderer());
		addComponent(0, m_resources.getString("TaskPriority.text"), m_priorityComboBox, 0, gbc);
		
		// Due
		m_dueComboBox = new JComboBox();
		updateDueComboBox();
		m_dueComboBox.setEditable(true);
		m_dueComboBox.setSelectedIndex(2);
		addComponent(2, m_resources.getString("TaskDue.text"), m_dueComboBox, 1, gbc);
		
		// Responsible
		Object[] responsible = {};
		try
		{
			responsible = m_application.getDiskoModuleLoader().getRoleTitles();
		} 
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		m_responsibleComboBox = new JComboBox(responsible);
		addComponent(0, m_resources.getString("TaskResponsible.text"), m_responsibleComboBox, 0, gbc);
		
		// Alert
		m_alertComboBox = new JComboBox();
		updateAlertComboBox();
		m_alertComboBox.setEditable(true);
		addComponent(2, m_resources.getString("TaskAlert.text"), m_alertComboBox, 1, gbc);
		
		// Status
		TaskStatus[] statusItems =
		{
			TaskStatus.UNPROCESSED,
			TaskStatus.STARTED,
			TaskStatus.POSTPONED,
			TaskStatus.FINISHED,
		};
		m_statusComboBox = new JComboBox(statusItems);
		m_statusComboBox.setSelectedIndex(0);
		m_statusComboBox.setRenderer(new TaskEnumListCellRenderer());
		m_statusComboBox.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				// If status is set to finished, progress should be set to 100%
				TaskStatus selectedStatus = (TaskStatus)m_statusComboBox.getSelectedItem();
				if(selectedStatus == TaskStatus.FINISHED)
				{
					m_progressComboBox.setSelectedIndex(10);
				}
				updateFieldsEditable();
			}
		});
		addComponent(0, m_resources.getString("TaskStatus.text"), m_statusComboBox, 0, gbc);
		
		// Progress
		String[] progressItems = {"0%", "10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%", "90%", "100%"};
		m_progressComboBox = new JComboBox(progressItems);
//		m_progressTextField.setText(String.valueOf(0));
		addComponent(2, m_resources.getString("TaskProgress.text"), m_progressComboBox, 1, gbc);
		
		// Description
		m_descriptionTextArea = new JTextArea();
		JScrollPane scrollPane = new JScrollPane(m_descriptionTextArea);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		m_descriptionTextArea.setRows(10);
		m_descriptionTextArea.setLineWrap(true);
		m_descriptionTextArea.setWrapStyleWord(true);
		gbc.gridwidth = 3;
		addComponent(0, m_resources.getString("TaskDescription.text"), scrollPane, 10, gbc);
		
		// Use source
		m_useSourceButton = DiskoButtonFactory.createSmallButton(m_resources.getString("TaskUseSource.text"));
		m_useSourceButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				m_descriptionTextArea.setText(m_sourceTextArea.getText());
			}
		});
		gbc.gridwidth = 1;
		gbc.gridheight = 1;
		gbc.fill = GridBagConstraints.NONE;
		gbc.gridy--;
		gbc.gridx = 0;
		m_contentsPanel.add(m_useSourceButton, gbc);
		gbc.gridy++;
		gbc.fill = GridBagConstraints.BOTH;
		
		// Source
		m_sourceTextArea = new JTextArea();
		m_sourceTextArea.setBackground(m_createdTextField.getBackground());
		m_sourceTextArea.setRows(4);
		m_sourceTextArea.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		m_sourceTextArea.setLineWrap(true);
		m_sourceTextArea.setWrapStyleWord(true);
		m_sourceTextArea.setEditable(false);
		gbc.gridwidth = 3;
		addComponent(0, m_resources.getString("TaskSource.text"), m_sourceTextArea, 4, gbc);
		
		// Object
		m_objectTextField = new JTextField();
		m_objectTextField.setEditable(false);
		gbc.gridwidth = 3;
		addComponent(0, m_resources.getString("TaskObject.text"), m_objectTextField, 1, gbc);
		
		// Finish button
		JPanel actionButtonPanel = new JPanel();
		m_finishedButton = DiskoButtonFactory.createSmallButton(ButtonType.FinishedButton);
		m_finishedButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if(timesValid())
				{
					updateTask();
					setVisible(false);
				}
				else
				{
					ErrorDialog error = new ErrorDialog(m_application.getFrame());
					error.showError(m_resources.getString("TimeError.header"),
							m_resources.getString("TimeError.text"));
				}
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
		gbc.gridheight = 1;
		m_contentsPanel.add(label, gbc);
		
		gbc.gridy += numRows;
	}
	
	public void setTask(ITaskIf task)
	{
		m_currentTask = task;
		updateFieldContents();
		updateFieldsEditable();
	}
	
	private boolean timesValid()
	{
		String selectedAlertTimeString = (String)m_alertComboBox.getSelectedItem();
		if(selectedAlertTimeString.equals(m_resources.getString("TaskNoAlert.text")))
		{
			return true;
		}
		// Check that alert time is after creation time.
		// Alert time will always be before due time
		try
		{
			Calendar creationTime = Calendar.getInstance();
			Calendar alertTime = null;
			String alertString = selectedAlertTimeString.split(" ")[0];
			String dueString = (String)m_dueComboBox.getSelectedItem();
			alertTime = DTG.DTGToCal(dueString);
			alertTime.add(Calendar.MINUTE, -Integer.valueOf(alertString));
			return alertTime.after(creationTime);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Decides whether or not fields can be edited, given the current message
	 */
	private void updateFieldsEditable()
	{
		// Check status
		TaskStatus status = (TaskStatus)m_statusComboBox.getSelectedItem();
		IDiskoRole role = m_application.getCurrentRole();
		boolean editable = TaskUtilities.canChangeFields(status) && 
		TaskUtilities.canChangeStatus(role, m_currentTask);	
		
		m_alertComboBox.setEnabled(editable);
		m_descriptionTextArea.setEditable(editable);
		if(editable)
		{
			m_descriptionTextArea.setBackground(Color.white);
		}
		else
		{
			m_descriptionTextArea.setBackground(m_createdTextField.getBackground());
		}
		m_dueComboBox.setEnabled(editable);
		m_priorityComboBox.setEnabled(editable);
		m_progressComboBox.setEnabled(editable);
		m_responsibleComboBox.setEnabled(editable);
		m_taskTextField.setEditable(editable);
		m_typeComboBox.setEnabled(editable);
	}

	/**
	 * Extracts values from swing components and updates/creates MSO task object
	 */
	public void updateTask()
	{
		// Get due time
		String dueTimeString = (String)m_dueComboBox.getSelectedItem();
		Calendar dueTime = null;
		try
		{
			dueTime = DTG.DTGToCal(dueTimeString);
		} 
		catch (IllegalMsoArgumentException e1)
		{
			System.err.println("Error parsing due time DTG, setting due time in 30 min");
			dueTime = Calendar.getInstance();
			dueTime.add(Calendar.MINUTE, 30);
		}
		
		// Create new task if current is set to null
		if(m_currentTask == null)
		{ 
			m_currentTask = m_application.getMsoModel().getMsoManager().createTask(dueTime);
			m_currentTask.setCreated(Calendar.getInstance());
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
		String responsible = (String)m_responsibleComboBox.getSelectedItem();
		m_currentTask.setResponsibleRole(responsible);
		
		// Get alert
		Calendar alertTime = null;
		try
		{
			String alertString = ((String)m_alertComboBox.getSelectedItem()).split(" ")[0];
			alertTime = DTG.DTGToCal((String)(m_dueComboBox.getSelectedItem()));
			alertTime.add(Calendar.MINUTE, -Integer.valueOf(alertString));
		}
		catch(Exception e)
		{
			// Invalid alert, set to null 
			alertTime = null;
		}
		m_currentTask.setAlert(alertTime);
		
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
		int progress = Integer.valueOf(((String)m_progressComboBox.getSelectedItem()).split("%")[0]);
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
	private void updateFieldContents()
	{
		if(m_currentTask != null)
		{
			// Updating an existing task
			
			// Task text
			String taskText = m_currentTask.getTaskText();
			m_taskTextField.setText(taskText);
			
			// Type
			TaskType type = m_currentTask.getType();
			m_typeComboBox.setSelectedItem(type);
			
			// Created
			Calendar created = m_currentTask.getCreated();
			m_createdTextField.setText(DTG.CalToDTG(created));
			
			// Priority
			TaskPriority priority = m_currentTask.getPriority();
			m_priorityComboBox.setSelectedItem(priority);
			
			// Alert
			updateAlertComboBox();
			
			// Due
			updateDueComboBox();
			
			// Responsible
			String responsible = m_currentTask.getResponsibleRole();
			m_responsibleComboBox.setSelectedItem(responsible);
			
			// Status
			TaskStatus status = m_currentTask.getStatus();
			m_statusComboBox.setSelectedItem(status);
			
			// Progress
			int progress = m_currentTask.getProgress();
			m_progressComboBox.setSelectedIndex(progress/10);
			
			// Description
			String description = m_currentTask.getDescription();
			m_descriptionTextArea.setText(description);
			
			// Source
			m_sourceTextArea.setText(m_currentTask.getSourceClassText());
			
			// Object
			IMsoObjectIf object = m_currentTask.getDependentObject();
			m_objectTextField.setText(object == null ? "" : object.shortDescriptor());
		}
		else
		{
			// Updating a new task
			m_typeComboBox.setSelectedItem(TaskType.GENERAL);
			m_createdTextField.setText(DTG.CalToDTG(Calendar.getInstance()));
			m_priorityComboBox.setSelectedItem(TaskPriority.NORMAL);
			updateDueComboBox();
			m_responsibleComboBox.setSelectedItem(null);
			m_alertComboBox.setSelectedIndex(0);
			m_statusComboBox.setSelectedItem(TaskStatus.UNPROCESSED);
			m_progressComboBox.setSelectedIndex(0);
			m_descriptionTextArea.setText("");
			m_sourceTextArea.setText("");
			m_objectTextField.setText("");
		}
	}
	
	private void updateDueComboBox()
	{
		m_dueComboBox.removeAllItems();
		int intervalSize = 15;
		int numItems = 5;
		Calendar dueItem = Calendar.getInstance();
		
		for(int i=0; i<numItems; i++)
		{
			m_dueComboBox.addItem(DTG.CalToDTG(dueItem));
			dueItem.add(Calendar.MINUTE, intervalSize);
		}
		
		m_dueComboBox.setSelectedIndex(2);
	}
	
	private void updateAlertComboBox()
	{
		int[] alertItems = {1, 5, 30, 60};
		
		m_alertComboBox.removeAllItems();
		
		m_alertComboBox.addItem(m_resources.getString("TaskNoAlert.text"));
	
		String alertItem = null;
		for(int i=0; i<alertItems.length; i++)
		{
			alertItem = String.valueOf(alertItems[i]) + " " + m_resources.getString("TaskAlertItem.text");
			m_alertComboBox.addItem(alertItem);
		}
		
		if(m_currentTask != null)
		{
			Calendar alertTime = m_currentTask.getAlert();
			if(alertTime == null)
			{
				m_alertComboBox.setSelectedIndex(0);
			}
			else
			{
				// TODO Calculate alert time from stored alert time
				m_alertComboBox.setSelectedIndex(2);
			}
		}
		else
		{
			m_alertComboBox.setSelectedIndex(2);
		}
	}
	
	/**
	 * Update task text field contents based on selected type, etc.
	 */
	private void updateTaskText()
	{
		// TODO Automatically change text in a stored task or not?
		if(m_currentTask == null)
		{
			String text = "";
			
			TaskType type = (TaskType)m_typeComboBox.getSelectedItem();
			switch(type)
			{
			case TRANSPORT:
				text = m_resources.getString("TaskTransportText.text") + " " + 
				m_objectTextField.getText();
				break;
			case RESOURCE:
				text = m_objectTextField.getText() + " " + 
				m_resources.getString("TaskResourceText.text");
				break;
			case INTELLIGENCE:
				text = m_objectTextField.getText() + " " + 
				m_resources.getString("TaskIntelligenceText.text");
				break;
			case GENERAL:
				text = 	m_resources.getString("TaskGeneralText.text");
				break;
			}
			m_taskTextField.setText(text);
		}
	}
	
	/**
	 * Renders task combo box items as simple label, gets text from {@link TaskImpl#getEnumText(Enum)}
	 * 
	 * @author thomasl
	 */
	@SuppressWarnings("unchecked")
	private class TaskEnumListCellRenderer extends JLabel implements ListCellRenderer
	{
		private static final long serialVersionUID = 1L;
		
		public TaskEnumListCellRenderer()
		{
			this.setOpaque(true);
		}

		public Component getListCellRendererComponent(JList list, Object value,
				int arg2, boolean isSelected, boolean hasFocus)
		{
			this.setText(TaskImpl.getEnumText((Enum)value));
			
			if (isSelected)
	        {
	            setBackground(list.getSelectionBackground());
	            setForeground(list.getSelectionForeground());
	        } else
	        {
	            setBackground(list.getBackground());
	            setForeground(list.getForeground());
	        }
			
			return this;
		}
	}
}
