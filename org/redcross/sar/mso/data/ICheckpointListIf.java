package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public interface ICheckpointListIf extends IMsoListIf<ICheckpointIf>
{
    public ICheckpointIf createCheckPoint();

    public ICheckpointIf createCheckPoint(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;
}