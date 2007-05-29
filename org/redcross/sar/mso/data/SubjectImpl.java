package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;

public class SubjectImpl extends AbstractPerson implements ISubjectIf
{
    private final AttributeImpl.MsoString m_description = new AttributeImpl.MsoString(this, "Description");

    public SubjectImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_description);
    }

    protected void defineLists()
    {
        super.defineLists();
    }

    protected void defineReferences()
    {
        super.defineReferences();
    }

    @Override
    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    @Override
    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }


    public static SubjectImpl implementationOf(ISubjectIf anInterface) throws MsoCastException
    {
        try
        {
            return (SubjectImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to SubjectImpl");
        }
    }

    public String toString()
    {
        return super.toString() + " " + m_description.getAttrValue();
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_SUBJECT;
    }


}