package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.IllegalOperationException;

import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.EnumSet;

/**
 *
 */
public interface IAssignmentIf extends IMsoObjectIf, ISerialNumberedIf, IEnumStatusHolder<IAssignmentIf.AssignmentStatus>
{
    public enum AssignmentStatus
    {
        EMPTY,
        DRAFT,
        READY,
        ALLOCATED,
        ASSIGNED,
        EXECUTING,
//        PAUSED,
        ABORTED,
        FINISHED,
        REPORTED
    }

    public enum AssignmentPriority
    {
        HIGH,
        NORMAL,
        LOW
    }

    public enum AssignmentType
    {
        GENERAL,
        ASSISTANCE,
        SEARCH
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setStatus(AssignmentStatus aStatus) throws IllegalOperationException;

    public void setStatusAndOwner(AssignmentStatus aStatus, IUnitIf aUnit) throws IllegalOperationException;

    public void setStatus(String aStatus) throws IllegalOperationException;

    public void setStatusAndOwner(String aStatus, IUnitIf aUnit) throws IllegalOperationException;

    public AssignmentStatus getStatus();

    public IMsoModelIf.ModificationState getStatusState();

    public IAttributeIf.IMsoEnumIf<AssignmentStatus> getStatusAttribute();

    public String getStatusText();

    public void setPriority(AssignmentPriority aPriority);

    public void setPriority(String aPriority);

    public AssignmentPriority getPriority();

    public IMsoModelIf.ModificationState getPriorityState();

    public String getPriorityText();

    public IAttributeIf.IMsoEnumIf<AssignmentPriority> getPriorityAttribute();

    public AssignmentType getType();

    public IMsoModelIf.ModificationState getTypeState();

    public IAttributeIf.IMsoEnumIf<AssignmentType> getTypeAttribute();

    public String getTypeText();

    public String getTypeAndNumber();

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

//    public void setTimeAssigned(Calendar aTimeAssigned);

    public Calendar getTimeAssigned();

    public IMsoModelIf.ModificationState getTimeAssignedState();

    public IAttributeIf.IMsoCalendarIf getTimeAssignedAttribute();

    public void setTimeEstimatedFinished(Calendar aTimeEstimatedFinished);

    public Calendar getTimeEstimatedFinished();

    public IMsoModelIf.ModificationState getTimeEstimatedFinishedState();

    public IAttributeIf.IMsoCalendarIf getTimeEstimatedFinishedAttribute();

//    public void setTimeStarted(Calendar aTimeStarted);

    public Calendar getTimeStarted();

    public IMsoModelIf.ModificationState getTimeStartedState();

    public IAttributeIf.IMsoCalendarIf getTimeStartedAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addAssignmentEquipment(IEquipmentIf anIEquipmentIf);

    public IEquipmentListIf getAssignmentEquipment();

    public IMsoModelIf.ModificationState getAssignmentEquipmentState(IEquipmentIf anIEquipmentIf);

    public Collection<IEquipmentIf> getAssignmentEquipmentItems();

    public void addAssignmentFinding(IPOIIf anIPOIIf);

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

    /**
     * Assign planned area
     *
     * @param anArea The area to assign
     * @throws IllegalOperationException If the area has been assigned to another assignment.
     */
    public void setPlannedArea(IAreaIf anArea) throws IllegalOperationException;

    public IAreaIf getPlannedArea();

    public IMsoModelIf.ModificationState getPlannedAreaState();

    public IMsoReferenceIf<IAreaIf> getPlannedAreaAttribute();

    /**
     * Assign reported area
     *
     * @param anArea The area to assign
     * @throws IllegalOperationException If the area has been assigned to another assignment.
     */
    public void setReportedArea(IAreaIf anArea) throws IllegalOperationException;

    public IAreaIf getReportedArea();

    public IMsoModelIf.ModificationState getReportedAreaState();

    public IMsoReferenceIf<IAreaIf> getReportedAreaAttribute();

    /*-------------------------------------------------------------------------------------------
    * Other methods
    *-------------------------------------------------------------------------------------------*/

    public boolean isNotReady();

    public boolean hasBeenAllocated();

