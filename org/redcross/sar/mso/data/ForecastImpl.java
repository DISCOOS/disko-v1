package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.IllegalMsoArgumentException;

import java.util.Calendar;

public class ForecastImpl extends AbstractTimeItem implements IForecastIf
{
    private final AttributeImpl.MsoString m_someText = new AttributeImpl.MsoString(this, "someText");

    public ForecastImpl(IMsoObjectIf.IObjectIdIf anObjectId, String aText)
    {
        super(anObjectId);
        setText(aText);
    }

    public ForecastImpl(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar, String aText)
    {
        super(anObjectId, aCalendar);
        setText(aText);
    }

    public ForecastImpl(IMsoObjectIf.IObjectIdIf anObjectId, String aDTG, String aText) throws IllegalMsoArgumentException
    {
        super(anObjectId, aDTG);
        setText(aText);
    }

    public ForecastImpl(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG, String aText) throws IllegalMsoArgumentException
    {
        super(anObjectId, aDTG);
        setText(aText);
    }

    protected void defineAttributes()
    {
        addAttribute(m_someText);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }

    public void setText(String aText)
    {
        m_someText.setValue(aText);
    }

    public String getText()
    {
        return m_someText.getString();
    }

    public String toString()
    {
        return super.toString() + " Forecast: " + m_someText.getString();
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_FORECAST;
    }


}