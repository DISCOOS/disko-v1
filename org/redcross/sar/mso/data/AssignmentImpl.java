package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.AssignmentTransferUtilities;
import org.redcross.sar.util.Internationalization;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.except.MsoCastException;

import java.util.*;

/**
 * Unit assignments
 */
public class AssignmentImpl extends AbstractMsoObject implements IAssignmentIf
{
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");
    private final AttributeImpl.MsoInteger m_prioritySequence = new AttributeImpl.MsoInteger(this, "PrioritySequence");
    private final AttributeImpl.MsoCalendar m_timeEstimatedFinished = new AttributeImpl.MsoCalendar(this, "TimeEstimatedFinished");
//    private final AttributeImpl.MsoCalendar m_timeAssigned = new AttributeImpl.MsoCalendar(this, "TimeAssigned");
//    private final AttributeImpl.MsoCalendar m_timeStarted = new AttributeImpl.MsoCalendar(this, "TimeStarted");


    private final AttributeImpl.MsoEnum<AssignmentPriority> m_priority = new AttributeImpl.MsoEnum<AssignmentPriority>(this, "Priority", AssignmentPriority.LOW);
    private final AttributeImpl.MsoEnum<AssignmentStatus> m_status = new AttributeImpl.MsoEnum<AssignmentStatus>(this, "Status", AssignmentStatus.EMPTY);
    private final AttributeImpl.MsoEnum<AssignmentType> m_type = new AttributeImpl.MsoEnum<AssignmentType>(this, "Type", AssignmentType.GENERAL);

    private final AttributeImpl.MsoInteger m_number = new AttributeImpl.MsoInteger(this, "Number");

    private final EquipmentListImpl m_assignmentEquipment = new EquipmentListImpl(this, "AssignmentEquipment", false);
    private final POIListImpl m_assignmentFindings = new POIListImpl(this, "AssignmentFindings", false);

    private final MsoReferenceImpl<IBriefingIf> m_assignmentBriefing = new MsoReferenceImpl<IBriefingIf>(this, "AssignmentBriefing", true);
    //    private final MsoReferenceImpl<IHypothesisIf> m_assignmentHypothesis = new MsoReferenceImpl<IHypothesisIf>(this, "AssignmentHypothesis", true); todo slett
    private final MsoReferenceImpl<IAreaIf> m_plannedArea = new MsoReferenceImpl<IAreaIf>(this, "PlannedArea", true);
    private final MsoReferenceImpl<IAreaIf> m_reportedArea = new MsoReferenceImpl<IAreaIf>(this, "ReportedArea", true);

    private static final ResourceBundle bundle = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Assignment");

    public static String getText(String aKey)
    {
        return Internationalization.getFullBundleText(bundle, aKey);
    }

    public static String getEnumText(Enum anEnum)
    {
        return getText(anEnum.getClass().getSimpleName() + "." + anEnum.name() + ".text");
    }

    public AssignmentImpl(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber)
    {
        super(anObjectId);
        setNumber(aNumber);
        setType(getTypeBySubclass());
    }

    protected void defineAttributes()
    {
        addAttribute(m_remarks);
        addAttribute(m_prioritySequence);
//        addAttribute(m_timeAssigned);
        addAttribute(m_timeEstimatedFinished);
//        addAttribute(m_timeStarted);
        addAttribute(m_number);
        addAttribute(m_status);
        addAttribute(m_priority);
        addAttribute(m_type);
    }

    protected void defineLists()
    {
        addList(m_assignmentEquipment);
        addList(m_assignmentFindings);
    }