    public boolean hasBeenAssigned();

    public boolean hasBeenStarted();

    public boolean hasBeenFinished();

    public IUnitIf getOwningUnit();

    /**
     * Verify that the assigment can be assigned to a given unit.
     *
     * @param newStatus          The new status that the assignment shall have.
     * @param aUnit              The unit that shall have the assigment.
     * @param unassignIfPossible If <code>true</code>, the assigment will be assigned from any other possible other unit.
     * @throws IllegalOperationException If the assignment cannot be done (verification failed).
     */
    public void verifyAllocatable(AssignmentStatus newStatus, IUnitIf aUnit, boolean unassignIfPossible) throws IllegalOperationException;

    int getTypenr();

//    public final static EnumSet<AssignmentStatus> CAN_START_SET = EnumSet.of(AssignmentStatus.ALLOCATED, AssignmentStatus.ASSIGNED);
    public final static EnumSet<AssignmentStatus> ACTIVE_SET = EnumSet.of(AssignmentStatus.ALLOCATED, AssignmentStatus.ASSIGNED,AssignmentStatus.EXECUTING);
    public final static EnumSet<AssignmentStatus> FINISHED_SET = EnumSet.of(AssignmentStatus.ABORTED, AssignmentStatus.FINISHED);
    public final static EnumSet<AssignmentStatus> FINISHED_AND_REPORTED_SET = EnumSet.of(AssignmentStatus.ABORTED, AssignmentStatus.FINISHED, AssignmentStatus.REPORTED);
    public final static AbstractMsoObject.StatusSelector<IAssignmentIf, AssignmentStatus> READY_SELECTOR = new AbstractMsoObject.StatusSelector<IAssignmentIf, AssignmentStatus>(AssignmentStatus.READY);
    public final static AbstractMsoObject.StatusSelector<IAssignmentIf, AssignmentStatus> ALLOCATED_SELECTOR = new AbstractMsoObject.StatusSelector<IAssignmentIf, AssignmentStatus>(AssignmentStatus.ALLOCATED);
    public final static AbstractMsoObject.StatusSelector<IAssignmentIf, AssignmentStatus> ASSIGNED_SELECTOR = new AbstractMsoObject.StatusSelector<IAssignmentIf, AssignmentStatus>(AssignmentStatus.ASSIGNED);
    public final static AbstractMsoObject.StatusSelector<IAssignmentIf, AssignmentStatus> EXECUTING_SELECTOR = new AbstractMsoObject.StatusSelector<IAssignmentIf, AssignmentStatus>(AssignmentStatus.EXECUTING);
    public final static AbstractMsoObject.StatusSetSelector<IAssignmentIf, AssignmentStatus> FINISHED_SELECTOR = new AbstractMsoObject.StatusSetSelector<IAssignmentIf, AssignmentStatus>(FINISHED_AND_REPORTED_SET);

    public final static Comparator<IAssignmentIf> PRIORITY_SEQUENCE_COMPARATOR = new Comparator<IAssignmentIf>()
    {
        public int compare(IAssignmentIf o1, IAssignmentIf o2)
        {
            return o1.getPrioritySequence() - o2.getPrioritySequence();
        }
    };

    public final static Comparator<IAssignmentIf> PRIORITY_COMPARATOR = new Comparator<IAssignmentIf>()
    {
        public int compare(IAssignmentIf o1, IAssignmentIf o2)
        {
            return o1.getPriority().compareTo(o2.getPriority());
        }
    };

    public final static Comparator<IAssignmentIf> PRIORITY_AND_NUMBER_COMPARATOR = new Comparator<IAssignmentIf>()
    {
        public int compare(IAssignmentIf u1, IAssignmentIf u2)
        {
            if (u1.getPriority().equals(u2.getPriority()))
            {
                return u1.getNumber() - u2.getNumber();
            } else
            {
                // Compare priorities so that high priority comes first (high priority is "smaller than" low priority)
                return u1.getPriority().compareTo(u2.getPriority());
            }
        }
    };

     public IMessageLineIf getLatestStatusChangeMessageLine(final IMessageLineIf.MessageLineType aType);

}