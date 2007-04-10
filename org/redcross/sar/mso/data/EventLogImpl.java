package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Calendar;

public class EventLogImpl extends MsoListImpl<IEventIf> implements IEventLogIf
{

    public EventLogImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public EventLogImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IEventIf createEvent(Calendar aCalendar)
    {
        checkCreateOp();
        return createdUniqueItem(new EventImpl(makeUniqueId(), makeSerialNumber(), aCalendar));
    }

    public IEventIf createEvent(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber, Calendar aCalendar) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new EventImpl(anObjectId, aNumber, aCalendar));
    }


}