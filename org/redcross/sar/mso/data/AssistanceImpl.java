package org.redcross.sar.mso.data;

/**
 *
 */
public class AssistanceImpl extends AssignmentImpl implements IAssistanceIf
{
    public AssistanceImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public IAreaIf getPlannedIncidentScene()
    {
        return null; /*todo*/
    }

    public IAreaIf getReportedIncidentScene()
    {
        return null; /*todo*/
    }

    public void setPlannedIncidentScene()
    {
    }

    public void setReportedIncidentScene()
    {
    }
}
