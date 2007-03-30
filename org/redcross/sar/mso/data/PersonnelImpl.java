package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;

public class PersonnelImpl extends AbstractPerson implements IPersonnelIf
{
    public PersonnelImpl(IMsoObjectIf.IObjectIdIf anObjectId,String aName)
    {
        super(anObjectId,aName);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
    }

    protected void defineLists()
    {
        super.defineLists();
    }

    protected void defineReferences()
    {
        super.defineReferences();
    }

    public static PersonnelImpl implementationOf(IPersonnelIf anInterface) throws MsoCastException
    {
        try
        {
            return (PersonnelImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to PersonnelImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_PERSONNEL;
    }

}