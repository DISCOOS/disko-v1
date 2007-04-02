package org.redcross.sar.mso.data;

/**
 *
 */
public interface IAssistanceIf extends IAssignmentIf
{
    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public IAreaIf getPlannedIncidentScene();

    public IAreaIf getReportedIncidentScene();

    public void setPlannedIncidentScene(IAreaIf anArea);

    public void setReportedIncidentScene(IAreaIf anArea);

}
