package org.redcross.sar.wp;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.wp.A008.LogisticsPanel;

import javax.swing.*;
import java.awt.*;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 11.apr.2007
 * To change this template use File | Settings | File Templates.
 */

/**
 *
 */
public class DiskoWpUnitImpl extends AbstractDiskoWpModule implements IDiskoWpUnit
{

    private JButton m_personnelButton = null;
    private JButton m_unitButton = null;

    public DiskoWpUnitImpl(IDiskoRole role)
    {
        super(role);
        initialize();
    }

    private void initialize()
    {
        loadProperties("properties");
        defineSubMenu();

        JPanel lPanel=new JPanel();
        lPanel.setBackground(Color.BLUE);
        layoutComponent(lPanel);

//        DiskoMap map = getMap();
//        map.setIsEditable(true);
//        layoutComponent(map);
    }

    public DiskoMap getMap()
    {
        return null;
    }


    private void defineSubMenu()
    {
        layoutButton(getPersonnelButton());
        layoutButton(getUnitButton());
    }

    /* (non-Javadoc)
    * @see com.geodata.engine.disko.task.DiskoAp#getName()
    */
    public String getName()
    {
        return "Enhet";
    }

    /* (non-Javadoc)
     * @see com.geodata.engine.disko.task.DiskoAp#cancel()
     */
    public void cancel()
    {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see com.geodata.engine.disko.task.DiskoAp#finish()
     */
    public void finish()
    {
        // TODO Auto-generated method stub
    }

    public JButton getPersonnelButton()
    {
        IDiskoApplication app = getDiskoRole().getApplication();
        if (m_personnelButton == null)
        {
            try
            {
                m_personnelButton = createLargeButton("Ny person", new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                    }
                });
            }
            catch (java.lang.Throwable e)
            {
                // TODO: Something
            }
        }
        return m_personnelButton;
    }

    public JButton getUnitButton()
    {
        IDiskoApplication app = getDiskoRole().getApplication();
        if (m_unitButton == null)
        {
            try
            {
                m_unitButton = createLargeButton("Ny enhet", new java.awt.event.ActionListener()
                {
                    public void actionPerformed(java.awt.event.ActionEvent e)
                    {
                    }
                });
            }
            catch (java.lang.Throwable e)
            {
                // TODO: Something
            }
        }
        return m_unitButton;
    }

}
