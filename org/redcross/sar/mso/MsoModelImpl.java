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

import no.cmr.tools.Log;
import org.redcross.sar.app.Utils;
import org.redcross.sar.modelDriver.IModelDriverIf;
import org.redcross.sar.modelDriver.ModelDriver;
import org.redcross.sar.modelDriver.SarModelDriver;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.MsoEventManagerImpl;
import org.redcross.sar.util.except.CommitException;

import javax.swing.*;
import java.util.Stack;


/**
 * Singleton class for accessing the MSO model
 */
public class MsoModelImpl implements IMsoModelIf
{
    private static MsoModelImpl ourInstance = new MsoModelImpl();
    private final MsoManagerImpl m_msoManager;
    private final MsoEventManagerImpl m_msoEventManager;
    private final CommitManager m_commitManager;

    private final Stack<UpdateMode> m_updateModeStack = new Stack<UpdateMode>();
    private final IModelDriverIf m_modelDriver;

    private boolean m_suspendClientUpdate = false;


    /**
     * Get the singleton instance object of this class.
     *
     * @return The singleton object
     */
    public static MsoModelImpl getInstance()
    {
        return ourInstance;
    }

    /**
     * Constructor.
     * <p/>
     * Initializes other classes that are accessed via this object..
     */
    private MsoModelImpl()
    {
        m_msoEventManager = new MsoEventManagerImpl();
        m_msoManager = new MsoManagerImpl(m_msoEventManager);
        m_commitManager = new CommitManager(this);
        m_updateModeStack.push(UpdateMode.LOCAL_UPDATE_MODE);
        m_modelDriver = System.getProperty("integrate.sara", "false").equalsIgnoreCase("true") ||
                Utils.getProperties().getProperty("integrate.sara", "false").equalsIgnoreCase("true") ?
                new SarModelDriver() : new ModelDriver();
//        m_modelDriver =  new ModelDriver(); // when testing;
    }

    public IMsoManagerIf getMsoManager()
    {
        return m_msoManager;
    }

    public IMsoEventManagerIf getEventManager()
    {
        return m_msoEventManager;
    }

    public IModelDriverIf getModelDriver()
    {
        return m_modelDriver;
    }

    /**
     * Set update mode to {@link IMsoModelIf.UpdateMode#LOCAL_UPDATE_MODE LOCAL_UPDATE_MODE}.
     */
    public void setLocalUpdateMode()
    {
        setUpdateMode(UpdateMode.LOCAL_UPDATE_MODE);
    }

    /**
     * Set update mode to {@link IMsoModelIf.UpdateMode#REMOTE_UPDATE_MODE REMOTE_UPDATE_MODE}.
     */
    public void setRemoteUpdateMode()
    {
        setUpdateMode(UpdateMode.REMOTE_UPDATE_MODE);
    }

    /**
     * Set update mode to {@link IMsoModelIf.UpdateMode#LOOPBACK_UPDATE_MODE LOOPBACK_UPDATE_MODE}.
     */
    public void setLoopbackUpdateMode()
    {
        setUpdateMode(UpdateMode.LOOPBACK_UPDATE_MODE);
    }

    protected void setUpdateMode(UpdateMode aMode)
    {
        m_updateModeStack.push(aMode);
        if (m_updateModeStack.size() > 10)
        {
            Log.error("Update mode stack grows too large, size:" + m_updateModeStack.size());
        }
    }

    /**
     * Restore previous update mode.
     */
    public void restoreUpdateMode()
    {
        if (m_updateModeStack.size() > 1)
        {
            m_updateModeStack.pop();
        }
    }

    /**
     * Get current update mode.
     */
    public UpdateMode getUpdateMode()
    {
        return m_updateModeStack.peek();
    }

    public boolean hasUncommitedChanges()
    {
        return m_commitManager.hasUncommitedChanges();
    }

    public void suspendClientUpdate()
    {
        m_suspendClientUpdate = true;
    }

    public void resumeClientUpdate()
    {
        m_suspendClientUpdate = false;
        m_msoManager.resumeClientUpdate();
    }

    public boolean updateSuspended()
    {
        return m_suspendClientUpdate;
    }

    public void commit()
    {
        try
        {
            m_commitManager.commit();
        }
        catch (CommitException e)
        {
            JOptionPane.showMessageDialog(null,e.getMessage(),null,JOptionPane.ERROR_MESSAGE);
            rollback();
            return;
        }
        suspendClientUpdate();
        setLoopbackUpdateMode();
        m_msoManager.postProcessCommit();
        restoreUpdateMode();
        resumeClientUpdate();
    }


    public void rollback()
    {
        suspendClientUpdate();
        setRemoteUpdateMode();
        m_commitManager.rollback();
        m_msoManager.rollback();
        restoreUpdateMode();
        resumeClientUpdate();
    }
}
