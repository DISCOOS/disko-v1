package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.IllegalOperationException;

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
        ALLOCATED,
        ASSIGNED,
        EXECUTING,
        PAUSED,
        ABORTED,
        FINISHED,
        REPORTED
    }

    public enum AssignmentPriority
    {
        HIGH,
        MEDIUM,
        LOW
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setStatus(AssignmentStatus aStatus) throws IllegalOperationException;

    public void setStatus(String aStatus);

    public AssignmentStatus getStatus();

    public IMsoModelIf.ModificationState getStatusState();

    public IAttributeIf.IMsoEnumIf<AssignmentStatus> getStatusAttribute();

    public void setPriority(AssignmentPriority aPriority);

    public void setPriority(String aPriority);

    public AssignmentPriority getPriority();

    public IMsoModelIf.ModificationState getPriorityState();

    public IAttributeIf.IMsoEnumIf<AssignmentPriority> getPriorityAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setRemarks(String aRemarks);

    public String getRemarks();

    public IMsoModelIf.ModificationState getRemarksState();

    public IAttributeIf.IMsoStringIf getRemarksAttribute();

    public void setPrioritySequence(int aPrioritySequence);

    public int getPrioritySequence();

    public IMsoModelIf.ModificationState getPrioritySequenceState();

    public IAttributeIf.IMsoIntegerIf getPrioritySequenceAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addAssignmentEquipment(IEquipmentIf anIEquipmentIf) throws DuplicateIdException;

    public IEquipmentListIf getAssignmentEquipment();

    public IMsoModelIf.ModificationState getAssignmentEquipmentState(IEquipmentIf anIEquipmentIf);

    public Collection<IEquipmentIf> getAssignmentEquipmentItems();

    public void addAssignmentFinding(IPOIIf anIPOIIf) throws DuplicateIdException;

    public IPOIListIf getAssignmentFindings();

    public IMsoModelIf.ModificationState getAssignmentFindingsState(IPOIIf anIPOIIf);

    public Collection<IPOIIf> getAssignmentFindingsItems();

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setAssignmentBriefing(IBriefingIf aBriefing);

    public IBriefingIf getAssignmentBriefing();

    public IMsoModelIf.ModificationState getAssignmentBriefingState();

    public IMsoReferenceIf<IBriefingIf> getAssignmentBriefingAttribute();

    public void setAssignmentHypothesis(IHypothesisIf anHypothesis);

    public IHypothesisIf getAssignmentHypothesis();

    public IMsoModelIf.ModificationState getAssignmentHypothesisState();

    public IMsoReferenceIf<IHypothesisIf> getAssignmentHypothesisAttribute();

    public void setPlannedArea(IAreaIf anArea) throws IllegalOperationException;

    public IAreaIf getPlannedArea();

    public IMsoModelIf.ModificationState getPlannedAreaState();

    public IMsoReferenceIf<IAreaIf> getPlannedAreaAttribute();

    public void setReportedArea(IAreaIf anArea) throws IllegalOperationException;

    public IAreaIf getReportedArea();

    public IMsoModelIf.ModificationState getReportedAreaState();

    public IMsoReferenceIf<IAreaIf> getReportedAreaAttribute();

    /*-------------------------------------------------------------------------------------------
    * Other methods
    *-------------------------------------------------------------------------------------------*/

    public boolean isNotReady();

    public boolean hasBeenAssigned();

    public boolean hasBeenStarted();

    public boolean hasBeenFinished();

    public boolean canChangeToState(AssignmentStatus newState);

    public IUnitIf getOwningUnit();

    /**
     * Verify that the assigment can be assigned to a given unit.
     * @param aUnit The unit that shall have the assigment.
     * @param newStatus The new status that the assignment shall have.
     * @param unassignIfPossible If <code>true</code>, the assigment will be assigned from any other possible other unit.
     * @throws IllegalOperationException If the assignment cannot be done (verification failed).
     */
    public void verifyAssignable(IUnitIf aUnit, IAssignmentIf.AssignmentStatus newStatus, boolean unassignIfPossible) throws IllegalOperationException;
}