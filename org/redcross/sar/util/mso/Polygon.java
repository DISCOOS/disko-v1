package org.redcross.sar.util.mso;

import java.util.Collection;
import java.util.Vector;

/**
 * Class for holding polygon information
 */
public class Polygon implements IGeodataIf
{
    private final String m_id;
    private String m_layout;

    private final String m_name;
    private final Vector<GeoPos> m_polygon;

    /**
     * Constructor, default collection size
     *
     * @param anId Object Id
     */
    public Polygon(String anId)
    {
        this(anId, "");
    }

    /**
     * Constructor, default collection size
     *
     * @param anId  Object Id
     * @param aName Name of route
     */
    public Polygon(String anId, String aName)
    {
        m_id = anId;
        m_name = aName;
        m_polygon = new Vector<GeoPos>();
    }

    /**
     * Constructor, parameter for collection size
     *
     * @param anId  Object Id
     * @param aName Name of route
     * @param aSize The collection size
     */
    public Polygon(String anId, String aName, int aSize)
    {
        m_id = anId;
        m_name = aName;
        m_polygon = new Vector<GeoPos>(aSize);
    }

    /**
     * Add a new vertex to the polygon.
     *
     * @param aVertex The vertex to add.
     */
    public void add(GeoPos aVertex)
    {
        m_polygon.add(aVertex);
    }

    /**
     * Add a new vertex to the route.
     *
     * @param aLongPosition The vertex' longitude
     * @param aLatPosition  The vertex' latitude
     */
    public void add(double aLongPosition, double aLatPosition)
    {
        add(new GeoPos(aLongPosition, aLatPosition));
    }

    /**
     * Get the collection of vertices of the polygon
     *
     * @return The vertice collection.
     */
    public Collection<GeoPos> getVertices()
    {
        return m_polygon;
    }

    /**
     * Get name of polygon
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
