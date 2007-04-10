package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Calendar;

public class MessageLogImpl extends MsoListImpl<IMessageIf> implements IMessageLogIf
{

    public MessageLogImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public MessageLogImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IMessageIf createMessage() throws DuplicateIdException
    {
        checkCreateOp();
        return createdUniqueItem(new MessageImpl(makeUniqueId(), makeSerialNumber()));
    }

    public IMessageIf createMessage(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new MessageImpl(anObjectId, aNumber));
    }

    public IMessageIf createMessage(Calendar aCalendar) throws DuplicateIdException
    {
        checkCreateOp();
        return createdUniqueItem(new MessageImpl(makeUniqueId(), makeSerialNumber(), aCalendar));
    }

    public IMessageIf createMessage(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber, Calendar aCalendar) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new MessageImpl(anObjectId, aNumber, aCalendar));
    }


}