package org.redcross.sar.wp.logistics;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.wp.AbstractDiskoWpModule;

import com.esri.arcgis.interop.AutomationException;

import javax.swing.*;

import java.io.IOException;
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
public class DiskoWpLogisticsImpl extends AbstractDiskoWpModule implements IDiskoWpLogistics {

    public DiskoWpLogisticsImpl(IDiskoRole role) {
        super(role);
        initialize();
    }

    private void initialize() {
        loadProperties("properties");
        JPanel lPanel = new LogisticsPanel(this).getPanel();
        layoutComponent(lPanel);
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
        myInterests.add(NavBar.ToolCommandType.MAP_TOGGLE_COMMAND);
        navBar.showButtons(myInterests);
        try {
			getMap().partialRefresh(null);
		} catch (AutomationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    /* (non-Javadoc)
    * @see com.geodata.engine.disko.task.DiskoAp#getName()
    */
    public String getName() {
        return "Logistikk";
    }

    /* (non-Javadoc)
     * @see com.geodata.engine.disko.task.DiskoAp#cancel()
     */
    public void cancel() {
        // TODO Auto-generated method stub
    }

    /* (non-Javadoc)
     * @see com.geodata.engine.disko.task.DiskoAp#finish()
     */
    public void finish() {
        // TODO Auto-generated method stub
    }

}
