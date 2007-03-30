package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public interface IPOIListIf extends IMsoListIf<IPOIIf>
{

    public IPOIIf createPOI();


    public IPOIIf createPOI(IMsoObjectIf.IObjectIdIf anObjectId) throws DuplicateIdException;

}