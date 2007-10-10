package org.redcross.sar.wp;

import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DiskoWpEvent;
import org.redcross.sar.event.IDiskoWpEventListener;
import org.redcross.sar.event.ITickEventListenerIf;
import org.redcross.sar.event.TickEvent;
import org.redcross.sar.gui.MainPanel;
import org.redcross.sar.gui.SubMenuPanel;
import org.redcross.sar.map.IDiskoMap;
import org.redcross.sar.map.IDiskoMapManager;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.data.ICmdPostIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.Timer;

/**
 * This abstract class is a base class that has a default implementation of the
 * IDiskoWpModule interface and the IDiskoMapEventListener interface.
 *
 * @author geira
 */
public abstract class AbstractDiskoWpModule implements IDiskoWpModule
{
    private IDiskoRole role = null;
    private IDiskoMap map = null;
    private boolean hasSubMenu = false;
    private Properties properties = null;
    private ArrayList<IDiskoWpEventListener> listeners = null;
    private DiskoWpEvent diskoWpEvent = null;
    protected String callingWp = null;
    protected boolean isEditing = false;
    protected ResourceBundle wpBundle;

    private final ArrayList<ITickEventListenerIf> tickListeners;
    private final TickEvent tickEvent;
    private long tickTime = 0;

    /**
     * @param role
     */
    public AbstractDiskoWpModule(IDiskoRole role)
    {
        this.role = role;
        listeners = new ArrayList<IDiskoWpEventListener>();
        diskoWpEvent = new DiskoWpEvent(this);
        tickListeners = new ArrayList<ITickEventListenerIf>();
        tickEvent = new TickEvent(this);
        initTickTimer();
    }

    public IDiskoRole getDiskoRole()
    {
        return role;
    }


