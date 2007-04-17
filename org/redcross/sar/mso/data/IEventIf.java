package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Calendar;
import java.util.Collection;

public interface IEventIf extends ITimeItemIf, ISerialNumberedIf
{
    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addEventTask(ITaskIf anITaskIf) throws DuplicateIdException;

    public ITaskListIf getEventTasks();

    public IMsoModelIf.ModificationState getEventTasksState(ITaskIf anITaskIf);

    public Collection<ITaskIf> getEventTasksItems();

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public Calendar getEventTime();

    public void setEventTime();
}
