package org.redcross.sar.wp;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.Utils;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.event.DiskoWpEvent;
import org.redcross.sar.event.IDiskoMapEventListener;
import org.redcross.sar.event.IDiskoWpEventListener;
import org.redcross.sar.gui.MainPanel;
import org.redcross.sar.gui.SubMenuPanel;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.mso.IMsoManagerIf;
import org.redcross.sar.mso.IMsoModelIf;
import org.redcross.sar.mso.event.IMsoEventManagerIf;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

/**
 * This abstract class is a base class that has a default implementation of the
 * IDiskoWpModule interface and the IDiskoMapEventListener interface.
 *
 * @author geira
 */
public abstract class AbstractDiskoWpModule implements IDiskoWpModule, IDiskoMapEventListener
{
    private IDiskoRole role = null;
    private DiskoMap map = null;
    private boolean hasSubMenu = false;
    private Properties properties = null;
    private ArrayList<IDiskoWpEventListener> listeners = null;
    private DiskoWpEvent diskoWpEvent = null;

    /**
     * @param role
     */
    public AbstractDiskoWpModule(IDiskoRole role)
    {
        this.role = role;
    }

    public IDiskoRole getDiskoRole()
    {
        return role;
    }


    /* (non-Javadoc)
      * @see org.redcross.sar.wp.IDiskoWpModule#getMap()
      */
    public DiskoMap getMap()
    {
        if (map == null)
        {
            try
            {
                String mxdDoc = getProperty("MxdDocument.path");
                map = new DiskoMap(mxdDoc);
                map.addDiskoMapEventListener(this);
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return map;
    }


    /* (non-Javadoc)
      * @see org.redcross.sar.wp.IDiskoWpModule#getName()
      */
    public String getName()
    {
        return null;
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

    public String getProperty(String key)
    {
        String value = properties != null ?
                properties.getProperty(key) : null;
        if (value == null)
        {
            //try application properties
            value = role.getApplication().getProperty(key);
        }
        return value;
    }

    public void loadProperties(String fileName)
    {
        try
        {
            properties = Utils.getProperties("properties");
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

    protected void fireWpCanceled()
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            listeners.get(i).wpCanceled(diskoWpEvent);
        }
    }

    protected void fireWpFinished()
    {
        for (int i = 0; i < listeners.size(); i++)
        {
            listeners.get(i).wpFinished(diskoWpEvent);
        }
    }

    protected void layoutComponent(JComponent comp)
    {
        String id = role.getName() + getName();
        MainPanel mainPanel = role.getApplication().getUIFactory().getMainPanel();
        mainPanel.addComponent(comp, id);
    }

    protected void layoutButton(AbstractButton button)
    {
        layoutButton(button, true);
    }

    protected void layoutButton(AbstractButton button, boolean addToGroup)
    {
        String id = role.getName() + getName();
        SubMenuPanel subMenuPanel = role.getApplication().getUIFactory().getSubMenuPanel();
        subMenuPanel.addItem(button, id, addToGroup);
        hasSubMenu = true;
    }

    protected JButton createLargeButton(String aText, java.awt.event.ActionListener aListener)
    {
        return createButton(aText, role.getApplication().getUIFactory().getLargeButtonSize(), aListener);
    }

    protected JButton createSmallButton(String aText, java.awt.event.ActionListener aListener)
    {
        return createButton(aText, role.getApplication().getUIFactory().getSmallButtonSize(), aListener);
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

    // **** DiskoMapEventListeners *****

    public void editLayerChanged(DiskoMapEvent e)
    {
        // TODO Auto-generated method stub
    }

    public void onAfterScreenDraw(DiskoMapEvent e) throws IOException
    {
        // TODO Auto-generated method stub
    }

    public void onExtentUpdated(DiskoMapEvent e) throws IOException
    {
        // TODO Auto-generated method stub
    }

    public void onMapReplaced(DiskoMapEvent e) throws IOException
    {
        // TODO Auto-generated method stub
    }

    public void onSelectionChanged(DiskoMapEvent e) throws IOException
    {
        // TODO Auto-generated method stub
    }

    public IMsoModelIf getMsoModel()
    {
            return role.getApplication().getMsoModel();
    }

    public IMsoManagerIf getMsoManager()
    {
            return getMsoModel().getMsoManager();
    }

    public IMsoEventManagerIf getMmsoEventManager()
    {
            return getMsoModel().getEventManager();
    }

}
