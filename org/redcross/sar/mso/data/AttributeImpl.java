package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.mso.*;
import org.redcross.sar.util.error.MsoError;

import java.awt.geom.Point2D;
import java.util.Calendar;
import java.util.Vector;

/**
 * Generic class for all MSO attrubutes
 */
public abstract class AttributeImpl<T> implements IAttributeIf<T>, Comparable<AttributeImpl<T>>
{
    protected final Class m_class;
    private final AbstractMsoObject m_owner;
    private final String m_name;
    private int m_indexNo;
    private boolean m_required = false;

    protected T m_localValue = null;
    protected T m_serverValue = null;
    protected IMsoModelIf.ModificationState m_state = IMsoModelIf.ModificationState.STATE_UNDEFINED;

    protected AttributeImpl(Class aClass, AbstractMsoObject theOwner, String theName, int theIndexNo, T theValue)
    {
        m_class = aClass;
        m_owner = theOwner;
        m_name = theName;
        m_indexNo = theIndexNo;
        if (theValue != null)
        {
            setAttrValue(theValue, true);
        }
    }

    /**
     * Compare attribute indices, used for sorting.
     *
     * @param anObject Object to compare
     * @return As {@link Comparable#compareTo(Object)}
     */
    public int compareTo(AttributeImpl<T> anObject)
    {
        return getIndexNo() - anObject.getIndexNo();
    }

    public String getName()
    {
        return m_name;
    }

    protected T getAttrValue()
    {
        return m_state == IMsoModelIf.ModificationState.STATE_LOCAL ? m_localValue : m_serverValue;
    }

    public IMsoModelIf.ModificationState getState()
    {
        return m_state;
    }

    protected void setAttrValue(T aValue)
    {
        setAttrValue(aValue, false);
    }

    protected void setAttrValue(T aValue, boolean isCreating)
    {
        IMsoModelIf.UpdateMode updateMode = MsoModelImpl.getInstance().getUpdateMode();
        IMsoModelIf.ModificationState newState;
        boolean valueChanged = false;
        switch (updateMode)
        {
            case LOOPBACK_UPDATE_MODE:
            {
                newState = IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL;
                if (m_serverValue != aValue)
                {
                    m_serverValue = aValue;
                    valueChanged = true;
                }
                break;
            }
            case REMOTE_UPDATE_MODE:
            {
                newState = m_state == IMsoModelIf.ModificationState.STATE_LOCAL ? IMsoModelIf.ModificationState.STATE_CONFLICTING : IMsoModelIf.ModificationState.STATE_SERVER_MODIFIED;
                if (m_serverValue != aValue)
                {
                    m_serverValue = aValue;
                    valueChanged = true;
                }
                break;
            }
            default:
            {
                newState = IMsoModelIf.ModificationState.STATE_LOCAL;
                if (m_localValue != aValue)
                {
                    m_localValue = aValue;
                    valueChanged = true;
                }
            }
        }
        if (m_state != newState)
        {
            m_state = newState;
            valueChanged = true;
        }
        if (valueChanged && !isCreating)
        {
            m_owner.registerModifiedData();
        }
    }

    /**
     * Set index without forced renumbering
     *
     * @param anIndexNo The index number
     */
    public void renumber(int anIndexNo)
    {
        m_indexNo = anIndexNo;
    }

    public void setIndexNo(int anIndexNo)
    {
        m_owner.rearrangeAttribute(this, anIndexNo);
    }

    public int getIndexNo()
    {
        return m_indexNo;
    }

    public Vector<T> getConflictingValues()
    {
        if (m_state == IMsoModelIf.ModificationState.STATE_CONFLICTING)
        {
            Vector<T> retVal = new Vector<T>(2);
            retVal.add(m_serverValue);
            retVal.add(m_localValue);
            return retVal;
        }
        return null;
    }


    public boolean rollback()
    {
        m_localValue = null;
        boolean isChanged = m_state == IMsoModelIf.ModificationState.STATE_LOCAL || m_state == IMsoModelIf.ModificationState.STATE_CONFLICTING;
        m_state = IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL;
        return isChanged;
    }

    public boolean commitLocal()
    {
        boolean isChanged = m_state == IMsoModelIf.ModificationState.STATE_LOCAL || m_state == IMsoModelIf.ModificationState.STATE_CONFLICTING;
        if (isChanged)
        {
            m_serverValue = m_localValue;
            m_localValue = null;
            m_state = IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL;
        }
        return isChanged;
    }


