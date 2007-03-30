package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Collection;

public class AssignmentImpl extends AbstractMsoObject implements IAssignmentIf
{
    private final AttributeImpl.MsoInteger m_priority = new AttributeImpl.MsoInteger(this, "Priority");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this, "Remarks");
    private final AttributeImpl.MsoInteger m_status = new AttributeImpl.MsoInteger(this, "Status");

    private final EquipmentListImpl m_assignmentEquipment = new EquipmentListImpl(this, "AssignmentEquipment", false);
    private final POIListImpl m_assignmentFindings = new POIListImpl(this, "AssignmentFindings", false);

    private final MsoReferenceImpl<IBriefingIf> m_assignmentBriefing = new MsoReferenceImpl<IBriefingIf>(this, "AssignmentBriefing", false);
    private final MsoReferenceImpl<IHypothesisIf> m_assignmentHypothesis = new MsoReferenceImpl<IHypothesisIf>(this, "AssignmentHypothesis", false);
    private final MsoReferenceImpl<IAreaIf> m_plannedArea = new MsoReferenceImpl<IAreaIf>(this, "PlannedArea", false);
    private final MsoReferenceImpl<IAreaIf> m_reportedArea = new MsoReferenceImpl<IAreaIf>(this, "ReportedArea", false);


    public AssignmentImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    protected void defineAttributes()
    {
        addAttribute(m_priority);
        addAttribute(m_remarks);
        addAttribute(m_status);
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

    public int POICount()
    {
        return m_assignmentFindings.size();
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

    public void setStatus(int aStatus)
    {
        m_status.setValue(aStatus);
    }

    public int getStatus()
    {
        return m_status.intValue();
    }

    public IMsoModelIf.ModificationState getStatusState()
    {
        return m_status.getState();
    }

    public IAttributeIf.IMsoIntegerIf getStatusAttribute()
    {
        return m_status;
    }

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

    public void addAssignmentFindings(IPOIIf anIPOIIf) throws DuplicateIdException
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

    public void setPlannedArea(IAreaIf anArea)
    {
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

    public void setReportedArea(IAreaIf anArea)
    {
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

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_ASSIGNMENT;
    }
}