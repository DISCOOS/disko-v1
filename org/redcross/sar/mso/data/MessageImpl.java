package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.except.MsoRuntimeException;

import java.util.Calendar;
import java.util.Collection;

/**
 * Communication message
 */
public class MessageImpl extends AbstractTimeItem implements IMessageIf
{
    private final static String CONFIRMED_RECEIVERS_NAME = "ConfirmedReceivers";
    private final static String UNCONFIRMED_RECEIVERS_NAME = "UnconfirmedReceivers";
    private final AttributeImpl.MsoBoolean m_broadcast = new AttributeImpl.MsoBoolean(this, "Broadcast");
    private final AttributeImpl.MsoCalendar m_created = new AttributeImpl.MsoCalendar(this, "Created");
    private final AttributeImpl.MsoInteger m_number = new AttributeImpl.MsoInteger(this, "Number");
    private final AttributeImpl.MsoEnum<MessageStatus> m_status = new AttributeImpl.MsoEnum<MessageStatus>(this, "Status", MessageStatus.UNCONFIRMED);

    private final MsoListImpl<ICommunicatorIf> m_confirmedReceivers = new MsoListImpl<ICommunicatorIf>(this, CONFIRMED_RECEIVERS_NAME, false);
    private final TaskListImpl m_messageTasks = new TaskListImpl(this, "MessageTasks", false);
    private final MsoListImpl<ICommunicatorIf> m_unconfirmedReceivers = new MsoListImpl<ICommunicatorIf>(this, UNCONFIRMED_RECEIVERS_NAME, false);

    private final MsoReferenceImpl<ICmdPostIf> m_sender = new MsoReferenceImpl<ICmdPostIf>(this, "Sender", true);

    public MessageImpl(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber)
    {
        super(anObjectId);
        setNumber(aNumber);
    }

    public MessageImpl(IMsoObjectIf.IObjectIdIf anObjectId, int aNumber, Calendar aCalendar)
    {
        super(anObjectId, aCalendar);
        setNumber(aNumber);
        setCreated(Calendar.getInstance());
    }

