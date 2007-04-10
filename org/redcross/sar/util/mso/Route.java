package org.redcross.sar.util.mso;

import java.util.Collection;
import java.util.Vector;

/**
 * Class for holding route information
 */
public class Route implements IGeodataIf
{
    private final String m_id;
    private String m_layout;

    private final String m_name;
    private final Vector<GeoPos> m_route;

    /**
     * Constructor, default collection size
     *
     * @param anId Object Id
     */
    public Route(String anId)
    {
        this(anId, "");
    }

    /**
     * Constructor, default collection size
     *
     * @param anId  Object Id
     * @param aName Name of route
     */
    public Route(String anId, String aName)
    {
        m_id = anId;
        m_name = aName;
        m_route = new Vector<GeoPos>();
    }

    /**
     * Constructor, parameter for collection size
     *
     * @param anId  Object Id
     * @param aName Name of route
     * @param aSize The collection size
     */
    public Route(String anId, String aName, int aSize)
    {
        m_id = anId;
        m_name = aName;
        m_route = new Vector<GeoPos>(aSize);
    }

    /**
     * Add a new point to the route.
     *
     * @param aPosition The point to add.
     */
    public void add(GeoPos aPosition)
    {
        m_route.add(aPosition);
    }

    /**
     * Add a new point to the route.
     *
     * @param aLongPosition The point's longitude
     * @param aLatPosition  The point's latitude
     */
    public void add(double aLongPosition, double aLatPosition)
    {
        add(new GeoPos(aLongPosition, aLatPosition));
    }


    /**
     * Get the collection of points in the route
     *
     * @return The GeoPos collection.
     */
    public Collection<GeoPos> getPositions()
    {
        return m_route;
    }

    /**
     * Get name of route
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
