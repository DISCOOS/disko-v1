package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public interface IAreaListIf extends IMsoListIf<IAreaIf>
{

    public IAreaIf createArea();

    public IAreaIf createArea(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

}