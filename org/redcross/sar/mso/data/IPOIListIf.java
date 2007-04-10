package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.mso.Position;

public interface IPOIListIf extends IMsoListIf<IPOIIf>
{
    public IPOIIf createPOI();

    public IPOIIf createPOI(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

    public IPOIIf createPOI(IPOIIf.POIType aType, Position aPosition);

    public IPOIIf createPOI(IMsoObjectIf.IObjectIdIf anObjectId, IPOIIf.POIType aType, Position aPosition) throws DuplicateIdException;

}