    @Override
    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_broadcast);
        addAttribute(m_created);
        addAttribute(m_number);
        addAttribute(m_status);
    }

    @Override
    protected void defineLists()
    {
        super.defineLists();
        addList(m_confirmedReceivers);
        addList(m_messageTasks);
        addList(m_unconfirmedReceivers);
    }

    @Override
    protected void defineReferences()
    {
        super.defineReferences();
        addReference(m_sender);
    }

    public void addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        if (anObject instanceof ITaskIf)
        {
            m_messageTasks.add((ITaskIf) anObject);
            return;
        }
        if (anObject instanceof ICommunicatorIf)
        {
            if (CONFIRMED_RECEIVERS_NAME.equals(aReferenceName))
            {
                m_confirmedReceivers.add((ICommunicatorIf) anObject);
                return;
            }
            if (UNCONFIRMED_RECEIVERS_NAME.equals(aReferenceName))
            {
                m_unconfirmedReceivers.add((ICommunicatorIf) anObject);
                return;
            }
        }
        super.addObjectReference(anObject, aReferenceName);
    }

    public void removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        if (anObject instanceof ITaskIf)
        {
            m_messageTasks.removeReference((ITaskIf) anObject);
            return;
        }
        if (anObject instanceof ICommunicatorIf)
        {
            if (CONFIRMED_RECEIVERS_NAME.equals(aReferenceName))
            {
                m_confirmedReceivers.removeReference((ICommunicatorIf) anObject);
                return;
            }
            if (UNCONFIRMED_RECEIVERS_NAME.equals(aReferenceName))
            {
                m_unconfirmedReceivers.removeReference((ICommunicatorIf) anObject);
                return;
            }
        }
        super.addObjectReference(anObject, aReferenceName);
    }

    public static MessageImpl implementationOf(IMessageIf anInterface) throws MsoCastException
    {
        try
        {
            return (MessageImpl) anInterface;
        }
        catch (ClassCastException e)
        {
            throw new MsoCastException("Illegal cast to MessageImpl");
        }
    }

    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setStatus(MessageStatus aStatus)
    {
        m_status.setValue(aStatus);
    }

    public void setStatus(String aStatus)
    {
        m_status.setValue(aStatus);
    }

    public MessageStatus getStatus()
    {
        return m_status.getValue();
    }

    public IMsoModelIf.ModificationState getStatusState()
    {
        return m_status.getState();
    }

    public IAttributeIf.IMsoEnumIf<MessageStatus> getStatusAttribute()
    {
        return m_status;
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setBroadcast(boolean aBroadcast)
    {
        m_broadcast.setValue(aBroadcast);
    }

    public boolean isBroadcast()
    {
        return m_broadcast.booleanValue();
    }

    public IMsoModelIf.ModificationState getBroadcastState()
    {
        return m_broadcast.getState();
    }

    public IAttributeIf.IMsoBooleanIf getBroadcastAttribute()
    {
        return m_broadcast;
    }

    public void setCreated(Calendar aCreated)
    {
        m_created.setValue(aCreated);
    }

    public Calendar getCreated()
    {
        return m_created.getCalendar();
    }

    public IMsoModelIf.ModificationState getCreatedState()
    {
        return m_created.getState();
    }

    public IAttributeIf.IMsoCalendarIf getCreatedAttribute()
    {
        return m_created;
    }

    // From ISerialNumberedIf
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

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addConfirmedReceiver(ICommunicatorIf anICommunicatorIf)
    {
        m_confirmedReceivers.add(anICommunicatorIf);
    }

    public IMsoListIf<ICommunicatorIf> getConfirmedReceivers()
    {
        return m_confirmedReceivers;
    }

    public IMsoModelIf.ModificationState getConfirmedReceiversState(ICommunicatorIf anICommunicatorIf)
    {
        return m_confirmedReceivers.getState(anICommunicatorIf);
    }

    public Collection<ICommunicatorIf> getConfirmedReceiversItems()
    {
        return m_confirmedReceivers.getItems();
    }

    public void addMessageTask(ITaskIf anITaskIf)
    {
        m_messageTasks.add(anITaskIf);
    }

    public ITaskListIf getMessageTasks()
    {
        return m_messageTasks;
    }

    public IMsoModelIf.ModificationState getMessageTasksState(ITaskIf anITaskIf)
    {
        return m_messageTasks.getState(anITaskIf);
    }

    public Collection<ITaskIf> getMessageTasksItems()
    {
        return m_messageTasks.getItems();
    }

    public void addUnconfirmedReceiver(ICommunicatorIf anICommunicatorIf)
    {
        m_unconfirmedReceivers.add(anICommunicatorIf);
    }

    public IMsoListIf<ICommunicatorIf> getUnconfirmedReceivers()
    {
        return m_unconfirmedReceivers;
    }

    public IMsoModelIf.ModificationState getUnconfirmedReceiversState(ICommunicatorIf anICommunicatorIf)
    {
        return m_unconfirmedReceivers.getState(anICommunicatorIf);
    }

    public Collection<ICommunicatorIf> getUnconfirmedReceiversItems()
    {
        return m_unconfirmedReceivers.getItems();
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setSender(ICmdPostIf aCommunicator)
    {
        m_sender.setReference(aCommunicator);
    }

    public ICmdPostIf getSender()
    {
        return m_sender.getReference();
    }

    public IMsoModelIf.ModificationState getSenderState()
    {
        return m_sender.getState();
    }

    public IMsoReferenceIf<ICmdPostIf> getSenderAttribute()
    {
        return m_sender;
    }

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public Calendar getOccuredTime()
    {
        return null; /*todo*/
    }

    public void setOccuredTime()
    {
    }

    /*-------------------------------------------------------------------------------------------
    * Other methods
    *-------------------------------------------------------------------------------------------*/


    public boolean addBroadcastNotAccepted(ICommunicatorIf aReceiver)
    {
        try
        {
            m_unconfirmedReceivers.add(aReceiver);
            return true;
        }
        catch (MsoRuntimeException e)
        {
            return false;
        }
    }

    public boolean confirmReceiver(ICommunicatorIf anICommunicatorIf)
    {
        if (!m_unconfirmedReceivers.removeReference(anICommunicatorIf))
        {
            return false;
        }
        try
        {
            m_confirmedReceivers.add(anICommunicatorIf);
            return true;
        }
        catch (MsoRuntimeException e)
        {
            return false;
        }
    }

    public MsoListImpl<ICommunicatorIf> getBroadcastUnconfirmed()
    {
        return m_unconfirmedReceivers;
    }

    public MsoListImpl<ICommunicatorIf> getBroadcastConfirmed()
    {
        return m_confirmedReceivers;
    }
}