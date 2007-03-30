package org.redcross.sar.mso.data;

public class TeamImpl extends AbstractUnit implements ITeamIf
{
    private final AttributeImpl.MsoInteger m_speed = new AttributeImpl.MsoInteger(this, "speed");

    public TeamImpl(IMsoObjectIf.IObjectIdIf anObjectId, Long aNumber)
    {
        super(anObjectId, aNumber);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_speed);
    }

    protected void defineLists()
    {
    }

    protected void defineReferences()
    {
    }


    public String toString()
    {
        return super.toString();
    }
}