    private boolean acceptConflicting(IMsoModelIf.ModificationState aState)
    {
        if (m_state == IMsoModelIf.ModificationState.STATE_CONFLICTING)
        {
            m_state = aState;
            m_owner.registerModifiedData();
            return true;
        }
        return false;
    }

    public boolean acceptLocal()
    {
        MsoModelImpl.getInstance().setLocalUpdateMode();
        boolean retVal = acceptConflicting(IMsoModelIf.ModificationState.STATE_LOCAL);
        MsoModelImpl.getInstance().restoreUpdateMode();
        return retVal;
    }

    public boolean acceptServer()
    {
        MsoModelImpl.getInstance().setRemoteUpdateMode();
        boolean retVal = acceptConflicting(IMsoModelIf.ModificationState.STATE_SERVER_ORIGINAL);
        MsoModelImpl.getInstance().restoreUpdateMode();
        return retVal;
    }

    public boolean isUncommitted()
    {
        return m_state == IMsoModelIf.ModificationState.STATE_LOCAL;
    }

    public boolean isGisAttribute()
    {
        return false;
    }

    public void setRequired(boolean aValue)
    {
        m_required = aValue;
    }

    public boolean isRequired()
    {
        return m_required;
    }

    public static class MsoBoolean extends AttributeImpl<Boolean> implements IMsoBooleanIf
    {
        public MsoBoolean(AbstractMsoObject theOwner, String theName)
        {
            super(Boolean.class, theOwner, theName, Integer.MAX_VALUE, false);
        }

        public MsoBoolean(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(Boolean.class, theOwner, theName, theIndexNo, false);
        }

        public MsoBoolean(AbstractMsoObject theOwner, String theName, int theIndexNo, Boolean aBool)
        {
            super(Boolean.class, theOwner, theName, theIndexNo, aBool);
        }

        public void setValue(boolean aValue)
        {
            setAttrValue(aValue);
        }

        public void setValue(Boolean aValue)
        {
            setAttrValue(aValue);
        }

        public boolean booleanValue()
        {
            return getAttrValue();
        }
    }

    public static class MsoInteger extends AttributeImpl<Integer> implements IMsoIntegerIf
    {
        public MsoInteger(AbstractMsoObject theOwner, String theName)
        {
            super(Integer.class, theOwner, theName, Integer.MAX_VALUE, 0);
        }

        public MsoInteger(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(Integer.class, theOwner, theName, theIndexNo, 0);
        }

        public MsoInteger(AbstractMsoObject theOwner, String theName, int theIndexNo, Integer anInt)
        {
            super(Integer.class, theOwner, theName, theIndexNo, anInt);
        }

        public void setValue(int aValue)
        {
            setAttrValue(aValue);
        }

        public void setValue(Integer aValue)
        {
            setAttrValue(aValue);
        }

        public int intValue()
        {
            return getAttrValue();
        }

        public long longValue()
        {
            return getAttrValue();
        }
    }

    public static class MsoLong extends AttributeImpl<Long> implements IMsoLongIf
    {
        public MsoLong(AbstractMsoObject theOwner, String theName)
        {
            super(Long.class, theOwner, theName, Integer.MAX_VALUE, (long) 0);
        }

        public MsoLong(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(Long.class, theOwner, theName, theIndexNo, (long) 0);
        }

        public MsoLong(AbstractMsoObject theOwner, String theName, int theIndexNo, Long aLong)
        {
            super(Long.class, theOwner, theName, theIndexNo, aLong);
        }

        public void setValue(long aValue)
        {
            setAttrValue(aValue);
        }

        public void setValue(Integer aValue)
        {
            setAttrValue(aValue.longValue());
        }

        public void setValue(Long aValue)
        {
            setAttrValue(aValue);
        }

        public int intValue()
        {
            return getAttrValue().intValue();
        }

        public long longValue()
        {
            return getAttrValue();
        }
    }

    public static class MsoDouble extends AttributeImpl<Double> implements IMsoDoubleIf
    {
        public MsoDouble(AbstractMsoObject theOwner, String theName)
        {
            super(Long.class, theOwner, theName, Integer.MAX_VALUE, (double) 0);
        }

        public MsoDouble(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(Double.class, theOwner, theName, theIndexNo, (double) 0);
        }

        public MsoDouble(AbstractMsoObject theOwner, String theName, int theIndexNo, Double aDouble)
        {
            super(Double.class, theOwner, theName, theIndexNo, aDouble);
        }

