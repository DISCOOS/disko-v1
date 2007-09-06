package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.IllegalOperationException;

import java.util.Calendar;
import java.util.Comparator;

public interface ITaskIf extends ITimeItemIf, ISerialNumberedIf, IEnumStatusHolder<ITaskIf.TaskStatus>, IEnumPriorityHolder<ITaskIf.TaskPriority>
{

    public enum TaskStatus
    {
        UNPROCESSED,
        STARTED,
        POSTPONED,
        FINISHED,
        DELETED
    }

    public enum TaskPriority
    {
        LOW,
        NORMAL,
        HIGH
    }

    public enum TaskType
    {
        TRANSPORT,
        RESOURCE,
        INTELLIGENCE,
        GENERAL
    }


    public void setDescription(String aDescription);

    public String getDescription();

    public IMsoModelIf.ModificationState getDescriptionState();

    public IAttributeIf.IMsoStringIf getDescriptionAttribute();

    public void setNumber(int aNumber);

    public int getNumber();

    public IMsoModelIf.ModificationState getNumberState();

    public IAttributeIf.IMsoIntegerIf getNumberAttribute();

    public void setProgress(int aProgress);

    public int getProgress();

    public IMsoModelIf.ModificationState getProgressState();

    public IAttributeIf.IMsoIntegerIf getProgressAttribute();

    public void setResponsibleRole(String aResponsibleRole);

    public String getResponsibleRole();

    public IMsoModelIf.ModificationState getResponsibleRoleState();

    public IAttributeIf.IMsoStringIf getResponsibleRoleAttribute();

    public void setTaskText(String aTaskText);

    public String getTaskText();

    public IMsoModelIf.ModificationState getTaskTextState();

    public IAttributeIf.IMsoStringIf getTaskTextAttribute();

    public void setAlert(Calendar aAlert);

    public Calendar getAlert();

    public IMsoModelIf.ModificationState getAlertState();

    public IAttributeIf.IMsoCalendarIf getAlertAttribute();

    public void setCreated(Calendar aCreated);

    public Calendar getCreated();

    public IMsoModelIf.ModificationState getCreatedState();

    public IAttributeIf.IMsoCalendarIf getCreatedAttribute();

    public void setCreatingWorkProcess(String aCreatingWorkProcess);

    public String getCreatingWorkProcess();

    public IMsoModelIf.ModificationState getCreatingWorkProcessState();

    public IAttributeIf.IMsoStringIf getCreatingWorkProcessAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setCreatedEvent(IEventIf aEvent);

    public IEventIf getCreatedEvent();

    public IMsoModelIf.ModificationState getCreatedEventState();

    public IMsoReferenceIf<IEventIf> getCreatedEventAttribute();

    public void setDependentObject(IMsoObjectIf anAbstractMsoObject);

    public IMsoObjectIf getDependentObject();

    public IMsoModelIf.ModificationState getDependentObjectState();

    public IMsoReferenceIf<IMsoObjectIf> getDependentObjectAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for enums
    *-------------------------------------------------------------------------------------------*/

    public void setPriority(TaskPriority aPriority);

    public void setPriority(String aPriority) throws IllegalOperationException;

    public TaskPriority getPriority();

    public IMsoModelIf.ModificationState getPriorityState();

    public IAttributeIf.IMsoEnumIf<TaskPriority> getPriorityAttribute();

    public String getPriorityText();

    public void setStatus(TaskStatus aStatus) throws IllegalOperationException;

    public void setStatus(String aStatus) throws IllegalOperationException;

    public TaskStatus getStatus();

    public IMsoModelIf.ModificationState getStatusState();

    public IAttributeIf.IMsoEnumIf<TaskStatus> getStatusAttribute();

    public String getStatusText();

    public void setType(TaskType aType);

    public void setType(String aType);

    public TaskType getType();

    public IMsoModelIf.ModificationState getTypeState();

    public IAttributeIf.IMsoEnumIf<TaskType> getTypeAttribute();

    public String getTypeText();

    public void setSourceClass(IMsoManagerIf.MsoClassCode aSourceClass);

    public void setSourceClass(String aSourceClass);

    public IMsoManagerIf.MsoClassCode getSourceClass();

    public IMsoModelIf.ModificationState getSourceClassState();

    public IAttributeIf.IMsoEnumIf<IMsoManagerIf.MsoClassCode> getSourceClassAttribute();

    public String getSourceClassText();

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public Calendar getDueTime();

    public void setDueTime(Calendar aCalendar);

    public IMsoModelIf.ModificationState getDueTimeState();

    public final static Comparator<ITaskIf> PRIORITY_COMPARATOR = new Comparator<ITaskIf>()
    {
        public int compare(ITaskIf o1, ITaskIf o2)
        {
            return o1.comparePriorityTo(o2);
        }
    };
}