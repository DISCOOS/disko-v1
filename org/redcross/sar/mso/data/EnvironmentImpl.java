package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.IllegalMsoArgumentException;

import java.util.Calendar;

public class EnvironmentImpl extends AbstractTimeItem implements IEnvironmentIf
{
    private final AttributeImpl.MsoString m_someText = new AttributeImpl.MsoString(this, "someText");

    public EnvironmentImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public EnvironmentImpl(IMsoObjectIf.IObjectIdIf anObjectId,Calendar aCalendar, String aText)
    {
        super(anObjectId,aCalendar);
        setText(aText);
    }

    public EnvironmentImpl(IMsoObjectIf.IObjectIdIf anObjectId,String aDTG, String aText) throws IllegalMsoArgumentException
    {
        super(anObjectId,aDTG);
        setText(aText);
    }

    public EnvironmentImpl(IMsoObjectIf.IObjectIdIf anObjectId,long aDTG, String aText) throws IllegalMsoArgumentException
    {
        super(anObjectId, aDTG);
        setText(aText);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_someText);
    }

    protected void defineLists()
    {
        super.defineLists();
    }

    protected void defineReferences()
    {
        super.defineReferences();
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
        return super.toString() + " EnvironmentImpl: " + m_someText.getString();
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_ENVIRONMENT;
    }


}