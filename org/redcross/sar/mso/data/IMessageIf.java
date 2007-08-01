package org.redcross.sar.mso.data;

import org.redcross.sar.mso.IMsoModelIf;

import java.util.Calendar;
import java.util.Collection;

public interface IMessageIf extends ITimeItemIf, ISerialNumberedIf
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

    public void addConfirmedReceiver(ICommunicatorIf anICommunicatorIf);

    public IMsoListIf<ICommunicatorIf> getConfirmedReceivers();

    public IMsoModelIf.ModificationState getConfirmedReceiversState(ICommunicatorIf anICommunicatorIf);

    public Collection<ICommunicatorIf> getConfirmedReceiversItems();

    public void addMessageTask(ITaskIf anITaskIf);

    public ITaskListIf getMessageTasks();

    public IMsoModelIf.ModificationState getMessageTasksState(ITaskIf anITaskIf);

    public Collection<ITaskIf> getMessageTasksItems();

    public void addMessageLine(IMessageLineIf anIMessageLineIf);

    public IMessageLineListIf getMessageLines();

    public IMsoModelIf.ModificationState getMessageLinesState(IMessageLineIf anIMessageLineIf);

    public Collection<IMessageLineIf> getMessageLineItems();

    public void addUnconfirmedReceiver(ICommunicatorIf anICommunicatorIf);

    public IMsoListIf<ICommunicatorIf> getUnconfirmedReceivers();

    public IMsoModelIf.ModificationState getUnconfirmedReceiversState(ICommunicatorIf anICommunicatorIf);

    public Collection<ICommunicatorIf> getUnconfirmedReceiversItems();

    /*-------------------------------------------------------------------------------------------
    * Methods for references
    *-------------------------------------------------------------------------------------------*/

    public void setSender(ICommunicatorIf aCommunicator);

    public ICommunicatorIf getSender();

    public IMsoModelIf.ModificationState getSenderState();

    public IMsoReferenceIf<ICommunicatorIf> getSenderAttribute();

    /*-------------------------------------------------------------------------------------------
    * Other specified methods
    *-------------------------------------------------------------------------------------------*/

    public Calendar getOccuredTime();

    public void setOccuredTime();

    /*-------------------------------------------------------------------------------------------
    * Other methods
    *-------------------------------------------------------------------------------------------*/

    /**
     * Confirm receiver.
     * <p/>
     * Trasnfer receiver to list og confirmed receivers.
     *
     * @param anICommunicatorIf The receiver to transfer.
     * @return <code>true</code> if succeeded, false otherwise
     */
    public boolean confirmReceiver(ICommunicatorIf anICommunicatorIf);

    /**
     * Get list of unconfimred receivers.
     *
     * @return the list.
     */
    public MsoListImpl<ICommunicatorIf> getBroadcastUnconfirmed();

    /**
     * Get list of confimred receivers.
     *
     * @return the list.
     */
    public MsoListImpl<ICommunicatorIf> getBroadcastConfirmed();


    /**
     * Find a (optionally create a new) message line of given type.
     *
     * @param aType       Type of line to create.
     * @param makeNewLine If set, create a new line if non-existing.
     * @return Actualø line if found or created, otherwise null.
     */
    public IMessageLineIf findMessageLine(IMessageLineIf.MessageLineType aType, boolean makeNewLine);

    /**
     * Delete a line
     * @param aLine The line to delete
     * @return
     */
    public boolean deleteMessageLine(IMessageLineIf aLine);

    public boolean deleteMessageLine(int aLineNumber);

    public boolean deleteMessageLine(IMessageLineIf.MessageLineType aType);

    public IMessageLineIf createMessageLine(IMessageLineIf.MessageLineType aType);

    public String[] getLines();

}