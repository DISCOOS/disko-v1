package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class CmdPostListImpl extends MsoListImpl<ICmdPostIf> implements ICmdPostListIf
{

    public CmdPostListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public ICmdPostIf createCmdPost() throws DuplicateIdException
    {
        if (size() > 0)
        {
            throw new DuplicateIdException();
        }
        checkCreateOp();
        return createdUniqueItem(new CmdPostImpl(makeUniqueId()));
    }

    public ICmdPostIf createCmdPost(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new CmdPostImpl(anObjectId));
    }


}