package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Calendar;
import java.util.Collection;

public interface ICmdPostIf extends IMsoObjectIf
{
    public enum CmdPostStatus
    {
        IDLE,
        OPERATING,
        PAUSED,
        RELEASED
    }

    public void setEstablished(Calendar anEstablished);

    public Calendar getEstablished();

    public IMsoModelIf.ModificationState getEstablishedState();

    public IAttributeIf.IMsoCalendarIf getEstablishedAttribute();

    public void setCallSign(String aCallSign);

    public String getCallSign();

    public IMsoModelIf.ModificationState getCallSignState();

    public IAttributeIf.IMsoStringIf getCallSignAttribute();

    public void setReleased(Calendar aReleased);

    public Calendar getReleased();

    public IMsoModelIf.ModificationState getReleasedState();

    public IAttributeIf.IMsoCalendarIf getReleasedAttribute();

    public void setShift(int aShift);

    public int getShift();

    public IMsoModelIf.ModificationState getShiftState();

    public IAttributeIf.IMsoIntegerIf getShiftAttribute();

    public void setTelephone1(String aTelephone1);

    public String getTelephone1();

    public IMsoModelIf.ModificationState getTelephone1State();

    public IAttributeIf.IMsoStringIf getTelephone1Attribute();

    public void setTelephone2(String aTelephone2);

    public String getTelephone2();

    public IMsoModelIf.ModificationState getTelephone2State();

    public IAttributeIf.IMsoStringIf getTelephone2Attribute();

    public void setTelephone3(String aTelephone3);

    public String getTelephone3();

    public IMsoModelIf.ModificationState getTelephone3State();

    public IAttributeIf.IMsoStringIf getTelephone3Attribute();

    public void setState(CmdPostStatus aState);

    public void setState(String aState);

    public CmdPostStatus getState();

    public IMsoModelIf.ModificationState getStateState();

    public IAttributeIf.IMsoEnumIf<CmdPostStatus> getStateAttribute();

    public void addAreaList(IAreaIf anIAreaIf) throws DuplicateIdException;

    public IAreaListIf getAreaList();

    public IMsoModelIf.ModificationState getAreaListState(IAreaIf anIAreaIf);

    public Collection<IAreaIf> getAreaListItems();

    public void addAssignmentList(IAssignmentIf anIAssignmentIf) throws DuplicateIdException;

    public IAssignmentListIf getAssignmentList();

    public IMsoModelIf.ModificationState getAssignmentListState(IAssignmentIf anIAssignmentIf);

    public Collection<IAssignmentIf> getAssignmentListItems();

    public void addAttendanceList(IPersonnelIf anIPersonnelIf) throws DuplicateIdException;

    public IPersonnelListIf getAttendanceList();

    public IMsoModelIf.ModificationState getAttendanceListState(IPersonnelIf anIPersonnelIf);

    public Collection<IPersonnelIf> getAttendanceListItems();

    public void addBriefingList(IBriefingIf anIBriefingIf) throws DuplicateIdException;

    public IBriefingListIf getBriefingList();

    public IMsoModelIf.ModificationState getBriefingListState(IBriefingIf anIBriefingIf);

    public Collection<IBriefingIf> getBriefingListItems();

    public void addCalloutList(ICalloutIf anICalloutIf) throws DuplicateIdException;

    public ICalloutListIf getCalloutList();

    public IMsoModelIf.ModificationState getCalloutListState(ICalloutIf anICalloutIf);

    public Collection<ICalloutIf> getCalloutListItems();

    public void addCheckpointList(ICheckpointIf anICheckpointIf) throws DuplicateIdException;

    public ICheckpointListIf getCheckpointList();

    public IMsoModelIf.ModificationState getCheckpointListState(ICheckpointIf anICheckpointIf);

    public Collection<ICheckpointIf> getCheckpointListItems();

    public void addDataSourceList(IDataSourceIf anIDataSourceIf) throws DuplicateIdException;

    public IDataSourceListIf getDataSourceList();

    public IMsoModelIf.ModificationState getDataSourceListState(IDataSourceIf anIDataSourceIf);

    public Collection<IDataSourceIf> getDataSourceListItems();

    public void addEnvironmentList(IEnvironmentIf anIEnvironmentIf) throws DuplicateIdException;

    public IEnvironmentListIf getEnvironmentList();

    public IMsoModelIf.ModificationState getEnvironmentListState(IEnvironmentIf anIEnvironmentIf);

    public Collection<IEnvironmentIf> getEnvironmentListItems();

    public void addEquipmentList(IEquipmentIf anIEquipmentIf) throws DuplicateIdException;

