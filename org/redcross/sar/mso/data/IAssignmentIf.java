package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Collection;

/**
 *
 */
public interface IAssignmentIf extends IMsoObjectIf
{
    public enum AssignmentStatus
    {
        EMPTY,
        DRAFT,
        READY,
        ASSIGNED,
        EXECUTING,
        ABORTED,
        FINISHED,
        REPORTED
    }

    public void setPriority(int aPriority);

    public int getPriority();

    public IMsoModelIf.ModificationState getPriorityState();

    public IAttributeIf.IMsoIntegerIf getPriorityAttribute();

    public void setRemarks(String aRemarks);

    public String getRemarks();

    public IMsoModelIf.ModificationState getRemarksState();

    public IAttributeIf.IMsoStringIf getRemarksAttribute();

    public void setStatus(int aStatus);

    public int getStatus();

    public IMsoModelIf.ModificationState getStatusState();

    public IAttributeIf.IMsoIntegerIf getStatusAttribute();

    public void addAssignmentEquipment(IEquipmentIf anIEquipmentIf) throws DuplicateIdException;

    public IEquipmentListIf getAssignmentEquipment();

    public IMsoModelIf.ModificationState getAssignmentEquipmentState(IEquipmentIf anIEquipmentIf);

    public Collection<IEquipmentIf> getAssignmentEquipmentItems();

    public void addAssignmentFindings(IPOIIf anIPOIIf) throws DuplicateIdException;

    public IPOIListIf getAssignmentFindings();

    public IMsoModelIf.ModificationState getAssignmentFindingsState(IPOIIf anIPOIIf);

    public Collection<IPOIIf> getAssignmentFindingsItems();

    public void setAssignmentBriefing(IBriefingIf aBriefing);

    public IBriefingIf getAssignmentBriefing();

    public IMsoModelIf.ModificationState getAssignmentBriefingState();

    public IMsoReferenceIf<IBriefingIf> getAssignmentBriefingAttribute();

    public void setAssignmentHypothesis(IHypothesisIf anHypothesis);

    public IHypothesisIf getAssignmentHypothesis();

    public IMsoModelIf.ModificationState getAssignmentHypothesisState();

    public IMsoReferenceIf<IHypothesisIf> getAssignmentHypothesisAttribute();

    public void setPlannedArea(IAreaIf anArea);

    public IAreaIf getPlannedArea();

    public IMsoModelIf.ModificationState getPlannedAreaState();

    public IMsoReferenceIf<IAreaIf> getPlannedAreaAttribute();

    public void setReportedArea(IAreaIf anArea);

    public IAreaIf getReportedArea();

    public IMsoModelIf.ModificationState getReportedAreaState();

    public IMsoReferenceIf<IAreaIf> getReportedAreaAttribute();
}