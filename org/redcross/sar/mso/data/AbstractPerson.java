package org.redcross.sar.mso.data;

import java.util.Calendar;

public abstract class AbstractPerson extends AbstractMsoObject implements IPersonIf
{
    private AttributeImpl.MsoString m_name = new AttributeImpl.MsoString(this,"Name",1);
    private AttributeImpl.MsoCalendar m_dateOfBirth;

    public AbstractPerson(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
        addAttribute(m_name);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }

    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
    }

    public int age()
    {
        Calendar now = Calendar.getInstance();
        if (m_dateOfBirth != null && m_dateOfBirth.getAttrValue() != null)
        {
            long ageInMillis = now.getTimeInMillis() - m_dateOfBirth.getAttrValue().getTimeInMillis();
            int ageInYears = (int) (ageInMillis / (1000 * 3600 * 24 * 365.25));
            return ageInYears;
        }
        return -1;
    }

    public String toString()
    {
        return m_name.getAttrValue();
    }
}