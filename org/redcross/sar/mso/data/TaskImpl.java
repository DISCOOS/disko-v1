package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;

import java.util.Calendar;

public class TaskImpl extends AbstractTimeItem implements ITaskIf
{
    private final MsoReferenceImpl<IEventIf> m_createdEvent = new MsoReferenceImpl<IEventIf>(this, "CreatedEvent", true);

    public TaskImpl(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar)

    {
        super(anObjectId, aCalendar);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
    }

    protected void defineLists()
    {
        super.defineLists();
    }

    protected void defineReferences()
    {
        super.defineReferences();
        addReference(m_createdEvent);
    }

    public static TaskImpl implementationOf(ITaskIf anInterface) throws MsoCastException
    {
        try
        {
            return (TaskImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to TaskImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_TASK;
    }
}