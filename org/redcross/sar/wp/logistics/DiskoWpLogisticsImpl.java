package org.redcross.sar.wp.logistics;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.map.DiskoMap;
import org.redcross.sar.wp.AbstractDiskoWpModule;
import org.redcross.sar.wp.TestData.BuildTestData;

import javax.swing.*;
import java.awt.*;
import java.util.EnumSet;
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
    	super.activated();
		NavBar navBar = getApplication().getNavBar();
		EnumSet<NavBar.ToolCommandType> myInterests =
			EnumSet.of(NavBar.ToolCommandType.ZOOM_IN_TOOL);
		myInterests.add(NavBar.ToolCommandType.ZOOM_OUT_TOOL);
		myInterests.add(NavBar.ToolCommandType.PAN_TOOL);
		myInterests.add(NavBar.ToolCommandType.ZOOM_FULL_EXTENT_COMMAND);
		myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND);
		myInterests.add(NavBar.ToolCommandType.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND);
		navBar.showButtons(myInterests);
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
