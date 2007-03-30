package org.redcross.sar.util.mso;

import java.awt.geom.Point2D;
import java.util.Calendar;

/**
 *  Class for handling a position object with time
 */
public class TimePos extends Position implements Comparable<TimePos>
{
    private final Calendar m_time;

    public TimePos()
    {
        super();
        m_time = null;
    }

    public TimePos(Calendar aCalendar)
    {
        super();
        m_time = aCalendar;
    }

    public TimePos(Point2D.Double aPosition, Calendar aCalendar)
    {
        super(aPosition);
        m_time = aCalendar;
    }

    public TimePos(double aLongPosition, double aLatPosition, Calendar aCalendar)
    {
        super(aLongPosition,aLatPosition);
        m_time = aCalendar;
    }



    public TimePos(Point2D.Double aPosition, String fromCoordSystem, Calendar aCalendar)
    {
        super(aPosition, fromCoordSystem);
        m_time = aCalendar;
    }

    public String getDTG ()
    {
        return DTG.CalToDTG(m_time);
    }

    /**
     * Calculate difference to another TimePos object
     * @param aTimePos The other value.
     * @return  Difference in hours. Is negative if aTimePos is after this value
     */
    public double timeSince(TimePos aTimePos)
    {
        return (m_time.getTimeInMillis()-aTimePos.m_time.getTimeInMillis())/(1000*3600);
    }
    
    /**
     * Calculate average speed in km/h along a line to another TimePos
     * @param aTimePos The other point
     * @return Average speed, set to 0 if time difference is 0.
     */
    public double speed(TimePos aTimePos)
    {
        double time = Math.abs(timeSince(aTimePos));
        double distance = distance(aTimePos);
        if (time > 0)
        {
            return distance/time;
        }
        return 0;
    }

    public int compareTo(TimePos aTimePos)
    {
        return m_time.compareTo(aTimePos.m_time);
    }
}