    public IEquipmentListIf getEquipmentList();

    public IMsoModelIf.ModificationState getEquipmentListState(IEquipmentIf anIEquipmentIf);

    public Collection<IEquipmentIf> getEquipmentListItems();

    public void addEventLog(IEventIf anIEventIf) throws DuplicateIdException;

    public IEventLogIf getEventLog();

    public IMsoModelIf.ModificationState getEventLogState(IEventIf anIEventIf);

    public Collection<IEventIf> getEventLogItems();

    public void addForecastList(IForecastIf anIForecastIf) throws DuplicateIdException;

    public IForecastListIf getForecastList();

    public IMsoModelIf.ModificationState getForecastListState(IForecastIf anIForecastIf);

    public Collection<IForecastIf> getForecastListItems();

    public void addHypothesisList(IHypothesisIf anIHypothesisIf) throws DuplicateIdException;

    public IHypothesisListIf getHypothesisList();

    public IMsoModelIf.ModificationState getHypothesisListState(IHypothesisIf anIHypothesisIf);

    public Collection<IHypothesisIf> getHypothesisListItems();

    public void addIntelligenceList(IIntelligenceIf anIIntelligenceIf) throws DuplicateIdException;

    public IIntelligenceListIf getIntelligenceList();

    public IMsoModelIf.ModificationState getIntelligenceListState(IIntelligenceIf anIIntelligenceIf);

    public Collection<IIntelligenceIf> getIntelligenceListItems();

    public void addMessageLog(IMessageIf anIMessageIf) throws DuplicateIdException;

    public IMessageLogIf getMessageLog();

    public IMsoModelIf.ModificationState getMessageLogState(IMessageIf anIMessageIf);

    public Collection<IMessageIf> getMessageLogItems();

    public void addOperationAreaList(IOperationAreaIf anIOperationAreaIf) throws DuplicateIdException;

    public IOperationAreaListIf getOperationAreaList();

    public IMsoModelIf.ModificationState getOperationAreaListState(IOperationAreaIf anIOperationAreaIf);

    public Collection<IOperationAreaIf> getOperationAreaListItems();

    public void addPOIList(IPOIIf anIPOIIf) throws DuplicateIdException;

    public IPOIListIf getPOIList();

    public IMsoModelIf.ModificationState getPOIListState(IPOIIf anIPOIIf);

    public Collection<IPOIIf> getPOIListItems();

    public void addRouteList(IRouteIf anIRouteIf) throws DuplicateIdException;

    public IRouteListIf getRouteList();

    public IMsoModelIf.ModificationState getRouteListState(IRouteIf anIRouteIf);

    public Collection<IRouteIf> getRouteListItems();

    public void addSearchAreaList(ISearchAreaIf anISearchAreaIf) throws DuplicateIdException;

    public ISearchAreaListIf getSearchAreaList();

    public IMsoModelIf.ModificationState getSearchAreaListState(ISearchAreaIf anISearchAreaIf);

    public Collection<ISearchAreaIf> getSearchAreaListItems();

    public void addSketchList(ISketchIf anISketchIf) throws DuplicateIdException;

    public ISketchListIf getSketchList();

    public IMsoModelIf.ModificationState getSketchListState(ISketchIf anISketchIf);

    public Collection<ISketchIf> getSketchListItems();

    public void addSubjectList(ISubjectIf anISubjectIf) throws DuplicateIdException;

    public ISubjectListIf getSubjectList();

    public IMsoModelIf.ModificationState getSubjectListState(ISubjectIf anISubjectIf);

    public Collection<ISubjectIf> getSubjectListItems();

    public void addTaskList(ITaskIf anITaskIf) throws DuplicateIdException;

    public ITaskListIf getTaskList();

    public IMsoModelIf.ModificationState getTaskListState(ITaskIf anITaskIf);

    public Collection<ITaskIf> getTaskListItems();

    public void addTrackList(ITrackIf anITrackIf) throws DuplicateIdException;

    public ITrackListIf getTrackList();

    public IMsoModelIf.ModificationState getTrackListState(ITrackIf anITrackIf);

    public Collection<ITrackIf> getTrackListItems();

    public void addUnitList(IUnitIf anIUnitIf) throws DuplicateIdException;

    public IUnitListIf getUnitList();

    public IMsoModelIf.ModificationState getUnitListState(IUnitIf anIUnitIf);

    public Collection<IUnitIf> getUnitListItems();

    public AbstractDerivedList<ICommunicatorIf> getCcommunicatorList();

    public ITimeLineIf getTimeLine();
}