package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.Internationalization;
import org.redcross.sar.util.except.IllegalOperationException;
import org.redcross.sar.util.except.MsoCastException;

import java.util.Calendar;
import java.util.ResourceBundle;

public class TaskImpl extends AbstractTimeItem implements ITaskIf
{
    private final AttributeImpl.MsoString m_description = new AttributeImpl.MsoString(this, "Description");
    private final AttributeImpl.MsoInteger m_number = new AttributeImpl.MsoInteger(this, "Number");
    private final AttributeImpl.MsoInteger m_progress = new AttributeImpl.MsoInteger(this, "Progress");
    private final AttributeImpl.MsoString m_responsibleRole = new AttributeImpl.MsoString(this, "ResponsibleRole");
    private final AttributeImpl.MsoString m_taskText = new AttributeImpl.MsoString(this, "TaskText");
    private final AttributeImpl.MsoEnum<TaskStatus> m_status = new AttributeImpl.MsoEnum<TaskStatus>(this, "Status", TaskStatus.IDLE);

    private final MsoReferenceImpl<IEventIf> m_createdEvent = new MsoReferenceImpl<IEventIf>(this, "CreatedEvent", true);

    private final AttributeImpl.MsoEnum<TaskPriority> m_priority = new AttributeImpl.MsoEnum<TaskPriority>(this, "Priority", TaskPriority.LOW);

    private static final ResourceBundle bundle = ResourceBundle.getBundle("org.redcross.sar.mso.data.properties.Task");

    public static String getText(String aKey)
    {
        return Internationalization.getFullBundleText(bundle, aKey);
    }

    public static String getEnumText(Enum anEnum)
    {
        return getText(anEnum.getClass().getSimpleName() + "." + anEnum.name() + ".text");
    }

    public TaskImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public TaskImpl(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar)
    {
        super(anObjectId, aCalendar);
    }

    @Override
    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_description);
        addAttribute(m_number);
        addAttribute(m_progress);
        addAttribute(m_responsibleRole);
        addAttribute(m_taskText);
        addAttribute(m_priority);
        addAttribute(m_status);
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
        addReference(m_createdEvent);
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

    public static TaskImpl implementationOf(ITaskIf anInterface) throws MsoCastException
    {
        try
        {
            return (TaskImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to TaskImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_TASK;
    }

    public void setDescription(String aDescription)
    {
        m_description.setValue(aDescription);
    }

    public String getDescription()
    {
        return m_description.getString();
    }

    public IMsoModelIf.ModificationState getDescriptionState()
    {
        return m_description.getState();
    }

    public IAttributeIf.IMsoStringIf getDescriptionAttribute()
    {
        return m_description;
    }

    public void setNumber(int aNumber)
    {
        m_number.setValue(aNumber);
    }

    public int getNumber()
    {
        return m_number.intValue();
    }

    public IMsoModelIf.ModificationState getNumberState()
    {
        return m_number.getState();
    }

    public IAttributeIf.IMsoIntegerIf getNumberAttribute()
    {
        return m_number;
    }

    public void setProgress(int aProgress)
    {
        m_progress.setValue(aProgress);
    }

    public int getProgress()
    {
        return m_progress.intValue();
    }

    public IMsoModelIf.ModificationState getProgressState()
    {
        return m_progress.getState();
    }

    public IAttributeIf.IMsoIntegerIf getProgressAttribute()
    {
        return m_progress;
    }

    public void setResponsibleRole(String aResponsibleRole)
    {
        m_responsibleRole.setValue(aResponsibleRole);
    }

    public String getResponsibleRole()
    {
        return m_responsibleRole.getString();
    }

    public IMsoModelIf.ModificationState getResponsibleRoleState()
    {
        return m_responsibleRole.getState();
    }

    public IAttributeIf.IMsoStringIf getResponsibleRoleAttribute()
    {
        return m_responsibleRole;
    }

    public void setTaskText(String aTaskText)
    {
        m_taskText.setValue(aTaskText);
    }

    public String getTaskText()
    {
        return m_taskText.getString();
    }

    public IMsoModelIf.ModificationState getTaskTextState()
    {
        return m_taskText.getState();
    }

    public IAttributeIf.IMsoStringIf getTaskTextAttribute()
    {
        return m_taskText;
    }

    public int comparePriorities(IEnumPriorityHolder anObject)
    {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public void setPriority(TaskPriority aPriority)
    {
        m_priority.setValue(aPriority);
    }

    public void setPriority(String aPriority)
    {
        m_priority.setValue(aPriority);
    }

    public TaskPriority getPriority()
    {
        return m_priority.getValue();
    }

    public IMsoModelIf.ModificationState getPriorityState()
    {
        return m_priority.getState();
    }

    public IAttributeIf.IMsoEnumIf<TaskPriority> getPriorityAttribute()
    {
        return m_priority;
    }

    public void setStatus(TaskStatus aStatus) throws IllegalOperationException
    {
        m_status.setValue(aStatus);
    }

    public void setStatus(String aStatus)  throws IllegalOperationException
    {
        m_status.setValue(aStatus);
    }

    public TaskStatus getStatus()
    {
        return m_status.getValue();
    }

    public IMsoModelIf.ModificationState getStatusState()
    {
        return m_status.getState();
    }

    public IAttributeIf.IMsoEnumIf<TaskStatus> getStatusAttribute()
    {
        return m_status;
    }

    public void setCreatedEvent(IEventIf aEvent)
    {
        m_createdEvent.setReference(aEvent);
    }

    public IEventIf getCreatedEvent()
    {
        return m_createdEvent.getReference();
    }

    public IMsoModelIf.ModificationState getCreatedEventState()
    {
        return m_createdEvent.getState();
    }

    public IMsoReferenceIf<IEventIf> getCreatedEventAttribute()
    {
        return m_createdEvent;
    }

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public Calendar getDueTime()
    {
        return getTimeStamp();
    }

    public void setDueTime(Calendar aCalendar)
    {
        setTimeStamp(aCalendar);
    }


    public IMsoModelIf.ModificationState getDueTimeState()
    {
        return getTimeStampState();
    }
}