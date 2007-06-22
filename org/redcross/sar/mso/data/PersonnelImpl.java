package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;

import java.util.Calendar;

public class PersonnelImpl extends AbstractPerson implements IPersonnelIf
{

    private final AttributeImpl.MsoCalendar m_arrived = new AttributeImpl.MsoCalendar(this,"Arrived");
    private final AttributeImpl.MsoCalendar m_callOut = new AttributeImpl.MsoCalendar(this,"CallOut");
    private final AttributeImpl.MsoString m_dataSourceID = new AttributeImpl.MsoString(this,"DataSourceID");
    private final AttributeImpl.MsoCalendar m_estimatedArrival = new AttributeImpl.MsoCalendar(this,"EstimatedArrival");
    private final AttributeImpl.MsoCalendar m_released = new AttributeImpl.MsoCalendar(this,"Released");
    private final AttributeImpl.MsoString m_remarks = new AttributeImpl.MsoString(this,"Remarks");

    private final AttributeImpl.MsoEnum<PersonnelStatus> m_status = new AttributeImpl.MsoEnum<PersonnelStatus>(this,"Status", PersonnelStatus.IDLE);
    private final AttributeImpl.MsoEnum<PersonnelType> m_type = new AttributeImpl.MsoEnum<PersonnelType>(this,"Type", PersonnelType.VOLUNTEER);

    private final MsoReferenceImpl<IDataSourceIf> m_dataSourceName = new MsoReferenceImpl<IDataSourceIf>(this,"DataSourceName",true);

    public PersonnelImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    @Override
    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_arrived);
        addAttribute(m_callOut);
        addAttribute(m_dataSourceID);
        addAttribute(m_estimatedArrival);
        addAttribute(m_released);
        addAttribute(m_remarks);
        addAttribute(m_status);
        addAttribute(m_type);
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
        addReference(m_dataSourceName);

    }

    @Override
    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.addObjectReference(anObject,aReferenceName);
    }

    @Override
    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        super.removeObjectReference(anObject,aReferenceName);
    }

    public static PersonnelImpl implementationOf(IPersonnelIf anInterface) throws MsoCastException
    {
        try
        {
            return (PersonnelImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to PersonnelImpl");
        }
    }

    public void setArrived(Calendar anArrived){m_arrived.setValue(anArrived);} public Calendar getArrived(){return m_arrived.getCalendar();} public IMsoModelIf.ModificationState getArrivedState(){return m_arrived.getState();}public IAttributeIf.IMsoCalendarIf getArrivedAttribute(){return m_arrived;}
    public void setCallOut(Calendar aCallOut){m_callOut.setValue(aCallOut);} public Calendar getCallOut(){return m_callOut.getCalendar();} public IMsoModelIf.ModificationState getCallOutState(){return m_callOut.getState();}public IAttributeIf.IMsoCalendarIf getCallOutAttribute(){return m_callOut;}
    public void setDataSourceID(String aDataSourceID){m_dataSourceID.setValue(aDataSourceID);} public String getDataSourceID(){return m_dataSourceID.getString();} public IMsoModelIf.ModificationState getDataSourceIDState(){return m_dataSourceID.getState();}public IAttributeIf.IMsoStringIf getDataSourceIDAttribute(){return m_dataSourceID;}
    public void setEstimatedArrival(Calendar anEstimatedArrival){m_estimatedArrival.setValue(anEstimatedArrival);} public Calendar getEstimatedArrival(){return m_estimatedArrival.getCalendar();} public IMsoModelIf.ModificationState getEstimatedArrivalState(){return m_estimatedArrival.getState();}public IAttributeIf.IMsoCalendarIf getEstimatedArrivalAttribute(){return m_estimatedArrival;}
    public void setReleased(Calendar aReleased){m_released.setValue(aReleased);} public Calendar getReleased(){return m_released.getCalendar();} public IMsoModelIf.ModificationState getReleasedState(){return m_released.getState();}public IAttributeIf.IMsoCalendarIf getReleasedAttribute(){return m_released;}
    public void setRemarks(String aRemarks){m_remarks.setValue(aRemarks);} public String getRemarks(){return m_remarks.getString();} public IMsoModelIf.ModificationState getRemarksState(){return m_remarks.getState();}public IAttributeIf.IMsoStringIf getRemarksAttribute(){return m_remarks;}
    public void setStatus(PersonnelStatus aStatus){m_status.setValue(aStatus);} public void setStatus(String aStatus){m_status.setValue(aStatus);} public PersonnelStatus getStatus(){return m_status.getValue();} public IMsoModelIf.ModificationState getStatusState(){return m_status.getState();}public IAttributeIf.IMsoEnumIf<PersonnelStatus> getStatusAttribute(){return m_status;}
    public void setType(PersonnelType aType){m_type.setValue(aType);} public void setType(String aType){m_type.setValue(aType);} public PersonnelType getType(){return m_type.getValue();} public IMsoModelIf.ModificationState getTypeState(){return m_type.getState();}public IAttributeIf.IMsoEnumIf<PersonnelType> getTypeAttribute(){return m_type;}

    public void setDataSourceName(IDataSourceIf aDataSource){m_dataSourceName.setReference(aDataSource);} public IDataSourceIf getDataSourceName(){return m_dataSourceName.getReference();} public IMsoModelIf.ModificationState getDataSourceNameState(){return m_dataSourceName.getState();}public IMsoReferenceIf<IDataSourceIf > getDataSourceNameAttribute(){return m_dataSourceName;}

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_PERSONNEL;
    }

}