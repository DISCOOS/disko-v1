package org.redcross.sar.wp.tasks;

import javax.swing.table.AbstractTableModel;

import org.redcross.sar.mso.data.ITaskListIf;
import org.redcross.sar.mso.data.TaskListImpl;

public class TaskTableModel extends AbstractTableModel
{
	private static final long serialVersionUID = 1L;
	
	protected ITaskListIf m_tasks;
	protected IDiskoWpTasks m_wpTasks;
	
	public TaskTableModel(IDiskoWpTasks wp)
	{
		m_wpTasks = wp;
//		m_tasks = new TaskListImpl();
	}
	
	public int getColumnCount()
	{
		return 6;
	}

	public int getRowCount()
	{
//		return m_tasks.size();
		return 0;
	}
	
    @Override
    public String getColumnName(int column)
    {
    	return m_wpTasks.getText("TableHeader" + column + ".text");
    }


	public Object getValueAt(int arg0, int arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

}
