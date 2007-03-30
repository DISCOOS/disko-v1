package org.redcross.sar.mso.data;

public abstract class AbstractTransportUnit extends AbstractUnit implements ITransportIf
{
    protected AttributeImpl.MsoString m_kjennetegn = new AttributeImpl.MsoString(this, "kjennetegn");
    protected AttributeImpl.MsoInteger m_speed = new AttributeImpl.MsoInteger(this, "speed");

    public AbstractTransportUnit(IMsoObjectIf.IObjectIdIf anObjectId, Long aNumber, String aKjennetegn, int aHastighet)
    {
        super(anObjectId, aNumber);
        m_kjennetegn.setValue(aKjennetegn);
        m_speed.setValue(aHastighet);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_kjennetegn);
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
        return super.toString() + " " + m_kjennetegn.getString() + " " + m_speed.intValue();
    }
}