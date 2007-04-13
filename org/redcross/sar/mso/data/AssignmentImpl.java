package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.except.DuplicateIdException;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.mso.Selector;

import java.util.Collection;
import java.util.List;
import java.util.EnumSet;
import java.util.Comparator;

/**
 * Unit assignments
 */
public class AssignmentImpl extends AbstractMsoObject implements IAssignmentIf
{
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");
    private final AttributeImpl.MsoInteger m_prioritySequence = new AttributeImpl.MsoInteger(this, "PrioritySequence");
    private final AttributeImpl.MsoEnum<AssignmentPriority> m_priority = new AttributeImpl.MsoEnum<AssignmentPriority>(this, "Priority", AssignmentPriority.LOW);
    private final AttributeImpl.MsoEnum<AssignmentStatus> m_status = new AttributeImpl.MsoEnum<AssignmentStatus>(this, "Status", AssignmentStatus.EMPTY);

    private final EquipmentListImpl m_assignmentEquipment = new EquipmentListImpl(this, "AssignmentEquipment", false);
    private final POIListImpl m_assignmentFindings = new POIListImpl(this, "AssignmentFindings", false);

    private final MsoReferenceImpl<IBriefingIf> m_assignmentBriefing = new MsoReferenceImpl<IBriefingIf>(this, "AssignmentBriefing", true);
    private final MsoReferenceImpl<IHypothesisIf> m_assignmentHypothesis = new MsoReferenceImpl<IHypothesisIf>(this, "AssignmentHypothesis", true);
    private final MsoReferenceImpl<IAreaIf> m_plannedArea = new MsoReferenceImpl<IAreaIf>(this, "PlannedArea", true);
    private final MsoReferenceImpl<IAreaIf> m_reportedArea = new MsoReferenceImpl<IAreaIf>(this, "ReportedArea", true);

    private final static EnumSet<AssignmentStatus> m_activeSet = EnumSet.range(AssignmentStatus.EXECUTING, AssignmentStatus.PAUSED);
    private final static EnumSet<AssignmentStatus> m_readySet = EnumSet.range(AssignmentStatus.READY, AssignmentStatus.ASSIGNED);
    private final static StatusSelector m_assignedSelector = new StatusSelector(AssignmentStatus.ASSIGNED);
    private final static StatusSelector m_executingSelector = new StatusSelector(AssignmentStatus.EXECUTING);
    private final static StatusSelector m_finishedSelector = new StatusSelector(AssignmentStatus.FINISHED);
    private final static PrioritySequenceComparator m_prioritySequenceComparator  = new PrioritySequenceComparator();

    public static StatusSelector getAssignedSelector()
    {
        return m_assignedSelector;
    }

    public static StatusSelector getExecutingSelector()
    {
        return m_executingSelector;
    }

    public static StatusSelector getFinishedSelector()
    {
        return m_finishedSelector;
    }

    public static PrioritySequenceComparator getPrioritySequenceComparator()
    {
        return m_prioritySequenceComparator;
    }

