package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.except.MsoException;

public class CalloutImpl extends AbstractMsoObject implements ICalloutIf
{
    private final PersonnelListImpl m_personnel = new PersonnelListImpl(this, "CalloutPersonnel", false);

    public CalloutImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
    }

    protected void defineLists()
    {
        addList(m_personnel);
    }

    protected void defineReferences()
    {
    }

    public static CalloutImpl implementationOf(ICalloutIf anInterface) throws MsoCastException
    {
        try
        {
            return (CalloutImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to Callout");
        }
    }

    public IPersonnelIf createPersonnel(IMsoObjectIf.IObjectIdIf anObjectId, String aPersonellId, IPersonnelListIf aPesonnelList) throws DuplicateIdException
    {
        IPersonnelIf result = aPesonnelList.createPersonnel(anObjectId, aPersonellId);
        if (addPersonel(result))
        {
            return result;
        }
        throw new DuplicateIdException("Duplicated id in Callout: " + anObjectId);
    }

    public boolean addPersonel(IPersonnelIf aPersonnel)
    {
        try
        {
            m_personnel.add(aPersonnel);
            return true;
        }
        catch (MsoException e)
        {
            return false;
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_CALLOUT;
    }



}