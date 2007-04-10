package org.redcross.sar.mso.data;

/**
 * Subject assistance assignment
 */
public class AssistanceImpl extends AssignmentImpl implements IAssistanceIf
{
    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public AssistanceImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public IAreaIf getPlannedIncidentScene()
    {
        return getPlannedArea();
    }

    public IAreaIf getReportedIncidentScene()
    {
        return getReportedArea();
    }

    public void setPlannedIncidentScene(IAreaIf anArea)
    {
        setPlannedArea(anArea);
    }

    public void setReportedIncidentScene(IAreaIf anArea)
    {
        setReportedArea(anArea);
    }
}
