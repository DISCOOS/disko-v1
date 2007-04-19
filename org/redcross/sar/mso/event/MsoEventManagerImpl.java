package org.redcross.sar.mso.event;

import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.data.IAttributeIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.Collection;

/**
 * Class for managing MsoUpdateEvents.
 * <p/>
 * Manages listener (observer) sets and notifications to the listeners.
 */
public class MsoEventManagerImpl implements IMsoEventManagerIf
{
    private final Collection<IMsoUpdateListenerIf> m_clientUpdateListeners = new Vector<IMsoUpdateListenerIf>();
    private final Collection<IMsoUpdateListenerIf> m_serverUpdateListeners = new Vector<IMsoUpdateListenerIf>();
    private final Collection<IMsoCommitListenerIf> m_commitListeners = new Vector<IMsoCommitListenerIf>();
    private final Collection<IMsoGisListenerIf> m_gisListeners = new Vector<IMsoGisListenerIf>();
    private final Collection<IMsoDerivedUpdateListenerIf> m_derivedUpdateListeners = new Vector<IMsoDerivedUpdateListenerIf>();

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

    private void fireUpdate(Collection<IMsoUpdateListenerIf> theListeners, IMsoObjectIf aSource, int anEventTypeMask)
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

    private void fireCommit(Collection<IMsoCommitListenerIf> theListeners, ICommitWrapperIf aSource, int anEventTypeMask)
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

    public void addDerivedUpdateListener(IMsoDerivedUpdateListenerIf aListener)
    {
        m_derivedUpdateListeners.add(aListener);
    }

    public void removeDerivedUpdateListener(IMsoDerivedUpdateListenerIf aListener)
    {
        m_derivedUpdateListeners.remove(aListener);
    }

    public void notifyDerivedUpdate(IMsoObjectIf aSource,int anEventTypeMask)
    {
        if (m_derivedUpdateListeners.size() == 0)
        {
            return;
        }
        MsoEvent.DerivedUpdate event = new MsoEvent.DerivedUpdate(aSource, anEventTypeMask);
        for (IMsoDerivedUpdateListenerIf listener : m_derivedUpdateListeners)
        {
            listener.handleMsoDerivedUpdateEvent(event);
        }
    }


}
