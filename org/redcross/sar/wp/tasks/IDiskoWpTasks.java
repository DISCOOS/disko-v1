package org.redcross.sar.wp.tasks;

import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.wp.IDiskoWpModule;

public interface IDiskoWpTasks extends IDiskoWpModule
{
	public void setCurrentTask(ITaskIf task);
}
