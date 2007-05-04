package org.redcross.sar.mso.data;

import org.redcross.sar.util.mso.Route;
import org.redcross.sar.util.except.DuplicateIdException;

public interface IRouteListIf extends IMsoListIf<IRouteIf>
{
    public IRouteIf createRoute(Route aRoute);

    public IRouteIf createRoute(IMsoObjectIf.IObjectIdIf anObjectId, Route aRoute);

}