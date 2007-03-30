package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;

public class SearchAreaImpl extends AbstractMsoObject implements ISearchAreaIf
{
    private AttributeImpl.MsoPolygon m_area = new AttributeImpl.MsoPolygon(this, "area");

    public SearchAreaImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
        addAttribute(m_area);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }

    public static SearchAreaImpl implementationOf(ISearchAreaIf anInterface) throws MsoCastException
    {
        try
        {
            return (SearchAreaImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to SearchAreaImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_SEARCHAREA;
    }


}