package org.redcross.sar.mso.data;

import org.redcross.sar.mso.data.IMsoObjectIf.IObjectIdIf;

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

    private int makeUnitSerialNumber(Class aClass)
    {
        int retVal = 0;
        for (IUnitIf item : getItems())
        {
            if (item.getClass() == aClass && item.getNumber() > retVal)
            {
                retVal = item.getNumber();
            }
        }
        return retVal + 1;
    }

    void rearrangeUnitSerialNumber(Class aClass)
    {
        // todo make code
        for (IUnitIf item : getItems())
        {
        }
    }

    public IVehicleIf createVehicle(String anIdentifier)
    {
        checkCreateOp();
        return (VehicleImpl) createdUniqueItem(new VehicleImpl(makeUniqueId(), makeUnitSerialNumber(VehicleImpl.class), anIdentifier));
    }

    public IVehicleIf createVehicle(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        checkCreateOp();
        IVehicleIf retVal = (IVehicleIf) getItem(anObjectId);
        return retVal != null ? retVal : (IVehicleIf) createdItem(new VehicleImpl(anObjectId, -1));
    }

    public IBoatIf createBoat(String anIdentifier)
    {
        checkCreateOp();
        return (BoatImpl) createdUniqueItem(new BoatImpl(makeUniqueId(), makeUnitSerialNumber(BoatImpl.class), anIdentifier));
    }

    public IBoatIf createBoat(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        checkCreateOp();
        IBoatIf retVal = (IBoatIf) getItem(anObjectId);
        return retVal != null ? retVal : (IBoatIf) createdItem(new BoatImpl(anObjectId, -1));
    }
    
    public IDogIf createDog(IObjectIdIf objectId)
    {
    	checkCreateOp();
    	IDogIf retVal = (IDogIf)getItem(objectId);
    	return retVal != null ? retVal : (IDogIf) createdItem(new DogImpl(objectId, -1));
    }

	public IAircraftIf createAircraft(IObjectIdIf objectId)
	{
		checkCreateOp();
		IAircraftIf retVal = (IAircraftIf)getItem(objectId);
		return retVal != null ? retVal : (IAircraftIf)createdItem(new AircraftImpl(objectId, -1));
	}

	public ITeamIf createTeam(IObjectIdIf objectId)
	{
		checkCreateOp();
		ITeamIf retVal = (ITeamIf)getItem(objectId);
		return retVal != null ? retVal : (ITeamIf)createdItem(new TeamImpl(objectId, -1));
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