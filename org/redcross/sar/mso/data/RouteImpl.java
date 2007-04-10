package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.mso.Route;

public class RouteImpl extends AbstractMsoObject implements IRouteIf
{
    private final AttributeImpl.MsoRoute m_geodata = new AttributeImpl.MsoRoute(this, "Geodata");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");

    public RouteImpl(IMsoObjectIf.IObjectIdIf anObjectId, Route aRoute)
    {
        super(anObjectId);
        setGeodata(aRoute);
    }

    protected void defineAttributes()
    {
        addAttribute(m_geodata);
        addAttribute(m_remarks);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }

    public static RouteImpl implementationOf(IRouteIf anInterface) throws MsoCastException
    {
        try
        {
            return (RouteImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to RouteImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_ROUTE;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/
    public void setGeodata(Route aGeodata)
    {
        m_geodata.setValue(aGeodata);
    }

    public Route getGeodata()
    {
        return m_geodata.getRoute();
    }

    public IMsoModelIf.ModificationState getGeodataState()
    {
        return m_geodata.getState();
    }

    public IAttributeIf.IMsoRouteIf getGeodataAttribute()
    {
        return m_geodata;
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