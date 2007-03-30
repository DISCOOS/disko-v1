package org.redcross.sar.mso.event;

import org.redcross.sar.mso.committer.CommitWrapper;
import org.redcross.sar.mso.committer.ICommitWrapperIf;
import org.redcross.sar.mso.data.IAttributeIf;
import org.redcross.sar.mso.data.IMsoObjectIf;

/**
 * Interface for Update manager
 * <p/>
 * Manages listener (observer) sets and notifications to the listeners.
 */
public interface IMsoEventManagerIf
{
    /**
     * Add a listener in the Client Update Listeners queue.
     * @param aListener The listener
     */
    public void addClientUpdateListener(IMsoUpdateListenerIf aListener);

    /**
     * Remove a listener in the Client Update Listeners queue.
     * @param aListener The listener
     */
    public void removeClientUpdateListener(IMsoUpdateListenerIf aListener);

    /**
     * Notify a client update.
     * @param aSource The source object
     * @param anEventTypeMask Type of event (see {@link org.redcross.sar.mso.event.MsoEvent.EventType})
     */
    public void notifyClientUpdate(IMsoObjectIf aSource, int anEventTypeMask);

    /**
     * Add a listener in the Server Update Listeners} queue.
     * @param aListener The listener
     */
    public void addServerUpdateListener(IMsoUpdateListenerIf aListener);

    /**
     * Remove a listener in the Server Update Listeners queue.
     * @param aListener The listener
     */
    public void removeServerUpdateListener(IMsoUpdateListenerIf aListener);

    /**
     * Notify a server update.
     * @param aSource The source object
     * @param anEventTypeMask Type of event (see {@link org.redcross.sar.mso.event.MsoEvent.EventType})
     */
    public void notifyServerUpdate(IMsoObjectIf aSource, int anEventTypeMask);

    /**
     * Add a listener in the Commit Listeners queue.
     * @param aListener The listener
     */
    public void addCommitListener(IMsoCommitListenerIf aListener);

    /**
     * Remove a listener in the Commit Listeners queue.
     * @param aListener The listener
     */
    public void removeCommitListener(IMsoCommitListenerIf aListener);

    /**
     * Notify a commit.
     * @param aSource The {@link CommitWrapper} that contains the committable objects and relations
     */
    public void notifyCommit(ICommitWrapperIf aSource);

    /**
     * Add a listener in the Gis Update Listeners queue.
     * @param aListener The listener
     */
    public void addGisUpdateListener(IMsoGisListenerIf aListener);

    /**
     * Remove a listener in the Gis Update Listeners queue.
     * @param aListener The listener
     */
    public void removeGisUpdateListener(IMsoGisListenerIf aListener);

    /**
     * Notify a gis update.
     * @param aSource The source object
     */
    public void notifyGisUpdate(IAttributeIf aSource);

    public void addItemListener(IMsoItemListenerIf aListener);

    public void removeItemUpdateListener(IMsoItemListenerIf aListener);

    public void notifyItemUpdate(IMsoObjectIf aSource,int anEventTypeMask);


}
