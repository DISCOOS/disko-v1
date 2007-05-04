package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

/**
 *
 */
public interface IUnitListIf extends IMsoListIf<IUnitIf>
{
    public IVehicleIf createVehicle(String anIdentifier);

    public IVehicleIf createVehicle(IMsoObjectIf.IObjectIdIf anObjectId);

    /**
     * Find a unit with a given unit number.
     * @param aUnitNr The required number
     * @return The actual unit, or <code>null</code> if non-existent.
     */
    public IUnitIf getUnit(int aUnitNr);
}