    public AssignmentImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }


    protected void defineAttributes()
    {
        addAttribute(m_remarks);
        addAttribute(m_prioritySequence);
        addAttribute(m_status);
        addAttribute(m_priority);
    }

    protected void defineLists()
    {
        addList(m_assignmentEquipment);
        addList(m_assignmentFindings);
    }

    protected void defineReferences()
    {
        addReference(m_assignmentBriefing);
        addReference(m_assignmentHypothesis);
        addReference(m_plannedArea);
        addReference(m_reportedArea);
    }

    public static AssignmentImpl implementationOf(IAssignmentIf anInterface) throws MsoCastException
    {
        try
        {
            return (AssignmentImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to CmdPostImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT;
    }

    public int POICount()
    {
        return m_assignmentFindings.size();
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setStatus(AssignmentStatus aStatus) throws IllegalOperationException
    {
        if (!canChangeToState(aStatus))
        {
            throw new IllegalOperationException("Cannont change status from " + getStatus() + " to " + aStatus);
        }
        m_status.setValue(aStatus);
    }

    public void setStatus(String aStatus)
    {
        m_status.setValue(aStatus);
    }

    public AssignmentStatus getStatus()
    {
        return m_status.getValue();
    }

    public IMsoModelIf.ModificationState getStatusState()
    {
        return m_status.getState();
    }

    public IAttributeIf.IMsoEnumIf<AssignmentStatus> getStatusAttribute()
    {
        return m_status;
    }

    public void setPriority(AssignmentPriority aPriority)
    {
        m_priority.setValue(aPriority);
    }

    public void setPriority(String aPriority)
    {
        m_priority.setValue(aPriority);
    }

    public AssignmentPriority getPriority()
    {
        return m_priority.getValue();
    }

    public IMsoModelIf.ModificationState getPriorityState()
    {
        return m_priority.getState();
    }

    public IAttributeIf.IMsoEnumIf<AssignmentPriority> getPriorityAttribute()
    {
        return m_priority;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setRemarks(String aRemarks)
    {
        m_remarks.setValue(aRemarks);
    }

    public String getRemarks()
    {
        return m_remarks.getString();
    }

    public IMsoModelIf.ModificationState getRemarksState()
    {
        return m_remarks.getState();
    }

    public IAttributeIf.IMsoStringIf getRemarksAttribute()
    {
        return m_remarks;
    }

    public void setPrioritySequence(int aPrioritySequence)
    {
        m_prioritySequence.setValue(aPrioritySequence);
    }

    public int getPrioritySequence()
    {
        return m_prioritySequence.intValue();
    }

    public IMsoModelIf.ModificationState getPrioritySequenceState()
    {
        return m_prioritySequence.getState();
    }

    public IAttributeIf.IMsoIntegerIf getPrioritySequenceAttribute()
    {
        return m_prioritySequence;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addAssignmentEquipment(IEquipmentIf anIEquipmentIf) throws DuplicateIdException
    {
        m_assignmentEquipment.add(anIEquipmentIf);
    }

    public IEquipmentListIf getAssignmentEquipment()
    {
        return m_assignmentEquipment;
    }

    public IMsoModelIf.ModificationState getAssignmentEquipmentState(IEquipmentIf anIEquipmentIf)
    {
        return m_assignmentEquipment.getState(anIEquipmentIf);
    }

    public Collection<IEquipmentIf> getAssignmentEquipmentItems()
    {
        return m_assignmentEquipment.getItems();
    }

    public void addAssignmentFinding(IPOIIf anIPOIIf) throws DuplicateIdException
    {
        m_assignmentFindings.add(anIPOIIf);
    }

    public IPOIListIf getAssignmentFindings()
    {
        return m_assignmentFindings;
    }

    public IMsoModelIf.ModificationState getAssignmentFindingsState(IPOIIf anIPOIIf)
    {
        return m_assignmentFindings.getState(anIPOIIf);
    }

    public Collection<IPOIIf> getAssignmentFindingsItems()
    {
        return m_assignmentFindings.getItems();
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setAssignmentBriefing(IBriefingIf aBriefing)
    {
        m_assignmentBriefing.setReference(aBriefing);
    }

    public IBriefingIf getAssignmentBriefing()
    {
        return m_assignmentBriefing.getReference();
    }

    public IMsoModelIf.ModificationState getAssignmentBriefingState()
    {
        return m_assignmentBriefing.getState();
    }

    public IMsoReferenceIf<IBriefingIf> getAssignmentBriefingAttribute()
    {
        return m_assignmentBriefing;
    }

    public void setAssignmentHypothesis(IHypothesisIf anHypothesis)
    {
        m_assignmentHypothesis.setReference(anHypothesis);
    }

    public IHypothesisIf getAssignmentHypothesis()
    {
        return m_assignmentHypothesis.getReference();
    }

    public IMsoModelIf.ModificationState getAssignmentHypothesisState()
    {
        return m_assignmentHypothesis.getState();
    }

    public IMsoReferenceIf<IHypothesisIf> getAssignmentHypothesisAttribute()
    {
        return m_assignmentHypothesis;
    }

    public void setPlannedArea(IAreaIf anArea) throws IllegalOperationException
    {
        anArea.verifyAssignable(this);
        m_plannedArea.setReference(anArea);
    }

    public IAreaIf getPlannedArea()
    {
        return m_plannedArea.getReference();
    }

    public IMsoModelIf.ModificationState getPlannedAreaState()
    {
        return m_plannedArea.getState();
    }

    public IMsoReferenceIf<IAreaIf> getPlannedAreaAttribute()
    {
        return m_plannedArea;
    }

    public void setReportedArea(IAreaIf anArea) throws IllegalOperationException
    {
        anArea.verifyAssignable(this);
        m_reportedArea.setReference(anArea);
    }

    public IAreaIf getReportedArea()
    {
        return m_reportedArea.getReference();
    }

    public IMsoModelIf.ModificationState getReportedAreaState()
    {
        return m_reportedArea.getState();
    }

    public IMsoReferenceIf<IAreaIf> getReportedAreaAttribute()
    {
        return m_reportedArea;
    }


    public boolean isNotReady()
    {
        return getStatus().ordinal() < AssignmentStatus.READY.ordinal();
    }

    public boolean hasBeenAssigned()
    {
        return getStatus().ordinal() >= AssignmentStatus.ASSIGNED.ordinal();
    }

    public boolean hasBeenStarted()
    {
        return getStatus().ordinal() >= AssignmentStatus.EXECUTING.ordinal();
    }

    public boolean hasBeenFinished()
    {
        return getStatus().ordinal() >= AssignmentStatus.FINISHED.ordinal();
    }

    public boolean canChangeToState(String newState)
    {
        AssignmentStatus status = getStatusAttribute().enumValue(newState);
        if (status == null)
        {
            return false;
        }
        return canChangeToState(status);
    }

    public boolean canChangeToState(AssignmentStatus newState)
    {
        if (getStatus() == newState)
        {
            return true;
        }
        if (getStatus() == AssignmentStatus.EMPTY)
        {
            return newState == AssignmentStatus.DRAFT || newState == AssignmentStatus.READY;
        }
        if (getStatus() == AssignmentStatus.DRAFT)
        {
            return newState == AssignmentStatus.READY;
        }
        if (getStatus().ordinal() < newState.ordinal())
        {
            return true;
        }
        if (m_readySet.contains(getStatus()))
        {
            return m_readySet.contains(newState);
        }
        if (m_activeSet.contains(getStatus()))
        {
            return m_activeSet.contains(newState);
        }
        return false;
    }

    public IUnitIf getOwningUnit()
    {
        List<IUnitIf> retVal = MsoModelImpl.getInstance().getMsoManager().getCmdPost().getUnitList().selectItems(
                new SelfSelector<IAssignmentIf, IUnitIf>(this)
                {
                    public boolean select(IUnitIf anObject)
                    {
                        return (anObject.getUnitAssignments().contains(m_object));
                    }
                }, null);
        return (retVal.size() == 0) ? null : retVal.get(0);
    }

    public void verifyAssignable(IUnitIf aUnit, AssignmentStatus newStatus, boolean unassignIfPossible) throws IllegalOperationException
    {
        // todo Test on type of assigment compared to type of unit.
        if (!canChangeToState(AssignmentStatus.ASSIGNED))
        {
            throw new IllegalOperationException("Assignment " + this + " cannot change status to ASSIGNED.");
        }
        if (getStatus() == AssignmentStatus.ASSIGNED && !unassignIfPossible)
        {
            throw new IllegalOperationException("Assignment " + this + " is already assigned to another unit and cannot be reassigned.");
        }
        IUnitIf owningUnit = getOwningUnit();
        if (owningUnit != null && owningUnit != aUnit)
        {
            owningUnit.removeUnitAssignment(this, newStatus);
        } else
        {
            setStatus(newStatus);
        }
    }

    /**
     * Selector used for selecting assignments with a given status.
     */
    public static class StatusSelector implements Selector<IAssignmentIf>
    {
        AssignmentStatus m_selectValue;

        /**
         * Construct a Selector object
         * @param aStatus  The status to test against
         */
        public StatusSelector(AssignmentStatus aStatus)
        {
            m_selectValue = aStatus;
        }

        public boolean select(IAssignmentIf anObject)
        {
            return anObject.getStatus() == m_selectValue;
        }
    }
    
    public static class PrioritySequenceComparator implements Comparator<IAssignmentIf>
    {
        public int compare(IAssignmentIf o1, IAssignmentIf o2)
        {
            return o1.getPrioritySequence() - o2.getPrioritySequence();
        }
    }


}