package org.redcross.sar.util.mso;

import java.util.Collection;
import java.util.Vector;

/**
 * Class for holding route information
 */
public class Route
{
    private final String m_name;
    private final Vector<Position> m_route;

    /**
     * Constructor, default collection size
     */
    public Route()
    {
        this("");
    }

    /**
     * Constructor, default collection size
     *
     * @param aName Name of route
     */
    public Route(String aName)
    {
        m_name = aName;
        m_route = new Vector<Position>();
    }

    /**
     * Constructor, parameter for collection size
     *
     * @param aName Name of route
     * @param aSize The collection size
     */
    public Route(String aName, int aSize)
    {
        m_name = aName;
        m_route = new Vector<Position>(aSize);
    }

    /**
     * Add a new point to the route.
     *
     * @param aPosition The point to add.
     */
    public void add(Position aPosition)
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
        add(new Position(aLongPosition, aLatPosition));
    }


    /**
     * Get the collection of points in the route
     *
     * @return The Position collection.
     */
    public Collection<Position> getPositions()
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
}
