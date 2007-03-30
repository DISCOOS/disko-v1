package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public interface IRouteListIf extends IMsoListIf<IRouteIf>
{
    public IRouteIf createRoute();

    public IRouteIf createRoute(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

}