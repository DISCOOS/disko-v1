package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.mso.Position;

/**
 * Point of interest
 */
public class POIImpl extends AbstractMsoObject implements IPOIIf
{
    private final AttributeImpl.MsoPosition m_position = new AttributeImpl.MsoPosition(this, "Position");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");
    private final AttributeImpl.MsoEnum<POIType> m_type = new AttributeImpl.MsoEnum<POIType>(this, "Type", POIType.GENERAL);

    public POIImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        this(anObjectId, POIType.GENERAL, null);
    }

    public POIImpl(IMsoObjectIf.IObjectIdIf anObjectId, POIType aType, Position aPosition)
    {
        super(anObjectId);
        m_type.setValue(aType);
        m_position.setValue(aPosition);
    }

    protected void defineAttributes()
    {
        addAttribute(m_position);
        addAttribute(m_remarks);
        addAttribute(m_type);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }


    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
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

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setType(POIType aType)
    {
        m_type.setValue(aType);
    }

    public void setType(String aType)
    {
        m_type.setValue(aType);
    }

    public POIType getType()
    {
        return m_type.getValue();
    }

    public IMsoModelIf.ModificationState getTypeState()
    {
        return m_type.getState();
    }

    public IAttributeIf.IMsoEnumIf<POIType> getTypeAttribute()
    {
        return m_type;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setPosition(Position aPosition)
    {
        m_position.setValue(aPosition);
    }

    public Position getPosition()
    {
        return m_position.getPosition();
    }

    public IMsoModelIf.ModificationState getPositionState()
    {
        return m_position.getState();
    }

    public IAttributeIf.IMsoPositionIf getPositionAttribute()
    {
        return m_position;
    }

    public void setRemarks(String aRemarks)
    {
        m_remarks.setValue(aRemarks);
    }

    public String getRemarks()
    {
        return m_remarks.getString();
    }

    public IMsoModelIf.ModificationState getRemarksState()
    {
        return m_remarks.getState();
    }

    public IAttributeIf.IMsoStringIf getRemarksAttribute()
    {
        return m_remarks;
    }


}