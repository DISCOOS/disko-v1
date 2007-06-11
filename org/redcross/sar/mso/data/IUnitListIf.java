package org.redcross.sar.mso.data;

/**
 *
 */
public interface IUnitListIf extends IMsoListIf<IUnitIf>
{
    public IVehicleIf createVehicle(String anIdentifier);

    public IVehicleIf createVehicle(IMsoObjectIf.IObjectIdIf anObjectId);

    public IBoatIf createBoat(String anIdentifier);

    public IBoatIf createBoat(IMsoObjectIf.IObjectIdIf anObjectId);

    /**
     * Find a unit with a given unit number.
     * @param aUnitNr The required number
     * @return The actual unit, or <code>null</code> if non-existent.
     */
    public IUnitIf getUnit(int aUnitNr);
}
