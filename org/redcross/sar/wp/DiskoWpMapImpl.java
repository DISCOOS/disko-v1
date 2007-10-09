/**
 * 
 */
package org.redcross.sar.wp;

import java.util.EnumSet;
import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.map.DiskoMap;

/**
 * Implements the DiskoApKart interface
 * @author geira
 *
 */
public class DiskoWpMapImpl extends AbstractDiskoWpModule implements IDiskoWpMap {
	
	/**
	 * Constructs a DiskoApKartImpl
	 * @param rolle A reference to the DiskoRolle
	 */
	public DiskoWpMapImpl(IDiskoRole rolle) {
		super(rolle);
		initialize();
	}
	
	private void initialize() {
		DiskoMap map = (DiskoMap)getMap();
		layoutComponent(map);
	}
	
	public void activated() {
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
	}
	
	/* (non-Javadoc)
	 * @see com.geodata.engine.disko.task.DiskoAp#getName()
	 */
	public String getName() {
		return "Kart";
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

	public void reInitWP()
	{
		// TODO Auto-generated method stub
		
	}
}
