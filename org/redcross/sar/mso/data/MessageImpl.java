package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.MsoModelImpl;
import org.redcross.sar.util.Internationalization;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.except.MsoRuntimeException;
import org.redcross.sar.util.mso.Selector;

import java.util.Calendar;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
 * Communication message
 */
public class MessageImpl extends AbstractTimeItem implements IMessageIf
{
    private static final String CONFIRMED_RECEIVERS_NAME = "ConfirmedReceivers";
    private static final String UNCONFIRMED_RECEIVERS_NAME = "UnconfirmedReceivers";
    private final AttributeImpl.MsoBoolean m_broadcast = new AttributeImpl.MsoBoolean(this, "Broadcast");
    private final AttributeImpl.MsoCalendar m_created = new AttributeImpl.MsoCalendar(this, "Created");
    private final AttributeImpl.MsoInteger m_number = new AttributeImpl.MsoInteger(this, "Number",true);
    private final AttributeImpl.MsoEnum<MessageStatus> m_status = new AttributeImpl.MsoEnum<MessageStatus>(this, "Status", MessageStatus.UNCONFIRMED);

    private final MsoListImpl<ICommunicatorIf> m_confirmedReceivers = new MsoListImpl<ICommunicatorIf>(this, CONFIRMED_RECEIVERS_NAME, false);
    private final TaskListImpl m_messageTasks = new TaskListImpl(this, "MessageTasks", false);
    private final MsoListImpl<ICommunicatorIf> m_unconfirmedReceivers = new MsoListImpl<ICommunicatorIf>(this, UNCONFIRMED_RECEIVERS_NAME, false);

    private final MessageLineListImpl m_messageLines = new MessageLineListImpl(this, "MessageLines", false);

    private final MsoReferenceImpl<ICommunicatorIf> m_sender = new MsoReferenceImpl<ICommunicatorIf>(this, "Sender", true);

    public static String getText(String aKey)
    {
        return Internationalization.getFullBundleText(Internationalization.getBundle(IMessageIf.class), aKey);
    }

