package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.util.except.DuplicateIdException;

import java.util.Calendar;
import java.util.Collection;

public interface IMessageIf extends ITimeItemIf
{
    public enum MessageStatus
    {
        UNCONFIRMED,
        CONFIRMED,
        POSTPONED
    }

    /*-------------------------------------------------------------------------------------------
    * Methods for ENUM attributes
    *-------------------------------------------------------------------------------------------*/

    public void setStatus(MessageStatus aStatus);

    public void setStatus(String aStatus);

    public MessageStatus getStatus();

    public IMsoModelIf.ModificationState getStatusState();

    public IAttributeIf.IMsoEnumIf<MessageStatus> getStatusAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for attributes
    *-------------------------------------------------------------------------------------------*/

    public void setBroadcast(boolean aBroadcast);

    public boolean isBroadcast();

    public IMsoModelIf.ModificationState getBroadcastState();

    public IAttributeIf.IMsoBooleanIf getBroadcastAttribute();

    public void setCreated(Calendar aCreated);

    public Calendar getCreated();

    public IMsoModelIf.ModificationState getCreatedState();

    public IAttributeIf.IMsoCalendarIf getCreatedAttribute();

    /*-------------------------------------------------------------------------------------------
    * Methods for lists
    *-------------------------------------------------------------------------------------------*/

    public void addConfirmedReceiver(ICommunicatorIf anICommunicatorIf) throws DuplicateIdException;

    public IMsoListIf<ICommunicatorIf> getConfirmedReceivers();

    public IMsoModelIf.ModificationState getConfirmedReceiversState(ICommunicatorIf anICommunicatorIf);

    public Collection<ICommunicatorIf> getConfirmedReceiversItems();

    public void addMessageTask(ITaskIf anITaskIf) throws DuplicateIdException;

    public ITaskListIf getMessageTasks();

    public IMsoModelIf.ModificationState getMessageTasksState(ITaskIf anITaskIf);

    public Collection<ITaskIf> getMessageTasksItems();

    public void addUnconfirmedReceiver(ICommunicatorIf anICommunicatorIf) throws DuplicateIdException;

    public IMsoListIf<ICommunicatorIf> getUnconfirmedReceivers();

    public IMsoModelIf.ModificationState getUnconfirmedReceiversState(ICommunicatorIf anICommunicatorIf);

    public Collection<ICommunicatorIf> getUnconfirmedReceiversItems();

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setSender(ICmdPostIf aCommunicator);

    public ICmdPostIf getSender();

    public IMsoModelIf.ModificationState getSenderState();

    public IMsoReferenceIf<ICmdPostIf> getSenderAttribute();

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public Calendar getOccuredTime();

    public void setOccuredTime();

    /*-------------------------------------------------------------------------------------------
    * Other methods
    *-------------------------------------------------------------------------------------------*/

    public boolean confirmReceiver(ICommunicatorIf anICommunicatorIf);
}