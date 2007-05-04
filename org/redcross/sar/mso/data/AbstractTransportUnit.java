package org.redcross.sar.mso.data;

public abstract class AbstractTransportUnit extends AbstractUnit implements ITransportIf
{
    protected AttributeImpl.MsoString m_identifier = new AttributeImpl.MsoString(this, "identifier");
    protected AttributeImpl.MsoInteger m_speed = new AttributeImpl.MsoInteger(this, "speed");


    public AbstractTransportUnit(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber)
    {
        super(anObjectId, aNumber);
    }

    public AbstractTransportUnit(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber, String anIdentifier)
    {
        super(anObjectId, aNumber);
        m_identifier.setValue(anIdentifier);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_identifier);
        addAttribute(m_speed);
    }

    protected void defineLists()
    {
        super.defineLists();
    }

    protected void defineReferences()
    {
        super.defineReferences();
    }

    public String toString()
    {
        return super.toString() + " " + m_identifier.getString() + " " + m_speed.intValue();
    }
}