package org.redcross.sar.mso.data;

public class VehicleImpl extends AbstractTransportUnit implements IVehicleIf
{
    public VehicleImpl(IMsoObjectIf.IObjectIdIf anObjectId, long aNumber, String aKjennetegn, int aHastighet) 
    {
        super(anObjectId, aNumber, aKjennetegn, aHastighet);
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

    public String toString()
    {
        return super.toString();
    }
}