package org.redcross.sar.util.mso;

import java.util.Collection;
import java.util.Vector;

/**
 * Class for holding a set of {@link IGeodataIf} objects
 */
public class GeoCollection
{
    private final String m_id;

    private final String m_name;
    private final Vector<IGeodataIf> m_geodata;

    /**
     * Constructor, default collection size
     *
     * @param anId Object Id
     */
    public GeoCollection(String anId)
    {
        this(anId, "");
    }

    /**
     * Constructor, default collection size
     *
     * @param anId  Object Id
     * @param aName Name of collection
     */
    public GeoCollection(String anId, String aName)
    {
        m_id = anId;
        m_name = aName;
        m_geodata = new Vector<IGeodataIf>();
    }

    /**
     * Constructor, parameter for collection size
     *
     * @param anId  Object Id
     * @param aName Name of collection
     * @param aSize The collection size
     */
    public GeoCollection(String anId, String aName, int aSize)
    {
        m_id = anId;
        m_name = aName;
        m_geodata = new Vector<IGeodataIf>(aSize);
    }

    /**
     * Add a new object to the collection.
     *
     * @param aGeodata The object to add.
     */
    public void add(IGeodataIf aGeodata)
    {
        m_geodata.add(aGeodata);
    }


    /**
     * Get the collection
     *
     * @return The IGeodataIf collection.
     */
    public Collection<IGeodataIf> getPositions()
    {
        return m_geodata;
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
}
