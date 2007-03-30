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

    public IForecastIf createForecast(String aText)
    {
        checkCreateOp();
        return createdUniqueItem(new ForecastImpl(makeUniqueId(), aText));
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

    public IForecastIf createForecast( long aDTG, String aText) throws IllegalMsoArgumentException
    {
        checkCreateOp();
        return createdUniqueItem(new ForecastImpl(makeUniqueId(), aDTG, aText));
    }

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, String aText) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new ForecastImpl(anObjectId, aText));
    }

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId,Calendar aCalendar, String aText) throws DuplicateIdException
    {
        checkCreateOp();
        return createdItem(new ForecastImpl(anObjectId, aCalendar, aText));
    }

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId,String aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException
    {
        checkCreateOp();
        return createdItem(new ForecastImpl(anObjectId, aDTG, aText));
    }

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException
    {
        checkCreateOp();
        return createdItem(new ForecastImpl(anObjectId, aDTG, aText));
    }
}