package org.redcross.sar.wp.tasks;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import org.redcross.sar.gui.DiskoButtonFactory;
import org.redcross.sar.gui.DiskoDialog;
import org.redcross.sar.gui.DiskoButtonFactory.ButtonType;
import org.redcross.sar.mso.data.ITaskIf;

/**
 * Dialog for deleting a task
 * @author thomasl
 */
public class DeleteTaskDialog extends DiskoDialog
{
	private static final long serialVersionUID = 1L;
	
	protected IDiskoWpTasks m_wpTasks;
	
	protected ITaskIf m_currentTask;
	
	protected JPanel m_contentsPanel;
	protected JTextField m_taskTextField;
	protected JTextArea m_descriptionTextArea;
	protected JButton m_cancelButton;
	protected JButton m_deleteButton;
	
	public DeleteTaskDialog(IDiskoWpTasks wp)
	{
		super(wp.getApplication().getFrame());
		m_wpTasks = wp;
		
		initialize();
	}
	
	/**
	 * Initialize GUI
	 */
	private void initialize()
	{
		m_contentsPanel = new JPanel(new GridBagLayout());
		m_contentsPanel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.gridx = 0;
		gbc.gridy = 0;
		
		JLabel headerLabel = new JLabel(m_wpTasks.getText("DeleteTask.text"));
		gbc.gridwidth = 2;
		m_contentsPanel.add(headerLabel, gbc);
		
		JLabel taskLabel = new JLabel(m_wpTasks.getText("Task.text"));
		gbc.gridwidth = 1;
		gbc.gridy++;
		m_contentsPanel.add(taskLabel, gbc);
		
		m_taskTextField = new JTextField();
		m_taskTextField.setColumns(30);
		gbc.gridx++;
		m_contentsPanel.add(m_taskTextField, gbc);
		
		JLabel descriptionLabel = new JLabel(m_wpTasks.getText("TaskDescription.text"));
		gbc.gridx--;
		gbc.gridy++;
		m_contentsPanel.add(descriptionLabel, gbc);
		
		m_descriptionTextArea = new JTextArea();
		m_descriptionTextArea.setRows(5);
		m_descriptionTextArea.setColumns(30);
		m_descriptionTextArea.setBorder(BorderFactory.createLineBorder(Color.lightGray));
		gbc.gridx++;
		m_contentsPanel.add(m_descriptionTextArea, gbc);
		
		JPanel buttonRowPanel = new JPanel();
		m_deleteButton = DiskoButtonFactory.createSmallButton(ButtonType.DeleteButton);
		m_deleteButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				delete();
			}
		});
		buttonRowPanel.add(m_deleteButton);
		m_cancelButton = DiskoButtonFactory.createSmallButton(ButtonType.CancelButton);
		m_cancelButton.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				cancelDelete();
			}
		});
		buttonRowPanel.add(m_cancelButton);
		gbc.gridwidth = 2;
		gbc.gridx--;
		gbc.gridy++;
		m_contentsPanel.add(buttonRowPanel, gbc);
		
		this.add(m_contentsPanel);
		this.pack();
	}
	
	/**
	 * Cancel delete action
	 */
	private void cancelDelete()
	{
		this.setVisible(false);
	}
	
	/**
	 * Finish delete action, delete selected task
	 */
	private void delete()
	{
		if(m_currentTask != null)
		{
			// TODO Delete selected task
		}
		this.setVisible(false);
	}
}
