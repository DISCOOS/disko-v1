package org.redcross.sar.mso.event;

import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.data.IAttributeIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

import java.util.HashSet;
import java.util.Set;

/**
 * Class for managing MsoUpdateEvents.
 * <p/>
 * Manages listener (observer) sets and notifications to the listeners.
 */
public class MsoEventManagerImpl implements IMsoEventManagerIf
{
    private final Set<IMsoUpdateListenerIf> m_clientUpdateListeners = new HashSet<IMsoUpdateListenerIf>();
    private final Set<IMsoUpdateListenerIf> m_serverUpdateListeners = new HashSet<IMsoUpdateListenerIf>();
    private final Set<IMsoCommitListenerIf> m_commitListeners = new HashSet<IMsoCommitListenerIf>();
    private final Set<IMsoGisListenerIf> m_gisListeners = new HashSet<IMsoGisListenerIf>();
    private final Set<IMsoItemListenerIf> m_itemListeners = new HashSet<IMsoItemListenerIf>();

    /**
     * Add a listener in the {@link #m_clientUpdateListeners} queue.
     */
    public void addClientUpdateListener(IMsoUpdateListenerIf aListener)
    {
        m_clientUpdateListeners.add(aListener);
    }

    /**
     * Remove a listener in the {@link #m_clientUpdateListeners} queue.
     */
    public void removeClientUpdateListener(IMsoUpdateListenerIf aListener)
    {
        m_clientUpdateListeners.remove(aListener);
    }

    public void notifyClientUpdate(IMsoObjectIf aSource, int anEventTypeMask)
    {
        fireUpdate(m_clientUpdateListeners, aSource, anEventTypeMask);
    }

    public void addServerUpdateListener(IMsoUpdateListenerIf aListener)
    {
        m_serverUpdateListeners.add(aListener);
    }

    public void removeServerUpdateListener(IMsoUpdateListenerIf aListener)
    {
        m_serverUpdateListeners.remove(aListener);
    }

    public void notifyServerUpdate(IMsoObjectIf aSource, int anEventTypeMask)
    {
        fireUpdate(m_serverUpdateListeners, aSource, anEventTypeMask);
    }

    /**
     * Add a listener in the {@link #m_commitListeners} queue.
     */
    public void addCommitListener(IMsoCommitListenerIf aListener)
    {
        m_commitListeners.add(aListener);
    }

    /**
     * Remove a listener in the {@link #m_commitListeners} queue.
     */
    public void removeCommitListener(IMsoCommitListenerIf aListener)
    {
        m_commitListeners.remove(aListener);
    }

    public void notifyCommit(ICommitWrapperIf aSource)
    {
        fireCommit(m_commitListeners, aSource, MsoEvent.EventType.COMMIT_EVENT.maskValue());
    }

    private void fireUpdate(Set<IMsoUpdateListenerIf> theListeners, IMsoObjectIf aSource, int anEventTypeMask)
    {
        if (theListeners.size() == 0 || anEventTypeMask == 0)
        {
            return;
        }
        MsoEvent.Update event = new MsoEvent.Update(aSource, anEventTypeMask);
        for (IMsoUpdateListenerIf listener : theListeners)
        {
            if (listener.hasInterestIn(aSource))
            {
                listener.handleMsoUpdateEvent(event);
            }
        }
    }

    private void fireCommit(Set<IMsoCommitListenerIf> theListeners, ICommitWrapperIf aSource, int anEventTypeMask)
    {
        if (theListeners.size() == 0 || anEventTypeMask == 0)
        {
            return;
        }
        MsoEvent.Commit event = new MsoEvent.Commit(aSource, anEventTypeMask);
        for (IMsoCommitListenerIf listener : theListeners)
        {
            listener.handleMsoCommitEvent(event);
        }
    }

    public void addGisUpdateListener(IMsoGisListenerIf aListener)
    {
        m_gisListeners.add(aListener);
    }

    public void removeGisUpdateListener(IMsoGisListenerIf aListener)
    {
        m_gisListeners.remove(aListener);
    }

    public void notifyGisUpdate(IAttributeIf aSource)
    {
        if (m_gisListeners.size() == 0)
        {
            return;
        }
        MsoEvent.Gis event = new MsoEvent.Gis(aSource);
        for (IMsoGisListenerIf listener : m_gisListeners)
        {
            listener.handleMsoGisEvent(event);
        }
    }

    public void addItemListener(IMsoItemListenerIf aListener)
    {
        m_itemListeners.add(aListener);
    }

    public void removeItemUpdateListener(IMsoItemListenerIf aListener)
    {
        m_itemListeners.remove(aListener);
    }

    public void notifyItemUpdate(IMsoObjectIf aSource,int anEventTypeMask)
    {
        if (m_itemListeners.size() == 0)
        {
            return;
        }
        MsoEvent.Item event = new MsoEvent.Item(aSource, anEventTypeMask);
        for (IMsoItemListenerIf listener : m_itemListeners)
        {
            listener.handleMsoItemEvent(event);
        }
    }


}
