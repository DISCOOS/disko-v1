package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalMsoArgumentException;

import java.util.Calendar;

public interface IForecastListIf extends IMsoListIf<IForecastIf>
{
    public IForecastIf createForecast(String aText);

    public IForecastIf createForecast(Calendar aCalendar, String aText);

    public IForecastIf createForecast(String aDTG, String aText) throws IllegalMsoArgumentException;

    public IForecastIf createForecast( long aDTG, String aText) throws IllegalMsoArgumentException;

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, String aText) throws DuplicateIdException;

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId,Calendar aCalendar, String aText) throws DuplicateIdException;

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId,String aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;

    public IForecastIf createForecast(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG, String aText) throws DuplicateIdException, IllegalMsoArgumentException;


}