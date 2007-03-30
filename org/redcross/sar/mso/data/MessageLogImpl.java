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
        return createMessage(null,null);
    }

    public IMessageIf createMessage(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        return createMessage(anObjectId,null);
    }

    public IMessageIf createMessage(Calendar aCalendar) throws DuplicateIdException
    {
        return createMessage(null,aCalendar);
    }

    public IMessageIf createMessage(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new MessageImpl(anObjectId, aCalendar));
    }


}