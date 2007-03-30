package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.MsoCastException;

import java.util.Collection;

public class AreaImpl extends AbstractMsoObject implements IAreaIf
{
    //    todo private final AttributeImpl.MsoGeodata m_geodata = new AttributeImpl.MsoGeodata(this, "Geodata");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");
    private final POIListImpl m_areaPOIs = new POIListImpl(this, "AreaPOIs", false);

    public AreaImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
// todo       addAttribute(m_geodata);
        addAttribute(m_remarks);
    }

    protected void defineLists()
    {
        addList(m_areaPOIs);
    }

    protected void defineReferences()
    {
    }

    // todo    public void setGeodata(geodata aGeodata)
//    public void setGeodata(geodata aGeodata)
//    {
//        m_geodata.setValue(aGeodata);
//    }
//
//    public geodata getGeodata()
//    {
//        return m_geodata.getGeodata();
//    }
//
//    public IMsoModelIf.ModificationState getGeodataState()
//    {
//        return m_geodata.getState();
//    }
//
//    public IAttributeIf.IMsoGeodataIf getGeodataAttribute()
//    {
//        return m_geodata;
//    }

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

    public IAssignmentIf getAssignment()
    {
        return null; /*todo*/
    }

    public IHypothesisIf getHypotesis()
    {
        return null; /*todo*/
    }

    public void addAreaPOIs(IPOIIf anIPOIIf) throws DuplicateIdException
    {
        m_areaPOIs.add(anIPOIIf);
    }

    public IPOIListIf getAreaPOIs()
    {
        return m_areaPOIs;
    }

    public IMsoModelIf.ModificationState getAreaPOIsState(IPOIIf anIPOIIf)
    {
        return m_areaPOIs.getState(anIPOIIf);
    }

    public Collection<IPOIIf> getAreaPOIsItems()
    {
        return m_areaPOIs.getItems();
    }

    public static AreaImpl implementationOf(IAreaIf anInterface) throws MsoCastException
    {
        try
        {
            return (AreaImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to AreaImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_AREA;
    }

}