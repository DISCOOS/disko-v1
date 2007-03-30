package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class POIListImpl extends MsoListImpl<IPOIIf> implements IPOIListIf
{

    public POIListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public POIListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IPOIIf createPOI()
    {
        checkCreateOp();
        return createdUniqueItem(new POIImpl(makeUniqueId()));
    }

    public IPOIIf createPOI(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new POIImpl(anObjectId));
    }


}