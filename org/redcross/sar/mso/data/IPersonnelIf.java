package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

import java.util.Calendar;

/**
 *
 */
public interface IPersonnelIf extends IPersonIf
{
    public enum PersonnelStatus
    {
        IDLE,
        ON_ROUTE,
        ARRIVED,
        RELEASED
    }

    public enum PersonnelType
    {
        VOLUNTEER,
        POLICE,
        MILITARY,
        CIVIL_DEFENSE,
        HEALTH_WORKER,
        FIREFIGHTER,
        OTHER
    }

    public void setArrived(Calendar anArrived);

    public Calendar getArrived();

    public IMsoModelIf.ModificationState getArrivedState();

    public IAttributeIf.IMsoCalendarIf getArrivedAttribute();

    public void setCallOut(Calendar aCallOut);

    public Calendar getCallOut();

    public IMsoModelIf.ModificationState getCallOutState();

    public IAttributeIf.IMsoCalendarIf getCallOutAttribute();

    public void setEstimatedArrival(Calendar anEstimatedArrival);

    public Calendar getEstimatedArrival();

    public IMsoModelIf.ModificationState getEstimatedArrivalState();

    public IAttributeIf.IMsoCalendarIf getEstimatedArrivalAttribute();

    public void setReleased(Calendar aReleased);

    public Calendar getReleased();

    public IMsoModelIf.ModificationState getReleasedState();

    public IAttributeIf.IMsoCalendarIf getReleasedAttribute();

    public void setRemarks(String aRemarks);

    public String getRemarks();

    public IMsoModelIf.ModificationState getRemarksState();

    public IAttributeIf.IMsoStringIf getRemarksAttribute();

    public void setStatus(PersonnelStatus aStatus);

    public void setStatus(String aStatus);

    public PersonnelStatus getStatus();

    public IMsoModelIf.ModificationState getStatusState();

    public IAttributeIf.IMsoEnumIf<PersonnelStatus> getStatusAttribute();

    public void setType(PersonnelType aType);

    public void setType(String aType);

    public PersonnelType getType();

    public IMsoModelIf.ModificationState getTypeState();

    public IAttributeIf.IMsoEnumIf<PersonnelType> getTypeAttribute();

    public void setDataSourceName(IDataSourceIf aDataSource);

    public IDataSourceIf getDataSourceName();

    public IMsoModelIf.ModificationState getDataSourceNameState();

    public IMsoReferenceIf<IDataSourceIf> getDataSourceNameAttribute();
}
