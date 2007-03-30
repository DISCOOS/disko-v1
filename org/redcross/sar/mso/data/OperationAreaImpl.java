package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;

public class OperationAreaImpl extends AbstractMsoObject implements IOperationAreaIf
{
    private AttributeImpl.MsoPolygon m_area = new AttributeImpl.MsoPolygon(this, "area");

    public OperationAreaImpl(IMsoObjectIf.IObjectIdIf anObjectId)
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

    public static OperationAreaImpl implementationOf(IOperationAreaIf anInterface) throws MsoCastException
    {
        try
        {
            return (OperationAreaImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to OperationAreaImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_OPERATIONAREA;
    }


}