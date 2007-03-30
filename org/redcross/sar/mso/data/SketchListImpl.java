package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class SketchListImpl extends MsoListImpl<ISketchIf> implements ISketchListIf
{

    public SketchListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public SketchListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public ISketchIf createSketch()
    {
        checkCreateOp();
        return createdUniqueItem(new SketchImpl(makeUniqueId()));
    }

    public ISketchIf createSketch(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new SketchImpl(anObjectId));
    }

}