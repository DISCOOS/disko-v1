package org.redcross.sar.util.mso;

import java.awt.geom.Point2D;

/**
 *
 */
public class Position extends GeoPosition
{
    private static String MSO_COORD_SYS = "";  // todo finn verdi

    public Position()
    {
        super(MSO_COORD_SYS);
    }

    /**
     * Create a position at a point
     *
     * @param aPosition The point's coordinates
     */
    public Position(Point2D.Double aPosition)
    {
        super(aPosition, MSO_COORD_SYS);
    }

    /**
     * Create a position at a given long/lat
     *
     * @param aLongPosition The point's longitude
     * @param aLatPosition  The point's latitude
     */
    public Position(double aLongPosition, double aLatPosition)
    {
        super(new Point2D.Double(aLongPosition, aLatPosition), MSO_COORD_SYS);
    }

    public Position(Point2D.Double aPosition, String fromCoordSystem)
    {
        super(aPosition, fromCoordSystem, MSO_COORD_SYS);
    }

}
