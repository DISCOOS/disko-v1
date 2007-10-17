/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 14.des.2006
 * To change this template use File | Settings | File Templates.
 */
/**
 *
 */
package org.redcross.sar.mso;

import org.redcross.sar.modelDriver.IModelDriverIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;


/**
 * Interface for singleton class for accessing the MSO model
 */
public interface IMsoModelIf
{
    /**
     * Update modes, to be used when updating local data in order to inform the GUI how data have been updated.
     */
    public enum UpdateMode
    {
        LOCAL_UPDATE_MODE,
        REMOTE_UPDATE_MODE,
        LOOPBACK_UPDATE_MODE
    }

    /**
     * Modification state, tells the modification state of each attribute or relation.
     */
    enum ModificationState
    {
        STATE_UNDEFINED,
        STATE_SERVER,
//        STATE_SERVER_MODIFIED,
        STATE_CONFLICTING,
        STATE_LOCAL
    }


    /**
     * Get the {@link IMsoManagerIf MSO manager}
     *
     * @return The MSO manager
     */
    public IMsoManagerIf getMsoManager();

    /**
     * Get the {@link org.redcross.sar.mso.event.IMsoEventManagerIf event manager}
     *
     * @return The event manager.
     */
    public IMsoEventManagerIf getEventManager();

    /**
     * Get the {@link org.redcross.sar.modelDriver.IModelDriverIf Model driver}
     *
     * @return The Model driver.
     */
    public IModelDriverIf getModelDriver();

    /**
     * Set update mode to {@link UpdateMode#LOCAL_UPDATE_MODE LOCAL_UPDATE_MODE}.
     */
    public void setLocalUpdateMode();

    /**
     * Set update mode to {@link UpdateMode#REMOTE_UPDATE_MODE REMOTE_UPDATE_MODE}.
     */
    public void setRemoteUpdateMode();

    /**
     * Set update mode to {@link UpdateMode#LOOPBACK_UPDATE_MODE LOOPBACK_UPDATE_MODE}.
     */
    public void setLoopbackUpdateMode();

    /**
     * Restore previous update mode.
     */
    public void restoreUpdateMode();

    /**
     * Get current update mode.
     *
     * @return The update mode
     */
    public UpdateMode getUpdateMode();

    /**
     * Perform commit.
     * <p/>
     * Local modifications since previous commit or rollback are sent to server.
     */
    public void commit();

    /**
     * Perform rollback.
     * <p/>
     * Local modifications since previous commit or rollback are ignored.
     */
    public void rollback();

    public boolean hasUncommitedChanges();

    public void suspendClientUpdate();

    public void resumeClientUpdate();
}