    /* (non-Javadoc)
      * @see org.redcross.sar.wp.IDiskoWpModule#getMap()
      */
    public IDiskoMap getMap()
    {
        if (map == null)
        {
            try
            {
                IDiskoMapManager manager = role.getApplication().getDiskoMapManager();
                map = manager.getMapInstance();
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return map;
    }

    public IDiskoApplication getApplication()
    {
        return role.getApplication();
    }

    /* (non-Javadoc)
      * @see org.redcross.sar.wp.IDiskoWpModule#getName()
      */
    public String getName()
    {
        return null;
    }

    public void setCallingWp(String name)
    {
        callingWp = name;
    }

    public String getCallingWp()
    {
        return callingWp;
    }

    /* (non-Javadoc)
      * @see org.redcross.sar.wp.IDiskoWpModule#hasMap()
      */
    public boolean hasMap()
    {
        return map != null;
    }

    /* (non-Javadoc)
      * @see org.redcross.sar.wp.IDiskoWpModule#hasSubMenu()
      */
    public boolean hasSubMenu()
    {
        return hasSubMenu;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public String getProperty(String key)
    {
        String value = properties != null ?
                properties.getProperty(key) : null;
        if (value == null)
        {
            //try application properties
            value = getApplication().getProperty(key);
        }
        return value;
    }

    public void loadProperties(String fileName)
    {
        try
        {
            properties = Utils.loadProperties("properties");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public void addDiskoWpEventListener(IDiskoWpEventListener listener)
    {
        if (listeners.indexOf(listener) == -1)
        {
            listeners.add(listener);
        }
    }

    public void removeDiskoWpEventListener(IDiskoWpEventListener listener)
    {
        listeners.remove(listener);
    }

    protected void fireTaskStarted()
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            listeners.get(i).taskStarted(diskoWpEvent);
        }
    }

    protected void fireTaskCanceled()
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            listeners.get(i).taskCanceled(diskoWpEvent);
        }
    }

    protected void fireTaskFinished()
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            listeners.get(i).taskFinished(diskoWpEvent);
        }
    }

    /* (non-Javadoc)
    * @see org.redcross.sar.wp.IDiskoWpModule#activated()
    */
    public void activated()
    {
        setFrameText(null);
    }

    /* (non-Javadoc)
    * @see org.redcross.sar.wp.IDiskoWpModule#deactivated()
    */
    public void deactivated()
    {
        getApplication().getFrame().setTitle(null);
    }

    public boolean isEditing()
    {
        return isEditing;
    }

    public void setFrameText(String text)
    {
        String s = "DISKO " + getDiskoRole().getTitle() + " " + getName();
        if (text != null)
        {
            s += " " + text;
        }
        getApplication().getFrame().setTitle(s);
    }

    public void showWarning(String msg)
    {
        final String displayMsg = getText(msg); // Internationalization
        Runnable r = new Runnable()
        {
            public void run()
            {
                JOptionPane.showMessageDialog(getApplication().getFrame(),
                        displayMsg, null, JOptionPane.WARNING_MESSAGE);
            }
        };
        SwingUtilities.invokeLater(r);
    }

    protected void layoutComponent(JComponent comp)
    {
        String id = role.getName() + getName();
        MainPanel mainPanel = getApplication().getUIFactory().getMainPanel();
        mainPanel.addComponent(comp, id);
    }

    protected void layoutButton(AbstractButton button)
    {
        layoutButton(button, true);
    }

    protected void layoutButton(AbstractButton button, boolean addToGroup)
    {
        String id = role.getName() + getName();
        SubMenuPanel subMenuPanel = getApplication().getUIFactory().getSubMenuPanel();
        subMenuPanel.addItem(button, id, addToGroup);
        hasSubMenu = true;
    }

    protected JButton createLargeButton(String aText, java.awt.event.ActionListener aListener)
    {
        return createButton(aText, getApplication().getUIFactory().getLargeButtonSize(), aListener);
    }

    protected JButton createSmallButton(String aText, java.awt.event.ActionListener aListener)
    {
        return createButton(aText, getApplication().getUIFactory().getSmallButtonSize(), aListener);
    }

    protected JButton createButton(String aText, Dimension aSize, java.awt.event.ActionListener aListener)
    {
        JButton createdButton = new JButton();
        createdButton.setText(aText);
        createdButton.setPreferredSize(aSize);
        if (aListener != null)
        {
            createdButton.addActionListener(aListener);
        }
        return createdButton;
    }

    public IMsoModelIf getMsoModel()
    {
        return getApplication().getMsoModel();
    }

    public IMsoManagerIf getMsoManager()
    {
        return getMsoModel().getMsoManager();
    }

    public IMsoEventManagerIf getMmsoEventManager()
    {
        return getMsoModel().getEventManager();
    }
    
    public ICmdPostIf getCmdPost()
    {
    	return getMsoManager().getCmdPost();
    }

    public String getText(String aKey)
    {
        if (wpBundle == null)
        {
            return aKey;
        }
        try
        {
            String retVal = wpBundle.getString(aKey);
            return retVal != null ? retVal : aKey;
        }
        catch (Exception e)
        {
            return aKey;
        }
    }

    protected void assignWpBundle(String bundleName)
    {
        try
        {
            wpBundle = ResourceBundle.getBundle(bundleName);
        }
        catch (java.util.MissingResourceException e)
        {
            System.out.println("Classname " + e.getClassName());
            System.out.println("Key " + e.getKey());
            e.printStackTrace();
        }
    }

    private static final int TIMER_DELAY = 1000; // 1 second
    private final WpTicker ticker = new WpTicker();

    /**
     * Creates a timer for generating {@link TickEvent} objects periodically.
     */
    private void initTickTimer()
    {
        Timer timer = new Timer(true);
        timer.schedule(new TimerTask()
        {
            public void run()
            {
                long newTickTime = Calendar.getInstance().getTimeInMillis();
                if (tickTime == 0)
                {
                    tickTime = newTickTime;
                } else if (newTickTime > tickTime)
                {
                    ticker.setElapsedTime(newTickTime - tickTime);
                    SwingUtilities.invokeLater(ticker);
                    tickTime = newTickTime;
                }

            }
        }, 0, TIMER_DELAY);
    }


    public void addTickEventListener(ITickEventListenerIf listener)
    {
        tickListeners.add(listener);
    }

    public void removeTickEventListener(ITickEventListenerIf listener)
    {
        tickListeners.remove(listener);
    }

    /**
     * Count down the interval timer for each listern and fire tick events to all listeners where interval time has expired.
     *
     * @param aMilli Time in milliseconds since previous call.
     */
    protected void fireTick(long aMilli)
    {
        if (tickListeners.size() == 0 || aMilli == 0)
        {
            return;
        }

        for (ITickEventListenerIf listener : tickListeners)
        {
            long timer = listener.getTimeCounter() - aMilli;
            if (timer > 0)
            {
                listener.setTimeCounter(timer);
            } else
            {
                listener.handleTick(tickEvent);
                listener.setTimeCounter(listener.getInterval());
            }
        }
    }

    /**
     * Class that embeds a runnable that performs the GUI updates by firing the ticks to the listeners.
     *
     * The class is not thread-safe. The run() method is run with the latest given elapsed time.
     */
    class WpTicker implements Runnable
    {
        long m_elapsedTime;

        void setElapsedTime(long aTime)
        {
            m_elapsedTime = aTime;
        }

        public void run()
        {
            fireTick(m_elapsedTime);
        }
    }
    
    public boolean confirmDeactivate()
    {
    	return true;
    }

}
