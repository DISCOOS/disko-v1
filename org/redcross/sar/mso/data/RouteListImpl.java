package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class RouteListImpl extends MsoListImpl<IRouteIf> implements IRouteListIf
{

    public RouteListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IRouteIf createRoute()
    {
        checkCreateOp();
        return createdUniqueItem(new RouteImpl(makeUniqueId()));
    }

    public IRouteIf createRoute(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new RouteImpl(anObjectId));
    }


}