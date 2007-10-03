package org.redcross.sar.util.mso;

import java.awt.geom.Point2D;

/**
 *
 */
public class GeoPos
{
    private Point2D.Double m_position;

    /**
     * Create a position with no point
     */
    public GeoPos()
    {
        setPosition(null);
    }

    /**
     * Create a position at a point
     *
     * @param aPosition The point's coordinates
     */
    public GeoPos(Point2D.Double aPosition)
    {
        setPosition(aPosition);
    }

    /**
     * Create a position at a given long/lat
     *
     * @param aLong The point's longitude
     * @param aLat  The point's latitude
     */
    public GeoPos(double aLong, double aLat)
    {
        setPosition(aLong, aLat);
    }

    /**
     * Set position at a point
     *
     * @param aPosition The point's coordinates
     */
    public void setPosition(Point2D.Double aPosition)
    {
        m_position = (Point2D.Double) aPosition.clone();
    }

    /**
     * Set position at a given long/lat
     *
     * @param aLong The point's longitude
     * @param aLat  The point's latitude
     */
    public void setPosition(double aLong, double aLat)
    {
        m_position = new Point2D.Double(aLong, aLat);
    }

    /**
     * Get position as a point
     */
    public Point2D.Double getPosition()
    {
        return m_position;
    }

    /**
     * Calculate distance to another position.
     *
     * @param aPos The other position.
     * @return The distance (in kilometers)
     */
    public double distance(GeoPos aPos)
    {
        return distance(this, aPos);
    }

    /**
     * Calculate distance between two positions.
     *
     * @param aPos1 The first position.
     * @param aPos2 The other position.
     * @return The distance (in kilometers)
     */
    public static double distance(GeoPos aPos1, GeoPos aPos2)
    {
        return aPos1.getPosition().distance(aPos2.getPosition()); // todo fix
    }

    /**
     * Calculate bearing (in degrees) to another position.
     *
     * @param aPos The other position.
     * @return The bearing (in degrees)
     */
    public int bearing(GeoPos aPos)
    {
        return bearing(this, aPos);
    }

    /**
     * Calculate bearing between two positions.
     *
     * @param aPos1 The first position.
     * @param aPos2 The other position.
     * @return The bearing (in degrees)
     */
    public static int bearing(GeoPos aPos1, GeoPos aPos2)
    {
        return 0;  // todo fix
    }

    private final static double MAX_EQUALITY_DISTANCE = 10.0e-6;

    /**
     * Two positions are vonsidered to be equal if their distance in both directions (lat/long) is less than {@link #MAX_EQUALITY_DISTANCE}
     * @param obj
     * @return
     */
   @Override
   public boolean equals(Object obj)
   {
      if(obj instanceof GeoPos)
      {
         GeoPos in=(GeoPos)obj;
//         return getPosition().getY()==in.getPosition().getY() &&
//               getPosition().getX()==in.getPosition().getX();
         return Math.abs(getPosition().getY() - in.getPosition().getY())  < MAX_EQUALITY_DISTANCE &&
               Math.abs(getPosition().getX() - in.getPosition().getX()) < MAX_EQUALITY_DISTANCE;
      }
      else
      {
         return false;
      }
   }

   public int hashCode()
   {
      int result=37;
      result=51*result+(int)(Double.doubleToLongBits(getPosition().getX())^((Double.doubleToLongBits(getPosition().getX())>>>32)));
      result=51*result+(int)(Double.doubleToLongBits(getPosition().getY())^((Double.doubleToLongBits(getPosition().getY())>>>32)));
      return result;
   }
}
