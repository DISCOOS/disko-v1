package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

/**
 *
 */
public interface IUnitListIf extends IMsoListIf<IUnitIf>
{
    public IVehicleIf createVehicle(String anIdentifier);

    public IVehicleIf createVehicle(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber, String anIdentifier) throws DuplicateIdException;
}
