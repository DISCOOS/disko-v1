package org.redcross.sar.util.mso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

/**
 * Class for holding track information
 */
public class Track
{
    private final String m_name;
    private final ArrayList<TimePos> m_track;

    /**
     * Constructor, default collection size
     */
    public Track()
    {
        this("");
    }

    /**
     * Constructor, default collection size
     *
     * @param aName Name of track
     */
    public Track(String aName)
    {
        m_name = aName;
        m_track = new ArrayList<TimePos>();
    }

    /**
     * Constructor, parameter for collection size
     *
     * @param aName Name of track
     * @param aSize The collection size
     */
    public Track(String aName, int aSize)
    {
        m_name = aName;
        m_track = new ArrayList<TimePos>(aSize);
    }

    /**
     * Add a new point to the track.
     * After adding, the collection is sorted according to time.
     *
     * @param aTimePos The point to add.
     */
    public void add(TimePos aTimePos)
    {
        m_track.add(aTimePos);
        Collections.sort(m_track);
    }

    /**
     * Add a new point to the track.
     * Calls {@link #add(TimePos)} .
     *
     * @param aLongPosition
     * @param aLatPosition
     * @param aCalendar
     */
    public void add(double aLongPosition, double aLatPosition, Calendar aCalendar)
    {
        add(new TimePos(aLongPosition,aLatPosition,aCalendar));
    }

    /**
     * Get the collection of points in the track
     *
     * @return The TimePos collection.
     */
    public Collection<TimePos> getTrackTimePos()
    {
        return m_track;
    }

    /**
     * Get name of track
     *
     * @return The name
     */
    public String getName()
    {
        return m_name;
    }
}
