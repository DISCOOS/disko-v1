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
public class DiskoWpBriefingImpl extends AbstractDiskoWpModule implements IDiskoWpBriefing
{

    private JButton m_situationButton = null;

    public DiskoWpBriefingImpl(IDiskoRole role)
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
        layoutButton(getSituationButton());
    }

    /* (non-Javadoc)
    * @see com.geodata.engine.disko.task.DiskoAp#getName()
    */
    public String getName()
    {
        return "5PO";
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

    private JButton getSituationButton()
    {
        IDiskoApplication app = getDiskoRole().getApplication();
        if (m_situationButton == null)
        {
            try
            {
                m_situationButton = createLargeButton("Situasjon", new java.awt.event.ActionListener()
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
        return m_situationButton;
    }

	public void reInitWP()
	{
		// TODO Auto-generated method stub
		
	}
}
