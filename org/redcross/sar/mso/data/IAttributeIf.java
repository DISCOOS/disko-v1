package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.IllegalMsoArgumentException;
import org.redcross.sar.util.mso.*;

import java.awt.geom.Point2D;
import java.util.Calendar;
import java.util.Vector;

/**
 * Base nterface for MSO Attributes
 */
public interface IAttributeIf<T>
{
    /**
     * Get name of attribute
     *
     * @return The name
     */
    public String getName();

    /**
     * Set index number for the attribute
     * Will force renumbering of attributes.
     *
     * @param anIndexNo The index
     */
    public void setIndexNo(int anIndexNo);

    /**
     * Get index number for the attribute
     *
     * @return The index
     */
    public int getIndexNo();

    /**
     * Get the {@link org.redcross.sar.mso.IMsoModelIf.ModificationState ModificationState} of the attribute
     *
     * @return The state
     */
    public IMsoModelIf.ModificationState getState();

    /**
     * Get conflicting values
     *
     * @return Vector containing the two conflicting values.
     */
    public Vector<T> getConflictingValues();

    /**
     * Perform rollback on the attribute
     *
     * @return True if something has been done.
     */
    public boolean rollback();

    /**
     * Accept local (client) value in case of conflict.
     *
     * @return True if something has been done.
     */
    public boolean acceptLocal();

    /**
     * Accept server value in case of conflict.
     *
     * @return True if something has been done.
     */
    public boolean acceptServer();

    /**
     * Tell if attrubute has been changed and not committed.
     *
     * @return True if not committed
     */
    public boolean isUncommitted();

    /**
     * Tell if this attribute concerns GIS objects
     *
     * @return <code>true</code>  if a GIS attribute, <code>false</code> oterhwise.
     */
    public boolean isGisAttribute();

    /**
     * Tell if this attribute is reqired objects
     *
     * @return <code>true</code>  if required (cannot be null) in order to commit the object, <code>false</code> oterhwise.
     */
    public boolean isRequired();

    /**
     * Interface for {@link Boolean} attributes.
     */
    public interface IMsoBooleanIf extends IAttributeIf<Boolean>
    {
        public void setValue(boolean aValue);

        public void setValue(Boolean aValue);

        public boolean booleanValue();
    }

    /**
     * Interface for {@link Integer} attributes.
     */
    public interface IMsoIntegerIf extends IAttributeIf<Integer>
    {
        public void setValue(int aValue);

        public void setValue(Integer aValue);

        public int intValue();

        public long longValue();
    }

    /**
     * Interface for {@link Long} attributes.
     */
    public interface IMsoLongIf extends IAttributeIf<Long>
    {
        public void setValue(long aValue);

        public void setValue(Integer aValue);

        public void setValue(Long aValue);

        public int intValue();

        public long longValue();
    }

    /**
     * Interface for {@link Double} attributes.
     */
    public interface IMsoDoubleIf extends IAttributeIf<Double>
    {
        public void setValue(long aValue);

        public void setValue(Integer aValue);

        public void setValue(Long aValue);

        public void setValue(Float aValue);

        public void setValue(Double aValue);

        public int intValue();

        public long longValue();

        public double doubleValue();
    }

    /**
     * Interface for {@link String} attributes.
     */
    public interface IMsoStringIf extends IAttributeIf<String>
    {
        public void setValue(String aValue);

        public String getString();
    }

    /**
     * Interface for {@link java.util.Calendar} attributes.
     */
    public interface IMsoCalendarIf extends IAttributeIf<Calendar>
    {
        public void setDTG(String aDTG) throws IllegalMsoArgumentException;

        public void setValue(String aDTG) throws IllegalMsoArgumentException;

        public void setValue(Long aDTG) throws IllegalMsoArgumentException;

        public void setValue(Calendar aDTG);

        public String getDTG();

        public Calendar getCalendar();
    }

    /**
     * Interface for {@link org.redcross.sar.util.mso.Position} attributes.
     */
    public interface IMsoPositionIf extends IAttributeIf<Position>
    {
        public void setValue(Position aPosition);

        public void setValue(Point2D.Double aPoint);

        public Position getPosition();
    }

    /**
     * Interface for {@link org.redcross.sar.util.mso.TimePos} attributes.
     */
    public interface IMsoTimePosIf extends IAttributeIf<TimePos>
    {
        public void setValue(TimePos aTimePos);

        public TimePos getTimePos();
    }

    /**
     * Interface for {@link org.redcross.sar.util.mso.Polygon} attributes.
     */
    public interface IMsoPolygonIf extends IAttributeIf<Polygon>
    {
        public void setValue(Polygon aPolygon);

        public Polygon getPolygon();
    }

    /**
     * Interface for {@link org.redcross.sar.util.mso.Route} attributes.
     */
    public interface IMsoRouteIf extends IAttributeIf<Route>
    {
        public void setValue(Route aRoute);

        public Route getRoute();
    }

    /**
     * Interface for {@link org.redcross.sar.util.mso.Track} attributes.
     */
    public interface IMsoTrackIf extends IAttributeIf<Track>
    {
        public void setValue(Track aTrack);

        public Track getTrack();
    }

    /**
     * Interface for {@link Enum} attributes.
     */
    public interface IMsoEnumIf<E extends Enum> extends IAttributeIf<E>
    {
        public void setValue(E anEnum);

        public void setValue(String aName);

        public E getValue();

        public String getValueName();

        public E enumValue(String aName);
    }

}


