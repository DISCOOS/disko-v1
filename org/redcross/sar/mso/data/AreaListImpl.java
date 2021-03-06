package org.redcross.sar.mso.data;

public class AreaListImpl extends MsoListImpl<IAreaIf> implements IAreaListIf
{
    public AreaListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public AreaListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IAreaIf createArea()
    {
        checkCreateOp();
        return createdUniqueItem(new AreaImpl(makeUniqueId()));
    }

    public IAreaIf createArea(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        checkCreateOp();
        IAreaIf retVal = getItem(anObjectId);
        return retVal != null ? retVal : createdItem(new AreaImpl(anObjectId));
    }
}