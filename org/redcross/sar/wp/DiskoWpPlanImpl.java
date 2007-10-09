package org.redcross.sar.wp;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.app.IDiskoApplication;
import org.redcross.sar.map.DiskoMap;

import javax.swing.*;
/**
 * Created by IntelliJ IDEA.
 * User: vinjar
 * Date: 11.apr.2007
 */

/**
 *
 */
public class DiskoWpPlanImpl extends AbstractDiskoWpModule implements IDiskoWpPlan
{

    private JButton m_tidsplanButton = null;
    private JButton m_grovplanButton = null;

    public DiskoWpPlanImpl(IDiskoRole role)
    {
        super(role);
        initialize();
    }

    private void initialize()
    {
        loadProperties("properties");
        defineSubMenu();

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
        layoutButton(getTidsplanButton());
        layoutButton(getGrovplanButton());
    }

    /* (non-Javadoc)
    * @see com.geodata.engine.disko.task.DiskoAp#getName()
    */
    public String getName()
    {
        return "Plan";
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

    private JButton getTidsplanButton()
    {
        IDiskoApplication app = getDiskoRole().getApplication();
        if (m_tidsplanButton == null)
        {
            try
            {
                m_tidsplanButton = createLargeButton("Tidsplan", new java.awt.event.ActionListener()
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
        return m_tidsplanButton;
    }

    private JButton getGrovplanButton()
    {
        IDiskoApplication app = getDiskoRole().getApplication();
        if (m_grovplanButton == null)
        {
            try
            {
                m_grovplanButton = createLargeButton("Grovplan", new java.awt.event.ActionListener()
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
        return m_grovplanButton;
    }

	public void reInitWP()
	{
		// TODO Auto-generated method stub		
	}
}
