package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

/**
 *
 */
public interface ICmdPostListIf extends IMsoListIf<ICmdPostIf>
{
    public ICmdPostIf createCmdPost() throws DuplicateIdException;

    public ICmdPostIf createCmdPost(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;
}
