package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;

public class UnitListImpl extends MsoListImpl<IUnitIf> implements IUnitListIf
{
    public UnitListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public UnitListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IVehicleIf createVehicle(String anIdentifier)
    {
        checkCreateOp();
        return (VehicleImpl) createdUniqueItem(new VehicleImpl(makeUniqueId(), makeSerialNumber(), anIdentifier));
    }

    public IVehicleIf createVehicle(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        checkCreateOp();
        IVehicleIf retVal = (IVehicleIf) getItem(anObjectId);
        return retVal != null ? retVal : (IVehicleIf) createdItem(new VehicleImpl(anObjectId, -1));
    }

    public IUnitIf getUnit(int aUnitNr)
    {
        for (IUnitIf unit : getItems())
        {
            if (unit.getNumber() == aUnitNr)
            {
                return unit;
            }
        }
        return null;
    }
}