        public void setValue(long aValue)
        {
            setAttrValue((double) aValue);
        }

        public void setValue(Integer aValue)
        {
            setAttrValue(aValue.doubleValue());
        }

        public void setValue(Long aValue)
        {
            setAttrValue(aValue.doubleValue());
        }

        public void setValue(Float aValue)

        {
            setAttrValue(aValue.doubleValue());
        }

        public void setValue(Double aValue)
        {
            setAttrValue(aValue);
        }

        public int intValue()
        {
            return getAttrValue().intValue();
        }

        public long longValue()
        {
            return getAttrValue().longValue();
        }

        public double doubleValue()
        {
            return getAttrValue();
        }

        public double floatValue()
        {
            return getAttrValue();
        }
    }

    public static class MsoString extends AttributeImpl<String> implements IMsoStringIf
    {
        public MsoString(AbstractMsoObject theOwner, String theName)
        {
            super(String.class, theOwner, theName, Integer.MAX_VALUE, "");
        }

        public MsoString(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(String.class, theOwner, theName, theIndexNo, "");
        }

        public MsoString(AbstractMsoObject theOwner, String theName, int theIndexNo, String aString)
        {
            super(String.class, theOwner, theName, theIndexNo, aString);
        }

        public void setValue(String aValue)
        {
            setAttrValue(aValue);
        }

        public String getString()
        {
            return getAttrValue();
        }
    }

    public static class MsoCalendar extends AttributeImpl<Calendar> implements IMsoCalendarIf
    {
        public MsoCalendar(AbstractMsoObject theOwner, String theName)
        {
            super(Calendar.class, theOwner, theName, Integer.MAX_VALUE, Calendar.getInstance());
        }

        public MsoCalendar(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(Calendar.class, theOwner, theName, theIndexNo, Calendar.getInstance());
        }

        public MsoCalendar(AbstractMsoObject theOwner, String theName, int theIndexNo, Calendar aCalendar)
        {
            super(Calendar.class, theOwner, theName, theIndexNo, aCalendar);
        }

        public void setDTG(String aDTG) throws IllegalMsoArgumentException
        {
            setValue(aDTG);
        }

        public void setValue(String aDTG) throws IllegalMsoArgumentException
        {
            Calendar cal = DTG.DTGToCal(aDTG);
            setAttrValue(cal);
        }

        public void setValue(Long aDTG) throws IllegalMsoArgumentException
        {
            Calendar cal = DTG.DTGToCal(aDTG);
            setAttrValue(cal);
        }

        public void setValue(Calendar aDTG)
        {
            setAttrValue(aDTG);
        }

        public Calendar getCalendar()
        {
            return getAttrValue();
        }

        public String getDTG()
        {
            return DTG.CalToDTG(getAttrValue());
        }
    }

    public static class MsoPosition extends AttributeImpl<Position> implements IMsoPositionIf
    {
        public MsoPosition(AbstractMsoObject theOwner, String theName)
        {
            super(Position.class, theOwner, theName, Integer.MAX_VALUE, null);
        }

        public MsoPosition(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(Position.class, theOwner, theName, theIndexNo, null);
        }

        public MsoPosition(AbstractMsoObject theOwner, String theName, int theIndexNo, Position aPosition)
        {
            super(Position.class, theOwner, theName, theIndexNo, aPosition);
        }

        public void setValue(Position aPosition)
        {
            super.setAttrValue(aPosition);
        }

        public void setValue(String anId, Point2D.Double aPoint)
        {
            Position pos = new Position(anId, aPoint);
            setAttrValue(pos);
        }

        public Position getPosition()
        {
            return getAttrValue();
        }
    }

    public static class MsoTimePos extends AttributeImpl<TimePos> implements IMsoTimePosIf
    {
        public MsoTimePos(AbstractMsoObject theOwner, String theName)
        {
            super(TimePos.class, theOwner, theName, Integer.MAX_VALUE, null);
        }

        public MsoTimePos(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(TimePos.class, theOwner, theName, theIndexNo, null);
        }

        public MsoTimePos(AbstractMsoObject theOwner, String theName, int theIndexNo, TimePos aTimePos)
        {
            super(TimePos.class, theOwner, theName, theIndexNo, aTimePos);
        }

        public void setValue(TimePos aTimePos)
        {
            super.setAttrValue(aTimePos);
        }

        public TimePos getTimePos()
        {
            return getAttrValue();
        }

        public boolean isGisAttribute()
        {
            return true;
        }
    }

