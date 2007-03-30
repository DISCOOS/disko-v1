package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

public class BoatImpl extends AbstractTransportUnit implements IBoatIf
{
    private final AttributeImpl.MsoInteger m_averageSpeed = new AttributeImpl.MsoInteger(this, "AverageSpeed");
    private final AttributeImpl.MsoInteger m_capacity = new AttributeImpl.MsoInteger(this, "Capacity");
    private final AttributeImpl.MsoInteger m_depth = new AttributeImpl.MsoInteger(this, "Depth");
    private final AttributeImpl.MsoInteger m_freeboard = new AttributeImpl.MsoInteger(this, "Freeboard");
    private final AttributeImpl.MsoInteger m_height = new AttributeImpl.MsoInteger(this, "Height");
    private final AttributeImpl.MsoInteger m_length = new AttributeImpl.MsoInteger(this, "Length");
    private final AttributeImpl.MsoInteger m_maxSpeed = new AttributeImpl.MsoInteger(this, "MaxSpeed");
    private final AttributeImpl.MsoInteger m_type = new AttributeImpl.MsoInteger(this, "Type");


    public BoatImpl(IMsoObjectIf.IObjectIdIf anObjectId, long aNumber, String aKjennetegn, int aHastighet)
    {
        super(anObjectId, aNumber, aKjennetegn, aHastighet);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_averageSpeed);
        addAttribute(m_capacity);
        addAttribute(m_depth);
        addAttribute(m_freeboard);
        addAttribute(m_height);
        addAttribute(m_length);
        addAttribute(m_maxSpeed);
        addAttribute(m_type);
    }

    protected void defineLists()
    {
        super.defineLists();
    }

    protected void defineReferences()
    {
        super.defineReferences();
    }

    public void setAverageSpeed(int anAverageSpeed)
    {
        m_averageSpeed.setValue(anAverageSpeed);
    }

    public int getAverageSpeed()
    {
        return m_averageSpeed.intValue();
    }

    public IMsoModelIf.ModificationState getAverageSpeedState()
    {
        return m_averageSpeed.getState();
    }

    public IAttributeIf.IMsoIntegerIf getAverageSpeedAttribute()
    {
        return m_averageSpeed;
    }

    public void setCapacity(int aCapacity)
    {
        m_capacity.setValue(aCapacity);
    }

    public int getCapacity()
    {
        return m_capacity.intValue();
    }

    public IMsoModelIf.ModificationState getCapacityState()
    {
        return m_capacity.getState();
    }

    public IAttributeIf.IMsoIntegerIf getCapacityAttribute()
    {
        return m_capacity;
    }

    public void setDepth(int aDepth)
    {
        m_depth.setValue(aDepth);
    }

    public int getDepth()
    {
        return m_depth.intValue();
    }

    public IMsoModelIf.ModificationState getDepthState()
    {
        return m_depth.getState();
    }

    public IAttributeIf.IMsoIntegerIf getDepthAttribute()
    {
        return m_depth;
    }

    public void setFreeboard(int aFreeboard)
    {
        m_freeboard.setValue(aFreeboard);
    }

    public int getFreeboard()
    {
        return m_freeboard.intValue();
    }

    public IMsoModelIf.ModificationState getFreeboardState()
    {
        return m_freeboard.getState();
    }

    public IAttributeIf.IMsoIntegerIf getFreeboardAttribute()
    {
        return m_freeboard;
    }

    public void setHeight(int aHeight)
    {
        m_height.setValue(aHeight);
    }

    public int getHeight()
    {
        return m_height.intValue();
    }

    public IMsoModelIf.ModificationState getHeightState()
    {
        return m_height.getState();
    }

    public IAttributeIf.IMsoIntegerIf getHeightAttribute()
    {
        return m_height;
    }

    public void setLength(int aLength)
    {
        m_length.setValue(aLength);
    }

    public int getLength()
    {
        return m_length.intValue();
    }

    public IMsoModelIf.ModificationState getLengthState()
    {
        return m_length.getState();
    }

    public IAttributeIf.IMsoIntegerIf getLengthAttribute()
    {
        return m_length;
    }

    public void setMaxSpeed(int aMaxSpeed)
    {
        m_maxSpeed.setValue(aMaxSpeed);
    }

    public int getMaxSpeed()
    {
        return m_maxSpeed.intValue();
    }

    public IMsoModelIf.ModificationState getMaxSpeedState()
    {
        return m_maxSpeed.getState();
    }

    public IAttributeIf.IMsoIntegerIf getMaxSpeedAttribute()
    {
        return m_maxSpeed;
    }

    public void setType(int aType)
    {
        m_type.setValue(aType);
    }

    public int getType()
    {
        return m_type.intValue();
    }

    public IMsoModelIf.ModificationState getTypeState()
    {
        return m_type.getState();
    }

    public IAttributeIf.IMsoIntegerIf getTypeAttribute()
    {
        return m_type;
    }


    public String getTypeName()
    {
        return null; /*todo*/
    }


    public String toString()
    {
        return super.toString();
    }
}