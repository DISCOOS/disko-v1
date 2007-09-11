package org.redcross.sar.wp.tasks;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.mso.data.ITaskIf;
import org.redcross.sar.mso.data.ITaskIf.TaskStatus;

/**
 * Utility class for handling business logic in tasks
 * 
 * @author thomasl
 */
public class TaskUtilities
{
	/**
	 * @param role
	 * @param task
	 * @return Whether role can change task status or not
	 */
	public static boolean canChangeStatus(IDiskoRole role, ITaskIf task)
	{
		if(task == null)
		{
			return true;
		}
		
		String responsible = task.getResponsibleRole();
		if(responsible == null || responsible.equals(role.getTitle()))
		{
			return true;
		}
		
		// TODO Check creating role, implemented in task? (11.09.07)
		
		return false;
	}
	
	/**
	 * @param task
	 * @return Whether task can change it's fields or not
	 */
	public static boolean canChangeFields(TaskStatus status)
	{
		switch(status)
		{
		case UNPROCESSED:
		case STARTED:
		case POSTPONED:
			return true;
		case FINISHED:
			return false;
		default:
			return false;
		}
	}
}
