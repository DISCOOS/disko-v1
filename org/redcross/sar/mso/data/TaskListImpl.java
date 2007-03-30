package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Calendar;

public class TaskListImpl extends MsoListImpl<ITaskIf> implements ITaskListIf
{

    public TaskListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public TaskListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public ITaskIf createTask(Calendar aCalendar)
    {
        checkCreateOp();
        return createdUniqueItem(new TaskImpl(makeUniqueId(), aCalendar));
    }

    public ITaskIf createTask(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new TaskImpl(anObjectId, aCalendar));
    }
}