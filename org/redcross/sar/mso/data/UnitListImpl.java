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

    public VehicleImpl createVehicle(String anIdentifier)
    {
        checkCreateOp();
        return (VehicleImpl) createdUniqueItem(new VehicleImpl(makeUniqueId(), makeSerialNumber(), anIdentifier));
    }

    public VehicleImpl createVehicle(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber, String anIdentifier) throws DuplicateIdException
    {
        checkCreateOp();
        return (VehicleImpl) createdItem(new VehicleImpl(anObjectId, aNumber, anIdentifier));
    }
}