    public static class MsoPolygon extends AttributeImpl<Polygon> implements IMsoPolygonIf
    {
        public MsoPolygon(AbstractMsoObject theOwner, String theName)
        {
            super(Polygon.class, theOwner, theName, Integer.MAX_VALUE, null);
        }

        public MsoPolygon(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(Polygon.class, theOwner, theName, theIndexNo, null);
        }

        public MsoPolygon(AbstractMsoObject theOwner, String theName, int theIndexNo, Polygon aPolygon)
        {
            super(Polygon.class, theOwner, theName, theIndexNo, aPolygon);
        }

        public void setValue(Polygon aPolygon)
        {
            super.setAttrValue(aPolygon);
        }

        public Polygon getPolygon()
        {
            return getAttrValue();
        }

        public boolean isGisAttribute()
        {
            return true;
        }
    }

    public static class MsoRoute extends AttributeImpl<Route> implements IMsoRouteIf
    {
        public MsoRoute(AbstractMsoObject theOwner, String theName)
        {
            super(Route.class, theOwner, theName, Integer.MAX_VALUE, null);
        }

        public MsoRoute(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(Route.class, theOwner, theName, theIndexNo, null);
        }

        public MsoRoute(AbstractMsoObject theOwner, String theName, int theIndexNo, Route aRoute)
        {
            super(Route.class, theOwner, theName, theIndexNo, aRoute);
        }

        public void setValue(Route aRoute)
        {
            super.setAttrValue(aRoute);
        }

        public Route getRoute()
        {
            return getAttrValue();
        }

        public boolean isGisAttribute()
        {
            return true;
        }
    }

    public static class MsoTrack extends AttributeImpl<Track> implements IMsoTrackIf
    {
        public MsoTrack(AbstractMsoObject theOwner, String theName)
        {
            super(Track.class, theOwner, theName, Integer.MAX_VALUE, null);
        }

        public MsoTrack(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(Track.class, theOwner, theName, theIndexNo, null);
        }

        public MsoTrack(AbstractMsoObject theOwner, String theName, int theIndexNo, Track aTrack)
        {
            super(Track.class, theOwner, theName, theIndexNo, aTrack);
        }

        public void setValue(Track aTrack)
        {
            super.setAttrValue(aTrack);
        }

        public Track getTrack()
        {
            return getAttrValue();
        }

        public boolean isGisAttribute()
        {
            return true;
        }
    }

    public static class MsoGeoCollection extends AttributeImpl<GeoCollection> implements IMsoGeoCollectionIf
    {
        public MsoGeoCollection(AbstractMsoObject theOwner, String theName)
        {
            super(GeoCollection.class, theOwner, theName, Integer.MAX_VALUE, null);
        }

        public MsoGeoCollection(AbstractMsoObject theOwner, String theName, int theIndexNo)
        {
            super(GeoCollection.class, theOwner, theName, theIndexNo, null);
        }

        public MsoGeoCollection(AbstractMsoObject theOwner, String theName, int theIndexNo, GeoCollection aGeoCollection)
        {
            super(GeoCollection.class, theOwner, theName, theIndexNo, aGeoCollection);
        }

        public void setValue(GeoCollection aGeoCollection)
        {
            super.setAttrValue(aGeoCollection);
        }

        public GeoCollection getGeoCollection()
        {
            return getAttrValue();
        }

        public boolean isGisAttribute()
        {
            return true;
        }
    }

    public static class MsoEnum<E extends Enum> extends AttributeImpl<E> implements IMsoEnumIf<E>
    {
        public MsoEnum(AbstractMsoObject theOwner, String theName, E anInstance)
        {
            super(anInstance.getClass(), theOwner, theName, Integer.MAX_VALUE, anInstance);
        }

        public void setValue(E anEnum)
        {
            super.setAttrValue(anEnum);
        }

        public void setValue(String aName)
        {
            E anEnum = enumValue(aName);
            if (anEnum == null)
            {
                throw new MsoError("Cannot set enum value " + aName + " to " + this);
            }
            super.setAttrValue(enumValue(aName));
        }

        public E getValue()
        {
            return getAttrValue();
        }

        public String getValueName()
        {
            return getAttrValue().name();
        }

        public E enumValue(String aName)
        {
            E retVal;
            try
            {
                retVal = (E) Enum.valueOf(m_class, aName);
            }
            catch (NullPointerException e)
            {
                return null;
            }
            return retVal;
        }
    }
}
