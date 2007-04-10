package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

/**
 *
 */
public interface IHypothesisIf extends IMsoObjectIf
{
    /**
     * Status enum
     */
    public enum HypothesisStatus
    {
        ACTIVE,
        PLAUSIBLE,
        REJECTED,
        CHECKED,
        SUCCESS
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setStatus(HypothesisStatus aStatus);

    public void setStatus(String aStatus);

    public HypothesisStatus getStatus();

    public IMsoModelIf.ModificationState getStatusState();

    public IAttributeIf.IMsoEnumIf<HypothesisStatus> getStatusAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setDescription(String aDescription);

    public String getDescription();

    public IMsoModelIf.ModificationState getDescriptionState();

    public IAttributeIf.IMsoStringIf getDescriptionAttribute();

    public void setPriority(int aPriority);

    public int getPriority();

    public IMsoModelIf.ModificationState getPriorityState();

    public IAttributeIf.IMsoIntegerIf getPriorityAttribute();
}
