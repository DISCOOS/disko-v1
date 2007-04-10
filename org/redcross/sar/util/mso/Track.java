package org.redcross.sar.util.mso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;

/**
 * Class for holding track information
 */
public class Track implements IGeodataIf
{
    private final String m_id;
    private String m_layout;

    private final String m_name;
    private final ArrayList<TimePos> m_track;

    /**
     * Constructor, default collection size
     *
     * @param anId Object Id
     */
    public Track(String anId)
    {
        this(anId, "");
    }

    /**
     * Constructor, default collection size
     *
     * @param anId  Object Id
     * @param aName Name of track
     */
    public Track(String anId, String aName)
    {
        m_id = anId;
        m_name = aName;
        m_track = new ArrayList<TimePos>();
    }

    /**
     * Constructor, parameter for collection size
     *
     * @param anId  Object Id
     * @param aName Name of track
     * @param aSize The collection size
     */
    public Track(String anId, String aName, int aSize)
    {
        m_id = anId;
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
        add(new TimePos(aLongPosition, aLatPosition, aCalendar));
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

    public String getId()
    {
        return m_id;
    }

    public void setLayout(String aLayout)
    {
        m_layout = aLayout;
    }

    public String getLayout()
    {
        return m_layout;
    }
}
