package org.redcross.sar.mso.data;

public class VehicleImpl extends AbstractTransportUnit implements IVehicleIf
{
    public VehicleImpl(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber)
    {
        super(anObjectId, aNumber);
    }

    public VehicleImpl(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber, String anIdentifier)
    {
        super(anObjectId, aNumber, anIdentifier);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
    }

    protected void defineLists()
    {
        super.defineLists();
    }

    protected void defineReferences()
    {
        super.defineReferences();
    }

    @Override
    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.addObjectReference(anObject, aReferenceName);
    }

    @Override
    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.removeObjectReference(anObject, aReferenceName);
    }

    protected UnitType getTypeBySubclass()
    {
        return IUnitIf.UnitType.VEHICLE;
    }


    public String getSubTypeName()
    {
        return "CAR"; // todo expand
    }

    public String toString()
    {
        return super.toString();
    }
}