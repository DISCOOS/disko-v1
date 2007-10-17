package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;

import java.util.Calendar;

public class ForecastImpl extends AbstractTimeItem implements IForecastIf
{
    private final AttributeImpl.MsoString m_someText = new AttributeImpl.MsoString(this, "someText");

    public ForecastImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public ForecastImpl(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar, String aText)
    {
        super(anObjectId, aCalendar);
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

    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.addObjectReference(anObject,aReferenceName);
    }

    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.removeObjectReference(anObject,aReferenceName);
    }


    public void setText(String aText)
    {
        m_someText.setValue(aText);
    }

    public String getText()
    {
        return m_someText.getString();
    }

//    public String toString()
//    {
//        return super.toString() + " Forecast: " + m_someText.getString();
//    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_FORECAST;
    }


}