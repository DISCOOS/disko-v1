/**
 * 
 */
package org.redcross.sar.wp;

import java.io.IOException;

import org.redcross.sar.app.IDiskoRole;
import org.redcross.sar.event.DiskoMapEvent;
import org.redcross.sar.gui.NavBar;
import org.redcross.sar.gui.UIFactory;
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
		DiskoMap map = getMap();
		layoutComponent(map);
	}
	
	public void activated() {
		UIFactory uiFactory = getDiskoRole().getApplication().getUIFactory();
		NavBar navBar = uiFactory.getMainPanel().getNavBar();
		int[] buttonIndexes = {
				NavBar.PUI_TOOL,
				NavBar.ZOOM_IN_TOOL,
				NavBar.ZOOM_OUT_TOOL,
				NavBar.PAN_TOOL,
				NavBar.ZOOM_IN_FIXED_COMMAND,
				NavBar.ZOOM_OUT_FIXED_COMMAND,
				NavBar.ZOOM_FULL_EXTENT_COMMAND,
				NavBar.ZOOM_TO_LAST_EXTENT_FORWARD_COMMAND,
				NavBar.ZOOM_TO_LAST_EXTENT_BACKWARD_COMMAND
		};
		navBar.showButtons(buttonIndexes);
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
}
