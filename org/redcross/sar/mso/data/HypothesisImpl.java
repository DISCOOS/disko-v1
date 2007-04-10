package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;

public class HypothesisImpl extends AbstractMsoObject implements IHypothesisIf
{
    private final AttributeImpl.MsoString m_description = new AttributeImpl.MsoString(this, "Description");
    private final AttributeImpl.MsoInteger m_number = new AttributeImpl.MsoInteger(this, "Number");
    private final AttributeImpl.MsoInteger m_priority = new AttributeImpl.MsoInteger(this, "Priority");

    private final AttributeImpl.MsoEnum<HypothesisStatus> m_status = new AttributeImpl.MsoEnum<HypothesisStatus>(this, "Status", HypothesisStatus.ACTIVE);


    public HypothesisImpl(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber)
    {
        super(anObjectId);
        setNumber(aNumber);
    }

    protected void defineAttributes()
    {
        addAttribute(m_description);
        addAttribute(m_number);
        addAttribute(m_priority);
        addAttribute(m_status);
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

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setStatus(HypothesisStatus aStatus)
    {
        m_status.setValue(aStatus);
    }

    public void setStatus(String aStatus)
    {
        m_status.setValue(aStatus);
    }

    public HypothesisStatus getStatus()
    {
        return m_status.getValue();
    }

    public IMsoModelIf.ModificationState getStatusState()
    {
        return m_status.getState();
    }

    public IAttributeIf.IMsoEnumIf<HypothesisStatus> getStatusAttribute()
    {
        return m_status;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setDescription(String aDescription)
    {
        m_description.setValue(aDescription);
    }

    public String getDescription()
    {
        return m_description.getString();
    }

    public IMsoModelIf.ModificationState getDescriptionState()
    {
        return m_description.getState();
    }

    public IAttributeIf.IMsoStringIf getDescriptionAttribute()
    {
        return m_description;
    }

    public IAttributeIf.IMsoIntegerIf getNumberAttribute()
    {
        return m_number;
    }

    public void setPriority(int aPriority)
    {
        m_priority.setValue(aPriority);
    }

    public int getPriority()
    {
        return m_priority.intValue();
    }

    public IMsoModelIf.ModificationState getPriorityState()
    {
        return m_priority.getState();
    }

    public IAttributeIf.IMsoIntegerIf getPriorityAttribute()
    {
        return m_priority;
    }

    // From ISerialNumberedIf
    public void setNumber(int aNumber)
    {
        m_number.setValue(aNumber);
    }

    public int getNumber()
    {
        return m_number.intValue();
    }

    public IMsoModelIf.ModificationState getNumberState()
    {
        return m_number.getState();
    }

}