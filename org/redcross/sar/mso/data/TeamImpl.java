package org.redcross.sar.mso.data;

public class TeamImpl extends AbstractUnit implements ITeamIf
{
    private final AttributeImpl.MsoInteger m_speed = new AttributeImpl.MsoInteger(this, "speed");

    public TeamImpl(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber)
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

    protected UnitType getTypeBySubclass()
    {
        return IUnitIf.UnitType.TEAM;
    }

    public String getSubTypeName()
    {
        return "CAR"; // todo expand
    }
    
    public String toString()
    {
        return super.toString();
    }
}