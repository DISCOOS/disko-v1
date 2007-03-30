package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class CalloutListImpl extends MsoListImpl<ICalloutIf> implements ICalloutListIf
{

    public CalloutListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public CalloutListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public ICalloutIf createCallout()
    {
        checkCreateOp();
        return createdUniqueItem(new CalloutImpl(makeUniqueId()));
    }

    public ICalloutIf createCallout(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new CalloutImpl(anObjectId));
    }

}