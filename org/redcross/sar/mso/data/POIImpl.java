package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.Internationalization;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.mso.Position;

import java.util.Collection;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * Point of interest
 */
public class POIImpl extends AbstractMsoObject implements IPOIIf
{
    private final AttributeImpl.MsoPosition m_position = new AttributeImpl.MsoPosition(this, "Position");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");
    private final AttributeImpl.MsoEnum<POIType> m_type = new AttributeImpl.MsoEnum<POIType>(this, "Type", POIType.GENERAL);
    private final AttributeImpl.MsoInteger m_areaSequenceNumber = new AttributeImpl.MsoInteger(this, "AreaSequenceNumber");

    public static ResourceBundle getBundle()
    {
        return Internationalization.getBundle(IPOIIf.class);
    }

    public static String getText(String aKey)
    {
        return Internationalization.getFullBundleText(Internationalization.getBundle(IPOIIf.class),aKey);
    }

    public String getTypeText()
    {
        return m_type.getInternationalName();
    }

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
        addAttribute(m_areaSequenceNumber);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }


    public boolean addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        return true;
    }

    public boolean removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        return true;
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

    public void setAreaSequenceNumber(int aNumber)
    {
        m_areaSequenceNumber.setValue(aNumber);
    }

    public int getAreaSequenceNumber()
    {
        return m_areaSequenceNumber.intValue();
    }

    public IMsoModelIf.ModificationState getAreaSequenceNumberState()
    {
        return m_areaSequenceNumber.getState();
    }

    public IAttributeIf.IMsoIntegerIf getAreaSequenceNumberAttribute()
    {
        return m_areaSequenceNumber;
    }

    private final static SelfSelector<IPOIIf, IMessageLineIf> referringMesssageLineSelector = new SelfSelector<IPOIIf, IMessageLineIf>()
    {
        public boolean select(IMessageLineIf anObject)
        {
            return (m_object.equals(anObject.getLinePOI()));
        }
    };

    public Set<IMessageLineIf> getReferringMessageLines()
    {
        referringMesssageLineSelector.setSelfObject(this);
        return MsoModelImpl.getInstance().getMsoManager().getCmdPost().getMessageLines().selectItems(referringMesssageLineSelector);
    }

    public Set<IMessageLineIf> getReferringMessageLines(Collection<IMessageLineIf> aCollection)
    {
        referringMesssageLineSelector.setSelfObject(this);
        return MsoListImpl.selectItemsInCollection(referringMesssageLineSelector,aCollection);
    }
}