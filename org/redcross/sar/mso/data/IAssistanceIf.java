package org.redcross.sar.mso.data;

/**
 *
 */
public interface IAssistanceIf extends IAssignmentIf
{
    public IAreaIf getPlannedIncidentScene();

    public IAreaIf getReportedIncidentScene();

    public void setPlannedIncidentScene();

    public void setReportedIncidentScene();

}
