package org.redcross.sar.util.mso;

import java.util.Collection;
import java.util.Vector;

/**
 * Class for holding polygon information
 */
public class Polygon
{
    private final String m_name;
    private final Vector<Position> m_polygon;

    /**
     * Constructor, default collection size
     */
    public Polygon()
    {
        this("");
    }

    /**
     * Constructor, default collection size
     *
     * @param aName Name of route
     */
    public Polygon(String aName)
    {
        m_name = aName;
        m_polygon = new Vector<Position>();
    }

    /**
     * Constructor, parameter for collection size
     *
     * @param aName Name of route
     * @param aSize The collection size
     */
    public Polygon(String aName, int aSize)
    {
        m_name = aName;
        m_polygon = new Vector<Position>(aSize);
    }

    /**
     * Add a new vertex to the polygon.
     *
     * @param aVertex The vertex to add.
     */
    public void add(Position aVertex)
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
        add(new Position(aLongPosition, aLatPosition));
    }

    /**
     * Get the collection of vertices of the polygon
     *
     * @return The vertice collection.
     */
    public Collection<Position> getVertices()
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

}