    public String getStatusText()
    {
        return m_status.getInternationalName();
    }

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
        addList(m_messageLines);
    }

    @Override
    protected void defineReferences()
    {
        super.defineReferences();
        addReference(m_sender);
    }

    @Override
    public boolean addObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        if (anObject instanceof ITaskIf)
        {
            m_messageTasks.add((ITaskIf) anObject);
            return true;
        }
        if (anObject instanceof IMessageLineIf)
        {
            m_messageLines.add((IMessageLineIf) anObject);
            return true;
        }
        if (anObject instanceof ICommunicatorIf)
        {
            if (CONFIRMED_RECEIVERS_NAME.equals(aReferenceName))
            {
                m_confirmedReceivers.add((ICommunicatorIf) anObject);
                return true;
            }
            if (UNCONFIRMED_RECEIVERS_NAME.equals(aReferenceName))
            {
                m_unconfirmedReceivers.add((ICommunicatorIf) anObject);
                return true;
            }
        }
        return super.addObjectReference(anObject, aReferenceName);
    }

    public boolean removeObjectReference(IMsoObjectIf anObject, String aReferenceName)
    {
        if (anObject instanceof ITaskIf)
        {
            return m_messageTasks.removeReference((ITaskIf) anObject);
        }
        if (anObject instanceof IMessageLineIf)
        {
            return m_messageLines.removeReference((IMessageLineIf) anObject);
        }
        if (anObject instanceof ICommunicatorIf)
        {
            if (CONFIRMED_RECEIVERS_NAME.equals(aReferenceName))
            {
                return m_confirmedReceivers.removeReference((ICommunicatorIf) anObject);
            }
            if (UNCONFIRMED_RECEIVERS_NAME.equals(aReferenceName))
            {
                return m_unconfirmedReceivers.removeReference((ICommunicatorIf) anObject);
            }
        }
        return super.removeObjectReference(anObject, aReferenceName);
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
    	// Clear receiver lists when changing broadcast state
    	m_unconfirmedReceivers.deleteAll();
    	m_confirmedReceivers.deleteAll();

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


    public void addMessageLine(IMessageLineIf anIMessageLineIf)
    {
        m_messageLines.add(anIMessageLineIf);
    }

    public IMessageLineListIf getMessageLines()
    {
        return m_messageLines;

    }

    public IMsoModelIf.ModificationState getMessageLinesState(IMessageLineIf anIMessageLineIf)
    {
        return m_messageLines.getState(anIMessageLineIf);
    }

    public Collection<IMessageLineIf> getMessageLineItems()
    {
        return m_messageLines.getItems();
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

    public void setSender(ICommunicatorIf aCommunicator)
    {
        m_sender.setReference(aCommunicator);
    }

    public ICommunicatorIf getSender()
    {
        return m_sender.getReference();
    }

    public IMsoModelIf.ModificationState getSenderState()
    {
        return m_sender.getState();
    }

    public IMsoReferenceIf<ICommunicatorIf> getSenderAttribute()
    {
        return m_sender;
    }

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public Calendar getOccuredTime()
    {
        return getTimeStamp();
    }

    public void setOccuredTime(Calendar aCalendar)
    {
        setTimeStamp(aCalendar);
    }

    public IMsoModelIf.ModificationState getOccuredTimeState()
    {
        return getTimeStampState();
    }

    /*-------------------------------------------------------------------------------------------
    * Other methods
    *-------------------------------------------------------------------------------------------*/

//    public String toString()
//    {
//        return "Message " + getNumber() + " " + super.toString();
//    }

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

    public ICommunicatorIf getSingleReceiver()
    {
        return m_confirmedReceivers.getItem();
    }

    public void setSingleReceiver(ICommunicatorIf communicator)
    {
        setBroadcast(false);
        m_unconfirmedReceivers.deleteAll();
        m_confirmedReceivers.deleteAll();
        m_confirmedReceivers.add(communicator);
    }

    private int getNextLineNumber()
    {
        int retVal = 0;
        for (IMessageLineIf ml : m_messageLines.getItems())
        {
            if (ml.getLineNumber() > retVal)
            {
                retVal = ml.getLineNumber();
            }
        }
        return retVal + 1;
    }

    private IMessageLineIf getMessageLine(int aNumber)
    {
        for (IMessageLineIf ml : m_messageLines.getItems())
        {
            if (ml.getLineNumber() == aNumber)
            {
                return ml;
            }
        }
        return null;
    }

    private IMessageLineIf getMessageLine(IMessageLineIf.MessageLineType aType)
    {
        for (IMessageLineIf ml : m_messageLines.getItems())
        {
            if (ml.getLineType() == aType)
            {
                return ml;
            }
        }
        return null;
    }

    public boolean deleteMessageLine(IMessageLineIf aLine)
    {
        if (aLine == null)
        {
            return false;
        }
        int deletedLineNumber = aLine.getLineNumber();
        if (!m_messageLines.removeReference(aLine))
        {
            return false;
        }
        for (IMessageLineIf ml : m_messageLines.getItems())
        {
            if (ml.getLineNumber() > deletedLineNumber)
            {
                ml.setLineNumber(ml.getLineNumber() - 1);
            }
        }
        return true;
    }

    public boolean deleteMessageLine(int aLineNumber)
    {
        return deleteMessageLine(getMessageLine(aLineNumber));
    }

    public boolean deleteMessageLine(IMessageLineIf.MessageLineType aType)
    {
        return deleteMessageLine(getMessageLine(aType));
    }

    public IMessageLineIf createMessageLine(IMessageLineIf.MessageLineType aType)
    {
        IMessageLineIf retVal = MsoModelImpl.getInstance().getMsoManager().getCmdPost().getMessageLines().createMessageLine();
        retVal.setLineType(aType);
        retVal.setLineNumber(getNextLineNumber());
        m_messageLines.add(retVal);
        return retVal;
    }

    private static EnumSet<IMessageLineIf.MessageLineType> assignmentLines = EnumSet.of(MessageLineImpl.MessageLineType.ASSIGNED,
            MessageLineImpl.MessageLineType.STARTED, MessageLineImpl.MessageLineType.COMPLETE);

    public IMessageLineIf findMessageLine(IMessageLineIf.MessageLineType aType, boolean makeNewLine)
    {
        return findMessageLine(aType, null, makeNewLine);
    }

    public IMessageLineIf findMessageLine(IMessageLineIf.MessageLineType aType, IAssignmentIf anAssignment, boolean makeNewLine)
    {
        for (IMessageLineIf ml : m_messageLines.getItems())
        {
            if (ml.getLineType() == aType)
            {
                if (assignmentLines.contains(ml.getLineType()))
                {
                    if (anAssignment == ml.getLineAssignment())
                    {
                        return ml;
                    }
                } else
                {
                    return ml;
                }
            }
        }
        if (makeNewLine)
        {
            IMessageLineIf retVal = createMessageLine(aType);
            retVal.setLineAssignment(anAssignment);
            return retVal;
        }
        return null;
    }

    public List<IMessageLineIf> getTypeMessageLines(IMessageLineIf.MessageLineType aType)
    {
        m_lineTypeSelector.setLineType(aType);
        return m_messageLines.selectItems(m_lineTypeSelector, IMessageLineIf.LINE_NUMBER_COMPARATOR);
    }


    public String[] getLines()
    {
        List<IMessageLineIf> lines = m_messageLines.selectItems(m_messageLineSelector, IMessageLineIf.LINE_NUMBER_COMPARATOR);
        int numLines = lines.size();
        String[] lineArray = new String[numLines];
        for (int i = 0; i < numLines; i++)
        {
            lineArray[i] = lines.get(i).toString();
        }
        return lineArray;
    }

    private final Selector<IMessageLineIf> m_messageLineSelector = new Selector<IMessageLineIf>()
    {
        public boolean select(IMessageLineIf aMessageLine)
        {
            return true;
        }
    };

    private final typeSelector m_lineTypeSelector = new typeSelector();

    private class typeSelector implements Selector<IMessageLineIf>
    {
       private IMessageLineIf.MessageLineType m_testLineType;

        void setLineType(IMessageLineIf.MessageLineType aLineType)
        {
            m_testLineType = aLineType;
        }

        public boolean select(IMessageLineIf anObject)
        {
            return anObject.getLineType() == m_testLineType;
        }
    };
}