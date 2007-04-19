package org.redcross.sar.wp.A008;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.UIFactory;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.wp.AbstractDiskoWpModule;
import org.redcross.sar.wp.TestData.BuildTestData;
import org.redcross.sar.wp.A008.IDiskoWpLogistics;

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
public class DiskoWpLogisticsImpl extends AbstractDiskoWpModule implements IDiskoWpLogistics
{

    public DiskoWpLogisticsImpl(IDiskoRole role)
    {
        super(role);
        initialize();
    }

    private void initialize()
    {
        loadProperties("properties");

        BuildTestData.createCmdPost(getMsoModel());
        BuildTestData.createUnitsAndAssignments(getMsoModel());

        JPanel lPanel=new LogisticsPanel(this).getPanel();
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
    
    public void activated() {
    	UIFactory uiFactory = getDiskoRole().getApplication().getUIFactory();
		NavBar navBar = uiFactory.getMainPanel().getNavBar();
		int[] buttonIndexes = {
				//NavBar.INDEX_ERASE_TOGGLE_BUTTON,
				NavBar.INDEX_ZOOM_IN_TOGGLE_BUTTON,
				NavBar.INDEX_ZOOM_OUT_TOGGLE_BUTTON,
				NavBar.INDEX_PAN_TOGGLE_BUTTON,
				NavBar.INDEX_ZOOM_IN_FIXED_BUTTON,
				NavBar.INDEX_ZOOM_OUT_FIXED_BUTTON,
				NavBar.INDEX_ZOOM_FULL_EXTENT_BUTTON,
				NavBar.INDEX_ZOOM_TO_LAST_EXTENT_FORWARD_BUTTON,
				NavBar.INDEX_ZOOM_TO_LAST_EXTENT_BACKWARD_BUTTON
		};
		navBar.showButtons(buttonIndexes);
    }

    /* (non-Javadoc)
    * @see com.geodata.engine.disko.task.DiskoAp#getName()
    */
    public String getName()
    {
        return "Logistikk";
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

}
