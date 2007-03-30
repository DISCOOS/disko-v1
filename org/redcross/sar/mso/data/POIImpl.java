package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.mso.Position;

public class POIImpl extends AbstractMsoObject implements IPOIIf
{
    private final AttributeImpl.MsoString m_poiType = new AttributeImpl.MsoString(this, "PoiType", 1);
    private final AttributeImpl.MsoPosition m_position = new AttributeImpl.MsoPosition(this, "Position", 2);

    public POIImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        this(anObjectId, "",null);
    }

    public POIImpl(IMsoObjectIf.IObjectIdIf anObjectId, String aType, Position aPosition)
    {
        super(anObjectId);
        m_poiType.setValue(aType);
        m_position.setValue(aPosition);
    }

    protected void defineAttributes()
    {
    }

    protected void defineLists()
    {
        addAttribute(m_poiType);
        addAttribute(m_position);
    }

    protected void defineReferences()
    {
    }

    public void setPosition(Position aPosition)
    {
        m_position.setValue(aPosition);
    }

    public Position getPosition()
    {
        return m_position.getPosition();
    }

    public void setType(String aType)
    {
        m_poiType.setValue(aType);
    }

    public String getType()
    {
        return m_poiType.getString();
    }

    public static POIImpl implementationOf(IPOIIf anInterface) throws MsoCastException
    {
        try
        {
            return (POIImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to POIImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_POI;
    }


}