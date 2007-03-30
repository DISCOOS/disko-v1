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

    public VehicleImpl createVehicle(long aNumber, String aKjennetegn, int aSpeed)
    {
        checkCreateOp();
        return (VehicleImpl)createdUniqueItem( new VehicleImpl(makeUniqueId(), aNumber, aKjennetegn, aSpeed));
    }

    public VehicleImpl createVehicle(IMsoObjectIf.IObjectIdIf anObjectId, long aNumber, String aKjennetegn, int aSpeed) throws DuplicateIdException
    {
        checkCreateOp();
        return (VehicleImpl)createdItem( new VehicleImpl(anObjectId, aNumber, aKjennetegn, aSpeed));
    }
}