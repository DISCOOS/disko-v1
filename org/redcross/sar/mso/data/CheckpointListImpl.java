package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class CheckpointListImpl extends MsoListImpl<ICheckpointIf> implements ICheckpointListIf
{

    public CheckpointListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public CheckpointListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public ICheckpointIf createCheckPoint()
    {
        checkCreateOp();
        return createdUniqueItem(new CheckpointImpl(makeUniqueId()));
    }

    public ICheckpointIf createCheckPoint(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new CheckpointImpl(anObjectId));
    }


}