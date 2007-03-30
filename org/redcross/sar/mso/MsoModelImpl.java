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
import org.redcross.sar.modelDriver.LoopbackCommitHandler;
import org.redcross.sar.modelDriver.ModelDriver;
import org.redcross.sar.mso.event.IMsoCommitListenerIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;
import org.redcross.sar.mso.event.MsoEventManagerImpl;

import java.util.Stack;


/**
 * Singleton class for accessing the MSO model
 */
public class MsoModelImpl implements IMsoModelIf
{
    private static MsoModelImpl ourInstance = new MsoModelImpl();
    private final MsoManagerImpl m_IMsoManager;
    private final MsoEventManagerImpl m_msoEventManager;
    private final CommitManager m_commitManager;
    private final IMsoCommitListenerIf m_commitListener;

    private final Stack<UpdateMode> m_updateModeStack = new Stack<UpdateMode>();
    private final ModelDriver m_modelDriver = new ModelDriver();

    /**
     * Get the singleton instance object of this class.
     * @return The singleton object
     */
    public static MsoModelImpl getInstance()
    {
        return ourInstance;
    }

    /**
     * Constructor.
     * Initializes other classes that are accessed via this object..
     */
    private MsoModelImpl()
    {
        m_msoEventManager = new MsoEventManagerImpl();
        m_IMsoManager = new MsoManagerImpl(m_msoEventManager);
        m_commitManager = new CommitManager(this);
        m_commitListener = new LoopbackCommitHandler(this);
        m_updateModeStack.push(UpdateMode.LOCAL_UPDATE_MODE);
    }

    /**
     * Get the MSO manager.
     */
    public IMsoManagerIf getMsoManager()
    {
        return m_IMsoManager;
    }

    /**
     * Get the Change manager (for event handling).
     */
    public IMsoEventManagerIf getEventManager()
    {
        return m_msoEventManager;
    }

    public IModelDriverIf getModelDriver()
    {
        return m_modelDriver;
    }

    public CommitManager getCommitManager()
    {
        return m_commitManager;
    }

    public IMsoCommitListenerIf getCommitListener()
    {
        return m_commitListener;
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
            System.out.println("Update mode stack grows too large, size:" + m_updateModeStack.size());
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


    public void commit()
    {
        m_commitManager.commit();
        setLoopbackUpdateMode();
        m_IMsoManager.commit();
        m_IMsoManager.commitLocal();
        restoreUpdateMode();
    }


    public void rollback()
    {
        setRemoteUpdateMode();
        m_commitManager.rollback();
        m_IMsoManager.rollback();
        restoreUpdateMode();
    }
}
