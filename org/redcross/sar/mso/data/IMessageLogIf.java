package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Calendar;

public interface IMessageLogIf extends IMsoListIf<IMessageIf>
{
    public IMessageIf createMessage() throws DuplicateIdException;

    public IMessageIf createMessage(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber) throws DuplicateIdException;

    public IMessageIf createMessage(Calendar aCalendar) throws DuplicateIdException;

    public IMessageIf createMessage(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber, Calendar aCalendar) throws DuplicateIdException;

}