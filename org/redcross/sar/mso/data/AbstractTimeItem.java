package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.mso.DTG;

import java.util.Calendar;

public abstract class AbstractTimeItem extends AbstractMsoObject implements ITimeItemIf
{
    protected final AttributeImpl.MsoCalendar m_calendar = new AttributeImpl.MsoCalendar(this, "TimeStamp");
    protected final AttributeImpl.MsoBoolean m_visible = new AttributeImpl.MsoBoolean(this, "Visible");

    public AbstractTimeItem(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        this(anObjectId, null, true);
    }

    public AbstractTimeItem(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar)
    {
        this(anObjectId, aCalendar, true);
    }

    public AbstractTimeItem(IMsoObjectIf.IObjectIdIf anObjectId, String aDTG) throws IllegalMsoArgumentException
    {
        this(anObjectId, DTG.DTGToCal(aDTG), true);
    }

    public AbstractTimeItem(IMsoObjectIf.IObjectIdIf anObjectId, long aDTG) throws IllegalMsoArgumentException
    {
        this(anObjectId, DTG.DTGToCal(aDTG), true);
    }

    public AbstractTimeItem(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar, boolean aVisible)
    {
        super(anObjectId);
//        System.out.println("AbstractTimeItem");
        m_calendar.setValue(aCalendar);
        setVisible(aVisible);
    }

    protected void defineAttributes()
    {
        addAttribute(m_calendar);
        addAttribute(m_visible);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }

    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    public String getDTG()
    {
        return m_calendar.getDTG();
    }

    public void setCalendar(Calendar aDTG)
    {
        m_calendar.setValue(aDTG);
    }

    public Calendar getCalendar()
    {
        return m_calendar.getCalendar();
    }

    public IMsoModelIf.ModificationState getCalendarState()
    {
        return m_calendar.getState();
    }

    public IAttributeIf.IMsoCalendarIf getCalendarAttribute()
    {
        return m_calendar;
    }

    public void setDTG(Long aDTG) throws IllegalMsoArgumentException
    {
        m_calendar.setValue(aDTG);
    }

    public void setDTG(String aDTG) throws IllegalMsoArgumentException
    {
        m_calendar.setValue(aDTG);
    }

    /**
     * Compare time with another ITimeItemIf object.
     * The following rule applies: A <code>null</code>-reference element is considered to be greater (later) than all other objects,
     * two <code>null</code>-reference elements are equal.
     *
     * @param aTimeObject The object to compare.
     * @return As defined in java.lang.Comparable
     */
    public int compareTo(ITimeItemIf aTimeObject)
    {
        if (aTimeObject != null)
        {
            if (m_calendar != null && aTimeObject.getCalendar() != null)
            {
                return m_calendar.getCalendar().compareTo(aTimeObject.getCalendar());
            } else if (m_calendar == null && aTimeObject.getCalendar() == null)
            {
                return 0;
            } else if (aTimeObject.getCalendar() == null)
            {
                return 1;
            } else
            {
                return -1;
            }
        }
        return 1;
    }

    public void setVisible(boolean aVisible)
    {
        m_visible.setValue(aVisible);
    }

    public boolean isVisible()
    {
        return m_visible.booleanValue();
    }

    public String toString()
    {
        return "Item " + getDTG();
    }

    public IMsoModelIf.ModificationState getVisibleState()
    {
        return m_visible.getState();
    }

    public IAttributeIf.IMsoBooleanIf getVisibleAttribute()
    {
        return m_visible;
    }


}