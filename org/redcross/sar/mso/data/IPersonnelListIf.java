package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

/**
 *
 */
public interface IPersonnelListIf extends IMsoListIf<IPersonnelIf>
{
    public IPersonnelIf createPersonnel(String aPersonellId);

    public IPersonnelIf createPersonnel(IMsoObjectIf.IObjectIdIf anObjectId, String aPersonellId) throws DuplicateIdException;
}
