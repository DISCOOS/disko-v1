package org.redcross.sar.mso.data;

import org.redcross.sar.util.except.IllegalOperationException;

/**
 * Subject assistance assignment
 */
public class AssistanceImpl extends AssignmentImpl implements IAssistanceIf
{
    public AssistanceImpl(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber)
    {
        super(anObjectId, aNumber);
    }

    @Override
    protected void defineAttributes()
    {
        super.defineAttributes();
    }

    @Override
    protected void defineLists()
    {
        super.defineLists();
    }

    @Override
    protected void defineReferences()
    {
        super.defineReferences();
    }

    @Override
    protected AssignmentType getTypeBySubclass()
    {
        return AssignmentType.ASSISTANCE;
    }

    @Override
    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.addObjectReference(anObject, aReferenceName);
    }

    @Override
    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.removeObjectReference(anObject, aReferenceName);
    }

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public IAreaIf getPlannedIncidentScene()
    {
        return getPlannedArea();
    }

    public IAreaIf getReportedIncidentScene()
    {
        return getReportedArea();
    }

    public void setPlannedIncidentScene(IAreaIf anArea) throws IllegalOperationException
    {
        setPlannedArea(anArea);
    }

    public void setReportedIncidentScene(IAreaIf anArea) throws IllegalOperationException
    {
        setReportedArea(anArea);
    }

    public int getTypenr()
    {
        return 1;
    }

}
