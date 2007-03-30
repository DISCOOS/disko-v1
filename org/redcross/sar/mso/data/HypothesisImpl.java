package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;

public class HypothesisImpl extends AbstractMsoObject implements IHypothesisIf
{
    public HypothesisImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }

    public static HypothesisImpl implementationOf(IHypothesisIf anInterface) throws MsoCastException
    {
        try
        {
            return (HypothesisImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to HypothesisImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_HYPOTHESIS;
    }


}