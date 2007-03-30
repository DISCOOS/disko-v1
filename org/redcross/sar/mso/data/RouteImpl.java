package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;

public class RouteImpl extends AbstractMsoObject implements IRouteIf
{
    private AttributeImpl.MsoRoute m_route = new AttributeImpl.MsoRoute(this,"route");

    public RouteImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
        addAttribute(m_route);
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


}