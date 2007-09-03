package org.redcross.sar.wp.tasks;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * Listen for selections made in task panel, inform work process of which is the current task to
 * manipulate
 * 
 * @author thomasl
 */
public class TaskSelectionListener implements ListSelectionListener
{
	protected IDiskoWpTasks m_wpTasks;
	
	public TaskSelectionListener(IDiskoWpTasks wp)
	{
		m_wpTasks = wp;
	}
	public void valueChanged(ListSelectionEvent arg0)
	{
		// Get selected task 
		// Inform work process
	}
}
