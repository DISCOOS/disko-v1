package org.redcross.sar.wp.tasks;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.redcross.sar.mso.data.ITaskIf;

/**
 * Listen for selections made in task panel, inform work process of which is the current task to
 * manipulate
 * 
 * @author thomasl
 */
public class TaskSelectionListener implements ListSelectionListener
{
	protected IDiskoWpTasks m_wpTasks;
	protected TaskTableModel m_model;
	
	public TaskSelectionListener(IDiskoWpTasks wp, TaskTableModel model)
	{
		m_wpTasks = wp;
		m_model = model;
	}
	public void valueChanged(ListSelectionEvent lse)
	{
		// Get selected task 
		ListSelectionModel lsm = (ListSelectionModel)lse.getSource();
		
		int index = lsm.getMinSelectionIndex();
		
		// Inform work process of selection
		ITaskIf task = m_model.getTask(index);
		
		m_wpTasks.setCurrentTask(task);
	}
}
