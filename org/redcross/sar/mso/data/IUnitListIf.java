package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

/**
 *
 */
public interface IUnitListIf extends IMsoListIf<IUnitIf>
{
    public VehicleImpl createVehicle(long aNumber, String aKjennetegn, int aSpeed);

    public VehicleImpl createVehicle(IMsoObjectIf.IObjectIdIf anObjectId, long aNumber, String aKjennetegn, int aSpeed) throws DuplicateIdException;
}
