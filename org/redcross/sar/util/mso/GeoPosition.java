package org.redcross.sar.util.mso;

import java.awt.geom.Point2D;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 06.feb.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class GeoPosition
{
    private Point2D.Double m_position;
    private final String m_coordSystem;

    public GeoPosition(String aCoordSystem)
    {
        m_position = null;
        m_coordSystem = aCoordSystem;
    }

    public GeoPosition(Point2D.Double aPoint, String aCoordSystem)
    {
        m_position = (Point2D.Double) aPoint.clone();
        m_coordSystem = aCoordSystem;
    }

    public GeoPosition(Point2D.Double aPosition, String fromCoordSystem, String toCoordSystem)
    {
        m_position = transformPoint(aPosition, fromCoordSystem, toCoordSystem);
        m_coordSystem = toCoordSystem;
    }

    public void setPosition(double aLat, double aLong, String fromCoordSystem)
    {
        m_position = transformPoint(new Point2D.Double(aLong, aLat), fromCoordSystem, m_coordSystem);
    }

    public void setPosition(Point2D.Double aPosition, String fromCoordSystem)
    {
        m_position = transformPoint(aPosition, fromCoordSystem, m_coordSystem);
    }

    public void setPosition(Point2D.Double aPosition)
    {
        m_position = (Point2D.Double) aPosition.clone();
    }

    Point2D.Double getPosition()
    {
        return m_position;
    }

    Point2D.Double getPosition(String toCoordSystem)
    {
        return transformPoint(m_position, m_coordSystem, toCoordSystem);
    }

    String getCoordSys()
    {
        return m_coordSystem;
    }

    public static Point2D.Double transformPoint(Point2D.Double aPosition, String fromCoordSys, String toCoordSys)
    {
        if (fromCoordSys.equals(toCoordSys))
        {
            return (Point2D.Double) aPosition.clone();
        }
        return (Point2D.Double) aPosition.clone(); // todo fix
    }

    public static Point2D.Double transformPoint(GeoPosition aGeoPos, String toCoordSys)
    {
        return transformPoint(aGeoPos.getPosition(), aGeoPos.getCoordSys(), toCoordSys); // todo fix
    }

    public double distance(GeoPosition aGeoPos)
    {
        return distance(this, aGeoPos);
    }

    public static double distance(GeoPosition aGeoPos1, GeoPosition aGeoPos2)
    {
        return aGeoPos1.getPosition().distance(transformPoint(aGeoPos2, aGeoPos1.getCoordSys())); // todo fix
    }

    /**
     * <p>Calculate bearing (in degrees) to another position</p>
     *
     * @param aGeoPos The other position.
     * @return  The bearing (in degrees)
     */
    public float bearing(GeoPosition aGeoPos)
    {
        return 0;
    }
}
