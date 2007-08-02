package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;

import java.util.Calendar;

public class TaskImpl extends AbstractTimeItem implements ITaskIf
{
    private final MsoReferenceImpl<IEventIf> m_createdEvent = new MsoReferenceImpl<IEventIf>(this, "CreatedEvent", true);

    public TaskImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public TaskImpl(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar)
    {
        super(anObjectId, aCalendar);
    }

    @Override
    protected void defineAttributes()
    {
        super.defineAttributes();
    }

    @Override
    protected void defineLists()
    {
        super.defineLists();
    }

    @Override
    protected void defineReferences()
    {
        super.defineReferences();
        addReference(m_createdEvent);
    }

    @Override
    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.addObjectReference(anObject,aReferenceName);
    }

    @Override
    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.removeObjectReference(anObject,aReferenceName);
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


    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public Calendar getDueTime()
    {
        return getCalendar();
    }

    public void setDueTime(Calendar aCalendar)
    {
        setCalendar(aCalendar);
    }


    public IMsoModelIf.ModificationState getDueTimeState()
    {
        return getCalendarState();
    }
}