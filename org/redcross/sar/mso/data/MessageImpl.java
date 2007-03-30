package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.util.except.MsoCastException;
import org.redcross.sar.util.except.MsoException;

import java.util.Calendar;

public class MessageImpl extends AbstractTimeItem implements IMessageIf
{
    private final TaskListImpl m_taskList = new TaskListImpl(this, "MessageTasks", false);
    private final MsoListImpl<ICommunicatorIf> m_broadcastNotAccepted = new MsoListImpl<ICommunicatorIf>(this, "BroadcastReceivers", false);
    private final MsoListImpl<ICommunicatorIf> m_broadcastAcccepted = new MsoListImpl<ICommunicatorIf>(this, "BroadcastAccepted", false);

    protected final AttributeImpl.MsoBoolean m_isBroadcast = new AttributeImpl.MsoBoolean(this, "isBroadcast");


    public MessageImpl(IMsoObjectIf.IObjectIdIf anObjectId)
    {
        super(anObjectId);
    }

    public MessageImpl(IMsoObjectIf.IObjectIdIf anObjectId, Calendar aCalendar)
    {
        super(anObjectId, aCalendar);
    }

    protected void defineAttributes()
    {
        super.defineAttributes();
        addAttribute(m_isBroadcast);
    }

    protected void defineLists()
    {
        super.defineLists();
        addList(m_taskList);
        addList(m_broadcastNotAccepted);
        addList(m_broadcastAcccepted);
    }

    protected void defineReferences()
    {
        super.defineReferences();
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

    public boolean addBroadcastNotAccepted(ICommunicatorIf aReceiver)
    {
        try
        {
            m_broadcastNotAccepted.add(aReceiver);
            return true;
        }
        catch (MsoException e)
        {
            return false;
        }
    }

    public boolean acceptBroadcastReceiver(ICommunicatorIf aReceiver)
    {
        if (!m_broadcastNotAccepted.removeReference(aReceiver))
        {
            return false;
        }
        try
        {
            m_broadcastAcccepted.add(aReceiver);
            return true;
        }
        catch (MsoException e)
        {
            return false;
        }
    }

    public MsoListImpl<ICommunicatorIf> getBroadcastReceivers()
    {
        return m_broadcastNotAccepted;
    }

    public MsoListImpl<ICommunicatorIf> getBroadcastAccepted()
    {
        return m_broadcastAcccepted;
    }

    public boolean isBroadcast()
    {
        return m_isBroadcast.booleanValue();
    }


    public IMsoManagerIf.MsoClassCode getMsoClassCode()
    {
        return IMsoManagerIf.MsoClassCode.CLASSCODE_MESSAGE;
    }
}