    protected void defineReferences()
    {
        addReference(m_assignmentBriefing);
//        addReference(m_assignmentHypothesis);
        addReference(m_plannedArea);
        addReference(m_reportedArea);
    }

    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        if (anObject instanceof IPOIIf)
        {
            m_assignmentFindings.add((IPOIIf) anObject);

        } else if (anObject instanceof IEquipmentIf)
        {
            m_assignmentEquipment.add((IEquipmentIf) anObject);
        }
    }

    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        if (anObject instanceof IPOIIf)
        {
            m_assignmentFindings.removeReference((IPOIIf) anObject);
        } else if (anObject instanceof IEquipmentIf)
        {
            m_assignmentEquipment.removeReference((IEquipmentIf) anObject);
        }
    }

    protected AssignmentType getTypeBySubclass()
    {
        return AssignmentType.GENERAL;
    }

    /**
     * Local implementation of {@link AbstractMsoObject#registerModifiedData()}
     * Resets correct subclass in case of incorrect changes by application or others.
     */
    public void registerModifiedData()
    {
        if (getType() != getTypeBySubclass())
        {
            setType(getTypeBySubclass());
        }
        super.registerModifiedData();
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
        setStatusAndOwner(aStatus, getOwningUnit());
    }

    public void setStatusAndOwner(AssignmentStatus aStatus, IUnitIf aUnit) throws IllegalOperationException
    {
        if (!AssignmentTransferUtilities.assignmentCanChangeToStatus(this, aStatus, aUnit))
        {
            throw new IllegalOperationException("Cannont change status from " + getStatus() + " to " + aStatus);
        }
        changeStatusAndOwner(aUnit, aStatus);
    }

    public void setStatus(String aStatus) throws IllegalOperationException
    {
        setStatusAndOwner(aStatus, getOwningUnit());
    }

    public void setStatusAndOwner(String aStatus, IUnitIf aUnit) throws IllegalOperationException
    {
        AssignmentStatus status = getStatusAttribute().enumValue(aStatus);
        if (status == null)
        {
            return;
        }
        setStatusAndOwner(status, aUnit);
    }

    private void changeStatusAndOwner(IUnitIf aUnit, AssignmentStatus aStatus)
    {
        IUnitIf owningUnit = getOwningUnit();
        AssignmentStatus oldStatus = getStatus();
        boolean changeOwner = aUnit != owningUnit;
        if (changeOwner && owningUnit != null)
        {
            owningUnit.removeUnitReference(this);
        }
        m_status.setValue(aStatus);

        if (aStatus == AssignmentStatus.ALLOCATED)
        {
            setPrioritySequence(Integer.MAX_VALUE);
        }

        if (aUnit != null)
        {
            if (changeOwner)
            {
                aUnit.addUnitReference(this);
            } else // prioritySequence can have been changed even if status is unchanged.
            {
                aUnit.rearrangeAsgPrioritiesAfterStatusChange(this, oldStatus);
            }
        }

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

    public String getStatusText()
    {
        return getEnumText(getStatus());
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

    public String getPriorityText()
    {
        return getEnumText(getPriority());
    }


    protected void setType(AssignmentType aType)
    {
        m_type.setValue(aType);
    }

    public AssignmentType getType()
    {
        return m_type.getValue();
    }

    public IMsoModelIf.ModificationState getTypeState()
    {
        return m_type.getState();
    }

    public IAttributeIf.IMsoEnumIf<AssignmentType> getTypeAttribute()
    {
        return m_type;
    }

    public String getTypeText()
    {
        return getEnumText(getType());
    }
    
    public String getTypeAndNumber()
    {
    	return getTypeText() + " " + getNumber();
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

    public IAttributeIf.IMsoIntegerIf getNumberAttribute()
    {
        return m_number;
    }

//    public void setTimeAssigned(Calendar aTimeAssigned)
//    {
//        m_timeAssigned.setValue(aTimeAssigned);
//    }

    public Calendar getTimeAssigned()
    {
        return getMessageLineTime(IMessageLineIf.MessageLineType.ASSIGNED);
    }

    private Calendar getMessageLineTime(IMessageLineIf.MessageLineType aLineType)
    {
        IMessageLineIf line = getLatestStatusChangeMessageLine(aLineType);
        if (line != null)
        return line.getOperationTime();
        return null;
    }

    private IMsoModelIf.ModificationState  getMessageLineTimeState(IMessageLineIf.MessageLineType aLineType)
    {
        IMessageLineIf line = getLatestStatusChangeMessageLine(aLineType);
        if (line != null)
        return line.getOperationTimeState();
        return null;
    }

    private IAttributeIf.IMsoCalendarIf getMessageLineTimeAttribute(IMessageLineIf.MessageLineType aLineType)
    {
        IMessageLineIf line = getLatestStatusChangeMessageLine(aLineType);
        if (line != null)
        return line.getOperationTimeAttribute();
        return null;
    }

    public IMsoModelIf.ModificationState getTimeAssignedState()
    {
        return getMessageLineTimeState(IMessageLineIf.MessageLineType.ASSIGNED);
    }

    public IAttributeIf.IMsoCalendarIf getTimeAssignedAttribute()
    {
        return getMessageLineTimeAttribute(IMessageLineIf.MessageLineType.ASSIGNED);
    }

    public void setTimeEstimatedFinished(Calendar aTimeEstimatedFinished)
    {
        m_timeEstimatedFinished.setValue(aTimeEstimatedFinished);
    }

    public Calendar getTimeEstimatedFinished()
    {
        return m_timeEstimatedFinished.getCalendar();
    }

    public IMsoModelIf.ModificationState getTimeEstimatedFinishedState()
    {
        return m_timeEstimatedFinished.getState();
    }

    public IAttributeIf.IMsoCalendarIf getTimeEstimatedFinishedAttribute()
    {
        return m_timeEstimatedFinished;
    }

//    public void setTimeStarted(Calendar aTimeStarted)
//    {
//        m_timeStarted.setValue(aTimeStarted);
//    }

    public Calendar getTimeStarted()
    {
            return getMessageLineTime(IMessageLineIf.MessageLineType.STARTED);
    }

    public IMsoModelIf.ModificationState getTimeStartedState()
    {
        return getMessageLineTimeState(IMessageLineIf.MessageLineType.STARTED);
    }

    public IAttributeIf.IMsoCalendarIf getTimeStartedAttribute()
    {
        return getMessageLineTimeAttribute(IMessageLineIf.MessageLineType.STARTED);
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addAssignmentEquipment(IEquipmentIf anIEquipmentIf)
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

    public void addAssignmentFinding(IPOIIf anIPOIIf)
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

    public boolean hasBeenAllocated()
    {
        return getStatus().ordinal() >= AssignmentStatus.ALLOCATED.ordinal();
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
        return getStatus().ordinal() >= AssignmentStatus.ABORTED.ordinal();
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

    public void verifyAllocatable(AssignmentStatus newStatus, IUnitIf aUnit, boolean unassignIfPossible) throws IllegalOperationException
    {
        // todo Test on type of assigment compared to type of unit.
        if (!AssignmentTransferUtilities.assignmentCanChangeToStatus(this, AssignmentStatus.ALLOCATED, aUnit))
        {
            throw new IllegalOperationException("Assignment " + this + " cannot change status to ALLOCATED.");
        }
        if (getStatus() == AssignmentStatus.ALLOCATED && !unassignIfPossible)
        {
            throw new IllegalOperationException("Assignment " + this + " is already allocated to another unit and cannot be reallocated.");
        }
        IUnitIf owningUnit = getOwningUnit();
        if (owningUnit != null && owningUnit != aUnit)
        {
            owningUnit.removeUnitReference(this);
        } else
        {
            setStatusAndOwner(newStatus, aUnit);
        }
    }

    public int getTypenr()
    {
        return 0;
    }

    public IMessageLineIf getLatestStatusChangeMessageLine(final IMessageLineIf.MessageLineType aType)
    {
        List<IMessageLineIf> retVal = MsoModelImpl.getInstance().getMsoManager().getCmdPost().getMessageLines().selectItems(
                new SelfSelector<IAssignmentIf, IMessageLineIf>(this)
                {
                    public boolean select(IMessageLineIf anObject)
                    {
                        return (anObject.getLineAssignment() == m_object && anObject.getLineType() == aType && anObject.getOperationTime() != null);
                    }
                },
                new Comparator<IMessageLineIf>()
                {
                    public int compare(IMessageLineIf o1, IMessageLineIf o2)
                    {
                        return o2.getOperationTime().compareTo(o1.getOperationTime()); // Sort according to latest time
                    }
                });
        return (retVal.size() == 0) ? null : retVal.get(0);
    }
}