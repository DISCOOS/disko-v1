package org.redcross.sar.util.mso;

import java.awt.geom.Point2D;

/**
 *
 */
public class Position extends GeoPos implements IGeodataIf
{
    private final String m_id;
    private String m_layout;

    /**
     * Constructor, no point defined.
     *
     * @param anId Object Id
     */

    public Position(String anId)
    {
        super();
        m_id = anId;
    }

    /**
     * Create a position at a point.
     *
     * @param anId      Object Id
     * @param aPosition The point's coordinates
     */
    public Position(String anId, Point2D.Double aPosition)
    {
        super(aPosition);
        m_id = anId;
    }

    /**
     * Create a position at a given long/lat
     *
     * @param anId  Object Id
     * @param aLong The point's longitude
     * @param aLat  The point's latitude
     */
    public Position(String anId, double aLong, double aLat)
    {
        super(aLong, aLat);
        m_id = anId;
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
