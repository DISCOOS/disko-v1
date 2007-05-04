package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalMsoArgumentException;

import java.util.Calendar;

public class ForecastListImpl extends MsoListImpl<IForecastIf> implements IForecastListIf
{
    public ForecastListImpl(IMsoObjectIf anOwner, String theName, boolean isMain)
    {
        super(anOwner, theName, isMain);
    }

    public ForecastListImpl(IMsoObjectIf anOwner, String theName, boolean isMain, int aSize)
    {
        super(anOwner, theName, isMain, aSize);
    }

    public IForecastIf createForecast(Calendar aCalendar, String aText)
    {
        checkCreateOp();
        return createdUniqueItem(new ForecastImpl(makeUniqueId(), aCalendar, aText));
    }

    public IForecastIf createForecast(String aDTG, String aText) throws IllegalMsoArgumentException
    {
        checkCreateOp();
        return createdUniqueItem(new ForecastImpl(makeUniqueId(), aDTG, aText));
    }

    public IForecastIf createForecast(long aDTG, String aText) throws IllegalMsoArgumentException
    {
        checkCreateOp();
        return createdUniqueItem(new ForecastImpl(makeUniqueId(), aDTG, aText));
    }

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        checkCreateOp();
        IForecastIf retVal = getItem(anObjectId);
        return retVal != null ? retVal : createdItem(new ForecastImpl(anObjectId));
    }

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, String aDTG, String aText) throws IllegalMsoArgumentException
    {
        checkCreateOp();
        IForecastIf retVal = getItem(anObjectId);
        return retVal != null ? retVal : createdItem(new ForecastImpl(anObjectId, aDTG, aText));
    }

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG, String aText) throws IllegalMsoArgumentException
    {
        checkCreateOp();
        IForecastIf retVal = getItem(anObjectId);
        return retVal != null ? retVal : createdItem(new ForecastImpl(anObjectId, aDTG, aText));
    }
}